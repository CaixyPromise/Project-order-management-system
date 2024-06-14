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
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.OrderInfoMapper;
import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import com.caixy.adminSystem.model.dto.file.UploadFileInfoDTO;
import com.caixy.adminSystem.model.dto.file.UploadFileRequest;
import com.caixy.adminSystem.model.dto.order.OrderInfoQueryRequest;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.*;
import com.caixy.adminSystem.model.vo.order.OrderInfoPageVO;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.utils.RedisUtils;
import com.caixy.adminSystem.utils.RegexUtils;
import com.caixy.adminSystem.utils.RocketMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
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




    @Override
    public void validOrderInfo(OrderInfo post, boolean add)
    {
        // 1. 检查邮箱
        String email = post.getCustomerEmail();
        // 如果邮箱非空，校验邮箱
        if (email != null && !RegexUtils.isEmail(email))
        {
            throw new IllegalArgumentException("邮箱格式错误");
        }
        // 2. 校验枚举类型
        // 2.1 订单状态
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getByCode(post.getOrderStatus());
        ThrowUtils.throwIf(orderStatusEnum == null, ErrorCode.PARAMS_ERROR, "订单状态不正确");
        // 2.2 订单来源
        OrderSourceEnum orderSourceEnum = OrderSourceEnum.getByCode(post.getOrderSource());
        ThrowUtils.throwIf(orderSourceEnum == null, ErrorCode.PARAMS_ERROR, "订单来源不正确");
        // 2.3 支付方式
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(post.getPaymentMethod());
        ThrowUtils.throwIf(paymentMethodEnum == null, ErrorCode.PARAMS_ERROR, "支付方式不正确");
        // 2.4 联系方式
        ContactTypeEnum contactTypeEnum = ContactTypeEnum.getEnumByValue(post.getCustomerContactType());
        ThrowUtils.throwIf(contactTypeEnum == null, ErrorCode.PARAMS_ERROR, "联系方式不正确");
        if (contactTypeEnum == ContactTypeEnum.EMAIL)
        {
            ThrowUtils.throwIf(!RegexUtils.isEmail(post.getCustomerContact()), ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        else if (contactTypeEnum == ContactTypeEnum.PHONE)
        {
            ThrowUtils.throwIf(!RegexUtils.isMobilePhone(post.getCustomerContact()), ErrorCode.PARAMS_ERROR, "手机号格式错误");
        }
        // 检查boolean类型
        Integer isAssigned = post.getIsAssigned();
        ThrowUtils.throwIf(!integerToBool(isAssigned), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!integerToBool(post.getIsPaid()), ErrorCode.PARAMS_ERROR);
        // 检查比例，如果是分配的需要进行校验比例
        if (isAssigned == 1)
        {
            Integer commissionRate = post.getOrderCommissionRate();
            ThrowUtils.throwIf(commissionRate == null || commissionRate < 0 || commissionRate > 100, ErrorCode.PARAMS_ERROR, "佣金比例不正确");
        }

        // 校验描述
        String description = post.getOrderDesc();
        if (description != null && description.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单描述过长，不得超过: " + MAX_DESC_SIZE + "个字符");
        }
        // 校验备注
        String remark = post.getOrderRemark();
        if (remark != null && remark.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单备注过长，不得超过: " + MAX_DESC_SIZE + "个字符");
        }
        // 校验标题
        String title = post.getOrderTitle();
        if (title != null && title.length() > MAX_TITLE_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单标题过长，不得超过: " + MAX_TITLE_SIZE + "个字符");
        }

        // 校验时间
        // 截止时间
        Date deadlineTime = post.getOrderDeadline();
        ThrowUtils.throwIf(deadlineTime == null || deadlineTime.before(new Date()),
                ErrorCode.PARAMS_ERROR, "订单截止时间不能早于当前时间");

        // 校验价格
        BigDecimal amount = post.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单金额必须大于0");
        }
        // 校验已支付金额
        BigDecimal paidAmount = post.getAmountPaid();
        if (paidAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单已支付金额必须大于0");
        }

        // 校验语言是否合法
        Long orderLang = post.getOrderLangId();
        Boolean isExistById = languageTypeService.checkLangIsExistById(orderLang);
        if (!isExistById)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单语言不合法");
        }
        // 校验分类是否合法
        Long orderCategory = post.getOrderCategoryId();
        Boolean isExistByCategoryId = orderCategoryService.checkOrderCategoryExistById(orderCategory);
        if (!isExistByCategoryId)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单分类不合法");
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
    public OrderInfoPageVO getOrderInfoVO(OrderInfo post, HttpServletRequest request)
    {
        return null;
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

        // todo: 性能优化，这里可以将lang和分类等长期不更新的数据放入缓存，降低数据库使用率
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
        orderFileInfo.setFileName(uploadFileConfig.getFileInfo().getFilename());
        boolean save = orderFileInfoService.save(orderFileInfo);
        if (!save)
        {
            removeById(orderId);
            throw new FileUploadActionException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
        return true;
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
        Map<Object, Object> cacheData = redisUtils.getHash(RedisConstant.UPLOAD_FILE_KEY, orderId, uuid);
        if (cacheData == null || cacheData.isEmpty())
        {
            removeById(Long.parseLong(orderId));
            throw new FileUploadActionException(ErrorCode.PARAMS_ERROR, "文件上传超时");
        }
        String cacheFileSha256 = cacheData.get("fileSha256").toString().replaceAll("\"", "");
        String cacheUuid = cacheData.get("uuid").toString().replaceAll("\"", "");
        log.info("payload: {}", payload);
        log.info("cacheData: {}", cacheData);
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




