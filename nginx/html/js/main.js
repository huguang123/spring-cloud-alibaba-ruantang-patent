import { createApp } from 'vue';
import apiService from './api-service';

// 创建Vue应用
const app = createApp({
  // 应用配置
});

// 全局API服务
app.config.globalProperties.$api = apiService;

// 挂载应用
app.mount('#app');

// 在全局window对象上挂载API服务，兼容旧的JS代码
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
  },
  
  // 其他API方法
  // ...
}; 