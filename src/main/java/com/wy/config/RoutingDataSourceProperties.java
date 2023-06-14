package com.wy.config;

import com.wy.model.DataSourceDO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 自定义配置属性
 *
 * @author wy
 */
@Data
@ConfigurationProperties(prefix = "routing.datasource")
public class RoutingDataSourceProperties {

    private List<DataSourceDO> configList;

}
