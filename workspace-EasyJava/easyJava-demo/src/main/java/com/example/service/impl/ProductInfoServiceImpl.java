package com.example.service.impl;

import java.util.List;
import com.example.entity.po.ProductInfo;
import com.example.service.ProductInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.mapper.ProductInfoMapper;

/**
* @Description: 商品信息 service implementation
* @CreatTime: 2024-10-24 00:44:14
* @Author: 卖火柴的屑魔女 , Ciallo～(∠・ω< )⌒★
*/


@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    /**
    * 根据主键查询
    * @param id
    * @return List<ProductInfo>
    */
    @Override
    public List<ProductInfo> selectById(Long id) {
        return productInfoMapper.selectById(id);
    }

    /**
    * 动态查询
    * @param productInfo
    * @return List<ProductInfo>
    */
    @Override
    public List<ProductInfo> selectList(ProductInfo productInfo) {
        return productInfoMapper.selectList(productInfo);
    }

    /**
    * 根据主键更新
    * @param id
    * @return Integer
    */
    @Override
    public Integer updateById(Long id,ProductInfo productInfo) {
        productInfo.setId(id);
        return productInfoMapper.updateById(productInfo);
    }

    /**
    * 根据主键删除
    * @param id
    * @return Integer
    */
    @Override
    public Integer deleteById(Long id) {
        return productInfoMapper.deleteById(id);

    }

    /**
    * 批量删除
    * @param id
    * @return Integer
    */
    @Override
    public Integer deleteByIds(List<Long> id) {
        return productInfoMapper.deleteByIds(id);
    }

    /**
    * 查询所有
    * @param 
    * @return List<ProductInfo>
    */
    @Override
    public List<ProductInfo> selectAll() {
        return productInfoMapper.selectAll();
    }

}