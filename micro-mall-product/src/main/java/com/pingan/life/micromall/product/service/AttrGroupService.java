package com.pingan.life.micromall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.micromall.product.entity.AttrGroupEntity;
import com.pingan.life.micromall.product.vo.AttrGroupsWithAttrVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类ID查询分类下所有关联分组及其关联属性信息
     * @param catId 分类ID
     */
    List<AttrGroupsWithAttrVO> selectRelGroupsWithAttrByCategory(Long catId);
}

