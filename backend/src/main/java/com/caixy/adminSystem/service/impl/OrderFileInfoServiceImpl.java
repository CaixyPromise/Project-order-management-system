package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.mapper.OrderFileInfoMapper;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.FileActionBizEnum;
import com.caixy.adminSystem.model.enums.RedisConstant;
import com.caixy.adminSystem.model.vo.file.DownloadFileVO;
import com.caixy.adminSystem.service.OrderFileInfoService;
import com.caixy.adminSystem.service.UploadFileService;
import com.caixy.adminSystem.utils.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public Map<Long, List<OrderFileInfo>> getOrderFileInfoListByOrderIdList(Collection<Long> orderIdList)
    {
        return this.list(new QueryWrapper<OrderFileInfo>().in("orderId", orderIdList))
                .stream().collect(Collectors.groupingBy(OrderFileInfo::getOrderId));
    }


    /**
     * 生成订单文件下载VO
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/7/2 下午4:47
     */
    @Override
    public DownloadFileVO generateOrderFileVO(OrderFileInfo orderFileInfo)
    {
        String fileId = UUID.randomUUID().toString();
        DownloadFileVO downloadFileVO = new DownloadFileVO();
        downloadFileVO.setId(fileId);
        downloadFileVO.setFileCategoryName(FileActionBizEnum.ORDER_ATTACHMENT.getValue());
        downloadFileVO.setSha256(orderFileInfo.getFileSha256());
        redisUtils.setObject(RedisConstant.DOWNLOAD_FILE_KEY, orderFileInfo, fileId);
        return downloadFileVO;
    }

    @Override
    public OrderFileInfo getFileInfoFromCache(String fileId)
    {
        return redisUtils.getObject(RedisConstant.DOWNLOAD_FILE_KEY, OrderFileInfo.class, fileId);
    }

    /**
     * 删除缓存key
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/7/2 下午9:07
     */
    @Override
    public void removeFileInfoFromCache(String fileId)
    {
        redisUtils.delete(RedisConstant.DOWNLOAD_FILE_KEY, fileId);
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
        uploadFileService.deleteFile(FileActionBizEnum.ORDER_ATTACHMENT, userId, filename);
    }

}




