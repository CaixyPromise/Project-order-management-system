package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.entity.MessageInfo;
import com.caixy.adminSystem.model.enums.BackendMessageLevelEnum;

/**
 * @author CAIXYPROMISE
 * @description 针对表【message_info(站内消息信息表)】的数据库操作Service
 * @createDate 2024-06-14 21:09:17
 */
public interface MessageInfoService extends IService<MessageInfo>
{

    void sendSystemMessage(String subject, Long orderId, Long forUser, String content, BackendMessageLevelEnum levelEnum);
}
