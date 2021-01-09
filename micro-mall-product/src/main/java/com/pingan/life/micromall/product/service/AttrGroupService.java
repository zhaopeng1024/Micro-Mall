package com.pingan.life.micromall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

