package com.caixy.adminSystem.annotation;

import com.caixy.adminSystem.model.enums.FileUploadBizEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @name: com.caixy.adminSystem.annotation.FileUploadActionTarget
 * @description: 文件上传业务处理器
 * @author: CAIXYPROMISE
 * @date: 2024-06-11 19:47
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FileUploadActionTarget
{
    FileUploadBizEnum value();
}
