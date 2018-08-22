package org.nerve.web.security;

import org.nerve.web.security.jwt.TokenAuthenticationFilter;
import org.nerve.web.security.captcha.CaptchaFilter;
import org.apache.commons.lang3.StringUtils;
import org.nerve.web.security.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.nerve.web.security.AuthConfig.*;

/**
 * com.zeus.web.security
 * Created by zengxm on 2017/8/23.
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name ="nerve.security",matchIfMissing = true,havingValue = "true")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private UserDetailsService userDetailS;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug("[AUTH] ------------ START SETUP ------------");
        http.headers().contentTypeOptions().disable();

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(String.format("%s%s%s", authConfig.statics, SPLIT, authConfig.popular).split(SPLIT)).permitAll();

        logger.debug("[AUTH] set permitAll for {},{}", authConfig.statics, authConfig.popular);

        if(authConfig.map != null){
            authConfig.map.forEach((k,v)->{
                try {
                    http.authorizeRequests()
                            .antMatchers(k).hasAnyAuthority(StringUtils.split(v, SPLIT));
                    logger.debug("[AUTH] register ：{} for anyAuthority: {} ",k,v);
                } catch (Exception e) {
                    logger.error("[AUTH] error on register anyAuthority:{}={}",k,v);
                }
            });
        }

        http.authorizeRequests().anyRequest().authenticated();

        logger.debug("[AUTH] remember me : {}", authConfig.rememberMe);
        //开启remember-me
        if(authConfig.rememberMe){
            logger.debug("[AUTH] open RememberMe...");
            http.rememberMe()
                    .rememberMeCookieName(authConfig.cookie)
                    .tokenValiditySeconds(authConfig.expire)
                    .tokenRepository(persistentTokenRepository());
        }

        http
                .logout()
                .logoutSuccessHandler(new ExitSuccessHandler())
                .permitAll();

        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable();
        http.headers().frameOptions().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(new LoginRequiredEntryPoint())
                .accessDeniedHandler(new JsonAccessDeniedHandler());

		/*
		登录设置
		 */
        http.formLogin()
                .loginProcessingUrl(authConfig.loginPostPage)
                .loginPage(authConfig.loginPage)
                .failureHandler(new LoginFailHandler())
                .successHandler(loginSuccessHandler)
        ;
        logger.debug("[AUTH] loginPage={}, loginProcessingUrl={}", authConfig.loginPage, authConfig.loginPostPage);

        if(authConfig.captcha.isEnable()){
            logger.debug("[AUTH] config captcha code for login check....");

            CaptchaFilter captchaFilter = new CaptchaFilter(authConfig);
            http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        }

        //jwt 相关设置
        http
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        logger.debug("[AUTH] ------------ FINISH SETUP ------------");
    }

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailS)
                .passwordEncoder(passwordEncoder());
    }

    //如果采用持久化 token 的方法则需要指定保存token的方法
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new MongoTokenRepositoryImpl();
    }
}
