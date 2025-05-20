package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.request.UserQueryRequest;
import com.ruantang.service.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers> implements SysUserService {
    
    @Override
    public SysUserDTO getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        // 查询用户
        LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUsers::getLoginName, username);
        SysUsers user = baseMapper.selectOne(queryWrapper);
        
        if (user == null) {
            return null;
        }
        
        // 转换为DTO
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 不返回敏感信息
        userDTO.setPassword(null);
        
        return userDTO;
    }
    
    @Override
    public ApiResult<Page<SysUserDTO>> queryUserPage(UserQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键字搜索（用户名、手机号、邮箱模糊查询）
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(SysUsers::getUserName, request.getKeyword())
                    .or()
                    .like(SysUsers::getUserPhone, request.getKeyword())
                    .or()
                    .like(SysUsers::getUserMail, request.getKeyword())
            );
        }
        
        // 精确查询条件
        if (StringUtils.hasText(request.getUserName())) {
            queryWrapper.eq(SysUsers::getUserName, request.getUserName());
        }
        
        if (StringUtils.hasText(request.getUserPhone())) {
            queryWrapper.eq(SysUsers::getUserPhone, request.getUserPhone());
        }
        
        if (StringUtils.hasText(request.getUserMail())) {
            queryWrapper.eq(SysUsers::getUserMail, request.getUserMail());
        }
        
        if (request.getGender() != null) {
            queryWrapper.eq(SysUsers::getGender, request.getGender());
        }
        
        if (request.getTenantId() != null) {
            queryWrapper.eq(SysUsers::getTenantId, request.getTenantId());
        }
        
        if (request.getOrgId() != null) {
            queryWrapper.eq(SysUsers::getOrgId, request.getOrgId());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(SysUsers::getCreateTime);
        
        // 分页查询
        Page<SysUsers> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<SysUsers> userPage = baseMapper.selectPage(page, queryWrapper);
        
        // 转换结果
        Page<SysUserDTO> resultPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<SysUserDTO> userDTOList = userPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        resultPage.setRecords(userDTOList);
        
        return ApiResult.success(resultPage);
    }
    
    @Override
    public ApiResult<SysUserDTO> getUserById(Long id) {
        SysUsers user = baseMapper.selectById(id);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        return ApiResult.success(convertToDTO(user));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteUserById(Long id) {
        SysUsers user = baseMapper.selectById(id);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        baseMapper.deleteById(id);
        return ApiResult.success(true);
    }
    
    /**
     * 将用户实体转换为DTO
     * 
     * @param user 用户实体
     * @return 用户DTO
     */
    private SysUserDTO convertToDTO(SysUsers user) {
        if (user == null) {
            return null;
        }
        
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 不返回敏感信息
        userDTO.setPassword(null);
        
        return userDTO;
    }
} 