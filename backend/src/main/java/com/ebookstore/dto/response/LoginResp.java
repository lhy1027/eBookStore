package com.ebookstore.dto.response;

import lombok.Data;

@Data
public class LoginResp {
    private String token;
    private Long userId;
    private String username;
    private String role;
}
