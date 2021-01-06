package com.pingan.life.micromall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.pingan.life.micromall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MicroMallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroMallProductApplication.class, args);
    }

}
