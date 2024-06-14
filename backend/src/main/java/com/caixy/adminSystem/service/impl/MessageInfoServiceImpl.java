package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.mapper.MessageInfoMapper;
import com.caixy.adminSystem.model.entity.domain.MessageInfo;
import com.caixy.adminSystem.model.enums.BackendMessageLevelEnum;
import com.caixy.adminSystem.service.MessageInfoService;
import org.springframework.stereotype.Service;

/**
 * @author CAIXYPROMISE
 * @description 针对表【message_info(站内消息信息表)】的数据库操作Service实现
 * @createDate 2024-06-14 21:09:17
 */
@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo>
        implements MessageInfoService
{
    private static final Long SYSTEM_MSG_CODE = 0L;

    /**
     * 设置文件上传过期信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/14 下午9:27
     */
    @Override
    public void sendSystemMessage(Long orderId, Long forUser, String content, BackendMessageLevelEnum levelEnum)
    {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent(content);
        messageInfo.setLevel(levelEnum.getCode());
        messageInfo.setForUser(forUser);
        messageInfo.setFromUser(CommonConstant.SYSTEM_ID_CODE);
        messageInfo.setOrderId(orderId);
        this.save(messageInfo);
    }
}




