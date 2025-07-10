一.service-matter-core服务数据库表
-- ----------------------------
-- Table structure for core_applicant
-- ----------------------------
DROP TABLE IF EXISTS `core_applicant`;
CREATE TABLE `core_applicant`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `client_id` bigint NULL DEFAULT NULL COMMENT '委托人ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请人名称',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '英文名称',
  `applicant_type` tinyint NOT NULL COMMENT '申请人类型(1-个人/2-企业/3-大专院校/4-科研机构/5-机关团体',
  `nationality` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '国籍',
  `registration_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册号(企业适用)',
  `id_type` tinyint NULL DEFAULT NULL COMMENT '证件类型（1-身份证/2-护照/3-营业执照/4-其他）',
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证件号码',
  `contact_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `applicant_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `applicant_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `applicant_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系地址',
  `is_pct_applicant` tinyint NOT NULL COMMENT '是否PCT申请人',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '申请人信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_client
-- ----------------------------
DROP TABLE IF EXISTS `core_client`;
CREATE TABLE `core_client`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业全称',
  `unified_credit_code` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统一社会信用代码',
  `type` int NOT NULL COMMENT '客户类型（1-企业/2-机构/3-个人）',
  `contact_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主要联系人（业务对接负责人）',
  `client_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `client_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `client_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系地址',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '委托人表（企业客户）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_inventor
-- ----------------------------
DROP TABLE IF EXISTS `core_inventor`;
CREATE TABLE `core_inventor`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `client_id` bigint NULL DEFAULT NULL COMMENT '委托人ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发明人姓名',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '英文姓名',
  `nationality` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '国籍（法律要求）',
  `id_type` tinyint NOT NULL COMMENT '证件类型（1-身份证/2-护照/3-其他）',
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '证件号码',
  `contribution_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '贡献描述',
  `inventor_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `inventor_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `inventor_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系地址',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '发明人信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_patent
-- ----------------------------
DROP TABLE IF EXISTS `core_patent`;
CREATE TABLE `core_patent`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `patent_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统案件编号(业务唯一标识)',
  `application_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '官方申请号(申请后获得)',
  `client_id` bigint NOT NULL COMMENT '委托人ID',
  `patent_type` tinyint NOT NULL COMMENT '专利类型(1-发明,2-实用新型,3-外观设计)',
  `patent_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文标题',
  `patent_title_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '英文标题',
  `tech_field_ipc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主IPC分类号',
  `tech_field_cpc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CPC分类号',
  `current_status` tinyint NOT NULL COMMENT '当前状态(\'新申请\',\'撰写中\',\'审核中\',\'客户确认\',\'已递交\',\'审查中\',\'答复中\',\'授权\',\'驳回\',\'终止\')',
  `agent_id` bigint NULL DEFAULT NULL COMMENT '主代理师ID',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  `filing_deadline` bigint NULL DEFAULT NULL COMMENT '法定申请截止日',
  `confidential_flag` tinyint NOT NULL COMMENT '保密标志',
  `version` int NULL DEFAULT 0 COMMENT '版本号(乐观锁)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专利案件主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_patent_applicant
-- ----------------------------
DROP TABLE IF EXISTS `core_patent_applicant`;
CREATE TABLE `core_patent_applicant`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `patent_id` bigint NOT NULL COMMENT '案件ID',
  `applicant_id` bigint NOT NULL COMMENT '申请人ID',
  `is_main_applicant` tinyint NOT NULL COMMENT '是否主申请人',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '案件与申请人关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_patent_document
-- ----------------------------
DROP TABLE IF EXISTS `core_patent_document`;
CREATE TABLE `core_patent_document`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `patent_id` bigint NOT NULL COMMENT '关联案件ID',
  `document_type` tinyint NOT NULL COMMENT '文档类型(1-交底书/2-说明书/3-权利要求书/4-附图/5-摘要/6-审查意见/7-答复书/8-授权书',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件存储url',
  `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '版本号',
  `uploaded_by` bigint NOT NULL COMMENT '上传人ID',
  `uploaded_at` bigint NOT NULL COMMENT '上传时间',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文档描述',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '案件文档表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_patent_inventor
