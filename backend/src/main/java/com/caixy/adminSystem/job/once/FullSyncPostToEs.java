package com.caixy.adminSystem.job.once;

import cn.hutool.core.collection.CollUtil;
import com.caixy.adminSystem.esdao.OrderEsRepository;
import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.entity.OrderFileInfo;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.enums.OrderSourceEnum;
import com.caixy.adminSystem.model.enums.OrderStatusEnum;
import com.caixy.adminSystem.model.enums.PaymentMethodEnum;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.utils.CommonUtils;
import com.caixy.adminSystem.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 全量同步帖子到 es
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner
{
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderEsRepository orderEsRepository;


    @Override
    public void run(String... args)
    {
        List<OrderInfo> postList = orderInfoService.list();
        if (CollUtil.isEmpty(postList))
        {
            return;
        }
        List<OrderInfoEsDTO> orderInfoEsDTOS = orderInfoService.getOrderInfoEsDTOList(postList);
        final int pageSize = 500;
        int total = orderInfoEsDTOS.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize)
        {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            orderEsRepository.saveAll(orderInfoEsDTOS.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
    }

}
