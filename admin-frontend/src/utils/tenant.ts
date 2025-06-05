/**
 * 租户信息工具函数
 * 提供获取/设置租户ID、组织ID等全局信息的方法
 */

// 获取租户ID
export function getTenantId(): string {
  return localStorage.getItem('tenant_id') || '';
}

// 设置租户ID
export function setTenantId(id: string | number): void {
  localStorage.setItem('tenant_id', String(id));
}

// 获取组织ID
export function getOrgId(): string {
  return localStorage.getItem('org_id') || '';
}

// 设置组织ID
export function setOrgId(id: string | number): void {
  localStorage.setItem('org_id', String(id));
}

// 获取用户ID
export function getUserId(): string {
  return localStorage.getItem('user_id') || '';
}

// 获取所有租户相关信息
export function getTenantInfo() {
  return {
    tenantId: getTenantId(),
    orgId: getOrgId(),
    userId: getUserId()
  };
}

// 清除所有租户相关信息
export function clearTenantInfo(): void {
  localStorage.removeItem('tenant_id');
  localStorage.removeItem('org_id');
} 