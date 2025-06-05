import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { clearTenantInfo } from '@/utils/tenant'

// 使用相对路径访问API - 认证相关
const AUTH_BASE_URL = '/ums/auth';
// 用户信息相关
const USER_BASE_URL = '/ums';

// API请求通用方法
const request = async (url: string, method = 'GET', data: any = null, isUserApi = false) => {
  try {
    // 根据不同API类型选择基础URL
    const baseUrl = isUserApi ? USER_BASE_URL : AUTH_BASE_URL;
    const apiUrl = `${baseUrl}${url}`;
    console.log(`准备发送请求: ${method} ${apiUrl}`);
    
    const headers: Record<string, string> = {
      'Content-Type': 'application/json'
    };
    
    // 添加认证令牌（如果存在）
    const token = localStorage.getItem('auth_token');
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    const options: RequestInit = {
      method,
      headers
    };
    
    if (data) {
      options.body = JSON.stringify(data);
    }
    
    // 设置超时
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 10000); // 10秒超时
    options.signal = controller.signal;
    
    // 测试日志 - 记录完整请求信息
    console.log('发送Fetch请求:', {
      url: apiUrl,
      method: method,
      headers: headers,
      body: data ? JSON.stringify(data) : undefined
    });
    
    const response = await fetch(apiUrl, options);
    clearTimeout(timeoutId);
    
    if (!response.ok) {
      // 兼容模式：如果API不可用，使用模拟数据
      if (url === '/login' && method === 'POST') {
        console.log('API不可用，使用模拟数据');
        return await mockLoginResponse(data);
      }
      
      return {
        code: response.status,
        message: `HTTP错误: ${response.status} ${response.statusText}`,
        data: null
      };
    }
    
    return await response.json();
  } catch (error) {
    console.error('API请求错误:', error);
    
    // 兼容模式：如果API不可用，使用模拟数据
    if (url === '/login' && method === 'POST') {
      console.log('API不可用，使用模拟数据');
      return await mockLoginResponse(data);
    }
    
    return {
      code: 500,
      message: (error as Error).message || '网络错误，请稍后重试',
      data: null
    };
  }
};

// 模拟登录响应
const mockLoginResponse = async (data: any) => {
  return new Promise(resolve => {
    setTimeout(() => {
      if (data && data.loginName && data.password) {
        resolve({
          code: 200,
          message: '登录成功',
          data: {
            token: 'mock_token_' + Math.random().toString(36).substring(2),
            userId: new Date().getTime()
          }
        });
      } else {
        resolve({
          code: 401,
          message: '用户名或密码错误',
          data: null
        });
      }
    }, 800);
  });
};

// 登录API
const loginApi = async (loginName: string, password: string, userMail = '') => {
  console.log('调用登录API，使用相对路径');
  return request('/login', 'POST', { loginName, password, userMail }, false);
};

// 注册API
const registerApi = async (loginName: string, password: string, userMail = '') => {
  console.log('调用注册API，使用相对路径');
  return request('/register', 'POST', { loginName, password, userMail }, false);
};

