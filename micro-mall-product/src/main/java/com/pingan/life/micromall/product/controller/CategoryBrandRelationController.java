package com.pingan.life.micromall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pingan.life.micromall.product.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pingan.life.micromall.product.entity.CategoryBrandRelationEntity;
import com.pingan.life.micromall.product.service.CategoryBrandRelationService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2021-01-23 01:51:08
 */
@RestController
@RequestMapping("product/categoryBrandRelation")
public class CategoryBrandRelationController {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     *
     */
    @RequestMapping("/brand/list")
    public R brandList(@RequestParam(value = "catId") Long catId){
        List<BrandEntity> brandList = categoryBrandRelationService.selectBrandsByCategory(catId);
        return R.ok().put("data", brandList);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);
        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveCategoryBrandRelation(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
