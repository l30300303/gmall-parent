package com.liu.gmall.gateway.filter;
/*
 *@title UserAuthFilter
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 11:22
 */

import com.alibaba.fastjson.JSON;
import com.liu.gmall.gateway.properties.UrlPathProperties;
import com.liu.gmall.user.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class UserAuthFilter implements GlobalFilter, Ordered {


    @Autowired
    private UrlPathProperties urlPathProperties;

    //路径匹配器对象
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 拦截请求，做是否登录校验
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取真实请求路径
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //对不需要登录就能获取的资源进行放行
        List<String> noauthurls = urlPathProperties.getNoauthurls();
        for (String noauthurl : noauthurls) {
            if (antPathMatcher.match(noauthurl, path)) {
                return chain.filter(exchange);
            }
        }
        //处理需要登陆的资源
        List<String> authurls = urlPathProperties.getAuthurls();
        for (String authurl : authurls) {
            //判断资源是否需要登录
            if (antPathMatcher.match(authurl, path)) {
                String token = getToken(exchange);
                if (StringUtils.isEmpty(token)) {
                    return toLoginPage(exchange, chain);
                } else {
                    //携带了token获取用户数据
                    UserInfo userInfo = getUserInfo(token);
                    if (userInfo == null) {
                        return toLoginPage(exchange, chain);
                    } else {
                        //重新设置请求头，携带用户信息
                        return userIdThrough(exchange, chain, userInfo);
                    }
                }
            }
        }

        String token = getToken(exchange);
        //未登录直接放行，登录了的需要透传用户id
        if (StringUtils.isEmpty(token)) {
            return userTempIdThrough(exchange, chain);
        } else {
            UserInfo userInfo = getUserInfo(token);
            if (userInfo == null) {
                return toLoginPage(exchange, chain);
            } else {
                return userIdThrough(exchange, chain, userInfo);
            }
        }
    }

    //放行之前透传临时用户id
    private Mono<Void> userTempIdThrough(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取临时用户id
        String userTempId = getUserTempId(exchange);
        //透传临时用户id
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().header("userTempId",userTempId).build();
        ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).response(exchange.getResponse()).build();
        return chain.filter(serverWebExchange);
    }

    public String getUserTempId(ServerWebExchange exchange) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        HttpCookie cookie = httpRequest.getCookies().getFirst("userTempId");
        if (cookie != null) {
            return cookie.getValue();
        }else {
            return httpRequest.getHeaders().getFirst("userTempId");
        }
    }

    //该方法需要同时透传用户id和临时用户id，用于合并购物车内容
    private Mono<Void> userIdThrough(ServerWebExchange exchange, GatewayFilterChain chain, UserInfo userInfo) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        //获取临时用户id
        String userTempId = getUserTempId(exchange);
        ServerHttpRequest serverHttpRequest = httpRequest.mutate()
                .header("userId", String.valueOf(userInfo.getId()))
                .header("userTempId", userTempId)
                .build();
        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).response(exchange.getResponse()).build();
        return chain.filter(build);
    }

    //将用户重定向到登陆页面
    private Mono<Void> toLoginPage(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取当前请求路径
        ServerHttpRequest httpRequest = exchange.getRequest();
        String path = httpRequest.getURI().toString();
        ServerHttpResponse httpResponse = exchange.getResponse();
        //设置相应状态码
        httpResponse.setStatusCode(HttpStatus.FOUND);
        //设置location响应头
        httpResponse.getHeaders().set("location", urlPathProperties.getLoginpage() + "?originUrl=" + path);

        //如果token不合法，把cookie中的token删掉，避免无线重定向(将token值设置为空，相当于删除了token)
        ResponseCookie responseCookie = ResponseCookie.from("token", "").maxAge(-1).path("/").domain(".gmall.com").build();
        httpResponse.addCookie(responseCookie);

        return httpResponse.setComplete();
    }

    private UserInfo getUserInfo(String token) {
        String info = redisTemplate.opsForValue().get("user:login:" + token);
        if (StringUtils.isEmpty(info)) {
            return null;
        } else {
            return JSON.parseObject(info, UserInfo.class);
        }
    }

    private String getToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpCookie httpCookie = request.getCookies().getFirst("token");
        if (httpCookie != null) {
            return httpCookie.getValue();
        } else {
            return request.getHeaders().getFirst("token");
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
