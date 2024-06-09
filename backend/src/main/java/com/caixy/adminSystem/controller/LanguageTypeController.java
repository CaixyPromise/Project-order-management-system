package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.lang.LanguageTypeAddRequest;
import com.caixy.adminSystem.model.dto.lang.LanguageTypeQueryRequest;
import com.caixy.adminSystem.model.dto.lang.LanguageTypeUpdateRequest;
import com.caixy.adminSystem.model.entity.LanguageType;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.lang.LanguageTypeVO;
import com.caixy.adminSystem.service.LanguageTypeService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 语言类型接口
 */
@RestController
@RequestMapping("/languageType")
@Slf4j
public class LanguageTypeController
{

    @Resource
    private LanguageTypeService languageTypeService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建语言类型
     *
     * @param languageTypeAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addLanguageType(@RequestBody LanguageTypeAddRequest languageTypeAddRequest, HttpServletRequest request)
    {
        ThrowUtils.throwIf(languageTypeAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        LanguageType languageType = new LanguageType();
        BeanUtils.copyProperties(languageTypeAddRequest, languageType);
        // 数据校验
        languageTypeService.validLanguageType(languageType, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        languageType.setCreatorId(loginUser.getId());
        // 写入数据库
        boolean result = languageTypeService.save(languageType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newLanguageTypeId = languageType.getId();
        return ResultUtils.success(newLanguageTypeId);
    }

    /**
     * 删除语言类型
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteLanguageType(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        LanguageType oldLanguageType = languageTypeService.getById(id);
        ThrowUtils.throwIf(oldLanguageType == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldLanguageType, user);
        // 操作数据库
        boolean result = languageTypeService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新语言类型（仅管理员可用）
     *
     * @param languageTypeUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateLanguageType(@RequestBody LanguageTypeUpdateRequest languageTypeUpdateRequest)
    {
        if (languageTypeUpdateRequest == null || languageTypeUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        LanguageType languageType = new LanguageType();
        BeanUtils.copyProperties(languageTypeUpdateRequest, languageType);
        // 数据校验
        languageTypeService.validLanguageType(languageType, false);
        // 判断是否存在
        long id = languageTypeUpdateRequest.getId();
        LanguageType oldLanguageType = languageTypeService.getById(id);
        ThrowUtils.throwIf(oldLanguageType == null, ErrorCode.NOT_FOUND_ERROR);

        // 操作数据库
        boolean result = languageTypeService.updateById(languageType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取语言类型（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/voById")
    public BaseResponse<LanguageTypeVO> getLanguageTypeVOById(long id, HttpServletRequest request)
    {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        LanguageType languageType = languageTypeService.getById(id);
        ThrowUtils.throwIf(languageType == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(languageTypeService.getLanguageTypeVO(languageType, request));
    }

    /**
     * 根据 id 获取订单分类（封装类）
     * 用在获取下拉框选项里
     */
    @GetMapping("/get/vo/list")
    public BaseResponse<Map<Long, LanguageTypeVO>> getLangTypeVoS()
    {
        // 查询数据库
        List<LanguageType> languageTypeList = languageTypeService.list();
        // 获取封装类
        return ResultUtils.success(languageTypeService.getLangtYpeVoMap(languageTypeList));
    }

    /**
     * 分页获取语言类型列表（仅管理员可用）
     *
     * @param languageTypeQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<LanguageTypeVO>> listLanguageTypeByPage(@RequestBody LanguageTypeQueryRequest languageTypeQueryRequest)
    {
        long current = languageTypeQueryRequest.getCurrent();
        long size = languageTypeQueryRequest.getPageSize();
        // 查询数据库
        Page<LanguageType> languageTypePage = languageTypeService.page(new Page<>(current, size),
                languageTypeService.getQueryWrapper(languageTypeQueryRequest));

        return ResultUtils.success(languageTypeService.getLanguageTypeVOPage(languageTypePage, null));
    }

    // endregion

    private void checkIsSelfOrAdmin(LanguageType languageType, User loginUser)
    {
        if (!languageType.getCreatorId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }
}
