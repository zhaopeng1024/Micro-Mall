package com.pingan.life.micromall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pingan.life.micromall.product.entity.AttrAttrgroupRelationEntity;
import com.pingan.life.micromall.product.service.AttrAttrgroupRelationService;
import com.pingan.life.micromall.product.service.AttrService;
import com.pingan.life.micromall.product.service.CategoryService;
import com.pingan.life.micromall.product.vo.AttrGroupsWithAttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pingan.life.micromall.product.entity.AttrGroupEntity;
import com.pingan.life.micromall.product.service.AttrGroupService;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.R;



/**
 * 属性分组
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        attrGroup.setCategoryPath(categoryService.selectFullPath(attrGroup.getCatelogId()));
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 查询指定属性分组关联的所有基础属性数据
     * @param attrGroupId 属性分组ID
     */
    @GetMapping("/relatedAttr/{attrGroupId}")
    public R relatedAttr(@PathVariable("attrGroupId") Long attrGroupId) {
        return R.ok().put("data", attrService.selectRelatedAttrList(attrGroupId));
    }

    /**
     * 查询指定属性分组未关联的所有基础属性数据
     * @param attrGroupId 属性分组ID
     */
    @GetMapping("/noRelatedAttr/{attrGroupId}")
    public R noRelatedAttr(@PathVariable("attrGroupId") Long attrGroupId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.selectNoRelatedAttrList(attrGroupId, params);
        return R.ok().put("page", page);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

    /**
     * 删除属性分组与属性的关联关系
     */
    @PostMapping("/deleteRelationAttr")
    public R relationAttr(@RequestBody AttrAttrgroupRelationEntity[] relationEntities) {
        attrService.removeRelations(relationEntities);
        return R.ok();
    }

    @GetMapping("/relGroupsWithAttr")
    public R relGroupsWithAttr(@RequestParam(value = "catId") Long catId) {
        List<AttrGroupsWithAttrVO> data = attrGroupService.selectRelGroupsWithAttrByCategory(catId);
        return R.ok().put("data", data);
    }


}
