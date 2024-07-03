package com.caixy.adminSystem.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.esdao.OrderEsRepository;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.common.EsPage;
import com.caixy.adminSystem.model.dto.file.UploadFileInfoDTO;
import com.caixy.adminSystem.model.dto.order.*;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.file.DownloadFileVO;
import com.caixy.adminSystem.model.vo.order.EventVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoPageVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;
import com.caixy.adminSystem.mq.producer.exchange.OrderAttachment.OrderAttachmentProducer;
import com.caixy.adminSystem.mq.producer.exchange.OrderInfo.OrderInfoSaveProducer;
import com.caixy.adminSystem.service.OrderInfoService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
    private OrderInfoService orderInfoService;

    @Resource
    private UserService userService;

    // 最大标签个数
    private final static Integer MAX_TAGS_SIZE = 10;
    // 最大标签字数
    private final static Integer MAX_TAG_TEXT_SIZE = 8;

    @Resource
    private OrderAttachmentProducer orderAttachmentProducer;

    @Resource
    private OrderInfoSaveProducer orderInfoSaveProducer;

    @Resource
    private OrderEsRepository orderEsRepository;

    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<OrderInfoUploadResponse> addOrderInfo(@RequestBody OrderInfoAddRequest postAddRequest,
                                                              HttpServletRequest request)
    {
        if (postAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo post = new OrderInfo();
        BeanUtils.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getOrderTag();
        // 如果标签非空，则进行校验
        if (tags != null)
        {
            validTags(tags);
            post.setOrderTags(JSONUtil.toJsonStr(tags));
        }
        // 校验语言是否合法
        orderInfoService.validOrderInfo(post, true);
        log.info("验证成功: {}", post);
        User loginUser = userService.getLoginUser(request);
        post.setCreatorId(loginUser.getId());
        // 检查是否有附件
        List<UploadFileInfoDTO> attachmentList = postAddRequest.getAttachmentList();
        boolean hasAttachment = !attachmentList.isEmpty();
        post.setOrderAttachmentNum(attachmentList.size());
        boolean result = orderInfoService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        orderInfoSaveProducer.sendMessage(post.getId());
        return ResultUtils.success(buildOrderInfoResponse(post, hasAttachment, attachmentList, loginUser));
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteOrderInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        OrderInfo oldOrderInfo = orderInfoService.getById(id);
        ThrowUtils.throwIf(oldOrderInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldOrderInfo.getCreatorId().equals(user.getId()) && !userService.isAdmin(request))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Boolean deleted = orderInfoService.deleteOrderInfo(oldOrderInfo);
        // 如果删除成功
        if (deleted)
        {
            orderEsRepository.deleteById(oldOrderInfo.getId());
        }
        return ResultUtils.success(deleted);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<OrderInfoUploadResponse> updateOrderInfo(@RequestBody OrderInfoUpdateRequest postUpdateRequest,
                                                                 HttpServletRequest request)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取用户
        User loginUser = userService.getLoginUser(request);

        OrderInfo post = new OrderInfo();
        BeanUtils.copyProperties(postUpdateRequest, post);
        // 参数校验
        orderInfoService.validOrderInfo(post, false);
        long id = postUpdateRequest.getId();

        // 判断是否存在
        OrderInfo oldOrderInfo = orderInfoService.getById(id);
        ThrowUtils.throwIf(oldOrderInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 检查是否有附件
        List<UploadFileInfoDTO> attachmentList = postUpdateRequest.getAttachmentList();
        boolean hasAttachment = !attachmentList.isEmpty();
        // 附加文件数量
        if (hasAttachment)
        {
            int newOrderAttachmentNum = post.getOrderAttachmentNum() + attachmentList.size();
            post.setOrderAttachmentNum(newOrderAttachmentNum);
        }
        boolean result = orderInfoService.updateById(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        orderInfoSaveProducer.sendMessage(post.getId());
        return ResultUtils.success(buildOrderInfoResponse(post, hasAttachment, attachmentList, loginUser));
    }

    /**
     * 根据 id 获取：系统内的订单id，不是平台订单id
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<OrderInfoVO> getOrderInfoVOById(@RequestParam("id") Long id, HttpServletRequest request)
    {

        return ResultUtils.success(orderInfoService.getOrderInfoVO(id));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<EsPage<OrderInfoPageVO>> listOrderInfoVOByPage(@RequestBody OrderInfoQueryRequest postQueryRequest,
                                                                       HttpServletRequest request)
    {
        long size = postQueryRequest.getPageSize();
        long current = postQueryRequest.getCurrent();
        if (current <= 0) // 如果当前页小于等于0，则设置为1
        {
            postQueryRequest.setCurrent(1);
        }
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        EsPage<OrderInfoPageVO> postPage = orderInfoService.getPageVoFromEs(postQueryRequest);
        return ResultUtils.success(postPage);
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
    public BaseResponse<Page<OrderInfoPageVO>> searchOrderInfoVOByPage(@RequestBody OrderInfoQueryRequest postQueryRequest,
                                                                       HttpServletRequest request)
    {
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<OrderInfoPageVO> postPage = orderInfoService.searchFromEs(postQueryRequest);
        return ResultUtils.success(postPage);
    }

    @GetMapping("/getEvent")
    public BaseResponse<List<EventVO<OrderInfoVO>>> getOrderEventList(
            @RequestParam(value = "startYear", required = false) Integer year,
            @RequestParam(value = "startMonth", required = false) Integer month,
            HttpServletRequest request
    )
    {
        if (year != null && month != null)
        {
            ThrowUtils.throwIf(year < 2000 || year > 2099 || month < 1 || month > 12, ErrorCode.PARAMS_ERROR, "时间错误");
        }
        else
        {
            LocalDate now = LocalDate.now();
            year = now.getYear();
            month = now.getMonthValue();
        }
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(orderInfoService.getEvents(year, month, loginUser.getId()));
    }

    @GetMapping("/get/downloadUrl")
    public BaseResponse<DownloadFileVO> getOrderFileDownloadUrlById(
                @RequestParam("id") Long id,
                @RequestParam("orderId") Long orderId,
                HttpServletRequest request
        )
    {
        User loginUser = userService.getLoginUser(request);
        DownloadFileVO downloadVo = orderInfoService.getOrderFileDownloadUrlById(id, orderId, loginUser.getId());
        return ResultUtils.success(downloadVo);
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

    private OrderInfoUploadResponse buildOrderInfoResponse(OrderInfo post, Boolean hasAttachment, List<UploadFileInfoDTO> attachmentList, User loginUser)
    {
        long newOrderInfoId = post.getId();
        OrderInfoUploadResponse orderInfoUploadResponse = new OrderInfoUploadResponse();
        if (hasAttachment)
        {
            orderInfoUploadResponse.setIsFinish(false);
            orderInfoUploadResponse.setTokenMap(orderInfoService.generateFileUploadToken(attachmentList, newOrderInfoId));
            // 发送检查文件是否上传完成与数量齐全的延迟消息
            OrderFileUploadMqDTO orderFileUploadMqDTO = new OrderFileUploadMqDTO();
            orderFileUploadMqDTO.setFileCount(attachmentList.size());
            orderFileUploadMqDTO.setOrderId(newOrderInfoId);
            orderFileUploadMqDTO.setUserId(loginUser.getId());
            orderAttachmentProducer.sendMessage(orderFileUploadMqDTO);
            log.info("发送延迟消息: {}", orderFileUploadMqDTO);
        }
        else
        {
            orderInfoUploadResponse.setIsFinish(true);
        }
        return orderInfoUploadResponse;
    }
}
