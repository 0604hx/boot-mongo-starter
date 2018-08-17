package org.nerve.web.security.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.nerve.auth.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * Created by fan.jin on 2016-10-19.
 */
@Component
public class TokenHelper {

    @Autowired
    JwtConfig config;

    @Autowired
    UserDetailsService userDetailsService;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    /**
     * 密码过滤器，不在 Token 中记录密码信息，而是用 **** 代替
     * 因为后续创建 Spring Security User 对象时，密码不能为空：详见：org.springframework.security.core.userdetails.User.<init>(User.java:106)
     */
    private ValueFilter passwordJSONFilter = (object, name, value) -> "password".equals(name)?"****":value;

    /**
     * 获取 token 的 subject 信息
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getSubjectFromToken(token);
    }

    public String getSubjectFromToken(String token){
        String data;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            data = claims.getSubject();
        } catch (Exception e) {
            data = null;
        }
        return data;
    }

    /**
     * 创建用户 token
     * @param data
     * @return
     */
    public String generateToken(String data) {
        return Jwts.builder()
                .setIssuer( config.name )
                .setSubject(data)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith( SIGNATURE_ALGORITHM, config.secret )
                .compact();
    }

    /**
     * 创建 Account Token（去除 Password 属性）
     * @param account
     * @return
     */
    public String generateToken(Account account){
        return generateToken(JSON.toJSONString(account, passwordJSONFilter));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(config.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(config.name)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith( SIGNATURE_ALGORITHM, config.secret )
                .compact();
    }

    /**
     * 验证此 token 能否刷新
     * 条件：
     * 1. 此 token 没有过期
     * 2. 此 token 创建日期早于上一次密码更新的日期
     * @param token
     * @param lastPwdResetDate
     * @return
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPwdResetDate) {
        try {
            final Claims claims = getClaimsFromToken(token);
            final Date expirationDate = claims.getExpiration();
            return expirationDate.compareTo(generateCurrentDate()) > 0      //验证是否过期
                    &&
                    (lastPwdResetDate==null || claims.getIssuedAt().after(lastPwdResetDate))   //验证 token 有效期间是否有重设过密码
                    ;
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.setIssuedAt(generateCurrentDate());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    private long getCurrentTimeMillis() {
        return new Date().getTime();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    private Date generateExpirationDate() {

        return new Date(getCurrentTimeMillis() + config.expire * 1000);
    }

    public String getToken(HttpServletRequest request ) {
        /**
         *  Getting the token from Cookie store
         */
        Cookie authCookie = getCookieValueByName( request, config.cookie );
        if ( authCookie != null ) {
            return authCookie.getValue();
        }
        /**
         *  Getting the token from Authentication header
         *  e.g Bearer your_token
         */
        String authHeader = request.getHeader(config.header);
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    /**
     * Find a specific HTTP cookie in a request.
     *
     * @param request
     *            The HTTP request object.
     * @param name
     *            The cookie name to look for.
     * @return The cookie, or <code>null</code> if not found.
     */
    public Cookie getCookieValueByName(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (int i = 0; i < request.getCookies().length; i++) {
            if (request.getCookies()[i].getName().equals(name)) {
                return request.getCookies()[i];
            }
        }
        return null;
    }
}

