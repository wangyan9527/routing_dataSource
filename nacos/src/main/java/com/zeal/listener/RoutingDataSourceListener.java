package com.zeal.listener;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.spring.util.NacosUtils;
import com.zeal.config.DynamicRoutingDataSource;
import com.zeal.config.RoutingDataSourceProperties;
import com.zeal.factory.DataSourceFactory;
import com.zeal.model.DataSourceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoutingDataSourceListener {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @NacosConfigListener(groupId = "DEFAULT_GROUP", dataId = "dataSource.yml", type = ConfigType.YAML)
    public void listener(String msg) {
        List<DataSourceDO> dataSourceDOList = convertToDataSourceProperties(msg).getConfigList();
        // 根据内容进行调整修改，这里上锁防止并发修改时产生脏数据
        synchronized (this) {
            Map<String, DataSourceDO> changeDataSourceConfigMap = dataSourceDOList.stream().collect(
                    Collectors.toMap(DataSourceDO::getCode, Function.identity(), (o, n) -> n));
            dynamicRoutingDataSource.setChangeDataSourceConfigMap(changeDataSourceConfigMap);
            dynamicRoutingDataSource.afterPropertiesSet();
        }
    }

    /**
     * 将消息转为dataSource实体类
     *
     * @param msg 消息
     * @return 路由配置实体类
     */
    public RoutingDataSourceProperties convertToDataSourceProperties(String msg) {
        Map<String, Object> properties = NacosUtils.toProperties(msg, ConfigType.YAML.getType());
        List<ConfigurationPropertySource> propertySources = Collections.singletonList(new MapConfigurationPropertySource(properties));
        return new Binder(propertySources).bindOrCreate("routing.datasource", Bindable.of(RoutingDataSourceProperties.class));
    }

}
