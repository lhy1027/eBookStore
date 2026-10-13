package com.ebookstore.controller.client;

import com.ebookstore.dto.request.LoginReq;
import com.ebookstore.dto.request.RegisterReq;
import com.ebookstore.dto.response.LoginResp;
import com.ebookstore.service.interf.IUserService;
import com.ebookstore.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req) {
        return Result.success(userService.login(req));
    }

    @PostMapping("/register")
    public Result<LoginResp> register(@Valid @RequestBody RegisterReq req) {
        return Result.success(userService.register(req));
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出成功");
    }
}