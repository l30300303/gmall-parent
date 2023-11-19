package com.liu.gmall.search.repository;
/*
 *@title GoodsRepository
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 19:21
 */


import com.liu.gmall.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

}
