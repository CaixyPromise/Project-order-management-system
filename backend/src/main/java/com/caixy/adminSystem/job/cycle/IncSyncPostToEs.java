package com.caixy.adminSystem.job.cycle;

import cn.hutool.core.collection.CollUtil;
import com.caixy.adminSystem.esdao.OrderEsRepository;
import com.caixy.adminSystem.mapper.OrderInfoMapper;
import com.caixy.adminSystem.mapper.PostMapper;
import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.dto.post.PostEsDTO;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.entity.Post;
import com.caixy.adminSystem.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步帖子到 es
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncPostToEs
{
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderEsRepository orderEsRepository;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run()
    {
        // 查询近 2 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 2 * 60 * 1000L);
        List<OrderInfo> postList = orderInfoService.listOrderInfoWithDeleteByUpdateDate(fiveMinutesAgoDate);
        if (CollUtil.isEmpty(postList))
        {
            log.info("no inc post");
            return;
        }
        List<OrderInfoEsDTO> orderInfoEsDTOS = orderInfoService.getOrderInfoEsDTOList(postList);
        final int pageSize = 500;
        int total = orderInfoEsDTOS.size();
        log.info("IncSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize)
        {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            orderEsRepository.saveAll(orderInfoEsDTOS.subList(i, end));
        }
        log.info("IncSyncPostToEs end, total {}", total);
    }
}
