package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.OrderInfo;
import generator.service.OrderInfoService;
import generator.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author CAIXYPROMISE
* @description 针对表【order_info(订单信息)】的数据库操作Service实现
* @createDate 2024-06-06 21:36:44
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{

}




