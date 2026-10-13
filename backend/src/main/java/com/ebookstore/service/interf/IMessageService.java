package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Message;
import java.util.List;

public interface IMessageService extends IService<Message> {
    List<Message> getUserMessages(Long userId);
    void addMessage(Message message, Long userId);
    void replyMessage(Long id, String reply);
}
