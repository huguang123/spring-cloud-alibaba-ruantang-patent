--------系统用户表----------
sys_users
CREATE TABLE `ruantang_patent_info`.`sys_users`  (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
`login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
`password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
`level` int NULL DEFAULT NULL COMMENT '职级',
`user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
`user_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户手机号',
`user_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户邮箱',
`gender` int NULL DEFAULT NULL COMMENT '性别（0：男，1：女）',
`weixin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信联系方式',
`qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'QQ联系方式',
`is_deleted` int NULL DEFAULT NULL COMMENT '删除状态（0：删除，1：未删除）',
`create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-----------系统角色表------------
sys_roles
CREATE TABLE `ruantang_patent_info`.`sys_roles`  (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
`roles_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
`roles_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

----------系统权限表------------
sys_perms
CREATE TABLE `ruantang_patent_info`.`sys_perms`  (
`id` bigint NOT NULL COMMENT '主键id',
`perms_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限名称',
`perms_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限描述',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

----------系统组织表-------------
sys_organization
CREATE TABLE `ruantang_patent_info`.`sys_organization`  (
`id` bigint NOT NULL COMMENT '主键id',
`org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织名称',
`org_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织属性（0：内部专利小组、1：外部商户【客户】组织）',
`org_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
`org_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系邮箱',
`state` int NULL DEFAULT NULL COMMENT '状态（0：禁用、1：启用）',
`is_deleted` int NULL DEFAULT NULL COMMENT '删除状态（0：删除、1：未删除）',
`description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注说明',
`create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织表' ROW_FORMAT = Dynamic;

----------用户角色映射表------------
rel_user_roles
CREATE TABLE `ruantang_patent_info`.`rel_user_roles`  (
`id` bigint NOT NULL COMMENT '主键id',
`user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
`role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色映射表' ROW_FORMAT = Dynamic;

----------用户组织映射表------------
rel_user_organization
CREATE TABLE `ruantang_patent_info`.`rel_user_organization`  (
`id` bigint NOT NULL COMMENT '主键id',
`user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
`org_id` bigint NULL DEFAULT NULL COMMENT '组织id',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户组织映射表' ROW_FORMAT = Dynamic;

----------角色权限映射表------------
rel_roles_perm
CREATE TABLE `ruantang_patent_info`.`rel_roles_perm`  (
`id` bigint NOT NULL COMMENT '主键id',
`roles_id` bigint NULL DEFAULT NULL COMMENT '角色id',
`perms_id` bigint NULL DEFAULT NULL COMMENT '权限id',
`right_type` int NULL DEFAULT NULL COMMENT '权限类型（0：可访问、1：可授权）',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限映射表' ROW_FORMAT = Dynamic;

-----------组织用户映射表-------------
rel_organization_user
CREATE TABLE `ruantang_patent_info`.`rel_organization_user`  (
`id` bigint NOT NULL COMMENT '主键id',
`org_id` bigint NULL DEFAULT NULL COMMENT '组织id',
`roles_id` bigint NULL DEFAULT NULL COMMENT '角色id',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织角色映射表' ROW_FORMAT = Dynamic;