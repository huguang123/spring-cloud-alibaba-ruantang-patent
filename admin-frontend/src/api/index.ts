// API 接口统一导出
import * as authApi from './auth'
import * as portraitApi from './portrait'

// 统一导出所有API接口
export default {
  // 认证API
  login: authApi.login,
  getUserInfo: authApi.getUserInfo,
  logout: authApi.logout,
  
  // 智能画像API
  ...portraitApi,
  
  // 扩展登录API以符合原有格式
  loginLegacy(loginName: string, password: string, userMail: string = '') {
    return authApi.login(loginName, password)
      .then(response => {
        return {
          code: 200,
          message: '登录成功',
          data: {
            token: response.token,
            userId: '1', // 模拟ID
            username: loginName
          }
        }
      })
      .catch(error => {
        return {
          code: 401,
          message: error.message || '登录失败',
          data: null
        }
      })
  }
} 