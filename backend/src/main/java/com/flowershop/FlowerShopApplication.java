package com.flowershop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.flowershop.module.*.mapper")
public class FlowerShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerShopApplication.class, args);
    }
}
