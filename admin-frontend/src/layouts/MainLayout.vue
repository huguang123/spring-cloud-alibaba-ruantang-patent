<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed }">
      <div class="logo-container flex items-center justify-center h-16 border-b border-gray-200">
        <img src="@/assets/images/logo.svg" alt="Logo" class="h-8" v-if="!isCollapsed">
        <img src="@/assets/images/logo-mini.svg" alt="Logo" class="h-8" v-else>
      </div>
      
      <el-menu
        default-active="dashboard"
        :collapse="isCollapsed"
        class="sidebar-menu"
        background-color="#ffffff"
        text-color="#333333"
        active-text-color="#409EFF"
        :router="true"
      >
        <el-menu-item index="/dashboard">
          <el-icon>
            <Monitor />
          </el-icon>
          <template #title>仪表盘</template>
        </el-menu-item>
        
        <el-sub-menu index="organization-management">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>组织架构管理中心</span>
          </template>
          <el-menu-item index="/organization/tree">组织架构</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/tenant">
          <el-icon>
            <OfficeBuilding />
          </el-icon>
          <template #title>租户管理中心</template>
        </el-menu-item>
        
        <el-sub-menu index="permission">
          <template #title>
            <el-icon><Key /></el-icon>
            <span>权限配置中心</span>
          </template>
          <el-menu-item index="/permission/basic">基础权限配置</el-menu-item>
          <el-menu-item index="/permission/system-template-config">系统模板权限配置</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="enterprise-template">
            <template #title>
                <el-icon><Collection /></el-icon>
                <span>企业模板管理中心</span>
            </template>
            <el-menu-item index="/enterprise-template/list">企业模板列表</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="role-management">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>角色管理中心</span>
          </template>
          <el-menu-item index="/role-management/list">角色列表</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="3" v-if="hasPermission('提示词工程')">
          <template #title>
            <el-icon><EditPen /></el-icon>
            <span>提示词工程</span>
          </template>
          <el-menu-item index="/promptengine">提示词工程概览</el-menu-item>
          <el-menu-item index="/promptengine/domain-config">技术领域管理</el-menu-item>
          <el-menu-item index="/promptengine/template-management">提示词模板管理</el-menu-item>
          <el-menu-item index="/promptengine/writer">交底书撰写</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/portrait">
          <el-icon>
            <PieChart />
          </el-icon>
          <template #title>智能画像</template>
        </el-menu-item>
        
        <el-menu-item index="/audit">
          <el-icon>
            <Lock />
          </el-icon>
          <template #title>审计追踪</template>
        </el-menu-item>
        
        <el-menu-item index="/settings">
          <el-icon>
            <Setting />
          </el-icon>
          <template #title>系统设置</template>
        </el-menu-item>
      </el-menu>
      
      <div class="sidebar-footer flex items-center justify-center p-2 border-t border-gray-200">
        <el-button 
          type="text" 
          @click="toggleCollapse"
        >
          <el-icon>
            <i :class="isCollapsed ? 'fas fa-chevron-right' : 'fas fa-chevron-left'"></i>
          </el-icon>
        </el-button>
      </div>
    </aside>
    
    <!-- 主要内容区 -->
    <div class="main-content">
      <!-- 顶部导航栏 -->
      <header class="header bg-white border-b border-gray-200">
        <div class="flex items-center justify-between px-4 h-16">
          <div class="flex items-center">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="flex items-center">
            <el-tooltip content="全屏" placement="bottom">
              <el-button type="text" @click="toggleFullscreen">
                <el-icon><FullScreen /></el-icon>
              </el-button>
            </el-tooltip>
            
            <el-tooltip content="通知" placement="bottom">
              <el-badge :value="3" class="ml-4">
                <el-button type="text">
                  <el-icon><Bell /></el-icon>
                </el-button>
              </el-badge>
            </el-tooltip>
            
            <el-dropdown trigger="click" class="ml-4">
              <div class="flex items-center cursor-pointer user-info-container">
                <el-avatar :size="32" :src="userInfo.avatar" class="user-avatar" />
                <div class="user-details ml-2">
                  <span class="user-name">{{ userInfo.username || '用户' }}</span>
                  <span class="user-role">{{ userInfo.roles?.[0] || '普通用户' }}</span>
                </div>
                <el-icon class="el-icon--right ml-1"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="refreshUserInfo">
                    <el-icon><Refresh /></el-icon>刷新用户信息
                  </el-dropdown-item>
                  <el-dropdown-item @click="goToProfile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item @click="goToSettings">
                    <el-icon><Setting /></el-icon>账号设置
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </header>
      
      <!-- 页面内容 -->
      <main class="page-content p-6">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessageBox } from 'element-plus'
import { 
  Monitor, Connection, Document, PieChart, Lock, Setting, 
  FullScreen, Bell, ArrowDown, User, SwitchButton, EditPen, 
  OfficeBuilding, Key, Collection, Avatar as UserFilled, Refresh
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

// 监听用户信息变化
watch(userInfo, (newUserInfo) => {
  console.log('MainLayout - 用户信息变化:', newUserInfo);
}, { deep: true, immediate: true });

// 使用userStore中的hasPermission方法
const hasPermission = (permission: string): boolean => {
  return userStore.hasPermission(permission);
}

// 侧边栏收起与展开
const isCollapsed = ref(false)
const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

// 全屏切换
function toggleFullscreen() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    }
  }
}

// 导航
function goToProfile() {
  router.push('/profile')
}

function goToSettings() {
  router.push('/settings')
}

// 退出登录
function handleLogout() {
  ElMessageBox.confirm(
    '确定要退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    userStore.logoutAction()
    router.push('/login')
  })
}

// 刷新用户信息
async function refreshUserInfo() {
  try {
    console.log('手动刷新用户信息...');
    await userStore.getUserInfoAction();
    console.log('用户信息刷新成功:', userStore.userInfo);
  } catch (error) {
    console.error('刷新用户信息失败:', error);
  }
}
</script>

<style scoped>
.layout-container {
  @apply flex min-h-screen;
}

.sidebar {
  @apply w-64 bg-white border-r border-gray-200 flex flex-col transition-all duration-300;
  flex-shrink: 0;
}

.sidebar-collapsed {
  @apply w-20;
}

.sidebar-menu {
  @apply flex-1 overflow-y-auto;
  border-right: none;
}

.main-content {
  @apply flex flex-col flex-1;
}

.page-content {
  @apply flex-1 overflow-auto bg-gray-50;
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 用户信息样式 */
.user-info-container {
  padding: 4px 8px;
  border-radius: 6px;
  transition: background-color 0.2s ease;
}

.user-info-container:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  border: 2px solid #e4e7ed;
  transition: border-color 0.2s ease;
}

.user-info-container:hover .user-avatar {
  border-color: #409eff;
}

.user-details {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.2;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 2px;
}

.user-role {
  font-size: 12px;
  color: #909399;
}
</style> 