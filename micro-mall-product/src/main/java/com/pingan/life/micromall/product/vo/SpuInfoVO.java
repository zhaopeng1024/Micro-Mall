package com.pingan.life.micromall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpuInfoVO {
    // 商品名称
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private Integer publishStatus;
    // 描述图片
    private List<String> descImages;
    // 图片集
    private List<String> images;
    private PointsVO points;
    private List<BasicAttrVO> basicAttrs;
    private List<SkuInfoVO> skuInfos;
}
