package com.wy.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.wy.model.DataSourceDO;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由数据源配置类
 *
 * @author wy
 */
@Data
@EnableConfigurationProperties(RoutingDataSourceProperties.class)
@Configuration
//@Component
//@ConfigurationProperties(prefix = "routing.datasource")
public class RoutingDataSourceConfig {

    private final RoutingDataSourceProperties routingDataSourceProperties;

    private static final String DEFAULT = "default";

    private List<DataSourceDO> configList;

    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public DynamicRoutingDataSource buildDynamicRoutingDataSource() {
        List<DataSourceDO> configList = routingDataSourceProperties.getConfigList();
        Map<Object, Object> targetDataSources = new HashMap<>();
        configList.forEach(config -> {
            targetDataSources.put(config.getCode(), buildDataSource(config));
        });
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(dataSource());
        dynamicDataSource.setLenientFallback(false);
        return dynamicDataSource;
    }

    private DataSource buildDataSource(DataSourceDO dataSourceDO) {
        DataSource dataSource = DataSourceBuilder.create()
                .type(dataSourceDO.getType())
                .driverClassName(dataSourceDO.getDriverClassName())
                .url(dataSourceDO.getUrl())
                .username(dataSourceDO.getUsername())
                .password(dataSourceDO.getPassword())
                .build();
        if (dataSourceDO.getType().isAssignableFrom(DruidDataSource.class)) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            druidDataSource.setInitialSize(dataSourceDO.getInitialSize());
            druidDataSource.setMinIdle(dataSourceDO.getMinIdle());
            druidDataSource.setMaxActive(dataSourceDO.getMaxActive());
        }
        return dataSource;
    }

}
