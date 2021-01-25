package com.pingan.life.micromall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.product.entity.BrandEntity;
import com.pingan.life.micromall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2021-01-23 01:51:08
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存分类与品牌的关联关系信息
     * @param categoryBrandRelation 分类与品牌的关联关系信息
     */
    void saveCategoryBrandRelation(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 根据分类ID，获取当前分类下的所有分组
     * @param catId 分类ID
     * @return 分组集合
     */
    List<BrandEntity> selectBrandsByCategory(Long catId);
}

