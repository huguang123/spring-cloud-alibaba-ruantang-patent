import axios from 'axios';
import apiConfig from './api-config';

// 创建axios实例
const http = axios.create({
  baseURL: apiConfig.baseURL,
  timeout: apiConfig.timeout,
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

// 导出封装的方法
export default {
  // GET请求
  get(url, params = {}) {
    return http.get(url, { params });
  },
  
  // POST请求
  post(url, data = {}) {
    return http.post(url, data);
  },
  
  // PUT请求
  put(url, data = {}) {
    return http.put(url, data);
  },
  
  // DELETE请求
  delete(url, params = {}) {
    return http.delete(url, { params });
  },
  
  // 使用服务名和路径构建请求
  request(method, service, path, data = {}) {
    const url = apiConfig.getApiUrl(service, path);
    return http[method.toLowerCase()](url, data);
  }
}; 