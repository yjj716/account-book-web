package com.yjj.accountbook.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;



@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    public static final String ATTR_USER_ID = "userId";
    public LoginInterceptor(JwtUtil jwtUtil){this.jwtUtil=jwtUtil;}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
    ,Object handler)
throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            write401(response, "未登录");
            return false;
        }
        String token = auth.substring(7);
        try {
            Integer userId = jwtUtil.parseUserId(token);
            request.setAttribute(ATTR_USER_ID, userId);
            return true;
        } catch (Exception e) {
            write401(response, "登录已过期或无效，请重新登录");
            return false;
        }
    }

    private void write401(HttpServletResponse response,String msg)throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
    }
}
