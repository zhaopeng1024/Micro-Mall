package com.pingan.life.micromall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.pingan.life.micromall.product.dao")
@EnableFeignClients(basePackages = "com.pingan.life.micromall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MicroMallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroMallProductApplication.class, args);
    }

}
