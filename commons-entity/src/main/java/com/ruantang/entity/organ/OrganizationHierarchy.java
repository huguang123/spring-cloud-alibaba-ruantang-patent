package com.ruantang.entity.organ;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织层级关系实体类
 */
@Data
@EqualsAndHashCode
@TableName("organization_hierarchy")
@ApiModel(value = "组织层级关系对象", description = "组织层级关系")
public class OrganizationHierarchy {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "祖先节点ID")
    private Long ancestor;

    @ApiModelProperty(value = "后代节点ID")
    private Long descendant;

    @ApiModelProperty(value = "层级深度")
    private Integer depth;
} 