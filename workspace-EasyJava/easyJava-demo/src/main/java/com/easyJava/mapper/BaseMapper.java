package com.easyJava.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;
import java.util.List;

/**
 * 基础Mapper接口，提供通用的CRUD操作方法。
 *
 * @param <T> 实体类类型
 * @param <ID> 主键类型，要求实现Serializable接口
 */

public interface BaseMapper<T, P> {

    /**
     * insert:插入
     */
    Integer insert(@Param("bean") T t);

    /**
     * insertUpdate:插入或更新
     */
    Integer insertUpdate(@Param("bean") T t);

    /**
     * insertBatch:批量插入
     */
    Integer insertBatch(@Param("list") List<T> list);


    /**
     * insertUpdateBatch:批量插入或更新
     */
    Integer insertUpdateBatch(@Param("list") List<T> list);


    /**
     * selectList:根据参数查询集合
     */
    Iterable<T> selectList(@Param("query") P param);

    /**
     * selectCount:根据参数查询数量
     */
    Integer selectCount(@Param("query") P param);

}

