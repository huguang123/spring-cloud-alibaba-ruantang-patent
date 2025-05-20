package com.ruantang.entity.organ;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织实体类
 */
@Data
@EqualsAndHashCode
@TableName("organization")
@ApiModel(value = "组织对象", description = "组织")
public class Organization {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "父组织ID")
    private Long parentId;

    @ApiModelProperty(value = "所属租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    @ApiModelProperty(value = "层级路径")
    private String orgPath;

    @ApiModelProperty(value = "联系电话")
    private String orgPhone;

    @ApiModelProperty(value = "联系邮箱")
    private String orgMail;

    @ApiModelProperty(value = "状态（0：禁用、1：启用）")
    private Integer state;

    @ApiModelProperty(value = "备注说明")
    private String description;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 