package com.caixy.adminSystem.controller;

import cn.hutool.core.io.FileUtil;
import com.caixy.adminSystem.annotation.FileUploadActionTarget;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.FileUploadActionException;
import com.caixy.adminSystem.model.dto.file.UploadFileConfig;
import com.caixy.adminSystem.model.dto.file.UploadFileRequest;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.enums.FileUploadBizEnum;
import com.caixy.adminSystem.model.enums.SaveFileMethodEnum;
import com.caixy.adminSystem.service.FileUploadActionService;
import com.caixy.adminSystem.service.UploadFileService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.utils.FileUtils;
import com.caixy.adminSystem.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController
{
    @Resource
    private UserService userService;

    @Resource
    private UploadFileService uploadFileService;

    @Resource
    private List<FileUploadActionService> fileUploadActionService;

    private HashMap<FileUploadBizEnum, FileUploadActionService> serviceCache;

    @PostConstruct
    public void initActionService()
    {
        serviceCache =
                SpringContextUtils.getServiceFromAnnotation(fileUploadActionService, FileUploadActionTarget.class);
    }

    @PostMapping("/upload")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> uploadFile(
            @RequestPart("file") MultipartFile multipartFile,
            UploadFileRequest uploadFileRequest,
            HttpServletRequest request)
    {
        String savePath = null;
        UploadFileConfig uploadFileConfig = getUploadFileConfig(multipartFile, uploadFileRequest, request);
        SaveFileMethodEnum saveFileMethod = uploadFileConfig.getFileUploadBizEnum().getSaveFileMethod();
        try
        {
            // 获取文件处理类，如果找不到就会直接报错
            FileUploadActionService actionService = getFileUploadActionService(uploadFileConfig);
            boolean doVerifyFileToken = doBeforeFileUploadAction(actionService, uploadFileConfig, uploadFileRequest);
            if (!doVerifyFileToken)
            {
                log.error("{}-验证token：文件上传失败，文件信息：{}, 上传用户Id: {}", saveFileMethod.getDesc(),
                        uploadFileConfig.getFileInfo(),
                        uploadFileConfig.getUserId());
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
            }
            savePath = uploadFileService.saveFile(uploadFileConfig);
            boolean doAfterFileUpload =
                    doAfterFileUploadAction(actionService, uploadFileConfig, savePath, uploadFileRequest);
            if (!doAfterFileUpload)
            {
                log.error("{}：文件上传成功，文件路径：{}，但后续处理失败", saveFileMethod.getDesc(), savePath);
                uploadFileService.deleteFile(uploadFileConfig.getFileUploadBizEnum(), savePath);

                log.error("{}：文件上传成功，文件路径：{}，后处理失败后，成功删除文件", saveFileMethod.getDesc(), savePath);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传成功，但后续处理失败");
            }
            log.info("{}：文件上传成功，文件路径：{}", saveFileMethod.getDesc(), savePath);
            return ResultUtils.success(savePath);
        }
        catch (FileUploadActionException | IOException e)
        {
            log.error("{}: 文件上传失败，错误信息: {}", saveFileMethod.getDesc(), e.getMessage());
            // 如果 savePath 不为空，则意味着文件已经上传成功，需要删除它
            if (savePath != null)
            {
                uploadFileService.deleteFile(uploadFileConfig.getFileUploadBizEnum(), savePath);
                log.info("{}：文件上传失败，删除文件成功，文件路径：{}", saveFileMethod.getDesc(), savePath);
            }
            // 抛出业务异常，以触发事务回滚
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }


//    /**
//     * 文件上传
//     *
//     * @param multipartFile
//     * @param uploadFileRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/upload/cos")
//    public BaseResponse<String> uploadFileSaveOnCos(@RequestPart("file") MultipartFile multipartFile,
//                                           @RequestBody UploadFileRequest uploadFileRequest,
//                                           HttpServletRequest request)
//    {
//        String savePath = null;
//        UploadFileConfig uploadFileConfig = getUploadFileConfig(multipartFile, uploadFileRequest, request);
//        try
//        {
//            // 获取文件处理类，如果找不到就会直接报错
//            FileUploadActionService actionService = getFileUploadActionService(uploadFileConfig);
//            boolean doVerifyFileToken = doBeforeFileUploadAction(actionService, uploadFileConfig, uploadFileRequest);
//            if (!doVerifyFileToken)
//            {
//                log.error("COS对象存储-验证token：文件上传失败，文件信息：{}, 上传用户Id: {}", uploadFileConfig.getFileInfo(),
//                        uploadFileConfig.getUserId());
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
//            }
//            savePath = uploadFileService.saveFile(uploadFileConfig);
//            boolean doAfterFileUpload =
//                    doAfterFileUploadAction(actionService, uploadFileConfig, savePath, uploadFileRequest);
//            if (!doAfterFileUpload)
//            {
//                log.error("COS对象存储：文件上传成功，文件路径：{}，但后续处理失败", savePath);
//                uploadFileService.deleteFile(uploadFileConfig.getFileUploadBizEnum(), savePath);
//
//                log.error("COS对象存储：文件上传成功，文件路径：{}，后处理失败后，成功删除文件", savePath);
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传成功，但后续处理失败");
//            }
//            log.info("COS对象存储：文件上传成功，文件路径：{}", savePath);
//            return ResultUtils.success(savePath);
//        }
//        catch (FileUploadActionException | IOException e)
//        {
//            log.error("文件上传失败，错误信息: {}", e.getMessage());
//
//            // 如果 savePath 不为空，则意味着文件已经上传成功，需要删除它
//            if (savePath != null)
//            {
//                uploadFileService.deleteFile(uploadFileConfig.getFileUploadBizEnum(), savePath);
//                log.info("COS对象存储：文件上传失败，删除文件成功，文件路径：{}", savePath);
//            }
//            // 抛出业务异常，以触发事务回滚
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
//        }
//    }
//
//
//    /**
//     * 保存文件到本地
//     *
//     * @author CAIXYPROMISE
//     * @version 1.0
//     * @since 2024/5/21 下午10:36
//     */
//    @PostMapping("/upload/local")
//    public BaseResponse<String> uploadFileToLocal(@RequestPart("file") MultipartFile multipartFile,
//                                                  UploadFileRequest uploadFileRequest,
//                                                  HttpServletRequest request)
//    {
//        String savePath = null;
//        UploadFileConfig uploadFileConfig = getUploadFileConfig(multipartFile, uploadFileRequest, request);
//        try
//        {
//            // 获取文件处理类，如果找不到就会直接报错
//            FileUploadActionService actionService = getFileUploadActionService(uploadFileConfig);
//            boolean doVerifyFileToken = doBeforeFileUploadAction(actionService, uploadFileConfig, uploadFileRequest);
//            if (!doVerifyFileToken)
//            {
//                log.error("本地文件存储-验证token：文件上传失败，文件信息：{}, 上传用户Id: {}", uploadFileConfig.getFileInfo(),
//                        uploadFileConfig.getUserId());
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
//            }
//            savePath = uploadFileService.saveFile(uploadFileConfig);
//            log.info("本地存储：文件上传成功，文件路径：{}", savePath);
//            boolean doAfterFileUpload =
//                    doAfterFileUploadAction(actionService, uploadFileConfig, savePath, uploadFileRequest);
//            if (!doAfterFileUpload)
//            {
//                log.error("本地文件存储：文件上传成功，文件路径：{}，但后续处理失败", savePath);
//                uploadFileService.deleteFile(uploadFileConfig.getFileUploadBizEnum(), savePath);
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传成功，但后续处理失败");
//            }
//            return ResultUtils.success(uploadFileConfig.getFileInfo().getFileURL());
//        }
//        catch (FileUploadActionException | IOException e)
//        {
//            log.error("本地文件上传失败，错误信息: {}", e.getMessage());
//
//            // 如果 savePath 不为空，则意味着文件已经上传成功，需要删除它
//            if (savePath != null)
//            {
//                uploadFileService.deleteFile(uploadFileConfig.getFileUploadBizEnum(), savePath);
//
//                log.info("本地文件存储：文件上传失败，删除文件成功，文件路径：{}", savePath);
//            }
//            // 抛出业务异常，以触发事务回滚
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
//        }
//    }

    /**
     * 校验文件
     *
     * @param multipartFile
     */
    private FileUploadBizEnum validFile(MultipartFile multipartFile, UploadFileRequest uploadFileRequest)
    {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());

        Set<String> acceptFileSuffixList = fileUploadBizEnum.getFileSuffix();
        if (!acceptFileSuffixList.contains(fileSuffix))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不正确");
        }
        boolean lessThanOrEqualTo = fileUploadBizEnum.getMaxSize().isLessThanOrEqualTo(fileSize);
        if (!lessThanOrEqualTo)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小超过限制");
        }
        return fileUploadBizEnum;
    }

    /**
     * 获取上传文件配置信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/21 下午10:54
     */
    private UploadFileConfig getUploadFileConfig(MultipartFile multipartFile,
                                                 UploadFileRequest uploadFileRequest,
                                                 HttpServletRequest request)
    {
        FileUploadBizEnum fileUploadBizEnum = validFile(multipartFile, uploadFileRequest);
        User loginUser = userService.getLoginUser(request);
        UploadFileConfig uploadFileConfig = new UploadFileConfig();
        uploadFileConfig.setFileUploadBizEnum(fileUploadBizEnum);
        uploadFileConfig.setMultipartFile(multipartFile);
        uploadFileConfig.setUserId(loginUser.getId());
        uploadFileConfig.setSha256(FileUtils.getMultiPartFileSha256(multipartFile));
        uploadFileConfig.setFileSize(multipartFile.getSize());
        UploadFileConfig.FileInfo fileInfo = uploadFileConfig.convertFileInfo();
        uploadFileConfig.setFileInfo(fileInfo);
        return uploadFileConfig;
    }

    /**
     * 获取业务文件上传处理器
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/11 下午8:00
     */
    private FileUploadActionService getFileUploadActionService(UploadFileConfig uploadFileConfig)
    {
        FileUploadBizEnum fileUploadBizEnum = uploadFileConfig.getFileUploadBizEnum();
        FileUploadActionService actionService = serviceCache.get(fileUploadBizEnum);
        if (actionService == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "暂无该文件操作处理类");
        }
        return actionService;
    }

    private boolean doAfterFileUploadAction(FileUploadActionService actionService, UploadFileConfig uploadFileConfig, String savePath, UploadFileRequest uploadFileRequest)
    {
        return actionService.doAfterUploadAction(uploadFileConfig, savePath, uploadFileRequest);
    }

    private boolean doBeforeFileUploadAction(FileUploadActionService actionService, UploadFileConfig uploadFileConfig
            , UploadFileRequest uploadFileRequest)
    {
        return actionService.doBeforeUploadAction(uploadFileConfig, uploadFileRequest);
    }
}
