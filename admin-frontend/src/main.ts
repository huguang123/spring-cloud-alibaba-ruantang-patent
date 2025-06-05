import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@fortawesome/fontawesome-free/css/all.css'
import './assets/css/tailwind.css'
import App from './App.vue'
import router from './router'

// 调试代理问题
console.log('应用初始化中...');

// pinia 状态管理
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

// 创建Vue应用实例
const app = createApp(App)

// 使用插件
app.use(pinia)
app.use(router)
app.use(ElementPlus)

// 挂载应用
app.mount('#app')

// 输出调试信息
console.log('应用已启动，API请求将使用相对路径') 