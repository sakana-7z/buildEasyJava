package com.easyJava.mapper;
import com.easyJava.mapper.BaseMapper;

/**
* @Description: 商品信息mapper
* @CreatTime: 2024-10-08 20:51:12
* @Author: 卖火柴的屑魔女 , Ciallo～(∠・ω< )⌒★
*/
public interface ProductInfoMapper<T,P> extends BaseMapper {
    T selectById(P param);
}
