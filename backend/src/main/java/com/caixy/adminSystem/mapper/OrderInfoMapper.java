package com.caixy.adminSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.entity.Post;

import java.util.Date;
import java.util.List;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_info(订单信息)】的数据库操作Mapper
 * @createDate 2024-06-04 20:50:04
 * @Entity com.caixy.adminSystem.model.entity.OrderInfo
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo>
{
    /**
     * 查询帖子列表（包括已被删除的数据）
     */
    List<OrderInfo> listOrderInfoWithDelete(Date minUpdateTime);

}




