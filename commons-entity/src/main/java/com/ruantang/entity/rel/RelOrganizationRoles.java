package com.ruantang.entity.rel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "组织权限映射对象",description = "组织权限映射")
@TableName(value = "rel_organization_roles")
@Data
@EqualsAndHashCode
public class RelOrganizationRoles {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "组织id")
    private Long orgId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

}
