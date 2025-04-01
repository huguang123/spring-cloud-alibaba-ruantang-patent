/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : ruantang_patent_info

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 01/04/2025 15:49:36
*/

create database ruantang_patent_info;

use ruantang_patent_info;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for prom_doc_section_template
-- ----------------------------
DROP TABLE IF EXISTS `prom_doc_section_template`;
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

-- ----------------------------
-- Records of prom_doc_section_template
-- ----------------------------
INSERT INTO `prom_doc_section_template` VALUES (1102217494791327744, 1102201510365892608, 1102215043421966336, '技术背景', 1, NULL, 1742955594600, 1742955645892);
INSERT INTO `prom_doc_section_template` VALUES (1102217534620438528, 1102201510365892608, 1102216107982458880, '技术方案', 2, NULL, 1742955604096, 1742955664955);
INSERT INTO `prom_doc_section_template` VALUES (1102217581672140800, 1102201510365892608, 1102212659442487296, '技术问题', 3, NULL, 1742955615314, 1742955691303);
INSERT INTO `prom_doc_section_template` VALUES (1102217615117520896, 1102201510365892608, 1102213910855028736, '技术效果', 4, NULL, 1742955623288, 1742955711516);
INSERT INTO `prom_doc_section_template` VALUES (1102217661858844672, 1102201510365892608, 1102217175038562304, '实施例1', 5, NULL, 1742955634431, 1743041929093);
INSERT INTO `prom_doc_section_template` VALUES (1102578989576359936, 1102578879643652096, NULL, '背景技术', 1, NULL, 1743041781673, 1743041781673);
INSERT INTO `prom_doc_section_template` VALUES (1102579579769458688, 1102201510365892608, 1102217175038562304, '实施例2', 6, NULL, 1743041922386, 1743041979734);

-- ----------------------------
-- Table structure for prom_doc_template
-- ----------------------------
DROP TABLE IF EXISTS `prom_doc_template`;
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

-- ----------------------------
-- Records of prom_doc_template
-- ----------------------------
INSERT INTO `prom_doc_template` VALUES (1102201510365892608, 1101479527722389504, 1102201401515315200, 1, '电子信息技术领域技术交底书模板', NULL, 1742951783616, 1742951783616);
INSERT INTO `prom_doc_template` VALUES (1102578879643652096, 1101479527722389504, 1101525599727521792, 1, '机械测试交底书', NULL, 1743041755462, 1743041755462);

