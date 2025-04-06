import { createRouter, createWebHistory } from 'vue-router';

// 导入视图组件（目前我们使用App.vue内的iframe作为主内容，暂不需要路由）
// 这里仅为标准Vue项目结构示例

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../App.vue')
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router; 