package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.mapper.OrderFileInfoMapper;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.service.OrderFileInfoService;
import org.springframework.stereotype.Service;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_file_info(订单附件文件信息表)】的数据库操作Service实现
 * @createDate 2024-06-12 12:19:51
 */
@Service
public class OrderFileInfoServiceImpl extends ServiceImpl<OrderFileInfoMapper, OrderFileInfo>
        implements OrderFileInfoService
{
}




