<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { EnterprisePortrait } from '@/types/portrait';
import { getEnterprisePortrait, getEnterpriseRelationGraph, exportEnterpriseReport } from '@/api/portrait';
import EnterpriseRadarChart from './components/EnterpriseRadarChart.vue';
import RelationGraph from './components/RelationGraph.vue';
import SuggestionCard from './components/SuggestionCard.vue';

const route = useRoute();
const loading = ref(true);
const exportLoading = ref(false);
const activeTab = ref('ability');
const portrait = ref<EnterprisePortrait | null>(null);
const graphData = ref(null);

// 获取企业ID
const enterpriseId = route.params.id as string;

// 获取企业画像数据
const fetchPortraitData = async () => {
  try {
    loading.value = true;
    portrait.value = await getEnterprisePortrait(enterpriseId);
    loading.value = false;
  } catch (error) {
    console.error('获取企业画像数据失败', error);
    ElMessage.error('获取企业画像数据失败');
    loading.value = false;
  }
};

// 获取企业关系图谱数据
const fetchRelationGraph = async () => {
  try {
    graphData.value = await getEnterpriseRelationGraph(enterpriseId, 2);
  } catch (error) {
    console.error('获取企业关系图谱失败', error);
    ElMessage.error('获取企业关系图谱失败');
  }
};

// 导出报告
const handleExport = async (format: 'pdf' | 'xlsx') => {
  try {
    exportLoading.value = true;
    const blob = await exportEnterpriseReport(enterpriseId, format);
    
    // 创建下载链接
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `企业画像报告_${portrait.value?.enterpriseName || enterpriseId}.${format}`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
    
    exportLoading.value = false;
    ElMessage.success(`报告已成功导出为${format.toUpperCase()}格式`);
  } catch (error) {
    console.error('导出报告失败', error);
    ElMessage.error('导出报告失败');
    exportLoading.value = false;
  }
};

// 切换标签页
const handleTabChange = (tab: string) => {
  if (tab === 'relation' && !graphData.value) {
    fetchRelationGraph();
  }
};

onMounted(() => {
  fetchPortraitData();
});
</script>

<template>
  <div class="enterprise-portrait-container">
    <el-card class="header-card" v-loading="loading">
      <template #header>
        <div class="header-info">
          <h2>{{ portrait?.enterpriseName || '企业' }}能力画像</h2>
          <div class="portrait-date">
            生成日期: {{ portrait?.reportDate || '加载中...' }}
          </div>
          <div class="action-buttons">
            <el-button 
              type="primary" 
              :loading="exportLoading"
              @click="handleExport('pdf')"
            >
              导出PDF
            </el-button>
            <el-button 
              :loading="exportLoading"
              @click="handleExport('xlsx')"
            >
              导出Excel
            </el-button>
          </div>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="能力画像" name="ability">
          <div class="ability-content" v-if="portrait">
            <div class="chart-container">
              <enterprise-radar-chart :chart-data="portrait.radarData" />
            </div>
            
            <div class="dimension-details">
              <h3>维度详情</h3>
              <el-row :gutter="20">
                <el-col :span="8" v-for="(dimension, index) in portrait.dimensionDetails" :key="index">
                  <el-card class="dimension-card">
                    <h4>{{ dimension.name }}</h4>
                    <div class="score">
                      <span class="value">{{ dimension.score }}</span>
                      <span class="max">/100</span>
                    </div>
                    <div class="description">{{ dimension.description }}</div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
            
            <div class="suggestions-container">
              <h3>改进建议</h3>
              <el-row :gutter="20">
                <el-col :span="12" v-for="(suggestion, index) in portrait.suggestions" :key="index">
                  <suggestion-card :suggestion="suggestion" />
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="关系图谱" name="relation">
          <div class="relation-content">
            <relation-graph 
              v-if="graphData" 
              :graph-data="graphData" 
              :enterprise-id="enterpriseId"
            />
            <div v-else class="loading-graph">
              <el-skeleton :rows="10" animated />
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="行业比较" name="comparison">
          <div class="comparison-content" v-if="portrait">
            <h3>行业能力对比</h3>
            <div class="comparison-description">
              <p>当前企业在行业中处于 <strong>{{ portrait.industryRanking.percentile }}%</strong> 的位置。</p>
              <p>比较基准: {{ portrait.industryRanking.baseline }}</p>
            </div>
            
            <el-table :data="portrait.dimensionDetails" style="width: 100%; margin-top: 20px;">
              <el-table-column prop="name" label="能力维度" width="180" />
              <el-table-column prop="score" label="企业得分" width="120" />
              <el-table-column prop="industryAvg" label="行业平均" width="120" />
              <el-table-column label="对比">
                <template #default="scope">
                  <div class="comparison-progress">
                    <el-progress 
                      :percentage="scope.row.score" 
                      :color="scope.row.score > scope.row.industryAvg ? '#67C23A' : '#F56C6C'"
                    />
                    <div class="industry-marker" :style="{left: scope.row.industryAvg + '%'}">
                      <el-tooltip content="行业平均">
                        <div class="marker"></div>
                      </el-tooltip>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped>
.enterprise-portrait-container {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.header-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
}

.header-info h2 {
  margin: 0;
  font-size: 22px;
}

.portrait-date {
  color: #606266;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.chart-container {
  height: 400px;
  margin-bottom: 30px;
}

.ability-content, .relation-content, .comparison-content {
  padding: 20px 0;
}

.dimension-details, .suggestions-container {
  margin-top: 30px;
}

.dimension-card {
  height: 180px;
  margin-bottom: 20px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.dimension-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.dimension-card h4 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 18px;
}

.score {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 15px;
}

.score .value {
  color: #409EFF;
}

.score .max {
  font-size: 16px;
  color: #909399;
}

.description {
  color: #606266;
  font-size: 14px;
}

.loading-graph {
  padding: 40px;
}

.comparison-description {
  margin-bottom: 30px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.comparison-progress {
  position: relative;
  padding-top: 10px;
}

.industry-marker {
  position: absolute;
  top: 0;
  transform: translateX(-50%);
}

.marker {
  width: 2px;
  height: 16px;
  background-color: #606266;
  cursor: pointer;
}

@media (max-width: 768px) {
  .header-info {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .action-buttons {
    margin-top: 15px;
  }
}
</style> 