package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.category.OrderFileInfoAddRequest;
import com.caixy.adminSystem.model.dto.category.OrderFileInfoEditRequest;
import com.caixy.adminSystem.model.dto.category.OrderFileInfoQueryRequest;
import com.caixy.adminSystem.model.dto.category.OrderFileInfoUpdateRequest;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.category.OrderFileInfoVO;
import com.caixy.adminSystem.service.OrderFileInfoService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 订单文件附件接口
 *


 */
@RestController
@RequestMapping("/orderFileInfo")
@Slf4j
public class OrderFileInfoController {

    @Resource
    private OrderFileInfoService orderFileInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建订单文件附件
     *
     * @param orderFileInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addOrderFileInfo(@RequestBody OrderFileInfoAddRequest orderFileInfoAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(orderFileInfoAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        OrderFileInfo orderFileInfo = new OrderFileInfo();
        BeanUtils.copyProperties(orderFileInfoAddRequest, orderFileInfo);
        // 数据校验
        orderFileInfoService.validOrderFileInfo(orderFileInfo, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        orderFileInfo.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = orderFileInfoService.save(orderFileInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newOrderFileInfoId = orderFileInfo.getId();
        return ResultUtils.success(newOrderFileInfoId);
    }

    /**
     * 删除订单文件附件
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrderFileInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        OrderFileInfo oldOrderFileInfo = orderFileInfoService.getById(id);
        ThrowUtils.throwIf(oldOrderFileInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldOrderFileInfo, user);
        // 操作数据库
        boolean result = orderFileInfoService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新订单文件附件（仅管理员可用）
     *
     * @param orderFileInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateOrderFileInfo(@RequestBody OrderFileInfoUpdateRequest orderFileInfoUpdateRequest) {
        if (orderFileInfoUpdateRequest == null || orderFileInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        OrderFileInfo orderFileInfo = new OrderFileInfo();
        BeanUtils.copyProperties(orderFileInfoUpdateRequest, orderFileInfo);
        // 数据校验
        orderFileInfoService.validOrderFileInfo(orderFileInfo, false);
        // 判断是否存在
        long id = orderFileInfoUpdateRequest.getId();
        OrderFileInfo oldOrderFileInfo = orderFileInfoService.getById(id);
        ThrowUtils.throwIf(oldOrderFileInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = orderFileInfoService.updateById(orderFileInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取订单文件附件（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<OrderFileInfoVO> getOrderFileInfoVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        OrderFileInfo orderFileInfo = orderFileInfoService.getById(id);
        ThrowUtils.throwIf(orderFileInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(orderFileInfoService.getOrderFileInfoVO(orderFileInfo, request));
    }

    /**
     * 分页获取订单文件附件列表（仅管理员可用）
     *
     * @param orderFileInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<OrderFileInfo>> listOrderFileInfoByPage(@RequestBody OrderFileInfoQueryRequest orderFileInfoQueryRequest) {
        long current = orderFileInfoQueryRequest.getCurrent();
        long size = orderFileInfoQueryRequest.getPageSize();
        // 查询数据库
        Page<OrderFileInfo> orderFileInfoPage = orderFileInfoService.page(new Page<>(current, size),
                orderFileInfoService.getQueryWrapper(orderFileInfoQueryRequest));
        return ResultUtils.success(orderFileInfoPage);
    }

    /**
     * 分页获取订单文件附件列表（封装类）
     *
     * @param orderFileInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<OrderFileInfoVO>> listOrderFileInfoVOByPage(@RequestBody OrderFileInfoQueryRequest orderFileInfoQueryRequest,
                                                               HttpServletRequest request) {
        long current = orderFileInfoQueryRequest.getCurrent();
        long size = orderFileInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<OrderFileInfo> orderFileInfoPage = orderFileInfoService.page(new Page<>(current, size),
                orderFileInfoService.getQueryWrapper(orderFileInfoQueryRequest));
        // 获取封装类
        return ResultUtils.success(orderFileInfoService.getOrderFileInfoVOPage(orderFileInfoPage, request));
    }

    /**
     * 分页获取当前登录用户创建的订单文件附件列表
     *
     * @param orderFileInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<OrderFileInfoVO>> listMyOrderFileInfoVOByPage(@RequestBody OrderFileInfoQueryRequest orderFileInfoQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(orderFileInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        orderFileInfoQueryRequest.setUserId(loginUser.getId());
        long current = orderFileInfoQueryRequest.getCurrent();
        long size = orderFileInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<OrderFileInfo> orderFileInfoPage = orderFileInfoService.page(new Page<>(current, size),
                orderFileInfoService.getQueryWrapper(orderFileInfoQueryRequest));
        // 获取封装类
        return ResultUtils.success(orderFileInfoService.getOrderFileInfoVOPage(orderFileInfoPage, request));
    }

    /**
     * 编辑订单文件附件（给用户使用）
     *
     * @param orderFileInfoEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editOrderFileInfo(@RequestBody OrderFileInfoEditRequest orderFileInfoEditRequest, HttpServletRequest request) {
        if (orderFileInfoEditRequest == null || orderFileInfoEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        OrderFileInfo orderFileInfo = new OrderFileInfo();
        BeanUtils.copyProperties(orderFileInfoEditRequest, orderFileInfo);
        // 数据校验
        orderFileInfoService.validOrderFileInfo(orderFileInfo, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = orderFileInfoEditRequest.getId();
        OrderFileInfo oldOrderFileInfo = orderFileInfoService.getById(id);
        ThrowUtils.throwIf(oldOrderFileInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        checkIsSelfOrAdmin(oldOrderFileInfo, loginUser);
        // 操作数据库
        boolean result = orderFileInfoService.updateById(orderFileInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

    private void checkIsSelfOrAdmin(OrderFileInfo orderFileInfo, User loginUser)
    {
        if (!orderFileInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }
}
