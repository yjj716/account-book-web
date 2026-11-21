package com.yjj.accountbook.controller;

import com.yjj.accountbook.common.JwtUtil;
import com.yjj.accountbook.common.Result;
import com.yjj.accountbook.entity.User;
import com.yjj.accountbook.entity.dto.UserLoginRequest;
import com.yjj.accountbook.entity.dto.UserRegisterRequest;
import com.yjj.accountbook.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    public UserController(UserMapper userMapper,JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/register")
    public Result<Integer> register(@Valid @RequestBody UserRegisterRequest reg) {
        if (userMapper.findByUsername(reg.getUsername()) != null) {
            return Result.fail(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(reg.getUsername());
        user.setPassword(encoder.encode(reg.getPassword()));
        userMapper.insert(user);
        return Result.ok(user.getId());
    }
    @PostMapping("/login")
    public Result<String>login(@Valid @RequestBody UserLoginRequest req){
        User user = userMapper.findByUsername(req.getUsername());
        if (user == null || !encoder.matches(req.getPassword(), user.getPassword())) {
            return Result.fail(401,"用户名或密码错误");
        }
        return Result.ok(jwtUtil.generate((user.getId())));
    }
}
