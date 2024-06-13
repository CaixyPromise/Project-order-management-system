package com.caixy.adminSystem.service;

import com.caixy.adminSystem.model.dto.file.UploadFileConfig;

import java.io.File;

/**
 * @name: com.caixy.adminSystem.service.UploadFileService
 * @description: 文件上传下载服务
 * @author: CAIXYPROMISE
 * @date: 2024-05-21 21:52
 **/
public interface UploadFileService
{
    /**
     * 上传文件到COS
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/21 下午10:32
     */
    String saveFileToCos(UploadFileConfig uploadFileConfig);

    /**
     * 本地存储文件
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/21 下午10:32
     */
    String saveFileToLocal(UploadFileConfig uploadFileConfig);

    void deleteFileOnCos(String filepath);

    void deleteFileOnLocal(String filePath);

    void deleteFileOnLocal(File file);
}