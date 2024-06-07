package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.category.OrderCategoryQueryRequest;
import com.caixy.adminSystem.model.entity.OrderCategory;
import com.caixy.adminSystem.model.vo.category.OrderCategoryVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单分类服务
 *


 */
public interface OrderCategoryService extends IService<OrderCategory> {

    /**
     * 校验数据
     *
     * @param orderCategory
     * @param add 对创建的数据进行校验
     */
    void validOrderCategory(OrderCategory orderCategory, boolean add);

    /**
     * 获取查询条件
     *
     * @param orderCategoryQueryRequest
     * @return
     */
    QueryWrapper<OrderCategory> getQueryWrapper(OrderCategoryQueryRequest orderCategoryQueryRequest);
    
    /**
     * 获取订单分类封装
     *
     * @param orderCategory
     * @param request
     * @return
     */
    OrderCategoryVO getOrderCategoryVO(OrderCategory orderCategory, HttpServletRequest request);

    /**
     * 分页获取订单分类封装
     *
     * @param orderCategoryPage
     * @param request
     * @return
     */
    Page<OrderCategoryVO> getOrderCategoryVOPage(Page<OrderCategory> orderCategoryPage, HttpServletRequest request);
}
