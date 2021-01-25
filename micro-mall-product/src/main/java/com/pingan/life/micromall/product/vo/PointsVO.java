package com.pingan.life.micromall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointsVO {

    /**
     * 成长积分
     */
    private BigDecimal buyBounds;
    /**
     * 购物积分
     */
    private BigDecimal growBounds;

}
