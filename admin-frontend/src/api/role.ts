// 定义基本的分页参数和响应结构
export interface PaginationParams {
  pageNum?: number; // 修改与后端匹配
  pageSize?: number;
}

export interface PaginatedResponse<T> {
  records: T[]; // 修改与后端匹配
  total: number;
  current: number; // 修改与后端匹配
  size: number; // 修改与后端匹配
  data?: { // 添加可选的 data 字段，兼容嵌套的响应结构
    records?: T[];
    total?: number;
    current?: number;
    size?: number;
  };
}

/**
 * 角色类型
 * 1: 平台角色
 * 2: 企业角色
 * 3: 代理所角色
 */
export enum RoleType {
  PLATFORM = 1, // 修改为与接口文档匹配的值
  ENTERPRISE = 2,
  AGENCY = 3,
}

export const RoleTypeMap: Record<RoleType, string> = {
  [RoleType.PLATFORM]: '平台角色',
  [RoleType.ENTERPRISE]: '企业角色',
  [RoleType.AGENCY]: '代理所角色',
};

export interface RoleInfo {
  id: string;
  rolesCode: string; // 使用与后端一致的字段名
  rolesName: string;
  rolesType: RoleType;
  rolesTypeName?: string; // 添加后端返回的类型名称字段
  rolesDescription?: string;
  createTime?: string | number; // 支持字符串和数字类型的时间戳
  updateTime?: string | number;
}

export interface GetRoleListParams extends PaginationParams {
  roleName?: string; // 与API文档匹配的参数名
  roleCode?: string;
  roleType?: RoleType | string | undefined; // 移除 null 类型
  keyword?: string; // 兼容旧代码
  page?: number; // 兼容旧代码
}

import request from '@/utils/request'

// 分页查询角色列表
export const getRoleList = (params: GetRoleListParams): Promise<PaginatedResponse<RoleInfo>> => {
  return request({
    url: '/perm/api/perm/roles/page',
    method: 'post',
    data: {
      roleName: params.roleName || params.keyword, // 兼容旧代码
      roleCode: params.roleCode,
      roleType: params.roleType,
      pageNum: params.pageNum || params.page || 1, // 兼容旧代码
      pageSize: params.pageSize || 10
    }
  })
}

export type CreateRolePayload = Omit<RoleInfo, 'id' | 'createTime' | 'updateTime' | 'rolesTypeName'>;

// 创建角色
export const createRole = (payload: CreateRolePayload): Promise<RoleInfo> => {
  return request({
    url: '/perm/api/perm/roles',
    method: 'post',
    data: payload
  })
}

// 根据接口文档修改更新角色的类型定义
export interface UpdateRolePayload {
  id: string;
  rolesName?: string;
  rolesDescription?: string;
}

// 更新角色
export const updateRole = (payload: UpdateRolePayload): Promise<boolean> => {
  // 确保只传递接口文档要求的字段
  const data = {
    id: payload.id,
    rolesName: payload.rolesName,
    rolesDescription: payload.rolesDescription
  };
  
  return request({
    url: '/perm/api/perm/roles',
    method: 'put',
    data
  })
}

// 删除角色
export const deleteRole = (roleId: string): Promise<boolean> => {
  return request({
    url: `/perm/api/perm/roles/${roleId}`,
    method: 'delete'
  })
}

// 获取角色详情
export const getRoleDetails = (roleId: string): Promise<RoleInfo> => {
  return request({
    url: `/perm/api/perm/roles/${roleId}`,
    method: 'get'
  })
}

// 获取角色权限树结构
export interface PermissionModuleNode {
  id: number | string;
  moduleName: string;
  moduleType: number;
  moduleTypeName: string;
  nodes: PermissionNodeItem[];
}

export interface PermissionNodeItem {
  id: number | string;
  nodeName: string;
  nodeType: number;
  nodeTypeName: string;
  permType: number;
  permTypeName: string;
  permId?: number | string;
  permDetail?: any;
  dataPolicyId?: number | string;
  dataPolicyDetail?: any;
  selected: boolean;
}

export interface PermissionTreeResponse {
  operationModules: PermissionModuleNode[];
  dataModules: PermissionModuleNode[];
}

export const getRolePermissionTree = (roleId: string): Promise<PermissionTreeResponse> => {
  return request({
    url: `/perm/api/perm/roles/${roleId}/permission-tree`,
    method: 'get'
  })
}

// 为角色分配权限
export interface AssignPermissionsPayload {
  roleId: string | number;
  permIds: (string | number)[];
  dataPolicyIds: (string | number)[];
}

export const assignPermissions = (payload: AssignPermissionsPayload): Promise<boolean> => {
  return request({
    url: '/perm/api/perm/roles/assign-permissions',
    method: 'post',
    data: payload
  })
}

// 根据角色类型获取角色列表
export const getRolesByType = (roleType: RoleType): Promise<RoleInfo[]> => {
  return request({
    url: `/perm/api/perm/roles/type/${roleType}`,
    method: 'get'
  })
}

// 获取用户绑定的角色列表
export const getUserRoles = (userId: string): Promise<RoleInfo[]> => {
  return request({
    url: `/perm/api/perm/roles/user/${userId}`,
    method: 'get'
  })
}

// 为用户分配角色
export interface AssignUserRolesPayload {
  userId: string | number;
  roleIds: (string | number)[];
}

export const assignUserRoles = (payload: AssignUserRolesPayload): Promise<boolean> => {
  return request({
    url: '/perm/api/perm/roles/assign-user-roles',
    method: 'post',
    data: payload
  })
} 