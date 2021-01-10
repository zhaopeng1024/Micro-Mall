package com.pingan.life.micromall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 支持跨域访问的配置
 * 浏览器为了安全，默认采用同源策略来拒绝跨域请求，跨域指协议、域名、端口其中任何一个不同都会产生跨域
 * 浏览器会先发起预检请求（method=OPTIONS），当服务器响应允许跨域时，才会发起真实请求
 * 注意简单请求不会触发CORS预检请求，即content-type为text/plain、multipart/form-data或者application/x-www-form-urlencoded的请求
 * 均不会出发CORS，而content-type为application/json会出发CORS
 * 该配置类用于服务器响应允许跨域
 */
@Configuration
public class GatewayCorsConfiguration {

    private static final String[] METHODS = new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    @Bean
    public CorsWebFilter corsWebFilter(){
        //选用org.springframework.web.cors.reactive包下的，网关是webflux编程，响应式编程，
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowedMethods(Arrays.asList(METHODS));
        //允许携带cookie
        corsConfiguration.setAllowCredentials(true);
        //任意路径都要配置
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
