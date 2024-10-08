package com.easyJava.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FieldInfo {
    private String FieldName;//字段名称
    private String propertyName;//属性名称
    private String sqlType;//数据库类型
    private String comment;//字段备注
    private Boolean AutoIncrement;//是否自增长
    private String javaType;//在java中的类型

}
