<!--
  软唐知识产权后台管理系统 - 登录模块
  基于Vue3 + Element Plus + Pinia开发
  支持用户名密码登录，记住用户名，登录状态持久化
  界面采用响应式设计，支持移动端访问
-->

<template>
  <div class="auth-container flex items-center justify-center w-full h-screen">
    <div class="relative z-10 w-full max-w-md px-8 py-10 auth-card rounded-xl shadow-2xl animate-fadeIn">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-800 mb-2">
          <i class="fas fa-shield-alt text-blue-600 mr-2"></i>软唐知识产权后台管理系统
        </h1>
        <p class="text-gray-600">一站式知识产权管理与服务平台</p>
      </div>
      
      <!-- 登录表单 -->
      <form @submit.prevent="handleLogin" class="space-y-5">
        <div>
          <label class="block text-gray-700 mb-1 font-medium">用户名</label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
              <i class="fas fa-user"></i>
            </span>
            <input 
              type="text" 
              v-model="loginForm.loginName"
              class="w-full py-2.5 pl-10 pr-3 border rounded-lg focus:outline-none form-control"
              :class="{'input-error': errors.loginName}"
              placeholder="请输入用户名"
            >
          </div>
          <p v-if="errors.loginName" class="error-message text-red-500 text-sm mt-1">{{ errors.loginName }}</p>
        </div>
        
        <div>
          <label class="block text-gray-700 mb-1 font-medium">密码</label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
              <i class="fas fa-lock"></i>
            </span>
            <input 
              :type="showPassword ? 'text' : 'password'" 
              v-model="loginForm.password"
              class="w-full py-2.5 pl-10 pr-10 border rounded-lg focus:outline-none form-control"
              :class="{'input-error': errors.password}"
              placeholder="请输入密码"
            >
            <button 
              type="button"
              @click="showPassword = !showPassword"
              class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500"
            >
              <i :class="[showPassword ? 'fa-eye-slash' : 'fa-eye', 'fas']"></i>
            </button>
          </div>
          <p v-if="errors.password" class="error-message text-red-500 text-sm mt-1">{{ errors.password }}</p>
        </div>
        
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input type="checkbox" id="remember" v-model="rememberMe" class="h-4 w-4 text-blue-600">
            <label for="remember" class="ml-2 text-gray-600 text-sm">记住我</label>
          </div>
          <a href="#" class="text-sm text-blue-600 hover:underline">忘记密码?</a>
        </div>
        
        <button 
          type="submit" 
          :disabled="loading"
          class="w-full py-2.5 px-4 bg-gradient-primary text-white rounded-lg btn-hover-effect flex items-center justify-center"
        >
          <i v-if="loading" class="fas fa-spinner fa-spin mr-2"></i>
          <span>{{ loading ? '登录中...' : '登录' }}</span>
        </button>
        
        <div class="text-center mt-6">
          <p class="text-gray-600 text-sm">
            还没有账号? 
            <a href="javascript:void(0);" @click="goToRegister" class="text-blue-600 hover:underline">立即注册</a>
          </p>
        </div>
      </form>
      
      <!-- 技术动效背景 -->
      <div class="absolute bottom-4 right-4 opacity-50">
        <i class="fas fa-cog fa-spin text-blue-400 text-xl"></i>
        <i class="fas fa-cog fa-spin-pulse text-teal-400 text-sm ml-2"></i>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

// 登录表单数据
const loginForm = reactive({
  loginName: '',
  password: '',
  userMail: ''
});

// 状态
const loading = ref(false);
const showPassword = ref(false);
const rememberMe = ref(false);
const errors = reactive({
  loginName: '',
  password: ''
});

// 表单验证
const validateForm = () => {
  let valid = true;
  errors.loginName = '';
  errors.password = '';
  
  if (!loginForm.loginName.trim()) {
    errors.loginName = '请输入用户名';
    valid = false;
  }
  
  if (!loginForm.password) {
    errors.password = '请输入密码';
    valid = false;
  } else if (loginForm.password.length < 6) {
    errors.password = '密码长度不能少于6位';
    valid = false;
  }
  
  return valid;
};

// 登录处理
const handleLogin = async () => {
  if (!validateForm()) {
    return;
  }
  
  console.log('准备登录:', loginForm);
  
  loading.value = true;
  try {
    // 调用用户存储的登录方法
    const loginSuccess = await userStore.loginAction(
      loginForm.loginName, 
      loginForm.password
    );
    
    if (loginSuccess) {
      // 获取用户信息
      await userStore.getUserInfoAction();
      
      // 记住用户名
      if (rememberMe.value) {
        localStorage.setItem('remember_username', loginForm.loginName);
      } else {
        localStorage.removeItem('remember_username');
      }
      
      // 跳转到首页
      router.push('/dashboard');
    }
  } catch (error) {
    console.error('登录错误:', error);
    ElMessage.error('网络错误，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 跳转到注册页
const goToRegister = () => {
  router.push('/register');
};

// 初始化 - 检查记住的用户名
const initForm = () => {
  const rememberedUsername = localStorage.getItem('remember_username');
  if (rememberedUsername) {
    loginForm.loginName = rememberedUsername;
    rememberMe.value = true;
  }
  
  // 如果已经登录，则跳转
  if (userStore.isLoggedIn) {
    router.push('/dashboard');
  }
};

// 在组件挂载后执行初始化
onMounted(initForm);
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  background-image: url('https://images.pexels.com/photos/8386434/pexels-photo-8386434.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');
  background-size: cover;
  background-position: center;
  position: relative;
}

.auth-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 15, 40, 0.6);
  z-index: 1;
}

.auth-card {
  background-color: white;
  transition: all 0.3s ease;
}

.bg-gradient-primary {
  background: linear-gradient(135deg, #3498db, #1d6fa5);
}

.btn-hover-effect {
  transition: all 0.3s ease;
}

.btn-hover-effect:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
}

.btn-hover-effect:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.input-error {
  border-color: #e74c3c !important;
}

.error-message {
  color: #e74c3c;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

.animate-fadeIn {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.fa-spin {
  animation: spin 1s linear infinite;
}

.fa-spin-pulse {
  animation: spin 3s cubic-bezier(0.68, -0.55, 0.27, 1.55) infinite;
}
</style> 