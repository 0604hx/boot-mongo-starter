package org.nerve.web.security.jwt;

import org.nerve.utils.encode.MD5Util;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * com.zeus.web.security.jwt
 * Created by zengxm on 2017/8/23.
 */
@Configuration
@ConfigurationProperties(prefix = "zeus.jwt")
public class JwtConfig {

    /**
     * jwt 发布者名称
     */
    String name = "ZEUS";

    /**
     * 签名密钥
     */
    String secret = MD5Util.getStringMD5(name);

    /**
     * token 有限期，单位：秒，默认 30 分钟
     */
    int expire = 1800;

    /**
     * Headers 的参数名，默认：Authorization
     */
    String header="Authorization";

    String cookie="AUTH_TOKEN";

    /**
     * 跳过的 url
     */
    List<String> skipPath = Arrays.asList(
            "/login","/logout",
            "/static/**","/public/**",
            "/index.html","/mobile.html",
            "/favicon.ico");

    /**
     * JWT filter 使用 DB 来获取数据
     * 如果为 true 则每次验证请求时都根据用户名去查询用户信息（对于性能要求较高的场景不推荐），可以得到最新的用户信息
     *
     * 默认 false （用户信息保存到 token 中）
     */
    boolean filterWithDb = false;

    public boolean isFilterWithDb() {
        return filterWithDb;
    }

    public JwtConfig setFilterWithDb(boolean filterWithDb) {
        this.filterWithDb = filterWithDb;
        return this;
    }

    public List<String> getSkipPath() {
        return skipPath;
    }

    public JwtConfig setSkipPath(List<String> skipPath) {
        this.skipPath = skipPath;
        return this;
    }

    public String getName() {
        return name;
    }

    public JwtConfig setName(String name) {
        this.name = name;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public JwtConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public int getExpire() {
        return expire;
    }

    public JwtConfig setExpire(int expire) {
        this.expire = expire;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public JwtConfig setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getCookie() {
        return cookie;
    }

    public JwtConfig setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }
}

