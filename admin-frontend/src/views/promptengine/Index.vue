<template>
  <div class="prompt-home">
    <!-- 头部区域 -->
    <div class="home-header">
      <h1>提示工程平台</h1>
      <p class="sub-heading">智能撰写技术文档，高效管理专利交底书</p>
    </div>

    <!-- 统计区域 -->
    <el-row class="statistics-section" :gutter="20">
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-icon domain">
            <el-icon><Folder /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.value.domainCount }}</div>
            <div class="stat-label">技术领域</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-icon template">
            <el-icon><Files /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.value.templateCount }}</div>
            <div class="stat-label">提示词模板</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-icon document">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.value.documentCount }}</div>
            <div class="stat-label">生成文档</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 模块卡片区域 -->
    <h2 class="section-title">功能模块</h2>
    <el-row class="modules-section" :gutter="20">
      <!-- 技术领域管理 -->
      <el-col :span="8">
        <div class="module-card" @click="navigateTo('/promptengine/domain-config')">
          <div class="card-content">
            <img src="https://img.freepik.com/free-vector/gradient-technology-background_23-2149171887.jpg" class="card-image" alt="技术领域管理" />
            <h3>技术领域管理</h3>
            <div class="module-desc">创建和管理技术领域分类，构建专业知识体系</div>
            <div class="module-features">
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>多级技术分类管理</span>
              </div>
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>章节结构定义</span>
              </div>
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>模板绑定与关联</span>
              </div>
            </div>
            <el-button type="primary" @click.stop="navigateTo('/promptengine/domain-config')">进入管理</el-button>
          </div>
        </div>
      </el-col>
      
      <!-- 提示词模板管理 -->
      <el-col :span="8">
        <div class="module-card" @click="navigateTo('/promptengine/template-management')">
          <div class="card-content">
            <img src="https://img.freepik.com/free-vector/gradient-network-connection-background_23-2149011060.jpg" class="card-image" alt="提示词模板管理" />
            <h3>提示词模板管理</h3>
            <div class="module-desc">设计和优化AI提示词模板，提升文档生成质量</div>
            <div class="module-features">
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>多版本模板管理</span>
              </div>
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>变量参数配置</span>
              </div>
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>模板测试与优化</span>
              </div>
            </div>
            <el-button type="primary" @click.stop="navigateTo('/promptengine/template-management')">管理模板</el-button>
          </div>
        </div>
      </el-col>
      
      <!-- 交底书撰写 -->
      <el-col :span="8">
        <div class="module-card" @click="navigateTo('/promptengine/writer')">
          <div class="card-content">
            <img src="https://img.freepik.com/free-vector/gradient-blur-pink-blue-abstract-background_53876-117324.jpg" class="card-image" alt="交底书撰写" />
            <h3>交底书撰写</h3>
            <div class="module-desc">AI辅助撰写技术交底书，提高创作效率</div>
            <div class="module-features">
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>智能内容生成</span>
              </div>
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>实时编辑与优化</span>
              </div>
              <div class="feature-item">
                <el-icon><Check /></el-icon>
                <span>一键导出文档</span>
              </div>
            </div>
            <el-button type="primary" @click.stop="navigateTo('/promptengine/writer')">开始创作</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 最近活动区域 -->
    <div class="recent-activities">
      <h2 class="section-title">最近活动</h2>
      <div class="activity-timeline">
        <div class="activity-item" v-for="(activity, index) in recentActivities" :key="index">
          <div :class="`activity-icon ${activity.type}`">
            <el-icon v-if="activity.type === 'domain'"><Folder /></el-icon>
            <el-icon v-else-if="activity.type === 'template'"><Files /></el-icon>
            <el-icon v-else-if="activity.type === 'document'"><Document /></el-icon>
          </div>
          <div class="activity-content">
            <div class="activity-text">{{ activity.text }}</div>
            <div class="activity-time">{{ activity.time }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  Document, 
  Folder, 
  Files, 
  Check, 
  Edit
} from '@element-plus/icons-vue'

