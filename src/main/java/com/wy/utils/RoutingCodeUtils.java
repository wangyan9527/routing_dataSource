package com.wy.utils;

/**
 * 保存数据源路由code的ThreadLocal
 *
 * @author wy
 */
public class RoutingCodeUtils {

    private static final ThreadLocal<String> DATA_SOURCE_CODE = new ThreadLocal<>();

    public static void setCode(String code) {
        DATA_SOURCE_CODE.set(code);
    }

    public static String getCode() {
        return DATA_SOURCE_CODE.get();
    }

    public static void clear() {
        DATA_SOURCE_CODE.remove();
    }

}
