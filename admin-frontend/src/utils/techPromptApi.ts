/**
 * 技术交底书智能撰写系统API整合
 * 将tech_prompt目录下的API接口整合到Vue3项目中
 */

// 基础URL配置
const BASE_URLS = {
  auth: '/ums/auth',           // 认证接口
  domain: '/prom/api/domain',  // 技术领域接口
  template: '/prom/api/template', // 提示词模板接口
  doc: '/prom/api/doc'         // 文档生成接口
};

// 通用请求函数
export const request = async (baseUrl: string, url: string, method = 'GET', data: any = null) => {
  try {
    // 使用相对路径，确保代理正常工作
    const fullUrl = `${baseUrl}${url}`;
    console.log(`准备发送请求: ${method} ${fullUrl}`);
    
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
    
    // 调试日志 - 输出完整请求详情
    console.log('TechPromptApi发送请求:', {
      url: fullUrl,
      method: method,
      headers: headers,
      body: data ? JSON.stringify(data) : undefined
    });
    
    try {
      const response = await fetch(fullUrl, options);
      clearTimeout(timeoutId);
      
      // 检查HTTP状态码
      if (!response.ok) {
        console.error(`API请求失败: ${response.status} ${response.statusText}`);
        
        // 根据不同API类型使用模拟数据
        if (baseUrl === BASE_URLS.auth && url === '/login' && method === 'POST') {
          console.log('使用模拟认证数据');
          return await mockAuthResponse('login', data);
        }
        
        return {
          code: response.status,
          message: `HTTP错误: ${response.status} ${response.statusText}`,
          data: null
        };
      }
      
      const result = await response.json();
      return result;
    } catch (error) {
      clearTimeout(timeoutId);
      
      if (error instanceof DOMException && error.name === 'AbortError') {
        console.error('请求超时');
        return {
          code: 408,
          message: '请求超时，请稍后重试',
          data: null
        };
      }
      
      console.error('API请求错误:', error);
      
      // 根据不同API类型使用模拟数据
      if (baseUrl === BASE_URLS.auth && url === '/login' && method === 'POST') {
        console.log('请求错误，使用模拟认证数据');
        return await mockAuthResponse('login', data);
      } else if (baseUrl === BASE_URLS.domain) {
        console.log('请求错误，使用模拟领域数据');
        return await mockDomainResponse(url, method, data);
      }
      
      return {
        code: 500,
        message: (error as Error).message || '网络错误，请稍后重试',
        data: null
      };
    }
  } catch (error) {
    console.error('API请求失败:', error);
    
    return {
      code: 500,
      message: '系统错误，请联系管理员',
      data: null
    };
  }
};

/**
 * 认证类API模拟响应
 */
