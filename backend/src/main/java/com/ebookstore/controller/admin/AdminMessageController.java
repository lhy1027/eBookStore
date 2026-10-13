package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.Message;
import com.ebookstore.entity.User;
import com.ebookstore.mapper.UserMapper;
import com.ebookstore.service.interf.IMessageService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMessageController {

    private final IMessageService messageService;
    private final UserMapper userMapper;

    @GetMapping
    public Result<IPage<Message>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<Message> result = messageService.page(new Page<>(page, size),
                new QueryWrapper<Message>().orderByDesc("create_time"));
        // 填充用户名
        List<Message> records = result.getRecords();
        if (!records.isEmpty()) {
            List<Long> userIds = records.stream().map(Message::getUserId).distinct().collect(Collectors.toList());
            Map<Long, String> usernameMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, User::getUsername));
            for (Message m : records) {
                m.setUsername(usernameMap.getOrDefault(m.getUserId(), "未知"));
            }
        }
        return Result.success(result);
    }

    @PutMapping("/{id}/reply")
    @Log("回复留言")
    public Result<String> reply(@PathVariable Long id, @RequestBody Map<String, String> params) {
        messageService.replyMessage(id, params.get("reply"));
        return Result.success("回复成功");
    }

    @DeleteMapping("/{id}")
    @Log("删除留言")
    public Result<String> delete(@PathVariable Long id) {
        messageService.removeById(id);
        return Result.success("删除成功");
    }
}
