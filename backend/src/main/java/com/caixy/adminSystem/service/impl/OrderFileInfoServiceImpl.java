package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.OrderFileInfoMapper;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.FileUploadBizEnum;
import com.caixy.adminSystem.model.enums.RedisConstant;
import com.caixy.adminSystem.model.vo.file.OrderFileVO;
import com.caixy.adminSystem.service.OrderFileInfoService;
import com.caixy.adminSystem.service.UploadFileService;
import com.caixy.adminSystem.utils.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_file_info(订单附件文件信息表)】的数据库操作Service实现
 * @createDate 2024-06-12 12:19:51
 */
@Service
@AllArgsConstructor
public class OrderFileInfoServiceImpl extends ServiceImpl<OrderFileInfoMapper, OrderFileInfo>
        implements OrderFileInfoService
{

    private final RedisUtils redisUtils;

    private final UploadFileService uploadFileService;

    @Override
    public List<OrderFileVO> getOrderFileInfoList(Long orderId)
    {
        QueryWrapper<OrderFileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderId", orderId);
        List<OrderFileInfo> orderFileInfoList = this.list(queryWrapper);

        if (orderFileInfoList != null && !orderFileInfoList.isEmpty())
        {
            return orderFileInfoList.stream().map(item -> {
                String fileId = UUID.randomUUID().toString();
                OrderFileVO orderFileVO = new OrderFileVO();
                BeanUtils.copyProperties(item, orderFileVO);
                orderFileVO.setFileSha256(null);
                orderFileVO.setId(fileId);
                redisUtils.setObject(RedisConstant.DOWNLOAD_FILE_KEY, orderFileVO, fileId);
                return orderFileVO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeOrderFileInfo(OrderInfo orderInfo)
    {
        QueryWrapper<OrderFileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderId", orderInfo.getId());
        List<OrderFileInfo> orderFileInfoList = list(queryWrapper);
        // 没有订单附件
        if (orderFileInfoList.isEmpty())
        {
            return true;
        }
        for (OrderFileInfo orderFileInfo : orderFileInfoList)
        {
            String fileName = orderFileInfo.getFileName();
            removeFile(orderFileInfo.getUserId(), fileName);
        }
        return removeByIds(orderFileInfoList);
    }

    private void removeFile(Long userId, String filename)
    {
        uploadFileService.deleteFile(FileUploadBizEnum.ORDER_ATTACHMENT, userId, filename);
    }

}




