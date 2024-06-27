package com.caixy.adminSystem.model.dto.lang;

import com.caixy.adminSystem.constant.CommonConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 语言类型DTO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.dto.lang.LangTypeEsDTO
 * @since 2024-06-25 16:07
 **/
@Document(indexName = "lang_type")
@Data
public class LangTypeEsDTO implements Serializable
{
    @Id
    private Long id;

    /**
     * 编程语言名称
     */
    private String languageName;

    /**
     * 创建用户id
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