-- ----------------------------
DROP TABLE IF EXISTS `core_patent_inventor`;
CREATE TABLE `core_patent_inventor`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `patent_id` bigint NOT NULL COMMENT '案件ID',
  `inventor_id` bigint NOT NULL COMMENT '发明人ID',
  `is_main_inventor` tinyint NOT NULL DEFAULT 0 COMMENT '是否第一发明人',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '案件与发明人关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_patent_remark
-- ----------------------------
DROP TABLE IF EXISTS `core_patent_remark`;
CREATE TABLE `core_patent_remark`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `patent_id` bigint NOT NULL COMMENT '关联案件ID',
  `remark_type` tinyint NOT NULL COMMENT '备注类型(1-客户要求/2-内部讨论/3-官方通知/4-特别指示)',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注内容',
  `created_by` bigint NOT NULL COMMENT '创建人',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `visibility` tinyint NOT NULL COMMENT '可见范围（1-公开/2-内部/3-机密）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '案件备注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_patent_status_journey
-- ----------------------------
DROP TABLE IF EXISTS `core_patent_status_journey`;
CREATE TABLE `core_patent_status_journey`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `patent_id` bigint NOT NULL COMMENT '关联案件ID',
  `transition_id` bigint NOT NULL COMMENT '迁移规则ID',
  `from_status` tinyint NOT NULL COMMENT '原状态',
  `to_status` tinyint NOT NULL COMMENT '新状态',
  `transition_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态变更原因',
  `operator_id` bigint NOT NULL COMMENT '操作人ID',
  `operation_time` bigint NOT NULL COMMENT '操作时间',
  `related_document_id` bigint NULL DEFAULT NULL COMMENT '相关文档ID',
  `task_id` bigint NULL DEFAULT NULL COMMENT '关联任务ID',
  `deadline_id` bigint NULL DEFAULT NULL COMMENT '关联期限ID',
  `participant_id` bigint NULL DEFAULT NULL COMMENT '关联负责人ID',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '案件状态迁移旅程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_priority_claim
-- ----------------------------
DROP TABLE IF EXISTS `core_priority_claim`;
CREATE TABLE `core_priority_claim`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `patent_id` bigint NOT NULL COMMENT '关联案件ID',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优先权国家/地区',
  `application_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优先权申请号',
  `filing_date` bigint NOT NULL COMMENT '优先权申请日',
  `document_status` tinyint NOT NULL COMMENT '文件状态(1-未提交\\2-已提交\\3-已认证\\4-拒绝)',
  `certified_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优先权证明文件url',
  `is_active` tinyint NOT NULL COMMENT '是否有效声明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优先权声明表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_transition_coordination
-- ----------------------------
DROP TABLE IF EXISTS `core_transition_coordination`;
CREATE TABLE `core_transition_coordination`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID（UUID）',
  `journey_id` bigint NOT NULL COMMENT '关联journey记录',
  `tx_status` tinyint NULL DEFAULT 1 COMMENT '1-PENDING/2-PROCESSING/3-COMPLETED/4-FAILED',
  `expected_callbacks` json NOT NULL COMMENT '预期回调服务',
  `received_callbacks` json NULL COMMENT '已接收回调',
  `created_at` bigint NOT NULL COMMENT '创建时间戳',
  `expiry_time` bigint NOT NULL COMMENT '事务过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '迁移事务协调表' ROW_FORMAT = Dynamic;

二.service-matter-workflow服务数据库表
-- ----------------------------
-- Table structure for workflow_definition
-- ----------------------------
DROP TABLE IF EXISTS `workflow_definition`;
CREATE TABLE `workflow_definition`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `type` tinyint NOT NULL COMMENT '专利类型(1-发明,2-实用新型,3-外观设计)',
  `workflow_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工作流名称',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '版本号',
  `effective_date` bigint NOT NULL COMMENT '生效日期',
  `is_active` tinyint NOT NULL COMMENT '是否激活',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工作流定义表，存储专利、商标等不同类型的流程配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for workflow_event_definition
