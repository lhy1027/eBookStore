package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.Message;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.MessageMapper;
import com.ebookstore.service.interf.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Override
    public List<Message> getUserMessages(Long userId) {
        return list(new QueryWrapper<Message>().eq("user_id", userId)
                .orderByDesc("create_time"));
    }

    @Override
    public void addMessage(Message message, Long userId) {
        message.setUserId(userId);
        save(message);
    }

    @Override
    public void replyMessage(Long id, String reply) {
        Message message = getById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }
        message.setReply(reply);
        message.setReplyTime(LocalDateTime.now());
        updateById(message);
    }
}
