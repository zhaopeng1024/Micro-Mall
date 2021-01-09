package com.pingan.life.micromall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;
import com.pingan.life.micromall.product.dao.CategoryDao;
import com.pingan.life.micromall.product.entity.CategoryEntity;
import com.pingan.life.micromall.product.enums.CategoryEnum;
import com.pingan.life.micromall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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

}