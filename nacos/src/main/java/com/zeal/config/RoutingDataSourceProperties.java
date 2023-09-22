package com.zeal.config;

import com.zeal.model.DataSourceDO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 自定义配置属性
 *
 * @author wy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "routing.datasource")
public class RoutingDataSourceProperties {

    private List<DataSourceDO> configList;

}
