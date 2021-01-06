package com.pingan.life.micromall.sell.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.sell.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-24 23:23:50
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

