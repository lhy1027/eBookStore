package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.User;
import com.ebookstore.mapper.UserMapper;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserMapper userMapper;

    @GetMapping
    public Result<IPage<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("username", keyword).or().like("phone", keyword);
        }
        wrapper.orderByDesc("create_time");
        wrapper.select("id", "username", "phone", "email", "status", "create_time");
        return Result.success(userMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PutMapping("/{id}/status")
    @Log("禁用/启用会员")
    public Result<String> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setStatus(params.get("status"));
            userMapper.updateById(user);
        }
        return Result.success("操作成功");
    }
}