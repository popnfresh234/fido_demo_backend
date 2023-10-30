package com.example.apilogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiLoginApplication.class, args);
    }

}
