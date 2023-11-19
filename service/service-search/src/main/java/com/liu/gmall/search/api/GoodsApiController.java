package com.liu.gmall.search.api;
/*
 *@title GoodsApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 18:51
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.service.GoodsService;
import com.liu.gmall.search.vo.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inner/search")
public class GoodsApiController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/saveGoods")
    public Result saveGoods(@RequestBody Goods goods){
        goodsService.saveGoods(goods);
        return Result.ok();
    }

    @DeleteMapping("/deleteById/{skuId}")
    public Result deleteById(@PathVariable("skuId")Long skuId){
        goodsService.deleteById(skuId);
        return Result.ok();
    }

    @PostMapping("/search")
    public Result<SearchResponseVo> search(@RequestBody SearchParamDto searchParamDto){
        SearchResponseVo searchResponseVo = goodsService.search(searchParamDto);
        return Result.ok(searchResponseVo);
    }
}
