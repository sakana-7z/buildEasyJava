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

public class BuildController {
    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    /**
     * 根据给定的表信息生成 Controller 类文件。
     *
     * @param tableInfo 表信息对象
     */
    public static void execute(TableInfo tableInfo) {
        // 获取 Controller 文件所在的目录
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 构建 Controller 类文件的完整路径
        File controllerFile = new File(folder, tableInfo.getBeanName() + "Controller.java");

        try (OutputStream out = Files.newOutputStream(controllerFile.toPath());
             OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outw)) {

            // 包声明
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();

            // 导包语句
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.*;");
            bw.newLine();
            bw.write("import org.springframework.beans.factory.annotation.Autowired;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + "Service;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_RESULT + ".Result;");
            bw.newLine();
            bw.newLine();

            // 添加注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + " controller");
            bw.newLine();
            bw.newLine();

            bw.write("@RestController");
            bw.newLine();
            bw.write("@RequestMapping(\"/" + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + "\")");
            bw.newLine();
            bw.newLine();

            // 类声明
            bw.write("public class " + tableInfo.getBeanName() + "Controller {");
            bw.newLine();
            bw.newLine();
            bw.write("    @Autowired");
            bw.newLine();
            bw.write("    private " + tableInfo.getBeanName() + "Service " + StringUtils.firstToLowerCase(tableInfo.getBeanName()) + "Service;");
            bw.newLine();
            bw.newLine();

            // 获取主键信息
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            List<FieldInfo> keyList = keyIndexMap.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (!keyList.isEmpty()) {
                String keyType = keyList.get(0).getJavaType(); // 主键类型
                String keyName = keyList.get(0).getFieldName(); // 主键名称
                String beanNameLower = StringUtils.firstToLowerCase(tableInfo.getBeanName());
                String beanNameUpper = StringUtils.firstToUpperCase(tableInfo.getBeanName());
                String keyNameUpper = StringUtils.firstToUpperCase(keyName);

                // 添加查询方法
                BuildComment.createMethodComment(bw, "根据主键查询", keyName, tableInfo.getBeanName());
                bw.write("    @GetMapping(\"/selectBy" + keyNameUpper + "/{" + keyName + "}\")");
                bw.newLine();
                bw.write("    public Result<List<" + beanNameUpper + ">> selectBy" + keyNameUpper + "(@PathVariable " + keyType + " " + keyName + ") {");
                bw.newLine();
                bw.write("        try {");
                bw.newLine();
                bw.write("            List<" + beanNameUpper + "> " + beanNameLower + " = " + beanNameLower + "Service.selectBy" + keyNameUpper + "(" + keyName + ");");
                bw.newLine();
                bw.write("            return Result.success(" + beanNameLower + ");");
                bw.newLine();
                bw.write("        } catch (Exception e) {");
                bw.newLine();
                bw.write("            return Result.failure(500, \"服务器内部错误\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 添加动态查询方法
                BuildComment.createMethodComment(bw, "动态查询", "", beanNameUpper);
                bw.write("    @GetMapping(\"/selectList\")");
                bw.newLine();
                bw.write("    public Result<List<" + beanNameUpper + ">> selectList(@RequestBody " + beanNameUpper + " " + beanNameLower + ") {");
                bw.newLine();
                bw.write("        try {");
                bw.newLine();
                bw.write("            List<" + beanNameUpper + "> " + beanNameLower + "List = " + beanNameLower + "Service.selectList(" + beanNameLower + ");");
                bw.newLine();
                bw.write("            return Result.success(" + beanNameLower + "List);");
                bw.newLine();
                bw.write("        } catch (Exception e) {");
                bw.newLine();
                bw.write("            return Result.failure(500, \"服务器内部错误\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 更新方法
                BuildComment.createMethodComment(bw, "更新数据", "", tableInfo.getBeanName());
                bw.write("    @PutMapping(\"/update\")");
                bw.newLine();
                bw.write("    public Result<Void> update(@RequestBody " + tableInfo.getBeanName() + " " + beanNameLower + ") {");
                bw.newLine();
                bw.write("        try {");
                bw.newLine();
                bw.write("            if (" + beanNameLower +".get"+StringUtils.firstToUpperCase(keyName)+ "() == null) {");
                bw.newLine();
                bw.write("            return Result.failure(400, \"产品ID不能为空\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("            "+keyType+ " "+keyName + " = " + beanNameLower + ".get" + StringUtils.firstToUpperCase(keyName) + "();");
                bw.newLine();
                bw.write("            " + beanNameLower + "Service.updateBy"+StringUtils.firstToUpperCase(keyName)+"(" +keyName+","+ beanNameLower + ");");
                bw.newLine();
                bw.write("            return Result.success();");
                bw.newLine();
                bw.write("        } catch (Exception e) {");
                bw.newLine();
                bw.write("            //logger.error(\"更新产品信息失败: {}\", e.getMessage(), e);");
                bw.newLine();
                bw.write("            return Result.failure(500, \"服务器内部错误\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 删除方法
                BuildComment.createMethodComment(bw, "根据主键删除", keyName, tableInfo.getBeanName());
                bw.write("    @DeleteMapping(\"/delete/{" + keyName + "}\")");
                bw.newLine();
                bw.write("    public Result<Void> deleteById(@PathVariable " + keyType + " " + keyName + ") {");
                bw.newLine();
                bw.write("        try {");
                bw.newLine();
                bw.write("            " + beanNameLower + "Service.deleteBy"+StringUtils.firstToUpperCase(keyName)+"(" + keyName + ");");
                bw.newLine();
                bw.write("            return Result.success(null);");
                bw.newLine();
                bw.write("        } catch (Exception e) {");
                bw.newLine();
                bw.write("            return Result.failure(500, \"服务器内部错误\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 批量删除方法
                BuildComment.createMethodComment(bw, "批量删除", "", tableInfo.getBeanName());
                bw.write("    @DeleteMapping(\"/batchDelete\")");
                bw.newLine();
                bw.write("    public Result<Void> batchDelete(@RequestBody List<" + keyType + "> " + keyName + "s) {");
                bw.newLine();
                bw.write("        try {");
                bw.newLine();
                bw.write("            " + beanNameLower + "Service.deleteBy"+StringUtils.firstToUpperCase(keyName)+"s(" + keyName + "s);");
                bw.newLine();
                bw.write("            return Result.success();");
                bw.newLine();
                bw.write("        } catch (Exception e) {");
                bw.newLine();
                bw.write("            return Result.failure(500, \"服务器内部错误\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();

                // 查询所有方法
                BuildComment.createMethodComment(bw, "查询所有数据", "", tableInfo.getBeanName());
                bw.write("    @GetMapping(\"/selectAll\")");
                bw.newLine();
                bw.write("    public Result<List<" + beanNameUpper + ">> selectAll() {");
                bw.newLine();
                bw.write("        try {");
                bw.newLine();
                bw.write("            List<" + beanNameUpper + "> " + beanNameLower + "List = " + beanNameLower + "Service.selectAll();");
                bw.newLine();
                bw.write("            return Result.success(" + beanNameLower + "List);");
                bw.newLine();
                bw.write("        } catch (Exception e) {");
                bw.newLine();
                bw.write("            return Result.failure(500, \"服务器内部错误\");");
                bw.newLine();
                bw.write("        }");
                bw.newLine();
                bw.write("    }");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
            logger.info("生成 Controller 类成功：{}", controllerFile.getAbsolutePath());

        } catch (IOException e) {
            logger.error("生成 Controller 类失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
