package com.pingan.life.micromall.product.service.impl;

import com.pingan.life.micromall.product.dao.BrandDao;
import com.pingan.life.micromall.product.dao.CategoryDao;
import com.pingan.life.micromall.product.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;

import com.pingan.life.micromall.product.dao.CategoryBrandRelationDao;
import com.pingan.life.micromall.product.entity.CategoryBrandRelationEntity;
import com.pingan.life.micromall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveCategoryBrandRelation(CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelation.setBrandName(brandDao.selectById(categoryBrandRelation.getBrandId()).getName());
        categoryBrandRelation.setCatelogName(categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName());
        baseMapper.insert(categoryBrandRelation);
    }

    @Override
    public List<BrandEntity> selectBrandsByCategory(Long catId) {
        List<CategoryBrandRelationEntity> relationEntities = baseMapper.selectList(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        return relationEntities.stream().map(item -> brandDao.selectById(item.getBrandId())).collect(Collectors.toList());
    }

}