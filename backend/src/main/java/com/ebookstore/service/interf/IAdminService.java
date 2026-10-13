package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.dto.request.LoginReq;
import com.ebookstore.dto.response.LoginResp;
import com.ebookstore.entity.Admin;

public interface IAdminService extends IService<Admin> {
    LoginResp login(LoginReq req);
}
