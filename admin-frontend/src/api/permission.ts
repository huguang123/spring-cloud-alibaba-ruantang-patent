import request from '@/utils/request'
import {
  mockGetPlatformRoles,
  mockGetRolePermissions,
  mockSaveRolePermissions,
  mockAddPlatformRole,
  mockUpdatePlatformRole,
  mockDeletePlatformRole
} from '@/api/mock/permissionMock';
import { RoleType } from '@/api/role';

// --- Define Types Directly Here (Temporary) ---
/**
 * sys_roles 表对应的平台角色类型 (roles_type = 0)
 */
export interface PlatformRole {
  id: number | string; 
  rolesCode: string;
  rolesName: string;
  tenantId: number; 
  rolesType: 0;
  rolesDescription?: string;
  createTime?: number;
  updateTime?: number;
}

/**
 * 前端权限节点 (perm_node) 或模块 (perm_module) 映射到统一的树节点结构
 */
export interface PermissionNode {
  id: string; // Unique ID for tree/checkboxes (e.g., `module-${moduleId}` or `node-${nodeId}`)
  label: string; // nodeName or moduleName
  // Type to distinguish between different kinds of nodes in the tree
  nodeType: 0 | 1 | 2 | 3 | 10; // 0: Module Root, 1: Menu, 2: Button, 3: Field, 10: Data Module Sub-item
  moduleType?: 1 | 2; // 1: Functional Module Root, 2: Data Module Root (only for top-level nodes)
  permType?: 0 | 1; // 0: Operation, 1: Data (relevant for leaves)
  permId?: number | string; // sys_perms.id (if permType=0)
  dataPolicyId?: number | string; // sys_data_policy.id (if permType=1)
  dataScope?: 1 | 2; // 1: View, 2: Edit (if nodeType=3 or data nodes)
  isBasic?: 0 | 1;
  rawModuleId: number | string; // Original perm_module.id or a reference ID for the module group
  rawNodeId?: number | string; // Original perm_node.id (for leaf nodes)
  // Indicates if this node itself is checkable (usually leaf nodes)
  isLeaf?: boolean; 
  children?: PermissionNode[];
  // Optional properties for display
  icon?: string;
}

/**
 * 权限树类型
 */
export type PermissionTree = PermissionNode[];

/**
 * 保存角色权限时发送到后端的数据结构
 * (Assuming backend wants a single list of leaf node IDs for now)
 */
export interface RolePermissionSavePayload {
  roleId: number | string;
  nodeIds: string[]; // Combined list of selected functional and data leaf node IDs
}
// --- End Define Types ---

// 权限模块类型
export interface PermissionModule {
  id: number;
  moduleName: string;
  parentId: number | null;
  tenantId: number;
  moduleType: number; // 1=功能权限 2=数据权限
  sort: number;
  icon: string;
  createTime: number;
  updateTime: number;
  children?: PermissionModule[];
}

// 获取权限模块树
export function getPermissionModuleTree(params?: { tenantId?: number }) {
  return request({
    url: '/sys/permModule/tree',
    method: 'get',
    params
  })
}

// 创建权限模块
export function createPermissionModule(data: Partial<PermissionModule>) {
  return request({
    url: '/sys/permModule',
    method: 'post',
    data
  })
}

// 更新权限模块
export function updatePermissionModule(id: number, data: Partial<PermissionModule>) {
  return request({
    url: `/sys/permModule/${id}`,
    method: 'put',
    data
  })
}

// 删除权限模块
export function deletePermissionModule(id: number) {
  return request({
    url: `/sys/permModule/${id}`,
    method: 'delete'
  })
}

// 获取模块下的权限节点
export function getPermissionNodes(moduleId: number) {
  return request({
    url: `/sys/permNode/list`,
    method: 'get',
    params: { moduleId }
  })
}

// 创建权限节点
export function createPermissionNode(data: Partial<PermissionNode>) {
  return request({
    url: '/sys/permNode',
    method: 'post',
    data
  })
}

