package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.OrderCategoryMapper;
import com.caixy.adminSystem.model.dto.category.OrderCategoryQueryRequest;
import com.caixy.adminSystem.model.entity.OrderCategory;
import com.caixy.adminSystem.model.vo.category.OrderCategoryVO;
import com.caixy.adminSystem.service.OrderCategoryService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单分类服务实现
 */
@Service
@Slf4j
public class OrderCategoryServiceImpl extends ServiceImpl<OrderCategoryMapper, OrderCategory> implements OrderCategoryService
{
    private static final Integer MAX_NAME_SIZE = 10;

    private static final Integer MAX_DESC_SIZE = 256;

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param orderCategory
     * @param add           对创建的数据进行校验
     */
    @Override
    public void validOrderCategory(OrderCategory orderCategory, boolean add)
    {
        ThrowUtils.throwIf(orderCategory == null, ErrorCode.PARAMS_ERROR);

        // 校验分类名
        String categoryName = orderCategory.getCategoryName();
        if (StringUtils.isBlank(categoryName) || categoryName.length() > MAX_NAME_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称长度不能大于: " + MAX_NAME_SIZE);
        }
        // 校验分类描述
        String categoryDesc = orderCategory.getCategoryDesc();
        if (StringUtils.isBlank(categoryDesc) || categoryDesc.length() > MAX_DESC_SIZE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类描述长度不能大于： " + MAX_DESC_SIZE);
        }
        // 判断是否有重名
        QueryWrapper<OrderCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("categoryName", orderCategory.getCategoryName());
        long count = this.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "分类名称已存在");
    }

    /**
     * 根据id判断分类是否存在
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/7 下午12:08
     */
    @Override
    public Boolean checkOrderCategoryExistById(Long id)
    {
        return id != null && this.getById(id) != null;
    }

    /**
     * 批量根据id查询
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/7 下午12:14
     */
    @Override
    public Boolean checkOrderCategoryExistByIds(List<Long> ids)
    {
        return ids != null && !ids.isEmpty() && this.listByIds(ids).size() == ids.size();
    }


    /**
     * 获取查询条件
     *
     * @param orderCategoryQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<OrderCategory> getQueryWrapper(OrderCategoryQueryRequest orderCategoryQueryRequest)
    {
        QueryWrapper<OrderCategory> queryWrapper = new QueryWrapper<>();
        if (orderCategoryQueryRequest == null)
        {
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
        if (StringUtils.isNotBlank(searchText))
        {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList))
        {
            for (String tag : tagList)
            {
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
    public OrderCategoryVO getOrderCategoryVO(OrderCategory orderCategory, HttpServletRequest request)
    {
        return OrderCategoryVO.objToVo(orderCategory);
    }

    /**
     * 获取订单分类列表封装
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/7 下午12:46
     */
    @Override
    public Map<Long, OrderCategoryVO> getOrderCategoryVOS(List<OrderCategory> categoryList)
    {
        return OrderCategoryVO.listToIdVoMap(categoryList);
    }


    /**
     * 分页获取订单分类封装
     *
     * @param orderCategoryPage
     * @param request
     * @return
     */
    @Override
    public Page<OrderCategoryVO> getOrderCategoryVOPage(Page<OrderCategory> orderCategoryPage, HttpServletRequest request)
    {
        // 获取数据信息
        List<OrderCategory> orderCategoryList = orderCategoryPage.getRecords();
        Page<OrderCategoryVO> orderCategoryVOPage = new Page<>(orderCategoryPage.getCurrent(), orderCategoryPage.getSize());
        Set<Long> creatorIds = orderCategoryList.stream().map(OrderCategory::getCreatorId).collect(Collectors.toSet());
        Map<Long, String> userNameByIds = userService.getUserNameByIds(creatorIds);
        // 获取封装类
        List<OrderCategoryVO> categoryVOList = orderCategoryList.stream().map(item ->
        {
            OrderCategoryVO categoryVO = new OrderCategoryVO();
            BeanUtils.copyProperties(item, categoryVO);
            String creatorNames = userNameByIds.get(item.getCreatorId());
            if (creatorNames != null && !creatorNames.isEmpty())
            {
                categoryVO.setCreatorName(creatorNames);
            }
            else
            {
                categoryVO.setCreatorName("未知");
            }
            return categoryVO;
        }).collect(Collectors.toList());
        orderCategoryVOPage.setRecords(categoryVOList);
        orderCategoryVOPage.setTotal(orderCategoryPage.getTotal());
        return orderCategoryVOPage;
    }

    @Override
    public Map<Long, String>getCategoryNameByIds(Set<Long> categoryIds)
    {
        List<OrderCategory> orderCategories = listByIds(categoryIds);
        return orderCategories.stream().collect(Collectors.toMap(OrderCategory::getId, OrderCategory::getCategoryName));
    }

}
