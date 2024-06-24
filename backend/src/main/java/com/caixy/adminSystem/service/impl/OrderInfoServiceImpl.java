package com.caixy.adminSystem.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.annotation.FileUploadActionTarget;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.FileUploadActionException;
import com.caixy.adminSystem.mapper.OrderInfoMapper;
import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import com.caixy.adminSystem.model.dto.file.UploadFileInfoDTO;
import com.caixy.adminSystem.model.dto.file.UploadFileRequest;
import com.caixy.adminSystem.model.dto.order.OrderInfoQueryRequest;
import com.caixy.adminSystem.model.entity.*;
import com.caixy.adminSystem.model.enums.*;
import com.caixy.adminSystem.model.vo.file.OrderFileVO;
import com.caixy.adminSystem.model.vo.order.EventVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoPageVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.utils.RedisUtils;
import com.caixy.adminSystem.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_info(订单信息)】的数据库操作Service实现
 * @createDate 2024-06-04 20:50:04
 */
@Service
@FileUploadActionTarget(FileUploadBizEnum.ORDER_ATTACHMENT)
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService, FileUploadActionService
{
    private static final Integer MAX_DESC_SIZE = 1024;

    private static final Integer MAX_TITLE_SIZE = 30;

    private static final byte[] TOKEN_SIGN = "orderAttachment_syu".getBytes();

    // token过期时间: 10分钟
    private static final Long TOKEN_EXPIRE_TIME = RedisConstant.UPLOAD_FILE_KEY.getExpire();

    @Resource
    private LanguageTypeService languageTypeService;

    @Resource
    private OrderCategoryService orderCategoryService;

    @Resource
    private UserService userService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private OrderFileInfoService orderFileInfoService;


    private OrderValidator orderValidator;

    @PostConstruct
    public void init()
    {
        orderValidator = new OrderValidator(languageTypeService, orderCategoryService);
    }


    @Override
    public void validOrderInfo(OrderInfo post, boolean add)
    {
        if (add)
        {
            orderValidator.validateAllFields(post);
        }
        else
        {
            orderValidator.validateNonNullFields(post);
        }
    }

    @Override
    public QueryWrapper<OrderInfo> getQueryWrapper(OrderInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public Page<OrderInfo> searchFromEs(OrderInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public OrderInfoVO getOrderInfoVO(OrderInfo post, HttpServletRequest request)
    {
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        BeanUtils.copyProperties(post, orderInfoVO);
        // 获取用户名
        Long creatorId = post.getCreatorId();
        Optional<User> creatorInfo = Optional.ofNullable(userService.getById(creatorId));
        if (creatorInfo.isPresent())
        {
            String creatorName = creatorInfo.get().getUserName();
            orderInfoVO.setCreatorName(creatorName);
        }
        else
        {
            orderInfoVO.setCreatorName("未知用户");
        }

        // 获取语言名
        Long orderLangId = post.getOrderLangId();
        Optional<LanguageType> langInfo = Optional.ofNullable(languageTypeService.getById(orderLangId));
        if (langInfo.isPresent())
        {
            String langName = langInfo.get().getLanguageName();
            orderInfoVO.setLangName(langName);
        }
        else
        {
            orderInfoVO.setLangName("未知语言");
        }

        // 获取分类名
        Long orderCategoryId = post.getOrderCategoryId();
        Optional<OrderCategory> categoryInfo = Optional.ofNullable(orderCategoryService.getById(orderCategoryId));
        if (categoryInfo.isPresent())
        {
            String categoryName = categoryInfo.get().getCategoryName();
            orderInfoVO.setCategoryName(categoryName);
        }
        else
        {
            orderInfoVO.setCategoryName("未知分类");
        }

        // 获取订单附件信息
        List<OrderFileVO> orderFileInfoList = orderFileInfoService.getOrderFileInfoList(post.getId());
        orderInfoVO.setOrderAttachmentList(orderFileInfoList);
        return orderInfoVO;
    }

    @Override
    public Page<OrderInfoPageVO> getOrderInfoVOPage(Page<OrderInfo> postPage, HttpServletRequest request)
    {
        Page<OrderInfoPageVO> orderInfoVOPage = new Page<>(postPage.getCurrent(), postPage.getSize());
        List<OrderInfo> orderInfos = postPage.getRecords();
        Set<Long> userIds = new HashSet<>();
        Set<Long> langIds = new HashSet<>();
        Set<Long> categoryIds = new HashSet<>();

        orderInfos.forEach(item -> {
            userIds.add(item.getCreatorId());
            langIds.add(item.getOrderLangId());
            categoryIds.add(item.getOrderCategoryId());
        });
        if (userIds.isEmpty() || langIds.isEmpty() || categoryIds.isEmpty())
        {
            orderInfoVOPage.setRecords(Collections.emptyList());
            orderInfoVOPage.setTotal(0);
            return orderInfoVOPage;
        }

        // todo: 性能优化，这里可以将lang和分类等长期不更新的数据放入缓存，降低数据库使用率，或是创建ES宽表
        Map<Long, String> userNameByIds = userService.getUserNameByIds(userIds);
        Map<Long, String> langNameByIds = languageTypeService.getLangNameByIds(langIds);
        Map<Long, String> categoryNameByIds = orderCategoryService.getCategoryNameByIds(categoryIds);

        List<OrderInfoPageVO> orderInfoPageVOList = orderInfos.stream().map(item -> {
            OrderInfoPageVO orderInfoPageVO = new OrderInfoPageVO();
            BeanUtils.copyProperties(item, orderInfoPageVO);
            OrderStatusEnum orderStatus = OrderStatusEnum.getByCode(item.getOrderStatus());
            OrderSourceEnum orderSource = OrderSourceEnum.getByCode(item.getOrderSource());

            orderInfoPageVO.setIsAssigned(integerToBool(item.getIsAssigned()));
            orderInfoPageVO.setIsPaid(integerToBool(item.getIsPaid()));
            orderInfoPageVO.setHasOrderAttachment(integerToBool(item.getOrderAttachmentNum()));
            orderInfoPageVO.setOrderStatus(orderStatus == null ? "未知状态" : orderStatus.getDesc());
            orderInfoPageVO.setOrderSource(orderSource == null ? "未知来源" : orderSource.getDesc());
            orderInfoPageVO.setCreatorName(userNameByIds.get(item.getCreatorId()) == null ? "未知用户" :
                                           userNameByIds.get(item.getCreatorId()));
            orderInfoPageVO.setLangName(langNameByIds.get(item.getOrderLangId()) == null ? "未知语言" :
                                        langNameByIds.get(item.getOrderLangId()));
            orderInfoPageVO.setOrderCategoryName(categoryNameByIds.get(item.getOrderCategoryId()) == null ? "未知分类" :
                                                 categoryNameByIds.get(item.getOrderCategoryId()));
            return orderInfoPageVO;
        }).collect(Collectors.toList());
        orderInfoVOPage.setRecords(orderInfoPageVOList);
        orderInfoVOPage.setTotal(postPage.getTotal());
        return orderInfoVOPage;
    }


    private Boolean integerToBool(Integer value)
    {
        return value != null && value >= 0 && value <= 1;
    }

    @Override
    public Map<String, String> generateFileUploadToken(List<UploadFileInfoDTO> fileInfoList,
                                                       Long orderId)
    {
        Map<String, String> tokenMap = new HashMap<>();

        for (UploadFileInfoDTO fileInfo : fileInfoList)
        {
            String uuid = UUID.randomUUID().toString();
            Map<String, Object> payload = new HashMap<>();
            payload.put("fileUid", fileInfo.getFileUid());
            payload.put("fileName", fileInfo.getFileName());
            payload.put("fileSha256", fileInfo.getFileSha256());
            payload.put("orderId", orderId);
            payload.put("uuid", uuid);

            String token = generateToken(payload);
            fileInfo.setToken(token);
            // 设置文件信息进入redis，对应的key为：upload_file_key:orderId:uuid
            redisUtils.setHashMap(
                    RedisConstant.UPLOAD_FILE_KEY,
                    payload,
                    orderId.toString(),
                    uuid
            );
            tokenMap.put(fileInfo.getFileUid(), token);
        }
        return tokenMap;
    }

    @Override
    public Long countUploadOrderAttachment(Long orderId)
    {
        QueryWrapper<OrderFileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderId", orderId);
        return orderFileInfoService.count(queryWrapper);
    }
    @Cacheable(value = "events", key = "#userId + ':' + #year + ':' + #month")
    @Override
    public List<EventVO<OrderInfoVO>> getEvents(Integer year, Integer month, Long userId)
    {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        // 将 LocalDate 转换为 Date
        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("orderDeadline", startDate, endDate);
        queryWrapper.eq("creatorId", userId);
        List<OrderInfo> orderInfoList = this.list(queryWrapper);

        return orderInfoList.isEmpty() ? Collections.emptyList() :
               orderInfoList.stream()
                       .map(this::convertEventOrderInfo)
                       .collect(Collectors.toList());
    }

    public EventVO<OrderInfoVO> convertEventOrderInfo(OrderInfo item)
    {
        EventVO<OrderInfoVO> eventVO = new EventVO<>();
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        BeanUtils.copyProperties(item, orderInfoVO);

        LocalDate today = LocalDate.now();
        LocalDate deadline = item.getOrderDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysUntilDeadline = today.until(deadline, ChronoUnit.DAYS);

        eventVO.setId(item.getId());
        eventVO.setContent(orderInfoVO);
        eventVO.setDate(Date.from(deadline.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        eventVO.setLevel(UrgencyLevelEnum.getUrgency(daysUntilDeadline).getLevel());
        return eventVO;
    }

    public Map<String, Object> getTokenPayload(String token)
    {
        try
        {   // 解析 JWT 并获取 payload
            JWT jwt = JWTUtil.parseToken(token);
            if (jwt.setKey(TOKEN_SIGN).verify())
            {
                return jwt.getPayload().getClaimsJson();
            }
            else
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Token 验证失败");
            }
        }
        catch (JWTException e)
        {
            // 处理校验失败
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Token 无效或已过期");
        }
    }

    private String generateToken(Map<String, Object> params)
    {
        JWT jWtCreator = JWT.create();
        params.forEach(jWtCreator::setPayload);
        jWtCreator.setKey(TOKEN_SIGN);
        jWtCreator.setExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_TIME * 1000));
        return jWtCreator.sign();
    }

    @Override
    public Boolean doAfterUploadAction(UploadFileConfig uploadFileConfig, String savePath, UploadFileRequest uploadFileRequest)
    {
        String token = uploadFileRequest.getToken();
        Map<String, Object> payload = getTokenPayload(token);
        Long orderId = Long.parseLong(payload.get("orderId").toString());
        OrderFileInfo orderFileInfo = new OrderFileInfo();
        orderFileInfo.setOrderId(orderId);
        orderFileInfo.setUserId(uploadFileConfig.getUserId());
        orderFileInfo.setFileSize(uploadFileConfig.getFileSize());
        orderFileInfo.setFileSha256(uploadFileConfig.getSha256());
        orderFileInfo.setFileName(uploadFileConfig.getFileInfo().getFileInnerName());
        orderFileInfo.setFileRealName(uploadFileConfig.getFileInfo().getFileRealName());
        orderFileInfo.setFileSuffix(uploadFileConfig.getFileInfo().getFileSuffix());
        boolean save = orderFileInfoService.save(orderFileInfo);
        if (!save)
        {
            removeById(orderId);
            throw new FileUploadActionException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
        log.info("文件上传成功，文件信息已保存, 订单ID: {}，文件UUID: {}", orderId, uploadFileConfig.getFileInfo().getUuid());
        return true;
    }

    /**
     * 删除订单，包括订单文件信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/20 下午9:46
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOrderInfo(OrderInfo orderInfo)
    {
        boolean removeOrder = this.removeById(orderInfo.getId());
        if (removeOrder)
        {
            return orderFileInfoService.removeOrderFileInfo(orderInfo);
        }
        else
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除订单失败");
        }
    }


    @Override
    public Boolean doBeforeUploadAction(UploadFileConfig uploadFileConfig,
                                        UploadFileRequest uploadFileRequest)
    {
        String token = uploadFileRequest.getToken();
        if (token == null || StringUtils.isBlank(token))
        {
            throw new FileUploadActionException(ErrorCode.PARAMS_ERROR, "Token 不存在");
        }
        String uploadFileSha256 = uploadFileConfig.getSha256();

        Map<String, Object> payload = getTokenPayload(token);
        String orderId = payload.get("orderId").toString();
        String uuid = payload.get("uuid").toString();
        Map<Object, Object> cacheData = redisUtils.getHashMap(RedisConstant.UPLOAD_FILE_KEY, orderId, uuid);
        // 如果缓存里没有token信息
        if (cacheData == null || cacheData.isEmpty())
        {
            removeById(Long.parseLong(orderId));
            throw new FileUploadActionException(ErrorCode.PARAMS_ERROR, "文件上传超时或校验失败");
        }
        String cacheFileSha256 = cacheData.get("fileSha256").toString().replaceAll("\"", "");
        String cacheUuid = cacheData.get("uuid").toString().replaceAll("\"", "");
        if (!cacheFileSha256.equals(uploadFileSha256) ||
                !uuid.equals(cacheUuid))
        {
            removeById(Long.parseLong(orderId));
            throw new FileUploadActionException(ErrorCode.PARAMS_ERROR, "文件校验失败");
        }
        // 删除redis中的缓存数据
        redisUtils.delete(RedisConstant.UPLOAD_FILE_KEY, orderId, uuid);
        return true;
    }

    @Override
    public void setOrderValid(Long orderId, boolean validCode)
    {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setIsValid(validCode ? 1 : 0);
        this.updateById(orderInfo);
    }



}

class OrderValidator
{

    private static final int MAX_DESC_SIZE = 255;
    private static final int MAX_TITLE_SIZE = 50;

    private final LanguageTypeService languageTypeService;
    private final OrderCategoryService orderCategoryService;

    // 构造函数注入需要的服务
    public OrderValidator(LanguageTypeService languageTypeService, OrderCategoryService orderCategoryService)
    {
        this.languageTypeService = languageTypeService;
        this.orderCategoryService = orderCategoryService;
    }

    // 初始化验证映射
    private final Map<String, Consumer<OrderInfo>> validationMap = createValidationMap();

    private Map<String, Consumer<OrderInfo>> createValidationMap()
    {
        Map<String, Consumer<OrderInfo>> map = new HashMap<>();
        map.put("customerEmail", this::validateEmail);
        map.put("orderStatus", this::validateOrderStatus);
        map.put("orderSource", this::validateOrderSource);
        map.put("paymentMethod", this::validatePaymentMethod);
        map.put("customerContactType", this::validateCustomerContactType);
        map.put("isAssigned", this::validateIsAssigned);
        map.put("orderDesc", this::validateOrderDesc);
        map.put("orderRemark", this::validateOrderRemark);
        map.put("orderTitle", this::validateOrderTitle);
        map.put("orderDeadline", this::validateOrderDeadline);
        map.put("amount", this::validateAmount);
        map.put("amountPaid", this::validateAmountPaid);
        map.put("orderLangId", this::validateOrderLangId);
        map.put("orderCategoryId", this::validateOrderCategoryId);
        map.put("orderCompletionTime", this::validateOrderCompletionTime);
        return map;
    }


    public void validateAllFields(OrderInfo post)
    {
        validationMap.values().forEach(validator -> validator.accept(post));
    }

    public void validateNonNullFields(OrderInfo post)
    {
        validationMap.forEach((field, validator) -> {
            try
            {
                Field declaredField = OrderInfo.class.getDeclaredField(field);
                declaredField.setAccessible(true);
                Object value = declaredField.get(post);
                if (value != null)
                {
                    validator.accept(post);
                }
            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new RuntimeException("Field access error: " + field, e);
            }
        });
    }


    // 示例的验证函数
    private void validateEmail(OrderInfo post)
    {
        String email = post.getCustomerEmail();
        if (email != null && !RegexUtils.isEmail(email))
        {
            throw new IllegalArgumentException("邮箱格式错误");
        }
    }

    private void validateOrderStatus(OrderInfo post)
    {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getByCode(post.getOrderStatus());
        if (orderStatusEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单状态不正确");
        }
    }

    private void validateOrderSource(OrderInfo post)
    {
        OrderSourceEnum orderSourceEnum = OrderSourceEnum.getByCode(post.getOrderSource());
        if (orderSourceEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单来源不正确");
        }
    }

    private void validatePaymentMethod(OrderInfo post)
    {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(post.getPaymentMethod());
        if (paymentMethodEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "支付方式不正确");
        }
    }

    private void validateCustomerContactType(OrderInfo post)
    {
        ContactTypeEnum contactTypeEnum = ContactTypeEnum.getEnumByValue(post.getCustomerContactType());
        if (contactTypeEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "联系方式不正确");
        }
        if (contactTypeEnum == ContactTypeEnum.EMAIL)
        {
            if (!RegexUtils.isEmail(post.getCustomerContact()))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
            }
        }
        else if (contactTypeEnum == ContactTypeEnum.PHONE)
        {
            if (!RegexUtils.isMobilePhone(post.getCustomerContact()))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
            }
        }
    }

    private void validateIsAssigned(OrderInfo post)
    {
        Integer isAssigned = post.getIsAssigned();
        if (!integerToBool(isAssigned))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (isAssigned == 1)
        {
            Integer commissionRate = post.getOrderCommissionRate();
            if (commissionRate == null || commissionRate < 0 || commissionRate > 100)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "佣金比例不正确");
            }
        }
    }

    private void validateOrderDesc(OrderInfo post)
    {
        String description = post.getOrderDesc();
        if (description != null && description.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单描述过长，不得超过: " + MAX_DESC_SIZE + "个字符");
        }
    }

    private void validateOrderRemark(OrderInfo post)
    {
        String remark = post.getOrderRemark();
        if (remark != null && remark.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单备注过长，不得超过: " + MAX_DESC_SIZE + "个字符");
        }
    }

    private void validateOrderTitle(OrderInfo post)
    {
        String title = post.getOrderTitle();
        if (title != null && title.length() > MAX_TITLE_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单标题过长，不得超过: " + MAX_TITLE_SIZE + "个字符");
        }
    }

    private void validateOrderDeadline(OrderInfo post)
    {
        Date deadlineTime = post.getOrderDeadline();
        if (deadlineTime == null || deadlineTime.before(new Date()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单截止时间不能早于当前时间");
        }
    }

    private void validateAmount(OrderInfo post)
    {
        BigDecimal amount = post.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单金额必须大于0");
        }
    }

    private void validateAmountPaid(OrderInfo post)
    {
        BigDecimal paidAmount = post.getAmountPaid();
        if (paidAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单已支付金额必须大于0");
        }
    }

    private void validateOrderLangId(OrderInfo post)
    {
        Long orderLang = post.getOrderLangId();
        Boolean isExistById = languageTypeService.checkLangIsExistById(orderLang);
        if (!isExistById)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单语言不合法");
        }
    }

    private void validateOrderCategoryId(OrderInfo post)
    {
        Long orderCategory = post.getOrderCategoryId();
        Boolean isExistByCategoryId = orderCategoryService.checkOrderCategoryExistById(orderCategory);
        if (!isExistByCategoryId)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单分类不合法");
        }
    }

    private void validateOrderCompletionTime(OrderInfo post)
    {
        Date completionTime = post.getOrderCompletionTime();
        if (Objects.equals(post.getOrderStatus(), OrderStatusEnum.FINISHED.getCode()) && completionTime == null || completionTime.before(new Date()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单完成时间不合法");
        }
    }


    private static boolean integerToBool(Integer value)
    {
        return value != null && (value == 0 || value == 1);
    }
}