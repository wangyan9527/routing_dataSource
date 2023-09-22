package com.zeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MysqlWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlWebApplication.class, args);
    }

}
