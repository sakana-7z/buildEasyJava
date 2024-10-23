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

import static com.easyJava.bean.Constants.PACKAGE_MAPPER;
import static com.easyJava.bean.Constants.PATH_MAPPER;
import static com.easyJava.bean.Constants.SUFFIX_MAPPER;

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    /**
     * 执行 Mapper 接口生成逻辑
     *
     * @param tableInfo 表信息
     * @throws Exception 如果文件操作失败
     */
    public static void execute(TableInfo tableInfo) throws Exception {
        // 获取 Mapper 文件所在的目录
        File folder = new File(PATH_MAPPER);
        if (!folder.exists()) {
            // 如果目录不存在，则创建目录
            folder.mkdirs();
        }

        // 构建 Mapper 文件的完整路径
        String className = tableInfo.getBeanName() + SUFFIX_MAPPER;
        File mapperFile = new File(folder, className + ".java");

        try (OutputStream out = Files.newOutputStream(mapperFile.toPath());
             OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outw)) {

            // 包声明
            bw.write("package " + PACKAGE_MAPPER + ";");
            bw.newLine();
            bw.newLine();

            // 导包语句
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.*;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + " mapper");
            bw.newLine();

            // 生成注解
            bw.write("@Mapper");
            bw.newLine();

            // 写入接口声明
            bw.write("public interface " + className + " extends BaseMapper {");
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

                // 生成 selectById 方法
                BuildComment.createFieldComment(bw, "根据" + keyName + "查询");
                bw.write("    List<" + tableInfo.getBeanName() + "> selectBy" + StringUtils.firstToUpperCase(keyName) + "(@Param(\"" + keyName + "\") " + keyType + " " + keyName + ");");
                bw.newLine();
                bw.newLine();

                // 生成 selectAll 方法
                BuildComment.createFieldComment(bw, "查询全部");
                bw.write("    List<" + tableInfo.getBeanName() + "> selectAll();");
                bw.newLine();
                bw.newLine();

                //生成 动态查询 方法
                BuildComment.createFieldComment(bw, "动态查询");
                bw.write("    List<" + tableInfo.getBeanName() + "> selectList(@Param(\"" + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + "\") " + tableInfo.getBeanName() + " " + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + ");");
                bw.newLine();
                bw.newLine();

                // 生成 update 方法
                BuildComment.createFieldComment(bw, "根据" + keyName + "更新");
                bw.write("    Integer updateBy"+StringUtils.firstToUpperCase(keyName)+"(@Param(\"" + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + "\") " + tableInfo.getBeanName() + " " + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + ");");
                bw.newLine();
                bw.newLine();

                // 生成 insert 方法
                BuildComment.createFieldComment(bw, "插入");
                bw.write("    Integer insert(@Param(\"" + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + "\") " + tableInfo.getBeanName() + " " + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + ");");
                bw.newLine();
                bw.newLine();

                // 生成 deleteById 方法
                BuildComment.createFieldComment(bw, "根据" + keyName + "删除");
                bw.write("    Integer deleteBy" + StringUtils.firstToUpperCase(keyName) + "(@Param(\"" + keyName + "\") " + keyType + " " + keyName + ");");
                bw.newLine();
                bw.newLine();

                //生成批量删除方法
                BuildComment.createFieldComment(bw, "批量删除");
                bw.write("    Integer deleteBy" + StringUtils.firstToUpperCase(keyName) + "s(@Param(\"" + keyName + "\") List<" + keyType + "> " + keyName + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.newLine();
            bw.write("}");
            bw.newLine(); // 添加接口结束括号

        } catch (Exception e) {
            // 记录生成 Mapper 失败的日志
            logger.error("生成 Mapper 失败", e);
            throw e; // 重新抛出异常
        }

        logger.info("生成 Mapper 成功：{}", mapperFile.getAbsolutePath());
    }
}
