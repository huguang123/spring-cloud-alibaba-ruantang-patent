import request from '@/utils/request'
import { getTenantId } from '@/utils/tenant'

// 组织结构数据类型
export interface Organization {
  id: number;
  orgName: string;
  parentId: number;
  tenantId: number;
  orgCode: string;
  orgPath?: string;
  orgPhone?: string;
  orgMail?: string;
  state: number; // 0: 禁用，1: 启用
  description?: string;
  createBy?: number;
  createTime: number;
  updateTime: number;
  staffCount?: number; // 员工数量
  memberCount?: number; // API返回的实际成员数量字段
  children?: Organization[]; // 树形结构子节点
}

// 组织用户数据类型
export interface OrganizationUser {
  id: number;
  username: string;
  realName: string;
  email?: string;
  phone?: string;
  userName?: string; // API返回的用户名字段
  userMail?: string; // API返回的邮箱字段
  userPhone?: string; // API返回的电话字段
  loginName?: string; // API返回的登录名字段
  roles: Role[]; // 用户角色
  entryDate?: number; // 入职日期
  joinDate?: number; // API返回的入职日期字段
  orgId: number; // 所属组织ID
  roleIds?: number[]; // 角色ID列表
}

// 角色类型
export interface Role {
  id: number;
  rolesCode: string;
  rolesName: string;
  rolesType: number;
  roleId?: number; // API返回的角色ID字段
}

// 获取当前登录用户的租户ID
export function getCurrentTenantId(): string {
  const tenantId = getTenantId();
  if (!tenantId) {
    console.warn('警告: 租户ID为空，请确保已正确登录并获取用户信息');
  }
  return tenantId;
}

