package com.liu.gmall.prodect;
/*
 *@title SpelExpressionTest
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/13 19:45
 */


import com.liu.gmall.product.entity.SkuInfo;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelExpressionTest {
    public static void main(String[] args) {
        String str = "sku:info:#{ #skuInfo.getSkuName() }";
        String toUpper = "abc:#{ 'abc'.toUpperCase()}";
        String iter = "#{ #args[2] }";
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSkuName("this is skuName");
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(iter, ParserContext.TEMPLATE_EXPRESSION);
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariable("args", new String[]{"abc", "def", "ghi", "jkl"});
        standardEvaluationContext.setVariable("skuInfo", skuInfo);
        String value = expression.getValue(standardEvaluationContext, String.class);
        System.out.println(value);
    }
}