// 更新权限节点
export function updatePermissionNode(id: number, data: Partial<PermissionNode>) {
  return request({
    url: `/sys/permNode/${id}`,
    method: 'put',
    data
  })
}

// 删除权限节点
export function deletePermissionNode(id: number) {
  return request({
    url: `/sys/permNode/${id}`,
    method: 'delete'
  })
}

/**
 * 获取平台角色列表
 */
export const getPlatformRoles = async (params?: { keyword?: string }): Promise<{ list: PlatformRole[], total: number }> => {
  // TODO: Replace with actual API call to backend endpoint for platform roles (roles_type=0)
  // Example: return request({ url: '/sys/roles/platform', method: 'get', params });
  console.log('API Service: Calling mockGetPlatformRoles');
  return mockGetPlatformRoles(params);
};

/**
 * 获取指定角色的权限树和已选中的节点ID
 * @param roleId - 角色ID
 */
export const getRolePermissions = async (roleId: string | number): Promise<{ treeData: PermissionTree, selectedNodeIds: string[] }> => {
  // TODO: Replace with actual API call to backend endpoint for role permissions
  // Example: return request({ url: `/sys/roles/${roleId}/permissions`, method: 'get' });
  console.log('API Service: Calling mockGetRolePermissions for role', roleId);
  return mockGetRolePermissions(roleId);
};

// New function to get permissions based on role type
export const getPermissionsForRoleConfig = async (
  roleId: string | number,
  roleType: RoleType
): Promise<{ treeData: PermissionTree, selectedNodeIds: string[] }> => {
  console.log(`API: Getting permissions for role ${roleId}, type ${roleType}`);
  if (roleType === RoleType.PLATFORM) {
    // For platform roles, use the existing function
    return getRolePermissions(roleId);
  }

  // For ENTERPRISE and AGENCY roles, fetch from system_template_permissions (mocked for now)
  // This mock structure should be similar to what getRolePermissions returns
  let mockTreeData: PermissionTree = [];
  let mockSelectedIds: string[] = [];

  // TODO: Replace with actual API call to fetch permissions from system_template_permissions
  // based on roleType or a specific system template linked to that roleType.

  if (roleType === RoleType.ENTERPRISE) {
    mockTreeData = [
      {
        id: 'ent_module_1',
        label: '企业模板专属模块A',
        moduleType: 1, // Functional
        nodeType: 0, // Module Root
        rawModuleId: 'ent_module_1',
        children: [
          { id: 'ent_func_1_1', label: '企业功能A1', isLeaf: true, nodeType: 2, rawModuleId: 'ent_module_1' }, 
          { id: 'ent_func_1_2', label: '企业功能A2', isLeaf: true, nodeType: 2, rawModuleId: 'ent_module_1' }, 
        ],
      },
      {
        id: 'ent_module_data_1',
        label: '企业数据权限集X',
        moduleType: 2, // Data
        nodeType: 0, // Module Root
        rawModuleId: 'ent_module_data_1',
        children: [
          { id: 'ent_data_1_1', label: '查看企业数据X1', isLeaf: true, nodeType: 10, rawModuleId: 'ent_module_data_1' }, 
        ],
      }
    ];
    if (roleId === 'some_specific_enterprise_role_id_to_test') { 
        mockSelectedIds = ['ent_func_1_1'];
    } else {
        mockSelectedIds = ['ent_func_1_2', 'ent_data_1_1'];
    }
    
  } else if (roleType === RoleType.AGENCY) {
    mockTreeData = [
      {
        id: 'agn_module_1',
        label: '代理所模板模块B',
        moduleType: 1, // Functional
        nodeType: 0, // Module Root
        rawModuleId: 'agn_module_1',
        children: [
          { id: 'agn_func_1_1', label: '代理所功能B1', isLeaf: true, nodeType: 2, rawModuleId: 'agn_module_1' }, 
        ],
      },
    ];
    mockSelectedIds = ['agn_func_1_1'];
  }

  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ treeData: mockTreeData, selectedNodeIds: mockSelectedIds });
    }, 300);
  });
};

