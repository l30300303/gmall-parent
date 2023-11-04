package com.liu.gmall.common.service;
/*
 *@title FileUploadService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/4 9:19
 */


import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String fileUpload(MultipartFile file);
}
