package com.liu.gmall.common.exception;
/*
 *@title GlobalExceptionHandler
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 18:53
 */


import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GmallException.class)
    public Result gmallExceptionHandler(GmallException e){
        e.printStackTrace();
        return Result.build(null,e.getResultCodeEnum());
    }

    @ExceptionHandler(value = Exception.class)
    public Result systemExceptionHandler(Exception e){
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }
}