-- ----------------------------
DROP TABLE IF EXISTS `workflow_event_definition`;
CREATE TABLE `workflow_event_definition`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `workflow_id` bigint NOT NULL COMMENT '关联工作流ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '事件编码(SUBMIT/APPROVE/REJECT)',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '事件名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事件描述',
  `allowed_roles` tinyint NULL DEFAULT NULL COMMENT '允许触发角色(1-代理师/2-审核员/3-客户)',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '事件定义表，存储状态迁移的触发事件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for workflow_patent_cond_field
-- ----------------------------
DROP TABLE IF EXISTS `workflow_patent_cond_field`;
CREATE TABLE `workflow_patent_cond_field`  (
  `id` bigint NOT NULL COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段名称',
  `data_type` tinyint NOT NULL COMMENT '数据类型(1-BOOLEAN/2-ENUM/3-NUMBER)',
  `allowed_values` json NULL COMMENT '允许值列表（枚举数据类型使用）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段描述',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专利条件字段表，存储专利特有的规则条件字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for workflow_state_definition
-- ----------------------------
DROP TABLE IF EXISTS `workflow_state_definition`;
CREATE TABLE `workflow_state_definition`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `workflow_id` bigint NOT NULL COMMENT '关联工作流ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态编码(NEW/DRAFTING/REVIEW)',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态名称',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态类型(INITIAL/PROCESSING/TERMINAL)',
  `is_stable` tinyint NOT NULL COMMENT '是否稳定状态',
  `ui_config` json NULL COMMENT '前端UI配置',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '状态定义表，存储工作流中各个状态的元数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for workflow_transition_action
-- ----------------------------
DROP TABLE IF EXISTS `workflow_transition_action`;
CREATE TABLE `workflow_transition_action`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `rule_id` bigint NOT NULL COMMENT '关联迁移规则ID',
  `action_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '动作类型(CREATE_TASK/SET_DEADLINE)',
  `routing_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'RabbitMQ路由键',
  `parameters` json NOT NULL COMMENT '消息参数',
  `execution_order` int NOT NULL COMMENT '执行顺序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '动作描述',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '迁移动作表，消息队列驱动的动作定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for workflow_transition_condition
-- ----------------------------
DROP TABLE IF EXISTS `workflow_transition_condition`;
CREATE TABLE `workflow_transition_condition`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `rule_id` bigint NOT NULL COMMENT '关联迁移规则ID',
  `field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '条件字段(applicationType/isPCT)',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '比较符(EQ/NEQ/GT)',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '比较值',
  `logic_link` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑连接(AND/OR/NOT)',
  `seq_order` int NOT NULL COMMENT '条件顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '迁移条件表，存储状态迁移的业务规则条件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for workflow_transition_rule
-- ----------------------------
DROP TABLE IF EXISTS `workflow_transition_rule`;
CREATE TABLE `workflow_transition_rule`  (
  `id` bigint NOT NULL COMMENT '主键',
  `workflow_id` bigint NOT NULL COMMENT '关联工作流ID',
  `from_state_id` bigint NOT NULL COMMENT '源状态ID',
  `to_state_id` bigint NOT NULL COMMENT '目标状态ID',
  `event_id` bigint NOT NULL COMMENT '触发事件ID',
  `priority` int NOT NULL DEFAULT 10 COMMENT '规则优先级',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则描述',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_at` bigint NOT NULL COMMENT '创建时间',
  `updated_at` bigint NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '迁移规则表，定义状态迁移的核心规则' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
