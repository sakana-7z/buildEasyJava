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
import java.util.stream.Collectors;

public class BuildServiceImpl {
    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);

    /**
     * 根据给定的表信息生成 Service 实现类文件。
     *
     * @param tableInfo 表信息对象
     */
    public static void execute(TableInfo tableInfo) {
        // 获取 Service 文件所在的目录
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        if (!folder.exists()) {
            // 如果目录不存在，则创建目录
            folder.mkdirs();
        }

        // 构建 Service 实现类文件的完整路径
        File serviceFile = new File(folder, tableInfo.getBeanName() + "ServiceImpl.java");

        try (OutputStream out = Files.newOutputStream(serviceFile.toPath()); // 创建文件输出流
             OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8); // 创建输出流写入器，指定编码为 UTF-8
             BufferedWriter bw = new BufferedWriter(outw)) { // 创建缓冲写入器

            // 包声明
            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();

            // 导包语句
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + "Service;");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.write("import org.springframework.beans.factory.annotation.Autowired;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPER + "." + tableInfo.getBeanName() + "Mapper;");
            bw.newLine();
            bw.newLine();

            // 添加注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + " service implementation");
            bw.newLine();
            bw.newLine();

            bw.write("@Service");
            bw.newLine();



            // 类声明
            bw.write("public class " + tableInfo.getBeanName() + "ServiceImpl implements " + tableInfo.getBeanName() + "Service {");
            bw.newLine();
            bw.newLine();
            bw.write("    @Autowired");
            bw.newLine();
            bw.write("    private " + tableInfo.getBeanName() + "Mapper " + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + "Mapper;");
            bw.newLine();
            bw.newLine();

            // 获取字段名称和属性名称
            List<FieldInfo> fieldList = tableInfo.getFieldList();
            String fieldNames = fieldList.stream()
                    .map(FieldInfo::getFieldName)
                    .map(StringUtils::firstToUpperCase)
                    .collect(Collectors.joining(", ")); // 数据库中字段名集合
            String propertyNames = fieldList.stream()
                    .map(FieldInfo::getPropertyName)
                    .collect(Collectors.joining("},#{")); // 实体类中的属性名集合

            // 获取主键信息
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            List<FieldInfo> keyList = keyIndexMap.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (!keyList.isEmpty()) {
                String keyNames = keyList.stream()
                        .map(FieldInfo::getFieldName)
                        .collect(Collectors.joining(" AND ")); // 主键名称集合
                String placeholders = keyList.stream()
                        .map(fieldInfo -> "#{".concat(fieldInfo.getFieldName()).concat("}"))
                        .collect(Collectors.joining(", ")); // 占位符集合
                String keyType = keyList.get(0).getJavaType(); // 主键类型
                String keyName = keyList.get(0).getFieldName(); // 主键名称

                // 添加查询方法
                BuildComment.createMethodComment(bw, "根据主键查询", keyName, "List<" + tableInfo.getBeanName() + ">");
                bw.write("    @Override");
                bw.newLine();
                bw.write("    public List<" + tableInfo.getBeanName() + "> selectBy" + StringUtils.firstToUpperCase(keyName) + "(" + keyType + " " + keyName + ") {");
                bw.newLine();
                bw.write("        return "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+"Mapper"+".selectBy"+StringUtils.firstToUpperCase(keyName)+"("+keyName+");");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 添加动态查询方法
                BuildComment.createMethodComment(bw, "动态查询", StringUtils.firstToLowerCase(tableInfo.getBeanName()), "List<" + tableInfo.getBeanName() + ">");
                bw.write("    @Override");
                bw.newLine();
                bw.write("    public List<" + tableInfo.getBeanName() + "> selectList(" + tableInfo.getBeanName() + " " + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + ") {");
                bw.newLine();
                bw.write("        return "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+"Mapper"+".selectList("+StringUtils.firstToLowerCase(tableInfo.getBeanName())+");");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 更新方法
                BuildComment.createMethodComment(bw, "根据主键更新", keyName, "Integer");
                bw.write("    @Override");
                bw.newLine();
                bw.write("    public Integer updateBy" + StringUtils.firstToUpperCase(keyName) + "(" + keyType + " " + keyName +","+ tableInfo.getBeanName() + " " + StringUtils.firstToLowerCase(tableInfo.getBeanName())+ ") {");
                bw.newLine();
                bw.write("        "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+".set"+StringUtils.firstToUpperCase(keyName)+"("+keyName+");");
                bw.newLine();
                bw.write("        return "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+"Mapper"+".updateBy"+StringUtils.firstToUpperCase(keyName)+"("+StringUtils.firstToLowerCase(tableInfo.getBeanName())+");");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 删除方法
                BuildComment.createMethodComment(bw, "根据主键删除", keyName, "Integer");
                bw.write("    @Override");
                bw.newLine();
                bw.write("    public Integer deleteBy" + StringUtils.firstToUpperCase(keyName) + "(" + keyType + " " + keyName + ") {");
                bw.newLine();
                bw.write("        return "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+"Mapper"+".deleteBy"+StringUtils.firstToUpperCase(keyName)+"("+keyName+");");
                bw.newLine();
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 批量删除方法
                BuildComment.createMethodComment(bw, "批量删除", keyName, "Integer");
                bw.write("    @Override");
                bw.newLine();
                bw.write("    public Integer deleteBy" + StringUtils.firstToUpperCase(keyName) + "s(List<" + keyType + "> " + keyName + ") {");
                bw.newLine();
                bw.write("        return "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+"Mapper"+".deleteBy"+StringUtils.firstToUpperCase(keyName)+"s("+keyName+");");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 查询所有方法
                BuildComment.createMethodComment(bw, "查询所有", "", "List<" + tableInfo.getBeanName() + ">");
                bw.write("    @Override");
                bw.newLine();
                bw.write("    public List<" + tableInfo.getBeanName() + "> selectAll() {");
                bw.newLine();
                bw.write("        return "+StringUtils.firstToLowerCase(tableInfo.getBeanName())+"Mapper"+".selectAll();");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            // 刷新缓冲区
            bw.flush();
            logger.info("生成 ServiceImpl 实现类成功：{}", serviceFile.getAbsolutePath());

        } catch (IOException e) {
            // 记录生成 Service 实现类失败的日志
            logger.error("生成 ServiceImpl 实现类失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
