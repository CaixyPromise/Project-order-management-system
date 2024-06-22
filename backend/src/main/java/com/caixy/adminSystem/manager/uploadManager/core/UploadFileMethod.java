package com.caixy.adminSystem.manager.uploadManager.core;

import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @name: com.caixy.adminSystem.manager.uploadManager.core.UploadFileMethod
 * @description: 上传文件服务接口实现类
 * @author: CAIXYPROMISE
 * @date: 2024-06-21 20:44
 **/
public interface UploadFileMethod
{
    String saveFile(UploadFileConfig uploadFileConfig) throws IOException;
    void deleteFile(String key) throws IOException;
}
