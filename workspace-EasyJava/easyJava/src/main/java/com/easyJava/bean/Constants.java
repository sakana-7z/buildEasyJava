package com.easyJava.bean;

import com.easyJava.utils.PropertiesUtils;
import com.easyJava.utils.StringUtils;

import java.util.List;
import java.util.Map;

public class Constants {
    /**
     * 参数相关
     */
    public static final Boolean IGNORE_TABLE_PREFIX; // 是否忽略表前缀
    public static final String SUFFIX_BEAN_PARAM; // 参数类后缀
    public static final String SUFFIX_BEAN_QUERY_FUZZY;
    public static final String SUFFIX_BEAN_QUERY_TIME_START;
    public static final String SUFFIX_BEAN_QUERY_TIME_END;
    public static final String SUFFIX_MAPPER;


    /**
     * 路径相关
     */
    private static final String PATH_JAVA = "java";
    private static final String PATH_RESOURCES = "resources";
    public static final String PATH_BASE; // 基础路径
    public static final String PACKAGE_BASE; // 包基础路径
    public static final String PATH_PO; // PO 路径
    public static final String PACKAGE_PO; // PO 包路径
    public static final String PATH_QUERY;
    public static final String PACKAGE_QUERY;
    public static final String PATH_RESULT;
    public static final String PACKAGE_RESULT;

    public static final String PATH_CONTROLLER;
    public static final String PACKAGE_CONTROLLER;
    public static final String PATH_SERVICE;
    public static final String PACKAGE_SERVICE;
    public static final String PATH_SERVICE_IMPL;
    public static final String PACKAGE_SERVICE_IMPL;
    public static final String PATH_MAPPER;
    public static final String PACKAGE_MAPPER;
    public static final String PATH_MAPPER_XML;

    //PACKAGE_MAPPER就等于PACKAGE_MAPPER_XML所以不写了



    /**
     * 其他
     */
    public static final String AUTHOR_NAME; // 作者名称





    static {
        // 初始化配置项
        AUTHOR_NAME = PropertiesUtils.getString("author.name");
        IGNORE_TABLE_PREFIX = Boolean.parseBoolean(PropertiesUtils.getString("ignore.table.prefix")); // 是否忽略表前缀
        SUFFIX_BEAN_PARAM = PropertiesUtils.getString("suffix.bean.param"); // 参数类后缀
        SUFFIX_BEAN_QUERY_FUZZY=PropertiesUtils.getString("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_TIME_START=PropertiesUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END=PropertiesUtils.getString("suffix.bean.query.time.end");
        SUFFIX_MAPPER = PropertiesUtils.getString("suffix.mapper");



        // 获取包基础路径
        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_RESULT = PACKAGE_BASE+"."+PropertiesUtils.getString("package.result");
        PACKAGE_CONTROLLER= PACKAGE_BASE+"."+PropertiesUtils.getString("package.controller");
        PACKAGE_SERVICE= PACKAGE_BASE+"."+PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL=PACKAGE_BASE+"."+PropertiesUtils.getString("package.service.impl");
        PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mapper");



        // 获取基础路径
        String pathBase = PropertiesUtils.getString("path.base");
        PATH_BASE = pathBase + "/" + PATH_JAVA;
        PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");
        PATH_QUERY = PATH_BASE + "/" + PACKAGE_QUERY.replace(".", "/");
        PATH_RESULT = PATH_BASE + "/" + PACKAGE_RESULT.replace(".", "/");
        PATH_CONTROLLER = PATH_BASE + "/" + PACKAGE_CONTROLLER.replace(".", "/");
        PATH_SERVICE = PATH_BASE + "/" + PACKAGE_SERVICE.replace(".", "/");
        PATH_SERVICE_IMPL = PATH_BASE + "/" + PACKAGE_SERVICE_IMPL.replace(".", "/");
        PATH_MAPPER = PATH_BASE + "/" + PACKAGE_MAPPER.replace(".", "/");
        PATH_MAPPER_XML = pathBase+"/resources/"+PACKAGE_MAPPER.replace(".","/");




    }



    /**
     * 在SQL中的类型
     */
    public static final String[] SQL_DATE_TIME_TYPES = {"datetime", "timestamp"}; // 时间类型
    public static final String[] SQL_DATE_TYPES = {"date"}; // 日期类型
    public static final String[] SQL_BIG_DECIMAL_TYPES = {"decimal", "numeric"}; // BigDecimal类型
    public static final String[] SQL_BOOLEAN_TYPES = {"bit", "boolean"}; // Boolean类型
    public static final String[] SQL_INT_TYPES = {"int", "integer"}; // Integer类型
    public static final String[] SQL_SHORT_TYPES = {"smallint"}; // Short类型
    public static final String[] SQL_LONG_TYPES = {"bigint"}; // Long类型
    public static final String[] SQL_BYTE_TYPES = {"tinyint"}; // Byte类型
    public static final String[] SQL_FLOAT_TYPES = {"float", "real"}; // Float类型
    public static final String[] SQL_DOUBLE_TYPES = {"double", "double precision"}; // Double类型
    public static final String[] SQL_STRING_TYPES = {"char", "varchar", "nchar", "nvarchar", "text", "ntext", "longtext", "longnvarchar", "clob", "blob", "bytea"}; // String类型
    public static final String[] SQL_AUTO_INCREMENT_TYPES = {
            "int identity", "integer identity", "bigint identity", "bigserial", "serial",
            "tinyint identity", "smallint identity", "mediumint identity", "bigint identity",
            "int auto_increment", "integer auto_increment", "bigint auto_increment", "bigserial", "serial",
            "tinyint auto_increment", "smallint auto_increment", "mediumint auto_increment", "bigint auto_increment"
    }; // 自增类型

   public static void main(String[] args) {
//        System.out.println(PATH_BASE);
//        System.out.println(PACKAGE_PO);
//        System.out.println(PATH_PO);
       System.out.println(PATH_MAPPER_XML);
       System.out.println(PACKAGE_MAPPER);
       //System.out.println(PK_ID_NAME);
   }
}

