create database ruantang_patent_info;
--
use ruantang_patent_info;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_perm_module
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_module`;
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

-- ----------------------------
-- Records of config_perm_module
-- ----------------------------
INSERT INTO `config_perm_module` VALUES (1119699347957747712, '企业数据看板', 1119694451464343552, 1, 1, 1747123593191, 1747123593191);
INSERT INTO `config_perm_module` VALUES (1119699528245710848, '客户管理', 1119694451464343552, 1, 2, 1747123636176, 1747123636176);
INSERT INTO `config_perm_module` VALUES (1122503525805461504, '测试模块', 1119694451464343552, 1, 10, 1747792161289, 1747792161289);
INSERT INTO `config_perm_module` VALUES (1122518041469521920, '测试模块', 1747121924671, 1, 10, 1747795622093, 1747795622093);
INSERT INTO `config_perm_module` VALUES (1122577184163237888, '测试模块2', 1119694451464343552, 1, 40, 1747809722810, 1747809722810);
INSERT INTO `config_perm_module` VALUES (1122600242349477888, '测试数据模块', 1119694451464343552, 2, 50, 1747815220311, 1747815220311);
INSERT INTO `config_perm_module` VALUES (1124436170495889408, '提示词工程.技术领域管理', 1124436123284803584, 1, 8, 1748252939690, 1748488891355);
INSERT INTO `config_perm_module` VALUES (1125424540554498048, '组织架构管理中心.组织架构', 1124436123284803584, 1, 2, 1748488585474, 1748488608130);
INSERT INTO `config_perm_module` VALUES (1125424926489186304, '租户管理中心', 1124436123284803584, 1, 3, 1748488677490, 1748488677490);
INSERT INTO `config_perm_module` VALUES (1125425129552220160, '权限配置中心.继承权限配置', 1124436123284803584, 1, 4, 1748488725904, 1748488725904);
INSERT INTO `config_perm_module` VALUES (1125425350843699200, '权限配置中心.系统模板权限配置', 1124436123284803584, 1, 4, 1748488778664, 1748488778664);
INSERT INTO `config_perm_module` VALUES (1125425403914227712, '仪表盘', 1124436123284803584, 1, 1, 1748488791317, 1748488791317);
INSERT INTO `config_perm_module` VALUES (1125425548298948608, '企业模板管理中心.企业模板列表', 1124436123284803584, 1, 6, 1748488825741, 1748488825741);
INSERT INTO `config_perm_module` VALUES (1125425675646406656, '角色管理中心.角色列表', 1124436123284803584, 1, 7, 1748488856103, 1748488856103);
INSERT INTO `config_perm_module` VALUES (1125425929817034752, '提示词工程.提示词模板管理', 1124436123284803584, 1, 9, 1748488916701, 1748488916701);
INSERT INTO `config_perm_module` VALUES (1125426026650931200, '交底书撰写', 1124436123284803584, 1, 10, 1748488939788, 1748488939788);

-- ----------------------------
-- Table structure for config_perm_node
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_node`;
CREATE TABLE `config_perm_node`  (
                                     `id` bigint NOT NULL COMMENT '主键',
                                     `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '显示名称（如：查看数据看板）',
                                     `module_id` bigint NOT NULL COMMENT '所属模块ID',
                                     `node_type` tinyint NOT NULL COMMENT '节点类型（1=菜单项 2=操作按钮 3=数据字段）',
                                     `perm_type` tinyint NOT NULL COMMENT '绑定权限类型（0:操作权限 1:数据权限）',
                                     `perm_id` bigint NULL DEFAULT NULL COMMENT '绑定操作权限ID',
                                     `data_policy_id` bigint NULL DEFAULT NULL COMMENT '绑定数据权限ID',
                                     `sort` int NULL DEFAULT NULL COMMENT '排序号',
                                     `is_basic` tinyint NULL DEFAULT 0 COMMENT '是否基础权限（0=自定义 1=系统预置）',
                                     `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '前端权限节点' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_perm_node
-- ----------------------------
INSERT INTO `config_perm_node` VALUES (1127323893179224064, '根据名称查询提示词', 1124436170495889408, 1, 0, 1127320395775610880, NULL, 1, 1, 1748941426430, 1749107341280);
INSERT INTO `config_perm_node` VALUES (1127323978520727552, '删除章节', 1124436170495889408, 1, 0, 1127314231318941696, NULL, 2, 1, 1748941446777, 1749107341303);
INSERT INTO `config_perm_node` VALUES (1127324191549427712, '编辑章节', 1124436170495889408, 1, 0, 1127313705860730880, NULL, 3, 1, 1748941497566, 1749107341322);
INSERT INTO `config_perm_node` VALUES (1127330129735979008, '创建章节', 1124436170495889408, 1, 0, 1127312366434914304, NULL, 4, 1, 1748942913339, 1749107341341);
INSERT INTO `config_perm_node` VALUES (1127330227366793216, '查看章节', 1124436170495889408, 1, 0, 1127311414541815808, NULL, 5, 1, 1748942936618, 1749107341363);
INSERT INTO `config_perm_node` VALUES (1128008240270020608, '测试', 1124436170495889408, 2, 0, 1127322702932217856, NULL, 7, 1, 1749104587490, 1749107341422);
INSERT INTO `config_perm_node` VALUES (1128018163288641536, '测试2', 1124436170495889408, 1, 0, 1127322702932217856, NULL, 6, 0, 1749106953322, 1749107341391);

-- ----------------------------
-- Table structure for config_perm_template
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_template`;
CREATE TABLE `config_perm_template`  (
                                         `id` bigint NOT NULL COMMENT '模板ID',
                                         `template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码（唯一标识）',
                                         `template_type` tinyint NOT NULL COMMENT '模板类型（1:平台配置模板 2:企业租户配置模板 3:代理所配置模板）',
                                         `base_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '基础描述',
                                         `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                         `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置权限模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_perm_template
-- ----------------------------
INSERT INTO `config_perm_template` VALUES (1747119619032, 'SYS_PLATFORM_DEFAULT', 1, '平台超级租户角色默认的权限配置模板', 1747119619032, 1747120575719);
INSERT INTO `config_perm_template` VALUES (1747119717699, 'SYS_ENT_DEFAULT', 2, '新企业租户默认分配的权限模板', 1747119717699, 1747791661179);
INSERT INTO `config_perm_template` VALUES (1747119746958, 'SYS_AGENT_DEFAULT', 3, '代理所租户角色默认的权限配置模板', 1747119746958, 1747119746958);

-- ----------------------------
-- Table structure for config_perm_version
-- ----------------------------
DROP TABLE IF EXISTS `config_perm_version`;
CREATE TABLE `config_perm_version`  (
                                        `id` bigint NOT NULL COMMENT '主键ID',
                                        `template_id` bigint NOT NULL COMMENT '关联模板ID',
                                        `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语义化版本号',
                                        `is_default` tinyint NULL DEFAULT NULL COMMENT '是否默认版本',
                                        `version_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本描述',
                                        `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                        `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板版本配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_perm_version
-- ----------------------------
INSERT INTO `config_perm_version` VALUES (1747121924671, 1747119717699, 'v2.1.0', 0, '企业模板2.1版本', 1747121924670, 1747793439157);
INSERT INTO `config_perm_version` VALUES (1119694451464343552, 1747119717699, 'v2.0.0', 1, '企业模板2.0大版本', 1747122425747, 1747793455103);
INSERT INTO `config_perm_version` VALUES (1124436123284803584, 1747119619032, '1.0.1', 1, '基于 最新配置 创建的新版本', 1748252928435, 1748253165305);

-- ----------------------------
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
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1125778116703293440, '软唐知识产权', 0, 1121780694490681344, 'RUANTANG', '.1125778116703293440.', '', '', 1, '软唐知识产权管理总组织', NULL, 1748572884597, 1748586053967);
INSERT INTO `organization` VALUES (1125778444773363712, '撰写部', 1125778116703293440, 1121780694490681344, 'WRIT_DEP', '.1125778116703293440.1125778444773363712.', '', '', 1, '软唐撰写部门', NULL, 1748572962816, 1748572962816);
INSERT INTO `organization` VALUES (1125830126320357376, '撰写1组', 1125778444773363712, 1121780694490681344, 'WRIT_001', '.1125778444773363712.1125778116703293440.1125830126320357376.', '', '', 1, '撰写一组', NULL, 1748585284657, 1748585658941);
INSERT INTO `organization` VALUES (1125830536980467712, '撰写2组', 1125778444773363712, 1121780694490681344, 'WRIT_002', '.1125778444773363712.1125778116703293440.1125830536980467712.', '', '', 1, '撰写第二组', NULL, 1748585382566, 1748585630831);
INSERT INTO `organization` VALUES (1125830936441786368, '撰写3组', 1125778444773363712, 1121780694490681344, 'WRIT_003', '.1125778444773363712.1125778116703293440.1125830936441786368.', '', '', 1, '撰写第三组', NULL, 1748585477804, 1748585637239);
INSERT INTO `organization` VALUES (1125831509283049472, '撰写4组', 1125778444773363712, 1121780694490681344, 'WRIT_004', '.1125778444773363712.1125778116703293440.1125831509283049472.', '', '', 1, '撰写第四组', NULL, 1748585614381, 1748585644567);
INSERT INTO `organization` VALUES (1125847958932295680, '答审组', 1125778444773363712, 1121780694490681344, 'ANSWER_TEAM', '.1125778444773363712.1125778116703293440.1125847958932295680.', '', '', 1, '答审组', NULL, 1748589536284, 1748589536284);
INSERT INTO `organization` VALUES (1125848691324882944, '运营部', 1125778116703293440, 1121780694490681344, 'OPERATE_DEP', '.1125778116703293440.1125848691324882944.', '', '', 1, '软唐运营部门', NULL, 1748589710900, 1748589770786);
INSERT INTO `organization` VALUES (1125861268792676352, '流程部', 1125778116703293440, 1121780694490681344, 'PROCESS_DEP', '.1125778116703293440.1125861268792676352.', '', '', 1, '软唐流程部门', NULL, 1748592709601, 1748592709601);
INSERT INTO `organization` VALUES (1125861651309006848, '人事行政部', 1125778116703293440, 1121780694490681344, 'PERSONNEL', '.1125778116703293440.1125861651309006848.', '', '', 1, '软唐人事行政部门', NULL, 1748592800801, 1748592800801);
INSERT INTO `organization` VALUES (1125861946910969856, '财务部', 1125778116703293440, 1121780694490681344, 'FINANCE_DEP', '.1125778116703293440.1125861946910969856.', '', '', 1, '软唐财务部门', NULL, 1748592871278, 1748592871278);
INSERT INTO `organization` VALUES (1125862710072971264, '总经办', 1125778116703293440, 1121780694490681344, 'GENERAL_DEP', '.1125778116703293440.1125862710072971264.', '', '', 1, '软唐总经办', NULL, 1748593053229, 1748593053229);
INSERT INTO `organization` VALUES (1125863025476243456, '成都办事处', 1125778116703293440, 1121780694490681344, 'CHENDU_DEP', '.1125778116703293440.1125863025476243456.', '', '', 1, '软唐成都办事处', NULL, 1748593128428, 1748593128428);
INSERT INTO `organization` VALUES (1125863313075474432, '软件部', 1125778116703293440, 1121780694490681344, 'SOFTWARE_DEP', '.1125778116703293440.1125863313075474432.', '', '', 1, '软唐软件部', NULL, 1748593196996, 1748593196996);
INSERT INTO `organization` VALUES (1125863897291689984, '提示词小组', 1125863313075474432, 1121780694490681344, 'PROMPT_GROP', '.1125863313075474432.1125778116703293440.1125863897291689984.', '', '', 1, '提示词构建小组', NULL, 1748593336284, 1748593336284);

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

-- ----------------------------
-- Records of organization_hierarchy
-- ----------------------------
INSERT INTO `organization_hierarchy` VALUES (1748572884602, 1125778116703293440, 1125778116703293440, 0);
INSERT INTO `organization_hierarchy` VALUES (1748572962825, 1125778444773363712, 1125778444773363712, 0);
INSERT INTO `organization_hierarchy` VALUES (1748572962826, 1125778116703293440, 1125778444773363712, 1);
INSERT INTO `organization_hierarchy` VALUES (1125830126454575104, 1125830126320357376, 1125830126320357376, 0);
INSERT INTO `organization_hierarchy` VALUES (1125830126454575105, 1125778444773363712, 1125830126320357376, 1);
INSERT INTO `organization_hierarchy` VALUES (1125830126454575106, 1125778116703293440, 1125830126320357376, 1);
INSERT INTO `organization_hierarchy` VALUES (1125830536997244928, 1125830536980467712, 1125830536980467712, 0);
INSERT INTO `organization_hierarchy` VALUES (1125830536997244929, 1125778444773363712, 1125830536980467712, 1);
INSERT INTO `organization_hierarchy` VALUES (1125830536997244930, 1125778116703293440, 1125830536980467712, 1);
INSERT INTO `organization_hierarchy` VALUES (1125830936475340800, 1125830936441786368, 1125830936441786368, 0);
INSERT INTO `organization_hierarchy` VALUES (1125830936475340801, 1125778444773363712, 1125830936441786368, 1);
INSERT INTO `organization_hierarchy` VALUES (1125830936475340802, 1125778116703293440, 1125830936441786368, 1);
INSERT INTO `organization_hierarchy` VALUES (1125831509299826688, 1125831509283049472, 1125831509283049472, 0);
INSERT INTO `organization_hierarchy` VALUES (1125831509299826689, 1125778444773363712, 1125831509283049472, 1);
INSERT INTO `organization_hierarchy` VALUES (1125831509299826690, 1125778116703293440, 1125831509283049472, 1);
INSERT INTO `organization_hierarchy` VALUES (1125847958957461504, 1125847958932295680, 1125847958932295680, 0);
INSERT INTO `organization_hierarchy` VALUES (1125847958957461505, 1125778444773363712, 1125847958932295680, 1);
INSERT INTO `organization_hierarchy` VALUES (1125847958957461506, 1125778116703293440, 1125847958932295680, 1);
INSERT INTO `organization_hierarchy` VALUES (1125848691341660160, 1125848691324882944, 1125848691324882944, 0);
INSERT INTO `organization_hierarchy` VALUES (1125848691341660161, 1125778116703293440, 1125848691324882944, 1);
INSERT INTO `organization_hierarchy` VALUES (1125861268813647872, 1125861268792676352, 1125861268792676352, 0);
INSERT INTO `organization_hierarchy` VALUES (1125861268813647873, 1125778116703293440, 1125861268792676352, 1);
INSERT INTO `organization_hierarchy` VALUES (1125861651321589760, 1125861651309006848, 1125861651309006848, 0);
INSERT INTO `organization_hierarchy` VALUES (1125861651321589761, 1125778116703293440, 1125861651309006848, 1);
INSERT INTO `organization_hierarchy` VALUES (1125861946923552768, 1125861946910969856, 1125861946910969856, 0);
INSERT INTO `organization_hierarchy` VALUES (1125861946923552769, 1125778116703293440, 1125861946910969856, 1);
INSERT INTO `organization_hierarchy` VALUES (1125862710089748480, 1125862710072971264, 1125862710072971264, 0);
INSERT INTO `organization_hierarchy` VALUES (1125862710089748481, 1125778116703293440, 1125862710072971264, 1);
INSERT INTO `organization_hierarchy` VALUES (1125863025493020672, 1125863025476243456, 1125863025476243456, 0);
INSERT INTO `organization_hierarchy` VALUES (1125863025493020673, 1125778116703293440, 1125863025476243456, 1);
INSERT INTO `organization_hierarchy` VALUES (1125863313083863040, 1125863313075474432, 1125863313075474432, 0);
INSERT INTO `organization_hierarchy` VALUES (1125863313083863041, 1125778116703293440, 1125863313075474432, 1);
INSERT INTO `organization_hierarchy` VALUES (1125863897300078592, 1125863897291689984, 1125863897291689984, 0);
INSERT INTO `organization_hierarchy` VALUES (1125863897300078593, 1125863313075474432, 1125863897291689984, 1);
INSERT INTO `organization_hierarchy` VALUES (1125863897300078594, 1125778116703293440, 1125863897291689984, 1);

-- ----------------------------
-- Table structure for perm
-- ----------------------------
DROP TABLE IF EXISTS `perm`;
CREATE TABLE `perm`  (
                         `id` bigint NOT NULL COMMENT '主键id',
                         `perms_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限标识(如order:view)',
                         `perm_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限类型（API BUTTON）',
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
-- Records of perm
-- ----------------------------
INSERT INTO `perm` VALUES (1127286737777856512, 'domain_tree:view', 'API', '查看技术领域树数据', 'GET', '/prom/api/tech-domains/tree', NULL, '查询技术领域树数据', 1748932567892, 1748932567892);
INSERT INTO `perm` VALUES (1127287456178245632, 'domain_tree:create', 'API', '创建技术领域树节点', 'POST', '/prom/api/tech-domains', NULL, '创建技术领域节点', 1748932739171, 1748933087601);
INSERT INTO `perm` VALUES (1127288230425792512, 'domain_tree:edit', 'API', '编辑技术领域树节点', 'PUT', '/prom/api/tech-domains/*', NULL, '编辑技术领域树节点', 1748932923767, 1748932923767);
INSERT INTO `perm` VALUES (1127289620132925440, 'domain_tree:delete', 'API', '删除技术领域树节点', 'DELETE', '/prom/api/tech-domains/*', NULL, '删除技术领域树节点', 1748933255098, 1748933255098);
INSERT INTO `perm` VALUES (1127308332558848000, 'doc_templates:view', 'API', '查看文档模板绑定数据', 'GET', '/prom/api/doc-templates/*', NULL, '查看文档模板绑定数据', 1748937716487, 1748937716487);
INSERT INTO `perm` VALUES (1127309259449700352, 'doc_templates:creat', 'API', '创建文档模板', 'POST', '/prom/api/doc-templates', NULL, '创建文档模板', 1748937937477, 1748937937477);
INSERT INTO `perm` VALUES (1127310384458502144, 'doc_templates:edit', 'API', '编辑文档模板', 'PUT', '/prom/api/doc-templates/*', NULL, '编辑文档模板', 1748938205700, 1748938205700);
INSERT INTO `perm` VALUES (1127310691020181504, 'doc_templates:delete', 'API', '删除文档模板', 'DELETE', '/prom/api/doc-templates/*', NULL, '删除文档模板', 1748938278790, 1748938278790);
INSERT INTO `perm` VALUES (1127311414541815808, 'doc_section:view', 'API', '查看文档章节数据', 'GET', '/prom/api/doc-section-templates/*', NULL, '查看章节数据', 1748938451291, 1748939018130);
INSERT INTO `perm` VALUES (1127312366434914304, 'doc_section:create', 'API', '创建文档章节', 'POST', '/prom/api/doc-section-templates', NULL, '创建文档章节', 1748938678238, 1748938678238);
INSERT INTO `perm` VALUES (1127313705860730880, 'doc_section:edit', 'API', '编辑文档章节', 'PUT', '/prom/api/doc-section-templates/*', NULL, '编辑文档章节', 1748938997584, 1748938997584);
INSERT INTO `perm` VALUES (1127314231318941696, 'doc_section:delete', 'API', '删除文档章节', 'DELETE', '/prom/api/doc-section-templates/*', NULL, '删除文档章节', 1748939122862, 1748939122862);
INSERT INTO `perm` VALUES (1127320395775610880, 'prom_by_name:view', 'API', '根据名称查看提示词模板', 'GET', '/prom/api/prom-templates/by-name*', NULL, '根据名称查看提示词模板', 1748940592584, 1748940592584);
INSERT INTO `perm` VALUES (1127321807586725888, 'domain:all', 'API', '技术领域全部权限', '*', '/prom/api/tech-domains/**', NULL, '技术领域全部权限', 1748940929186, 1748940929186);
INSERT INTO `perm` VALUES (1127322404935307264, 'doc_template:all', 'API', '文档模板全部权限', '*', '/prom/api/doc-templates/**', NULL, '文档模板全部权限', 1748941071605, 1748941071605);
INSERT INTO `perm` VALUES (1127322702932217856, 'doc_type:view', 'API', '查看文档类型数据', 'GET', '/prom/api/doc-template-types', NULL, '查看文档类型数据', 1748941142653, 1748941142653);

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
                                     `policy_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '策略描述',
                                     `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据权限策略表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of perm_data_policy
-- ----------------------------
INSERT INTO `perm_data_policy` VALUES (1747042048595, 'DEPT_AND_CHILDREN', '部门及子部门', 2, '@{permissionService.getOrgIdsWithChildren(#current_user.orgId)}', 'sys_order,sys_customer', 50, '查看用户所在部门及其所有子部门数据', 1747042048595, 1747100149561);
INSERT INTO `perm_data_policy` VALUES (1747099204826, 'DEPT_DATA_ONLY', '当前部门数据', 1, 'org_id = #{current_user.orgId}', 'sys_users,sys_roles', 50, '仅查看用户所在部门数据', 1747099204826, 1747099204826);
INSERT INTO `perm_data_policy` VALUES (1747099283110, 'ALL_DATA', '全部数据', 1, '1=1', '*', 10, '查看所有租户数据', 1747099283110, 1747099283110);
INSERT INTO `perm_data_policy` VALUES (1747099335046, 'PERSONAL_DATA', '个人数据', 1, 'create_by = #{current_user.id}', 'sys_task', 90, '仅查看自己创建的数据', 1747099335046, 1747099335046);

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
-- Records of perm_rel_policy_binding
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档分项模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of prom_doc_section_template
-- ----------------------------
INSERT INTO `prom_doc_section_template` VALUES (1102217494791327744, 1102201510365892608, 1102215043421966336, '技术背景', 1, NULL, 1742955594600, 1742955645892);
INSERT INTO `prom_doc_section_template` VALUES (1102217534620438528, 1102201510365892608, 1102216107982458880, '技术方案', 2, NULL, 1742955604096, 1742955664955);
INSERT INTO `prom_doc_section_template` VALUES (1102217581672140800, 1102201510365892608, 1102212659442487296, '技术问题', 3, NULL, 1742955615314, 1742955691303);
INSERT INTO `prom_doc_section_template` VALUES (1102217615117520896, 1102201510365892608, 1102213910855028736, '技术效果', 4, NULL, 1742955623288, 1742955711516);
INSERT INTO `prom_doc_section_template` VALUES (1102217661858844672, 1102201510365892608, 1102217175038562304, '实施例1', 5, NULL, 1742955634431, 1743041929093);
INSERT INTO `prom_doc_section_template` VALUES (1102579579769458688, 1102201510365892608, 1102217175038562304, '实施例2', 6, NULL, 1743041922386, 1743041979734);
INSERT INTO `prom_doc_section_template` VALUES (1109208595871961088, 1109207752586170368, 1109480839370117120, '技术背景', 1, NULL, 1744622402986, 1744783361504);
INSERT INTO `prom_doc_section_template` VALUES (1109208635394887680, 1109207752586170368, 1109481917142667264, '技术方案', 2, NULL, 1744622412408, 1744783385375);
INSERT INTO `prom_doc_section_template` VALUES (1109208680454295552, 1109207752586170368, 1109482870419886080, '技术问题', 3, NULL, 1744622423152, 1744783400081);
INSERT INTO `prom_doc_section_template` VALUES (1109208719947862016, 1109207752586170368, 1109484198663688192, '技术效果', 4, NULL, 1744622432568, 1744783408878);
INSERT INTO `prom_doc_section_template` VALUES (1109208764726251520, 1109207752586170368, 1109485596633600000, '实施例1', 5, NULL, 1744622443244, 1744783416480);
INSERT INTO `prom_doc_section_template` VALUES (1109208800323309568, 1109207752586170368, 1109516698282758144, '实施例2', 6, NULL, 1744622451731, 1744783423557);
INSERT INTO `prom_doc_section_template` VALUES (1109209220185722880, 1109207844621783040, 1109517638465359872, '技术背景', 1, NULL, 1744622551834, 1744783466347);
INSERT INTO `prom_doc_section_template` VALUES (1109209266151100416, 1109207844621783040, 1109518400759140352, '技术方案', 2, NULL, 1744622562793, 1744783473963);
INSERT INTO `prom_doc_section_template` VALUES (1109209664987467776, 1109207844621783040, 1109519201346916352, '技术问题', 3, NULL, 1744622657883, 1744783481506);
INSERT INTO `prom_doc_section_template` VALUES (1109209702715232256, 1109207844621783040, 1109520014521798656, '技术效果', 4, NULL, 1744622666877, 1744783489724);
INSERT INTO `prom_doc_section_template` VALUES (1109209755504742400, 1109207844621783040, 1109520720909701120, '实施例1', 5, NULL, 1744622679464, 1744783497029);
INSERT INTO `prom_doc_section_template` VALUES (1109209790611066880, 1109207844621783040, 1109521335329099776, '实施例2', 6, NULL, 1744622687834, 1744783504013);
INSERT INTO `prom_doc_section_template` VALUES (1109209871703740416, 1109207938431586304, 1109521877832962048, '技术背景', 1, NULL, 1744622707167, 1744783518349);
INSERT INTO `prom_doc_section_template` VALUES (1109209906361274368, 1109207938431586304, 1109523367251283968, '技术方案', 2, NULL, 1744622715431, 1744783525013);
INSERT INTO `prom_doc_section_template` VALUES (1109209944617521152, 1109207938431586304, 1109523913030897664, '技术问题', 3, NULL, 1744622724551, 1744783532318);
INSERT INTO `prom_doc_section_template` VALUES (1109209983972675584, 1109207938431586304, 1109524485423370240, '技术效果', 4, NULL, 1744622733935, 1744783540565);
INSERT INTO `prom_doc_section_template` VALUES (1109210031859044352, 1109207938431586304, 1109527437231919104, '实施例1', 5, NULL, 1744622745352, 1744783547617);
INSERT INTO `prom_doc_section_template` VALUES (1109210074343149568, 1109207938431586304, 1109528090897420288, '实施例2', 6, NULL, 1744622755480, 1744783553757);
INSERT INTO `prom_doc_section_template` VALUES (1109210114872709120, 1109208187422248960, 1109565858495205376, '技术背景', 1, NULL, 1744622765144, 1744783621351);
INSERT INTO `prom_doc_section_template` VALUES (1109210153632272384, 1109208187422248960, 1109566612886917120, '技术方案', 2, NULL, 1744622774385, 1744783638343);
INSERT INTO `prom_doc_section_template` VALUES (1109210189900419072, 1109208187422248960, 1109567284009111552, '技术问题', 3, NULL, 1744622783032, 1744783649776);
INSERT INTO `prom_doc_section_template` VALUES (1109210228324438016, 1109208187422248960, 1109567809890947072, '技术效果', 4, NULL, 1744622792193, 1744783669421);
INSERT INTO `prom_doc_section_template` VALUES (1109210263032303616, 1109208187422248960, 1109568529256026112, '实施例1', 5, NULL, 1744622800468, 1744783677661);
INSERT INTO `prom_doc_section_template` VALUES (1109210293122240512, 1109208187422248960, 1109569153733365760, '实施例2', 6, NULL, 1744622807642, 1744783684064);
INSERT INTO `prom_doc_section_template` VALUES (1109210337175015424, 1109208276953862144, 1109569722938167296, '技术背景', 1, NULL, 1744622818145, 1744783733391);
INSERT INTO `prom_doc_section_template` VALUES (1109210371828355072, 1109208276953862144, 1109570427124060160, '技术方案', 2, NULL, 1744622826407, 1744783743040);
INSERT INTO `prom_doc_section_template` VALUES (1109210407949701120, 1109208276953862144, 1109570968373825536, '技术问题', 3, NULL, 1744622835019, 1744783751092);
INSERT INTO `prom_doc_section_template` VALUES (1109210443320266752, 1109208276953862144, 1109571489914556416, '技术效果', 4, NULL, 1744622843452, 1744783760400);
INSERT INTO `prom_doc_section_template` VALUES (1109210474341339136, 1109208276953862144, 1109572244390154240, '实施例1', 5, NULL, 1744622850848, 1744783766697);
INSERT INTO `prom_doc_section_template` VALUES (1109210520126361600, 1109208276953862144, 1109572734666543104, '实施例2', 6, NULL, 1744622861763, 1744783774306);
INSERT INTO `prom_doc_section_template` VALUES (1112170176293507072, 1112170045485748224, 1112177817069686784, '技术背景', 1, NULL, 1745328498782, 1745373162413);
INSERT INTO `prom_doc_section_template` VALUES (1112170214637834240, 1112170045485748224, 1112180267545661440, '技术问题', 2, NULL, 1745328507924, 1745332593714);
INSERT INTO `prom_doc_section_template` VALUES (1112170276071804928, 1112170045485748224, 1112184885436616704, '技术方案', 3, NULL, 1745328522571, 1745332604255);
INSERT INTO `prom_doc_section_template` VALUES (1112170318077759488, 1112170045485748224, 1112183337918795776, '技术效果', 4, NULL, 1745328532585, 1745332612965);
INSERT INTO `prom_doc_section_template` VALUES (1112170408104300544, 1112170045485748224, 1112186449853288448, '实施例1', 5, NULL, 1745328554049, 1745332622016);
INSERT INTO `prom_doc_section_template` VALUES (1112170914491011072, 1112170045485748224, 1112187280740716544, '实施例2', 6, NULL, 1745328674782, 1745332627744);
INSERT INTO `prom_doc_section_template` VALUES (1113165612999380992, 1113165431574761472, 1113173829842243584, '背景技术', 1, NULL, 1745565829386, 1745568070985);
INSERT INTO `prom_doc_section_template` VALUES (1113165671736414208, 1113165431574761472, NULL, '技术问题', 2, NULL, 1745565843390, 1745565843390);
INSERT INTO `prom_doc_section_template` VALUES (1113165757463793664, 1113165431574761472, NULL, '技术方案', 3, NULL, 1745565863830, 1745565863830);
INSERT INTO `prom_doc_section_template` VALUES (1113165870168936448, 1113165431574761472, NULL, '技术效果', 4, NULL, 1745565890701, 1745565890701);
INSERT INTO `prom_doc_section_template` VALUES (1113165988729327616, 1113165431574761472, NULL, '实施例1', 5, NULL, 1745565918967, 1745565918967);
INSERT INTO `prom_doc_section_template` VALUES (1113166060846190592, 1113165431574761472, NULL, '实施例2', 6, NULL, 1745565936162, 1745565936162);
INSERT INTO `prom_doc_section_template` VALUES (1113168849332736000, 1113168539088457728, 1113173829842243584, '背景技术', 1, NULL, 1745566600989, 1745568033684);
INSERT INTO `prom_doc_section_template` VALUES (1113169105936060416, 1113168539088457728, NULL, '技术问题', 2, NULL, 1745566662168, 1745566662168);
INSERT INTO `prom_doc_section_template` VALUES (1113169151104520192, 1113168539088457728, NULL, '技术方案', 3, NULL, 1745566672937, 1745566672937);
INSERT INTO `prom_doc_section_template` VALUES (1113169180045217792, 1113168539088457728, NULL, '技术效果', 4, NULL, 1745566679837, 1745566679837);
INSERT INTO `prom_doc_section_template` VALUES (1113169238719336448, 1113168539088457728, NULL, '实施例1', 5, NULL, 1745566693826, 1745566693826);
INSERT INTO `prom_doc_section_template` VALUES (1113169280159059968, 1113168539088457728, NULL, '实施例2', 6, NULL, 1745566703706, 1745566703706);
INSERT INTO `prom_doc_section_template` VALUES (1114533058817691648, 1114532940865474560, NULL, '背景技术', 1, NULL, 1745891853875, 1745891853875);
INSERT INTO `prom_doc_section_template` VALUES (1114533104703377408, 1114532940865474560, NULL, '技术问题', 2, NULL, 1745891864816, 1745891864816);
INSERT INTO `prom_doc_section_template` VALUES (1114533146306678784, 1114532940865474560, NULL, '技术方案', 3, NULL, 1745891874735, 1745891874735);
INSERT INTO `prom_doc_section_template` VALUES (1114533201960898560, 1114532940865474560, NULL, '技术效果', 4, NULL, 1745891888003, 1745891888003);
INSERT INTO `prom_doc_section_template` VALUES (1114533253840244736, 1114532940865474560, NULL, '实施例1', 5, NULL, 1745891900373, 1745891900373);
INSERT INTO `prom_doc_section_template` VALUES (1114533306004803584, 1114532940865474560, NULL, '实施例2', 6, NULL, 1745891912809, 1745891912809);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of prom_doc_template
-- ----------------------------
INSERT INTO `prom_doc_template` VALUES (1102201510365892608, 1101479527722389504, 1102201401515315200, 1, '电子信息技术领域技术交底书模板', NULL, 1742951783616, 1742951783616);
INSERT INTO `prom_doc_template` VALUES (1109207752586170368, 1101479527722389504, 1109199229764636672, 1, '电子信息-技术交底书', NULL, 1744622201931, 1744622201931);
INSERT INTO `prom_doc_template` VALUES (1109207844621783040, 1101479527722389504, 1109199717574774784, 1, '机械制造-技术交底书', NULL, 1744622223873, 1744622223873);
INSERT INTO `prom_doc_template` VALUES (1109207938431586304, 1101479527722389504, 1109200095099883520, 1, '材料化工-技术交底书', NULL, 1744622246239, 1744622246239);
INSERT INTO `prom_doc_template` VALUES (1109208187422248960, 1101479527722389504, 1109201521259712512, 1, '建筑工程-技术交底书', NULL, 1744622305604, 1744622305604);
INSERT INTO `prom_doc_template` VALUES (1109208276953862144, 1101479527722389504, 1109205018633965568, 1, '生物医药-技术交底书', NULL, 1744622326950, 1744622326950);
INSERT INTO `prom_doc_template` VALUES (1112170045485748224, 1101479527722389504, 1109205529944788992, 1, '光学仪器-技术交底书-1组', NULL, 1745328467595, 1745328467595);
INSERT INTO `prom_doc_template` VALUES (1113165431574761472, 1101479527722389504, 1109205529944788992, 1, '光学仪器-技术交底书-2组', NULL, 1745565786131, 1745565786131);
INSERT INTO `prom_doc_template` VALUES (1113168539088457728, 1101479527722389504, 1109205529944788992, 1, '光学仪器-技术交底书-03', NULL, 1745566527021, 1745566527021);
INSERT INTO `prom_doc_template` VALUES (1114532940865474560, 1101479527722389504, 1109205529944788992, 1, '光学仪器-技术交底书-4组', NULL, 1745891825746, 1745891825746);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文档模板分类表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '技术领域层级表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of prom_tech_domain
-- ----------------------------
INSERT INTO `prom_tech_domain` VALUES (1102201401515315200, '电子信息技术领域', NULL, 1, '电子信息技术领域，用于演示测试', 0, NULL, 1742951757654, 1742951757654);
INSERT INTO `prom_tech_domain` VALUES (1109098207138418688, '测试沙盒', NULL, 1, '', 0, NULL, 1744596084261, 1744596084261);
INSERT INTO `prom_tech_domain` VALUES (1109199229764636672, '电子信息技术领域-内测', 1109098207138418688, 2, 'IPC分类范围：G01-G16/H01-H05/H03K-H03M；\n文档模板特征：侧重电路拓扑、信号完整性、算法优化；\n验证方法特征：EMC测试/眼图分析/算法仿真验证', 0, NULL, 1744620169931, 1744620169931);
INSERT INTO `prom_tech_domain` VALUES (1109199717574774784, '机械制造技术领域-内测', 1109098207138418688, 2, 'IPC分类范围：B21-B32/F01-F28；\n文档模板特征：突出公差配合、疲劳寿命、热变形分析；\n验证方法特征：三坐标测量/振动台测试/CAE仿真', 0, NULL, 1744620286235, 1744620286235);
INSERT INTO `prom_tech_domain` VALUES (1109200095099883520, '材料化工技术领域-内测', 1109098207138418688, 2, 'IPC分类范围：C01-C14/D01-D07；\n文档模板特征：强调晶体结构、反应机理、催化剂特性；\n验证方法特征：XRD分析/GC-MS检测/流变学测试', 0, NULL, 1744620376244, 1744620376244);
INSERT INTO `prom_tech_domain` VALUES (1109201521259712512, '建筑工程技术领域-内测', 1109098207138418688, 2, 'IPC分类范围：E01-E06；\n文档模板特征：注重载荷分布、抗震性能、材料耐久性；\n验证方法特征：有限元分析/风洞试验/超声波探伤', 0, NULL, 1744620716267, 1744620716267);
INSERT INTO `prom_tech_domain` VALUES (1109205018633965568, '生物医药技术领域-内测', 1109098207138418688, 2, 'IPC分类范围：A61K/A61B/A61M；\n文档模板特征：聚焦药理机制、生物相容性、临床数据；\n验证方法特征：细胞毒性试验/动物实验/HPLC检测', 0, NULL, 1744621550106, 1744621550106);
INSERT INTO `prom_tech_domain` VALUES (1109205529944788992, '光学仪器技术领域-内测', 1109098207138418688, 2, 'IPC分类范围：G02B-G02F/H01S；\n文档模板特征：关注光路设计、波长精度、能量损耗；\n验证方法特征：干涉仪检测/光谱分析/MTF测试', 0, NULL, 1744621672012, 1744621672012);
INSERT INTO `prom_tech_domain` VALUES (1113171472085553152, '测试技术领域', 1109098207138418688, 2, '用于测试', 1, NULL, 1745567226302, 1745567269218);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '提示词模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of prom_template
-- ----------------------------
INSERT INTO `prom_template` VALUES (1102212659442487296, '技术问题', '电子信息-技术问题提示词', '请基于以下输入：${user_input}  \n使用${spark_approach}方法，生成${spark_knowledge}的技术问题。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742954441763, 1743131590783);
INSERT INTO `prom_template` VALUES (1102213910855028736, '技术效果', '电子信息-技术效果提示词', '请基于用户输入：${user_input}  \n采用${effect_type}技术路径，描述该方案在${effect_scope}中实现的${quantifiable_effect}。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742954740122, 1743131524257);
INSERT INTO `prom_template` VALUES (1102215043421966336, '背景技术', '电子信息-背景技术提示词', '请根据输入内容：${user_input}  \n结合${background_type}分析，阐述电子信息技术领域在${related_tech}方向的发展现状与技术挑战。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742955010148, 1743042756531);
INSERT INTO `prom_template` VALUES (1102216107982458880, '技术方案', '电子信息-技术方案提示词', '请基于技术构思：${user_input}  \n采用${solution_type}方法，详细描述涉及${core_modules}的电子信息技术实现方案。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742955263959, 1743042728678);
INSERT INTO `prom_template` VALUES (1102217175038562304, '实施例', '电子信息-实施例提示词', '请根据技术方案：${user_input}  \n设计${implementation_level}实施案例，具体说明在${test_scenario}中的${verification_method}。  \n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1742955518365, 1743042697429);
INSERT INTO `prom_template` VALUES (1109480839370117120, '背景技术', '内测-电子信息-背景技术提示词', '请基于以下输入：${user_input}\n描述${technical_field}领域中${key_technology}的技术发展现状，分析${application_scenario}场景下的核心痛点。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744687310893, 1744688215553);
INSERT INTO `prom_template` VALUES (1109481917142667264, '技术方案', '内测-电子信息-技术方案提示词', '请基于以下输入：${user_input}\n使用${implementation_level}层级的${core_technology}，设计${system_module}实施方案。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744687567855, 1744688196445);
INSERT INTO `prom_template` VALUES (1109482870419886080, '技术问题', '内测-电子信息-技术问题提示词', '请基于以下输入：${user_input}\n分析现有${existing_technology}在${problem_scenario}中导致的${negative_impact}。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744687795133, 1744688181783);
INSERT INTO `prom_template` VALUES (1109484198663688192, '技术效果', '内测-电子信息-技术效果提示词', '请基于以下输入：${user_input}\n验证${technical_improvement}在${test_condition}下的${evaluation_indicator}提升效果。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744688111812, 1744688164496);
INSERT INTO `prom_template` VALUES (1109485596633600000, '实施例', '内测-电子信息-实施例1（硬件实施）提示词', '请基于以下输入：${user_input}\n说明${hardware_platform}的${module_design}及${performance_metric}优化方案。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744688445113, 1744688445113);
INSERT INTO `prom_template` VALUES (1109516698282758144, '实施例', '内测-电子信息-实施例2（软件实施）提示词', '请基于以下输入：${user_input}\n阐述${algorithm}在${development_tool}中的${optimization_method}实现。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744695860325, 1744695860325);
INSERT INTO `prom_template` VALUES (1109517638465359872, '背景技术', '内测-机械制造-背景技术提示词', '请基于以下输入：${user_input}\n分析${technical_field}领域中${key_technology}的技术发展瓶颈，阐述在${application_scenario}中的产业需求。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744696084481, 1744696084481);
INSERT INTO `prom_template` VALUES (1109518400759140352, '技术方案', '内测-机械制造-技术方案提示词', '请基于以下输入：${user_input}\n设计${implementation_level}层级的${core_technology}实施方案，涉及${system_module}优化。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744696266227, 1744696266227);
INSERT INTO `prom_template` VALUES (1109519201346916352, '技术问题', '内测-机械制造-技术问题提示词', '请基于以下输入：${user_input}\n分析${existing_technology}在${problem_scenario}中引发的${negative_impact}。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744696457102, 1744696457102);
INSERT INTO `prom_template` VALUES (1109520014521798656, '技术效果', '内测-机械制造-技术效果提示词', '请基于以下输入：${user_input}\n验证${technical_improvement}在${test_condition}下的${evaluation_indicator}提升效果。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744696650978, 1744696650978);
INSERT INTO `prom_template` VALUES (1109520720909701120, '实施例', '内测-机械制造-实施例1（实施设备）提示词', '请基于以下输入：${user_input}\n说明${equipment_model}的${module_design}及${performance_metric}优化方案。\n示例参考：${example}，规范：${rules}\n', 5, 1, '1.0', NULL, 1744696819394, 1744696819394);
INSERT INTO `prom_template` VALUES (1109521335329099776, '实施例', '内测-机械制造-实施例2（工艺实施）提示词', '请基于以下输入：${user_input}\n阐述${process_method}在${production_scenario}中的${optimization_strategy}实现。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744696965883, 1744696965883);
INSERT INTO `prom_template` VALUES (1109521877832962048, '背景技术', '内测-材料化工-背景技术提示词', '请基于以下输入：${user_input}\n分析${technical_field}领域中${key_technology}的发展瓶颈，阐述在${application_scenario}中的行业需求。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744697095226, 1744697095226);
INSERT INTO `prom_template` VALUES (1109523367251283968, '技术方案', '内测-材料化工-技术方案提示词', '请基于以下输入：${user_input}\n设计${implementation_level}层级的${core_technology}实施方案，涉及${system_module}优化。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744697450330, 1744697450330);
INSERT INTO `prom_template` VALUES (1109523913030897664, '技术问题', '内测-材料化工-技术问题提示词', '请基于以下输入：${user_input}\n分析${existing_technology}在${problem_scenario}中引发的${negative_impact}。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744697580455, 1744697580455);
INSERT INTO `prom_template` VALUES (1109524485423370240, '技术效果', '内测-材料化工-技术效果提示词', '请基于以下输入：${user_input}\n验证${technical_improvement}在${test_condition}下的${evaluation_indicator}提升效果。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744697716924, 1744697716924);
INSERT INTO `prom_template` VALUES (1109527437231919104, '实施例', '内测-材料化工-实施例1（合成工艺）提示词', '请基于以下输入：${user_input}\n说明${equipment_model}的${process_parameter}及${yield_control}方案。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744698420690, 1744698420690);
INSERT INTO `prom_template` VALUES (1109528090897420288, '实施例', '内测-材料化工-实施例2（性能测试）提示词', '请基于以下输入：${user_input}\n阐述${characterization_method}在${material_property}中的${test_standard}实现。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744698576536, 1744698576536);
INSERT INTO `prom_template` VALUES (1109565858495205376, '背景技术', '内测-建筑工程-背景技术提示词', '请基于以下输入：${user_input}\n分析${technical_field}中${key_technology}的技术瓶颈，阐述在${application_scenario}场景下的工程需求。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744707581023, 1744707581023);
INSERT INTO `prom_template` VALUES (1109566612886917120, '技术方案', '内测-建筑工程-技术方案提示词', '请基于以下输入：${user_input}\n设计${implementation_level}层级的${core_technology}实施方案，涉及${system_module}优化。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744707760893, 1744707760893);
INSERT INTO `prom_template` VALUES (1109567284009111552, '技术问题', '内测-建筑工程-技术问题提示词', '请基于以下输入：${user_input}\n分析${existing_technology}在${problem_scenario}中引发的${negative_impact}。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744707920901, 1744707920901);
INSERT INTO `prom_template` VALUES (1109567809890947072, '技术效果', '内测-建筑工程-技术效果提示词', '请基于以下输入：${user_input}\n验证${technical_improvement}在${test_condition}下的${evaluation_indicator}提升效果。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708046281, 1744708046281);
INSERT INTO `prom_template` VALUES (1109568529256026112, '实施例', '内测-建筑工程-实施例1（施工工艺）提示词', '请基于以下输入：${user_input}\n说明${equipment_model}的${process_parameter}及${quality_control}方案。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708217791, 1744708217791);
INSERT INTO `prom_template` VALUES (1109569153733365760, '实施例', '内测-建筑工程-实施例2（结构设计）提示词', '请基于以下输入：${user_input}\n阐述${design_method}在${load_condition}中的${safety_factor}实现。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708366677, 1744708366677);
INSERT INTO `prom_template` VALUES (1109569722938167296, '背景技术', '内测-生物医药-背景技术提示词', '请基于以下输入：${user_input}\n分析${technical_field}中${key_technology}的技术瓶颈，阐述在${application_scenario}场景下的临床需求。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708502388, 1744709268456);
INSERT INTO `prom_template` VALUES (1109570427124060160, '技术方案', '内测-生物医药-技术方案提示词', '请基于以下输入：${user_input}\n设计${implementation_level}层级的${core_technology}实施方案，涉及${system_module}优化。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708670278, 1744709297209);
INSERT INTO `prom_template` VALUES (1109570968373825536, '技术问题', '内测-生物医药-技术问题提示词', '请基于以下输入：${user_input}\n分析${existing_technology}在${problem_scenario}中引发的${negative_impact}。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708799323, 1744709327041);
INSERT INTO `prom_template` VALUES (1109571489914556416, '技术效果', '内测-生物医药-技术效果提示词', '请基于以下输入：${user_input}\n验证${technical_improvement}在${test_condition}下的${evaluation_indicator}提升效果。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744708923668, 1744709347194);
INSERT INTO `prom_template` VALUES (1109572244390154240, '实施例', '内测-生物医药-实施例1（体外实验）提示词', '请基于以下输入：${user_input}\n说明${experimental_model}的${protocol_design}及${parameter_control}方案。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744709103548, 1744709361000);
INSERT INTO `prom_template` VALUES (1109572734666543104, '实施例', '内测-生物医药-实施例2（动物实验）提示词', '请基于以下输入：${user_input}\n阐述${animal_model}在${dosing_regimen}中的${safety_assessment}实现。\n示例参考：${example}，规范：${rules}', 5, 1, '1.0', NULL, 1744709220439, 1744783794678);
INSERT INTO `prom_template` VALUES (1112177817069686784, '背景技术', '内测-光学仪器-背景技术提示词-1组', '请基于用户输入的核心技术点：${user_input}，描述现有基于${existing_technology}的${target_system}在${specific_scenario}下存在${technical_deficiency}，导致${negative_impact}，这些问题亟待解决。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745330320485, 1745330361757);
INSERT INTO `prom_template` VALUES (1112180267545661440, '技术问题', '内测-光学仪器-技术问题提示词-1组', '本发明的技术问题是如何在${specific_scenario}下${problem_phenomenon}，从而${negative_consequence}，该问题直接影响了${system_name}的${system_function}。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745330904723, 1745331679123);
INSERT INTO `prom_template` VALUES (1112183337918795776, '技术效果', '内测-光学仪器-技术效果提示词-1组', '通过采用${solution_method}，本发明能够有效解决${technical_deficiency}问题，提升${performance_indicators}，并且具有${additional_benefits}，从而提高${system_name}的${system_function}。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745331636757, 1745331698973);
INSERT INTO `prom_template` VALUES (1112184885436616704, '技术方案', '内测-光学仪器-技术方案提示词-1组', '本发明针对${technical_problem}，提出了${solution_method}，该方案通过${solution_details}，有效解决了现有技术中存在的${technical_deficiency}问题。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745332005715, 1745332130353);
INSERT INTO `prom_template` VALUES (1112186449853288448, '实施例', '内测-光学仪器-实施例1提示词-1组', '在实施例1中，采用了${solution_method}，通过${operation_details}，实现了${technical_effect}，该实施例表明${system_name}在${specific_scenario}下具备了${desired_performance}。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745332378701, 1745332378701);
INSERT INTO `prom_template` VALUES (1112187280740716544, '实施例', '内测-光学仪器-实施例2提示词-1组', '在实施例2中，${system_name}通过${advanced_technology}和${methodology}，解决了${problem_scenario}中的${specific_issue}，达到了${result}，验证了该技术方案的可行性和优势。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745332576800, 1745332576800);
INSERT INTO `prom_template` VALUES (1113173829842243584, '背景技术', '内测-光学仪器-背景技术提示词-2组', '请基于以下输入：${user_input}  \n描述${technical_field}领域中${current_technology}的技术现状，分析${application_scenario}场景下的核心问题。  \n示例参考：${example}，规范：${rules}  ', 5, 1, '1.0', NULL, 1745567788435, 1745567960549);
INSERT INTO `prom_template` VALUES (1113180508256342016, '背景技术', '内测-光学仪器-背景技术-3组', '请基于以下输入：${user_input}  \n阐述${technical_field}领域中的${key_technology}技术的现有应用情况，分析${application_scenario}场景下的核心痛点。  \n示例参考：${example}，规则：${rules}', 5, 1, '1.0', NULL, 1745569380692, 1745569380692);
INSERT INTO `prom_template` VALUES (1113182302676062208, '技术方案', '内测-光学仪器-技术方案-3组', '请基于以下输入：${user_input}  \n阐述基于${key_technology}的${technical_field}方案如何解决${technical_problem}问题，达到${desired_effect}效果。  \n示例参考：${example}，规则：${rules}\n', 5, 1, '1.0', NULL, 1745569808515, 1745569808515);
INSERT INTO `prom_template` VALUES (1113183415919841280, '技术问题', '内测-光学仪器-技术问题-3组', '请基于以下输入：${user_input}  \n阐述${technical_field}领域中，现有技术如何未能有效解决${technical_problem}问题，导致了${negative_consequence}的产生。  \n示例参考：${example}，规则：${rules}', 5, 1, '1.0', NULL, 1745570073933, 1745570073933);
INSERT INTO `prom_template` VALUES (1114535599953547264, '背景技术', '内侧-光学仪器-背景技术-4组', '请基于以下输入：${user_input}\n描述光学仪器领域中${key_technology}的技术发展现状，分析${application_scenario}场景下的核心痛点。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745892459728, 1745893016435);
INSERT INTO `prom_template` VALUES (1114538527070228480, '技术问题', '内侧-光学仪器-技术问题-4组', '结合${user_input}与${example}，说明${technical_solution}如何突破${existing_limitation}。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745893157609, 1745893157609);
INSERT INTO `prom_template` VALUES (1114538988150067200, '技术方案', '内侧-光学仪器-技术方案-4组', '针对${user_input}中描述的${core_issue}，提出基于${technical_scheme}的解决方案。\n示例参考：${example}\n规范要求：${rules}', 5, 1, '1.0', NULL, 1745893267538, 1745893267538);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '模板参数表' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `prom_template_parameter` VALUES (1109484419648983040, 1109484198663688192, 'user_input', 1, '', '如：新型信道编码方案', '用户输入的技术改进点', NULL, 1744688164499, 1744688164499);
INSERT INTO `prom_template_parameter` VALUES (1109484419653177344, 1109484198663688192, 'technical_improvement', 0, '抗干扰算法/功耗优化方案/传输协议', '输入改进的技术点', '技术改进点', NULL, 1744688164499, 1744688164499);
INSERT INTO `prom_template_parameter` VALUES (1109484419653177345, 1109484198663688192, 'test_condition', 0, '24小时压力测试/极端温度环境/多节点并发', '描述测试环境', '测试条件', NULL, 1744688164499, 1744688164499);
INSERT INTO `prom_template_parameter` VALUES (1109484419653177346, 1109484198663688192, 'evaluation_indicator', 0, '吞吐量/时延/误码率', '输入检测指标', '评估指标', NULL, 1744688164499, 1744688164499);
INSERT INTO `prom_template_parameter` VALUES (1109484419653177347, 1109484198663688192, 'example', 0, '在{测试场景}中，{改进技术}使{评估指标}从{原始数值}提升至{优化后数值}', '可替换占位符内容', '示例模板', NULL, 1744688164499, 1744688164499);
INSERT INTO `prom_template_parameter` VALUES (1109484419653177348, 1109484198663688192, 'rules', 0, '1.必须包含对比数据；2.单位使用国际标准；3.禁用主观描述', '请输入 rules', '输出规范', NULL, 1744688164499, 1744688164499);
INSERT INTO `prom_template_parameter` VALUES (1109484492151721984, 1109482870419886080, 'user_input', 1, '', '如：信噪比下降30%', '用户输入的问题现象', NULL, 1744688181785, 1744688181785);
INSERT INTO `prom_template_parameter` VALUES (1109484492155916288, 1109482870419886080, 'existing_technology', 0, 'OFDM调制/CSMA-CD协议/Manchester编码', '输入被替代的技术名称', '现有技术方法', NULL, 1744688181785, 1744688181785);
INSERT INTO `prom_template_parameter` VALUES (1109484492155916289, 1109482870419886080, 'problem_scenario', 0, '高多普勒频移环境/多用户并发访问/长距离传输', '描述具体问题场景', '问题发生场景', NULL, 1744688181785, 1744688181785);
INSERT INTO `prom_template_parameter` VALUES (1109484492155916290, 1109482870419886080, 'negative_impact', 0, '误码率升高/传输延迟增加/功耗上升', '输入具体影响指标', '负面影响', NULL, 1744688181785, 1744688181785);
INSERT INTO `prom_template_parameter` VALUES (1109484492155916291, 1109482870419886080, 'example', 0, '{现有技术}在{问题场景}下因{缺陷原因}导致{量化指标}恶化', '可替换占位符内容', '示例模板', NULL, 1744688181785, 1744688181785);
INSERT INTO `prom_template_parameter` VALUES (1109484492155916292, 1109482870419886080, 'rules', 0, '1.需量化数据对比；2.包含因果关系链；3.禁用主观描述', '请输入 rules', '输出规范', NULL, 1744688181785, 1744688181785);
INSERT INTO `prom_template_parameter` VALUES (1109484553648607232, 1109481917142667264, 'user_input', 1, '', '输入待解决的技术问题', '用户输入的技术痛点', NULL, 1744688196447, 1744688196447);
INSERT INTO `prom_template_parameter` VALUES (1109484553648607233, 1109481917142667264, 'implementation_level', 0, '芯片级/板级/系统级', '选择实施层级', '实施层级', NULL, 1744688196447, 1744688196447);
INSERT INTO `prom_template_parameter` VALUES (1109484553648607234, 1109481917142667264, 'core_technology', 0, '并行计算架构/自适应滤波算法/高速串行接口', '输入核心技术名称', '核心技术', NULL, 1744688196447, 1744688196447);
INSERT INTO `prom_template_parameter` VALUES (1109484553648607235, 1109481917142667264, 'system_module', 0, '射频前端模块/电源管理单元/数据预处理单元', '输入模块名称', '系统模块', NULL, 1744688196447, 1744688196447);
INSERT INTO `prom_template_parameter` VALUES (1109484553652801536, 1109481917142667264, 'example', 0, '通过{创新技术手段}在{目标硬件平台}实现{关键指标}优化，具体包括：{技术特征1}、{技术特征2}', '可替换占位符内容', '示例模板', NULL, 1744688196447, 1744688196447);
INSERT INTO `prom_template_parameter` VALUES (1109484553652801537, 1109481917142667264, 'rules', 0, '1.分硬件/软件描述；2.包含3个以上技术特征；3.禁用代码片段', '请输入 rules', '输出规范', NULL, 1744688196447, 1744688196447);
INSERT INTO `prom_template_parameter` VALUES (1109484633793368064, 1109480839370117120, 'user_input', 1, '', '请输入核心技术点', '用户输入的技术主题', NULL, 1744688215555, 1744688215555);
INSERT INTO `prom_template_parameter` VALUES (1109484633793368065, 1109480839370117120, 'technical_field', 0, '电子信息系统/半导体器件/通信协议 中选择一个或几个', '选择所属技术领域', '技术领域分类', NULL, 1744688215555, 1744688215555);
INSERT INTO `prom_template_parameter` VALUES (1109484633793368066, 1109480839370117120, 'key_technology', 0, '信号调制技术/低功耗设计/抗干扰算法 中选择一个或几个', '输入关键技术名称', '关键技术点', NULL, 1744688215555, 1744688215555);
INSERT INTO `prom_template_parameter` VALUES (1109484633793368067, 1109480839370117120, 'application_scenario', 0, '5G基站通信/工业物联网环境/高密度计算集群 中选择一个或几个', '描述具体应用场景', '应用场景', NULL, 1744688215555, 1744688215555);
INSERT INTO `prom_template_parameter` VALUES (1109484633797562368, 1109480839370117120, 'example', 0, '现有基于{现有技术方法}的{目标系统}在{特定场景}下存在{技术缺陷}，导致{具体负面影响}', '可替换占位符内容', '示例模板', NULL, 1744688215555, 1744688215555);
INSERT INTO `prom_template_parameter` VALUES (1109484633797562369, 1109480839370117120, 'rules', 0, '1.限定200字内；2.需引出后续技术问题；3.禁用专业术语缩写 ', '请输入 rules', '输出规范', NULL, 1744688215555, 1744688215555);
INSERT INTO `prom_template_parameter` VALUES (1109485596637794304, 1109485596633600000, 'user_input', 1, '', '如：支持100Gbps吞吐量', '用户输入的硬件需求', NULL, 1744688445115, 1744688445115);
INSERT INTO `prom_template_parameter` VALUES (1109485596641988608, 1109485596633600000, 'hardware_platform', 0, 'Xilinx UltraScale+/Intel Stratix 10/Altera Cyclone', '输入芯片型号', '硬件平台', NULL, 1744688445115, 1744688445115);
INSERT INTO `prom_template_parameter` VALUES (1109485596641988609, 1109485596633600000, 'module_design', 0, '时钟树分布/电源网络/信号完整性', '描述设计重点', '模块设计', NULL, 1744688445115, 1744688445115);
INSERT INTO `prom_template_parameter` VALUES (1109485596641988610, 1109485596633600000, 'performance_metric', 0, '最大频率/功耗/误码率', '输入优化指标', '性能指标', NULL, 1744688445115, 1744688445115);
INSERT INTO `prom_template_parameter` VALUES (1109485596641988611, 1109485596633600000, 'example', 0, '采用{硬件平台}实现{功能模块}，通过{设计方法}使{性能指标}达到{目标值}', '可替换占位符内容', '示例模板', NULL, 1744688445115, 1744688445115);
INSERT INTO `prom_template_parameter` VALUES (1109485596641988612, 1109485596633600000, 'rules', 0, '1.分步描述开发流程；2.标注关键参数；3.禁用代码片段', '请输入 rules', '输出规范', NULL, 1744688445115, 1744688445115);
INSERT INTO `prom_template_parameter` VALUES (1109516698291146752, 1109516698282758144, 'user_input', 1, '', '如：实时信道估计', '用户输入的软件功能', NULL, 1744695860327, 1744695860327);
INSERT INTO `prom_template_parameter` VALUES (1109516698291146753, 1109516698282758144, 'algorithm', 0, 'CMA盲均衡/LMS自适应滤波/FFT频域分析', '输入算法名称', '核心算法', NULL, 1744695860327, 1744695860327);
INSERT INTO `prom_template_parameter` VALUES (1109516698295341056, 1109516698282758144, 'development_tool', 0, 'MATLAB 5G Toolbox/Vivado HLS/Keil MDK', '输入工具名称', '开发工具', NULL, 1744695860327, 1744695860327);
INSERT INTO `prom_template_parameter` VALUES (1109516698295341057, 1109516698282758144, 'optimization_method', 0, '并行计算优化/内存占用压缩/迭代次数控制', '描述优化手段', '优化方法', NULL, 1744695860327, 1744695860327);
INSERT INTO `prom_template_parameter` VALUES (1109516698295341058, 1109516698282758144, 'example', 0, '使用{开发工具}实现{核心算法}，通过{优化方法}使{性能指标}改善{提升幅度}', '可替换占位符内容', '示例模板', NULL, 1744695860327, 1744695860327);
INSERT INTO `prom_template_parameter` VALUES (1109516698295341059, 1109516698282758144, 'rules', 0, '1.需说明测试边界条件；2.时间/空间复杂度分析；3.禁用数学公式', '请输入 rules', '输出规范', NULL, 1744695860327, 1744695860327);
INSERT INTO `prom_template_parameter` VALUES (1109517638473748480, 1109517638465359872, 'user_input', 1, '', '如：高精度曲面加工', '用户输入的技术主题', NULL, 1744696084483, 1744696084483);
INSERT INTO `prom_template_parameter` VALUES (1109517638473748481, 1109517638465359872, 'technical_field', 0, '切削加工/增材制造/精密铸造', '选择所属技术领域', '技术领域分类', NULL, 1744696084483, 1744696084483);
INSERT INTO `prom_template_parameter` VALUES (1109517638473748482, 1109517638465359872, 'key_technology', 0, '刀具涂层技术/热变形控制/拓扑优化', '输入关键技术名称', '关键技术点', NULL, 1744696084483, 1744696084483);
INSERT INTO `prom_template_parameter` VALUES (1109517638473748483, 1109517638465359872, 'application_scenario', 0, '航空航天零部件制造/汽车模具生产/医疗器械成型', '描述具体应用场景', '应用场景', NULL, 1744696084483, 1744696084483);
INSERT INTO `prom_template_parameter` VALUES (1109517638473748484, 1109517638465359872, 'example', 0, '现有基于{现有技术方法}的{加工设备}在{特定场景}下存在{技术缺陷}，导致{具体负面影响}', '可替换占位符内容', '示例模板', NULL, 1744696084483, 1744696084483);
INSERT INTO `prom_template_parameter` VALUES (1109517638473748485, 1109517638465359872, 'rules', 0, '1.限定200字内；2.需引出后续技术问题；3.禁用行业缩略语', '请输入 rules', '输出规范', NULL, 1744696084483, 1744696084483);
INSERT INTO `prom_template_parameter` VALUES (1109518400767528960, 1109518400759140352, 'user_input', 1, '', '如：钛合金切削刀具磨损过快', '用户输入的技术痛点', NULL, 1744696266229, 1744696266229);
INSERT INTO `prom_template_parameter` VALUES (1109518400767528961, 1109518400759140352, 'implementation_level', 0, '设备级/产线级/工厂级', '选择实施层级', '实施层级', NULL, 1744696266229, 1744696266229);
INSERT INTO `prom_template_parameter` VALUES (1109518400771723264, 1109518400759140352, 'core_technology', 0, '切削参数优化/振动抑制算法/材料去除率控制', '输入核心技术名称', '核心技术', NULL, 1744696266229, 1744696266229);
INSERT INTO `prom_template_parameter` VALUES (1109518400771723265, 1109518400759140352, 'system_module', 0, '主轴驱动系统/冷却润滑系统/误差补偿装置', '输入模块名称', '系统模块', NULL, 1744696266229, 1744696266229);
INSERT INTO `prom_template_parameter` VALUES (1109518400771723266, 1109518400759140352, 'example', 0, '通过{创新技术手段}在{目标设备}实现{关键指标}提升，具体包括：{技术特征1}、{技术特征2}', '可替换占位符内容', '示例模板', NULL, 1744696266229, 1744696266229);
INSERT INTO `prom_template_parameter` VALUES (1109518400771723267, 1109518400759140352, 'rules', 0, '1.分机械结构/控制系统描述；2.包含3个以上技术特征；3.禁用公式代码', '请输入 rules', '输出规范', NULL, 1744696266229, 1744696266229);
INSERT INTO `prom_template_parameter` VALUES (1109519201355304960, 1109519201346916352, 'user_input', 1, '', '如：加工表面粗糙度超标', '用户输入的问题现象', NULL, 1744696457104, 1744696457104);
INSERT INTO `prom_template_parameter` VALUES (1109519201355304961, 1109519201346916352, 'existing_technology', 0, '传统车削工艺/普通涂层刀具/人工检测', '输入被替代的技术名称', '现有技术方法', NULL, 1744696457104, 1744696457104);
INSERT INTO `prom_template_parameter` VALUES (1109519201359499264, 1109519201346916352, 'problem_scenario', 0, '高硬度材料加工/复杂曲面成型/大批量生产', '描述具体问题场景', '问题发生场景', NULL, 1744696457104, 1744696457104);
INSERT INTO `prom_template_parameter` VALUES (1109519201359499265, 1109519201346916352, 'negative_impact', 0, '加工精度下降/良品率降低/生产成本上升', '输入具体影响指标', '负面影响', NULL, 1744696457104, 1744696457104);
INSERT INTO `prom_template_parameter` VALUES (1109519201363693568, 1109519201346916352, 'example', 0, '{现有技术}在{问题场景}下因{缺陷原因}导致{量化指标}超出允许范围±{阈值}', '可替换占位符内容', '示例模板', NULL, 1744696457104, 1744696457104);
INSERT INTO `prom_template_parameter` VALUES (1109519201363693569, 1109519201346916352, 'rules', 0, '1.需包含公差数据；2.与技术效果章节指标对应；3.禁用主观描述', '请输入 rules', '输出规范', NULL, 1744696457104, 1744696457104);
INSERT INTO `prom_template_parameter` VALUES (1109520014530187264, 1109520014521798656, 'user_input', 1, '', ' 如：新型刀具涂层工艺', '用户输入的技术改进点', NULL, 1744696650980, 1744696650980);
INSERT INTO `prom_template_parameter` VALUES (1109520014530187265, 1109520014521798656, 'technical_improvement', 0, '切削参数优化/冷却系统升级/误差补偿算法', '输入改进的技术点', '技术改进点', NULL, 1744696650980, 1744696650980);
INSERT INTO `prom_template_parameter` VALUES (1109520014530187266, 1109520014521798656, 'test_condition', 0, '连续加工72小时/高温高压环境/多材质混合加工', '描述测试环境', '测试条件', NULL, 1744696650980, 1744696650980);
INSERT INTO `prom_template_parameter` VALUES (1109520014530187267, 1109520014521798656, 'evaluation_indicator', 0, '表面粗糙度Ra值/刀具寿命/加工效率', '输入检测指标', '评估指标', NULL, 1744696650980, 1744696650980);
INSERT INTO `prom_template_parameter` VALUES (1109520014530187268, 1109520014521798656, 'example', 0, '在{测试场景}中，{改进技术}使{评估指标}从{原始值}优化至{目标值}，公差控制在±{精度范围}', '可替换占位符内容', '示例模板', NULL, 1744696650980, 1744696650980);
INSERT INTO `prom_template_parameter` VALUES (1109520014534381568, 1109520014521798656, 'rules', 0, '1.必须包含实测数据对比；2.单位使用国标(如Ra 0.8μm)；3.禁用主观评价', '请输入 rules', '输出规范', NULL, 1744696650980, 1744696650980);
INSERT INTO `prom_template_parameter` VALUES (1109520720918089728, 1109520720909701120, 'user_input', 1, '', '如：五轴联动加工中心', '用户输入的设备需求', NULL, 1744696819396, 1744696819396);
INSERT INTO `prom_template_parameter` VALUES (1109520720918089729, 1109520720909701120, 'equipment_model', 0, 'DMG MORI NHX6300/HAAS UMC-750/MAZAK INTEGREX i-200', '输入设备型号', '设备型号', NULL, 1744696819396, 1744696819396);
INSERT INTO `prom_template_parameter` VALUES (1109520720918089730, 1109520720909701120, 'module_design', 0, '主轴扭矩提升装置/刀库换刀机构/热变形补偿系统', '描述设计重点', '模块设计', NULL, 1744696819396, 1744696819396);
INSERT INTO `prom_template_parameter` VALUES (1109520720918089731, 1109520720909701120, 'performance_metric', 0, '定位精度/最大进给速度/重复定位精度', '输入优化指标', '性能指标', NULL, 1744696819396, 1744696819396);
INSERT INTO `prom_template_parameter` VALUES (1109520720918089732, 1109520720909701120, 'example', 0, '采用{设备型号}的{功能模块}，通过{设计方法}实现{性能指标}达到{目标值}，较上一代提升{改进幅度}', '可替换占位符内容', '示例模板', NULL, 1744696819396, 1744696819396);
INSERT INTO `prom_template_parameter` VALUES (1109520720918089733, 1109520720909701120, 'rules', 0, '1.分机械结构/控制系统描述；2.标注关键参数（如C轴定位精度±0.001°）', '请输入 rules', '输出规范', NULL, 1744696819396, 1744696819396);
INSERT INTO `prom_template_parameter` VALUES (1109521335337488384, 1109521335329099776, 'user_input', 1, '', '如：钛合金薄壁件加工', '用户输入的工艺需求', NULL, 1744696965885, 1744696965885);
INSERT INTO `prom_template_parameter` VALUES (1109521335337488385, 1109521335329099776, 'process_method', 0, '高速铣削/激光熔覆/精密铸造', '输入工艺名称', '工艺方法', NULL, 1744696965885, 1744696965885);
INSERT INTO `prom_template_parameter` VALUES (1109521335337488386, 1109521335329099776, 'production_scenario', 0, '小批量多品种/大批量连续生产/定制化制造', '描述应用场景', '生产场景', NULL, 1744696965885, 1744696965885);
INSERT INTO `prom_template_parameter` VALUES (1109521335337488387, 1109521335329099776, 'optimization_strategy', 0, '切削路径规划/层间温度控制/残余应力消除', '输入优化手段', '优化策略', NULL, 1744696965885, 1744696965885);
INSERT INTO `prom_template_parameter` VALUES (1109521335337488388, 1109521335329099776, 'example', 0, '在{生产场景}中，通过{优化策略}使{工艺方法}的{关键指标}优化{改进值}，良品率提升至{目标百分比}', '可替换占位符内容', '示例模板', NULL, 1744696965885, 1744696965885);
INSERT INTO `prom_template_parameter` VALUES (1109521335337488389, 1109521335329099776, 'rules', 0, '1.需说明工艺参数（如进给速度500mm/min）；2.与设备实施例数据联动', '请输入 rules', '输出规范', NULL, 1744696965885, 1744696965885);
INSERT INTO `prom_template_parameter` VALUES (1109521877845544960, 1109521877832962048, 'user_input', 1, '', '如：高纯度石墨烯制备', '用户输入的技术主题', NULL, 1744697095228, 1744697095228);
INSERT INTO `prom_template_parameter` VALUES (1109521877845544961, 1109521877832962048, 'technical_field', 0, '高分子合成/纳米材料/催化反应', '选择所属技术领域', '技术领域分类', NULL, 1744697095228, 1744697095228);
INSERT INTO `prom_template_parameter` VALUES (1109521877845544962, 1109521877832962048, 'key_technology', 0, '溶剂热法/化学气相沉积/分子自组装', '输入关键技术名称', '关键技术点', NULL, 1744697095228, 1744697095228);
INSERT INTO `prom_template_parameter` VALUES (1109521877845544963, 1109521877832962048, 'application_scenario', 0, '新能源电池材料/生物医用材料/环保涂料', '描述具体应用场景', '应用场景', NULL, 1744697095228, 1744697095228);
INSERT INTO `prom_template_parameter` VALUES (1109521877845544964, 1109521877832962048, 'example', 0, '现有基于{传统方法}的{目标材料}在{应用场景}下存在{技术缺陷}，导致{具体问题}', '可替换占位符内容', '示例模板', NULL, 1744697095228, 1744697095228);
INSERT INTO `prom_template_parameter` VALUES (1109521877845544965, 1109521877832962048, 'rules', 0, '1.限定200字内；2.需引出后续技术问题；3.禁用化学式缩写', '请输入 rules', '输出规范', NULL, 1744697095228, 1744697095228);
INSERT INTO `prom_template_parameter` VALUES (1109523367255478272, 1109523367251283968, 'user_input', 1, '', '如：催化剂失活速率快', '用户输入的技术痛点', NULL, 1744697450332, 1744697450332);
INSERT INTO `prom_template_parameter` VALUES (1109523367255478273, 1109523367251283968, 'implementation_level', 0, '实验室级/中试级/工业化级', '选择实施层级', '实施层级', NULL, 1744697450332, 1744697450332);
INSERT INTO `prom_template_parameter` VALUES (1109523367255478274, 1109523367251283968, 'core_technology', 0, '表面修饰技术/结晶度控制/反应动力学优化', '输入核心技术名称', '核心技术', NULL, 1744697450332, 1744697450332);
INSERT INTO `prom_template_parameter` VALUES (1109523367255478275, 1109523367251283968, 'system_module', 0, '反应釜温控系统/物料输送系统/废气处理装置', '输入模块名称', '系统模块', NULL, 1744697450332, 1744697450332);
INSERT INTO `prom_template_parameter` VALUES (1109523367259672576, 1109523367251283968, 'example', 0, '通过{创新技术}在{目标设备}实现{关键参数}优化，具体包括：{技术特征1}、{技术特征2}', '可替换占位符内容', '示例模板', NULL, 1744697450332, 1744697450332);
INSERT INTO `prom_template_parameter` VALUES (1109523367259672577, 1109523367251283968, 'rules', 0, '1.分工艺参数/设备设计描述；2.包含3个以上技术特征；3.禁用化学方程式', '请输入 rules', '输出规范', NULL, 1744697450332, 1744697450332);
INSERT INTO `prom_template_parameter` VALUES (1109523913039286272, 1109523913030897664, 'user_input', 1, '', '如：复合材料界面结合力不足', '用户输入的问题现象', NULL, 1744697580457, 1744697580457);
INSERT INTO `prom_template_parameter` VALUES (1109523913039286273, 1109523913030897664, 'existing_technology', 0, '机械混合法/传统烧结工艺/湿法浸渍', '输入被替代的技术名称', '现有技术方法', NULL, 1744697580457, 1744697580457);
INSERT INTO `prom_template_parameter` VALUES (1109523913043480576, 1109523913030897664, 'problem_scenario', 0, '高温高压环境/长期耐候性测试/复杂应力加载', '描述具体问题场景', '问题发生场景', NULL, 1744697580457, 1744697580457);
INSERT INTO `prom_template_parameter` VALUES (1109523913043480577, 1109523913030897664, 'negative_impact', 0, '材料强度下降/使用寿命缩短/生产成本上升', '输入具体影响指标', '负面影响', NULL, 1744697580457, 1744697580457);
INSERT INTO `prom_template_parameter` VALUES (1109523913043480578, 1109523913030897664, 'example', 0, '{现有技术}在{问题场景}下因{缺陷原因}导致{检测指标}劣化至{阈值以下}', '可替换占位符内容', '示例模板', NULL, 1744697580457, 1744697580457);
INSERT INTO `prom_template_parameter` VALUES (1109523913043480579, 1109523913030897664, 'rules', 0, '1.需包含ASTM测试标准数据；2.与技术效果章节指标对应；3.禁用主观描述', '请输入 rules', '输出规范', NULL, 1744697580457, 1744697580457);
INSERT INTO `prom_template_parameter` VALUES (1109524485431758848, 1109524485423370240, 'user_input', 1, '', '如：新型催化剂配方', '用户输入的技术改进点', NULL, 1744697716925, 1744697716925);
INSERT INTO `prom_template_parameter` VALUES (1109524485431758849, 1109524485423370240, 'technical_improvement', 0, '反应温度控制/溶剂配比优化/纳米分散技术', '输入改进的技术点', '技术改进点', NULL, 1744697716925, 1744697716925);
INSERT INTO `prom_template_parameter` VALUES (1109524485431758850, 1109524485423370240, 'test_condition', 0, '连续反应100小时/极端PH环境/高纯度要求', '描述测试环境', '测试条件', NULL, 1744697716925, 1744697716925);
INSERT INTO `prom_template_parameter` VALUES (1109524485431758851, 1109524485423370240, 'evaluation_indicator', 0, '转化率/产物纯度/选择性', '输入检测指标', '评估指标', NULL, 1744697716925, 1744697716925);
INSERT INTO `prom_template_parameter` VALUES (1109524485431758852, 1109524485423370240, 'example', 0, '在{测试场景}中，{改进技术}使{评估指标}从{原始值}提升至{目标值}±{误差范围}', '可替换占位符内容', '示例模板', NULL, 1744697716925, 1744697716925);
INSERT INTO `prom_template_parameter` VALUES (1109524485431758853, 1109524485423370240, 'rules', 0, '1.必须包含GC/MS检测数据；2.单位使用国际标准（如mol/L）；3.禁用主观评价', '请输入 rules', '输出规范', NULL, 1744697716925, 1744697716925);
INSERT INTO `prom_template_parameter` VALUES (1109527437244502016, 1109527437231919104, 'user_input', 1, '', '如：石墨烯批量制备', '用户输入的工艺需求', NULL, 1744698420693, 1744698420693);
INSERT INTO `prom_template_parameter` VALUES (1109527437244502017, 1109527437231919104, 'equipment_model', 0, '高压反应釜KCFD-5000/管式炉GSL-1700X/球磨机QM-3SP2', '输入设备型号', '设备型号', NULL, 1744698420693, 1744698420693);
INSERT INTO `prom_template_parameter` VALUES (1109527437244502018, 1109527437231919104, 'process_parameter', 0, '温度梯度控制/压力调节范围/搅拌速率', '描述关键参数', '工艺参数', NULL, 1744698420693, 1744698420693);
INSERT INTO `prom_template_parameter` VALUES (1109527437244502019, 1109527437231919104, 'yield_control', 0, '相分离抑制/副产物去除/结晶度优化', '输入控制方法', '收率控制', NULL, 1744698420693, 1744698420693);
INSERT INTO `prom_template_parameter` VALUES (1109527437244502020, 1109527437231919104, 'example', 0, '采用{设备型号}通过{工艺参数}实现{目标产物}收率达{目标值}，杂质含量≤{阈值}', '可替换占位符内容', '示例模板', NULL, 1744698420693, 1744698420693);
INSERT INTO `prom_template_parameter` VALUES (1109527437244502021, 1109527437231919104, 'rules', 0, '1.分步骤描述合成流程；2.标注关键参数（如压力5MPa±0.2）', '请输入 rules', '输出规范', NULL, 1744698420693, 1744698420693);
INSERT INTO `prom_template_parameter` VALUES (1109528090901614592, 1109528090897420288, 'user_input', 1, '', '如：复合材料耐腐蚀性测试', '用户输入的测试需求', NULL, 1744698576537, 1744698576537);
INSERT INTO `prom_template_parameter` VALUES (1109528090905808896, 1109528090897420288, 'characterization_method', 0, 'XRD衍射分析/BET比表面积测试/电化学阻抗谱', '输入测试方法', '表征方法', NULL, 1744698576537, 1744698576537);
INSERT INTO `prom_template_parameter` VALUES (1109528090905808897, 1109528090897420288, 'material_property', 0, '热稳定性/导电性/机械强度', '输入检测性能', '材料性能', NULL, 1744698576537, 1744698576537);
INSERT INTO `prom_template_parameter` VALUES (1109528090905808898, 1109528090897420288, 'test_standard', 0, 'ASTM G31-12/ISO 178:2019/GB/T 1040.2-2006', '输入标准编号', '测试标准', NULL, 1744698576537, 1744698576537);
INSERT INTO `prom_template_parameter` VALUES (1109528090905808899, 1109528090897420288, 'example', 0, '依据{测试标准}采用{表征方法}测得{材料性能}达到{目标值}，较传统材料提升{改进幅度}', '可替换占位符内容', '示例模板', NULL, 1744698576537, 1744698576537);
INSERT INTO `prom_template_parameter` VALUES (1109528090905808900, 1109528090897420288, 'rules', 0, '1.需注明检测仪器型号（如ZEISS SIGMA 300）；2.与合成实施例数据联动', '请输入 rules', '输出规范', NULL, 1744698576537, 1744698576537);
INSERT INTO `prom_template_parameter` VALUES (1109565858931412992, 1109565858495205376, 'user_input', 1, '', '如：深基坑支护优化', '用户输入的技术主题', NULL, 1744707581119, 1744707581119);
INSERT INTO `prom_template_parameter` VALUES (1109565858939801600, 1109565858495205376, 'technical_field', 0, '高层建筑/桥梁工程/地下空间开发', '选择所属技术领域', '技术领域分类', NULL, 1744707581119, 1744707581119);
INSERT INTO `prom_template_parameter` VALUES (1109565858943995904, 1109565858495205376, 'key_technology', 0, '预应力张拉技术/BIM协同设计/土体改良', '输入关键技术名称', '关键技术点', NULL, 1744707581119, 1744707581119);
INSERT INTO `prom_template_parameter` VALUES (1109565858952384512, 1109565858495205376, 'application_scenario', 0, '软土地基施工/大跨度钢结构安装/地铁隧道掘进', '描述具体应用场景', '应用场景', NULL, 1744707581119, 1744707581119);
INSERT INTO `prom_template_parameter` VALUES (1109565858952384513, 1109565858495205376, 'example', 0, '现有基于{传统工艺}的{工程结构}在{应用场景}下存在{技术缺陷}，导致{具体问题}', '可替换占位符内容', '示例模板', NULL, 1744707581119, 1744707581119);
INSERT INTO `prom_template_parameter` VALUES (1109565858969161728, 1109565858495205376, 'rules', 0, '1.限定200字内；2.需引出后续技术问题；3.禁用行业缩略语', '请输入 rules', '输出规范', NULL, 1744707581119, 1744707581119);
INSERT INTO `prom_template_parameter` VALUES (1109566612933054464, 1109566612886917120, 'user_input', 1, '', '如：混凝土裂缝控制不足', '用户输入的技术痛点', NULL, 1744707760904, 1744707760904);
INSERT INTO `prom_template_parameter` VALUES (1109566612937248768, 1109566612886917120, 'implementation_level', 0, '构件级/单体级/系统工程级', '选择实施层级', '实施层级', NULL, 1744707760904, 1744707760904);
INSERT INTO `prom_template_parameter` VALUES (1109566612941443072, 1109566612886917120, 'core_technology', 0, '智能监测系统/模架一体化技术/装配式施工', '输入核心技术名称', '核心技术', NULL, 1744707760904, 1744707760904);
INSERT INTO `prom_template_parameter` VALUES (1109566612941443073, 1109566612886917120, 'system_module', 0, '结构健康监测系统/模板支撑体系/防水隔震装置', '输入模块名称', '系统模块', NULL, 1744707760904, 1744707760904);
INSERT INTO `prom_template_parameter` VALUES (1109566612945637376, 1109566612886917120, 'example', 0, '通过{创新技术}在{目标结构}实现{关键指标}优化，具体包括：{技术特征1}、{技术特征2}', '可替换占位符内容', '示例模板', NULL, 1744707760904, 1744707760904);
INSERT INTO `prom_template_parameter` VALUES (1109566612945637377, 1109566612886917120, 'rules', 0, '1.分结构设计/施工工艺描述；2.包含3个以上技术特征；3.禁用公式代码', '请输入 rules', '输出规范', NULL, 1744707760904, 1744707760904);
INSERT INTO `prom_template_parameter` VALUES (1109567284051054592, 1109567284009111552, 'user_input', 0, '', '如：支护结构水平位移超标', '用户输入的问题现象', NULL, 1744707920909, 1744707920909);
INSERT INTO `prom_template_parameter` VALUES (1109567284055248896, 1109567284009111552, 'existing_technology', 0, '人工监测/传统满堂支架/普通振捣工艺', '输入被替代的技术名称', '现有技术方法', NULL, 1744707920909, 1744707920909);
INSERT INTO `prom_template_parameter` VALUES (1109567284059443200, 1109567284009111552, 'problem_scenario', 0, '高水位地层开挖/超高层泵送混凝土/地震带施工', '描述具体问题场景', '问题发生场景', NULL, 1744707920909, 1744707920909);
INSERT INTO `prom_template_parameter` VALUES (1109567284059443201, 1109567284009111552, 'negative_impact', 0, '结构安全性降低/施工进度延误/维修成本增加', '输入具体影响指标', '负面影响', NULL, 1744707920909, 1744707920909);
INSERT INTO `prom_template_parameter` VALUES (1109567284122357760, 1109567284009111552, 'example', 0, '{现有技术}在{问题场景}下因{缺陷原因}导致{检测指标}超出规范限值{阈值}', '可替换占位符内容', '示例模板', NULL, 1744707920909, 1744707920909);
INSERT INTO `prom_template_parameter` VALUES (1109567284130746368, 1109567284009111552, 'rules', 0, '1.需引用GB/T标准数据；2.与技术效果章节指标对应；3.禁用主观描述', '请输入 rules', '输出规范', NULL, 1744707920909, 1744707920909);
INSERT INTO `prom_template_parameter` VALUES (1109567809920307200, 1109567809890947072, 'user_input', 1, '', '如：新型模板支撑体系', '用户输入的技术改进点', NULL, 1744708046289, 1744708046289);
INSERT INTO `prom_template_parameter` VALUES (1109567809924501504, 1109567809890947072, 'technical_improvement', 0, '跳仓法施工/预应力补偿技术/智能养护系统', '输入改进的技术点', '技术改进点', NULL, 1744708046289, 1744708046289);
INSERT INTO `prom_template_parameter` VALUES (1109567809928695808, 1109567809890947072, 'test_condition', 0, '50年一遇风荷载测试/连续浇筑1000m³混凝土/3级地震模拟', '描述测试环境', '测试条件', NULL, 1744708046289, 1744708046289);
INSERT INTO `prom_template_parameter` VALUES (1109567809928695809, 1109567809890947072, 'evaluation_indicator', 0, '结构沉降量/裂缝宽度/施工周期', '输入检测指标', '评估指标', NULL, 1744708046289, 1744708046289);
INSERT INTO `prom_template_parameter` VALUES (1109567809928695810, 1109567809890947072, 'example', 0, '', '请输入 example', 'example 参数', NULL, 1744708046289, 1744708046289);
INSERT INTO `prom_template_parameter` VALUES (1109567809932890112, 1109567809890947072, 'rules', 0, '在{测试场景}中，{改进技术}使{评估指标}从{原始值}优化至{目标值}，符合{GB 50010-2010}要求', '可替换占位符内容', '示例模板', NULL, 1744708046289, 1744708046289);
INSERT INTO `prom_template_parameter` VALUES (1109568529474129920, 1109568529256026112, 'user_input', 1, '', '如：大体积混凝土浇筑', '用户输入的工艺需求', NULL, 1744708217844, 1744708217844);
INSERT INTO `prom_template_parameter` VALUES (1109568529478324224, 1109568529256026112, 'equipment_model', 0, '三一重工SY5390THB 62C-8泵车/中联重科ZCC9800塔吊', '输入设备型号', '设备型号', NULL, 1744708217844, 1744708217844);
INSERT INTO `prom_template_parameter` VALUES (1109568529482518528, 1109568529256026112, 'process_parameter', 0, '浇筑温度控制/分层厚度/振捣频率', '描述关键参数', '工艺参数', NULL, 1744708217844, 1744708217844);
INSERT INTO `prom_template_parameter` VALUES (1109568529482518529, 1109568529256026112, 'quality_control', 0, '温度应力监测/坍落度检测/养护周期控制', '输入控制方法', '质量控制', NULL, 1744708217844, 1744708217844);
INSERT INTO `prom_template_parameter` VALUES (1109568529482518530, 1109568529256026112, 'example', 0, '', '请输入 example', 'example 参数', NULL, 1744708217844, 1744708217844);
INSERT INTO `prom_template_parameter` VALUES (1109568529486712832, 1109568529256026112, 'rules', 0, '采用{设备型号}通过{工艺参数}实现{目标指标}，{质量指标}控制在{允许偏差}', '可替换占位符内容', '示例模板', NULL, 1744708217844, 1744708217844);
INSERT INTO `prom_template_parameter` VALUES (1109569153750142976, 1109569153733365760, 'user_input', 0, '', '如：抗震性能优化设计', '用户输入的设计需求', NULL, 1744708366682, 1744708366682);
INSERT INTO `prom_template_parameter` VALUES (1109569153750142977, 1109569153733365760, 'design_method', 0, '性能化抗震设计/拓扑优化/隔震支座布置', '输入设计方法名称', '设计方法', NULL, 1744708366682, 1744708366682);
INSERT INTO `prom_template_parameter` VALUES (1109569153758531584, 1109569153733365760, 'load_condition', 0, '风振效应组合/罕遇地震作用/人致振动', '描述荷载类型', '荷载工况', NULL, 1744708366682, 1744708366682);
INSERT INTO `prom_template_parameter` VALUES (1109569153758531585, 1109569153733365760, 'safety_factor', 0, '抗倾覆系数/承载力富余度/疲劳寿命', '输入安全指标', '安全系数', NULL, 1744708366682, 1744708366682);
INSERT INTO `prom_template_parameter` VALUES (1109569153762725888, 1109569153733365760, 'example', 0, '基于{设计方法}在{荷载工况}下，{安全指标}达到{目标值}，较传统设计提升{改进幅度}', '可替换占位符内容', '示例模板', NULL, 1744708366682, 1744708366682);
INSERT INTO `prom_template_parameter` VALUES (1109569153762725889, 1109569153733365760, 'rules', 0, '1.需注明计算软件（如MIDAS Gen）；2.引用JGJ规范条款', '请输入 rules', '输出规范', NULL, 1744708366682, 1744708366682);
INSERT INTO `prom_template_parameter` VALUES (1109572936207044608, 1109569722938167296, 'user_input', 1, '', '如：肿瘤靶向药物递送系统', '用户输入的技术主题', NULL, 1744709268491, 1744709268491);
INSERT INTO `prom_template_parameter` VALUES (1109572936211238912, 1109569722938167296, 'technical_field', 0, '基因治疗/免疫疗法/纳米药物', '选择所属技术领域', '技术领域分类', NULL, 1744709268491, 1744709268491);
INSERT INTO `prom_template_parameter` VALUES (1109572936211238913, 1109569722938167296, 'key_technology', 0, 'CRISPR-Cas9编辑/单克隆抗体制备/脂质体包裹技术', '输入关键技术名称', '关键技术点', NULL, 1744709268491, 1744709268491);
INSERT INTO `prom_template_parameter` VALUES (1109572936211238914, 1109569722938167296, 'application_scenario', 0, '实体瘤治疗/慢性炎症管理/神经退行性疾病干预', '描述具体应用场景', '应用场景', NULL, 1744709268491, 1744709268491);
INSERT INTO `prom_template_parameter` VALUES (1109572936211238915, 1109569722938167296, 'example', 0, '现有基于{现有技术方法}的{目标药物}在{应用场景}下存在{技术缺陷}，导致{具体临床问题}', '可替换占位符内容', '示例模板', NULL, 1744709268491, 1744709268491);
INSERT INTO `prom_template_parameter` VALUES (1109572936211238916, 1109569722938167296, 'rules', 0, '1.限定200字内；2.需引用至少2篇《Nature》子刊文献；3.禁用未经验证的假设', '请输入 rules', '输出规范', NULL, 1744709268491, 1744709268491);
INSERT INTO `prom_template_parameter` VALUES (1109573056675844096, 1109570427124060160, 'user_input', 1, '', '如：药物脱靶效应显著', '用户输入的技术痛点', NULL, 1744709297213, 1744709297213);
INSERT INTO `prom_template_parameter` VALUES (1109573056675844097, 1109570427124060160, 'implementation_level', 0, '分子级/细胞级/活体级', '选择实施层级', '实施层级', NULL, 1744709297213, 1744709297213);
INSERT INTO `prom_template_parameter` VALUES (1109573056680038400, 1109570427124060160, 'core_technology', 0, '抗体-药物偶联(ADC)/基因回路设计/器官芯片技术', '输入核心技术名称', '核心技术', NULL, 1744709297213, 1744709297213);
INSERT INTO `prom_template_parameter` VALUES (1109573056680038401, 1109570427124060160, 'system_module', 0, '靶向配体修饰系统/可控释放机制/生物分布调控', '输入模块名称', '系统模块', NULL, 1744709297213, 1744709297213);
INSERT INTO `prom_template_parameter` VALUES (1109573056680038402, 1109570427124060160, 'example', 0, '通过{创新技术}在{载体系统}实现{关键参数}优化，具体包括：{技术特征1}、{技术特征2}', '可替换占位符内容', '示例模板', NULL, 1744709297213, 1744709297213);
INSERT INTO `prom_template_parameter` VALUES (1109573056680038403, 1109570427124060160, 'rules', 0, '1.分体外/体内实验描述；2.需注明IC50值；3.禁用未经验证的临床数据', '请输入 rules', '输出规范', NULL, 1744709297213, 1744709297213);
INSERT INTO `prom_template_parameter` VALUES (1109573181804515328, 1109570968373825536, 'user_input', 1, '', '如：CAR-T细胞因子风暴', '用户输入的问题现象', NULL, 1744709327046, 1744709327046);
INSERT INTO `prom_template_parameter` VALUES (1109573181808709632, 1109570968373825536, 'existing_technology', 0, '传统化疗/第一代基因编辑/普通缓释制剂', '输入被替代的技术名称', '现有技术方法', NULL, 1744709327046, 1744709327046);
INSERT INTO `prom_template_parameter` VALUES (1109573181808709633, 1109570968373825536, 'problem_scenario', 0, '免疫抑制微环境/多药耐药性/血脑屏障穿透', '描述具体问题场景', '问题发生场景', NULL, 1744709327046, 1744709327046);
INSERT INTO `prom_template_parameter` VALUES (1109573181808709634, 1109570968373825536, 'negative_impact', 0, '治疗有效率<30%/严重副作用发生率>50%/患者依从性差', '输入具体影响指标', '负面影响', NULL, 1744709327046, 1744709327046);
INSERT INTO `prom_template_parameter` VALUES (1109573181808709635, 1109570968373825536, 'example', 0, '{现有技术}在{问题场景}下因{缺陷原因}导致{评估指标}超出{安全阈值}', '可替换占位符内容', '示例模板', NULL, 1744709327046, 1744709327046);
INSERT INTO `prom_template_parameter` VALUES (1109573181812903936, 1109570968373825536, 'rules', 0, '1.需引用FDA不良事件报告；2.数据精确到小数点后一位；3.禁用模糊表述', '请输入 rules', '输出规范', NULL, 1744709327046, 1744709327046);
INSERT INTO `prom_template_parameter` VALUES (1109573266336518144, 1109571489914556416, 'user_input', 1, '', '如：新型PD-1抑制剂', '用户输入的技术改进点', NULL, 1744709347200, 1744709347200);
INSERT INTO `prom_template_parameter` VALUES (1109573266340712448, 1109571489914556416, 'technical_improvement', 0, '表观遗传调控/双特异性抗体/微环境响应型递送', '输入改进的技术点', '技术改进点', NULL, 1744709347200, 1744709347200);
INSERT INTO `prom_template_parameter` VALUES (1109573266340712449, 1109571489914556416, 'test_condition', 0, '荷瘤小鼠模型/类器官培养系统/三期临床试验', '描述测试环境', '测试条件', NULL, 1744709347200, 1744709347200);
INSERT INTO `prom_template_parameter` VALUES (1109573266340712450, 1109571489914556416, 'evaluation_indicator', 0, '客观缓解率(ORR)/无进展生存期(PFS)/最大耐受剂量(MTD)', '输入检测指标', '评估指标', NULL, 1744709347200, 1744709347200);
INSERT INTO `prom_template_parameter` VALUES (1109573266340712451, 1109571489914556416, 'example', 0, '在{实验模型}中，{改进技术}使{评估指标}提升{改进幅度}，具有{统计学意义}', '可替换占位符内容', '示例模板', NULL, 1744709347200, 1744709347200);
INSERT INTO `prom_template_parameter` VALUES (1109573266344906752, 1109571489914556416, 'rules', 0, '1.必须包含统计学分析方法；2.遵循CONSORT标准；3.数据保留三位有效数字', '请输入 rules', '输出规范', NULL, 1744709347200, 1744709347200);
INSERT INTO `prom_template_parameter` VALUES (1109573324234690560, 1109572244390154240, 'user_input', 1, '', ' 如：癌细胞系药物敏感性测试', '用户输入的实验需求', NULL, 1744709361003, 1744709361003);
INSERT INTO `prom_template_parameter` VALUES (1109573324234690561, 1109572244390154240, 'experimental_model', 0, '3D肿瘤球模型/原代T细胞培养/肠道菌群共培养系统', '输入模型名称', '实验模型', NULL, 1744709361003, 1744709361003);
INSERT INTO `prom_template_parameter` VALUES (1109573324238884864, 1109572244390154240, 'protocol_design', 0, 'CCK-8法细胞毒性检测/Transwell迁移实验/流式细胞术分析', '描述实验方法', '实验方案设计', NULL, 1744709361003, 1744709361003);
INSERT INTO `prom_template_parameter` VALUES (1109573324238884865, 1109572244390154240, 'parameter_control', 0, '细胞接种密度控制/血清浓度梯度设置/培养时间节点', '输入控制参数', '参数控制', NULL, 1744709361003, 1744709361003);
INSERT INTO `prom_template_parameter` VALUES (1109573324238884866, 1109572244390154240, 'example', 0, '采用{实验模型}通过{检测方法}验证{目标参数}，设置{控制条件}保证实验有效性', '可替换占位符内容', '示例模板', NULL, 1744709361003, 1744709361003);
INSERT INTO `prom_template_parameter` VALUES (1109573324238884867, 1109572244390154240, 'rules', 0, '1.注明细胞系来源（如ATCC编号）；2.温度湿度精确控制±1%；3.禁用模糊描述', '请输入 rules', '输出规范', NULL, 1744709361003, 1744709361003);
INSERT INTO `prom_template_parameter` VALUES (1109885521817112576, 1109572734666543104, 'user_input', 1, '', '如：药物代谢动力学研究', '用户输入的评估需求', NULL, 1744783794691, 1744783794691);
INSERT INTO `prom_template_parameter` VALUES (1109885521825501184, 1109572734666543104, 'animal_model', 0, 'NOD/SCID小鼠/食蟹猴/人源化小鼠', '输入模型类型', '动物模型', NULL, 1744783794691, 1744783794691);
INSERT INTO `prom_template_parameter` VALUES (1109885521825501185, 1109572734666543104, 'dosing_regimen', 0, '单次静脉注射5mg/kg q3d/口服给药100mg bid/局部缓释植入', '描述给药方式', '给药方案', NULL, 1744783794691, 1744783794691);
INSERT INTO `prom_template_parameter` VALUES (1109885521825501186, 1109572734666543104, 'safety_assessment', 0, '血液生化指标分析/组织病理学检查/神经行为学评分', '输入评估方法', '安全性评估', NULL, 1744783794691, 1744783794691);
INSERT INTO `prom_template_parameter` VALUES (1109885521829695488, 1109572734666543104, 'example', 0, '在{动物模型}中，{给药方案}显示{安全性指标}维持在{安全范围}', '可替换占位符内容', '示例模板', NULL, 1744783794691, 1744783794691);
INSERT INTO `prom_template_parameter` VALUES (1109885521833889792, 1109572734666543104, 'rules', 0, '1.符合AAALAC认证标准；2.注明伦理审批编号；3.体重波动记录±5g', '请输入 rules', '输出规范', NULL, 1744783794691, 1744783794691);
INSERT INTO `prom_template_parameter` VALUES (1112177990185390080, 1112177817069686784, 'user_input', 1, '', '请输入核心技术点', '用户输入的技术主题', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990189584384, 1112177817069686784, 'existing_technology', 0, '基于光学成像的检测技术/基于激光扫描的测量技术/基于超声波的测距技术', '请提供现有的技术方法或方案', '当前的技术方法或解决方案', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990189584385, 1112177817069686784, 'target_system', 0, '光学成像系统/激光扫描系统/超声波测距仪', '请描述目标系统或设备', '目标系统或设备', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990189584386, 1112177817069686784, 'specific_scenario', 0, '低光照条件下的目标检测/复杂环境下的物体识别/动态场景下的运动检测', '请描述特定应用场景', '特定应用场景', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990189584387, 1112177817069686784, 'technical_deficiency', 0, '低光照条件下成像模糊/复杂场景下目标无法清晰识别/动态环境中目标追踪不稳定', '请提供现有技术存在的技术缺陷', '现有技术中的技术缺陷或不足', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990193778688, 1112177817069686784, 'negative_impact', 0, '导致目标检测精度低，影响系统性能/影响系统响应速度，降低稳定性/使目标追踪失败，影响应用效果', '请描述技术缺陷带来的负面影响', '技术缺陷或不足造成的负面影响', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990193778689, 1112177817069686784, 'example', 0, '现有基于{现有技术方法}的{目标系统}在{特定场景}下存在{技术缺陷}，导致{具体负面影响}', '请输入 example', '示例引导模板', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112177990193778690, 1112177817069686784, 'rules', 0, '', '1. 语言简洁，不超过200字；2. 必须包含技术缺陷和影响数据；3. 不应包含任何格式化内容（如加粗、斜体等），纯文本输出', '输出规范', NULL, 1745330361759, 1745330361759);
INSERT INTO `prom_template_parameter` VALUES (1112183515618873344, 1112180267545661440, 'specific_scenario', 0, '低光照条件/动态环境下的目标追踪/高动态场景', '请提供问题发生的场景', '特定的应用场景', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183515623067648, 1112180267545661440, 'problem_phenomenon', 0, '图像模糊/信号干扰/目标遮挡', '请描述问题表现', '现象或问题表现', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183515623067649, 1112180267545661440, 'negative_consequence', 0, '导致目标检测精度低/影响图像质量/影响实时性和稳定性', '请描述该问题带来的后果', '问题带来的负面后果', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183515623067650, 1112180267545661440, 'system_name', 0, '光学成像系统', '请描述系统名称', '系统或设备名称', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183515623067651, 1112180267545661440, 'system_function', 0, '目标识别功能', '请提供系统功能', '系统或设备的功能', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183515623067652, 1112180267545661440, 'example', 0, '本发明的技术问题是如何在{特定场景}下{问题表现}，从而{负面后果}，该问题直接影响了{系统名称}的{系统功能}。', '', '示例参考', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183515623067653, 1112180267545661440, 'rules', 0, '', '1. 分点列出；2. 每个问题包含现象+量化指标；3. 禁用解决方案描述；4. 不应包含任何格式化内容（如加粗、斜体等），纯文本输出', '输出规范', NULL, 1745331679125, 1745331679125);
INSERT INTO `prom_template_parameter` VALUES (1112183598875807744, 1112183337918795776, 'solution_method', 0, '高动态范围成像技术/图像去噪技术/传感器融合技术', '请描述采用的技术方案', '采用的技术方案', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002048, 1112183337918795776, 'technical_deficiency', 0, '图像模糊/低光照成像问题/成像延迟', '请提供现有技术中的技术缺陷', '现有技术中的技术缺陷', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002049, 1112183337918795776, 'performance_indicators', 0, '图像清晰度/目标识别精度/处理速度', '请描述提升的性能指标', '提升的性能指标', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002050, 1112183337918795776, 'additional_benefits', 0, '提高低光照条件下的目标识别能力/增强系统稳定性/提高图像实时处理能力', '请描述额外的益处', '其他额外的益处', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002051, 1112183337918795776, 'system_name', 0, '光学成像系统', '请描述系统名称', '系统名称', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002052, 1112183337918795776, 'system_function', 0, '目标检测精度', '请描述系统功能', '系统功能', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002053, 1112183337918795776, 'example', 0, '通过采用{技术方案}，本发明能够有效解决{技术缺陷}问题，提升{性能指标}，并且具有{其他益处}，从而提高{系统名称}的{系统功能}', '请输入 example', '示例参考', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112183598880002054, 1112183337918795776, 'rules', 0, '', '1. 数据单位使用国际标准符号；2. 不应包含任何格式化内容（如加粗、斜体等），纯文本输出', '输出规范', NULL, 1745331698975, 1745331698975);
INSERT INTO `prom_template_parameter` VALUES (1112185408218861568, 1112184885436616704, 'technical_problem', 0, '光照条件下目标模糊/图像处理速度慢/高动态范围图像处理不稳定', '请提供现有技术中的主要问题', '当前技术中的主要问题', NULL, 1745332130356, 1745332130356);
INSERT INTO `prom_template_parameter` VALUES (1112185408218861569, 1112184885436616704, 'solution_method', 0, '高动态范围成像技术/增强型图像去噪算法/多传感器融合技术', '请描述提出的技术解决方案', '提出的技术解决方案', NULL, 1745332130356, 1745332130356);
INSERT INTO `prom_template_parameter` VALUES (1112185408218861570, 1112184885436616704, 'solution_details', 0, '通过增加传感器灵敏度，提升图像处理算法/结合多光谱图像进行处理/通过融合多传感器数据提升成像清晰度', '请详细说明技术方案的具体实现', '方案的具体实现细节', NULL, 1745332130356, 1745332130356);
INSERT INTO `prom_template_parameter` VALUES (1112185408223055872, 1112184885436616704, 'technical_deficiency', 0, '低光照条件下成像模糊/图像细节丢失/高对比度环境下失真', '请提供现有技术中的技术缺陷', '现有技术中的技术缺陷', NULL, 1745332130356, 1745332130356);
INSERT INTO `prom_template_parameter` VALUES (1112185408223055873, 1112184885436616704, 'example', 0, '本发明针对{技术问题}，提出了{技术方案}，该方案通过{方案实现细节}，有效解决了现有技术中存在的{技术缺陷}问题。', '请输入 example', '示例参考', NULL, 1745332130356, 1745332130356);
INSERT INTO `prom_template_parameter` VALUES (1112185408223055874, 1112184885436616704, 'rules', 0, '', '1. 分步骤说明；2. 包含关键参数范围；3. 不应包含任何格式化内容（如加粗、斜体等），纯文本输出', '输出规范', NULL, 1745332130356, 1745332130356);
INSERT INTO `prom_template_parameter` VALUES (1112186449870065664, 1112186449853288448, 'solution_method', 0, '高动态范围成像', '请描述实施方案', '采用的技术方案', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449870065665, 1112186449853288448, 'operation_details', 0, '增加传感器灵敏度', '请详细说明操作步骤', '操作过程或步骤', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449870065666, 1112186449853288448, 'technical_effect', 0, '提高图像清晰度', '请描述实现的技术效果', '实现的技术效果', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449870065667, 1112186449853288448, 'system_name', 0, '光学成像系统', '请描述系统名称', '系统名称', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449870065668, 1112186449853288448, 'specific_scenario', 0, '低光照条件', '请描述应用场景', '特定应用场景', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449874259968, 1112186449853288448, 'desired_performance', 0, '提高目标识别精度', '请描述期望性能', '期望的性能或效果', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449874259969, 1112186449853288448, 'example', 0, '在实施例1中，{技术方案}，通过{操作步骤}，实现了{技术效果}，该实施例表明{系统名称}在{特定场景}下具备了{期望性能}。', '', '示例参考', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112186449874259970, 1112186449853288448, 'rules', 0, '', '1. 包含实施步骤+测试数据；2. 突出解决特定问题的创新方法；3. 不应包含任何格式化内容（如加粗、斜体等），纯文本输出', '输出规范', NULL, 1745332378704, 1745332378704);
INSERT INTO `prom_template_parameter` VALUES (1112187280753299456, 1112187280740716544, 'system_name', 0, '光学成像系统', '请描述系统名称', '系统名称', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280753299457, 1112187280740716544, 'advanced_technology', 0, '深度学习图像增强', '请提供使用的先进技术', '使用的先进技术', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280753299458, 1112187280740716544, 'methodology', 0, '基于卷积神经网络的优化方法', '请描述使用的方法或策略', '方法或策略', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280753299459, 1112187280740716544, 'problem_scenario', 0, '复杂环境下的目标检测', '请描述发生问题的场景', '问题发生的场景', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280753299460, 1112187280740716544, 'specific_issue', 0, '背景噪声干扰', '请描述需要解决的具体问题', '需要解决的具体问题', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280753299461, 1112187280740716544, 'result', 0, '显著提高检测精度', '请描述达到的效果', '达到的结果或效果', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280757493760, 1112187280740716544, 'example', 0, '在实施例2中，{系统名称}通过{先进技术}和{方法论}，解决了{问题场景}中的{具体问题}，达到了{结果}，验证了该技术方案的可行性和优势。', '请输入 example', '示例参考', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1112187280757493761, 1112187280740716544, 'rules', 0, '', '1. 与实施例1形成对比；2. 突出解决特定问题的创新方法；3. 不应包含任何格式化内容（如加粗、斜体等），纯文本输出', '输出规范', NULL, 1745332576802, 1745332576802);
INSERT INTO `prom_template_parameter` VALUES (1113174551749070848, 1113173829842243584, 'user_input', 1, '', '请输入技术背景关键词 ', '用户输入的技术主题', NULL, 1745567960551, 1745567960551);
INSERT INTO `prom_template_parameter` VALUES (1113174551753265152, 1113173829842243584, 'technical_field', 0, '光学仪器/光学设计/光学材料', '选择所属技术领域 ', '技术领域分类 ', NULL, 1745567960551, 1745567960551);
INSERT INTO `prom_template_parameter` VALUES (1113174551753265153, 1113173829842243584, 'current_technology', 0, '光学成像系统/光谱分析技术/激光干涉测量', '输入现有技术名称', '现有技术方法 ', NULL, 1745567960551, 1745567960551);
INSERT INTO `prom_template_parameter` VALUES (1113174551753265154, 1113173829842243584, 'application_scenario', 0, '高精度光学检测/复杂环境成像/微型光学器件', '描述具体应用场景', '应用场景 ', NULL, 1745567960551, 1745567960551);
INSERT INTO `prom_template_parameter` VALUES (1113174551753265155, 1113173829842243584, 'example', 0, ' 现有基于{传统光学设计}的{成像系统}在{低光照环境}下存在{分辨率不足}，导致{检测误差率增加30%} ', '自动填充', ' 示例模板 ', NULL, 1745567960551, 1745567960551);
INSERT INTO `prom_template_parameter` VALUES (1113174551753265156, 1113173829842243584, 'rules', 0, ' 1.限定200字内；2.需引出后续技术问题；3.纯文本输出，禁用术语缩写', '自动填充', '输出规范 ', NULL, 1745567960551, 1745567960551);
INSERT INTO `prom_template_parameter` VALUES (1113180508264730624, 1113180508256342016, 'user_input', 1, '', '请输入技术主题', '用户输入的技术主题', NULL, 1745569380695, 1745569380695);
INSERT INTO `prom_template_parameter` VALUES (1113180508264730625, 1113180508256342016, 'technical_field', 0, '光学仪器/光学设计/光学检测', '选择所属技术领域	', '技术领域分类', NULL, 1745569380695, 1745569380695);
INSERT INTO `prom_template_parameter` VALUES (1113180508264730626, 1113180508256342016, 'key_technology', 0, '透镜成像技术/光谱分析技术/激光干涉技术', '输入现有技术名称', '现有技术方法', NULL, 1745569380695, 1745569380695);
INSERT INTO `prom_template_parameter` VALUES (1113180508264730627, 1113180508256342016, 'application_scenario', 0, '高精度测量/复杂环境检测/微型化设备', '描述具体应用场景', '具体应用场景', NULL, 1745569380695, 1745569380695);
INSERT INTO `prom_template_parameter` VALUES (1113180508268924928, 1113180508256342016, 'example', 0, '现有基于{透镜成像技术}的{高精度测量系统}在{复杂光照环境}下存在{成像畸变}，导致{测量误差超过5%}', '可替换占位', '	示例模板', NULL, 1745569380695, 1745569380695);
INSERT INTO `prom_template_parameter` VALUES (1113180508268924929, 1113180508256342016, 'rules', 0, '1.限定200字内；2.需引出后续技术问题；3.禁用专业术语缩写；4.降低TOKEN；5.纯文本输出', '自动填充', '输出规范', NULL, 1745569380695, 1745569380695);
INSERT INTO `prom_template_parameter` VALUES (1113182302684450816, 1113182302676062208, 'user_input', 1, '', '请输入技术主题	', '用户输入的技术主题', NULL, 1745569808517, 1745569808517);
INSERT INTO `prom_template_parameter` VALUES (1113182302684450817, 1113182302676062208, 'technical_field', 0, '光学仪器/光学设计/光学检测', '选择所属技术领域', '技术领域分类', NULL, 1745569808517, 1745569808517);
INSERT INTO `prom_template_parameter` VALUES (1113182302684450818, 1113182302676062208, 'technical_problem', 0, '成像畸变/光谱干扰/设备体积过大', '输入具体技术问题', '待解决的技术问题', NULL, 1745569808517, 1745569808517);
INSERT INTO `prom_template_parameter` VALUES (1113182302684450819, 1113182302676062208, 'desired_effect', 0, '新型透镜设计/多光谱融合算法/微型化结构', '描述创新点', '核心技术创新点', NULL, 1745569808517, 1745569808517);
INSERT INTO `prom_template_parameter` VALUES (1113182302684450820, 1113182302676062208, 'example', 0, '通过{新型非球面透镜设计}解决{成像畸变}，结合{多光谱校准算法}提升{复杂环境下的测量精度至1%以内}', '可替换占位', '示例模板', NULL, 1745569808517, 1745569808517);
INSERT INTO `prom_template_parameter` VALUES (1113182302684450821, 1113182302676062208, 'rules', 0, '1.限定200字内；2.聚焦创新点；3.禁用专业术语缩写；4.降低TOKEN；5.纯文本输出', '自动填充', '输出规范', NULL, 1745569808517, 1745569808517);
INSERT INTO `prom_template_parameter` VALUES (1113183415924035584, 1113183415919841280, 'user_input', 1, '', '请输入技术主题', '用户输入的技术主题	', NULL, 1745570073935, 1745570073935);
INSERT INTO `prom_template_parameter` VALUES (1113183415928229888, 1113183415919841280, 'technical_field', 0, '光学仪器/光学设计/光学检测', '选择所属技术领域', '技术领域分类', NULL, 1745570073935, 1745570073935);
INSERT INTO `prom_template_parameter` VALUES (1113183415928229889, 1113183415919841280, 'technical_problem', 0, '传统透镜系统/单光谱检测方法/固定焦距设计', '输入现有解决方案', '现有解决方案', NULL, 1745570073935, 1745570073935);
INSERT INTO `prom_template_parameter` VALUES (1113183415928229890, 1113183415919841280, 'negative_consequence', 0, '测量精度/响应速度/设备稳定性', '输入受影响的性能指标	', '性能指标', NULL, 1745570073935, 1745570073935);
INSERT INTO `prom_template_parameter` VALUES (1113183415928229891, 1113183415919841280, 'example', 0, '{传统透镜系统}因{球面像差}导致{在边缘视场的分辨率下降30%}，且{无法适应动态变焦需求}', '可替换占位', '示例模板', NULL, 1745570073935, 1745570073935);
INSERT INTO `prom_template_parameter` VALUES (1113183415928229892, 1113183415919841280, 'rules', 0, '1.限定150字内；2.明确缺陷与影响；3.禁用专业术语缩写；4.降低TOKEN；5.纯文本输出', '自动填充', '输出规范', NULL, 1745570073935, 1745570073935);
INSERT INTO `prom_template_parameter` VALUES (1114537935081967616, 1114535599953547264, 'key_technology', 0, '光学成像算法/精密传感器设计', '', '关键技术点', NULL, 1745893016467, 1745893016467);
INSERT INTO `prom_template_parameter` VALUES (1114537935086161920, 1114535599953547264, 'application_scenario	', 0, '显微成像/激光加工', '', '应用场景', NULL, 1745893016467, 1745893016467);
INSERT INTO `prom_template_parameter` VALUES (1114537935090356224, 1114535599953547264, 'user_input', 0, '无', '请输入核心技术点', '用户输入的技术主题	', NULL, 1745893016467, 1745893016467);
INSERT INTO `prom_template_parameter` VALUES (1114537935098744832, 1114535599953547264, 'example', 0, '', '', '示例模板', NULL, 1745893016467, 1745893016467);
INSERT INTO `prom_template_parameter` VALUES (1114537935098744833, 1114535599953547264, 'rules', 0, '', '', '输出规则', NULL, 1745893016467, 1745893016467);
INSERT INTO `prom_template_parameter` VALUES (1114538527095394304, 1114538527070228480, 'existing_limitation', 0, '功耗过高/环境适应性差', '', '现有技术局限性', NULL, 1745893157615, 1745893157615);
INSERT INTO `prom_template_parameter` VALUES (1114538988166844416, 1114538988150067200, 'core_issue', 0, '信噪比不足/响应延迟', '', '核心技术问题', NULL, 1745893267542, 1745893267542);
INSERT INTO `prom_template_parameter` VALUES (1114538988166844417, 1114538988150067200, 'technical_scheme', 0, '自适应光学校正/边缘计算架构', '', '技术解决方案', NULL, 1745893267542, 1745893267542);

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
-- Records of rel_roles_perm
-- ----------------------------

-- ----------------------------
-- Table structure for rel_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_roles`;
CREATE TABLE `rel_user_roles`  (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
                                   `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
                                   `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rel_user_roles
-- ----------------------------
INSERT INTO `rel_user_roles` VALUES (1125863427743551488, 1107748679651037184, 1120042788315074560, 1748593224333);
INSERT INTO `rel_user_roles` VALUES (1127281449188331520, 1107748349882273792, 1124437048221110272, 1748931306992);
INSERT INTO `rel_user_roles` VALUES (1127281611646308352, 1107748379565363200, 1124437048221110272, 1748931345726);
INSERT INTO `rel_user_roles` VALUES (1127281947991740416, 1107748418060685312, 1124437048221110272, 1748931425916);
INSERT INTO `rel_user_roles` VALUES (1127282076387774464, 1107748596108890112, 1124437048221110272, 1748931456528);
INSERT INTO `rel_user_roles` VALUES (1127282305933643776, 1107747939570290688, 1124437048221110272, 1748931511257);

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
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

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
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

-- ----------------------------
-- Records of sys_roles
-- ----------------------------
INSERT INTO `sys_roles` VALUES (1120042788315074560, 'ROLE_SUPER_ADMIN', '系统管理员', 1, '系统最高权限角色', 1747205475753, 1747205475753);
INSERT INTO `sys_roles` VALUES (1124437048221110272, 'PROMPT_OPERATOR', '提示词工程操作员', 1, '编辑提示词工程人员', 1748253148956, 1748253148956);

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
-- Records of sys_users
-- ----------------------------
INSERT INTO `sys_users` VALUES (1107747939570290688, '软糖001', '{bcrypt}$2a$10$SZgOEV4jmJt7TD91AIctbOz1QqjLdlNfUloABlp3lKQ96iRzszLY2', 1121780694490681344, 1125863897291689984, NULL, '软糖撰写一组', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1744274155294, NULL);
INSERT INTO `sys_users` VALUES (1107748349882273792, '软糖002', '{bcrypt}$2a$10$kdlf1Gt2XXgHzIqTT2kNUOAXWc66oel28vqBDkVtb0CFTGfG78IsW', 1121780694490681344, 1125863897291689984, NULL, '软糖撰写二组', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1744274253121, NULL);
INSERT INTO `sys_users` VALUES (1107748379565363200, '软糖003', '{bcrypt}$2a$10$FdoERKuC8nPTMzjOND94MebPiscCox4sa.r3CRCfMVqyTJtHDtDvm', 1121780694490681344, 1125863897291689984, NULL, '软糖撰写三组', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1744274260206, NULL);
INSERT INTO `sys_users` VALUES (1107748418060685312, '软糖004', '{bcrypt}$2a$10$FCH6qixDpHWGg/hnTUF7Ou9tBECP6kqw4WdFkvTpzAEamRMRsgLCK', 1121780694490681344, 1125863897291689984, NULL, '软糖撰写四组', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1744274269386, NULL);
INSERT INTO `sys_users` VALUES (1107748596108890112, '软糖答审组', '{bcrypt}$2a$10$eQ9Njld4HihtwoCKT6lfWeLjc4YCVakq1cJzFzAUikO1iFhum0XC.', 1121780694490681344, 1125863897291689984, NULL, '软糖答审组', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1744274311836, NULL);
INSERT INTO `sys_users` VALUES (1107748679651037184, 'admin', '{bcrypt}$2a$10$LcRTd/GQIX5C2f2Psf9w6OFYg4QFohIwSLbhtYTLwkmzBD8GY2n/2', 1121780694490681344, 1125863313075474432, NULL, '软糖管理员', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1744274331754, NULL);
INSERT INTO `sys_users` VALUES (1122218126545653760, '测试平台用户001', '{bcrypt}$2a$10$H2zhd.gPuRItt0eFUe1/su9avlfooaIN44lIJ4PC7M/ux29oRwsNS', NULL, NULL, NULL, '用户RJ72G98I', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724116751, NULL);
INSERT INTO `sys_users` VALUES (1122218160196554752, '测试平台用户002', '{bcrypt}$2a$10$obNeiLnijZMJQrCOvWm7tOjGpmeF5OB8H92GLVbzDis1dhqK1kGI6', NULL, NULL, NULL, '用户1179TSZ6', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724124775, NULL);
INSERT INTO `sys_users` VALUES (1122218186192850944, '测试平台用户003', '{bcrypt}$2a$10$/vdUyVM.hLUn6IYv0mSjz.QTgzxPoVaoTrJuR4OXPHk8nzkYdCTL.', NULL, NULL, NULL, '用户E3QZQBBQ', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724130974, NULL);
INSERT INTO `sys_users` VALUES (1122218206858186752, '测试平台用户004', '{bcrypt}$2a$10$f0gAKWBBb/YNSlt.NREUi.KoK3jZ5X6EFsLCtyqpsBxFgu7iSTnWK', NULL, NULL, NULL, '用户Y5N8COKR', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724135900, NULL);
INSERT INTO `sys_users` VALUES (1122218226139402240, '测试平台用户005', '{bcrypt}$2a$10$dz8IJyZ3nxOQgfD.ommjKu1iVbvNYWCBq/qtzDUK7Js1FhQTeW/gS', NULL, NULL, NULL, '用户Z7FWKKDH', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724140499, NULL);
INSERT INTO `sys_users` VALUES (1122218319374585856, '测试企业用户001', '{bcrypt}$2a$10$pB14o7JRrWLI/C.bYWgxTuLqyP18U5CCZIksJPuQhdrXxm8IgfIXq', NULL, NULL, NULL, '用户76CDNZ2C', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724162727, NULL);
INSERT INTO `sys_users` VALUES (1122218337007439872, '测试企业用户002', '{bcrypt}$2a$10$/hyP7u75DftDZezaERt1Wu80SUaMPkAjIH7.PXQPGDc/k61F7mVn2', NULL, NULL, NULL, '用户BLE4J2QF', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724166931, NULL);
INSERT INTO `sys_users` VALUES (1122218356108300288, '测试企业用户003', '{bcrypt}$2a$10$2iENW8/GjyLgqyplltv3W../bLgFONyVeQLOVN42UInXt9LUCXUjq', NULL, NULL, NULL, '用户GLEDSARS', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724171484, NULL);
INSERT INTO `sys_users` VALUES (1122218392854597632, '测试企业用户004', '{bcrypt}$2a$10$W0Ad0GEWe814AroZNV5YIuvHBVFGdXgRpsX1pf9gLfJXfxNUT97li', NULL, NULL, NULL, '用户XTJXQWUN', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724180246, NULL);
INSERT INTO `sys_users` VALUES (1122218416208482304, '测试企业用户005', '{bcrypt}$2a$10$BErJpevLXebMILnsB4cR0.F9aaoaBFLnLKLfHkxOlxvBJhp4CRT82', NULL, NULL, NULL, '用户AZ2WYA1F', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724185814, NULL);
INSERT INTO `sys_users` VALUES (1122218430431367168, '测试企业用户006', '{bcrypt}$2a$10$Ilv/c/d0ocQcX1P.H5GSxezf6h6pSPZ0.qzNRpL4NhW6lcPPxvJU.', NULL, NULL, NULL, '用户2Y52MWL6', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724189203, NULL);
INSERT INTO `sys_users` VALUES (1122218474136014848, '测试代理所用户001', '{bcrypt}$2a$10$bCj3NdBTgaC0R3ANHVuTkep5E6gesUfZUJsuUwsYS2UNM8hbo1mZu', NULL, NULL, NULL, '用户1ZS7MBRV', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724199624, NULL);
INSERT INTO `sys_users` VALUES (1122218491424935936, '测试代理所用户002', '{bcrypt}$2a$10$Jot5jLUMBZMJXVDqDk2VkuyW/qmXznDlk.CS/UbTOG5cSDc/d64Ly', NULL, NULL, NULL, '用户HKO68S3N', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724203747, NULL);
INSERT INTO `sys_users` VALUES (1122218508470587392, '测试代理所用户003', '{bcrypt}$2a$10$7f.Z7RxcnaM954lvdMb1M.1DmxE9t.ysUFrio7ls4gY6bW2166o8S', NULL, NULL, NULL, '用户UHPDUCZN', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724207809, NULL);
INSERT INTO `sys_users` VALUES (1122218524031455232, '测试代理所用户004', '{bcrypt}$2a$10$m7nnrYqPVy2fgewqeN3jm.y3Q0XwBee9hiPUhEjdVcyS88fDTjnZG', NULL, NULL, NULL, '用户0YHGRSL6', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724211520, NULL);
INSERT INTO `sys_users` VALUES (1122218541165187072, '测试代理所用户005', '{bcrypt}$2a$10$adRkl4JjLqdPYjfDHeLy4.ycEhnI/4YusX2Hsqvv.bmDY3E5LpgLa', NULL, NULL, NULL, '用户QZ9W0DS9', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724215606, NULL);
INSERT INTO `sys_users` VALUES (1122218560823889920, '测试代理所用户006', '{bcrypt}$2a$10$mSZD90qrjMaWYVfWA69mKO4yFFRQfjuJ5U6mTgzLWWT2cVO3AA58m', NULL, NULL, NULL, '用户154QLYB0', NULL, '123@163.com', NULL, NULL, NULL, NULL, 1747724220292, NULL);
INSERT INTO `sys_users` VALUES (1127598326632550400, 'test', '{bcrypt}$2a$10$Kwzs2/FdJPvwHwPWn1.SUeofplzd739aGOfhttoqTmQFn1TQjriC2', NULL, NULL, NULL, '用户JR19KPV6', NULL, '1232@163.com', NULL, NULL, NULL, NULL, 1749006856379, NULL);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant
-- ----------------------------
INSERT INTO `tenant` VALUES (1121780694490681344, 'PLATFORM', '超管平台', 1, '13800138001', 'admin@platform', '测试平台租户', '陕西省西安市雁塔区旺座曲江L座31层', 3, 2534947200000, 1107748679651037184, 1747619824873, 1747985933914);
INSERT INTO `tenant` VALUES (1121781398517190656, 'TENANT20230001', '腾云科技有限公司', 2, '13800138002', 'admin@tenant20230001', '测试企业租户', '北京市海淀区中关村南大街10号企业总部', 3, 2536538118000, 1107747939570290688, 1747619992728, 1747619992728);
INSERT INTO `tenant` VALUES (1121781802789376000, 'TENANT20230003', '星辰数字科技集团', 2, '13800138003', 'admin@tenant20230003', '测试企业租户', '深圳市南山区科技园路1001号', 3, 2536538118000, 1107747939570290688, 1747620089114, 1747620089114);
INSERT INTO `tenant` VALUES (1121782253538643968, 'TENANT20230004', '蓝海咨询代理所', 3, '13800138004', 'admin@tenant20230004', '测试代理所租户', '上海市浦东新区世纪大道200号', 3, 2536538118000, 1107747939570290688, 1747620196581, 1747620196581);
INSERT INTO `tenant` VALUES (1121782797577621504, 'TENANT20230005', '绿松财务代理所', 3, '13800138005', 'admin@tenant20230005', '测试代理所租户', '广州市天河区珠江新城101号', 3, 2536538118000, 1107747939570290688, 1747620326290, 1747620326290);
INSERT INTO `tenant` VALUES (1121783083067117568, 'TENANTTEST', '测试更新企业租户页面', 2, '13800138006', 'admin@tenant20230006', '测试更新企业租户', '广州市天河区珠江新城101号', 3, 2536538118000, 1107747939570290688, 1747620394355, 1747897547153);

-- ----------------------------
-- Table structure for tenant_rel_template_role
-- ----------------------------
DROP TABLE IF EXISTS `tenant_rel_template_role`;
CREATE TABLE `tenant_rel_template_role`  (
                                             `id` bigint NOT NULL COMMENT '主键ID',
                                             `template_id` bigint NOT NULL COMMENT '模板ID',
                                             `role_id` bigint NOT NULL COMMENT '角色ID',
                                             `is_inherit` tinyint NOT NULL COMMENT '是否继承权限变更',
                                             `permission_snapshot` json NULL COMMENT '权限快照(创建时JSON结构)，如果继承权限变更，这里为空，如图不继承权限变更，需要存储权限快照',
                                             `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                             `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_rel_template_role
-- ----------------------------
INSERT INTO `tenant_rel_template_role` VALUES (1123323214576095232, 1123255061951156224, 1120042788315074560, 1, NULL, 1747987590321, 1747987590321);
INSERT INTO `tenant_rel_template_role` VALUES (1124437380040888320, 1123255061951156224, 1124437048221110272, 1, NULL, 1748253228068, 1748253228068);

-- ----------------------------
-- Table structure for tenant_rel_tenant_template
-- ----------------------------
DROP TABLE IF EXISTS `tenant_rel_tenant_template`;
CREATE TABLE `tenant_rel_tenant_template`  (
                                               `id` bigint NOT NULL COMMENT '主键ID',
                                               `tenant_id` bigint NOT NULL COMMENT '租户ID',
                                               `template_id` bigint NOT NULL COMMENT '模板ID',
                                               `bind_mode` tinyint NOT NULL COMMENT '绑定模式(1:继承 2:快照)',
                                               `effective_time` bigint NULL DEFAULT NULL COMMENT '生效时间戳',
                                               `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                               `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户模板关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_rel_tenant_template
-- ----------------------------
INSERT INTO `tenant_rel_tenant_template` VALUES (1123302334294790144, 1121780694490681344, 1123255061951156224, 1, 1747982590000, 1747982612075, 1747982612075);
INSERT INTO `tenant_rel_tenant_template` VALUES (1124783923419811840, 1121783083067117568, 1122857831419219968, 1, 1748335847536, 1748335850445, 1748335850445);

-- ----------------------------
-- Table structure for tenant_template
-- ----------------------------
DROP TABLE IF EXISTS `tenant_template`;
CREATE TABLE `tenant_template`  (
                                    `id` bigint NOT NULL COMMENT '主键id',
                                    `template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码(如: EDU_TEMPLATE)',
                                    `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称(如: 教育行业模板)',
                                    `template_type` tinyint NOT NULL COMMENT '模板类型（1：平台租户模板、2：企业租户模板、3：代理所租户模板）',
                                    `industry_type` tinyint NOT NULL COMMENT '行业类型(0:教育 1:医疗 2:金融)',
                                    `data_isolation_mode` tinyint NULL DEFAULT NULL COMMENT '数据隔离模式(继承租户配置)',
                                    `is_system` tinyint NOT NULL COMMENT '是否系统内置模板(0：否、1：是)',
                                    `template_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户模板描述',
                                    `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_template
-- ----------------------------
INSERT INTO `tenant_template` VALUES (1120781604541829120, 'EDU_STD_V1', '标准教育模板', 2, 0, 0, 1, '适用于K-12教育机构的标准配置模板', 1747381623267, 1747381623267);
INSERT INTO `tenant_template` VALUES (1120782032490860544, 'MED_CLINIC_BAS_1', '基础医疗诊所模板', 2, 1, 0, 1, '为小型医疗诊所提供的基础功能和权限', 1747381725300, 1747381725300);
INSERT INTO `tenant_template` VALUES (1120782841727291392, 'FIN_CORP_ADV_2', '高级金融企业模板', 2, 3, 0, 1, '大型金融机构使用的高级功能和权限套件', 1747381918237, 1747898890287);
INSERT INTO `tenant_template` VALUES (1123255061951156224, 'ROOT_TEST', '平台高级租户测试模板', 1, 0, NULL, 1, '', 1747971341469, 1748335918827);

SET FOREIGN_KEY_CHECKS = 1;
