package com.caixy.adminSystem.model.vo.lang;

import com.caixy.adminSystem.model.entity.LanguageType;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 语言类型视图
 */
@Data
public class LanguageTypeVO implements Serializable
{

    /**
     * id
     */
    private Long id;

    /**
     * 语言名称
     */
    private String languageName;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 批量返回vo
     */
    private Map<Long, List<LanguageTypeVO>> vos;

    /**
     * 更新时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 封装类转对象
     *
     * @param languageTypeVO
     * @return
     */
    public static LanguageType voToObj(LanguageTypeVO languageTypeVO)
    {
        if (languageTypeVO == null)
        {
            return null;
        }
        LanguageType languageType = new LanguageType();
        BeanUtils.copyProperties(languageTypeVO, languageType);
        return languageType;
    }

    /**
     * 对象转封装类
     *
     * @param languageType
     * @return
     */
    public static LanguageTypeVO objToVo(LanguageType languageType)
    {
        if (languageType == null)
        {
            return null;
        }
        LanguageTypeVO languageTypeVO = new LanguageTypeVO();
        BeanUtils.copyProperties(languageType, languageTypeVO);
        return languageTypeVO;
    }

    public static LanguageTypeVO objToVOs(List<LanguageType> languageTypeList)
    {
        if (languageTypeList == null || languageTypeList.isEmpty())
        {
            return null;
        }
        LanguageTypeVO categoryVO = new LanguageTypeVO();
        // 转VO并根据ID进行分组
        Map<Long, List<LanguageTypeVO>> collect = languageTypeList.stream().map(item -> {
            LanguageTypeVO orderCategoryVO = new LanguageTypeVO();
            BeanUtils.copyProperties(item, orderCategoryVO);
            return orderCategoryVO;
        }).collect(Collectors.groupingBy(LanguageTypeVO::getId));
        categoryVO.setVos(collect);
        return categoryVO;
    }
}
