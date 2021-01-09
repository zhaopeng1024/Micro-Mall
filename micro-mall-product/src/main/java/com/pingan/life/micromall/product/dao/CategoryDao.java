package com.pingan.life.micromall.product.dao;

import com.pingan.life.micromall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品三级分类
 * 
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

    /**
     * 查询树形结构的全部商品分类
     * 2021-1-9 过期
     * 过期原因：因为分类数据较多，更新平凡，采用mybatis递归查询，耗时较长，前端体验不佳
     * @return
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    List<CategoryEntity> selectCategoriesWithTree();

}
