package com.pingan.life.micromall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;
import com.pingan.life.micromall.product.dao.CategoryBrandRelationDao;
import com.pingan.life.micromall.product.dao.CategoryDao;
import com.pingan.life.micromall.product.entity.CategoryBrandRelationEntity;
import com.pingan.life.micromall.product.entity.CategoryEntity;
import com.pingan.life.micromall.product.enums.CategoryEnum;
import com.pingan.life.micromall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> selectCategoriesWithTree() {
        // 查询所有分类
        List<CategoryEntity> categories = baseMapper.selectList(null);
        // 组装成树形结构
        return categories.stream().filter(item -> item.getCatLevel() == CategoryEnum.LEVEL_1.getValue())
                .peek(item -> item.setSubCategories(getSubCategories(item, categories)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    private List<CategoryEntity> getSubCategories(CategoryEntity categoryEntity, List<CategoryEntity> categories) {
        return categories.stream().filter(item -> Objects.equals(item.getParentCid(), categoryEntity.getCatId()))
                .peek(item -> item.setSubCategories(getSubCategories(item, categories)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public Long[] selectFullPath(Long catId) {
        CategoryEntity category = baseMapper.selectById(catId);
        List<Long> fullPath = Lists.newArrayList();
        while (0 != category.getParentCid()) {
            fullPath.add(category.getCatId());
            category = baseMapper.selectById(category.getParentCid());
        }
        fullPath.add(category.getCatId());
        if (fullPath.size() > 1) {
            Collections.reverse(fullPath);
        }
        return fullPath.toArray(new Long[0]);
    }

    @Transactional
    @Override
    public void updateCategoryById(CategoryEntity category) {
        baseMapper.updateById(category);
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setCatelogName(category.getName());
        relationDao.update(relationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", category.getCatId()));
    }
}