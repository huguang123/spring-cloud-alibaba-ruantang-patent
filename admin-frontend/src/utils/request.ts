import axios, { AxiosRequestConfig, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'

// 定义通用API响应类型
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 避免精度丢失的处理函数
function preserveBigIntPrecision(obj: any): any {
  if (obj === null || obj === undefined) {
    return obj;
  }
  
  if (typeof obj === 'object') {
    if (Array.isArray(obj)) {
      return obj.map(item => preserveBigIntPrecision(item));
    } else {
      const result: any = {};
      for (const key in obj) {
        // 特殊处理可能是大整数的ID字段
        if (['id', 'tenantId', 'orgId', 'userId', 'parentId'].includes(key) && 
            typeof obj[key] === 'number' && obj[key] > 1000000) {
          // 转为字符串避免精度丢失
          result[key] = String(obj[key]);
        } else if (typeof obj[key] === 'object') {
          result[key] = preserveBigIntPrecision(obj[key]);
        } else {
          result[key] = obj[key];
        }
      }
      return result;
    }
  }
  
  return obj;
}

// 创建axios实例
const service = axios.create({
  baseURL: '',
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 获取需要特殊处理的大整数ID字段
    const bigIntFields = config.headers?.['X-BigInt-Fields'] ? 
      String(config.headers['X-BigInt-Fields']).split(',') : 
      ['id', 'tenantId', 'orgId', 'userId', 'parentId'];
    
    // 是否跳过ID处理
    const preserveIDs = config.headers?.['X-Preserve-IDs'] === 'true';
    
    // 添加认证令牌
    const token = localStorage.getItem('auth_token')
    if (token) {
      config.headers.set('Authorization', `Bearer ${token}`);
    }
    
    // 移除特殊头信息，避免发送到服务器
    if (config.headers.has('X-BigInt-Fields')) {
      config.headers.delete('X-BigInt-Fields');
    }
    
    // 特殊处理大整数ID，避免精度丢失
    if (!preserveIDs && config.data && typeof config.data === 'object') {
      console.log('请求数据处理前:', JSON.stringify(config.data, null, 2));
      
      // 遍历处理data中可能的大整数
      for (const key in config.data) {
        if (bigIntFields.includes(key)) {
          // 确保ID字段作为字符串处理
          if (config.data[key] !== null && config.data[key] !== undefined) {
            console.log(`处理请求字段 ${key}:`, config.data[key], ' -> ', String(config.data[key]));
            config.data[key] = String(config.data[key]);
          }
        }
      }
      
      console.log('请求数据处理后:', JSON.stringify(config.data, null, 2));
    }
    
    // 如果参数中有可能的大整数ID，确保它们作为字符串处理
    if (!preserveIDs && config.params && typeof config.params === 'object') {
      console.log('请求参数处理前:', JSON.stringify(config.params, null, 2));
      
      for (const key in config.params) {
        if (bigIntFields.includes(key)) {
          if (config.params[key] !== null && config.params[key] !== undefined) {
            console.log(`处理请求参数 ${key}:`, config.params[key], ' -> ', String(config.params[key]));
            config.params[key] = String(config.params[key]);
          }
        }
      }
      
      console.log('请求参数处理后:', JSON.stringify(config.params, null, 2));
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    
    // 处理响应中的大整数ID，防止精度丢失
    if (res) {
      try {
        // 处理data字段或整个响应数据
        if (res.data) {
          res.data = preserveBigIntPrecision(res.data);
        } else if (typeof res === 'object') {
          // 如果没有data字段，但是整个响应是对象，直接处理
          preserveBigIntPrecision(res);
        }
        // 确保records字段也被处理(如果存在)
        if (res.records && Array.isArray(res.records)) {
          res.records = preserveBigIntPrecision(res.records);
        }
        if (res.data && res.data.records && Array.isArray(res.data.records)) {
          res.data.records = preserveBigIntPrecision(res.data.records);
        }
      } catch (e) {
        console.error('处理响应数据中的大整数ID时出错:', e);
      }
    }
    
    // 直接返回后端的标准响应格式，不再提取data字段
    // 标准格式：{ code: 200, message: 'success', data: any }
    return res as unknown as AxiosResponse;
  },
  (error: AxiosError) => {
    console.error('Response error:', error)
    
    // 尝试从后端响应中提取错误信息
    let errorMessage = '请求失败';
    let errorCode = 500;
    
    if (error.response) {
      errorCode = error.response.status;
      
      // 优先使用后端返回的错误信息
      if (error.response.data && typeof error.response.data === 'object') {
        const responseData = error.response.data as any;
        if (responseData.message) {
          errorMessage = responseData.message;
        } else if (responseData.msg) {
          errorMessage = responseData.msg;
        } else if (responseData.error) {
          errorMessage = responseData.error;
        } else {
          errorMessage = error.message || '请求失败';
        }
      } else if (typeof error.response.data === 'string') {
        errorMessage = error.response.data;
      } else {
        errorMessage = error.message || '请求失败';
      }
      
      if (error.response.status === 401) {
        handleUnauthorized()
      } else {
        ElMessage({
          message: errorMessage,
          type: 'error',
          duration: 5 * 1000
        })
      }
    } else {
      errorMessage = '网络异常，请检查您的网络连接';
      ElMessage({
        message: errorMessage,
        type: 'error',
        duration: 5 * 1000
      })
    }
    
    // 保留原始错误对象，让业务层可以访问完整的错误信息
    return Promise.reject(error);
  }
)

// 处理未授权
function handleUnauthorized() {
  const userStore = useUserStore()
  
  ElMessageBox.confirm(
    '登录状态已过期，您可以继续留在该页面，或者重新登录',
    '系统提示',
    {
      confirmButtonText: '重新登录',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    userStore.logoutAction()
    window.location.href = '/login'
  })
}

export default service

// 导出API响应类型定义，便于在其他文件中使用
export type { ApiResponse }; 