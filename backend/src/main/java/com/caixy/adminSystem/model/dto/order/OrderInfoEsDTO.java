package com.caixy.adminSystem.model.dto.order;

import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.OrderSourceEnum;
import com.caixy.adminSystem.model.enums.OrderStatusEnum;
import com.caixy.adminSystem.model.enums.PaymentMethodEnum;
import com.caixy.adminSystem.utils.CommonUtils;
import com.caixy.adminSystem.utils.JsonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单 ES 包装类
 **/
@Document(indexName = "order")
@Data
@NoArgsConstructor
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
    @Field(type = FieldType.Keyword)
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
    @Field(type = FieldType.Double, index = false)
    private BigDecimal amountPaid;

    /**
     * 订单联系方式类型
     */
    private Integer customerContactType;

    /**
     * 订单联系方式类型文本
     */
    @Field(type = FieldType.Text, index = false)
    private String customerContactTypeText;

    /**
     * 订单联系方式
     */
    @Field(type = FieldType.Keyword)
    private String customerContact;

    /**
     * 顾客邮箱
     */
    @Field(type = FieldType.Keyword)
    private String customerEmail;

    /**
     * 是否是对外分配
     */
    @Field(type = FieldType.Boolean, index = false)
    private Boolean isAssignedValue;

    /**
     * 是否是对外分配-状态值
     */
    private Integer isAssigned;

    /**
     * 是否支付
     */
    @Field(type = FieldType.Boolean, index = false)
    private Boolean isPaidValue;

    /**
     * 是否支付状态值
     */
    private Integer isPaid;

    /**
     * 支付方式
     */
    @Field(type = FieldType.Text, index = false)
    private String paymentMethodText;

    /**
     * 支付方式代码
     */
    private Integer paymentMethod;

    /**
     * 订单来源文本
     */
    @Field(type = FieldType.Text, index = false)
    private String orderSourceText;

    /**
     * 订单来源枚举值
     */
    private Integer orderSource;

    /**
     * 订单分配人微信Id
     */
    @Field(type = FieldType.Keyword)
    private String orderAssignToWxId;

    /**
     * 订单佣金比例
     */
    @Field(type = FieldType.Integer, index = false)
    private Integer orderCommissionRate;

    /**
     * 订单分类
     */
    @Field(type = FieldType.Text, index = false)
    private String categoryName;

    /**
     * 分类id
     */
    @Field(type = FieldType.Keyword)
    private Long orderCategoryId;

    /**
     * 订单标签
     */
    @Field(type = FieldType.Text, index = false)
    private List<String> orderTags;

    /**
     * 订单编程语言名称
     */
    @Field(type = FieldType.Text, index = false)
    private String langName;

    /**
     * 订单语言id
     */
    @Field(type = FieldType.Keyword)
    private Long orderLangId;

    /**
     * 订单描述
     */
    @Field(type = FieldType.Text, index = false)
    private String orderDesc;

    /**
     * 是否包含订单附件
     */
    @Field(type = FieldType.Integer, index = false)
    private Integer orderAttachmentNum;

    /**
     * 订单备注
     */
    @Field(type = FieldType.Text, index = false)
    private String orderRemark;


    /**
     * 订单状态
     */
    @Field(type = FieldType.Text, index = false)
    private String orderStatusText;

    /**
     * 订单状态状态码
     */
    private Integer orderStatus;

    /**
     * 是否有效
     */
    private Integer isValid;

    /**
     * 订单附件列表
     */
    @Field(type = FieldType.Nested, index = false)
    private List<OrderFileInfo> orderAttachmentList;


    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {DateFormat.date_time}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {DateFormat.date_time}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date updateTime;
    /**
     * 交付截止日期
     */
    @Field(store = true, type = FieldType.Date, format = {DateFormat.date_time}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderDeadline;

    /**
     * 订单完成时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {DateFormat.date_time}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderCompletionTime;

    /**
     * 订单开始日期
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {DateFormat.date_time}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderStartDate;

    /**
     * 订单结束日期
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {DateFormat.date_time}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date orderEndDate;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

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
        orderInfoEsDTO.setIsAssignedValue(CommonUtils.integerToBool(orderInfo.getIsAssigned()));
        orderInfoEsDTO.setIsPaidValue(CommonUtils.integerToBool(orderInfo.getIsPaid()));
        orderInfoEsDTO.setOrderTags(JsonUtils.jsonToList(orderInfo.getOrderTags()));
        // 设置源数据
        orderInfoEsDTO.setOrderStatus(orderInfo.getOrderStatus());
        orderInfoEsDTO.setOrderSource(orderInfo.getOrderSource());

        orderInfoEsDTO.setOrderAttachmentList(Optional.ofNullable(fileInfoList).orElse(Collections.emptyList()));
        orderInfoEsDTO.setOrderStatusText(CommonUtils.isPresent(orderStatus, "未知状态", OrderStatusEnum::getDesc));
        orderInfoEsDTO.setOrderSourceText(CommonUtils.isPresent(orderSource, "未知来源", OrderSourceEnum::getDesc));
        orderInfoEsDTO.setPaymentMethodText(CommonUtils.isPresent(paymentMethodEnum, "未知支付方式", PaymentMethodEnum::getDesc));
        orderInfoEsDTO.setCreatorName(CommonUtils.isPresent(userName, "未知用户"));
        orderInfoEsDTO.setLangName(CommonUtils.isPresent(langName, "未知语言"));
        orderInfoEsDTO.setCategoryName(CommonUtils.isPresent(categoryName, "未知分类"));
        return orderInfoEsDTO;
    }


    public static OrderInfoEsDTO of(OrderInfo orderInfo)
    {
        OrderInfoEsDTO dto = new OrderInfoEsDTO();
        BeanUtils.copyProperties(orderInfo, dto);
        dto.setOrderStatus(orderInfo.getOrderStatus());
        dto.setOrderSource(orderInfo.getOrderSource());
        dto.setPaymentMethod(orderInfo.getPaymentMethod());
        return dto;
    }

}
