package com.zeal.model;

import lombok.Data;

import javax.sql.DataSource;

/**
 * 数据源配置信息
 *
 * @author wy
 */
@Data
public class DataSourceDO {

    private String code;

    private Class<? extends DataSource> type;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxActive;

    @SuppressWarnings("unchecked")
    public void setType(String type) throws ClassNotFoundException {
        this.type = (Class<? extends DataSource>) Class.forName(type);
    }
}
