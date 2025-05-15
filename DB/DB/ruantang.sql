一、权限管理模块

--配置权限模块表
CREATE TABLE `config_perm_module`  (
`id` bigint NOT NULL COMMENT '权限配置模块ID',
`module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模块名称（如：数据看板、交底书管理）',
`template_version_id` bigint NULL DEFAULT NULL COMMENT '模板版本ID',
`module_type` tinyint NOT NULL COMMENT '模块类型（1=功能权限模块 2=数据权限模块）',
`sort` int NULL DEFAULT NULL COMMENT '排序号',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置权限模块表（支撑前端树形结构）' ROW_FORMAT = DYNAMIC;

--配置权限节点表
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '前端权限节点' ROW_FORMAT = DYNAMIC;

--配置权限模板表
CREATE TABLE `config_perm_template`  (
 `id` bigint NOT NULL COMMENT '模板ID',
 `template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码（唯一标识）',
 `template_type` tinyint NOT NULL COMMENT '模板类型（1:平台配置模板 2:企业租户配置模板 3:代理所配置模板）',
 `base_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '基础描述',
 `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
 `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置权限模板表' ROW_FORMAT = DYNAMIC;

--模板版本配置表
CREATE TABLE `config_perm_version`  (
`id` bigint NOT NULL COMMENT '主键ID',
`template_id` bigint NOT NULL COMMENT '关联模板ID',
`version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语义化版本号',
`is_default` tinyint NULL DEFAULT NULL COMMENT '是否默认版本',
`version_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本描述',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板版本配置表' ROW_FORMAT = Dynamic;


二、组织架构模块

--组织表
CREATE TABLE `organization`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父组织ID',
  `tenant_id` bigint NOT NULL COMMENT '所属租户ID',
  `org_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织编码',
  `org_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '层级路径（如.1.3.5.)',
  `org_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `org_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `state` int NULL DEFAULT NULL COMMENT '状态（0：禁用、1：启用）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统组织表' ROW_FORMAT = DYNAMIC;

--组织层级关系表
CREATE TABLE `organization_hierarchy`  (
  `id` bigint NOT NULL COMMENT '主键',
  `ancestor` bigint NOT NULL COMMENT '祖先节点ID',
  `descendant` bigint NOT NULL COMMENT '后代节点ID',
  `depth` int NOT NULL COMMENT '层级深度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组织层级关系表（闭包表）' ROW_FORMAT = DYNAMIC;


三、权限管理模块

--权限表
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

--数据权限策略表
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


四、文档管理模块

--文档分项模板表
CREATE TABLE `prom_doc_section_template`  (
  `id` bigint NOT NULL COMMENT '主键',
  `doc_template_id` bigint NULL DEFAULT NULL COMMENT '关联文档模板',
  `prompt_id` bigint NULL DEFAULT NULL COMMENT '关联提示词模板',
  `section_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分项名称（如“技术问题”“技术效果”，需枚举约束）',
  `sort_order` int NULL DEFAULT NULL COMMENT '显示顺序',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档分项模板表' ROW_FORMAT = DYNAMIC;

--文档模板表
CREATE TABLE `prom_doc_template`  (
  `id` bigint NOT NULL COMMENT '主键',
  `template_type_id` bigint NULL DEFAULT NULL COMMENT '关联文档模板分类表',
  `domain_id` bigint NULL DEFAULT NULL COMMENT '关联技术领域（NULL表示通用模板）',
  `org_id` bigint NULL DEFAULT NULL COMMENT '关联组织表',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板名称（如“智能灌溉系统技术交底书”）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板表' ROW_FORMAT = DYNAMIC;

--文档模板分类表
CREATE TABLE `prom_doc_template_type`  (
  `id` bigint NOT NULL COMMENT '主键',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型说明（如“技术交底书”、“说明书”）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型描述',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板分类表' ROW_FORMAT = DYNAMIC;


五、技术领域模块

--技术领域表
CREATE TABLE `prom_tech_domain`  (
  `id` bigint NOT NULL COMMENT '主键',
  `domain_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '领域名称（如“机械技术领域” “网络安全技术领域”）',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级领域ID（NULL表示一级领域，支持无限层级扩展）',
  `level` int NOT NULL COMMENT '层级深度（1=一级、2=二级、预留字段支持未来扩展）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '领域描述（用于匹配模糊输入时的上下文关联）',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除状态（0：未删除，1：删除）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tech_unique`(`domain_name` ASC, `parent_id` ASC) USING BTREE COMMENT '唯一索引，防止同级重复命名'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '技术领域层级表' ROW_FORMAT = DYNAMIC;


六、提示词管理模块

--提示词模板表
CREATE TABLE `prom_template`  (
  `id` bigint NOT NULL COMMENT '主键',
  `section_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模板类型（例如：技术问题、技术效果）',
  `prom_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '提示词模板名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提示词模板（含占位符，如${sensor_type}）---------【请基于以下输入：${user_input}  \n使用${spark_approach}方法，生成${spark_knowledge}的技术问题。  \n示例参考：${example}【输出严格按照示例参考】，遵守【规范】${output_rules}-----------',
  `weight` int NULL DEFAULT 1 COMMENT '模板优先级（数值越大优先级越高）',
  `enabled` int NULL DEFAULT NULL COMMENT '禁用状态（0：启用，1：禁用）',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1.0' COMMENT '模板版本（支持迭代更新）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '提示词模板表' ROW_FORMAT = DYNAMIC;

--提示词模板参数表
CREATE TABLE `prom_template_parameter`  (
  `id` bigint NOT NULL COMMENT '主键',
  `template_id` bigint NULL DEFAULT NULL COMMENT '关联模板表',
  `param_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变量名（例如user_input、${sensor_type}）',
  `prom_role` int NULL DEFAULT NULL COMMENT '标识模板类型（0-系统提示词或1-用户提示词）',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数默认值（数据采集→偏差分析→模型修正）',
  `enter_reminder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '输入提醒',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '变量描述',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '模板参数表' ROW_FORMAT = DYNAMIC;

七、角色权限管理模块

--角色权限映射表
CREATE TABLE `rel_roles_perm`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `roles_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `perms_id` bigint NULL DEFAULT NULL COMMENT '权限id',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限映射表' ROW_FORMAT = DYNAMIC;

--策略绑定关系表
CREATE TABLE `perm_rel_policy_binding`  (
  `id` bigint NOT NULL COMMENT '主键',
  `policy_id` bigint NOT NULL COMMENT '策略id',
  `bind_type` int NOT NULL COMMENT '绑定类型(1:角色 2:租户)',
  `bind_id` bigint NOT NULL COMMENT '角色ID/租户ID',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略绑定关系表' ROW_FORMAT = DYNAMIC;

--用户角色映射表
CREATE TABLE `rel_user_roles`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `tenant_id` bigint NOT NULL COMMENT '所属租户ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色映射表' ROW_FORMAT = DYNAMIC;


八、系统管理模块

--操作日志表
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '操作人',
  `tenant_id` bigint NOT NULL COMMENT '操作租户ID',
  `api_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
  `api_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求路径',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作类型',
  `request_data` json NULL COMMENT '请求参数',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_time` bigint NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

--角色表
CREATE TABLE `sys_roles`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `roles_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `roles_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `roles_type` int NOT NULL COMMENT '类型(0:平台角色 1:企业角色 2：代理所角色)',
  `roles_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

--用户表
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

九、租户管理模块

--租户表
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户信息表' ROW_FORMAT = DYNAMIC;

--租户模板角色关联表
CREATE TABLE `tenant_rel_template_role`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `is_inherit` tinyint NOT NULL COMMENT '是否继承权限变更',
  `permission_snapshot` json NULL COMMENT '权限快照(创建时JSON结构)，如果继承权限变更，这里为空，如图不继承权限变更，需要存储权限快照',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板角色关联表' ROW_FORMAT = Dynamic;

--租户模板关联表
CREATE TABLE `tenant_rel_tenant_template`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `bind_mode` tinyint NOT NULL COMMENT '绑定模式(1:继承 2:快照)',
  `effective_time` bigint NULL DEFAULT NULL COMMENT '生效时间戳',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户模板关联表' ROW_FORMAT = Dynamic;

--租户模板表
CREATE TABLE `tenant_template`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码(如: EDU_TEMPLATE)',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称(如: 教育行业模板)',
  `industry_type` tinyint NOT NULL COMMENT '行业类型(0:教育 1:医疗 2:金融)',
  `data_isolation_mode` tinyint NULL DEFAULT NULL COMMENT '数据隔离模式(继承租户配置)',
  `is_system` tinyint NOT NULL COMMENT '是否系统内置模板',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户模板表' ROW_FORMAT = Dynamic;