-- ----------------------------
-- Table structure for prom_doc_template_type
-- ----------------------------
DROP TABLE IF EXISTS `prom_doc_template_type`;
CREATE TABLE `prom_doc_template_type`  (
  `id` bigint NOT NULL COMMENT '主键',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型说明（如“技术交底书”、“说明书”）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型描述',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prom_doc_template_type
-- ----------------------------
INSERT INTO `prom_doc_template_type` VALUES (1101479527722389504, '技术交底书', '技术交底书类型', NULL, 1742779649520, 1742779649520);
INSERT INTO `prom_doc_template_type` VALUES (1101483379599216640, '法律文书', '法律文书类型类型', NULL, 1742780567899, 1742780567899);

-- ----------------------------
-- Table structure for prom_tech_domain
-- ----------------------------
DROP TABLE IF EXISTS `prom_tech_domain`;
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

-- ----------------------------
-- Records of prom_tech_domain
-- ----------------------------
INSERT INTO `prom_tech_domain` VALUES (1101525599727521792, '机械技术领域', NULL, 1, '机械技术领域', 0, NULL, 1742790633962, 1742790633962);
INSERT INTO `prom_tech_domain` VALUES (1102201401515315200, '电子信息技术领域', NULL, 1, '电子信息技术领域，用于演示测试', 0, NULL, 1742951757654, 1742951757654);
INSERT INTO `prom_tech_domain` VALUES (1102274571358310400, '法律领域', NULL, 1, '', 0, NULL, 1742969202701, 1742969202701);
INSERT INTO `prom_tech_domain` VALUES (1102274613464928256, '专利领域', NULL, 1, '', 0, NULL, 1742969212753, 1742969212753);
INSERT INTO `prom_tech_domain` VALUES (1102274867903991808, '软件算法认证', NULL, 1, '', 0, NULL, 1742969273416, 1742969273416);
INSERT INTO `prom_tech_domain` VALUES (1102578666631729152, '机械技术领域', NULL, 1, '测试', 0, NULL, 1743041704675, 1743041704675);
INSERT INTO `prom_tech_domain` VALUES (1102587501547229184, '法律', NULL, 1, '法律', 0, NULL, 1743043811085, 1743043811085);

-- ----------------------------
-- Table structure for prom_template
-- ----------------------------
DROP TABLE IF EXISTS `prom_template`;
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '提示词模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prom_template
-- ----------------------------
INSERT INTO `prom_template` VALUES (1102212659442487296, '技术问题', '电子信息-技术问题提示词', '请基于以下输入：${user_input}  \n使用${spark_approach}方法，生成${spark_knowledge}的技术问题。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742954441763, 1743131590783);
INSERT INTO `prom_template` VALUES (1102213910855028736, '技术效果', '电子信息-技术效果提示词', '请基于用户输入：${user_input}  \n采用${effect_type}技术路径，描述该方案在${effect_scope}中实现的${quantifiable_effect}。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742954740122, 1743131524257);
INSERT INTO `prom_template` VALUES (1102215043421966336, '背景技术', '电子信息-背景技术提示词', '请根据输入内容：${user_input}  \n结合${background_type}分析，阐述电子信息技术领域在${related_tech}方向的发展现状与技术挑战。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742955010148, 1743042756531);
INSERT INTO `prom_template` VALUES (1102216107982458880, '技术方案', '电子信息-技术方案提示词', '请基于技术构思：${user_input}  \n采用${solution_type}方法，详细描述涉及${core_modules}的电子信息技术实现方案。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742955263959, 1743042728678);
INSERT INTO `prom_template` VALUES (1102217175038562304, '实施例', '电子信息-实施例提示词', '请根据技术方案：${user_input}  \n设计${implementation_level}实施案例，具体说明在${test_scenario}中的${verification_method}。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742955518365, 1743042697429);

-- ----------------------------
-- Table structure for prom_template_parameter
-- ----------------------------
DROP TABLE IF EXISTS `prom_template_parameter`;
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

-- ----------------------------
-- Records of prom_template_parameter
-- ----------------------------
INSERT INTO `prom_template_parameter` VALUES (1102582830606716928, 1102217175038562304, 'user_input', 1, '', '用户输入，不需要配置默认值', '用户输入', NULL, 1743042697438, 1743042697438);
INSERT INTO `prom_template_parameter` VALUES (1102582830610911232, 1102217175038562304, 'implementation_level', 1, '芯片级（ASIC/FPGA）、板级（射频/基带）、系统级（基站/终端）', '请输入实施层级（如板级/系统级）', '选择实施层级', NULL, 1743042697438, 1743042697438);
INSERT INTO `prom_template_parameter` VALUES (1102582830610911233, 1102217175038562304, 'test_scenario', 1, '高密度部署环境、极端温度条件、高速移动场景', '请输入测试场景（如极端温度/高速移动）', '测试场景类型', NULL, 1743042697438, 1743042697438);
INSERT INTO `prom_template_parameter` VALUES (1102582830610911234, 1102217175038562304, 'verification_method', 1, '仿真验证（工具+参数）、原型机测试（指标+环境）、对比实验（基准方案）', '请输入验证方法（如原型机测试/对比实验）', '验证方法', NULL, 1743042697438, 1743042697438);
INSERT INTO `prom_template_parameter` VALUES (1102582830615105536, 1102217175038562304, 'example', 1, '在{测试环境}中验证{技术方案}，使用{工具/设备}测得{关键指标}为{数值}，较{对比方案}提升{X%}', '实施例示例模板	', '实施例示例模板	', NULL, 1743042697438, 1743042697438);
INSERT INTO `prom_template_parameter` VALUES (1102582830619299840, 1102217175038562304, 'rules', 1, '1. 需注明测试工具版本（如ADS 2023） 2. 数据需包含误差范围（如±2%）3、尽最大的可能降低输入内容的TOKEN；输出严格按照示例参考、3、输出不要带有任何格式，不要加粗，纯文本输出', '实施例及输出规范', '实施例规范', NULL, 1743042697438, 1743042697438);
INSERT INTO `prom_template_parameter` VALUES (1102582961615802368, 1102216107982458880, 'user_input', 1, '', '用户输入，不需要配置默认值', '用户输入', NULL, 1743042728681, 1743042728681);
INSERT INTO `prom_template_parameter` VALUES (1102582961624190976, 1102216107982458880, 'solution_type', 1, '硬件架构重构、算法模型优化、协议标准改进、系统级协同设计', '请输入方案类型（如算法优化/协议改进）', '选择方案类型', NULL, 1743042728681, 1743042728681);
INSERT INTO `prom_template_parameter` VALUES (1102582961624190977, 1102216107982458880, 'core_modules', 1, '信号处理单元、功耗管理模块、通信协议栈、分布式调度引擎', '请输入核心模块（如功耗管理模块/协议栈）', '核心组件/模块', NULL, 1743042728681, 1743042728681);
INSERT INTO `prom_template_parameter` VALUES (1102582961624190978, 1102216107982458880, 'example', 1, '设计{创新组件}，通过{关键技术}实现{目标功能}，具体包括：{模块1}（负责{功能A}）、{模块2}（解决{问题B}）', '方案示例模板', '方案示例模板', NULL, 1743042728681, 1743042728681);
INSERT INTO `prom_template_parameter` VALUES (1102582961624190979, 1102216107982458880, 'rules', 1, '1. 硬件方案需标注工艺参数 2. 算法需说明输入输出关系 3、尽最大的可能降低输入内容的TOKEN；输出严格按照示例参考、3、输出不要带有任何格式，不要加粗，纯文本输出', '请输入 规范', '方案描述规范	', NULL, 1743042728681, 1743042728681);
INSERT INTO `prom_template_parameter` VALUES (1102583078456528896, 1102215043421966336, 'user_input', 1, '', '用户输入，属于用户提示词，不需要配置默认值', '用户输入', NULL, 1743042756534, 1743042756534);
INSERT INTO `prom_template_parameter` VALUES (1102583078456528897, 1102215043421966336, 'background_type', 1, '行业瓶颈、技术代际差距、政策法规限制、市场需求变化', '请输入背景类型', '选择背景类型', NULL, 1743042756534, 1743042756534);
INSERT INTO `prom_template_parameter` VALUES (1102583078460723200, 1102215043421966336, 'related_tech', 1, '信号处理架构、集成电路设计、网络协议栈、传感器技术', '请输入技术方向（如MIMO/边缘计算）', '关联技术方向', NULL, 1743042756534, 1743042756534);
INSERT INTO `prom_template_parameter` VALUES (1102583078460723201, 1102215043421966336, 'example', 1, '当前{现有技术方案}在{典型场景}中面临{核心问题}，主要表现为{具体表现}（如功耗>XW/误码率≥Y%）', '背景示例模板', '背景示例模板', NULL, 1743042756534, 1743042756534);
INSERT INTO `prom_template_parameter` VALUES (1102583078460723202, 1102215043421966336, 'rules', 1, '1. 需引用近3年行业报告数据 2. 需对比至少两种现有方案缺陷 3.尽最大的可能降低输入内容的TOKEN；输出严格按照示例参考、3、输出不要带有任何格式，不要加粗，纯文本输出', '背景分析及输入规范', '背景分析及输入规范', NULL, 1743042756534, 1743042756534);
INSERT INTO `prom_template_parameter` VALUES (1102955397272178688, 1102213910855028736, 'user_input', 1, '', '用户提示词，不需要配置默认值', '用户输入', NULL, 1743131524260, 1743131524260);
INSERT INTO `prom_template_parameter` VALUES (1102955397276372992, 1102213910855028736, 'effect_type', 1, '能效优化、信号稳定性增强、算法效率提升、资源利用率改进', '请输入效果类型（如能效优化/信号稳定性增强）', '选择效果类型', NULL, 1743131524260, 1743131524260);
INSERT INTO `prom_template_parameter` VALUES (1102955397276372993, 1102213910855028736, 'effect_scope', 1, '高密度通信节点、低功耗终端设备、实时计算系统', '请输入应用场景（如基站部署/终端设备）', '限定技术应用场景', NULL, 1743131524260, 1743131524260);
INSERT INTO `prom_template_parameter` VALUES (1102955397280567296, 1102213910855028736, 'quantifiable_effect', 1, '误差率降低至{目标值}、功耗降幅≥{X%}、时延缩短{时间单位}', '请填写指标（如误差率从{原值}降至{新值}）', '量化指标模板', NULL, 1743131524260, 1743131524260);
INSERT INTO `prom_template_parameter` VALUES (1102955397280567297, 1102213910855028736, 'example', 1, '通过{改进方法}，在{应用场景}下将{关键指标}从{原值}优化至{新值}，提升幅度达{X%}', '效果示例模板', '效果示例模板', NULL, 1743131524260, 1743131524260);
INSERT INTO `prom_template_parameter` VALUES (1102955397280567298, 1102213910855028736, 'rules', 1, '1. 量化指标需包含单位 2. 需说明测试条件3.尽最大的可能降低输入内容的TOKEN；输出严格按照示例参考、3、输出不要带有任何格式，不要加粗，纯文本输出', '默认规范', '默认规范', NULL, 1743131524260, 1743131524260);
INSERT INTO `prom_template_parameter` VALUES (1102955676302446592, 1102212659442487296, 'user_input', 1, '', '用户前端输入，不需要配置默认值', 'user_input 参数', NULL, 1743131590785, 1743131590785);
INSERT INTO `prom_template_parameter` VALUES (1102955676310835200, 1102212659442487296, 'spark_approach', 1, '硬件加速设计、算法优化、协议改进、抗干扰技术', '请输入分析方法（如算法优化/硬件加速设计）', '选择问题分析方法', NULL, 1743131590785, 1743131590785);
INSERT INTO `prom_template_parameter` VALUES (1102955676310835201, 1102212659442487296, 'spark_knowledge', 1, '通信基带处理、芯片能效瓶颈、协议栈冲突、信号同步机制', '请输入技术领域（如5G通信/芯片设计）', '指定技术领域核心知识', NULL, 1743131590785, 1743131590785);
INSERT INTO `prom_template_parameter` VALUES (1102955676310835202, 1102212659442487296, 'example', 1, '现有基于{现有技术方法}的{目标系统}在{特定场景}下存在{技术缺陷}，导致{具体负面影响}', '请填写示例中的{占位符}（如现有技术方法）', '技术问题示例模板', NULL, 1743131590785, 1743131590785);
INSERT INTO `prom_template_parameter` VALUES (1102955676315029504, 1102212659442487296, 'rules', 1, '1. 必须包含可量化缺陷 2. 需限定技术领域 3.尽最大的可能降低输入内容的TOKEN:输出严格按照示例参考、3、输出不要带有任何格式，不要加粗，纯文本输出', '输入规范规定', '输入定义规范', NULL, 1743131590785, 1743131590785);

-- ----------------------------
-- Table structure for rel_organization_roles
-- ----------------------------
DROP TABLE IF EXISTS `rel_organization_roles`;
CREATE TABLE `rel_organization_roles`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `org_id` bigint NULL DEFAULT NULL COMMENT '组织id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织角色映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_organization_roles
-- ----------------------------

-- ----------------------------
-- Table structure for rel_roles_perm
-- ----------------------------
DROP TABLE IF EXISTS `rel_roles_perm`;
CREATE TABLE `rel_roles_perm`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `roles_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `perms_id` bigint NULL DEFAULT NULL COMMENT '权限id',
  `right_type` int NULL DEFAULT NULL COMMENT '权限类型（0：可访问、1：可授权）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_roles_perm
-- ----------------------------

-- ----------------------------
-- Table structure for rel_user_organization
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_organization`;
CREATE TABLE `rel_user_organization`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `org_id` bigint NULL DEFAULT NULL COMMENT '组织id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户组织映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_user_organization
-- ----------------------------

-- ----------------------------
-- Table structure for rel_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_roles`;
CREATE TABLE `rel_user_roles`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_user_roles
-- ----------------------------

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织名称',
  `org_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织属性（0：内部专利小组、1：外部商户【客户】组织）',
  `org_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `org_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `state` int NULL DEFAULT NULL COMMENT '状态（0：禁用、1：启用）',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除状态（0：未删除、1：删除）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统组织表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_organization
-- ----------------------------

-- ----------------------------
-- Table structure for sys_perms
-- ----------------------------
DROP TABLE IF EXISTS `sys_perms`;
CREATE TABLE `sys_perms`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `perms_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限名称',
  `perms_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_perms
-- ----------------------------

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `roles_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `roles_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_roles
-- ----------------------------

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键id',
  `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
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
-- Records of sys_users
-- ----------------------------
INSERT INTO `sys_users` VALUES (1097518610273931264, '张三', '{bcrypt}$2a$10$Q37cyxctlZfJ7iBwR0CaG.6mYb94B2IsbxANvLz4.M7QdZ0uc/N7K', NULL, '用户GW4MW0F7', NULL, '13659205616@163.com', NULL, NULL, NULL, NULL, 1741835293176, NULL);
INSERT INTO `sys_users` VALUES (1097563409056141312, '李四', '{bcrypt}$2a$10$Sv3Ps7Xy3kZ.2LQBS39LieN/neW4zyM8imJ7iuBUSV/lTI77r2zo.', NULL, '用户A6QQIY5J', NULL, '13659205616@163.com', NULL, NULL, NULL, NULL, 1741845974039, NULL);
INSERT INTO `sys_users` VALUES (1097572739394441216, 'zhangshan', '{bcrypt}$2a$10$qsh0pFfMPQAkMw7LqlF6N.bkY89be8fe2iTJWOmJYCaYyaB9oKLxu', NULL, '用户E4DQSOFE', NULL, '13659205616@163.com', NULL, NULL, NULL, NULL, 1741848198565, NULL);

SET FOREIGN_KEY_CHECKS = 1;
