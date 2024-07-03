package com.caixy.adminSystem.service.impl;

import com.caixy.adminSystem.model.dto.file.UploadFileDTO;
import com.caixy.adminSystem.model.enums.FileActionBizEnum;
import com.caixy.adminSystem.service.UploadFileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@SpringBootTest
class UploadFileServiceImplTest
{
    @Resource
    private UploadFileService uploadFileService;


}