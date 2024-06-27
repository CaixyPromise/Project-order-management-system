package com.caixy.adminSystem.model.dto.order;

import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.OrderSourceEnum;
import com.caixy.adminSystem.model.enums.OrderStatusEnum;
import com.caixy.adminSystem.model.enums.PaymentMethodEnum;
import com.caixy.adminSystem.model.vo.order.OrderInfoPageVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;
import com.caixy.adminSystem.utils.CommonUtils;
import com.caixy.adminSystem.utils.JsonUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 帖子 ES 包装类
 **/
@Document(indexName = "order")
@Data
public class OrderInfoEsDTO implements Serializable
{


    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 创建人id
     */
    private Long creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 订单号
     */
    @Field(type = FieldType.Keyword)
    private String orderId;

    /**
     * 订单名称描述
     */
    @Field(type = FieldType.Text)
    private String orderTitle;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 已支付金额
     */
    @Field(type = FieldType.Double)
    private BigDecimal amountPaid;

    /**
     * 订单联系方式类型
     */
    private Integer customerContactType;

    /**
     * 订单联系方式
     */
    private String customerContact;

    /**
     * 顾客邮箱
     */
    private String customerEmail;

    /**
     * 是否是对外分配
     */
    @Field(type = FieldType.Boolean)
    private Boolean isAssigned;

    /**
     * 是否支付
     */
    @Field(type = FieldType.Boolean)
    private Boolean isPaid;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付方式代码
     */
    private Integer paymentMethodCode;


    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 订单来源码
     */
    private Integer orderSourceCode;

    /**
     * 订单分配人微信Id
     */
    private String orderAssignToWxId;

    /**
     * 订单佣金比例
     */
    private Integer orderCommissionRate;

    /**
     * 订单分类
     */
    private String categoryName;

    /**
     * 分类id
     */
    @Field(type = FieldType.Keyword)
    private Long orderCategoryId;

    /**
     * 订单标签
     */
    private List<String> orderTags;

    /**
     * 订单编程语言名称
     */
    private String langName;

    /**
     * 订单语言id
     */
    @Field(type = FieldType.Keyword)
    private Long orderLangId;

    /**
     * 订单描述
     */
    @Field(type = FieldType.Text)
    private String orderDesc;

    /**
     * 是否包含订单附件
     */
    private Integer orderAttachmentNum;

    /**
     * 订单备注
     */
    @Field(type = FieldType.Text)
    private String orderRemark;

    /**
     * 交付截止日期
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderDeadline;

    /**
     * 订单完成时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderCompletionTime;

    /**
     * 订单开始日期
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderStartDate;

    /**
     * 订单结束日期
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderEndDate;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单状态状态码
     */
    private Integer orderStatusCode;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isValid;

    /**
     * 订单附件列表
     */
    private List<OrderFileInfo> orderAttachmentList;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 转成PageVO
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/26 下午8:59
     */
    public static OrderInfoPageVO ofPageVO(OrderInfoEsDTO dto)
    {
        OrderInfoPageVO vo = new OrderInfoPageVO();
        BeanUtils.copyProperties(dto, vo);
        vo.setHasOrderAttachment(CommonUtils.isPresent(dto.getOrderAttachmentNum(), 0) > 0);
        return vo;
    }

    public static OrderInfoEsDTO objToDTO(OrderInfo orderInfo,
                                          Map<Long, String> userNameByIds,
                                          Map<Long, String> langNameByIds,
                                          Map<Long, String> categoryNameByIds,
                                          Map<Long, List<OrderFileInfo>> orderFileInfoList)
    {
        OrderInfoEsDTO orderInfoEsDTO = new OrderInfoEsDTO();
        BeanUtils.copyProperties(orderInfo, orderInfoEsDTO);
        List<OrderFileInfo> fileInfoList = orderFileInfoList.get(orderInfo.getId());
        String userName = userNameByIds.get(orderInfo.getCreatorId());
        String langName = langNameByIds.get(orderInfo.getOrderLangId());
        String categoryName = categoryNameByIds.get(orderInfo.getOrderCategoryId());

        OrderStatusEnum orderStatus = OrderStatusEnum.getByCode(orderInfo.getOrderStatus());
        OrderSourceEnum orderSource = OrderSourceEnum.getByCode(orderInfo.getOrderSource());
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(orderInfo.getPaymentMethod());
        orderInfoEsDTO.setIsAssigned(CommonUtils.integerToBool(orderInfo.getIsAssigned()));
        orderInfoEsDTO.setIsPaid(CommonUtils.integerToBool(orderInfo.getIsPaid()));
        orderInfoEsDTO.setOrderTags(JsonUtils.jsonToList(orderInfo.getOrderTags()));
        // 设置源数据
        orderInfoEsDTO.setOrderStatusCode(orderInfo.getOrderStatus());
        orderInfoEsDTO.setOrderSourceCode(orderInfo.getOrderSource());

        orderInfoEsDTO.setOrderAttachmentList(Optional.ofNullable(fileInfoList).orElse(Collections.emptyList()));
        orderInfoEsDTO.setOrderStatus(CommonUtils.isPresent(orderStatus, "未知状态", OrderStatusEnum::getDesc));
        orderInfoEsDTO.setOrderSource(CommonUtils.isPresent(orderSource, "未知来源", OrderSourceEnum::getDesc));
        orderInfoEsDTO.setPaymentMethod(CommonUtils.isPresent(paymentMethodEnum, "未知支付方式", PaymentMethodEnum::getDesc));
        orderInfoEsDTO.setCreatorName(CommonUtils.isPresent(userName, "未知用户"));
        orderInfoEsDTO.setLangName(CommonUtils.isPresent(langName, "未知语言"));
        orderInfoEsDTO.setCategoryName(CommonUtils.isPresent(categoryName, "未知分类"));
        return orderInfoEsDTO;
    }

    /**
     * 转成InfoVO
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/26 下午8:58
     */
    public static OrderInfoVO ofVO(OrderInfoEsDTO dto)
    {
        OrderInfoVO vo = new OrderInfoVO();
        BeanUtils.copyProperties(dto, vo);
        vo.setOrderStatus(dto.getOrderStatusCode());
        vo.setOrderSource(dto.getOrderSourceCode());
        vo.setPaymentMethod(dto.getPaymentMethod());
        vo.setPaymentMethodCode(dto.getPaymentMethodCode());
        vo.setOrderTags(JsonUtils.toJsonString(dto.getOrderTags()));
        return vo;
    }

    public static OrderInfoEsDTO from(OrderInfo orderInfo)
    {
        OrderInfoEsDTO dto = new OrderInfoEsDTO();
        BeanUtils.copyProperties(orderInfo, dto);
        dto.setOrderStatusCode(orderInfo.getOrderStatus());
        dto.setOrderSourceCode(orderInfo.getOrderSource());
        dto.setPaymentMethodCode(orderInfo.getPaymentMethod());
        return dto;
    }

}
