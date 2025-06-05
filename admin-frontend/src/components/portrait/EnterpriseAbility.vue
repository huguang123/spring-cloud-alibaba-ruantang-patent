<template>
  <el-card class="enterprise-ability">
    <template #header>
      <div class="card-header">
        <h3>企业能力分析</h3>
        <el-button type="primary" size="small" @click="refreshData">刷新数据</el-button>
      </div>
    </template>
    
    <div class="chart-container">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="6" animated />
      </div>
      <div v-else>
        <div class="radar-container">
          <RadarChart :data="radarData" title="企业能力维度评估" />
        </div>
        
        <el-divider />
        
        <div class="ability-details">
          <h4>维度详情分析</h4>
          <div class="ability-grid">
            <div v-for="(item, index) in dimensionDetails" :key="index" class="ability-item">
              <div class="ability-header">
                <el-icon :size="20" :color="getIconColor(item.score)">
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
                <el-tag :type="getTagType(item.score)">{{ item.score }}分</el-tag>
              </div>
              <p class="ability-description">{{ item.description }}</p>
              <div class="ability-progress">
                <el-progress
                  :percentage="item.score"
                  :stroke-width="15"
                  :color="getProgressColor(item.score)"
                />
              </div>
            </div>
          </div>
        </div>
        
        <el-divider />
        
        <div class="improvement-suggestions">
          <h4>能力提升建议</h4>
          <el-collapse accordion>
            <el-collapse-item v-for="(suggestion, index) in suggestions" :key="index" :title="suggestion.title">
              <div class="suggestion-content">
                <p>{{ suggestion.content }}</p>
                <ul>
                  <li v-for="(step, stepIndex) in suggestion.steps" :key="stepIndex">
                    {{ step }}
                  </li>
                </ul>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { Search, DataAnalysis, Cpu, Money, Document, Briefcase } from '@element-plus/icons-vue';
import RadarChart from './RadarChart.vue';
import { RadarChartData } from '@/types/portrait';

const loading = ref(true);

// 雷达图数据
const radarData = ref<RadarChartData>({
  indicators: [
    { name: '技术储备', max: 100 },
    { name: '研发投入', max: 100 },
    { name: '专利质量', max: 100 },
    { name: '市场表现', max: 100 },
    { name: '人才资源', max: 100 },
    { name: '创新能力', max: 100 }
  ],
  series: [
    {
      name: '当前企业',
      value: [85, 90, 78, 82, 95, 88],
      color: '#5470c6'
    },
    {
      name: '行业平均',
      value: [70, 75, 65, 60, 80, 70],
      color: '#91cc75'
    }
  ]
});

// 维度详情
const dimensionDetails = ref([
  {
    name: '技术储备',
    score: 85,
    icon: 'Cpu',
    description: '企业技术积累深厚，拥有多项核心技术专利，可支撑长期发展需求。',
  },
  {
    name: '研发投入',
    score: 90,
    icon: 'Money',
    description: '研发投入占比高于行业平均15%，持续的研发经费保障了创新能力。',
  },
  {
    name: '专利质量',
    score: 78,
    icon: 'Document',
    description: '专利结构合理，但高价值专利占比有待提升，核心专利保护需加强。',
  },
  {
    name: '市场表现',
    score: 82,
    icon: 'DataAnalysis',
    description: '产品市场占有率稳步增长，客户满意度高，但国际市场拓展不足。',
  },
  {
    name: '人才资源',
    score: 95,
    icon: 'Briefcase',
    description: '拥有强大的研发团队，人才梯队建设完善，核心技术人员稳定性高。',
  },
  {
    name: '创新能力',
    score: 88,
    icon: 'Search',
    description: '创新机制健全，产学研合作紧密，但颠覆性创新项目较少。',
  }
]);

// 提升建议
const suggestions = ref([
  {
    title: '专利质量提升计划',
    content: '建议加强高价值专利布局，提高专利质量评分',
    steps: [
      '聚焦核心技术领域，精选高价值专利申请方向',
      '加强专利挖掘，提高发明专利占比',
      '建立专利价值评估体系，定期评估现有专利组合'
    ]
  },
  {
    title: '国际市场拓展战略',
    content: '针对市场表现维度，建议加强国际市场布局',
    steps: [
      '制定差异化国际市场进入策略',
      '加强国际知识产权保护布局',
      '建立国际化人才培养机制'
    ]
  },
  {
    title: '颠覆性创新机制建设',
    content: '建议建立专门的颠覆性创新孵化机制',
    steps: [
      '设立创新实验室，鼓励探索性研究',
      '建立灵活的创新项目评估机制',
      '加强与高校和研究机构的深度合作'
    ]
  }
]);

// 根据分数获取图标颜色
const getIconColor = (score: number) => {
  if (score >= 90) return '#67c23a';
  if (score >= 80) return '#409eff';
  if (score >= 70) return '#e6a23c';
  return '#f56c6c';
};

// 根据分数获取标签类型
const getTagType = (score: number) => {
  if (score >= 90) return 'success';
  if (score >= 80) return 'primary';
  if (score >= 70) return 'warning';
  return 'danger';
};

// 根据分数获取进度条颜色
const getProgressColor = (score: number) => {
  if (score >= 90) return '#67c23a';
  if (score >= 80) return '#409eff';
  if (score >= 70) return '#e6a23c';
  return '#f56c6c';
};

// 刷新数据
const refreshData = () => {
  loading.value = true;
  // 模拟API请求
  setTimeout(() => {
    // 这里可以调用实际的API获取数据
    loading.value = false;
  }, 1500);
};

onMounted(() => {
  // 模拟数据加载
  setTimeout(() => {
    loading.value = false;
  }, 1000);
});
</script>

<style scoped>
.enterprise-ability {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  min-height: 400px;
}

.loading-container {
  padding: 20px;
}

.radar-container {
  height: 400px;
  margin-bottom: 20px;
}

.ability-details {
  margin: 20px 0;
}

.ability-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 15px;
}

.ability-item {
  padding: 15px;
  border-radius: 4px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.ability-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  gap: 10px;
}

.ability-header span {
  font-weight: bold;
  margin-right: auto;
}

.ability-description {
  font-size: 14px;
  color: #606266;
  margin-bottom: 15px;
  min-height: 40px;
}

.improvement-suggestions {
  margin-top: 20px;
}

.suggestion-content {
  padding: 10px 0;
}

.suggestion-content ul {
  margin-top: 10px;
  padding-left: 20px;
}

.suggestion-content li {
  margin-bottom: 8px;
  color: #606266;
}
</style> 