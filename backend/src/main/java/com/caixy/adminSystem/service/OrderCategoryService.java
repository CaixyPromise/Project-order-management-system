package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.common.OptionVO;
import com.caixy.adminSystem.model.dto.category.OrderCategoryQueryRequest;
import com.caixy.adminSystem.model.entity.OrderCategory;
import com.caixy.adminSystem.model.vo.category.OrderCategoryVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    Boolean checkOrderCategoryExistById(Long id);

    Boolean checkOrderCategoryExistByIds(List<Long> ids);

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

    Map<Long, OrderCategoryVO> getOrderCategoryVOS(List<OrderCategory> categoryList);

    /**
     * 分页获取订单分类封装
     *
     * @param orderCategoryPage
     * @param request
     * @return
     */
    Page<OrderCategoryVO> getOrderCategoryVOPage(Page<OrderCategory> orderCategoryPage, HttpServletRequest request);

    Map<Long, String> getCategoryNameByIds(Set<Long> categoryIds);

    List<OptionVO<Long>> convertCategoryOptionListAndCache(List<OrderCategory> orderCategoryList);

    List<OptionVO<Long>> getCategoryOptionList();
}
