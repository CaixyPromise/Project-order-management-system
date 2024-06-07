package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.OrderCategoryMapper;
import com.caixy.adminSystem.model.dto.category.OrderCategoryQueryRequest;
import com.caixy.adminSystem.model.entity.OrderCategory;

import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.category.OrderCategoryVO;

import com.caixy.adminSystem.service.OrderCategoryService;
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
 * 订单分类服务实现
 *


 */
@Service
@Slf4j
public class OrderCategoryServiceImpl extends ServiceImpl<OrderCategoryMapper, OrderCategory> implements OrderCategoryService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param orderCategory
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validOrderCategory(OrderCategory orderCategory, boolean add) {
        ThrowUtils.throwIf(orderCategory == null, ErrorCode.PARAMS_ERROR);

        // 修改数据时，有参数则校验
        // todo 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param orderCategoryQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<OrderCategory> getQueryWrapper(OrderCategoryQueryRequest orderCategoryQueryRequest) {
        QueryWrapper<OrderCategory> queryWrapper = new QueryWrapper<>();
        if (orderCategoryQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = orderCategoryQueryRequest.getId();
        Long notId = orderCategoryQueryRequest.getNotId();
        String title = orderCategoryQueryRequest.getTitle();
        String content = orderCategoryQueryRequest.getContent();
        String searchText = orderCategoryQueryRequest.getSearchText();
        String sortField = orderCategoryQueryRequest.getSortField();
        String sortOrder = orderCategoryQueryRequest.getSortOrder();
        List<String> tagList = orderCategoryQueryRequest.getTags();
        Long userId = orderCategoryQueryRequest.getUserId();
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
     * 获取订单分类封装
     *
     * @param orderCategory
     * @param request
     * @return
     */
    @Override
    public OrderCategoryVO getOrderCategoryVO(OrderCategory orderCategory, HttpServletRequest request) {
    // todo: 补充获取订单分类封装逻辑
        return null;
    }

    /**
     * 分页获取订单分类封装
     *
     * @param orderCategoryPage
     * @param request
     * @return
     */
    @Override
    public Page<OrderCategoryVO> getOrderCategoryVOPage(Page<OrderCategory> orderCategoryPage, HttpServletRequest request) {
        // todo: 补充分页获取订单分类封装逻辑
        return null;
    }

}
