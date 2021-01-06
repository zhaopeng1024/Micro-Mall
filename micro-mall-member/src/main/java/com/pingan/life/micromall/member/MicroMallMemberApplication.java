package com.pingan.life.micromall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.pingan.life.micromall.member.dao")
@EnableFeignClients(basePackages = "com.pingan.life.micromall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MicroMallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroMallMemberApplication.class, args);
    }

}
