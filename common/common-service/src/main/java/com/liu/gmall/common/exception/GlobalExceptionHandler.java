package com.liu.gmall.common.exception;
/*
 *@title GlobalExceptionHandler
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 18:53
 */


import com.alibaba.fastjson.JSON;
import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.sun.istack.internal.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GmallException.class)
    public Result gmallExceptionHandler(GmallException e) {
        e.printStackTrace();
        return Result.build(null, e.getResultCodeEnum());
    }

    @ExceptionHandler(value = Exception.class)
    public Result systemExceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();     // 获取错误提示信息
            if (!map.containsKey(fieldName)) {
                map.put(fieldName, defaultMessage);
            } else {
                map.put(fieldName, map.get(fieldName) + "," + defaultMessage);
            }
        }
        String jsonString = JSON.toJSONString(map);
        Result<String> result = new Result<>();
        result.setCode(227);
        result.setMessage(jsonString);
        return result;
    }
}
