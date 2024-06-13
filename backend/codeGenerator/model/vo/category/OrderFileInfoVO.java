package com.caixy.adminSystem.model.vo.category;

import cn.hutool.json.JSONUtil;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import lombok.Data;
import com.caixy.adminSystem.model.vo.user.UserVO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单文件附件视图
 *


 */
@Data
public class OrderFileInfoVO implements Serializable {

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
     * @param orderFileInfoVO
     * @return
     */
    public static OrderFileInfo voToObj(OrderFileInfoVO orderFileInfoVO) {
        if (orderFileInfoVO == null) {
            return null;
        }
        OrderFileInfo orderFileInfo = new OrderFileInfo();
        BeanUtils.copyProperties(orderFileInfoVO, orderFileInfo);
        return orderFileInfo;
    }

    /**
     * 对象转封装类
     *
     * @param orderFileInfo
     * @return
     */
    public static OrderFileInfoVO objToVo(OrderFileInfo orderFileInfo) {
        if (orderFileInfo == null) {
            return null;
        }
        OrderFileInfoVO orderFileInfoVO = new OrderFileInfoVO();
        BeanUtils.copyProperties(orderFileInfo, orderFileInfoVO);
        return orderFileInfoVO;
    }
}
