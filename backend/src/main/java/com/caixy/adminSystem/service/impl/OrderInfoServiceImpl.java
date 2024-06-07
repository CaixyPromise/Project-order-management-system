package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.OrderInfoMapper;
import com.caixy.adminSystem.model.dto.order.OrderInfoQueryRequest;
import com.caixy.adminSystem.model.entity.LanguageType;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.ContactTypeEnum;
import com.caixy.adminSystem.model.enums.OrderSourceEnum;
import com.caixy.adminSystem.model.enums.OrderStatusEnum;
import com.caixy.adminSystem.model.enums.PaymentMethodEnum;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;
import com.caixy.adminSystem.service.LanguageTypeService;
import com.caixy.adminSystem.service.OrderCategoryService;
import com.caixy.adminSystem.service.OrderInfoService;
import com.caixy.adminSystem.utils.RegexUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_info(订单信息)】的数据库操作Service实现
 * @createDate 2024-06-04 20:50:04
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService
{
    private static final Integer MAX_DESC_SIZE = 1024;

    @Resource
    private LanguageTypeService languageTypeService;

    @Resource
    private OrderCategoryService orderCategoryService;


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
        ThrowUtils.throwIf(!isBoolByInteger(post.getIsAssigned()), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!isBoolByInteger(post.getIsPaid()), ErrorCode.PARAMS_ERROR);
        // 检查比例
        Integer commissionRate = post.getOrderCommissionRate();
        ThrowUtils.throwIf(commissionRate == null || commissionRate < 0 || commissionRate > 100, ErrorCode.PARAMS_ERROR, "佣金比例不正确");


        // 校验描述
        String description = post.getOrderDesc();
        if (description != null && description.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单描述过长");
        }
        // 校验备注
        String remark = post.getOrderRemark();
        if (remark != null && remark.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单备注过长");
        }

        // 校验时间
        // 截止时间
        Date deadlineTime = post.getOrderDeadline();
        if (deadlineTime == null || deadlineTime.before(new Date()))
        {
            throw new IllegalArgumentException("订单截止时间不能早于当前时间");
        }

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
        Long orderLang = post.getOrderLang();
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
    public OrderInfoVO getOrderInfoVO(OrderInfo post, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public Page<OrderInfoVO> getOrderInfoVOPage(Page<OrderInfo> postPage, HttpServletRequest request)
    {
        return null;
    }


    private Boolean isBoolByInteger(Integer value)
    {
        return value != null && value != 1 && value != 0;
    }
}




