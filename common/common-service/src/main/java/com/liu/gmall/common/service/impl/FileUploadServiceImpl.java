package com.liu.gmall.common.service.impl;
/*
 *@title FileUploadServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/4 9:20
 */


import com.liu.gmall.common.properties.MinioProperties;
import com.liu.gmall.common.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioClient minioClient;


    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String fileUpload(MultipartFile file) {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/"+ UUID.randomUUID().toString().replace("-","") + extension;
            minioClient.putObject(PutObjectArgs
                    .builder()
                            .bucket(minioProperties.getBucket())
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build());
            String imgUrl = minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + fileName ;
            return imgUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
