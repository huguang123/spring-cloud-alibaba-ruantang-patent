package com.ruantang.service.user.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String rolesName;
    private String rolesDescription;
    private Long createTime;
    private Long updateTime;
    
    private List<PermDTO> permissions;
} 