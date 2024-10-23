package com.easyJava;

import com.easyJava.bean.TableInfo;
import com.easyJava.builder.*;

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
            BuildMapperXML.execute(tableInfo);
            BuildService.execute(tableInfo);
            BuildServiceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);

        }
    }
}
