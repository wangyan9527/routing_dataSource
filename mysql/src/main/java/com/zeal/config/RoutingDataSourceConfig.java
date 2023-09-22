package com.zeal.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.zeal.model.DataSourceDO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
        dynamicDataSource.setLenientFallback(true); // 找不到key对应数据源时，走默认数据源查询
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
