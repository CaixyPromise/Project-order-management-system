package com.caixy.adminSystem;

import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.model.vo.order.OrderInfoPageVO;
import com.caixy.adminSystem.service.OrderInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests
{

    @Resource
    private OrderInfoService orderInfoService;


    @Test
    public void test()
    {
        OrderInfo byId = orderInfoService.getById(1799120210523881473L);
        OrderInfoPageVO orderInfoPageVO = new OrderInfoPageVO();
        BeanUtils.copyProperties(byId, orderInfoPageVO);

        System.out.println(orderInfoPageVO.toString());
    }

}
