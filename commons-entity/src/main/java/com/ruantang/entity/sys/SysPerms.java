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
@TableName("sys_perms")
@ApiModel(value = "用户权限对象",description = "配置用户权限（操作、删除）")
public class SysPerms implements Serializable {

    private static final long serialVersionUID = -6331561934127218163L;

    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("权限名")
    private String permsName;

    @ApiModelProperty("权限备注")
    private String permsDescription;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

}
