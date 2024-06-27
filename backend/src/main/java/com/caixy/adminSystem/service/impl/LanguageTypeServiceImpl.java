package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.LanguageTypeMapper;
import com.caixy.adminSystem.model.common.OptionVO;
import com.caixy.adminSystem.model.dto.lang.LanguageTypeQueryRequest;
import com.caixy.adminSystem.model.entity.LanguageType;
import com.caixy.adminSystem.model.enums.RedisConstant;
import com.caixy.adminSystem.model.vo.lang.LanguageTypeVO;
import com.caixy.adminSystem.service.LanguageTypeService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.utils.JsonUtils;
import com.caixy.adminSystem.utils.RedisUtils;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 语言类型服务实现
 */
@Service
@Slf4j
    public class LanguageTypeServiceImpl extends ServiceImpl<LanguageTypeMapper, LanguageType> implements LanguageTypeService
{
    private final static Integer MAX_LANG_NAME = 10;

    @Resource
    private UserService userService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 检查语言是否存在
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/7 下午5:36
     */
    @Override
    public Boolean checkLangIsExistById(Long id)
    {
        return this.getById(id) != null;
    }


    /**
     * 校验数据
     *
     * @param languageType
     * @param add          对创建的数据进行校验
     */
    @Override
    public void validLanguageType(LanguageType languageType, boolean add)
    {
        ThrowUtils.throwIf(languageType == null, ErrorCode.PARAMS_ERROR);
        String name = languageType.getLanguageName();
        if (StringUtils.isBlank(name) || name.length() > MAX_LANG_NAME)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言长度不能大于: " + MAX_LANG_NAME);
        }
        // 唯一性判断
        QueryWrapper<LanguageType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("languageName", name);
        ThrowUtils.throwIf(this.count(queryWrapper) > 0, ErrorCode.PARAMS_ERROR, "语言已存在");
    }

    /**
     * 批量获得语言vo
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/7 下午4:17
     */
    @Override
    public Map<Long, LanguageTypeVO> getLangTypeVoMap(List<LanguageType> languageTypeList)
    {
        return LanguageTypeVO.listToIdVoMap(languageTypeList);
    }


    /**
     * 获取查询条件
     *
     * @param languageTypeQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<LanguageType> getQueryWrapper(LanguageTypeQueryRequest languageTypeQueryRequest)
    {
        QueryWrapper<LanguageType> queryWrapper = new QueryWrapper<>();
        if (languageTypeQueryRequest == null)
        {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = languageTypeQueryRequest.getId();
        Long notId = languageTypeQueryRequest.getNotId();
        String title = languageTypeQueryRequest.getTitle();
        String content = languageTypeQueryRequest.getContent();
        String searchText = languageTypeQueryRequest.getSearchText();
        String sortField = languageTypeQueryRequest.getSortField();
        String sortOrder = languageTypeQueryRequest.getSortOrder();
        List<String> tagList = languageTypeQueryRequest.getTags();
        Long userId = languageTypeQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText))
        {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList))
        {
            for (String tag : tagList)
            {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取语言类型封装
     *
     * @param languageType
     * @param request
     * @return
     */
    @Override
    public LanguageTypeVO getLanguageTypeVO(LanguageType languageType, HttpServletRequest request)
    {
        // todo: 补充获取语言类型封装逻辑
        return null;
    }

    /**
     * 分页获取语言类型封装
     *
     * @param languageTypePage
     * @param request
     * @return
     */
    @Override
    public Page<LanguageTypeVO> getLanguageTypeVOPage(Page<LanguageType> languageTypePage, HttpServletRequest request)
    {
        // 获取数据信息
        List<LanguageType> languageTypes = languageTypePage.getRecords();
        Page<LanguageTypeVO> languageTypeVOPage = new Page<>(languageTypePage.getCurrent(), languageTypePage.getSize());
        Set<Long> creatorIds = languageTypes.stream().map(LanguageType::getCreatorId).collect(Collectors.toSet());
        Map<Long, String> userNameByIds = userService.getUserNameByIds(creatorIds);
        // 获取封装类
        List<LanguageTypeVO> languageTypeVOS = languageTypes.stream().map(item -> {
            LanguageTypeVO languageTypeVO = new LanguageTypeVO();
            BeanUtils.copyProperties(item, languageTypeVO);
            String creatorNames = userNameByIds.get(item.getCreatorId());
            if (creatorNames != null && !creatorNames.isEmpty())
            {
                languageTypeVO.setCreatorName(creatorNames);
            }
            else
            {
                languageTypeVO.setCreatorName("未知");
            }
            return languageTypeVO;
        }).collect(Collectors.toList());
        languageTypeVOPage.setRecords(languageTypeVOS);
        languageTypeVOPage.setTotal(languageTypePage.getTotal());
        return languageTypeVOPage;
    }

    @Override
    public Map<Long, String> getLangNameByIds(Set<Long> langIds)
    {
        List<LanguageType> languageTypes = listByIds(langIds);
        return languageTypes.stream().collect(Collectors.toMap(LanguageType::getId, LanguageType::getLanguageName));
    }

    @Override
    public List<OptionVO<Long>> convertLangOptionListAndCache(List<LanguageType> languageTypeList)
    {
        List<OptionVO<Long>> optionVOList =
                languageTypeList.stream().map(item -> new OptionVO<>(item.getId(), item.getLanguageName())).collect(Collectors.toList());
        redisUtils.setString(RedisConstant.LANGUAGE_TYPE, JsonUtils.toJsonString(optionVOList), "type");
        return optionVOList;
    }

    @Override
    public List<OptionVO<Long>> getLangOptionList()
    {
        List<OptionVO<Long>> getCache = redisUtils.getJson(RedisConstant.LANGUAGE_TYPE, "type");
        if (getCache != null)
        {
            log.info("cache hit: {}", getCache);
            return getCache;
        }
        else
        {
            List<LanguageType> languageTypeList = list();
            return convertLangOptionListAndCache(languageTypeList);
        }
    }

    @Override
    public void clearCache()
    {
        redisUtils.delete(RedisConstant.LANGUAGE_TYPE, "type");
    }
}
