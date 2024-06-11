package com.caixy.adminSystem.service;

import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import com.caixy.adminSystem.model.dto.file.UploadFileRequest;

/**
 * @name: com.caixy.adminSystem.service.FileUploadActionService
 * @description: 文件上传操作接口类
 * @author: CAIXYPROMISE
 * @date: 2024-05-22 16:51
 **/
public interface FileUploadActionService
{
    /**
     * 文件上传后处理操作
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/10 下午11:51
     */
    Boolean doAfterUploadAction(UploadFileConfig uploadFileConfig, String savePath);

    /**
     * 解密token
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/10 下午11:51
     */
    default Boolean doBeforeUploadAction(UploadFileConfig uploadFileConfig
            , UploadFileRequest uploadFileRequest)
    {
        return true;
    }
}
