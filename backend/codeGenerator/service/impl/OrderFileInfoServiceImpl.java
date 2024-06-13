package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.OrderFileInfoMapper;
import com.caixy.adminSystem.model.dto.category.OrderFileInfoQueryRequest;
import com.caixy.adminSystem.model.entity.OrderFileInfo;

import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.category.OrderFileInfoVO;

import com.caixy.adminSystem.service.OrderFileInfoService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单文件附件服务实现
 *


 */
@Service
@Slf4j
public class OrderFileInfoServiceImpl extends ServiceImpl<OrderFileInfoMapper, OrderFileInfo> implements OrderFileInfoService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param orderFileInfo
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validOrderFileInfo(OrderFileInfo orderFileInfo, boolean add) {
        ThrowUtils.throwIf(orderFileInfo == null, ErrorCode.PARAMS_ERROR);

        // 修改数据时，有参数则校验
        // todo 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param orderFileInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<OrderFileInfo> getQueryWrapper(OrderFileInfoQueryRequest orderFileInfoQueryRequest) {
        QueryWrapper<OrderFileInfo> queryWrapper = new QueryWrapper<>();
        if (orderFileInfoQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = orderFileInfoQueryRequest.getId();
        Long notId = orderFileInfoQueryRequest.getNotId();
        String title = orderFileInfoQueryRequest.getTitle();
        String content = orderFileInfoQueryRequest.getContent();
        String searchText = orderFileInfoQueryRequest.getSearchText();
        String sortField = orderFileInfoQueryRequest.getSortField();
        String sortOrder = orderFileInfoQueryRequest.getSortOrder();
        List<String> tagList = orderFileInfoQueryRequest.getTags();
        Long userId = orderFileInfoQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取订单文件附件封装
     *
     * @param orderFileInfo
     * @param request
     * @return
     */
    @Override
    public OrderFileInfoVO getOrderFileInfoVO(OrderFileInfo orderFileInfo, HttpServletRequest request) {
    // todo: 补充获取订单文件附件封装逻辑
        return null;
    }

    /**
     * 分页获取订单文件附件封装
     *
     * @param orderFileInfoPage
     * @param request
     * @return
     */
    @Override
    public Page<OrderFileInfoVO> getOrderFileInfoVOPage(Page<OrderFileInfo> orderFileInfoPage, HttpServletRequest request) {
        // todo: 补充分页获取订单文件附件封装逻辑
        return null;
    }

}
