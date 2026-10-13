package com.ebookstore.controller.admin;

import com.ebookstore.annotation.Log;
import com.ebookstore.dto.request.LoginReq;
import com.ebookstore.dto.response.LoginResp;
import com.ebookstore.service.interf.IAdminService;
import com.ebookstore.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final IAdminService adminService;

    @Log("管理员登录")
    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq req) {
        return Result.success(adminService.login(req));
    }
}