const router = useRouter()

// 统计数据
const statistics = reactive({
  value: {
    domainCount: 0,
    templateCount: 0,
    documentCount: 0
  }
})

// 导航到指定路径
const navigateTo = (path: string) => {
  console.log('导航到：', path)
  // 使用router.push代替window.location.href进行导航
  router.push(path)
}

// 最近活动数据
const recentActivities = ref([
  {
    type: 'domain',
    text: '张工创建了新的技术领域"智能机器人控制系统"',
    time: '2023-11-28 15:30'
  },
  {
    type: 'template',
    text: '李工更新了"神经网络模型"提示词模板v2.3版本',
    time: '2023-11-28 14:05'
  },
  {
    type: 'document',
    text: '王工完成了"电动汽车电池管理系统"技术交底书',
    time: '2023-11-27 17:22'
  },
  {
    type: 'template',
    text: '赵工创建了"机器学习算法"提示词模板',
    time: '2023-11-27 10:15'
  },
  {
    type: 'domain',
    text: '系统管理员添加了"量子计算"技术领域及其子类别',
    time: '2023-11-26 09:30'
  }
])

// 模拟获取数据
onMounted(() => {
  setTimeout(() => {
    statistics.value.domainCount = 24
    statistics.value.templateCount = 67
    statistics.value.documentCount = 135
  }, 600)
})
</script>

<style scoped>
.prompt-home {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

.home-header {
  text-align: center;
  margin-bottom: 30px;
}

.home-header h1 {
  font-size: 32px;
  color: #1a365d;
  margin-bottom: 8px;
}

.sub-heading {
  font-size: 16px;
  color: #4a5568;
}

.statistics-section {
  background: linear-gradient(135deg, #1a365d, #2c5282);
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.stat-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 15px;
  display: flex;
  align-items: center;
  height: 100px;
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}

.stat-icon.domain {
  background-color: rgba(66, 153, 225, 0.3);
}

.stat-icon.template {
  background-color: rgba(72, 187, 120, 0.3);
}

.stat-icon.document {
  background-color: rgba(237, 137, 54, 0.3);
}

.stat-icon .el-icon {
  font-size: 24px;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
}

.stat-label {
  font-size: 14px;
  color: #e2e8f0;
}

.section-title {
  font-size: 22px;
  color: #2d3748;
  margin-bottom: 20px;
  font-weight: 600;
}

.modules-section {
  margin-bottom: 30px;
}

.module-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100%;
  cursor: pointer;
}

.module-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.card-content {
  padding: 20px;
}

.card-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 15px;
}

.module-card h3 {
  font-size: 18px;
  color: #2d3748;
  margin-bottom: 10px;
}

.module-desc {
  color: #718096;
  margin-bottom: 15px;
  font-size: 14px;
}

.module-features {
  margin-bottom: 15px;
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #4a5568;
  font-size: 14px;
}

.feature-item .el-icon {
  color: #38a169;
  margin-right: 8px;
}

.recent-activities {
  margin-top: 30px;
}

.activity-timeline {
  border-left: 2px solid #e2e8f0;
  padding-left: 20px;
}

.activity-item {
  display: flex;
  margin-bottom: 20px;
  position: relative;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  position: absolute;
  left: -16px;
  background: white;
  border: 2px solid #e2e8f0;
}

.activity-icon.domain {
  color: #4299e1;
  background-color: #ebf8ff;
  border-color: #4299e1;
}

.activity-icon.template {
  color: #48bb78;
  background-color: #f0fff4;
  border-color: #48bb78;
}

.activity-icon.document {
  color: #ed8936;
  background-color: #fffaf0;
  border-color: #ed8936;
}

.activity-content {
  background: #f7fafc;
  border-radius: 8px;
  padding: 15px;
  flex: 1;
  margin-left: 15px;
}

.activity-text {
  color: #4a5568;
  margin-bottom: 5px;
}

.activity-time {
  color: #a0aec0;
  font-size: 12px;
}
</style> 