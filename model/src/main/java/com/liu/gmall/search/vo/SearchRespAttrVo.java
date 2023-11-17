package com.liu.gmall.search.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRespAttrVo {

    private Long attrId ;
    private String attrName ;
    private List<String> attrValueList ;

}
