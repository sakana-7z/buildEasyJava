package com.easyJava.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    private String tableName;//表名
    private String beanName;//bean名称
    private String beanParamName;//参数名称
    private String comment;//表注释
    private List<FieldInfo> fieldList;//字段信息
    private Map<String, List<FieldInfo>> keyIndexMap=new LinkedHashMap<>();//唯一索引集合
    private Boolean haveDate;//是否有date类型
    private Boolean haveDateTime;//是否有时间类型
    private Boolean haveBigDecimal;//是否有bigDecimal类型


}
