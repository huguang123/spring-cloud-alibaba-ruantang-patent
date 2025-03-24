# prom_doc_section_template
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档分项模板表' ROW_FORMAT = Dynamic;

# prom_doc_template
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板表' ROW_FORMAT = Dynamic;


# prom_doc_template_type
CREATE TABLE `prom_doc_template_type`  (
                                           `id` bigint NOT NULL COMMENT '主键',
                                           `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型说明（如“技术交底书”、“说明书”）',
                                           `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型描述',
                                           `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` bigint NOT NULL COMMENT '创建时间',
                                           `update_time` bigint NOT NULL COMMENT '更新时间',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板分类表' ROW_FORMAT = Dynamic;

# prom_tech_domain
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '技术领域层级表' ROW_FORMAT = Dynamic;

# prom_template
CREATE TABLE `prom_template`  (
                                  `id` bigint NOT NULL COMMENT '主键',
                                  `section_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模板类型（例如：技术问题、技术效果）',
                                  `prom_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '提示词模板名称',
                                  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提示词模板（含占位符，如${sensor_type}）---------【请基于以下输入：${user_input}  \n使用${spark_approach}方法，生成${spark_knowledge}的技术问题。  \n示例参考：${example}【输出严格按照示例参考】，遵守【规范】${output_rules}-----------',
                                  `weight` int NULL DEFAULT 1 COMMENT '模板优先级（数值越大优先级越高）',
                                  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1.0' COMMENT '模板版本（支持迭代更新）',
                                  `is_deleted` int NULL DEFAULT NULL COMMENT '删除状态（0：未删除，1：删除）',
                                  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` bigint NOT NULL COMMENT '创建时间',
                                  `update_time` bigint NOT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '提示词模板表' ROW_FORMAT = Dynamic;
# porm_template_parameter
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '模板参数表' ROW_FORMAT = Dynamic;