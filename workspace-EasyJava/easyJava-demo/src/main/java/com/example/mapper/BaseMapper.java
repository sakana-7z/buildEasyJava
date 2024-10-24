package com.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BaseMapper<T, P> {

//    /**
//     * 插入一条记录
//     *
//     * @param t 要插入的对象
//     * @return 影响的行数
//     */
//    @Insert("INSERT INTO ${tableName} (${columns}) VALUES (${values})")
//    Integer insert(@Param("bean") T t);



//    /**
//     * 查询指定表的所有记录
//     *
//     * @param tableName 表名
//     * @return 表中的所有记录列表
//     */
//    @Select("SELECT * FROM ${tableName}")
//    List<T> selectAll(@Param("tableName") String tableName);



//    /**
//     * 根据主键查询记录
//     *
//     * @param primaryKey 查询对象的主键
//     * @return 符合条件的记录列表
//     */
//    @Select("SELECT * FROM ${tableName} WHERE ${primaryKey} = #{id}")
//    List<T> selectById(@Param("primaryKey") T primaryKey);



//    /**
//     * 删除一条记录
//     *
//     * @param id 要删除的记录的主键
//     * @return 影响的行数
//     */
//    @Delete("DELETE FROM ${tableName} WHERE id = #{id}")
//    Integer deleteById(@Param("id") P id);




// 以下方法暂未未实现
//
//    /**
//     * insertOrUpdate(插入或更新)
//     */
//     Integer insertOrUpdate(@Param("bean") T t);
//
//
//    /**
//     * insertBatch(批量插入)
//     */
//     Integer insertBatch(@Param("list") List<T> list);
//
//
//    /**
//     * selectList(根据参数查询集合)
//     */
//     List<T> selectList(@Param("bean") T t);
//
//
//     /**
//      * selectCount(根据参数查询总数)
//      */
//     Integer selectCount(@Param("bean") T t);
//
}
