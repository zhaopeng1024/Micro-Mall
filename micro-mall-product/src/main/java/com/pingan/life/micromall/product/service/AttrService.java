package com.pingan.life.micromall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.product.entity.AttrAttrgroupRelationEntity;
import com.pingan.life.micromall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
public interface AttrService extends IService<AttrEntity> {

    /**
     * 基础属性和销售属性的分页列表查询
     * @param params 分页数据：page-当前页码、limit：每页显示记录数）
     *               检索关键字：key
     *               所属分类：catelogId（0-全部分类，!0-指定分类）
     *               属性类型：attrType（0-销售属性；1-基础属性）
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存属性分组
     * @param attr
     */
    void saveAttr(AttrEntity attr);

    /**
     * 查询属性详情，基本属性需要带出分组ID
     * @param attrId
     * @return
     */
    AttrEntity getAttrById(Long attrId);

    /**
     * 修改属性信息
     * @param attr
     */
    void updateAttrById(AttrEntity attr);

    /**
     * 根据属性分组ID查询分组关联的所属基本属性
     * @param attrGroupId 属性分组ID
     * @return
     */
    List<AttrEntity> selectRelatedAttrList(Long attrGroupId);

    /**
     * 删除属性分组与基本属性的关联关系
     * @param relationEntities
     */
    void removeRelations(AttrAttrgroupRelationEntity[] relationEntities);

    /**
     * 根据属性分组ID查询分组未关联的所属基本属性（支持分页）
     * @param attrGroupId 属性分组ID
     * @param params 分页参数、搜索关键字
     * @return
     */
    PageUtils selectNoRelatedAttrList(Long attrGroupId, Map<String, Object> params);
}

