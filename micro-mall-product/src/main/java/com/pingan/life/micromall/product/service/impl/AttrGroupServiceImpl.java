package com.pingan.life.micromall.product.service.impl;

import com.pingan.life.micromall.product.entity.AttrEntity;
import com.pingan.life.micromall.product.service.AttrService;
import com.pingan.life.micromall.product.vo.AttrGroupsWithAttrVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

import com.pingan.life.micromall.product.dao.AttrGroupDao;
import com.pingan.life.micromall.product.entity.AttrGroupEntity;
import com.pingan.life.micromall.product.service.AttrGroupService;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource(type = AttrServiceImpl.class)
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        CharSequence key = (CharSequence) params.get("key");
        // 支持模糊查询
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and(obj -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
        }
        Integer catId = Integer.valueOf((String) params.get("catId"));
        if (0 != catId) {
            // 支持根据分类ID进行查询
            queryWrapper.eq("catelog_id", catId);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params), queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupsWithAttrVO> selectRelGroupsWithAttrByCategory(Long catId) {
        // 查询分类关联所有分组
        List<AttrGroupEntity> attrGroups = baseMapper.selectList(
                new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));

        // 查询所有分组关联的分类
        return attrGroups.stream().map(item -> {
            AttrGroupsWithAttrVO attrGroupsWithAttrVO = new AttrGroupsWithAttrVO();
            BeanUtils.copyProperties(item, attrGroupsWithAttrVO);
            List<AttrEntity> attrList = attrService.selectRelatedAttrList(item.getAttrGroupId());
            attrGroupsWithAttrVO.setAttrList(attrList);
            return attrGroupsWithAttrVO;
        }).collect(Collectors.toList());
    }
}