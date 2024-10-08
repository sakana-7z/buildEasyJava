package com.easyJava.builder;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.easyJava.bean.Constants.AUTHOR_NAME;


public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws Exception {
        // 使用 SimpleDateFormat 格式化当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());

        bw.write("/**");
        bw.newLine();
        bw.write("* @Description: " + classComment);
        bw.newLine();
        bw.write("* @CreatTime: " + formattedDate);
        bw.newLine();
        bw.write("* @Author: "+AUTHOR_NAME+" , Ciallo～(∠・ω< )⌒★");
        bw.newLine();
        bw.write("*/");
        bw.newLine();

    }

    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws Exception {
        bw.write("    // " + fieldComment);
        bw.newLine();
    }

}
