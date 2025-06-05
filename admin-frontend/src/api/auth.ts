import request from '@/utils/request'

// 登录参数接口
export interface LoginParams {
  loginName: string
  password: string
  userMail?: string
}

// 登录响应接口
interface LoginResponse {
  token: string
}

// 用户信息接口
interface UserInfo {
  id: string
  username: string
  avatar: string
  roles: string[]
  permissions: string[]
}

// 用户注册接口
export interface RegisterParams {
  loginName: string
  password: string
  userMail?: string
}

// 注册响应类型
export interface RegisterResponse {
  id: number
  loginName: string
  password: string
  tenantId: number
  orgId: number
  level: number
  userName: string
  userPhone: string
  userMail: string
  gender: number
  weixin: string
  qq: string
  createBy: number
  createTime: number
  updateTime: number
}

// 用户个人资料接口
export interface UserProfile {
  id: number
  loginName: string
  tenantId: number
  tenantName?: string
  orgId: number
  orgName?: string
  level: number
  userName: string
  userPhone: string
  userMail: string
  gender: number
  weixin: string
  qq: string
  createBy: number
  createTime: number
  updateTime: number
}

// 更新个人资料参数
export interface UpdateProfileParams {
  id: number
  userName?: string
  level?: number
  gender?: number
  weixin?: string
  qq?: string
}

// 修改密码参数
export interface ChangePasswordParams {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

// 账户注销参数
export interface DeactivateAccountParams {
  currentPassword: string
  reason: string
  confirmText: string
}

// 用户注册
export const registerUser = (params: RegisterParams) => {
  return request({
    url: '/ums/auth/register',
    method: 'POST',
    data: params
  })
}

// 用户登录
export const loginUser = (params: LoginParams) => {
  return request({
    url: '/ums/auth/login',
    method: 'POST',
    data: params
  })
}

// 获取当前用户的个人资料
export const getUserProfile = () => {
  return request({
    url: '/ums/api/user/users/profile',
    method: 'GET'
  })
}

// 更新当前用户的个人资料
export const updateUserProfile = (params: UpdateProfileParams) => {
  return request({
    url: '/ums/api/user/users/profile',
    method: 'PUT',
    data: params
  })
}

// 修改当前用户密码
export const changePassword = (params: ChangePasswordParams) => {
  return request({
    url: '/ums/api/user/users/update-password',
    method: 'PUT',
    data: params
  })
}

// 账户注销
export const deactivateAccount = (params: DeactivateAccountParams) => {
  return request({
    url: '/ums/api/user/users/deactivate-account',
    method: 'POST',
    data: params
  })
}

// 原有的登录接口（保留兼容性）
export function login(username: string, password: string): Promise<LoginResponse> {
  // 模拟登录接口
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        token: 'mock_token_' + username
      })
    }, 1000)
  })
  
  // 实际代码
  // return request({
  //   url: '/ums/auth/login',
  //   method: 'post',
  //   data: { username, password }
  // })
}

// 获取用户信息
export function getUserInfo(): Promise<UserInfo> {
  // 实际代码
  return request({
    url: '/ums/api/user/users/info',
    method: 'get'
  })
}

// 登出接口
export function logout(): Promise<void> {
  // 模拟登出
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve()
    }, 500)
  })
  
  // 实际代码
  // return request({
  //   url: '/ums/auth/logout',
  //   method: 'post'
  // })
} 