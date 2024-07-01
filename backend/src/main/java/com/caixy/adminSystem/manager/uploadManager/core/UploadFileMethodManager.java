package com.caixy.adminSystem.manager.uploadManager.core;

import com.caixy.adminSystem.model.dto.file.UploadFileConfig;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @name: com.caixy.adminSystem.manager.uploadManager.core.UploadFileMethodManager
 * @description: 上传文件服务接口实现类
 * @author: CAIXYPROMISE
 * @date: 2024-06-21 20:44
 **/
public interface UploadFileMethodManager
{
    Path saveFile(UploadFileConfig uploadFileConfig) throws IOException;
    void deleteFile(Path key) throws IOException;
}
