package com.ruantang.entity.rel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@TableName(value = "rel_roles_perm")
@ApiModel(value = "角色权限映射对象",description = "角色权限映射")
public class RelRolesPerm {

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "角色id")
    private Long rolesId;

    @ApiModelProperty(value = "权限id")
    private Long permsId;

    @ApiModelProperty(value = "权限类型（0：可访问、1：可授权）")
    private Integer rightType;


}
