package com.caixy.adminSystem.model.vo.file;

import com.caixy.adminSystem.model.entity.OrderFileInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单文件分页VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.file.OrderFilePageVO
 * @since 2024-07-02 13:19
 **/
@Data
public class OrderFilePageVO implements Serializable
{
    /**
     * 文件id
     */
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static OrderFilePageVO of(OrderFileInfo orderFileInfo)
    {
        OrderFilePageVO orderFilePageVO = new OrderFilePageVO();
        BeanUtils.copyProperties(orderFileInfo, orderFilePageVO);
        orderFilePageVO.setFileName(orderFileInfo.buildFileName());
        return orderFilePageVO;
    }
}
