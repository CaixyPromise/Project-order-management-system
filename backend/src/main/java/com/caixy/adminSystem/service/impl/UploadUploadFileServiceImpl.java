package com.caixy.adminSystem.service.impl;

import com.caixy.adminSystem.annotation.UploadMethodTarget;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.manager.uploadManager.CosManager;
import com.caixy.adminSystem.manager.uploadManager.LocalFileManager;
import com.caixy.adminSystem.manager.uploadManager.core.UploadFileMethod;
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
import java.util.List;
import java.util.Map;

/**
 * 文件服务实现类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.service.impl.UploadUploadFileServiceImpl
 * @since 2024-05-21 21:55
 **/
@Service
@Slf4j
public class UploadUploadFileServiceImpl implements UploadFileService
{
    @Resource
    private CosManager cosManager;

    @Resource
    private LocalFileManager localFileManager;

    @Resource
    private List<UploadFileMethod> uploadFileMethods;

    private Map<SaveFileMethodEnum, UploadFileMethod> uploadFileMethodMap;

    @PostConstruct
    public void initUploadFileMethods()
    {
        uploadFileMethodMap =
                SpringContextUtils.getServiceFromAnnotation(uploadFileMethods, UploadMethodTarget.class);
    }


//    /**
//     * 上传文件到COS
//     *
//     * @author CAIXYPROMISE
//     * @version 1.0
//     * @since 2024/5/21 下午10:32
//     */
//    public String saveFileToCos(UploadFileConfig uploadFileConfig)
//    {
//        MultipartFile multipartFile = uploadFileConfig.getMultipartFile();
//        UploadFileConfig.FileInfo fileInfo = uploadFileConfig.convertFileInfo();
//        uploadFileConfig.setFileInfo(fileInfo);
//        String filepath = fileInfo.getFilePath();
//        File file = null;
//        try
//        {
//            // 上传文件
//            file = File.createTempFile(filepath, null);
//            multipartFile.transferTo(file);
//            cosManager.putObject(filepath, file);
//            // 本地存储
//            // 返回可访问地址
//            return FileConstant.COS_HOST + filepath;
//        }
//        catch (Exception e)
//        {
//            log.error("file upload error, filepath = {}", filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        }
//        finally
//        {
//            deleteFileOnLocal(file);
//        }
//    }

//    /**
//     * 本地存储文件
//     *
//     * @author CAIXYPROMISE
//     * @version 1.0
//     * @since 2024/5/21 下午10:32
//     */
//    public String saveFileToLocal(UploadFileConfig uploadFileConfig)
//    {
//        UploadFileConfig.FileInfo fileInfo = uploadFileConfig.convertFileInfo();
//        uploadFileConfig.setFileInfo(fileInfo);
//        return localFileManager.saveFile(uploadFileConfig.getMultipartFile(), uploadFileConfig);
//    }
//
//    @Override
//    public void deleteFileOnCos(String filepath)
//    {
//        cosManager.deleteObject(filepath);
//    }
//
//    @Override
//    public void deleteFileOnLocal(String filePath)
//    {
//        File file = new File(filePath);
//        if (file.exists())
//        {
//            // 删除临时文件
//            boolean delete = file.delete();
//            if (!delete)
//            {
//                log.error("file delete error, filepath = {}", file.getAbsolutePath());
//            }
//        }
//    }

    @Override
    public void deleteFile(FileUploadBizEnum fileUploadBizEnum, String filePath)
    {
        UploadFileMethod uploadFileMethod = safetyGetUploadFileMethod(fileUploadBizEnum.getSaveFileMethod());
        try
        {
            uploadFileMethod.deleteFile(filePath);
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
        String filePath = fileUploadBizEnum.buildFileAbsoluteName(userId, filename);
        UploadFileMethod uploadFileMethod = safetyGetUploadFileMethod(fileUploadBizEnum.getSaveFileMethod());
        try
        {
            uploadFileMethod.deleteFile(filePath);
        }
        catch (IOException e)
        {
            log.error("file delete error, filepath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除文件失败: " + e);
        }
    }

    @Override
    public String saveFile( UploadFileConfig uploadFileConfig) throws IOException
    {
        FileUploadBizEnum fileUploadBizEnum = uploadFileConfig.getFileUploadBizEnum();
        UploadFileMethod uploadFileMethod = safetyGetUploadFileMethod(fileUploadBizEnum.getSaveFileMethod());
        return uploadFileMethod.saveFile(uploadFileConfig);
    }

    private UploadFileMethod safetyGetUploadFileMethod(SaveFileMethodEnum saveFileMethodEnum)
    {
        UploadFileMethod uploadFileMethod = uploadFileMethodMap.get(saveFileMethodEnum);
        if (uploadFileMethod == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的文件存储方式");
        }
        return uploadFileMethod;
    }
}
