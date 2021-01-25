package com.pingan.life.micromall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.pingan.life.common.enums.AttrEnum;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.Query;
import com.pingan.life.micromall.product.dao.AttrAttrgroupRelationDao;
import com.pingan.life.micromall.product.dao.AttrDao;
import com.pingan.life.micromall.product.dao.AttrGroupDao;
import com.pingan.life.micromall.product.dao.CategoryDao;
import com.pingan.life.micromall.product.entity.AttrAttrgroupRelationEntity;
import com.pingan.life.micromall.product.entity.AttrEntity;
import com.pingan.life.micromall.product.entity.AttrGroupEntity;
import com.pingan.life.micromall.product.entity.CategoryEntity;
import com.pingan.life.micromall.product.service.AttrService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    private final CategoryDao categoryDao;

    private final AttrGroupDao attrGroupDao;

    @Autowired
    public AttrServiceImpl(AttrAttrgroupRelationDao attrAttrgroupRelationDao, CategoryDao categoryDao, AttrGroupDao attrGroupDao) {
        this.attrAttrgroupRelationDao = attrAttrgroupRelationDao;
        this.categoryDao = categoryDao;
        this.attrGroupDao = attrGroupDao;
    }

    /**
     * 基础属性和销售属性的分页列表查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(obj -> obj.eq("attr_id", key).or().like("attr_name", key));
        }

        Long catelogId = (Long) params.get("catelogId");
        if (0L != catelogId) {
            queryWrapper.eq("catelog_id", catelogId);
        }

        Integer attrType = (Integer) params.get("attrType");
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper.eq("attr_type", attrType)
        );

        page.getRecords().forEach(item -> {
            Optional<CategoryEntity> categoryEntity = Optional.ofNullable(categoryDao.selectById(item.getCatelogId()));
            categoryEntity.ifPresent(obj -> item.setCategoryName(obj.getName()));
            // 基本属性才需要返回分组名
            if (AttrEnum.BASIC_ATTR.getValue() == item.getAttrType()) {
                Optional<AttrAttrgroupRelationEntity> attrGroupRelation = Optional.ofNullable(
                        attrAttrgroupRelationDao.selectOne(
                                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", item.getAttrId())));
                attrGroupRelation.ifPresent(obj -> {
                    if (null != obj.getAttrGroupId()) {
                        item.setAttrGroupName(attrGroupDao.selectById(obj.getAttrGroupId()).getAttrGroupName());
                    }
                });
            }
        });
        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrEntity attr) {
        // 保存属性信息
        baseMapper.insert(attr);
        // 保存基本属性关联分组信息
        if (attr.getAttrGroupId() != null && AttrEnum.BASIC_ATTR.getValue() == attr.getAttrType()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrSort(0);
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    @Override
    public AttrEntity getAttrById(Long attrId) {
        AttrEntity attr = baseMapper.selectById(attrId);
        if (AttrEnum.BASIC_ATTR.getValue() == attr.getAttrType()) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (ObjectUtils.isNotEmpty(relationEntity)) {
                attr.setAttrGroupId(relationEntity.getAttrGroupId());
                attr.setAttrGroupName(attrGroupDao.selectById(relationEntity.getAttrGroupId()).getAttrGroupName());
            }
        }
        attr.setCategoryName(categoryDao.selectById(attr.getCatelogId()).getName());
        return attr;
    }

    @Transactional
    @Override
    public void updateAttrById(AttrEntity attr) {
        baseMapper.updateById(attr);
        // 修改关联分组信息
        if (AttrEnum.BASIC_ATTR.getValue() == attr.getAttrType()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            if (attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId())) == 0) {
                relationEntity.setAttrSort(0);
                attrAttrgroupRelationDao.insert(relationEntity);
            } else {
                attrAttrgroupRelationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            }
        }
    }

    @Override
    public List<AttrEntity> selectRelatedAttrList(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIdList = relationList.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        List<AttrEntity> attrEntities = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(attrIdList)) {
            attrEntities = baseMapper.selectBatchIds(attrIdList);
        }
        return attrEntities;
    }

    @Transactional
    @Override
    public void removeRelations(AttrAttrgroupRelationEntity[] relationEntities) {
        List<Long> relationIds = Arrays.stream(relationEntities).map(item -> {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", item.getAttrId())
                    .eq("attr_group_id", item.getAttrGroupId())
            );
            return relationEntity.getId();
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBatchIds(relationIds);
    }

    @Override
    public PageUtils selectNoRelatedAttrList(Long attrGroupId, Map<String, Object> params) {

        // 当前分组所属分类
        final Long catId = attrGroupDao.selectById(attrGroupId).getCatelogId();
        // 当前分类下所有的分组
        List<AttrGroupEntity> attrGroups = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        List<Long> attrGroupIds = attrGroups.stream().map(item -> item.getAttrGroupId()).collect(Collectors.toList());

        // 当前分类下所有的分组已经关联的所有属性
        List<AttrAttrgroupRelationEntity> attrAttrGroupRelations = attrAttrgroupRelationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> relatedAttrIds = attrAttrGroupRelations.stream().map(item -> item.getAttrId()).collect(Collectors.toList());

        // 从当前分类的所有属性中过滤掉当前分类下所属分组已经关联的所有属性，并且分页查询出来
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id", catId).eq("attr_type", AttrEnum.BASIC_ATTR.getValue());
        if (!CollectionUtils.isEmpty(relatedAttrIds)) {
            queryWrapper.notIn("attr_id", relatedAttrIds);
        }
        CharSequence key = (CharSequence) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(obj -> obj.eq("attr_id", key).or().like("attr_name", key));
        }
        IPage<AttrEntity> page = baseMapper.selectPage(new Query<AttrEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    public static void main(String[] args) {

    }

}