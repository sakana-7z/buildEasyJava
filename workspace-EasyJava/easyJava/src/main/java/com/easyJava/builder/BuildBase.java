package com.easyJava.builder;

import com.easyJava.bean.Constants;
import com.easyJava.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute(TableInfo tableInfo) {
        List<String> headerInfoList = new ArrayList<>();

        // 生成Result类
        headerInfoList.add("package " + Constants.PACKAGE_RESULT + ";");
        build(headerInfoList, "Result", Constants.PATH_RESULT);
        logger.info("生成Result类成功");


        //生成BaseMapper接口
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_MAPPER + ";");
        build(headerInfoList, "BaseMapper", Constants.PATH_MAPPER);
        logger.info("生成BaseMapper成功");
    }

    private static void build(List<String> headerInfoList, String fileName, String outputPath) {
        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outputPath, fileName + ".java");

        try (
                OutputStream out = new FileOutputStream(javaFile);
                OutputStreamWriter outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(outw)
        ) {
            // 写入包名
            for (String headerInfo : headerInfoList) {
                bw.write(headerInfo);
                bw.newLine();
            }
            bw.newLine();

            // 读取模板文件内容
            String templatePath = "template/" + fileName + ".txt";
            try (InputStream in = BuildBase.class.getClassLoader().getResourceAsStream(templatePath);
                 InputStreamReader inr = new InputStreamReader(in, StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(inr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            bw.flush();
        } catch (FileNotFoundException e) {
            logger.error("文件未找到: {}", e.getMessage(), e);
        } catch (IOException e) {
            logger.error("IO异常: {}", e.getMessage(), e);
        }
    }
}
