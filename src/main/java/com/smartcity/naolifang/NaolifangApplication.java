package com.smartcity.naolifang;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@MapperScan("com.smartcity.naolifang.mapper")
public class NaolifangApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaolifangApplication.class, args);
    }

}