/**
 * 保存角色权限配置
 */
export const saveRolePermissions = async (payload: RolePermissionSavePayload): Promise<{ success: boolean }> => {
  // TODO: Replace with actual API call. Adjust payload if backend needs separate lists.
  // Example: request({ url: `/sys/roles/${payload.roleId}/permissions`, method: 'post', data: { functionalNodeIds: ..., dataNodeIds: ... } });
  console.log('API Service: Calling mockSaveRolePermissions');
  return mockSaveRolePermissions(payload); // Pass the potentially updated payload structure
};

/**
 * 新增平台角色
 */
export const addPlatformRole = async (roleData: Omit<PlatformRole, 'id' | 'tenantId' | 'rolesType'>): Promise<PlatformRole> => {
  // TODO: Replace with actual API call to backend endpoint for adding platform role
  // Example: return request({ url: '/sys/roles/platform', method: 'post', data: { ...roleData, rolesType: 0, tenantId: 0 } });
  console.log('API Service: Calling mockAddPlatformRole');
  return mockAddPlatformRole(roleData);
};

/**
 * 更新平台角色基本信息
 */
export const updatePlatformRole = async (roleData: PlatformRole): Promise<PlatformRole> => {
  // TODO: Replace with actual API call to backend endpoint for updating platform role
  // Example: return request({ url: `/sys/roles/platform/${roleData.id}`, method: 'put', data: roleData });
  console.log('API Service: Calling mockUpdatePlatformRole');
  return mockUpdatePlatformRole(roleData);
};

/**
 * 删除平台角色
 */
export const deletePlatformRole = async (roleId: number | string): Promise<{ success: boolean }> => {
  // TODO: Replace with actual API call to backend endpoint for deleting platform role
  // Example: return request({ url: `/sys/roles/platform/${roleId}`, method: 'delete' });
  console.log('API Service: Calling mockDeletePlatformRole for role', roleId);
  return mockDeletePlatformRole(roleId);
};

// 操作权限接口相关类型定义
export interface OperationPermission {
  id?: number;
  permsCode: string;
  permsName: string;
  apiMethod?: string;
  apiPath?: string;
  permType?: string; // 修改为permType，匹配后端字段
  permsDescription?: string;
  createTime?: number;
  updateTime?: number;
}

export interface PermQueryParams {
  permsName?: string;
  permsCode?: string;
  apiMethod?: string;
  permType?: string; // 改回permType，匹配后端接口
  pageNum: number;
  pageSize: number;
}

export interface PermPageResult {
  records: OperationPermission[];
  total: number;
  size: number;
  current: number;
}

/**
 * 分页查询操作权限
 */
