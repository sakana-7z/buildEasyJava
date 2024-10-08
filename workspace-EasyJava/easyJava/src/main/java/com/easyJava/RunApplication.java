package com.easyJava;

import com.easyJava.bean.TableInfo;
import com.easyJava.builder.BuildBase;
import com.easyJava.builder.BuildMapper;
import com.easyJava.builder.BuildPo;
import com.easyJava.builder.BuildTable;

import java.io.IOException;
import java.util.List;

public class RunApplication {
    public static void main(String[] args) throws Exception {
        List<TableInfo> tableInfoList = BuildTable.getTables();

        for (TableInfo tableInfo : tableInfoList) {
            BuildPo.execute(tableInfo);
            //BuildQuery.execute(tableInfo);
            BuildBase.execute(tableInfo);
            BuildMapper.execute(tableInfo);

        }
    }
}
