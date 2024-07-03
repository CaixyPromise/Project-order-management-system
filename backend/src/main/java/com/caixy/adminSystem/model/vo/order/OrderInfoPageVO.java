package com.caixy.adminSystem.model.vo.order;

import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.enums.OrderStatusEnum;
import com.caixy.adminSystem.model.vo.file.OrderFilePageVO;
import com.caixy.adminSystem.utils.CommonUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单信息VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.order.OrderInfoPageVO
 * @since 2024-06-04 20:56
 **/
@Data
public class OrderInfoPageVO implements Serializable
{

    /**
     * id
     */
    private Long id;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 订单平台id
     */
    private String orderId;

    /**
     * 订单名称描述
     */
    private String orderTitle;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 已支付金额
     */
    private BigDecimal amountPaid;

    /**
     * 是否是对外分配
     */
    private Boolean isAssigned;

    /**
     * 是否支付
     */
    private Boolean isPaid;

    /**
     * 订单联系方式
     */
    private String customerContact;

    /**
     * 顾客邮箱
     */
    private String customerEmail;

    /**
     * 订单分配人微信Id
     */
    private String orderAssignToWxId;

    /**
     * 是否包含附件
     */
    private Boolean hasOrderAttachment;


    /**
     * 订单来源
     */
    private String orderSource;


    /**
     * 订单分类名称
     */
    private String orderCategoryName;


    /**
     * 订单编程语言ID
     */
    private String langName;


    /**
     * 交付截止日期
     */
    private Date orderDeadline;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单附件列表
     */
    private List<OrderFilePageVO> orderAttachmentList;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

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
        vo.setOrderStatus(CommonUtils.isPresent(OrderStatusEnum.getByCode(dto.getOrderStatus()), "未知状态", OrderStatusEnum::getDesc));
        vo.setHasOrderAttachment(CommonUtils.isPresent(dto.getOrderAttachmentNum(), 0) > 0);
        vo.setIsAssigned(dto.getIsAssignedValue());
        vo.setOrderSource(dto.getOrderSourceText());
        vo.setOrderCategoryName(dto.getCategoryName());
        vo.setIsPaid(dto.getIsPaidValue());
        vo.setOrderAttachmentList(dto.getOrderAttachmentList().stream()
                .map(OrderFilePageVO::of)
                .collect(Collectors.toList()));
        return vo;
    }
}
