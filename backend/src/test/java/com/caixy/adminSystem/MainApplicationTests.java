package com.caixy.adminSystem;

import com.caixy.adminSystem.esdao.OrderEsRepository;
import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.service.OrderInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests
{

    @Autowired
    private OrderInfoService orderInfoService;  // 用于获取数据库中的订单数据

    @Autowired
    private OrderEsRepository orderInfoEsRepository;  // 用于与Elasticsearch交互

    /**
     * 将订单数据从数据库同步到 Elasticsearch
     */
    public void syncOrderInfoToEs()
    {
        List<OrderInfo> orderInfos = orderInfoService.list();  // 假设这是你获取所有订单数据的方法
        List<OrderInfoEsDTO> esDtos = orderInfos.stream()
                .map(this::convertToOrderInfoEsDTO)  // 转换方法
                .collect(Collectors.toList());
        orderInfoEsRepository.saveAll(esDtos);  // 保存到 Elasticsearch
    }

    /**
     * 将数据库模型 OrderInfo 转换为 Elasticsearch 模型 OrderInfoEsDTO
     */
    private OrderInfoEsDTO convertToOrderInfoEsDTO(OrderInfo orderInfo)
    {
        OrderInfoEsDTO esDTO = new OrderInfoEsDTO();
        // 属性复制，可能需要调整以适应复杂的转换逻辑
        BeanUtils.copyProperties(orderInfo, esDTO);
        // 其他需要转换的字段可以在这里处理
        return esDTO;
    }

}
