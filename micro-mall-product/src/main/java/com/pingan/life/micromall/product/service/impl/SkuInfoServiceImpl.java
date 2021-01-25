package com.pingan.life.micromall.product.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;

import com.pingan.life.micromall.product.dao.SkuInfoDao;
import com.pingan.life.micromall.product.entity.SkuInfoEntity;
import com.pingan.life.micromall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        queryWrapper.and(obj -> obj.eq("sku_id", key).or().like("sku_name", key));

        String catelogId = (String) params.get("catelog_id");
        if (StringUtils.isNotEmpty(catelogId)) {
            queryWrapper.eq("catelog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if (StringUtils.isNotEmpty(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if (StringUtils.isNotEmpty(max) && new BigDecimal(max).compareTo(BigDecimal.ZERO) > 0) {
            queryWrapper.le("price", max);
        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

}