package com.example.mapper;

import com.example.entity.po.ProductInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
* @Description: 商品信息 mapper
* @CreatTime: 2024-10-24 00:44:14
* @Author: 卖火柴的屑魔女 , Ciallo～(∠・ω< )⌒★
*/

@Mapper
public interface ProductInfoMapper extends BaseMapper {
    // 根据id查询
    List<ProductInfo> selectById(@Param("id") Long id);

    // 查询全部
    List<ProductInfo> selectAll();

    // 动态查询
    List<ProductInfo> selectList(@Param("productInfo") ProductInfo productInfo);

    // 根据id更新
    Integer updateById(@Param("productInfo") ProductInfo productInfo);

    // 插入
    Integer insert(@Param("productInfo") ProductInfo productInfo);

    // 根据id删除
    Integer deleteById(@Param("id") Long id);

    // 批量删除
    Integer deleteByIds(@Param("id") List<Long> id);


}
