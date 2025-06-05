import request from '@/utils/request'

// ------------- 模板相关类型和接口 -------------
export interface PermissionTemplate {
  id?: string | number;
  templateCode: string;
  templateType: number; // 1=平台配置模板 2=企业租户配置模板 3=代理所配置模板
  templateTypeName?: string;
  baseDescription?: string;
  createTime?: number;
  updateTime?: number;
}

export interface TemplateQueryParams {
  templateCode?: string;
  templateType?: number;
  pageNum: number;
  pageSize: number;
}

export interface TemplatePageResult {
  records: PermissionTemplate[];
  total: number;
  size: number;
  current: number;
}

/**
 * 分页查询模板
 */
export function getTemplatesPage(params: TemplateQueryParams) {
  return request({
    url: '/perm/api/perm/templates/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取模板详情
 */
export function getTemplateDetail(id: number | string) {
  return request({
    url: `/perm/api/perm/templates/${id}`,
    method: 'get'
  })
}

/**
 * 按类型获取模板
 */
export function getTemplateByType(templateType: number | string) {
  return request({
    url: `/perm/api/perm/templates/type/${templateType}`,
    method: 'get'
  })
}

/**
 * 创建模板
 */
export function createTemplate(data: PermissionTemplate) {
  return request({
    url: '/perm/api/perm/templates',
    method: 'post',
    data
  })
}

/**
 * 更新模板
 */
export function updateTemplate(data: PermissionTemplate) {
  return request({
    url: '/perm/api/perm/templates',
    method: 'put',
    data
  })
}

/**
 * 删除模板
 */
export function deleteTemplate(id: number | string) {
  return request({
    url: `/perm/api/perm/templates/${id}`,
    method: 'delete'
  })
}

// ------------- 版本相关类型和接口 -------------
export interface TemplateVersion {
  id?: string | number;
  templateId: string | number;
  templateName?: string;
  version: string;
  isDefault: number; // 0: 非默认版本, 1: 默认版本
  versionDescription?: string;
  createTime?: number;
  updateTime?: number;
}

// 版本更新接口参数类型（按照接口文档）
export interface VersionUpdateParams {
  id: string | number;
  version: string;
  isDefault: number;
  versionDescription?: string;
}

export interface VersionQueryParams {
  templateId?: number | string;
  isDefault?: number;
  pageNum: number;
  pageSize: number;
}

export interface VersionPageResult {
  records: TemplateVersion[];
  total: number;
  size: number;
  current: number;
}

/**
 * 分页查询版本
 */
export function getVersionsPage(params: VersionQueryParams) {
  return request({
    url: '/perm/api/perm/versions/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取版本详情
 */
export function getVersionDetail(id: number | string) {
  return request({
    url: `/perm/api/perm/versions/${id}`,
    method: 'get'
  })
}

/**
 * 获取模板默认版本
 */
export function getDefaultVersion(templateId: number | string) {
  return request({
    url: `/perm/api/perm/versions/default/${templateId}`,
    method: 'get'
  })
}

/**
 * 创建版本
 */
export function createVersion(data: TemplateVersion) {
  return request({
    url: '/perm/api/perm/versions',
    method: 'post',
    data
  })
}

/**
 * 更新版本
 */
export function updateVersion(data: VersionUpdateParams) {
  return request({
    url: '/perm/api/perm/versions',
    method: 'put',
    data
  })
}

/**
 * 删除版本
 */
export function deleteVersion(id: number | string) {
  return request({
    url: `/perm/api/perm/versions/${id}`,
    method: 'delete'
  })
}

// ------------- 模块相关类型和接口 -------------
export interface PermissionModule {
  id?: number | string;
  moduleName: string;
  templateVersionId?: number | string;
  moduleType: number; // 1: 功能权限模块, 2: 数据权限模块
  moduleTypeName?: string;
  sort?: number;
  createTime?: number;
  updateTime?: number;
}

export interface ModuleQueryParams {
  templateVersionId?: number | string;
  moduleName?: string;
  moduleType?: number;
  pageNum: number;
  pageSize: number;
}

export interface ModulePageResult {
  records: PermissionModule[];
  total: number;
  size: number;
  current: number;
}

/**
 * 分页查询模块
 */
export function getModulesPage(params: ModuleQueryParams) {
  return request({
    url: '/perm/api/perm/modules/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取模块详情
 */
export function getModuleDetail(id: number | string) {
  return request({
    url: `/perm/api/perm/modules/${id}`,
    method: 'get'
  })
}

/**
 * 根据版本获取模块列表
 * 使用原始字符串ID直接构建URL，避免任何ID转换或处理
 */
export function getModulesByVersion(versionId: number | string) {
  // 保留原始版本ID
  const originalId = String(versionId);
  console.log(`调用getModulesByVersion - 原始ID: ${originalId}, 类型: ${typeof originalId}, 长度: ${originalId.length}`);
  
  // 避免任何ID处理或转换
  return request({
    url: `/perm/api/perm/modules/version/${originalId}`,
    method: 'get',
    // 添加指示器，告诉请求拦截器不要处理此URL
    headers: {
      'X-Preserve-IDs': 'true' 
    }
  });
}

/**
 * 创建模块
 */
export function createModule(data: PermissionModule) {
  return request({
    url: '/perm/api/perm/modules',
    method: 'post',
    data
  })
}

/**
 * 更新模块
 */
export function updateModule(data: Omit<PermissionModule, 'templateVersionId'> & { templateVersionId?: number | string }) {
  return request({
    url: '/perm/api/perm/modules',
    method: 'put',
    data
  })
}

/**
 * 删除模块
 */
export function deleteModule(id: number | string) {
  return request({
    url: `/perm/api/perm/modules/${id}`,
    method: 'delete'
  })
}

// ------------- 节点相关类型和接口 -------------
export interface PermissionNode {
  id?: number | string;
  nodeName: string;
  moduleId: number | string;
  nodeType: number; // 1: 菜单项, 2: 操作按钮, 3: 数据字段
  nodeTypeName?: string;
  permType: number; // 0: 操作权限, 1: 数据权限
  permTypeName?: string;
  permId?: number | string; // 操作权限ID (permType=0时使用)
  dataPolicyId?: number | string; // 数据策略ID (permType=1时使用)
  isBasic?: number; // 0: 普通权限, 1: 基础权限
  dataScope?: number; // 1: 查看权限, 2: 编辑权限 (nodeType=3时使用)
  permDetail?: any; // 权限详情信息
  createTime?: number;
  updateTime?: number;
}

export interface NodeQueryParams {
  moduleId?: number | string;
  nodeName?: string;
  nodeType?: number;
  permType?: number;
  pageNum: number;
  pageSize: number;
}

export interface NodePageResult {
  records: PermissionNode[];
  total: number;
  size: number;
  current: number;
}

/**
 * 分页查询节点
 */
export function getNodesPage(params: NodeQueryParams) {
  return request({
    url: '/perm/api/perm/nodes/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取节点详情
 */
export function getNodeDetail(id: number | string) {
  return request({
    url: `/perm/api/perm/nodes/${id}`,
    method: 'get'
  })
}

/**
 * 根据模块获取节点
 */
export function getNodesByModule(moduleId: number | string) {
  // 保留原始模块ID
  const originalId = String(moduleId);
  console.log(`调用getNodesByModule - 原始ID: ${originalId}, 类型: ${typeof originalId}, 长度: ${originalId.length}`);
  
  return request({
    url: `/perm/api/perm/nodes/module/${originalId}`,
    method: 'get',
    // 添加指示器，告诉请求拦截器不要处理此URL
    headers: {
      'X-Preserve-IDs': 'true' 
    }
  }).then(response => {
    console.log(`getNodesByModule响应数据详情: ${JSON.stringify(response)}`);
    
    // 直接返回响应，让组件处理不同格式的数据
    return response;
  }).catch(error => {
    console.error(`getNodesByModule请求出错: ${error}`);
    throw error;
  });
}

/**
 * 创建节点
 */
export function createNode(data: PermissionNode) {
  return request({
    url: '/perm/api/perm/nodes',
    method: 'post',
    data
  })
}

/**
 * 更新节点
 */
export function updateNode(data: PermissionNode) {
  return request({
    url: '/perm/api/perm/nodes',
    method: 'put',
    data
  })
}

/**
 * 删除节点
 */
export function deleteNode(id: number | string) {
  return request({
    url: `/perm/api/perm/nodes/${id}`,
    method: 'delete'
  })
} 