package com.zeal.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.zeal.model.DataSourceDO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

/**
 * 路由数据源配置类
 *
 * @author wy
 */
@Data
@EnableConfigurationProperties(RoutingDataSourceProperties.class)
@Configuration
public class RoutingDataSourceConfig {

    private final RoutingDataSourceProperties routingDataSourceProperties;

    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public DynamicRoutingDataSource buildDynamicRoutingDataSource() {
        List<DataSourceDO> configList = routingDataSourceProperties.getConfigList();
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();

        // 设置路由数据源配置
        dynamicDataSource.setChangeDataSourceConfigMap(configList);

        // 设置主库数据源配置
        dynamicDataSource.setDefaultTargetDataSource(dataSource());
        return dynamicDataSource;
    }

}
