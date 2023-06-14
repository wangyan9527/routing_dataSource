package com.wy.config;

import com.wy.utils.RoutingCodeUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义路由规则
 *
 * @author wy
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingCodeUtils.getCode();
    }
}
