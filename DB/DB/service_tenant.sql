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
