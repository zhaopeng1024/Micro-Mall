package com.pingan.life.micromall.product.feign;

import com.pingan.life.common.dto.SkuFullReductionDTO;
import com.pingan.life.common.dto.SpuBoundsDTO;
import com.pingan.life.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("micro-mall-sell")
public interface SellFeignService {

    @PostMapping("/sell/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsDTO spuBoundsDTO);

    @PostMapping("/sell/skufullreduction/saveSkuInfo")
    R saveSkuFullReductionDTO(@RequestBody SkuFullReductionDTO skuFullReductionDTO);
}
