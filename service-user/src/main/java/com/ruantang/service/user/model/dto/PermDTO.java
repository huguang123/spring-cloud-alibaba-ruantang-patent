package com.ruantang.service.user.model.dto;

import lombok.Data;

@Data
public class PermDTO {
    private Long id;
    private String permsName;
    private String permsDescription;
    private Long createTime;
    private Long updateTime;
} 