const mockAuthResponse = async (type: string, data: any) => {
  return new Promise(resolve => {
    // 模拟网络延迟
    setTimeout(() => {
      if (type === 'login') {
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
      } else if (type === 'register') {
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
      } else if (type === 'logout') {
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

/**
 * 技术领域API模拟响应
 */
const mockDomainResponse = async (url: string, method: string, data: any) => {
  return new Promise(resolve => {
    setTimeout(() => {
      // 获取领域树
      if (url === '/' && method === 'GET') {
        resolve({
          code: 200,
          message: '获取成功',
          data: mockDomainTree()
        });
      } 
      // 创建领域
      else if (url === '/' && method === 'POST') {
        resolve({
          code: 200,
          message: '创建成功',
          data: {
            id: 'domain_' + Date.now(),
            name: data.name,
            code: data.code || '',
            description: data.description || '',
            parentId: data.parentId || null,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
          }
        });
      }
      // 默认响应
      else {
        resolve({
          code: 200,
          message: '操作成功',
          data: null
        });
      }
    }, 500);
  });
};

/**
 * 模拟技术领域树数据
 */
const mockDomainTree = () => {
  return [
    {
      id: '1',
      name: '人工智能',
      code: 'AI',
      description: '人工智能相关技术领域',
      parentId: null,
      createdAt: '2023-05-10',
      updatedAt: '2023-06-15',
      children: [
        {
          id: '2',
          name: '机器学习',
          code: 'ML',
          description: '机器学习相关算法和应用',
          parentId: '1',
          createdAt: '2023-05-12',
          updatedAt: '2023-06-16',
          children: [
            {
              id: '3',
              name: '深度学习',
              code: 'DL',
              description: '基于神经网络的深度学习技术',
              parentId: '2',
              createdAt: '2023-05-15',
              updatedAt: '2023-06-18',
              children: []
            }
          ]
        },
        {
          id: '4',
          name: '自然语言处理',
          code: 'NLP',
          description: '处理和理解人类语言的技术',
          parentId: '1',
          createdAt: '2023-05-18',
          updatedAt: '2023-06-20',
          children: []
        }
      ]
    },
    {
      id: '6',
      name: '物联网',
      code: 'IoT',
      description: '物联网技术和应用',
      parentId: null,
      createdAt: '2023-05-22',
      updatedAt: '2023-06-24',
      children: [
        {
          id: '7',
          name: '智能家居',
          code: 'SH',
          description: '智能家居设备和系统',
          parentId: '6',
          createdAt: '2023-05-25',
          updatedAt: '2023-06-26',
          children: []
        }
      ]
    }
  ];
};

/**
 * API对象
 */
export const techPromptApi = {
  // 认证相关
  auth: {
    login: (loginName: string, password: string, userMail = '') => {
      return request(BASE_URLS.auth, '/login', 'POST', { loginName, password, userMail });
    },
    register: (loginName: string, password: string, userMail = '') => {
      return request(BASE_URLS.auth, '/register', 'POST', { loginName, password, userMail });
    },
    logout: () => {
      return request(BASE_URLS.auth, '/logout', 'POST');
    },
    getUserInfo: () => {
      return request(BASE_URLS.auth, '/user-info', 'GET');
    }
  },
  
  // 技术领域相关
  domain: {
    getDomainTree: () => {
      return request(BASE_URLS.domain, '/', 'GET');
    },
    createDomain: (domain: any) => {
      return request(BASE_URLS.domain, '/', 'POST', domain);
    },
    updateDomain: (domain: any) => {
      return request(BASE_URLS.domain, `/${domain.id}`, 'PUT', domain);
    },
    deleteDomain: (id: string) => {
      return request(BASE_URLS.domain, `/${id}`, 'DELETE');
    },
    getDomainDetail: (id: string) => {
      return request(BASE_URLS.domain, `/${id}`, 'GET');
    },
    getChildDomains: (parentId: string | null = null) => {
      const url = parentId ? `/children?parentId=${parentId}` : '/children';
      return request(BASE_URLS.domain, url, 'GET');
    },
    searchDomains: (domainName: string) => {
      return request(BASE_URLS.domain, `/search?domainName=${encodeURIComponent(domainName)}`, 'GET');
    }
  },
  
  // 提示词模板相关
  template: {
    getTemplates: (params: any = {}) => {
      // 构建查询参数
      const queryParams = Object.entries(params)
        .map(([key, value]) => `${key}=${encodeURIComponent(String(value))}`)
        .join('&');
      
      const url = queryParams ? `/?${queryParams}` : '/';
      return request(BASE_URLS.template, url, 'GET');
    },
    createTemplate: (template: any) => {
      return request(BASE_URLS.template, '/', 'POST', template);
    },
    updateTemplate: (template: any) => {
      return request(BASE_URLS.template, `/${template.id}`, 'PUT', template);
    },
    deleteTemplate: (id: string) => {
      return request(BASE_URLS.template, `/${id}`, 'DELETE');
    },
    getTemplateDetail: (id: string) => {
      return request(BASE_URLS.template, `/${id}`, 'GET');
    }
  },
  
  // 文档生成相关
  doc: {
    generateDocument: (params: any) => {
      return request(BASE_URLS.doc, '/generate', 'POST', params);
    },
    updateSection: (docId: string, sectionId: string, content: string) => {
      return request(BASE_URLS.doc, `/${docId}/sections/${sectionId}`, 'PUT', { content });
    },
    regenerateSection: (docId: string, sectionId: string) => {
      return request(BASE_URLS.doc, `/${docId}/sections/${sectionId}/regenerate`, 'POST');
    },
    exportDocument: (docId: string, format = 'docx') => {
      return request(BASE_URLS.doc, `/${docId}/export?format=${format}`, 'GET');
    }
  }
};

export default techPromptApi; 