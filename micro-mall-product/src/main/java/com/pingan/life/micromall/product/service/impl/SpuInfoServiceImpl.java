package com.pingan.life.micromall.product.service.impl;

import com.pingan.life.common.dto.SkuFullReductionDTO;
import com.pingan.life.common.dto.SpuBoundsDTO;
import com.pingan.life.common.utils.R;
import com.pingan.life.micromall.product.dao.*;
import com.pingan.life.micromall.product.entity.*;
import com.pingan.life.micromall.product.feign.SellFeignService;
import com.pingan.life.micromall.product.service.*;
import com.pingan.life.micromall.product.vo.ImageVO;
import com.pingan.life.micromall.product.vo.PointsVO;
import com.pingan.life.micromall.product.vo.SkuInfoVO;
import com.pingan.life.micromall.product.vo.SpuInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescDao spuInfoDescDao;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private SkuInfoDao skuInfoDao;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SellFeignService sellFeignService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        // 支持模糊检索
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(obj -> obj.eq("id", key).or().like("spu_name", key));
        }

        // 支持根据上架状态检索
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) {
            queryWrapper.eq("publish_status", status);
        }

        // 支持根据品牌检索
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        // 支持根据分类检索
        String catelogId = (String) params.get("catelog_id");
        if (StringUtils.isNotEmpty(catelogId)) {
            queryWrapper.eq("catelog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);
    }

    /**
     * 商品发布
     * TODO 待高级部分完善
     * @param spuInfo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuInfoVO spuInfo) {

        // 1. 保存SPU基本信息（pms_spu_info）
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        baseMapper.insert(spuInfoEntity);

        // 2. 保存SPU的描述图片（pms_spu_info_desc）
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", spuInfo.getDescImages()));
        spuInfoDescDao.insert(spuInfoDescEntity);

        // 3. 保存SPU的图片集（pms_spu_images）
        List<SpuImagesEntity> spuImagesEntities = spuInfo.getImages().stream().map(item -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(item);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(spuImagesEntities);

        // 4. 保存SPU的规格参数（pms_product_attr_value）
        List<ProductAttrValueEntity> productAttrValueEntities = spuInfo.getBasicAttrs().stream().map(item -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrId(item.getAttrId());
            productAttrValueEntity.setAttrValue(item.getAttrValues());
            productAttrValueEntity.setQuickShow(item.getQuickShow());
            productAttrValueEntity.setAttrName(attrDao.selectById(item.getAttrId()).getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntities);

        // 5. 保存SPU的积分信息（sms_spu_bounds）
        SpuBoundsDTO spuBoundsDTO = new SpuBoundsDTO();
        BeanUtils.copyProperties(spuInfo.getPoints(), spuBoundsDTO);
        spuBoundsDTO.setSpuId(spuInfoEntity.getId());
        R r = sellFeignService.saveSpuBounds(spuBoundsDTO);
        if ((Integer) r.get("code") != 0) {
            log.error("远程保存SPU积分信息失败");
        }

        // 6. 保存SPU对应的SKU信息
        List<SkuInfoVO> skuInfos = spuInfo.getSkuInfos();
        if (!CollectionUtils.isEmpty(skuInfos)) {
            skuInfos.forEach(skuInfo -> {

                // 6.1 保存SKU基本信息（pms_sku_info）
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(skuInfo, skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                for (ImageVO image : skuInfo.getImages()) {
                    if (image.getDefaultImage() == 1) {
                        skuInfoEntity.setSkuDefaultImg(image.getUrl());
                    }
                }
                skuInfoDao.insert(skuInfoEntity);

                // 6.2 保存SKU图集信息（pms_sku_images）
                List<SkuImagesEntity> skuImagesEntities = skuInfo.getImages().stream().map(item -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuImagesEntity.setImgUrl(item.getUrl());
                    skuImagesEntity.setDefaultImg(item.getDefaultImage());
                    return skuImagesEntity;
                }).filter(item -> StringUtils.isNotEmpty(item.getImgUrl())).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);

                // 6.3 保存SKU销售属性信息（pms_sku_sale_attr_value）
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuInfo.getAttrs().stream().map(item -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(item, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // 6.4 保存SKU的优惠、满减等信息（sms_sku_ladder\sms_sku_full_reduction\sms_member_price）
                SkuFullReductionDTO skuFullReductionDTO = new SkuFullReductionDTO();
                BeanUtils.copyProperties(skuInfo, skuFullReductionDTO);
                skuFullReductionDTO.setSkuId(skuInfoEntity.getSkuId());
                if (skuFullReductionDTO.getFullCount() > 0 || skuFullReductionDTO.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    if ((Integer)sellFeignService.saveSkuFullReductionDTO(skuFullReductionDTO).get("code") != 0) {
                        log.error("远程保存SKU优惠信息失败");
                    }
                }
            });
        }

    }

}