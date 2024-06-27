package com.caixy.adminSystem.model.dto.category;

import com.caixy.adminSystem.constant.CommonConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单分类Es实体类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.dto.category.OrderCategoryEsDTO
 * @since 2024-06-25 16:09
 **/
@Document(indexName = "order_category")
@Data
public class OrderCategoryEsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    /**
     * 订单分类名称
     */
    private String categoryName;

    /**
     * 订单分类描述
     */
    private String categoryDesc;

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
}
