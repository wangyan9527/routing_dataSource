package com.zeal.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.zeal.model.DataSourceDO;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * 数据源工厂类
 *
 * @author zeal
 */
public class DataSourceUtils {

    public static DataSource getDataSource(DataSourceDO dataSourceDO) {
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

    public static void close(DataSource dataSource) {
        if (dataSource instanceof DruidDataSource) {
            ((DruidDataSource) dataSource).close();
        }
    }
}
