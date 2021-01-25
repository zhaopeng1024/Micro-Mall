package com.pingan.life.micromall.product.controller;

import com.pingan.life.common.jsr303.ParamCheckGroupForSave;
import com.pingan.life.common.jsr303.ParamCheckGroupForUpdate;
import com.pingan.life.common.jsr303.ParamCheckGroupForUpdateStatus;
import com.pingan.life.common.utils.PageUtils;
import com.pingan.life.common.utils.R;
import com.pingan.life.micromall.product.entity.BrandEntity;
import com.pingan.life.micromall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 品牌
 *
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody @Validated(ParamCheckGroupForSave.class) BrandEntity brand, BindingResult result){
		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody @Validated(ParamCheckGroupForUpdate.class) BrandEntity brand){
		brandService.updateBrandById(brand);
        return R.ok();
    }

    /**
     * 修改显示状态
     */
    @RequestMapping("/updateStatus")
    public R updateStatus(@RequestBody @Validated(ParamCheckGroupForUpdateStatus.class) BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }

}
