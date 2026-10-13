package com.ebookstore.controller.client;

import com.ebookstore.entity.Message;
import com.ebookstore.service.interf.IMessageService;
import com.ebookstore.util.SecurityUtil;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/messages")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Result<List<Message>> list() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(messageService.getUserMessages(userId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Result<String> add(@RequestBody Message message) {
        Long userId = SecurityUtil.getCurrentUserId();
        messageService.addMessage(message, userId);
        return Result.success("留言成功");
    }
}