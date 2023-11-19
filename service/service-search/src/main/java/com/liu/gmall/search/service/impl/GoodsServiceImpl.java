package com.liu.gmall.search.service.impl;

import cn.hutool.core.util.PageUtil;
import com.google.common.collect.Lists;
import com.liu.gmall.search.entity.SearchAttr;
import com.liu.gmall.search.vo.SearchOrderMapVo;
/*
 *@title GoodsServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 18:52
 */


import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.repository.GoodsRepository;
import com.liu.gmall.search.service.GoodsService;
import com.liu.gmall.search.vo.SearchRespAttrVo;
import com.liu.gmall.search.vo.SearchResponseVo;
import com.liu.gmall.search.vo.SearchTmVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void saveGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void deleteById(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public SearchResponseVo search(SearchParamDto searchParamDto) {
        log.info("调用 es search 接口");
        //构建的搜索的请求参数对象
        NativeSearchQuery nativeSearchQuery = buildNativeSearchQuery(searchParamDto);
        //发送搜索请求
        SearchHits<Goods> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Goods.class);

        //解析响应对象获取结果数据
        return parseSearchHits(searchHits, searchParamDto);
    }

    private SearchResponseVo parseSearchHits(SearchHits<Goods> searchHits, SearchParamDto searchParamDto) {
        SearchResponseVo searchResponseVo = new SearchResponseVo();
        searchResponseVo.setSearchParam(searchParamDto);
        String trademark = searchParamDto.getTrademark();
        //品牌属性面包屑
        if (!StringUtils.isEmpty(trademark)) {
            String[] trademarkArr = trademark.split(":");
            searchResponseVo.setTrademarkParam("品牌:" + trademarkArr[1]);

        }
        //平台属性面包屑
        String[] props = searchParamDto.getProps();
        List<SearchAttr> searchAttrs = new ArrayList<>();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                String[] propArr = prop.split(":");
                SearchAttr searchAttr = new SearchAttr(Long.parseLong(propArr[0]), propArr[1], propArr[2]);
                searchAttrs.add(searchAttr);
            }
            searchResponseVo.setPropsParamList(searchAttrs);
        }

        Aggregations aggregations = searchHits.getAggregations();
        ParsedLongTerms tmIdAgg = aggregations.get("tmIdAgg");
        List<? extends Terms.Bucket> buckets = tmIdAgg.getBuckets();
        List<SearchTmVo> searchTmVoList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            //品牌的id
            String tmIdStr = bucket.getKeyAsString();
            Aggregations bucketAggregations = bucket.getAggregations();

            ParsedStringTerms tmNameParsedStringTerms = bucketAggregations.get("tmNameAgg");
            String tmName = tmNameParsedStringTerms.getBuckets().get(0).getKeyAsString();

            ParsedStringTerms tmLogUrlAgg = bucketAggregations.get("tmLogoUrlAgg");
            String tmLogUrl = tmLogUrlAgg.getBuckets().get(0).getKeyAsString();

            SearchTmVo searchTmVo = new SearchTmVo(Long.parseLong(tmIdStr), tmName, tmLogUrl);
            searchTmVoList.add(searchTmVo);
        }
        searchResponseVo.setTrademarkList(searchTmVoList);

        String urlParam = buildUrlParam(searchParamDto);
        searchResponseVo.setUrlParam(urlParam);

        ParsedNested attrsAgg = aggregations.get("attrsAgg");
        Aggregations attrsAggAggregations = attrsAgg.getAggregations();
        ParsedLongTerms attrIdAgg = attrsAggAggregations.get("attrIdAgg");
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        List<SearchRespAttrVo> searchRespAttrVoList = new ArrayList<>();
        for (Terms.Bucket aggBucket : attrIdAggBuckets) {
            // 获取平台属性id
            String attrIdStr = aggBucket.getKeyAsString();

            // 获取平台属性名称
            Aggregations bucketAggregations = aggBucket.getAggregations();
            ParsedStringTerms attrNameAgg = bucketAggregations.get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();

            // 获取平台属性值
            ParsedStringTerms attrValueAgg = bucketAggregations.get("attrValueAgg");
            List<? extends Terms.Bucket> aggBuckets = attrValueAgg.getBuckets();
            List<String> attrValueList = new ArrayList<>();
            for (Terms.Bucket bucket : aggBuckets) {
                String attrValue = bucket.getKeyAsString();
                attrValueList.add(attrValue);
            }

            // 封装平台属性数据
            SearchRespAttrVo searchRespAttrVo = SearchRespAttrVo.builder()
                    .attrId(Long.parseLong(attrIdStr))
                    .attrName(attrName)
                    .attrValueList(attrValueList)
                    .build();

            // 把数据存储到searchRespAttrVoList对象
            searchRespAttrVoList.add(searchRespAttrVo);
        }
        searchResponseVo.setAttrsList(searchRespAttrVoList);

        String order = searchParamDto.getOrder();
        SearchOrderMapVo searchOrderMapVo = new SearchOrderMapVo("1", "desc");
        if (!StringUtils.isEmpty(order) && !"null".equals(order)) {
            String[] orderArr = order.split(":");
            String type = orderArr[0];
            String sort = orderArr[1];
            searchOrderMapVo = new SearchOrderMapVo(type, sort);
        }
        searchResponseVo.setOrderMap(searchOrderMapVo);

        List<SearchHit<Goods>> searchHitsList = searchHits.getSearchHits();
        List<Goods> goodsList = searchHitsList.stream().map(searchHit -> {
            Goods goods = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            if (highlightFields != null && highlightFields.size() > 0) {
                List<String> titleList = highlightFields.get("title");
                if (titleList != null && titleList.size() > 0) {
                    goods.setTitle(titleList.get(0));
                }
            }
            return goods;
        }).collect(Collectors.toList());

        searchResponseVo.setGoodsList(goodsList);

        searchResponseVo.setPageNo(searchParamDto.getPageNo());
        long totalHits = searchHits.getTotalHits();
        int totalPage = PageUtil.totalPage((int) totalHits,searchParamDto.getPageSize());
        searchResponseVo.setTotalPages(totalPage);

        return searchResponseVo;
    }

    private String buildUrlParam(SearchParamDto searchParamDto) {
        StringBuilder sb = new StringBuilder("list.html?");
        Long category1Id = searchParamDto.getCategory1Id();
        if (category1Id != null) {
            sb.append("&category1Id=").append(category1Id);
        }

        Long category2Id = searchParamDto.getCategory2Id();
        if (category2Id != null) {
            sb.append("&category2Id=").append(category2Id);
        }

        Long category3Id = searchParamDto.getCategory3Id();
        if (category3Id != null) {
            sb.append("&category3Id=").append(category3Id);
        }

        String keyword = searchParamDto.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            sb.append("&keyword=").append(keyword);
        }

        String[] props = searchParamDto.getProps();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                sb.append("&props=").append(prop);
            }
        }

        return sb.toString();
    }

    private NativeSearchQuery buildNativeSearchQuery(SearchParamDto searchParamDto) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Long category1Id = searchParamDto.getCategory1Id();
        if (category1Id != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("category1Id", category1Id));
        }
        Long category2Id = searchParamDto.getCategory2Id();
        if (category2Id != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("category2Id", category2Id));
        }
        Long category3Id = searchParamDto.getCategory3Id();
        if (category3Id != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("category3Id", category3Id));
        }
        //关键字查询
        String keyword = searchParamDto.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", keyword));
        }

        //设置品牌的搜索条件
        String trademark = searchParamDto.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            boolQueryBuilder.must(QueryBuilders.termQuery("tmId", Long.parseLong(split[0])));
        }

        //设置平台属性的搜索条件
        String[] props = searchParamDto.getProps();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                String[] split = prop.split(":");
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

                boolQuery.must(QueryBuilders.termQuery("attrs.attrId", Long.parseLong(split[0])));
                boolQuery.must(QueryBuilders.termQuery("attrs.attrValue", split[1]));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQueryBuilder.must(nestedQuery);
            }

        }
        //构建排序参数
        String order = searchParamDto.getOrder();
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("hotScore");
        if (!StringUtils.isEmpty(order) && !"null".equals(order)) {
            String[] orderAttr = order.split(":");
            String sortField = orderAttr[0];
            String sortOrder = orderAttr[1];
            switch (sortField) {
                case "1":
                    fieldSortBuilder = new FieldSortBuilder("hotScore");
                    break;
                case "2":
                    fieldSortBuilder = new FieldSortBuilder("price");
                    break;
            }
            SortOrder sortOrderEnum = "asc".equalsIgnoreCase(sortOrder) ? SortOrder.ASC : SortOrder.DESC;
            fieldSortBuilder.order(sortOrderEnum);
        }
        //设置分页参数
        PageRequest pageRequest = PageRequest.of(searchParamDto.getPageNo() - 1, searchParamDto.getPageSize());

        // 构建高亮的参数
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(fieldSortBuilder)
                .withPageable(pageRequest)
                .build();
        // 设置搜索面板品牌数据所需要的聚合参数
        TermsAggregationBuilder tmIdAggregationBuilder = AggregationBuilders.terms("tmIdAgg").field("tmId").size(100);
        TermsAggregationBuilder tmNameAggregationBuilder = AggregationBuilders.terms("tmNameAgg").field("tmName").size(1);
        TermsAggregationBuilder tmLogoUrlAggregationBuilder = AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl").size(1);
        tmIdAggregationBuilder.subAggregation(tmNameAggregationBuilder);
        tmIdAggregationBuilder.subAggregation(tmLogoUrlAggregationBuilder);
        nativeSearchQuery.addAggregation(tmIdAggregationBuilder);

        // 设置搜索面板平台属性数据所需要的聚合参数
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attrsAgg", "attrs");
        TermsAggregationBuilder attrIdAggAggregationBuilder = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(50);
        attrIdAggAggregationBuilder.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
        attrIdAggAggregationBuilder.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(50));
        nestedAggregationBuilder.subAggregation(attrIdAggAggregationBuilder);
        nativeSearchQuery.addAggregation(nestedAggregationBuilder);

        return nativeSearchQuery;
    }
}