// 获取组织树
export function getOrganizationTree(tenantId?: any) {
  const finalTenantId = tenantId || getCurrentTenantId();
  console.log('获取组织树，使用租户ID:', finalTenantId);
  
  if (!finalTenantId) {
    console.error('错误: 获取组织树时租户ID为空');
    // 返回一个Promise以保持接口一致性
    return Promise.reject(new Error('租户ID不能为空'));
  }
  
  return request({
    url: '/org/api/org/organizations/tree',
    method: 'get',
    params: { tenantId: finalTenantId },
    // 确保租户ID被正确处理，不会丢失精度
    paramsSerializer: (params) => {
      // 将params对象转换为URL查询字符串，确保租户ID作为字符串处理
      return Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(String(params[key]))}`)
        .join('&');
    }
  }).catch(error => {
    console.error('获取组织树失败:', error);
    // 当API失败时，可以选择返回模拟数据
    console.log('使用模拟数据作为后备方案');
    return Promise.resolve({
      code: 200,
      message: 'success',
      data: mockOrganizationTree
    });
  });
}

// 获取组织详情
export function getOrganizationDetail(id: number) {
  return request({
    url: `/org/api/org/organizations/${id}`,
    method: 'get'
  })
}

// 创建组织
export function createOrganization(data: Partial<Organization>) {
  // 确保tenantId作为字符串处理
  const tenantId = data.tenantId || getCurrentTenantId();
  console.log('创建组织，租户ID (原始):', tenantId);
  console.log('创建组织，租户ID (字符串):', String(tenantId));
  
  // 创建新的数据对象，避免修改原始数据
  const newData: Record<string, any> = { ...data };
  
  // 确保所有ID字段都作为字符串处理
  if (newData.id) newData.id = String(newData.id);
  if (newData.parentId) newData.parentId = String(newData.parentId);
  
  // 显式设置租户ID为字符串格式
  newData.tenantId = String(tenantId);
  
  // 记录最终发送的数据
  console.log('创建组织，最终发送数据:', JSON.stringify(newData));
  
  return request({
    url: '/org/api/org/organizations',
    method: 'post',
    data: newData as Partial<Organization>,
    headers: {
      // 添加头信息，明确标识大整数ID需要特殊处理
      'X-BigInt-Fields': 'tenantId,id,parentId'
    }
  });
}

// 更新组织
export function updateOrganization(data: Partial<Organization>) {
  // 创建新的数据对象，避免修改原始数据
  const newData: Record<string, any> = { ...data };
  
  // 确保所有ID字段都作为字符串处理
  if (newData.id) newData.id = String(newData.id);
  if (newData.parentId) newData.parentId = String(newData.parentId);
  
  // 移除tenantId字段，更新接口不需要这个字段
  delete newData.tenantId;
  
  console.log('更新组织，最终发送数据:', JSON.stringify(newData));
  
  return request({
    url: '/org/api/org/organizations',
    method: 'put',
    data: newData as Partial<Organization>,
    headers: {
      // 添加头信息，明确标识大整数ID需要特殊处理
      'X-BigInt-Fields': 'id,parentId'
    }
  });
}

// 删除组织
export function deleteOrganization(id: number) {
  return request({
    url: `/org/api/org/organizations/${id}`,
    method: 'delete'
  })
}

// 获取组织成员列表（分页）
export function getOrganizationUsers(orgId: number, params: any = {}) {
  return request({
    url: '/org/api/org/members/page',
    method: 'get',
    params: {
      ...params,
      orgId,
      tenantId: params.tenantId || getCurrentTenantId()
    }
  })
}

// 添加组织成员
export function addOrganizationUser(orgId: number, data: { userId: number, roleIds: number[] }) {
  return request({
    url: '/org/api/org/members/bind',
    method: 'post',
    data: {
      orgId,
      userId: data.userId,
      roleIds: data.roleIds,
      tenantId: getCurrentTenantId()
    }
  })
}

// 移除组织成员
export function removeOrganizationUser(orgId: number, userId: number, unbindTenant: boolean = false) {
  return request({
    url: '/org/api/org/members/unbind',
    method: 'delete',
    params: {
      orgId,
      userId,
      tenantId: getCurrentTenantId(),
      unbindTenant
    }
  })
}

// 更新组织成员信息
export function updateOrganizationUserRoles(orgId: number, userId: number, roleIds: number[]) {
  return request({
    url: '/org/api/org/members/update',
    method: 'put',
    data: {
      orgId,
      userId,
      roleIds,
      tenantId: getCurrentTenantId()
    }
  })
}

// 分页查询用户列表（搜索用户）
export function searchUsers(params: any = {}) {
  // 注意：用户相关API保留ums前缀，因为它们在用户服务下
  return request({
    url: '/ums/api/user/users/page',
    method: 'post',
    data: {
      ...params
      // 移除tenantId参数，允许搜索所有已注册用户，而不仅限于当前租户
    }
  })
}

// 获取租户绑定的角色列表
export function getRoleList(tenantId?: string) {
  const id = tenantId || getCurrentTenantId();
  console.log('获取角色列表，租户ID:', id);
  // 注意：租户相关API保留ums前缀，因为它们在租户服务下
  return request({
    url: `/tenant/api/tenant/tenants/${id}/roles`,
    method: 'get'
  })
}

// 以下保留模拟数据，用于在API不可用时回退使用
// 模拟数据：组织树
export const mockOrganizationTree: Organization[] = [
  {
    id: 1,
    orgName: '总公司',
    parentId: 0,
    tenantId: 1,
    orgCode: 'HQ',
    orgPath: '.1.',
    state: 1,
    createTime: 1672502400000,
    updateTime: 1672502400000,
    staffCount: 5,
    children: [
      {
        id: 2,
        orgName: '研发中心',
        parentId: 1,
        tenantId: 1,
        orgCode: 'RD',
        orgPath: '.1.2.',
        state: 1,
        createTime: 1672502400000,
        updateTime: 1672502400000,
        staffCount: 8,
        children: []
      },
      {
        id: 3,
        orgName: '市场部',
        parentId: 1,
        tenantId: 1,
        orgCode: 'MKT',
        orgPath: '.1.3.',
        state: 1,
        createTime: 1672502400000,
        updateTime: 1672502400000,
        staffCount: 10,
        children: []
      },
      {
        id: 4,
        orgName: '人力资源部',
        parentId: 1,
        tenantId: 1,
        orgCode: 'HR',
        orgPath: '.1.4.',
        state: 1,
        createTime: 1672502400000,
        updateTime: 1672502400000,
        staffCount: 6,
        children: []
      },
      {
        id: 5,
        orgName: '财务部',
        parentId: 1,
        tenantId: 1,
        orgCode: 'FN',
        orgPath: '.1.5.',
        state: 1,
        createTime: 1672502400000,
        updateTime: 1672502400000,
        staffCount: 7,
        children: []
      }
    ]
  }
];

// 模拟数据：角色列表
export const mockRoleList: Role[] = [
  { id: 1, rolesCode: 'admin', rolesName: '管理员', rolesType: 0 },
  { id: 2, rolesCode: 'manager', rolesName: '经理', rolesType: 1 },
  { id: 3, rolesCode: 'supervisor', rolesName: '主管', rolesType: 1 },
  { id: 4, rolesCode: 'staff', rolesName: '专员', rolesType: 1 },
];

// 模拟数据：财务部用户列表
export const mockFinanceUsers: OrganizationUser[] = [
  { 
    id: 101, 
    username: 'employee1',
    realName: '员工1',
    email: 'employee1@example.com',
    phone: '13812340000',
    roles: [{ id: 2, rolesCode: 'manager', rolesName: '经理', rolesType: 1 }],
    entryDate: 1577836800000, // 2020-01-01
    orgId: 5
  },
  { 
    id: 102, 
    username: 'employee2',
    realName: '员工2',
    email: 'employee2@example.com',
    phone: '13812340001',
    roles: [{ id: 3, rolesCode: 'supervisor', rolesName: '主管', rolesType: 1 }],
    entryDate: 1590969600000, // 2020-06-01
    orgId: 5
  },
  { 
    id: 103, 
    username: 'employee3',
    realName: '员工3',
    email: 'employee3@example.com',
    phone: '13812340002',
    roles: [{ id: 4, rolesCode: 'staff', rolesName: '专员', rolesType: 1 }],
    entryDate: 1586822400000, // 2020-04-14
    orgId: 5
  },
  { 
    id: 104, 
    username: 'employee4',
    realName: '员工4',
    email: 'employee4@example.com',
    phone: '13812340003',
    roles: [{ id: 2, rolesCode: 'manager', rolesName: '经理', rolesType: 1 }],
    entryDate: 1587600000000, // 2020-04-23
    orgId: 5
  },
  { 
    id: 105, 
    username: 'employee5',
    realName: '员工5',
    email: 'employee5@example.com',
    phone: '13812340004',
    roles: [{ id: 4, rolesCode: 'staff', rolesName: '专员', rolesType: 1 }],
    entryDate: 1584316800000, // 2020-03-16
    orgId: 5
  },
  { 
    id: 106, 
    username: 'employee6',
    realName: '员工6',
    email: 'employee6@example.com',
    phone: '13812340005',
    roles: [{ id: 3, rolesCode: 'supervisor', rolesName: '主管', rolesType: 1 }],
    entryDate: 1591920000000, // 2020-06-12
    orgId: 5
  },
  { 
    id: 107, 
    username: 'employee7',
    realName: '员工7',
    email: 'employee7@example.com',
    phone: '13812340006',
    roles: [{ id: 4, rolesCode: 'staff', rolesName: '专员', rolesType: 1 }],
    entryDate: 1592006400000, // 2020-06-13
    orgId: 5
  }
];

// 模拟数据：用户搜索结果
export function mockSearchUsers(keyword: string): Promise<OrganizationUser[]> {
  return new Promise((resolve) => {
    setTimeout(() => {
      const allUsers = [
        ...mockFinanceUsers,
        { 
          id: 201, 
          username: 'john.doe',
          realName: '约翰·多伊',
          email: 'john.doe@example.com',
          phone: '13900000001',
          roles: [],
          orgId: 0
        },
        { 
          id: 202, 
          username: 'jane.smith',
          realName: '简·史密斯',
          email: 'jane.smith@example.com',
          phone: '13900000002',
          roles: [],
          orgId: 0
        },
        { 
          id: 203, 
          username: 'alex.wong',
          realName: '亚历克斯·王',
          email: 'alex.wong@example.com',
          phone: '13900000003',
          roles: [],
          orgId: 0
        }
      ];
      
      const results = allUsers.filter(user => 
        user.username.toLowerCase().includes(keyword.toLowerCase()) ||
        user.realName.toLowerCase().includes(keyword.toLowerCase()) ||
        (user.email && user.email.toLowerCase().includes(keyword.toLowerCase())) ||
        (user.phone && user.phone.includes(keyword))
      );
      
      resolve(results);
    }, 500);
  });
} 