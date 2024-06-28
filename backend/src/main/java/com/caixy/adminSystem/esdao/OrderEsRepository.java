package com.caixy.adminSystem.esdao;

import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.dto.post.PostEsDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 订单 ES 操作
 */
public interface OrderEsRepository extends ElasticsearchRepository<OrderInfoEsDTO, Long>
{
    List<OrderInfoEsDTO> findByCreatorId(Long creatorId);

    List<OrderInfoEsDTO> findByOrderId(String orderId);
}