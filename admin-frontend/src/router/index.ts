import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

// 布局
import MainLayout from '@/layouts/MainLayout.vue'

// 路由表
const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginNew.vue'),
    meta: { title: '登录 - 软唐知识产权后台管理系统', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/RegisterNew.vue'),
    meta: { title: '注册 - 软唐知识产权后台管理系统', requiresAuth: false }
  },
  {
    path: '/login-new',
    name: 'LoginNew',
    component: () => import('@/views/auth/LoginNew.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        meta: { requiresAuth: true, title: '仪表盘' }
      },
      {
        path: 'organization/tree',
        name: 'OrganizationTree',
        component: () => import('@/views/organization/OrganizationTree.vue'),
        meta: { requiresAuth: true, title: '组织架构管理' }
      },
      {
        path: 'tenant',
        name: 'TenantManagement',
        component: () => import('@/views/tenant/Index.vue'),
        meta: { requiresAuth: true, title: '租户管理中心' }
      },
      // {
      //   path: 'tenant/detail/:id',
      //   name: 'TenantDetail',
      //   component: () => import('@/views/tenant/Detail.vue'),
      //   meta: { requiresAuth: true, title: '租户详情' }
      // },
      // {
      //   path: 'permission',
      //   name: 'Permission',
      //   component: () => import('@/views/permission/Index.vue'),
      //   meta: { requiresAuth: true, title: '权限配置中心' }
      // },
      {
        path: 'permission/basic',
        name: 'BasicPermission',
        component: () => import('@/views/permission/BasicPermission.vue'),
        meta: { requiresAuth: true, title: '基础权限配置' }
      },
      {
        path: 'permission/system-template-config',
        name: 'SystemTemplatePermissionConfig',
        component: () => import('@/views/permission/SystemTemplatePermission.vue'),
        meta: {
          requiresAuth: true, 
          title: '系统模板权限配置'
        }
      },
      // {
      //   path: 'enterprise-template-management',
      //   name: 'EnterpriseTemplateManagement',
      //   component: () => import('@/views/enterprise-template/Index.vue'),
      //   meta: { requiresAuth: true, title: '企业模板管理中心' }
      // },
      {
        path: 'enterprise-template/list',
        name: 'EnterpriseTemplateList',
        component: () => import('@/views/enterprise-template/TemplateList.vue'),
        meta: { requiresAuth: true, title: '企业模板列表' }
      },
      {
        path: 'role-management/list',
        name: 'RoleList',
        component: () => import('@/views/role-management/RoleList.vue'),
        meta: { requiresAuth: true, title: '角色列表' }
      },
      {
        path: 'promptengine',
        name: 'PromptEngine',
        component: () => import('@/views/promptengine/Index.vue'),
        meta: { requiresAuth: true, title: '提示词工程' }
      },
      {
        path: 'promptengine/domain-config',
        name: 'DomainConfig',
        component: () => import('@/views/promptengine/DomainConfigNew.vue'),
        meta: { requiresAuth: true, title: '技术领域管理 - 软唐知识产权后台管理系统' }
      },
      {
        path: 'promptengine/template-management',
        name: 'TemplateManagement',
        component: () => import('@/views/promptengine/TemplateManagementNew.vue'),
        meta: { requiresAuth: true, title: '提示词模板管理 - 软唐知识产权后台管理系统' }
      },
      {
        path: 'promptengine/writer',
        name: 'Writer',
        component: () => import('@/views/promptengine/WriterNew.vue'),
        meta: { requiresAuth: true, title: '交底书撰写 - 软唐知识产权后台管理系统' }
      },
      // {
      //   path: 'templates/edit/:id',
      //   name: 'TemplateEdit',
      //   component: () => import('@/views/templates/Edit.vue'),
      //   meta: { requiresAuth: true, title: '编辑模板' }
      // },
      {
        path: 'portrait',
        name: 'Portrait',
        component: () => import('@/views/portrait/Index.vue'),
        meta: { requiresAuth: true, title: '智能画像' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { requiresAuth: true, title: '个人中心' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/user/Settings.vue'),
        meta: { requiresAuth: true, title: '账号设置' }
      }
      // {
      //   path: 'audit',
      //   name: 'Audit',
      //   component: () => import('@/views/audit/Index.vue'),
      //   meta: { requiresAuth: true, title: '审计追踪' }
      // },
      // {
      //   path: 'settings',
      //   name: 'Settings',
      //   component: () => import('@/views/settings/Index.vue'),
      //   meta: { requiresAuth: true, title: '系统设置' }
      // }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  console.log('路由守卫触发:', to.path);
  console.log('当前登录状态:', userStore.isLoggedIn);
  console.log('当前租户ID:', localStorage.getItem('tenant_id'));
  
  // 判断是否需要登录权限
  if (to.meta.requiresAuth) {
    // 判断是否已登录
    if (userStore.isLoggedIn) {
      // 如果用户已登录但还没有获取用户信息，则获取用户信息
      if (!localStorage.getItem('tenant_id') || 
          (to.path.includes('/organization') && !localStorage.getItem('tenant_id'))) {
        try {
          console.log('路由守卫：尝试获取用户信息...');
          await userStore.getUserInfoAction();
          const tenantId = localStorage.getItem('tenant_id');
          console.log('路由守卫：获取用户信息成功，租户ID:', tenantId);
          
          // 如果访问组织页面但租户ID仍然为空，则返回仪表盘
          if (to.path.includes('/organization') && !tenantId) {
            console.error('访问组织页面但租户ID仍为空，重定向到仪表盘');
            ElMessage.error('租户信息获取失败，请重新登录');
            next('/dashboard');
            return;
          }
        } catch (error) {
          console.error('路由守卫：获取用户信息失败:', error);
          // 如果是组织页面，则重定向到仪表盘
          if (to.path.includes('/organization')) {
            next('/dashboard');
            return;
          }
        }
      }
      
      // 权限判断后续可以在这里增加
      next()
    } else {
      // 未登录则重定向到登录页
      next('/login')
    }
  } else {
    // 如果是登录或注册页面，且已经登录，则重定向到首页
    if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
      next('/dashboard')
    } else {
      next()
    }
  }
})

export default router 