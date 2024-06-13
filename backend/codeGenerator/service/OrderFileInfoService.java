package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.category.OrderFileInfoQueryRequest;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.vo.category.OrderFileInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单文件附件服务
 *


 */
public interface OrderFileInfoService extends IService<OrderFileInfo> {

    /**
     * 校验数据
     *
     * @param orderFileInfo
     * @param add 对创建的数据进行校验
     */
    void validOrderFileInfo(OrderFileInfo orderFileInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param orderFileInfoQueryRequest
     * @return
     */
    QueryWrapper<OrderFileInfo> getQueryWrapper(OrderFileInfoQueryRequest orderFileInfoQueryRequest);
    
    /**
     * 获取订单文件附件封装
     *
     * @param orderFileInfo
     * @param request
     * @return
     */
    OrderFileInfoVO getOrderFileInfoVO(OrderFileInfo orderFileInfo, HttpServletRequest request);

    /**
     * 分页获取订单文件附件封装
     *
     * @param orderFileInfoPage
     * @param request
     * @return
     */
    Page<OrderFileInfoVO> getOrderFileInfoVOPage(Page<OrderFileInfo> orderFileInfoPage, HttpServletRequest request);
}
