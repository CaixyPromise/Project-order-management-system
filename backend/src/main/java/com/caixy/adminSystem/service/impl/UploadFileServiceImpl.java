package com.caixy.adminSystem.service.impl;

import com.caixy.adminSystem.annotation.UploadMethodTarget;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.manager.uploadManager.core.UploadFileMethodManager;
import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import com.caixy.adminSystem.model.enums.FileUploadBizEnum;
import com.caixy.adminSystem.model.enums.SaveFileMethodEnum;
import com.caixy.adminSystem.service.UploadFileService;
import com.caixy.adminSystem.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * 文件服务实现类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.service.impl.UploadFileServiceImpl
 * @since 2024-05-21 21:55
 **/
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService
{
    @Resource
    private List<UploadFileMethodManager> uploadFileMethodManagers;

    private Map<SaveFileMethodEnum, UploadFileMethodManager> uploadFileMethodMap;

    @PostConstruct
    public void initUploadFileMethods()
    {
        uploadFileMethodMap =
                SpringContextUtils.getServiceFromAnnotation(uploadFileMethodManagers, UploadMethodTarget.class);
    }

    @Override
    public void deleteFile(FileUploadBizEnum fileUploadBizEnum, Path filePath)
    {
        UploadFileMethodManager uploadFileMethodManager = safetyGetUploadFileMethod(fileUploadBizEnum.getSaveFileMethod());
        try
        {
            uploadFileMethodManager.deleteFile(filePath);
        }
        catch (IOException e)
        {
            log.error("file delete error, filepath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除文件失败: " + e);
        }
    }


    @Override
    public void deleteFile(FileUploadBizEnum fileUploadBizEnum, Long userId, String filename)
    {
        Path filePath = fileUploadBizEnum.buildFileAbsolutePathAndName(userId, filename);
        UploadFileMethodManager uploadFileMethodManager = safetyGetUploadFileMethod(fileUploadBizEnum.getSaveFileMethod());
        try
        {
            uploadFileMethodManager.deleteFile(filePath);
        }
        catch (IOException e)
        {
            log.error("file delete error, filepath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除文件失败: " + e);
        }
    }

    @Override
    public Path saveFile(UploadFileConfig uploadFileConfig) throws IOException
    {
        FileUploadBizEnum fileUploadBizEnum = uploadFileConfig.getFileUploadBizEnum();
        UploadFileMethodManager uploadFileMethodManager = safetyGetUploadFileMethod(fileUploadBizEnum.getSaveFileMethod());
        // 把上传服务处理类暴露给 后处理操作 ，例如需要删除前置文件信息等
        uploadFileConfig.setUploadManager(uploadFileMethodManager);
        return uploadFileMethodManager.saveFile(uploadFileConfig);
    }

    private UploadFileMethodManager safetyGetUploadFileMethod(SaveFileMethodEnum saveFileMethodEnum)
    {
        UploadFileMethodManager uploadFileMethodManager = uploadFileMethodMap.get(saveFileMethodEnum);
        if (uploadFileMethodManager == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的文件存储方式");
        }
        return uploadFileMethodManager;
    }
}
