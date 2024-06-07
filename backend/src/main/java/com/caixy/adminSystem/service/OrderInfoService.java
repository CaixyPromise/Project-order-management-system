package com.caixy.adminSystem.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.order.OrderInfoQueryRequest;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_info(订单信息)】的数据库操作Service
 * @createDate 2024-06-04 20:50:04
 */
public interface OrderInfoService extends IService<OrderInfo>
{
    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validOrderInfo(OrderInfo post, boolean add);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<OrderInfo> getQueryWrapper(OrderInfoQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
    Page<OrderInfo> searchFromEs(OrderInfoQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param request
     * @return
     */
    OrderInfoVO getOrderInfoVO(OrderInfo post, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @param request
     * @return
     */
    Page<OrderInfoVO> getOrderInfoVOPage(Page<OrderInfo> postPage, HttpServletRequest request);
}
