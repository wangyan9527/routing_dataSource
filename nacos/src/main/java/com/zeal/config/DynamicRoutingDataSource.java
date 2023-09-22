package com.zeal.config;

import com.zeal.factory.DataSourceFactory;
import com.zeal.model.DataSourceDO;
import com.zeal.utils.RoutingCodeUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自定义路由规则
 *
 * @author wy
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 路由和数据源配置的映射关系
     */
    private Map<String, DataSourceDO> dataSourceConfigMap = new HashMap<>();

    /**
     * 当数据源发生变化时，用于保存新变化的数据
     */
    private Map<String, DataSourceDO> changeDataSourceConfigMap;

    private Object defaultTargetDataSource;

    private Map<Object, DataSource> resolvedDataSources;

    private DataSource resolvedDefaultDataSource;

    public void setChangeDataSourceConfigMap(List<DataSourceDO> dataSourceDOList) {
        this.changeDataSourceConfigMap = dataSourceDOList.stream()
                .collect(Collectors.toMap(DataSourceDO::getCode, Function.identity(), (o, n) -> {
                    throw new RuntimeException("存在相同路由 [" + o.getCode() + "] 的数据源配置!");
                }));
    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    /**
     * 重写初始化方法
     */
    @Override
    public void afterPropertiesSet() {
        if (this.changeDataSourceConfigMap == null) {
            throw new IllegalArgumentException("路由数据源未配置！");
        }
        // 处理路由和数据源的映射
        convertResolvedDataSources();
        if (this.defaultTargetDataSource != null) {
            this.resolvedDefaultDataSource = resolveSpecifiedDataSource(this.defaultTargetDataSource);
        }
    }

    /**
     * 根据路由Key找到对应的数据源连接
     *
     * @return 数据源
     */
    @Override
    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "路由数据源未正确初始化！");
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = this.resolvedDataSources.get(lookupKey);
        if (dataSource == null && lookupKey == null) {
            dataSource = this.resolvedDefaultDataSource;
        }
        if (dataSource == null) {
            throw new IllegalStateException("路由键：[" + lookupKey + "]对应的数据源不存在！");
        }
        return dataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingCodeUtils.getCode();
    }

    private void convertResolvedDataSources() {
        if (this.resolvedDataSources == null) {
            // 容器启动时的初始化，此时resolvedDataSources为空（其它情况下，不再初始化该map）
            this.resolvedDataSources = new ConcurrentHashMap<>(this.changeDataSourceConfigMap.size());
        }
        // 筛选出变化了的数据源
        this.changeDataSourceConfigMap.forEach((key, value) -> {
            DataSourceDO dataSourceDO = dataSourceConfigMap.get(key);
            // 判断是否有新增的数据源和修改的数据源
            if (dataSourceDO == null) {
                System.out.println("新增数据源：" + value);
                // 如果有，则添加或覆盖之前的数据源
                convertAndPutDataSource(key, value);
            } else if (!dataSourceDO.equals(value)) {
                System.out.println("修改数据源： 原=>" + dataSourceDO + "; 新=>" + value);
                convertAndPutDataSource(key, value);
            }
        });
        // 筛选删除了的数据源
        Set<String> originalKeySet = dataSourceConfigMap.keySet();
        originalKeySet.removeAll(changeDataSourceConfigMap.keySet());
        originalKeySet.forEach(key -> {
            System.out.println("移除数据源：" + this.dataSourceConfigMap.get(key));
            this.resolvedDataSources.remove(key);
        });

        // 重置数据，将变化数据置空
        dataSourceConfigMap = changeDataSourceConfigMap;
        changeDataSourceConfigMap = null;
    }

    public void convertAndPutDataSource(String key, DataSourceDO value) {
        Object lookupKey = resolveSpecifiedLookupKey(key);
        DataSource dataSource = resolveSpecifiedDataSource(DataSourceFactory.getDataSource(value));
        this.resolvedDataSources.put(lookupKey, dataSource);
    }
}
