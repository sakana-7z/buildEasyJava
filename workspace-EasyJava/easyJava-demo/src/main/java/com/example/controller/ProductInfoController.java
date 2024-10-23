package com.example.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.entity.po.ProductInfo;
import com.example.service.ProductInfoService;
import com.example.entity.Result;

/**
* @Description: 商品信息 controller
* @CreatTime: 2024-10-24 00:44:14
* @Author: 卖火柴的屑魔女 , Ciallo～(∠・ω< )⌒★
*/


@RestController
@RequestMapping("/productInfo")

public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService;

    /**
    * 根据主键查询
    * @param id
    * @return ProductInfo
    */
    @GetMapping("/selectById/{id}")
    public Result<List<ProductInfo>> selectById(@PathVariable Long id) {
        try {
            List<ProductInfo> productInfo = productInfoService.selectById(id);
            return Result.success(productInfo);
        } catch (Exception e) {
            return Result.failure(500, "服务器内部错误");
        }
    }

    /**
    * 动态查询
    * @param 
    * @return ProductInfo
    */
    @GetMapping("/selectList")
    public Result<List<ProductInfo>> selectList(@RequestBody ProductInfo productInfo) {
        try {
            List<ProductInfo> productInfoList = productInfoService.selectList(productInfo);
            return Result.success(productInfoList);
        } catch (Exception e) {
            return Result.failure(500, "服务器内部错误");
        }
    }

    /**
    * 更新数据
    * @param 
    * @return ProductInfo
    */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody ProductInfo productInfo) {
        try {
            if (productInfo.getId() == null) {
            return Result.failure(400, "产品ID不能为空");
        }
            Long id = productInfo.getId();
            productInfoService.updateById(id,productInfo);
            return Result.success();
        } catch (Exception e) {
            //logger.error("更新产品信息失败: {}", e.getMessage(), e);
            return Result.failure(500, "服务器内部错误");
        }
    }

    /**
    * 根据主键删除
    * @param id
    * @return ProductInfo
    */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        try {
            productInfoService.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(500, "服务器内部错误");
        }
    }

    /**
    * 批量删除
    * @param 
    * @return ProductInfo
    */
    @DeleteMapping("/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        try {
            productInfoService.deleteByIds(ids);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(500, "服务器内部错误");
        }
    }

    /**
    * 查询所有数据
    * @param 
    * @return ProductInfo
    */
    @GetMapping("/selectAll")
    public Result<List<ProductInfo>> selectAll() {
        try {
            List<ProductInfo> productInfoList = productInfoService.selectAll();
            return Result.success(productInfoList);
        } catch (Exception e) {
            return Result.failure(500, "服务器内部错误");
        }
    }

}