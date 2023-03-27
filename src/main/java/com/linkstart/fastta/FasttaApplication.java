package com.linkstart.fastta;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class FasttaApplication {
    public static void main(String[] args) {
        try{
            SpringApplication.run(FasttaApplication.class, args);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        log.info("Fastta 外卖平台系统启动成功!");
    }
}
