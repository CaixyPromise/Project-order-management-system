package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.common.OptionVO;
import com.caixy.adminSystem.model.dto.lang.LanguageTypeQueryRequest;
import com.caixy.adminSystem.model.entity.LanguageType;
import com.caixy.adminSystem.model.vo.lang.LanguageTypeVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 语言类型服务
 */
public interface LanguageTypeService extends IService<LanguageType>
{

    Boolean checkLangIsExistById(Long id);

    /**
     * 校验数据
     *
     * @param languageType
     * @param add          对创建的数据进行校验
     */
    void validLanguageType(LanguageType languageType, boolean add);

    Map<Long, LanguageTypeVO> getLangTypeVoMap(List<LanguageType> languageTypeList);

    /**
     * 获取查询条件
     *
     * @param languageTypeQueryRequest
     * @return
     */
    QueryWrapper<LanguageType> getQueryWrapper(LanguageTypeQueryRequest languageTypeQueryRequest);

    /**
     * 获取语言类型封装
     *
     * @param languageType
     * @param request
     * @return
     */
    LanguageTypeVO getLanguageTypeVO(LanguageType languageType, HttpServletRequest request);

    /**
     * 分页获取语言类型封装
     *
     * @param languageTypePage
     * @param request
     * @return
     */
    Page<LanguageTypeVO> getLanguageTypeVOPage(Page<LanguageType> languageTypePage, HttpServletRequest request);

    Map<Long, String> getLangNameByIds(Set<Long> langIds);

    List<OptionVO<Long>> convertLangOptionListAndCache(List<LanguageType> languageTypeList);

    List<OptionVO<Long>> getLangOptionList();

    void clearCache();
}
