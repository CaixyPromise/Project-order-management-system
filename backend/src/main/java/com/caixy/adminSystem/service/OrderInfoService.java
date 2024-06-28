package com.caixy.adminSystem.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.common.EsPage;
import com.caixy.adminSystem.model.dto.file.UploadFileInfoDTO;
import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.dto.order.OrderInfoQueryRequest;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.vo.order.EventVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoPageVO;
import com.caixy.adminSystem.model.vo.order.OrderInfoVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author CAIXYPROMISE
 * @description 针对表【order_info(订单信息)】的数据库操作Service
 * @createDate 2024-06-04 20:50:04
 */
public interface OrderInfoService extends IService<OrderInfo>
{
    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validOrderInfo(OrderInfo post, boolean add);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<OrderInfo> getQueryWrapper(OrderInfoQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
    EsPage<OrderInfoPageVO> searchFromEs(OrderInfoQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @return
     */
    OrderInfoVO getOrderInfoVO(Long id);

    Map<String, String> generateFileUploadToken(List<UploadFileInfoDTO> fileInfoList,
                                                Long orderId);

    Long countUploadOrderAttachment(Long orderId);

    List<EventVO<OrderInfoVO>> getEvents(Integer year, Integer month, Long userId);

    @Transactional(rollbackFor = Exception.class)
    Boolean deleteOrderInfo(OrderInfo orderInfo);

    void setOrderValid(Long orderId, boolean validCode);

    List<OrderInfo> listOrderInfoWithDeleteByUpdateDate(Date updateTime);

    List<OrderInfoEsDTO> getOrderInfoEsDTOList(List<OrderInfo> orderInfos);
}
