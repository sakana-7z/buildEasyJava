package com.example.service;

import java.util.List;
import com.example.entity.po.ProductInfo;

/**
* @Description: 商品信息 service
* @CreatTime: 2024-10-24 00:44:14
* @Author: 卖火柴的屑魔女 , Ciallo～(∠・ω< )⌒★
*/

public interface ProductInfoService {

    /**
    * 根据主键查询
    * @param id
    * @return List<ProductInfo>
    */
List<ProductInfo> selectById(Long id);

    /**
    * 动态查询
    * @param productInfo
    * @return List<ProductInfo>
    */
List<ProductInfo> selectList(ProductInfo productInfo);

    /**
    * 根据主键更新
    * @param id
    * @return Integer
    */
Integer updateById(Long id,ProductInfo productInfo);

    /**
    * 根据主键删除
    * @param id
    * @return Integer
    */
Integer deleteById(Long id);

    /**
    * 批量删除
    * @param id
    * @return Integer
    */
Integer deleteByIds(List<Long> id);

    /**
    * 查询所有
    * @param 
    * @return List<ProductInfo>
    */
List<ProductInfo> selectAll();

}