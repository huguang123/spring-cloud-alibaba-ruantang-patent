/**
 * 技术领域API接口模块
 */

// API 接口封装
const BASE_URL = '/ums/auth';

// 是否使用模拟数据（当真实API不可用时）
const USE_MOCK = true;

// 请求封装
const request = async (url, method = 'GET', data = null) => {
  try {
    console.log(`准备发送请求: ${method} ${BASE_URL}${url}`);
    
    // 如果启用模拟数据且是登录或注册请求
    if (USE_MOCK && (url === '/login' || url === '/register' || url === '/logout')) {
      console.log('使用模拟数据');
      return await mockApiResponse(url, data);
    }
    
    const headers = {
      'Content-Type': 'application/json'
    };
    
    // 添加认证令牌（如果存在）
    const token = localStorage.getItem('auth_token');
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    const options = {
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
    
    try {
      console.log('发送请求:', `${BASE_URL}${url}`, options);
      const response = await fetch(`${BASE_URL}${url}`, options);
      clearTimeout(timeoutId);
      
      console.log('收到响应:', response.status);
      
      // 检查HTTP状态码
      if (!response.ok) {
        return {
          code: response.status,
          message: `HTTP错误: ${response.status} ${response.statusText}`,
          data: null
        };
      }
      
      const result = await response.json();
      console.log('响应数据:', result);
      return result;
    } catch (error) {
      if (error.name === 'AbortError') {
        console.error('请求超时:', url);
        
        if (USE_MOCK) {
          console.log('请求超时，使用模拟数据');
          return await mockApiResponse(url, data);
        }
        
        return {
          code: 408,
          message: '请求超时，请稍后重试',
          data: null
        };
      }
      
      console.error('API请求错误:', error);
      
      if (USE_MOCK) {
        console.log('请求失败，使用模拟数据');
        return await mockApiResponse(url, data);
      }
      
      return {
        code: 500,
        message: error.message || '网络错误，请稍后重试',
        data: null
      };
    }
  } catch (error) {
    console.error('API请求失败:', error);
    
    if (USE_MOCK) {
      console.log('请求异常，使用模拟数据');
      return await mockApiResponse(url, data);
    }
    
    return {
      code: 500,
      message: '系统错误，请联系管理员',
      data: null
    };
  }
};

/**
 * 模拟API响应
 * @param {string} url 请求URL
 * @param {object} data 请求数据
 * @returns {Promise<object>} 模拟响应
 */
const mockApiResponse = async (url, data) => {
  return new Promise(resolve => {
    // 模拟网络延迟
    setTimeout(() => {
      if (url === '/login') {
        if (data && data.loginName && data.password) {
          // 模拟成功登录
          resolve({
            code: 200,
            message: '登录成功',
            data: {
              token: 'mock_token_' + Math.random().toString(36).substring(2),
              userId: new Date().getTime()
            }
          });
        } else {
          // 模拟登录失败
          resolve({
            code: 401,
            message: '用户名或密码错误',
            data: null
          });
        }
      } else if (url === '/register') {
        // 模拟注册响应
        resolve({
          code: 200,
          message: '注册成功',
          data: {
            id: new Date().getTime(),
            loginName: data.loginName,
            level: 1,
            userMail: data.userMail,
            createTime: new Date().getTime(),
            updateTime: new Date().getTime()
          }
        });
      } else if (url === '/logout') {
        // 模拟登出响应
        resolve({
          code: 200,
          message: '登出成功',
          data: true
        });
      } else {
        // 默认响应
        resolve({
          code: 200,
          message: '操作成功',
          data: null
        });
      }
    }, 500); // 添加500ms延迟模拟网络请求
  });
};

// 登录
const login = async (loginName, password, userMail = '') => {
  if (!loginName || !password) {
    return {
      code: 400,
      message: '用户名和密码不能为空',
      data: null
    };
  }
  
  return request('/login', 'POST', { loginName, password, userMail });
};

// 注册
const register = async (loginName, password, userMail = '') => {
  if (!loginName || !password) {
    return {
      code: 400,
      message: '用户名和密码不能为空',
      data: null
    };
  }
  
  return request('/register', 'POST', { loginName, password, userMail });
};

// 登出
const logout = async () => {
  return request('/logout', 'POST');
};

// API方法集合
const api = {
  login,
  register,
  logout,
  request
};

// 检查API对象是否已存在
if (typeof window !== 'undefined') {
  // 导出API方法
  window.api = api;
}

// 技术领域相关API
const techDomainApi = {
  /**
   * 获取技术领域树
   * @returns {Promise<Object>} 响应结果
   */
  async getDomainTree() {
    try {
      // 这里使用模拟数据，实际项目中应通过axios等工具发起请求
      // const response = await axios.get('/prom/api/tech-domains');
      // return response.data;
      
      // 模拟API调用
      return await mockFetchDomains();
    } catch (error) {
      console.error('获取技术领域树失败:', error);
      throw error;
    }
  },
  
  /**
   * 创建技术领域
   * @param {Object} domain 领域数据
   * @returns {Promise<Object>} 响应结果
   */
  async createDomain(domain) {
    try {
      // 这里使用模拟数据，实际项目中应通过axios等工具发起请求
      // const response = await axios.post('/prom/api/tech-domains', domain);
      // return response.data;
      
      // 模拟API调用
      return await new Promise(resolve => {
        setTimeout(() => {
          const newDomain = {
            ...domain,
            id: Date.now(), // 模拟ID生成
            level: domain.parent_id ? getParentLevel(domain.parent_id) + 1 : 1
          };
          resolve({
            code: 200,
            message: '技术领域创建成功',
            data: newDomain
          });
        }, 500);
      });
    } catch (error) {
      console.error('创建技术领域失败:', error);
      throw error;
    }
  },
  
  /**
   * 更新技术领域
   * @param {Object} domain 领域数据
   * @returns {Promise<Object>} 响应结果
   */
  async updateDomain(domain) {
    try {
      // 这里使用模拟数据，实际项目中应通过axios等工具发起请求
      // const response = await axios.put(`/prom/api/tech-domains/${domain.id}`, domain);
      // return response.data;
      
      // 模拟API调用
      return await new Promise(resolve => {
        setTimeout(() => {
          resolve({
            code: 200,
            message: '技术领域更新成功',
            data: domain
          });
        }, 500);
      });
    } catch (error) {
      console.error('更新技术领域失败:', error);
      throw error;
    }
  },
  
  /**
   * 删除技术领域
   * @param {String|Number} id 领域ID
   * @returns {Promise<Object>} 响应结果
   */
  async deleteDomain(id) {
    try {
      // 这里使用模拟数据，实际项目中应通过axios等工具发起请求
      // const response = await axios.delete(`/prom/api/tech-domains/${id}`);
      // return response.data;
      
      // 模拟API调用
      return await new Promise(resolve => {
        setTimeout(() => {
          resolve({
            code: 200,
            message: '技术领域删除成功'
          });
        }, 500);
      });
    } catch (error) {
      console.error('删除技术领域失败:', error);
      throw error;
    }
  },
  
  // 获取技术领域详情
  async getDomainDetail(id) {
    try {
      const response = await api.request(`/${id}`);
      return response.data;
    } catch (error) {
      console.error('获取技术领域详情失败:', error);
      throw error;
    }
  },
  
  // 获取子技术领域
  async getChildDomains(parentId = null) {
    try {
      const url = parentId ? `/children?parentId=${parentId}` : '/children';
      const response = await api.request(url);
      return response.data;
    } catch (error) {
      console.error('获取子技术领域失败:', error);
      throw error;
    }
  },
  
  // 根据名称搜索技术领域
  async searchDomains(domainName) {
    try {
      const response = await api.request(`/search-with-parents?domainName=${encodeURIComponent(domainName)}`);
      return response.data;
    } catch (error) {
      console.error('搜索技术领域失败:', error);
      throw error;
    }
  }
};

/**
 * 模拟获取领域数据
 * @returns {Promise<Object>} 模拟响应
 */
function mockFetchDomains() {
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockData = [
        {
          id: 1,
          domain_name: '机械技术领域',
          description: '包括机械设计、制造与自动化相关技术',
          parent_id: null,
          level: 1
        },
        {
          id: 2,
          domain_name: '电子信息技术领域',
          description: '包括计算机科学与技术、电子技术等',
          parent_id: null,
          level: 1
        },
        {
          id: 3,
          domain_name: '机械设计',
          description: '机械产品的设计理论与方法',
          parent_id: 1,
          level: 2
        },
        {
          id: 4,
          domain_name: '机械制造',
          description: '机械产品的生产工艺与方法',
          parent_id: 1,
          level: 2
        },
        {
          id: 5,
          domain_name: '计算机科学',
          description: '计算机基础理论与应用技术',
          parent_id: 2,
          level: 2
        },
        {
          id: 6,
          domain_name: '精密制造',
          description: '高精度零件加工技术',
          parent_id: 4,
          level: 3
        },
        {
          id: 7,
          domain_name: '微加工技术',
          description: '微米级精度的加工方法',
          parent_id: 6,
          level: 4
        }
      ];
      resolve({ code: 200, data: mockData });
    }, 600);
  });
}

/**
 * 辅助函数：获取父级领域的层级
 * @param {Number} parentId 父级ID
 * @returns {Number} 父级层级
 */
function getParentLevel(parentId) {
  // 在实际项目中，这个函数需要访问已有的领域数据
  // 这里简化处理，假设父级层级是1
  return 1;
}

// 导出认证API模块（主要导出）
export default api;

// 次要导出（技术领域API）
export { techDomainApi }; 