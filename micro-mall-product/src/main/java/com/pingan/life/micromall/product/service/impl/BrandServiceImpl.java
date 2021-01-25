package com.pingan.life.micromall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pingan.life.micromall.product.dao.CategoryBrandRelationDao;
import com.pingan.life.micromall.product.entity.CategoryBrandRelationEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;

import com.pingan.life.micromall.product.dao.BrandDao;
import com.pingan.life.micromall.product.entity.BrandEntity;
import com.pingan.life.micromall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.eq("branch_id", key).or().like("name", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateBrandById(BrandEntity brand) {
        baseMapper.updateById(brand);
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandName(brand.getName());
        relationDao.update(relationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brand.getBrandId()));
    }

}