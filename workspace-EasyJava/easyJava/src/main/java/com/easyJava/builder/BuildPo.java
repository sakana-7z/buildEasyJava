package com.easyJava.builder;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class BuildPo {
    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    /**
     * 根据给定的表信息生成 PO 类文件。
     *
     * @param tableInfo 表信息对象
     */
    public static void execute(TableInfo tableInfo) {
        // 获取 PO 文件所在的目录
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            // 如果目录不存在，则创建目录
            folder.mkdirs();
        }

        // 构建 PO 文件的完整路径
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");

        try (OutputStream out = Files.newOutputStream(poFile.toPath()); // 创建文件输出流
             OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8); // 创建输出流写入器，指定编码为 UTF-8
             BufferedWriter bw = new BufferedWriter(outw)) { // 创建缓冲写入器

            // 包声明
            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();

            // 导包语句
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            bw.write("import lombok.Data;");
            bw.newLine();
            bw.write("import lombok.NoArgsConstructor;");
            bw.newLine();
            bw.write("import lombok.AllArgsConstructor;");
            if (tableInfo.getHaveBigDecimal()) {
                bw.newLine();
                bw.write("import java.math.BigDecimal;");
            }
            if (tableInfo.getHaveDate()){
                bw.newLine();
                bw.write("import java.util.Date;");
            }
            if (tableInfo.getHaveDateTime()){
                bw.newLine();
                bw.write("import java.sql.Timestamp;");
            }
            bw.newLine();
            bw.newLine();
            BuildComment.createClassComment(bw,tableInfo.getComment());

            // 写入注解
            bw.write("@Data");
            bw.newLine();
            bw.write("@NoArgsConstructor");
            bw.newLine();
            bw.write("@AllArgsConstructor");
            bw.newLine();

            // 写入类声明
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");

            // 遍历字段列表，生成属性声明
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 获取字段名
                String fieldName = fieldInfo.getFieldName();
                // 获取属性名
                String propertyName = fieldInfo.getPropertyName();
                // 获取数据库类型
                String type = fieldInfo.getJavaType();
                // 获取备注
                String comment = fieldInfo.getComment();
                // 写入属性声明
                bw.newLine();
                BuildComment.createFieldComment(bw,comment);
                bw.write("    private " + type + " " + propertyName + ";");
                bw.newLine();

            }

            bw.write("}");
            // 刷新缓冲区
            bw.flush();
            logger.info("生成 PO 成功：{}", poFile.getAbsolutePath());

        } catch (IOException e) {
            // 记录生成 PO 失败的日志
            logger.error("生成 PO 失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
