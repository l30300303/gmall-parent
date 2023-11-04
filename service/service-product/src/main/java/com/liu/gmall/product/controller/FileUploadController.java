package com.liu.gmall.product.controller;
/*
 *@title FileUploadController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 20:09
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    public Result<String> fileUpload(@RequestPart("file") MultipartFile file) {
        String filePath = fileUploadService.fileUpload(file);
        return Result.ok(filePath);
    }
}
