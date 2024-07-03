package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.vo.file.DownloadFileVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_file_info(订单附件文件信息表)】的数据库操作Service
 * @createDate 2024-06-12 12:19:51
 */
public interface OrderFileInfoService extends IService<OrderFileInfo>
{
    Map<Long, List<OrderFileInfo>> getOrderFileInfoListByOrderIdList(Collection<Long> orderIdList);

    DownloadFileVO generateOrderFileVO(OrderFileInfo orderFileInfo);

    OrderFileInfo getFileInfoFromCache(String fileId);

    void removeFileInfoFromCache(String fileId);

    Boolean removeOrderFileInfo(OrderInfo orderInfo);
}
