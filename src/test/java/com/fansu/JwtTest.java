package com.fansu;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testGen(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","admin");
        //生成token
        String token = JWT.create()
                .withClaim("user",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*3))//设置过期时间
                .sign(Algorithm.HMAC256("dafansu"));//指定算法生成密钥

        System.out.println(token);
    }
    @Test
    public void testParse(){
        //定义字符串用来模拟传递过来的token
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"+
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6ImFkbWluIn0sImV4cCI6MTczMjY0Mzc0Mn0"+
                ".3S9220AxBMBt7BVzcrJlqXFK2MbToUm2jnDANXzsvOE";
        //解密算法,调用。bulid（）方法构建密码验证器jwtVerifier
        // 解密算法和加密算法必须保持一致
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("dafansu")).build();
        DecodedJWT decodedJWT =  jwtVerifier.verify(token);//验证token，生成一个解密后的token对象赋给decodedJWT
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));
    }

}
