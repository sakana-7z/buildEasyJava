package com.example.entity.po;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

/**
* @Description: 商品信息
* @CreatTime: 2024-10-24 00:44:14
* @Author: 卖火柴的屑魔女 , Ciallo～(∠・ω< )⌒★
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo implements Serializable {
    // 自增id
    private Long id;

    // 公司ID
    private String companyId;

    // 商品编号
    private String code;

    // 商品名字
    private String productName;

    // 价格
    private BigDecimal price;

    // sku类型
    private Byte skuType;

    // 颜色类型
    private Byte colorType;

    // 创建时间
    private Timestamp createTime;

    // 创建日期
    private Date createDate;

    // 库存
    private Long stock;

    // 状态
    private Byte status;
}