package com.caixy.adminSystem.model.vo.category;

import com.caixy.adminSystem.model.entity.OrderCategory;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单分类视图
 */
@Data
public class OrderCategoryVO implements Serializable
{
    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类描述
     */
    private String categoryDesc;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 更新时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 批量返回Vo映射
     */
    private Map<Long, List<OrderCategoryVO>> vos;


    /**
     * 封装类转对象
     *
     * @param orderCategoryVO
     * @return
     */
    public static OrderCategory voToObj(OrderCategoryVO orderCategoryVO)
    {
        if (orderCategoryVO == null)
        {
            return null;
        }
        OrderCategory orderCategory = new OrderCategory();
        BeanUtils.copyProperties(orderCategoryVO, orderCategory);
        return orderCategory;
    }

    /**
     * 对象转封装类
     *
     * @param orderCategory
     * @return
     */
    public static OrderCategoryVO objToVo(OrderCategory orderCategory)
    {
        if (orderCategory == null)
        {
            return null;
        }
        OrderCategoryVO orderCategoryVO = new OrderCategoryVO();
        BeanUtils.copyProperties(orderCategory, orderCategoryVO);
        return orderCategoryVO;
    }

    public static OrderCategoryVO objToVOs(List<OrderCategory> orderCategoryList)
    {
        if (orderCategoryList == null || orderCategoryList.isEmpty())
        {
            return null;
        }
        OrderCategoryVO categoryVO = new OrderCategoryVO();
        // 转VO并根据ID进行分组
        Map<Long, List<OrderCategoryVO>> collect = orderCategoryList.stream().map(item -> {
            OrderCategoryVO orderCategoryVO = new OrderCategoryVO();
            BeanUtils.copyProperties(item, orderCategoryVO);
            return orderCategoryVO;
        }).collect(Collectors.groupingBy(OrderCategoryVO::getId));
        categoryVO.setVos(collect);
        return categoryVO;
    }

}
