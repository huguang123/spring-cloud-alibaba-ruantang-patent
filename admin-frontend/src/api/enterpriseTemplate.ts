// src/api/enterpriseTemplate.ts
import request from '@/utils/request'
import type { RoleInfo } from './role';

// 分页请求参数
export interface PaginationParams {
  pageNum?: number;
  pageSize?: number;
}

// 分页响应结构
export interface PaginatedResponse<T> {
  records: T[];
  total: number;
  current: number;
  size: number;
  // 兼容可能的数据嵌套
  data?: {
    records?: T[];
    total?: number;
    current?: number;
    size?: number;
  };
}

/**
 * 模板类型
 * 1: 平台租户模板
 * 2: 企业租户模板
 * 3: 代理所租户模板
 */
export enum TemplateType {
  PLATFORM = 1,
  ENTERPRISE = 2,
  AGENCY = 3,
}

export const TemplateTypeMap: Record<TemplateType, string> = {
  [TemplateType.PLATFORM]: '平台租户模板',
  [TemplateType.ENTERPRISE]: '企业租户模板',
  [TemplateType.AGENCY]: '代理所租户模板',
};

/**
 * 行业类型 (0:教育 1:医疗 2:金融)
 */
export enum IndustryType {
  EDUCATION = 0,
  MEDICAL = 1,
  FINANCE = 2,
}

export const IndustryTypeMap: Record<IndustryType, string> = {
  [IndustryType.EDUCATION]: '教育',
  [IndustryType.MEDICAL]: '医疗',
  [IndustryType.FINANCE]: '金融',
};

/**
 * 租户模板信息
 */
export interface TenantTemplateInfo {
  id: string | number;
  templateCode: string; 
  templateName: string;
  templateType: TemplateType; // 模板类型
  templateTypeName?: string; // 模板类型名称
  industryType: IndustryType; // 行业类型
  industryTypeName?: string; // 行业类型名称
  dataIsolationMode?: number; // 数据隔离模式
  isSystem: 0 | 1; // 是否系统模板
  templateDesc?: string; // 模板描述
  createTime?: string | number;
  updateTime?: string | number;
  // 前端额外属性
  status?: string; // 状态（已启用、已禁用）
  // 模板绑定的角色列表
  roles?: Array<{
    id: string | number;
    roleId?: string | number;
    isInherit?: 0 | 1;
    [key: string]: any; // 允许其他可能的字段
  }>;
}

/**
 * 查询租户模板列表的参数
 */
export interface GetTenantTemplateListParams extends PaginationParams {
  templateName?: string; // 模板名称
  templateCode?: string; // 模板编码
  templateType?: TemplateType; // 模板类型
  industryType?: IndustryType; // 行业类型
  isSystem?: 0 | 1; // 是否系统模板
}

/**
 * 创建租户模板请求体
 */
export interface CreateTenantTemplatePayload {
  templateCode: string; // 模板编码
  templateName: string; // 模板名称
  templateType: TemplateType; // 模板类型
  industryType: IndustryType; // 行业类型
  dataIsolationMode?: number; // 数据隔离模式
  isSystem?: 0 | 1; // 是否系统模板
  templateDesc?: string; // 模板描述
}

/**
 * 更新租户模板请求体
 */
export interface UpdateTenantTemplatePayload {
  id: string | number; // 模板ID
  templateName?: string; // 模板名称
  templateType?: TemplateType; // 模板类型
  industryType?: IndustryType; // 行业类型
  dataIsolationMode?: number; // 数据隔离模式
  isSystem?: 0 | 1; // 是否系统模板
  templateDesc?: string; // 模板描述
}

/**
 * 角色绑定信息
 */
export interface RoleBinding {
  roleId: string | number; // 角色ID
  isInherit: 0 | 1; // 是否继承（1:是，0:否）
}

/**
 * 绑定模板角色请求体
 */
export interface BindTemplateRolesPayload {
  templateId: string | number; // 模板ID
  roleBinds: RoleBinding[]; // 角色绑定信息列表
}

/**
 * 分页查询租户模板
 */
export const getTenantTemplateList = (params: GetTenantTemplateListParams): Promise<PaginatedResponse<TenantTemplateInfo>> => {
  return request({
    url: '/tenant/api/tenant/templates/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取租户模板详情
 */
export const getTenantTemplateDetail = (id: string | number): Promise<TenantTemplateInfo> => {
  return request({
    url: `/tenant/api/tenant/templates/${id}`,
    method: 'get'
  })
}

/**
 * 创建租户模板
 */
export const createTenantTemplate = (data: CreateTenantTemplatePayload) => {
  return request({
    url: '/tenant/api/tenant/templates',
    method: 'post',
    data
  })
}

/**
 * 更新租户模板
 */
export const updateTenantTemplate = (data: UpdateTenantTemplatePayload) => {
  return request({
    url: '/tenant/api/tenant/templates',
    method: 'put',
    data
  })
}

/**
 * 删除租户模板
 */
export const deleteTenantTemplate = (id: string | number) => {
  return request({
    url: `/tenant/api/tenant/templates/${id}`,
    method: 'delete'
  })
}

/**
 * 绑定模板角色
 */
export const bindTemplateRoles = (data: BindTemplateRolesPayload) => {
  return request({
    url: '/tenant/api/tenant/templates/roles/bind',
    method: 'post',
    data
  })
}

/**
 * 获取指定角色类型的角色列表（从role.ts中引入）
 * 根据模板类型获取对应类型的角色列表
 */
import { getRolesByType, RoleType } from '@/api/role';

export const getRolesForTemplateType = async (templateType: TemplateType): Promise<RoleInfo[]> => {
  // 根据模板类型选择对应的角色类型
  let roleType: RoleType;
  
  switch (templateType) {
    case TemplateType.PLATFORM:
      roleType = RoleType.PLATFORM;
      break;
    case TemplateType.ENTERPRISE:
      roleType = RoleType.ENTERPRISE;
      break;
    case TemplateType.AGENCY:
      roleType = RoleType.AGENCY;
      break;
    default:
      roleType = RoleType.ENTERPRISE; // 默认企业角色
  }
  
  return getRolesByType(roleType);
}

/**
 * 获取模板已绑定的角色
 * 注：接口文档中没有单独获取已绑定角色的API，此处使用模板详情接口
 */
export const getTemplateBoundRoles = async (templateId: string | number): Promise<RoleBinding[]> => {
  try {
    const templateDetail = await getTenantTemplateDetail(templateId);
    if (templateDetail && templateDetail.roles) {
      // 假设roles字段包含角色绑定信息
      return templateDetail.roles.map(role => ({
        roleId: role.id,
        isInherit: role.isInherit || 1 // 默认继承
      }));
    }
    return [];
  } catch (error) {
    console.error('获取模板绑定角色失败:', error);
    return [];
  }
}

/**
 * 获取模板绑定的角色列表
 */
export const getTemplateRoles = (templateId: string | number): Promise<RoleInfo[]> => {
  return request({
    url: `/tenant/api/tenant/templates/${templateId}/roles`,
    method: 'get'
  })
} 