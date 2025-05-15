- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
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

-- ----------------------------
-- Table structure for organization_hierarchy
-- ----------------------------
DROP TABLE IF EXISTS `organization_hierarchy`;
CREATE TABLE `organization_hierarchy`  (
  `id` bigint NOT NULL COMMENT '主键',
  `ancestor` bigint NOT NULL COMMENT '祖先节点ID',
  `descendant` bigint NOT NULL COMMENT '后代节点ID',
  `depth` int NOT NULL COMMENT '层级深度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组织层级关系表（闭包表）' ROW_FORMAT = DYNAMIC;
