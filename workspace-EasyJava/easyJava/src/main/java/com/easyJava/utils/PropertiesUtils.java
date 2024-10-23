package com.easyJava.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class PropertiesUtils {
    private static final Properties props = new Properties();
    private static final ConcurrentHashMap<String, String> PROPER_MAP = new ConcurrentHashMap<>();

    // 静态代码块，仅执行一次
    static {
        // 通过继承InputStream的子类对象从数据源读取字节流
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            if (is != null) {
                props.load(new java.io.InputStreamReader(is, StandardCharsets.UTF_8));
            }
            // 将配置文件内容放入map中
            for (Object o : props.keySet()) {
                String key = (String) o;
                PROPER_MAP.put(key, props.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getString(String key) {
        return PROPER_MAP.get(key);
    }

//    public static void main(String[] args) {
//        System.out.println(getString("db.driver.name"));
//    }
}
