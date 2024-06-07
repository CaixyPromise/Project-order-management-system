package com.caixy.adminSystem.model.vo.category;

import cn.hutool.json.JSONUtil;
import com.caixy.adminSystem.model.entity.OrderCategory;
import lombok.Data;
import com.caixy.adminSystem.model.vo.user.UserVO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单分类视图
 *


 */
@Data
public class OrderCategoryVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param orderCategoryVO
     * @return
     */
    public static OrderCategory voToObj(OrderCategoryVO orderCategoryVO) {
        if (orderCategoryVO == null) {
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
    public static OrderCategoryVO objToVo(OrderCategory orderCategory) {
        if (orderCategory == null) {
            return null;
        }
        OrderCategoryVO orderCategoryVO = new OrderCategoryVO();
        BeanUtils.copyProperties(orderCategory, orderCategoryVO);
        return orderCategoryVO;
    }
}
