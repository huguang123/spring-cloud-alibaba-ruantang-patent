import { createApp } from 'vue';
import App from './App.vue';
import axios from 'axios';

// 创建Vue应用
const app = createApp(App);

// 设置全局API基础URL
const apiBaseUrl = '';  // 使用相对路径，不再使用localhost

// 创建axios实例
const http = axios.create({
  baseURL: apiBaseUrl,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
http.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('auth_token');
    if (token) {
      // 将token添加到请求头
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    // 请求错误处理
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
http.interceptors.response.use(
  response => {
    // 直接返回响应数据
    return response.data;
  },
  error => {
    // 响应错误处理
    if (error.response) {
      // 服务器返回错误状态码
      switch (error.response.status) {
        case 401:
          // 未授权，清除token并跳转到登录页
          localStorage.removeItem('auth_token');
          window.location.href = '/login';
          break;
        case 403:
          // 权限不足
          console.error('没有权限访问该资源');
          break;
        case 404:
          // 资源不存在
          console.error('请求的资源不存在');
          break;
        case 500:
          // 服务器错误
          console.error('服务器错误');
          break;
        default:
          console.error(`HTTP错误: ${error.response.status}`);
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      console.error('网络错误，无法连接服务器');
    } else {
      // 请求配置出错
      console.error('请求配置错误:', error.message);
    }
    return Promise.reject(error);
  }
);

// API服务
const apiService = {
  // 用户服务API
  user: {
    // 登录
    login(loginName, password, userMail = '') {
      return http.post('/ums/auth/login', { 
        loginName, 
        password,
        userMail
      });
    },
    
    // 注册
    register(userData) {
      return http.post('/ums/register', userData);
    },
    
    // 获取用户信息
    getUserInfo() {
      return http.get('/ums/userInfo');
    }
  },
  
  // 技术提示服务API
  prompt: {
    // 获取技术领域树
    getDomainTree() {
      return http.get('/prom/api/tech-domains/tree');
    },
    
    // 获取技术领域详情
    getDomainDetail(id) {
      return http.get(`/prom/api/tech-domains/${id}`);
    },
    
    // 创建技术领域
    createDomain(data) {
      return http.post('/prom/api/tech-domains', data);
    },
    
    // 更新技术领域
    updateDomain(id, data) {
      return http.put(`/prom/api/tech-domains/${id}`, data);
    },
    
    // 删除技术领域
    deleteDomain(id) {
      return http.delete(`/prom/api/tech-domains/${id}`);
    }
  }
};

// 全局API服务
app.config.globalProperties.$api = apiService;

// 兼容原有window.api接口
window.api = {
  // 登录接口
  login: async (loginName, password, userMail = '') => {
    try {
      const result = await apiService.user.login(loginName, password, userMail);
      if (result && result.code === 200 && result.data && result.data.token) {
        // 登录成功，保存token到localStorage
        localStorage.setItem('auth_token', result.data.token);
        localStorage.setItem('user_id', result.data.userId || '');
      }
      return result;
    } catch (error) {
      console.error('登录失败:', error);
      return {
        code: 500,
        message: `登录失败: ${error.message}`,
        data: null
      };
    }
  },
  
  // 注册接口
  register: async (formData) => {
    try {
      return await apiService.user.register(formData);
    } catch (error) {
      console.error('注册失败:', error);
      return {
        code: 500,
        message: `注册失败: ${error.message}`,
        data: null
      };
    }
  },
  
  // 获取用户信息
  getUserInfo: async () => {
    try {
      return await apiService.user.getUserInfo();
    } catch (error) {
      console.error('获取用户信息失败:', error);
      return {
        code: 500,
        message: `获取用户信息失败: ${error.message}`,
        data: null
      };
    }
  }
};

// 挂载应用
app.mount('#app'); 