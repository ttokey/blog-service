package com.ttokey.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
public class TtokeyBlogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtokeyBlogServiceApplication.class, args);
    }

}
