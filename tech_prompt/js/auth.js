// 授权管理模块

/**
 * 保存认证令牌到本地存储
 * @param {string} token - 认证令牌
 * @param {number} userId - 用户ID
 */
const saveAuth = (token, userId) => {
  try {
    if (!token) {
      console.error('保存授权失败: 令牌为空');
      return false;
    }
    
    localStorage.setItem('auth_token', token);
    
    if (userId) {
      localStorage.setItem('user_id', userId);
    }
    
    return true;
  } catch (error) {
    console.error('保存授权信息失败:', error);
    return false;
  }
};

/**
 * 清除认证信息
 */
const clearAuth = () => {
  try {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_id');
    return true;
  } catch (error) {
    console.error('清除授权信息失败:', error);
    return false;
  }
};

/**
 * 检查用户是否已登录
 * @returns {boolean} 是否已登录
 */
const isAuthenticated = () => {
  try {
    return !!localStorage.getItem('auth_token');
  } catch (error) {
    console.error('检查授权信息失败:', error);
    return false;
  }
};

/**
 * 获取当前用户ID
 * @returns {string|null} 用户ID
 */
const getUserId = () => {
  try {
    return localStorage.getItem('user_id');
  } catch (error) {
    console.error('获取用户ID失败:', error);
    return null;
  }
};

/**
 * 获取认证令牌
 * @returns {string|null} 认证令牌
 */
const getToken = () => {
  try {
    return localStorage.getItem('auth_token');
  } catch (error) {
    console.error('获取令牌失败:', error);
    return null;
  }
};

// 检查window对象是否存在
if (typeof window !== 'undefined') {
  // 导出方法
  window.auth = {
    saveAuth,
    clearAuth,
    isAuthenticated,
    getUserId,
    getToken
  };
}

export default {
  saveAuth,
  clearAuth,
  isAuthenticated,
  getUserId,
  getToken
}; 