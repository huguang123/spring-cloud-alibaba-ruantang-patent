SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_perm_module
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_module`;
CREATE TABLE `config_perm_module`  (
`id` bigint NOT NULL COMMENT '权限配置模块ID',
`module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模块名称（如：数据看板、交底书管理）',
`parent_id` bigint NULL DEFAULT NULL COMMENT '父模块ID（0为顶级）',
`template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
`module_type` tinyint NOT NULL COMMENT '模块类型（1=功能权限模块 2=数据权限模块）',
`sort` int NULL DEFAULT NULL COMMENT '排序号',
`icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块图标',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置权限模块表（支撑前端树形结构）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_perm_node
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_node`;
CREATE TABLE `config_perm_node`  (
`id` bigint NOT NULL COMMENT '主键',
`node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '显示名称（如：查看数据看板）',
`module_id` bigint NOT NULL COMMENT '所属模块ID',
`node_type` tinyint NOT NULL COMMENT '节点类型（1=菜单项 2=操作按钮 3=数据字段）',
`data_scope` tinyint NULL DEFAULT NULL COMMENT '数据权限类型（当node_type=3时有效，1=查看 2=编辑）',
`perm_type` tinyint NOT NULL COMMENT '绑定权限类型（0:操作权限 1:数据权限）',
`perm_id` bigint NULL DEFAULT NULL COMMENT '绑定操作权限ID',
`data_policy_id` bigint NULL DEFAULT NULL COMMENT '绑定数据权限ID',
`is_basic` tinyint NULL DEFAULT 0 COMMENT '是否基础权限（0=自定义 1=系统预置）',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '前端权限节点' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_perm_template
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_template`;
CREATE TABLE `config_perm_template`  (
`id` bigint NOT NULL COMMENT '模板ID',
`template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码（唯一标识）',
`template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称',
`description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '模板描述',
`template_type` tinyint NOT NULL COMMENT '模板类型（1:平台配置模板 2:企业租户配置模板 3:代理所配置模板）',
`version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本号',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置权限模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for perm
-- ----------------------------
DROP TABLE IF EXISTS `perm`;
CREATE TABLE `perm`  (
`id` bigint NOT NULL COMMENT '主键id',
`perms_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限标识(如order:view)',
`perms_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限名称',
`api_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'HTTP方法(GET、POST、PUT、DELETE)',
`api_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '接口路径',
`perm_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限作用域（PLATFORM:平台角色/TENANT:租户角色/ALL:通用）',
`perms_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限描述',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for perm_data_policy
-- ----------------------------
DROP TABLE IF EXISTS `perm_data_policy`;
CREATE TABLE `perm_data_policy`  (
`id` bigint NOT NULL COMMENT '主键id',
`policy_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略编码',
`policy_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 策略名称',
`condition_type` int NULL DEFAULT 1 COMMENT '条件类型(1:SQL片段 2:SpEL表达式)',
`condition_expression` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '条件表达式',
`effect_tables` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '生效表(逗号分隔)',
`priority` int NULL DEFAULT NULL COMMENT '优先级',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据权限策略表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for perm_rel_policy_binding
-- ----------------------------
DROP TABLE IF EXISTS `perm_rel_policy_binding`;
CREATE TABLE `perm_rel_policy_binding`  (
`id` bigint NOT NULL COMMENT '主键',
`policy_id` bigint NOT NULL COMMENT '策略id',
`bind_type` int NOT NULL COMMENT '绑定类型(1:角色 2:租户)',
`bind_id` bigint NOT NULL COMMENT '角色ID/租户ID',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略绑定关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rel_roles_perm
-- ----------------------------
DROP TABLE IF EXISTS `rel_roles_perm`;
CREATE TABLE `rel_roles_perm`  (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `roles_id` bigint NULL DEFAULT NULL COMMENT '角色id',
                                   `perms_id` bigint NULL DEFAULT NULL COMMENT '权限id',
                                   `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rel_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_roles`;
CREATE TABLE `rel_user_roles`  (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `tenant_id` bigint NOT NULL COMMENT '所属租户ID',
                                   `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
                                   `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
                                   `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles`  (
                              `id` bigint NOT NULL COMMENT '主键id',
                              `roles_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
                              `roles_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
                              `roles_type` int NOT NULL COMMENT '类型(0:系统角色 1:租户角色)',
                              `roles_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
                              `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users`  (
                              `id` bigint UNSIGNED NOT NULL COMMENT '主键id',
                              `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
                              `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                              `tenant_id` bigint NULL DEFAULT NULL COMMENT '所属租户ID',
                              `org_id` bigint NULL DEFAULT NULL COMMENT '所属组织ID',
                              `level` int NULL DEFAULT NULL COMMENT '职级',
                              `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
                              `user_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户手机号',
                              `user_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户邮箱',
                              `gender` int NULL DEFAULT NULL COMMENT '性别（0：男，1：女）',
                              `weixin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信联系方式',
                              `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'QQ联系方式',
                              `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                              `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tenant
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant`  (
                           `id` bigint NOT NULL COMMENT '主键ID',
                           `tenant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户编码',
                           `tenant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户名称',
                           `tenant_type` int NOT NULL COMMENT '租户属性（0：企业商户租户、1：代理所租户、2：平台管理租户）',
                           `tenant_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
                           `tenant_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
                           `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注说明',
                           `tenant_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户地址',
                           `data_isolation_mode` int NOT NULL COMMENT '数据隔离模式（1=行级 2=Schema 3=独立库）',
                           `expire_time` bigint NULL DEFAULT NULL COMMENT '服务到期时间',
                           `admin_user_id` bigint NULL DEFAULT NULL COMMENT '租户管理员ID（关联sys_users.id）',
                           `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                           `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
