package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.dto.request.LoginReq;
import com.ebookstore.dto.response.LoginResp;
import com.ebookstore.entity.Admin;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.AdminMapper;
import com.ebookstore.service.interf.IAdminService;
import com.ebookstore.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResp login(LoginReq req) {
        Admin admin = getOne(new QueryWrapper<Admin>().eq("username", req.getUsername()));
        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        if (!passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String role = admin.getRole() != null ? admin.getRole().replace("ROLE_", "") : "ADMIN";
        String token = jwtUtil.generateToken(admin.getId(), role);
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUserId(admin.getId());
        resp.setUsername(admin.getUsername());
        resp.setRole(role);
        return resp;
    }
}
