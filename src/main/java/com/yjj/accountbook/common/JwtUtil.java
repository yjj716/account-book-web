package com.yjj.accountbook.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long expireMillis;
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expire-minutes}") long expireMinutes){
        this.key= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireMillis=expireMinutes*60*1000;
    }
    public String generate(Integer userId){
        Date now=new Date();
        return Jwts.builder().subject(String.valueOf(userId)).issuedAt(now)
                .expiration(new Date(now.getTime() + expireMillis)).signWith(key).compact();
    }
    public Integer  parseUserId(String token){
        String sub=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).
                getPayload().getSubject();
        return Integer.valueOf(sub);
    }
}
