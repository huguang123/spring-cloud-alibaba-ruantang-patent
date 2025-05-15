package com.ruantang.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode
@TableName("sys_roles")
@ApiModel(value = "系统角色对象",description = "系统角色")
public class SysRoles {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    private String rolesCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String rolesName;

    /**
     * 角色类型(0:平台角色 1:企业角色 2：代理所角色)
     */
    @ApiModelProperty(value = "角色类型(1:平台角色 2:企业角色 3：代理所角色)")
    private Integer rolesType;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String rolesDescription;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

}
