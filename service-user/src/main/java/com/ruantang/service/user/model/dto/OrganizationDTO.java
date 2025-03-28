package com.ruantang.service.user.model.dto;

import lombok.Data;

@Data
public class OrganizationDTO {
    private Long id;
    private String orgName;
    private String orgType;
    private String orgPhone;
    private String orgMail;
    private Integer state;
    private Integer isDeleted;
    private String description;
    private Long createBy;
    private Long createTime;
    private Long updateTime;
} 