package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.dto.request.LoginReq;
import com.ebookstore.dto.request.RegisterReq;
import com.ebookstore.dto.response.LoginResp;
import com.ebookstore.entity.User;

public interface IUserService extends IService<User> {
    LoginResp login(LoginReq req);
    LoginResp register(RegisterReq req);
    void updateProfile(User user);
}
