package com.pingan.life.micromall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