// 获取用户信息API
const getUserInfoApi = async () => {
  try {
    console.log('开始获取用户信息...');
    // 正确的API路径应包含/ums前缀
    const response = await fetch('/ums/api/user/users/info', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('auth_token')}`
      }
    });
    
    if (!response.ok) {
      throw new Error(`获取用户信息失败: ${response.status}`);
    }
    
    const data = await response.json();
    console.log('用户信息API返回数据:', data);
    
    // 直接返回API的原始响应，不要再包装
    return data;
  } catch (error) {
    console.error('获取用户信息API调用失败:', error);
    return {
      code: 500,
      message: error instanceof Error ? error.message : '未知错误',
      data: null
    };
  }
};

// 登出API
const logoutApi = async () => {
  // 确保使用相对路径，避免直接使用完整URL
  return request('/logout', 'POST', null, false);
};

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref('')
  const userInfo = ref({
    id: '',
    username: '',
    avatar: '',
    roles: [] as string[],
    permissions: [] as string[]
  })
  
  // Getters
  const isLoggedIn = computed(() => !!token.value || !!localStorage.getItem('auth_token'))
  
  // Actions
  async function loginAction(username: string, password: string) {
    try {
      // 调用tech_prompt的登录接口
      const res = await loginApi(username, password);
      
      if (res.code === 200 && res.data) {
        // 保存token到状态和localStorage
        token.value = res.data.token;
        localStorage.setItem('auth_token', res.data.token);
        localStorage.setItem('user_id', res.data.userId);
        
        // 登录成功后立即获取用户信息，确保获取租户ID等重要信息
        await getUserInfoAction();
        
        ElMessage.success(res.message || '登录成功');
        return true;
      } else {
        ElMessage.error(res.message || '登录失败，请检查用户名和密码');
        return false;
      }
    } catch (error) {
      console.error('登录失败:', error);
      ElMessage.error('登录失败，请检查用户名和密码');
      return false;
    }
  }
  
  // 添加注册Action
  async function registerAction(loginName: string, password: string, userMail = '') {
    try {
      console.log('开始注册用户:', { loginName, userMail });
      
      // 调用注册接口
      const res = await registerApi(loginName, password, userMail);
      
      if (res.code === 200 && res.data) {
        ElMessage.success(res.message || '注册成功');
        return true;
      } else {
        ElMessage.error(res.message || '注册失败，请稍后重试');
        return false;
      }
    } catch (error) {
      console.error('注册失败:', error);
      ElMessage.error('注册失败，请检查网络连接');
      return false;
    }
  }
  
  async function getUserInfoAction() {
    try {
      // 调用用户信息API
      const res = await getUserInfoApi();
      console.log('获取用户信息响应:', res);
      
      if (res.code === 200 && res.data) {
        const responseData = res.data;
        
        // 直接从数据结构中获取用户信息
        const user = responseData.user;
        const roles = responseData.roles || [];
        
        console.log('用户数据:', user);
        console.log('角色数据:', roles);
        
        // 检查user对象是否存在
        if (!user) {
          console.error('用户数据为空:', responseData);
          throw new Error('用户数据为空');
        }
        
        // 直接获取所需字段
        const userId = user.id;
        const userName = user.userName;
        const loginName = user.loginName;
        const tenantId = user.tenantId;
        const orgId = user.orgId;
        const userMail = user.userMail;
        
        console.log('提取的用户信息:', {
          userId,
          userName,
          loginName,
          tenantId,
          orgId,
          userMail
        });
        
        // 保存到localStorage
        if (userId) localStorage.setItem('user_id', String(userId));
        if (tenantId) localStorage.setItem('tenant_id', String(tenantId));
        if (orgId) localStorage.setItem('org_id', String(orgId));
        
        // 更新用户信息状态
        userInfo.value = {
          id: String(userId || ''),
          username: userName || loginName || '',
          avatar: user.avatar || 'https://randomuser.me/api/portraits/men/1.jpg',
          roles: roles.map((role: any) => role.rolesName || role.rolesCode || '') || ['user'],
          permissions: user.permissions || []
        };
        
        console.log('最终用户信息状态:', userInfo.value);
        
        console.log('获取用户信息成功', {
          userId: userId,
          tenantId: tenantId,
          orgId: orgId,
          userName: userName
        });
      } else {
        // 如果API不可用，使用模拟数据
        // 为模拟数据设置默认的租户ID和组织ID
        const mockTenantId = '1001';
        const mockOrgId = '1'; 
        
        // 存储到localStorage
        localStorage.setItem('tenant_id', mockTenantId);
        localStorage.setItem('org_id', mockOrgId);
        localStorage.setItem('user_id', '1');
        
        userInfo.value = {
          id: '1',
          username: 'admin',
          avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
          roles: ['admin'],
          permissions: ['dashboard:view', 'organization:view', 'organization:edit', 'template:view', 'template:edit']
        };
        console.warn('无法获取用户信息，使用默认数据，租户ID:', mockTenantId, '组织ID:', mockOrgId);
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      ElMessage.error('获取用户信息失败');
      
      // 使用模拟数据，并设置默认租户ID和组织ID
      const mockTenantId = '1001';
      const mockOrgId = '1';
      
      // 存储到localStorage
      localStorage.setItem('tenant_id', mockTenantId);
      localStorage.setItem('org_id', mockOrgId);
      localStorage.setItem('user_id', '1');
      
      userInfo.value = {
        id: '1',
        username: 'admin',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
        roles: ['admin'],
        permissions: ['dashboard:view', 'organization:view', 'organization:edit', 'template:view', 'template:edit']
      };
      console.warn('使用模拟用户数据，租户ID:', mockTenantId, '组织ID:', mockOrgId);
    }
  }
  
  function logoutAction() {
    try {
      // 调用登出接口
      logoutApi();
      resetState();
      // 清除租户相关信息
      clearTenantInfo();
      ElMessage.success('退出登录成功');
    } catch (error) {
      console.error('退出登录失败:', error);
      ElMessage.error('退出登录失败');
    } finally {
      // 清除认证令牌
      localStorage.removeItem('auth_token');
      // 其他ID信息在resetState中清除
    }
  }
  
  function resetState() {
    token.value = '';
    userInfo.value = {
      id: '',
      username: '',
      avatar: '',
      roles: [],
      permissions: []
    };
    
    // 清除所有保存的ID信息
    localStorage.removeItem('user_id');
    localStorage.removeItem('tenant_id');
    localStorage.removeItem('org_id');
  }
  
  // 判断是否有权限
  function hasPermission(permission: string): boolean {
    // 开发阶段默认拥有所有权限，或者可以设置权限白名单
    const devPermissions = ['提示词工程', '模板工厂'];
    if (devPermissions.includes(permission)) return true;
    
    return userInfo.value.permissions.includes(permission);
  }
  
  return {
    token,
    userInfo,
    isLoggedIn,
    loginAction,
    registerAction,
    getUserInfoAction,
    logoutAction,
    hasPermission
  }
}, {
  persist: {
    paths: ['token']
  }
})