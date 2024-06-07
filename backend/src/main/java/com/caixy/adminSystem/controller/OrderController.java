package com.caixy.adminSystem.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;

import com.caixy.adminSystem.model.dto.order.OrderInfoAddRequest;
import com.caixy.adminSystem.model.dto.order.OrderInfoEditRequest;
import com.caixy.adminSystem.model.dto.order.OrderInfoQueryRequest;
import com.caixy.adminSystem.model.dto.order.OrderInfoUpdateRequest;
import com.caixy.adminSystem.model.entity.OrderInfo;

import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;
import com.caixy.adminSystem.service.OrderInfoService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController
{
    @Resource
    private OrderInfoService postService;

    @Resource
    private UserService userService;

    // 最大标签个数
    private final static Integer MAX_TAGS_SIZE = 10;
    // 最大标签字数
    private final static Integer MAX_TAG_TEXT_SIZE = 8;

    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addOrderInfo(@RequestBody OrderInfoAddRequest postAddRequest, HttpServletRequest request)
    {
        if (postAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo post = new OrderInfo();
        BeanUtils.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getOrderTags();
        // 如果标签非空，则进行校验
        if (tags != null)
        {
            validTags(tags);
            post.setOrderTags(JSONUtil.toJsonStr(tags));
        }
        // 校验语言是否合法

        postService.validOrderInfo(post, true);


        User loginUser = userService.getLoginUser(request);
        post.setCreatorId(loginUser.getId());
        boolean result = postService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newOrderInfoId = post.getId();
        return ResultUtils.success(newOrderInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrderInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        OrderInfo oldOrderInfo = postService.getById(id);
        ThrowUtils.throwIf(oldOrderInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldOrderInfo.getCreatorId().equals(user.getId()) && !userService.isAdmin(request))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = postService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateOrderInfo(@RequestBody OrderInfoUpdateRequest postUpdateRequest)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo post = new OrderInfo();
        BeanUtils.copyProperties(postUpdateRequest, post);
        List<String> tags = postUpdateRequest.getTags();
        if (tags != null)
        {
            post.setOrderTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
        postService.validOrderInfo(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        OrderInfo oldOrderInfo = postService.getById(id);
        ThrowUtils.throwIf(oldOrderInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = postService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<OrderInfoVO> getOrderInfoVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo post = postService.getById(id);
        if (post == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(postService.getOrderInfoVO(post, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<OrderInfo>> listOrderInfoByPage(@RequestBody OrderInfoQueryRequest postQueryRequest)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<OrderInfo> postPage = postService.page(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<OrderInfoVO>> listOrderInfoVOByPage(@RequestBody OrderInfoQueryRequest postQueryRequest,
                                                       HttpServletRequest request)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<OrderInfo> postPage = postService.page(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postService.getOrderInfoVOPage(postPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<OrderInfoVO>> listMyOrderInfoVOByPage(@RequestBody OrderInfoQueryRequest postQueryRequest,
                                                         HttpServletRequest request)
    {
        if (postQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        postQueryRequest.setUserId(loginUser.getId());
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<OrderInfo> postPage = postService.page(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postService.getOrderInfoVOPage(postPage, request));
    }

    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<OrderInfoVO>> searchOrderInfoVOByPage(@RequestBody OrderInfoQueryRequest postQueryRequest,
                                                         HttpServletRequest request)
    {
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<OrderInfo> postPage = postService.searchFromEs(postQueryRequest);
        return ResultUtils.success(postService.getOrderInfoVOPage(postPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param postEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editOrderInfo(@RequestBody OrderInfoEditRequest postEditRequest, HttpServletRequest request)
    {
        if (postEditRequest == null || postEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo post = new OrderInfo();
        BeanUtils.copyProperties(postEditRequest, post);
        List<String> tags = postEditRequest.getTags();
        if (tags != null)
        {
            post.setOrderTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
        postService.validOrderInfo(post, false);
        User loginUser = userService.getLoginUser(request);
        long id = postEditRequest.getId();
        // 判断是否存在
        OrderInfo oldOrderInfo = postService.getById(id);
        ThrowUtils.throwIf(oldOrderInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldOrderInfo.getCreatorId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = postService.updateById(post);
        return ResultUtils.success(result);
    }

    private void validTags(List<String> tags)
    {
        // 标签长度不能过长
        if (tags.size() > MAX_TAGS_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签数量超过限制");
        }
        for (String tag : tags)
        {
            if (StringUtils.isBlank(tag))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签不能为空");
            }
            if (tag.length() > MAX_TAG_TEXT_SIZE)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签文字过长过长，字符长度不能大于: " + MAX_TAG_TEXT_SIZE);
            }
        }
    }

}
