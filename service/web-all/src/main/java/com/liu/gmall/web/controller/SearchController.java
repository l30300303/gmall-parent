package com.liu.gmall.web.controller;
/*
 *@title SearchController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 19:36
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.search.GoodsFeignClient;
import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.vo.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @GetMapping(value = "/list.html")
    public String search(SearchParamDto searchParamDto , Model model) {
        Result<SearchResponseVo> responseVoResult = goodsFeignClient.search(searchParamDto);
        SearchResponseVo searchResponseVo = responseVoResult.getData();

        // 把数据存储到Model数据模型中
        model.addAttribute("searchParam" , searchResponseVo.getSearchParam()) ;
        model.addAttribute("trademarkParam" , searchResponseVo.getTrademarkParam()) ;
        model.addAttribute("urlParam" , searchResponseVo.getUrlParam()) ;
        model.addAttribute("propsParamList" , searchResponseVo.getPropsParamList()) ;
        model.addAttribute("trademarkList" , searchResponseVo.getTrademarkList()) ;
        model.addAttribute("attrsList" , searchResponseVo.getAttrsList()) ;
        model.addAttribute("orderMap" , searchResponseVo.getOrderMap()) ;
        model.addAttribute("goodsList" , searchResponseVo.getGoodsList()) ;
        model.addAttribute("pageNo" , searchResponseVo.getPageNo()) ;
        model.addAttribute("totalPages" , searchResponseVo.getTotalPages()) ;

        return "list/index";
    }
}
