package com.caixy.adminSystem.model.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 添加订单请求返回体
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.dto.order.OrderInfoUploadResponse
 * @since 2024-06-10 17:13
 **/
@Data
public class OrderInfoUploadResponse implements Serializable
{
    /**
     * 是否完成请求
     */
    Boolean isFinish;

    /**
     * 对应文件uid的token映射
     */
    Map<String, String> tokenMap;

    private static final long serialVersionUID = 1L;
}
