package com.ruantang.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode
@TableName("sys_organization")
@ApiModel(value = "组织对象", description = "包括内部组织对象和外部组织对象")
public class SysOrganization implements Serializable {

    private static final long serialVersionUID = -3877846308871094601L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "组织名")
    private String orgName;

    @ApiModelProperty(value = "组织类型 0-内部专利小组 1-外部商户【客户】组织")
    private String orgType;

    @ApiModelProperty(value = "组织联系电话")
    private String orgPhone;

    @ApiModelProperty(value = "组织联系邮箱")
    private String orgMail;

    @ApiModelProperty(value = "组织状态 0-禁用 1-启用")
    private Integer state;

    @ApiModelProperty(value = "删除状态 0-删除 1-未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

}
