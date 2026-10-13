package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.dto.request.LoginReq;
import com.ebookstore.dto.request.RegisterReq;
import com.ebookstore.dto.response.LoginResp;
import com.ebookstore.entity.User;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.UserMapper;
import com.ebookstore.service.interf.IUserService;
import com.ebookstore.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResp login(LoginReq req) {
        User user = getOne(new QueryWrapper<User>().eq("username", req.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), "USER");
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRole("USER");
        return resp;
    }

    @Override
    public LoginResp register(RegisterReq req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }
        User exist = getOne(new QueryWrapper<User>().eq("username", req.getUsername()));
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setStatus(1);
        save(user);
        String token = jwtUtil.generateToken(user.getId(), "USER");
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRole("USER");
        return resp;
    }

    @Override
    public void updateProfile(User user) {
        updateById(user);
    }
}
