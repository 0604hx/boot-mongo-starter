package org.nerve.web.security.jwt;

import com.alibaba.fastjson.JSON;
import org.nerve.auth.Account;
import org.nerve.web.security.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fan.jin on 2016-10-19.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtConfig config;

    private OrRequestMatcher skipPathMatcher;

    @PostConstruct
    protected void initMatch(){
        logger.debug("init JWT Filter matcher , skip path is {}", config.skipPath);
        List<RequestMatcher> m = config.skipPath.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        skipPathMatcher = new OrRequestMatcher(m);
    }

    /**
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("JWT Filter called: url={}", request.getRequestURI());
        String authToken = tokenHelper.getToken(request);

        if (authToken != null
                && SecurityContextHolder.getContext().getAuthentication() == null
                && !skipPathMatcher.matches(request)) {
            // get username from token
            try {
                String tokenData = tokenHelper.getSubjectFromToken(authToken);
                logger.debug("load {} from token {}", tokenData,authToken);

                if(StringUtils.hasText(tokenData)){
                    //从 token 中得到 Account 对象
                    Account account = JSON.parseObject(tokenData, Account.class);

                    // 根据配置来获取用户信息，从 DB 或者 token
                    UserDetails userDetails = config.filterWithDb ?
                            userDetailsService.loadUserByUsername(account.getName())
                            :
                            new LoginUser(account, account.getRoleList());

                    logger.debug("get user detail {}", JSON.toJSONString(userDetails));

                    // create authentication
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );

//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else
                    logger.debug("cloud not load username from token, may be token is expired!");
            } catch (Exception e) {
                logger.error("error on check token", e);
            }
        }

        chain.doFilter(request, response);
    }

}

