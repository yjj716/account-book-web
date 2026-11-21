package com.yjj.accountbook.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegisterRequest {
@NotBlank(message="用户名不能为空")
@Size(max = 30,min=4,message="用户名长度 4-30")
    private String username;
@NotNull(message = "密码不能为空")
@Size(min = 6, max = 20, message="密码长度 6-20")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
