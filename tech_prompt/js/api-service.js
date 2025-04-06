import http from './http';

// 用户服务API
const userApi = {
  // 登录
  login(loginName, password, userMail = '') {
    return http.request('post', 'user', 'auth/login', { 
      loginName, 
      password,
      userMail
    });
  },
  
  // 注册
  register(userData) {
    return http.request('post', 'user', 'register', userData);
  },
  
  // 获取用户信息
  getUserInfo() {
    return http.request('get', 'user', 'userInfo');
  }
};

// 技术提示服务API
const promptApi = {
  // 获取技术领域树
  getDomainTree() {
    return http.request('get', 'prompt', 'api/tech-domains/tree');
  },
  
  // 获取技术领域详情
  getDomainDetail(id) {
    return http.request('get', 'prompt', `api/tech-domains/${id}`);
  },
  
  // 创建技术领域
  createDomain(data) {
    return http.request('post', 'prompt', 'api/tech-domains', data);
  },
  
  // 更新技术领域
  updateDomain(id, data) {
    return http.request('put', 'prompt', `api/tech-domains/${id}`, data);
  },
  
  // 删除技术领域
  deleteDomain(id) {
    return http.request('delete', 'prompt', `api/tech-domains/${id}`);
  }
};

// 导出API服务
export default {
  user: userApi,
  prompt: promptApi
}; 