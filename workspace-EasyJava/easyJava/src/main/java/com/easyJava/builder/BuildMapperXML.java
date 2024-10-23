package com.easyJava.builder;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuildMapperXML {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXML.class);

    /**
     * 执行生成 Mapper XML 文件的操作
     *
     * @param tableInfo 表信息对象
     * @throws Exception 如果生成过程中发生异常
     */
    public static void execute(TableInfo tableInfo) throws Exception {
        // 创建 Mapper XML 文件所在的目录
        File folder = new File(Constants.PATH_MAPPER_XML);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 构建 Mapper XML 文件的类名和文件路径
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File mapperFile = new File(folder, className + ".xml");

        try (OutputStream out = Files.newOutputStream(mapperFile.toPath());
             OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outw)) {

            // 写入 XML 文件的头部信息
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPER + "." + className + "\">\n");
            bw.newLine();

            // 写入实体映射的注释
            bw.write("    <!--实体映射-->");
            bw.newLine();
            bw.write("    <resultMap type=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\" id=\"" + tableInfo.getBeanName() + "ResultMap\">");
            bw.newLine();

            // 获取主键信息
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            List<FieldInfo> keyFields = keyIndexMap.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            String keyNames = keyFields.stream()
                    .map(FieldInfo::getFieldName)
                    .collect(Collectors.joining(" AND "));
            String placeholders = keyFields.stream()
                    .map(field -> "#{ " + field.getFieldName() + " }")
                    .collect(Collectors.joining(", "));
            String keyType = keyFields.isEmpty() ? null : keyFields.get(0).getJavaType();
            String keyName = keyFields.isEmpty() ? null : keyFields.get(0).getFieldName();


            // 遍历表中的所有字段找出主键写入 XML 文件
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (Objects.equals(keyName, fieldInfo.getFieldName())) {
                    // 写入字段的注释
                    BuildComment.createFieldCommentInXml(bw, fieldInfo.getComment());

                    bw.write("        <id column=\"" + tableInfo.getTableName() + "." + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                    bw.newLine();
                    break;
                }
            }
            // 遍历表中的所有字段找出非主键写入 XML 文件
            for (FieldInfo fieldInfo : tableInfo.getFieldList()){
                if (!Objects.equals(keyName, fieldInfo.getFieldName())){
                    // 写入字段的注释
                    BuildComment.createFieldCommentInXml(bw, fieldInfo.getComment());

                    bw.write("        <result column=\"" +tableInfo.getTableName()+"." + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                    bw.newLine();
                }

            }
            bw.write("    </resultMap>");
            bw.newLine();
            bw.newLine();

            // 生成查询方法
            bw.write("    <!--查询-->");
            bw.newLine();
            bw.write("    <select id=\"selectBy" + StringUtils.firstToUpperCase(keyName) + "\" resultType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\" parameterType=\"java.lang." + keyType + "\">");
            bw.newLine();
            bw.write("        SELECT * FROM " + tableInfo.getTableName() + " WHERE " + keyName + " = #{" + keyName + "}");
            bw.newLine();
            bw.write("    </select>");
            bw.newLine();
            bw.newLine();


            bw.write("    <!--动态查询-->");
            bw.newLine();
            bw.write("    <select id=\"selectList\" resultType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            bw.write("        SELECT * FROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("        <where>");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("            <if test=\"" + StringUtils.firstToLowerCase(tableInfo.getBeanName())+"."+fieldInfo.getPropertyName() + " != null\"> AND " + fieldInfo.getFieldName() + " = #{" +StringUtils.firstToLowerCase(tableInfo.getBeanName())+"."+ fieldInfo.getPropertyName() + "}</if>");
                bw.newLine();
            }
            bw.write("        </where>");
            bw.newLine();
            bw.write("    </select>");
            bw.newLine();
            bw.newLine();

            // 生成查询全部
            bw.write("    <!--查询全部-->");
            bw.newLine();
            bw.write("    <select id=\"selectAll\" resultType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.write("        SELECT * FROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("    </select>");
            bw.newLine();
            bw.newLine();


            // 生成更新方法
            bw.write("    <!--更新-->");
            bw.newLine();
            bw.write("    <update id=\"updateBy"+StringUtils.firstToUpperCase(keyName)+"\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            bw.write("        UPDATE " + tableInfo.getTableName());
            bw.newLine();
            bw.write("        <set>");
            bw.newLine();
            // 遍历表中的所有字段找出非主键写入 XML 文件
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!Objects.equals(keyName, fieldInfo.getFieldName())){
                    bw.write("            <if test=\"" +StringUtils.firstToLowerCase(tableInfo.getBeanName()) +"."+ fieldInfo.getPropertyName() + " != null\">" + fieldInfo.getFieldName() + " = #{" +StringUtils.firstToLowerCase(tableInfo.getBeanName()) +"."+ fieldInfo.getPropertyName() + "},</if>");
                    bw.newLine();
                }
            }

            bw.write("        </set>");
            bw.newLine();
            bw.write("        WHERE " + keyName + " = #{" +StringUtils.firstToLowerCase(tableInfo.getBeanName()) +"."+keyName + "}");
            bw.newLine();
            bw.write("    </update>");
            bw.newLine();
            bw.newLine();


            // 生成插入方法
            bw.write("    <!--插入-->");
            bw.newLine();
            bw.write("    <insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            bw.write("        INSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            bw.write("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("            <if test=\"" + fieldInfo.getPropertyName() + " != null\">" + fieldInfo.getFieldName() + ",</if>");
                bw.newLine();
            }
            bw.write("        </trim>");
            bw.newLine();
            bw.write("        VALUES");
            bw.newLine();
            bw.write("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("            <if test=\"" + fieldInfo.getPropertyName() + " != null\">#{ " + fieldInfo.getPropertyName() + " },</if>");
                bw.newLine();
            }
            bw.write("        </trim>");
            bw.newLine();
            bw.write("    </insert>");
            bw.newLine();
            bw.newLine();



            // 生成删除方法
            bw.write("    <!--删除-->");
            bw.newLine();
            bw.write("    <delete id=\"deleteBy" + StringUtils.firstToUpperCase(keyName) + "\" parameterType=\"java.lang." + keyType + "\">");
            bw.newLine();
            bw.write("        DELETE FROM " + tableInfo.getTableName() + " WHERE " + keyName + " = #{" + keyName + "}");
            bw.newLine();
            bw.write("    </delete>");
            bw.newLine();
            bw.newLine();


            bw.write("    <!--批量删除-->");
            bw.newLine();
            bw.write("    <delete id=\"deleteBy" + StringUtils.firstToUpperCase(keyName) + "s\" parameterType=\"java.util.List\">");
            bw.newLine();
            bw.write("        DELETE FROM " + tableInfo.getTableName() + " WHERE " + keyName + " IN ");
            bw.newLine();
            bw.write("        <foreach item=\"item\" index=\"index\" collection=\""+keyName+"\" open=\"(\" separator=\",\" close=\")\">");
            bw.newLine();
            bw.write("        #{item}");
            bw.newLine();
            bw.write("        </foreach>");
            bw.newLine();
            bw.write("    </delete>");
            bw.newLine();
            bw.newLine();



            // 写入 XML 文件的尾部信息
            bw.newLine();
            bw.write("</mapper>\n");

        } catch (Exception e) {
            // 记录生成 Mapper XML 文件失败的日志
            logger.error("生成 Mapper.xml 失败", e);
            throw e; // 重新抛出异常
        }

        // 记录生成 Mapper XML 文件成功的日志
        logger.info("生成 Mapper.xml 成功：{}", mapperFile.getAbsolutePath());
    }
}
