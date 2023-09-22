package com.zeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NacosWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosWebApplication.class, args);
    }

}
