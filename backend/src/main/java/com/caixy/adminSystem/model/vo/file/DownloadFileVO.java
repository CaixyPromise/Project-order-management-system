package com.caixy.adminSystem.model.vo.file;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单文件信息VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.file.DownloadFileVO
 * @since 2024-06-16 10:06
 **/
@Data
public class DownloadFileVO implements Serializable
{
    /**
     * 文件id
     */
    private String id;

    /**
     * 文件sha256值
     */
    private String sha256;

    /**
     * 文件业务名称
     */
    private String fileCategoryName;
    private static final long serialVersionUID = -1L;
}
