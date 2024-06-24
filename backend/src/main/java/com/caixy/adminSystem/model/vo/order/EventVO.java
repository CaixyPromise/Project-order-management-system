package com.caixy.adminSystem.model.vo.order;

import com.caixy.adminSystem.model.entity.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单提醒事件VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.order.EventVO
 * @since 2024-06-24 13:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventVO<T> implements Serializable
{
    /**
     * 订单Id
     */
    private Long id;

    /**
     * 事件
     */
    private Date date;
    /**
     * 内容
     */
    private T content;
    /**
     * 等级
     */
    private String level;
    private static final long serialVersionUID = 1L;
}
