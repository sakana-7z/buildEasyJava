package com.easyJava.builder;

import com.easyJava.bean.FieldInfo;
import com.easyJava.utils.JsonUtils;
import com.easyJava.utils.PropertiesUtils;
import com.easyJava.bean.Constants;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BuildTable {
    //日志
    private static final Logger logger= LoggerFactory.getLogger(BuildTable.class);

    //数据库连接
    public static Connection conn=null;

    //查询表的状态，包括表名称、表备注等信息
    private static final String SQL_SHOW_TABLE_STATUS="show table status";

    //查询表的字段信息
    private static final String SQL_SHOW_TABLE_FIELDS="show full fields from %s";

    //查询表的索引信息
    private static final String SQL_SHOW_TABLE_INDEX="show index from %s";

    //初始化数据库连接
    static {
        String driverName= PropertiesUtils.getString("db.driver.name");
        String url= PropertiesUtils.getString("db.url");
        String user= PropertiesUtils.getString("db.username");
        String password= PropertiesUtils.getString("db.password");
        try{
            Class.forName(driverName);
            conn= DriverManager.getConnection(url,user,password);

        }catch (Exception e){
            logger.error("数据库连接失败",e);
        }

    }


    /**
     * 获取数据库中所有的表
     */
    public static List<TableInfo> getTables() {
        // 使用 try-with-resources 自动管理资源
        List<TableInfo> tableInfoList = null;
        try (PreparedStatement ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
             ResultSet tableResult = ps.executeQuery()) {

            tableInfoList = new ArrayList<>();

            // 遍历查询结果
            while (tableResult.next()) {
                // 获取表名
                String tableName = tableResult.getString("name");
                // 获取备注
                String comment = tableResult.getString("comment");

                // 处理表名
                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    int underscoreIndex = beanName.indexOf("_");
                    if (underscoreIndex != -1) {
                        beanName = beanName.substring(underscoreIndex + 1);
                    }
                }

                beanName = processField(beanName, true);

                // 封装表信息
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);

                // 读取字段信息
                readFieldInfo(tableInfo);

                // 读取索引信息
                readKeyIndexInfo(tableInfo);

                // 日志输出表信息
                //logger.info("表: {}", JsonUtils.ConvertToJson(tableInfo));

                // 将表信息添加到列表中
                tableInfoList.add(tableInfo);
            }

        } catch (SQLException e) {
            // 记录异常信息
            logger.error("读取表失败", e);
        }

        return tableInfoList;
    }


    /**
     * 读取表的字段信息
     */
    private static void readFieldInfo(TableInfo tableInfo) {
        // 使用 try-with-resources 自动管理资源
        try (PreparedStatement ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
             ResultSet fieldResult = ps.executeQuery()) {

            List<FieldInfo> fieldInfoList = new ArrayList<>();
            boolean haveDateTime = false;
            boolean haveDate = false;
            boolean haveBigDecimal = false;

            // 遍历查询结果
            while (fieldResult.next()) {
                // 获取字段名
                String field = fieldResult.getString("field");
                // 获取数据库类型
                String type = fieldResult.getString("type");
                // 获取备注
                String comment = fieldResult.getString("comment");
                // 获取主键标识
                String key = fieldResult.getString("key");
                // 获取其他信息
                String extra = fieldResult.getString("extra");

                // 去掉括号及其内容
                type = type.replaceAll("\\(.*\\)", "");

                // 属性转驼峰命名
                String propertyName = StringUtils.firstToLowerCase(field);

                // 封装字段信息
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo);

                fieldInfo.setFieldName(field);
                fieldInfo.setPropertyName(processField(propertyName, false));
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra)); // 是否自增
                fieldInfo.setJavaType(processJavaType(type)); // 转换数据库类型到 Java 类型

                // 可选的日志输出
                // logger.info("field:{}, propertyName:{}, javaType:{}", field, fieldInfo.getPropertyName(), fieldInfo.getJavaType());

                // 判断是否有时间类型
                haveDateTime |= ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type);

                // 判断是否有日期类型
                haveDate |= ArrayUtils.contains(Constants.SQL_DATE_TYPES, type);

                // 判断是否有 BigDecimal 类型
                haveBigDecimal |= ArrayUtils.contains(Constants.SQL_BIG_DECIMAL_TYPES, type);
            }

            // 设置字段列表
            tableInfo.setFieldList(fieldInfoList);

            // 设置最终判断结果
            tableInfo.setHaveDateTime(haveDateTime);
            tableInfo.setHaveDate(haveDate);
            tableInfo.setHaveBigDecimal(haveBigDecimal);

        } catch (SQLException e) {
            // 记录异常信息
            logger.error("读取表失败", e);
        }
    }



    /**
     * 读取索引信息
     */
    private static void readKeyIndexInfo(TableInfo tableInfo) {
        // 使用 try-with-resources 自动管理资源
        try (PreparedStatement ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
             ResultSet fieldResult = ps.executeQuery()) {

            // 预先构建一个映射，将列名映射到 FieldInfo 对象
            Map<String, FieldInfo> fieldInfoMap = tableInfo.getFieldList().stream()
                    .collect(Collectors.toMap(FieldInfo::getFieldName, fieldInfo -> fieldInfo));

            // 遍历查询结果
            while (fieldResult.next()) {
                // 获取索引名
                String keyName = fieldResult.getString("key_name");
                // 获取是否唯一标识
                Integer nonUnique = fieldResult.getInt("non_unique");
                // 获取列名
                String columnName = fieldResult.getString("column_name");

                // 跳过唯一索引
                if (nonUnique == 1) {
                    continue;
                }

                // 封装索引信息
                // 根据索引名获取或创建一个空列表
                List<FieldInfo> keyFieldInfoList = tableInfo.getKeyIndexMap().computeIfAbsent(keyName, k -> new ArrayList<>());

                // 从映射中直接获取 FieldInfo 对象
                FieldInfo fieldInfo = fieldInfoMap.get(columnName);
                if (fieldInfo == null) {
                    throw new NoSuchElementException("Column not found: " + columnName);
                }

                // 将找到的 FieldInfo 添加到列表中
                keyFieldInfoList.add(fieldInfo);

                // 可选的日志输出
                // logger.info("key:{},column:{}", keyName, columnName);
            }
        } catch (SQLException e) {
            // 记录异常信息
            logger.error("读取索引失败", e);
        }
    }




    /**
     * 转换字段命名格式
     */
    public static String processField(String field,boolean isCapital){
        //默认转换为驼峰命名
        //StringBuffer
        StringBuffer sb=new StringBuffer();

        String[] fields=field.split("_");
        sb.append(isCapital ? StringUtils.firstToUpperCase(fields[0]): fields[0]);

        for (int i=1;i<fields.length;i++){
            sb.append(StringUtils.firstToUpperCase(fields[i]));
        }
        return sb.toString();

    }


    /**
     *转换javatype
     */
    private static String processJavaType(String sqlType) {
        if (ArrayUtils.contains(Constants.SQL_INT_TYPES, sqlType) || "int unsigned".equals(sqlType)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_SHORT_TYPES, sqlType)) {
            return "Short";
        } else if (ArrayUtils.contains(Constants.SQL_FLOAT_TYPES, sqlType)) {
            return "Float";
        } else if (ArrayUtils.contains(Constants.SQL_DOUBLE_TYPES, sqlType)) {
            return "Double";
        } else if (ArrayUtils.contains(Constants.SQL_BIG_DECIMAL_TYPES, sqlType)) {
            return "BigDecimal";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, sqlType)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, sqlType)) {
            return "Timestamp";
        } else if (ArrayUtils.contains(Constants.SQL_BOOLEAN_TYPES, sqlType)) {
            return "Boolean";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES, sqlType)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_BYTE_TYPES, sqlType)) {
            return "Byte";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, sqlType)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_AUTO_INCREMENT_TYPES, sqlType)) {
            return "Long";
        } else if ("tinyint unsigned".equals(sqlType)) {
            return "Integer"; // 添加对 tinyint unsigned 的支持
        } else {
            throw new RuntimeException("无法识别的类型：" + sqlType);
        }
    }


}
