package com.easyJava.utils;

public class StringUtils {

    //首字母大写
    public static String firstToUpperCase(String field) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0,1).toUpperCase()+field.substring(1);
    }

    //首字母小写
    public static String firstToLowerCase(String field) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0,1).toLowerCase()+field.substring(1);
    }

    public static String toUpperCaseFirstOne(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public static String uperCaseFirstLetter(String propertyName) {
        return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

    }

//    public static void main(String[] args) {
//        System.out.println(firstToLowerCase("Aaa"));
//    }
}
