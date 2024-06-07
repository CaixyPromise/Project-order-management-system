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
import com.caixy.adminSystem.model.dto.category.OrderCategoryAddRequest;
import com.caixy.adminSystem.model.dto.category.OrderCategoryQueryRequest;
import com.caixy.adminSystem.model.dto.category.OrderCategoryUpdateRequest;
import com.caixy.adminSystem.model.entity.OrderCategory;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.category.OrderCategoryVO;
import com.caixy.adminSystem.service.OrderCategoryService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单分类接口
 *
 */
@RestController
@RequestMapping("/orderCategory")
@Slf4j
public class OrderCategoryController
{

    @Resource
    private OrderCategoryService orderCategoryService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建订单分类
     *
     * @param orderCategoryAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addOrderCategory(@RequestBody OrderCategoryAddRequest orderCategoryAddRequest, HttpServletRequest request)
    {
        ThrowUtils.throwIf(orderCategoryAddRequest == null, ErrorCode.PARAMS_ERROR);
        OrderCategory orderCategory = new OrderCategory();
        BeanUtils.copyProperties(orderCategoryAddRequest, orderCategory);
        User loginUser = userService.getLoginUser(request);
        orderCategory.setCreatorId(loginUser.getId());
        checkIsSelfOrAdmin(orderCategory, loginUser);
        // 数据校验
        orderCategoryService.validOrderCategory(orderCategory, true);
        orderCategory.setCreatorId(loginUser.getId());
        // 写入数据库
        boolean result = orderCategoryService.save(orderCategory);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newOrderCategoryId = orderCategory.getId();
        return ResultUtils.success(newOrderCategoryId);
    }

    /**
     * 删除订单分类
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrderCategory(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        OrderCategory oldOrderCategory = orderCategoryService.getById(id);
        ThrowUtils.throwIf(oldOrderCategory == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldOrderCategory, user);
        // 操作数据库
        boolean result = orderCategoryService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新订单分类（仅管理员可用）
     *
     * @param orderCategoryUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateOrderCategory(@RequestBody OrderCategoryUpdateRequest orderCategoryUpdateRequest, HttpServletRequest request)
    {
        if (orderCategoryUpdateRequest == null || orderCategoryUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        OrderCategory orderCategory = new OrderCategory();
        BeanUtils.copyProperties(orderCategoryUpdateRequest, orderCategory);
        // 数据校验
        // 判断是否存在
        long id = orderCategoryUpdateRequest.getId();
        OrderCategory oldOrderCategory = orderCategoryService.getById(id);
        ThrowUtils.throwIf(oldOrderCategory == null, ErrorCode.NOT_FOUND_ERROR);
        orderCategoryService.validOrderCategory(orderCategory, false);
        checkIsSelfOrAdmin(oldOrderCategory, userService.getLoginUser(request));
        // 操作数据库
        boolean result = orderCategoryService.updateById(orderCategory);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取订单分类（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/voById")
    public BaseResponse<OrderCategoryVO> getOrderCategoryVOById(long id, HttpServletRequest request)
    {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        OrderCategory orderCategory = orderCategoryService.getById(id);
        ThrowUtils.throwIf(orderCategory == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(orderCategoryService.getOrderCategoryVO(orderCategory, request));
    }

    /**
     * 根据 id 获取订单分类（封装类）
     *
     */
    @GetMapping("/get/vo/list")
    public BaseResponse<OrderCategoryVO> getOrderCategoryVOS()
    {
        // 查询数据库
        List<OrderCategory> orderCategory = orderCategoryService.list();
        // 获取封装类
        return ResultUtils.success(orderCategoryService.getOrderCategoryVOS(orderCategory));
    }

    /**
     * 分页获取订单分类列表（仅管理员可用）
     *
     * @param orderCategoryQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<OrderCategoryVO>> listOrderCategoryByPage(@RequestBody OrderCategoryQueryRequest orderCategoryQueryRequest)
    {
        long current = orderCategoryQueryRequest.getCurrent();
        long size = orderCategoryQueryRequest.getPageSize();
        // 查询数据库
        Page<OrderCategory> languageTypePage = orderCategoryService.page(new Page<>(current, size),
                orderCategoryService.getQueryWrapper(orderCategoryQueryRequest));
        // 获取数据信息
        List<OrderCategory> languageTypes = languageTypePage.getRecords();
        Page<OrderCategoryVO> languageTypeVOPage = new Page<>(current, size);
        Set<Long> creatorIds = languageTypes.stream().map(OrderCategory::getCreatorId).collect(Collectors.toSet());
        Map<Long, List<String>> userNameByIds = userService.getUserNameByIds(creatorIds);
        // 获取封装类
        List<OrderCategoryVO> categoryVOList = languageTypes.stream().map(item ->
        {
            OrderCategoryVO categoryVO = new OrderCategoryVO();
            BeanUtils.copyProperties(item, categoryVO);
            List<String> creatorNames = userNameByIds.get(item.getCreatorId());
            if (creatorNames != null && !creatorNames.isEmpty())
            {
                categoryVO.setCreatorName(creatorNames.get(0));
            }
            else
            {
                categoryVO.setCreatorName("未知");
            }
            return categoryVO;
        }).collect(Collectors.toList());
        languageTypeVOPage.setRecords(categoryVOList);
        languageTypeVOPage.setTotal(languageTypePage.getTotal());
        return ResultUtils.success(languageTypeVOPage);
    }
    

    // endregion

    private void checkIsSelfOrAdmin(OrderCategory orderCategory, User loginUser)
    {
        if (!orderCategory.getCreatorId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }
}
