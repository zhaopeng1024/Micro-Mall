package com.pingan.life.micromall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询树形结构的全部商品分类
     * @return
     */
    List<CategoryEntity> selectCategoriesWithTree();
}

