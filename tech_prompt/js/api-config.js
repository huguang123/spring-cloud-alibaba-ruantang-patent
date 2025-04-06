// API配置文件
const apiConfig = {
  // 根据环境选择不同的基础URL
  baseURL: process.env.NODE_ENV === 'production' 
    ? '/' // 生产环境使用相对路径，通过nginx代理
    : 'http://localhost:9000', // 开发环境直接访问网关
  
  // API路径前缀
  apiPrefix: {
    user: 'ums',
    prompt: 'prom',
    common: 'api'
  },
  
  // API超时设置
  timeout: 10000,
  
  // 是否启用模拟数据
  useMock: false,
  
  // 获取完整的API URL
  getApiUrl(service, path) {
    return `/${this.apiPrefix[service]}/${path}`;
  }
};

// 导出配置
export default apiConfig; 