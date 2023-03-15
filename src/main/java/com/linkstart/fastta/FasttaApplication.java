package com.linkstart.fastta;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FasttaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FasttaApplication.class, args);
        log.info("Fastta 外卖平台系统启动成功!");
    }
}
