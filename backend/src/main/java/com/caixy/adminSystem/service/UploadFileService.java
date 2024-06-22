package com.caixy.adminSystem.service;

import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import com.caixy.adminSystem.model.enums.FileUploadBizEnum;

import java.io.IOException;

/**
 * @name: com.caixy.adminSystem.service.UploadFileService
 * @description: 文件上传下载服务
 * @author: CAIXYPROMISE
 * @date: 2024-05-21 21:52
 **/
public interface UploadFileService
{


//    void deleteFileOnCos(String filepath);
//
//    void deleteFileOnLocal(String filePath);

//    void deleteFileOnLocal(File file);

    void deleteFile(FileUploadBizEnum fileUploadBizEnum, String filePath);

    void deleteFile(FileUploadBizEnum fileUploadBizEnum, Long userId, String filename);

    String saveFile(UploadFileConfig uploadFileConfig) throws IOException;
}
