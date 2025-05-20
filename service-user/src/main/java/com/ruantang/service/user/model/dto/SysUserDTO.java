package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "系统用户DTO对象", description = "系统用户DTO")
public class SysUserDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "所属租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "所属组织ID")
    private Long orgId;

    @ApiModelProperty(value = "职级")
    private Integer level;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "用户邮箱")
    private String userMail;

    @ApiModelProperty(value = "性别（0：男，1：女）")
    private Integer gender;

    @ApiModelProperty(value = "微信联系方式")
    private String weixin;

    @ApiModelProperty(value = "QQ联系方式")
    private String qq;

    @ApiModelProperty(value = "删除状态（0：未删除，1：删除）")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}

