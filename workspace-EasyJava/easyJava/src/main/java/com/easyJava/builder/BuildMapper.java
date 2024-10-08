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

import static com.easyJava.bean.Constants.PACKAGE_MAPPER;
import static com.easyJava.bean.Constants.PATH_MAPPER;
import static com.easyJava.bean.Constants.SUFFIX_MAPPER;

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    /**
     * 执行 Mapper 接口生成逻辑
     * @param tableInfo 表信息
     * @throws IOException 如果文件操作失败
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

            // 导包语句
            bw.write("import " + PACKAGE_MAPPER + ".BaseMapper;");
            bw.newLine();

            bw.newLine();

            // 生成类注释
            BuildComment.createClassComment(bw, tableInfo.getComment()+"mapper");

            // 写入接口声明
            bw.write("public interface " + className + "<T,P> extends BaseMapper {");
            bw.newLine();

            // 遍历主键和索引字段，生成方法声明
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> fieldList = entry.getValue();

                int index = 0;
                StringBuilder methodName = new StringBuilder();
                for (FieldInfo field : fieldList) {
                   index++;
                   methodName.append(StringUtils.uperCaseFirstLetter(field.getPropertyName()));
                   if (index < fieldList.size()) {
                       methodName.append("And");
                   }
                }
                bw.write("    T selectBy" + methodName + "(P param);");
                bw.newLine();
            }

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
