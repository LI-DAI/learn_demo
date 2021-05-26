package com.learn.security.utils;

import com.learn.common.utils.SpringContextUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LD
 * @date 2021/3/2 16:21
 */
@Slf4j
public class JwtTokenUtil {

    private static Integer DEFAULT_EXPIRE = 2;

    private static final Long TOKEN_EXPIRATION = DEFAULT_EXPIRE * 60 * 60 * 1000L;

    /**
     * refresh_token 比 access_token 有效期多一个小时
     */
    private static final Long REFRESH_TOKEN_EXPIRATION = (DEFAULT_EXPIRE + 1) * 60 * 60 * 1000L;

    private static final String SECRET = "C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));
    private static final JwtParser PARSER = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();

    static {
        SpringContextUtil.addCallBack(() -> {
            try {
                DEFAULT_EXPIRE = SpringContextUtil.getProperties("security.config.token-validity-in-hours", DEFAULT_EXPIRE, Integer.class);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public static String createAccessToken(String sign) {
        return createToken(sign, TOKEN_EXPIRATION);
    }

    public static String createRefreshToken(String sign) {
        return createToken(sign, REFRESH_TOKEN_EXPIRATION);
    }

    public static String createToken(String sign, long expire) {
        return createToken(sign, "", expire);
    }

    public static String createToken(String sign, String authorities, long expire) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(System.currentTimeMillis() + expire);
        Map<String, String> claims = new HashMap<>(4);
        claims.put("username", sign);
        claims.put("authorities", authorities);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setIssuer("LD")
                .setIssuedAt(createdDate)
                .setSubject(sign)
                .setExpiration(expirationDate)
                .compact();
    }


    public static Map<String, Object> parseJwtToken(String token) {
        return getClaims(token);
    }

    public static boolean isExpiration(String token) {
        try {
            getClaims(token);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                log.warn("当前 token 已过期: {}", e.getMessage());
                return true;
            }
            log.error(e.getMessage(), e);
        }
        //未抛出异常直接返回未过期
        return false;
    }

    public static Claims getClaims(String token) {
        return PARSER.parseClaimsJws(token).getBody();
    }

    public static void main(String[] args) {
        String token = createAccessToken("test");
        System.out.println(token);
        System.out.println(isExpiration(token));
        System.out.println(parseJwtToken(token));
    }

}

