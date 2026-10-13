package com.ebookstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ebookstore.mapper")
public class EBookStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(EBookStoreApplication.class, args);
    }
}