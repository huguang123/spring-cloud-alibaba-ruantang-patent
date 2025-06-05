import request from '@/utils/request'

// 定义租户类型 - 更新与后端API一致
export interface TenantInfo {
  id: number;
  tenantCode: string;
  tenantName: string;
  tenantType: number; // 1:平台管理租户 2:企业商户租户 3:代理所租户
  tenantTypeName?: string;
  tenantPhone?: string;
  tenantEmail?: string;
  description?: string;
  tenantAddress?: string;
  dataIsolationMode: number; // 1=行级 2=Schema 3=独立库
  dataIsolationModeName?: string;
  expireTime: number;
  adminUserId?: number | string; 
  adminUsername?: string;
  createTime: number;
  updateTime: number;
  templates?: any[];
}

// 定义权限模板类型
export interface PermTemplate {
  id: number;
  templateId?: number;
  templateName: string;
  templateCode: string;
  templateType?: number;
  moduleCount: number;
  createTime: number;
  status: string; // 已启用/未启用
  bindTime?: number;
  effectiveTime?: number;
  bindMode?: number;
}

// 绑定模板请求参数
export interface RoleBinding {
  roleId: string | number;
  isInherit: number; // 0:不继承 1:继承
}

export interface TemplateBindingRequest {
  templateId: number;
  roleBinds: RoleBinding[];
}

// 模板绑定参数
export interface TenantTemplateBindRequest {
  templateId: number;
  bindMode: number; // 1:继承 2:快照
  effectiveTime: number;
}

// 获取租户列表 - 分页查询
export function getTenantList(params: any) {
  return request({
    url: '/tenant/api/tenant/tenants/page',
    method: 'post',
    data: params
  })
}

// 获取租户详情
export function getTenantDetail(id: number) {
  return request({
    url: `/tenant/api/tenant/tenants/${id}`,
    method: 'get'
  })
}

// 创建租户
export function createTenant(data: Partial<TenantInfo>) {
  return request({
    url: '/tenant/api/tenant/tenants',
    method: 'post',
    data
  })
}

// 更新租户
export function updateTenant(id: number, data: Partial<TenantInfo>) {
  const updateData = { ...data, id };
  return request({
    url: '/tenant/api/tenant/tenants',
    method: 'put',
    data: updateData
  })
}

// 删除租户
export function deleteTenant(id: number) {
  return request({
    url: `/tenant/api/tenant/tenants/${id}`,
    method: 'delete'
  })
}

// 获取租户已绑定的权限模板
export function getTenantTemplates(tenantId: number) {
  return request({
    url: `/tenant/api/tenant/tenants/${tenantId}/templates`,
    method: 'get'
  })
}

// 为租户绑定权限模板
export function bindTemplateToTenant(data: {
  tenantId: number;
  templates: TenantTemplateBindRequest[];
}) {
  return request({
    url: '/tenant/api/tenant/tenants/bind/templates',
    method: 'post',
    data
  })
}

// 解绑租户的权限模板
export function unbindTemplateFromTenant(tenantId: number, templateId: number) {
  return request({
    url: `/tenant/api/tenant/tenants/${tenantId}/template/${templateId}`,
    method: 'delete'
  })
}

// 用户搜索接口 - 替换模拟数据
export interface UserForSelect {
  id: number | string;
  username: string;
  email: string;
}

// 搜索用户
export function searchUsers(keyword: string): Promise<UserForSelect[]> {
  return request({
    url: '/ums/api/user/users/page',
    method: 'post',
    data: {
      keyword,
      pageNum: 1,
      pageSize: 10
    }
  }).then((res: any) => {
    // 转换后端返回的用户数据为前端需要的格式
    if (res && res.records) {
      return res.records.map((user: any) => ({
        id: user.id,
        username: user.userName || user.loginName,
        email: user.userMail || ''
      }));
    }
    return [];
  });
} 