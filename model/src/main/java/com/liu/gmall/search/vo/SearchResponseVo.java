package com.liu.gmall.search.vo;

import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.entity.SearchAttr;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseVo {

    private SearchParamDto searchParam ;            // 封装请求参数的实体类
    private String trademarkParam ;                 // 品牌的面包屑
    private List<SearchAttr> propsParamList ;       // 平台属性面包屑
    private List<SearchTmVo> trademarkList ;        // 搜索面板中的品牌数据
    private String urlParam ;
    private List<SearchRespAttrVo> attrsList ;      // 搜索面板中的平台属性数据
    private SearchOrderMapVo orderMap ;             // 封装排序参数
    private List<Goods> goodsList ;                 // 当前页数据
    private Integer pageNo ;                        // 当前页码
    private Integer totalPages ;                    // 总页数

}