export function getOperationPermissionsPage(params: PermQueryParams) {
  return request({
    url: '/perm/api/perm/perms/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取操作权限详情
 */
export function getOperationPermissionDetail(id: number) {
  return request({
    url: `/perm/api/perm/perms/${id}`,
    method: 'get'
  })
}

/**
 * 创建操作权限
 */
export function createOperationPermission(data: OperationPermission) {
  return request({
    url: '/perm/api/perm/perms',
    method: 'post',
    data
  })
}

/**
 * 更新操作权限
 */
export function updateOperationPermission(data: OperationPermission) {
  return request({
    url: '/perm/api/perm/perms',
    method: 'put',
    data
  })
}

/**
 * 删除操作权限
 */
export function deleteOperationPermission(id: number) {
  return request({
    url: `/perm/api/perm/perms/${id}`,
    method: 'delete'
  })
}

// 数据权限策略相关类型
export interface DataStrategy {
  id?: number;
  policyCode: string;
  policyName: string;
  conditionType: number; // 1:SQL片段 2:SpEL
  conditionTypeName?: string;
  conditionExpression: string;
  effectTables: string;
  priority: number;
  policyDescription?: string;
  createTime?: number;
  updateTime?: number;
}

export interface DataStrategyQueryParams {
  pageNum: number;
  pageSize: number;
  policyName?: string;
  policyCode?: string;
  conditionType?: number;
}

export interface DataStrategyPageResult {
  records: DataStrategy[];
  total: number;
  size: number;
  current: number;
}

// 数据策略 API
export function getDataStrategiesPage(params: DataStrategyQueryParams) {
  return request({
    url: '/perm/api/perm/policies/page',
    method: 'post',
    data: params
  })
}

export function getDataStrategyDetail(id: number) {
  return request({
    url: `/perm/api/perm/policies/${id}`,
    method: 'get'
  })
}

export function createDataStrategy(data: DataStrategy) {
  return request({
    url: '/perm/api/perm/policies',
    method: 'post',
    data
  })
}

export function updateDataStrategy(data: DataStrategy) {
  return request({
    url: '/perm/api/perm/policies',
    method: 'put',
    data
  })
}

export function deleteDataStrategy(id: number) {
  return request({
    url: `/perm/api/perm/policies/${id}`,
    method: 'delete'
  })
}

// 租户模板类型
export interface PermissionTemplate {
  id: number;
  templateCode: string;
  templateName: string;
  templateType: number; // 1:平台租户模板 2:企业租户模板 3:代理所租户模板
  templateTypeName?: string;
  industryType: number; // 0:教育 1:医疗 2:金融等
  industryTypeName?: string;
  dataIsolationMode: number;
  isSystem: number; // 0:否 1:是
  templateDesc?: string;
  createTime: number;
  updateTime?: number;
  moduleCount?: number;
  status?: string;
}

// 角色信息
export interface RoleInfo {
  id: number;
  roleId?: number;
  roleName?: string;
  roleCode?: string;
  roleDescription?: string;
  rolesType?: number;
}

// 模板角色绑定
export interface TemplateRoleBind {
  roleId: number | string;
  isInherit: 0 | 1; // 0:不继承 1:继承
}

// 模板角色绑定请求
export interface TemplateRoleBindRequest {
  templateId: number;
  roleBinds: TemplateRoleBind[];
}

// 分页查询租户模板
export function getPermissionTemplates(params: any) {
  return request({
    url: '/tenant/api/tenant/templates/page',
    method: 'post',
    data: params
  })
}

// 获取模板详情
export function getTemplateDetail(id: number) {
  return request({
    url: `/tenant/api/tenant/templates/${id}`,
    method: 'get'
  })
}

// 创建模板
export function createTemplate(data: Partial<PermissionTemplate>) {
  return request({
    url: '/tenant/api/tenant/templates',
    method: 'post',
    data
  })
}

// 更新模板
export function updateTemplate(data: Partial<PermissionTemplate>) {
  return request({
    url: '/tenant/api/tenant/templates',
    method: 'put',
    data
  })
}

// 删除模板
export function deleteTemplate(id: number) {
  return request({
    url: `/tenant/api/tenant/templates/${id}`,
    method: 'delete'
  })
}

// 获取模板绑定的角色
export function getTemplateRoles(templateId: number) {
  return request({
    url: `/tenant/api/tenant/templates/${templateId}/roles`,
    method: 'get'
  })
}

// 为模板绑定角色
export function bindTemplateRoles(data: TemplateRoleBindRequest) {
  return request({
    url: '/tenant/api/tenant/templates/roles/bind',
    method: 'post',
    data
  })
}

// 角色类型枚举 - 使用不同的名称避免冲突
export enum TemplateRoleType {
  PLATFORM = 1,
  ENTERPRISE = 2,
  AGENCY = 3
}

// 角色类型映射
export const RoleTypeMap: Record<TemplateRoleType, string> = {
  [TemplateRoleType.PLATFORM]: '平台角色',
  [TemplateRoleType.ENTERPRISE]: '企业角色',
  [TemplateRoleType.AGENCY]: '代理所角色'
};

// 获取角色列表
export function getRolesByType(roleType: TemplateRoleType) {
  return request({
    url: '/tenant/api/role/roles/type',
    method: 'get',
    params: { roleType }
  })
} 