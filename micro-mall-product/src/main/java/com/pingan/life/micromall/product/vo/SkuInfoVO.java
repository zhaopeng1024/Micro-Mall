package com.pingan.life.micromall.product.vo;

import com.pingan.life.common.dto.MemberPriceDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuInfoVO {
    private List<AttrVO> attrs;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<ImageVO> images;
    // 迪斯卡尔
    private List<String> discal;
    // 满减数量
    private int fullCount;
    // 打几折
    private BigDecimal disCount;
    // 是否叠加其他优惠（0-否；1-是）
    private int addOther;
    private String skuDesc;
    // 满减价格
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    // 是否参与其他优惠（0-否；1-是）
    private int priceStatus;
    private List<MemberPriceDTO> memberPrices;
}
