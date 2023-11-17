package com.liu.gmall.search.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTmVo {

    private Long tmId ;
    private String tmName ;
    private String tmLogoUrl ;

}
