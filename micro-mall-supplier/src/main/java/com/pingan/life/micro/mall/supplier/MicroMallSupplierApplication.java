package com.pingan.life.micro.mall.supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroMallSupplierApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroMallSupplierApplication.class, args);
    }

}
