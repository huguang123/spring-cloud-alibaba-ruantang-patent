<template>
  <div class="company-portrait">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="company-info-card">
          <div class="company-header">
            <div class="company-logo">
              <img :src="companyInfo.logoUrl || '/images/default-company.png'" alt="公司logo" />
            </div>
            <div class="company-basic">
              <h2>{{ companyInfo.name }}</h2>
              <div class="info-row">
                <span>成立年份: {{ companyInfo.establishmentYear }}</span>
                <span>注册资本: {{ companyInfo.registeredCapital }}</span>
                <span>法定代表人: {{ companyInfo.legalRepresentative }}</span>
              </div>
              <div class="info-row">
                <span>所在地区: {{ companyInfo.location }}</span>
                <span>所属行业: {{ companyInfo.industry }}</span>
              </div>
              <div class="company-score">
                <span class="score-label">综合评分:</span>
                <el-rate
                  v-model="companyInfo.overallScore"
                  :max="10"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}"
                />
              </div>
            </div>
          </div>
          <div class="company-desc">
            <h3>企业简介</h3>
            <p>{{ companyInfo.description }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>企业能力雷达图</span>
              <el-tooltip content="企业六大维度能力评估">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <RadarChart :data="radarData" height="300px" :show-legend="true" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>专利分类统计</span>
              <el-tooltip content="企业专利类型分布情况">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div ref="patentPieRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>专利趋势分析</span>
              <el-button type="primary" size="small" @click="refreshTrendData">
                更新预测
              </el-button>
            </div>
          </template>
          <div ref="patentTrendRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>技术关键词</span>
              <el-tooltip content="企业技术领域关键词分布">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div ref="keywordCloudRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>报告导出</span>
            </div>
          </template>
          <el-form :model="exportForm" label-width="120px">
            <el-form-item label="报告标题">
              <el-input v-model="exportForm.title" placeholder="请输入报告标题" />
            </el-form-item>
            <el-form-item label="包含章节">
              <el-checkbox-group v-model="exportForm.sections">
                <el-checkbox label="company-info">企业基本信息</el-checkbox>
                <el-checkbox label="radar-chart">能力雷达图</el-checkbox>
                <el-checkbox label="patent-analysis">专利分析</el-checkbox>
                <el-checkbox label="keyword-analysis">关键词分析</el-checkbox>
                <el-checkbox label="prediction">未来趋势预测</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="导出格式">
              <el-radio-group v-model="exportForm.format">
                <el-radio label="pdf">PDF格式</el-radio>
                <el-radio label="docx">Word格式</el-radio>
                <el-radio label="pptx">PPT格式</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="exportReport">生成报告</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, shallowRef } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'
import { companyPortraitApi } from '@/api/portrait'
import RadarChart from '@/components/portrait/RadarChart.vue'
import type { 
  CompanyInfo, 
  RadarData, 
  PatentCategory, 
  PatentTrend, 
  Keyword,
  ExportConfig
} from '@/types/portrait'

const route = useRoute()
const companyId = ref(route.params.id as string)

// 企业基本信息
const companyInfo = reactive<CompanyInfo>({
  id: '',
  name: '',
  establishmentYear: 0,
  registeredCapital: '',
  legalRepresentative: '',
  location: '',
  overallScore: 0,
  description: '',
  industry: ''
})

// 雷达图数据
const radarData = reactive<RadarData>({
  dimensions: [],
  companyScores: [],
  industryAvg: []
})

// 专利分类数据
const patentCategories = ref<PatentCategory[]>([])

// 专利趋势数据
const patentTrends = ref<PatentTrend[]>([])

// 关键词数据
const keywords = ref<Keyword[]>([])

// 图表DOM引用
const patentPieRef = ref<HTMLElement | null>(null)
const patentTrendRef = ref<HTMLElement | null>(null)
const keywordCloudRef = ref<HTMLElement | null>(null)

// 图表实例
const patentPieChart = shallowRef<echarts.ECharts | null>(null)
const patentTrendChart = shallowRef<echarts.ECharts | null>(null)
const keywordCloudChart = shallowRef<echarts.ECharts | null>(null)

// 导出配置
const exportForm = reactive<ExportConfig>({
  title: '',
  sections: ['company-info', 'radar-chart', 'patent-analysis'],
  format: 'pdf'
})

// 初始化数据
const initData = async () => {
  try {
    // 实际项目中应当从后端获取数据
    // const { data } = await companyPortraitApi.getCompanyDetail(companyId.value)
    
    // 模拟数据
    companyInfo.id = companyId.value || '1001'
    companyInfo.name = '智能科技有限公司'
    companyInfo.establishmentYear = 2010
    companyInfo.registeredCapital = '5000万元'
    companyInfo.legalRepresentative = '张三'
    companyInfo.location = '北京市海淀区'
    companyInfo.overallScore = 8.5
    companyInfo.description = '该企业是一家专注于人工智能和大数据领域的高新技术企业，拥有多项核心专利技术，在行业内具有领先地位。'
    companyInfo.industry = '人工智能'
    
    // 雷达图数据
    radarData.dimensions = [
      { name: '技术储备', key: 'tech', score: 85 },
      { name: '研发投入', key: 'rd', score: 90 },
      { name: '专利质量', key: 'patent', score: 78 },
      { name: '市场影响', key: 'market', score: 82 },
      { name: '团队建设', key: 'team', score: 88 },
      { name: '创新能力', key: 'innovation', score: 92 }
    ]
    radarData.companyScores = radarData.dimensions.map(d => d.score)
    radarData.industryAvg = [75, 70, 65, 80, 75, 68]
    
    // 专利分类数据
    patentCategories.value = [
      { name: '发明专利', value: 45 },
      { name: '实用新型', value: 30 },
      { name: '外观设计', value: 15 },
      { name: '国际专利', value: 10 }
    ]
    
    // 专利趋势数据
    patentTrends.value = [
      { year: 2018, count: 5 },
      { year: 2019, count: 8 },
      { year: 2020, count: 12 },
      { year: 2021, count: 18 },
      { year: 2022, count: 25 },
      { year: 2023, count: 30 },
      { year: 2024, count: 35, predicted: true },
      { year: 2025, count: 42, predicted: true }
    ]
    
    // 关键词数据
    keywords.value = [
      { text: '人工智能', value: 100 },
      { text: '机器学习', value: 85 },
      { text: '深度学习', value: 80 },
      { text: '计算机视觉', value: 75 },
      { text: '自然语言处理', value: 70 },
      { text: '大数据', value: 65 },
      { text: '智能算法', value: 60 },
      { text: '神经网络', value: 55 },
      { text: '知识图谱', value: 50 },
      { text: '云计算', value: 45 },
      { text: '边缘计算', value: 40 },
      { text: '智能推荐', value: 35 }
    ]
    
    exportForm.title = `${companyInfo.name}智能画像分析报告`
    
    // 初始化图表
    initCharts()
  } catch (error) {
    console.error('获取企业数据失败:', error)
    ElMessage.error('获取企业数据失败')
  }
}

// 初始化图表
const initCharts = () => {
  initPatentPieChart()
  initPatentTrendChart()
  initKeywordCloudChart()
}

// 初始化专利分类饼图
const initPatentPieChart = () => {
  if (!patentPieRef.value) return
  
  patentPieChart.value = echarts.init(patentPieRef.value)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: 'bottom'
    },
    series: [
      {
        name: '专利分类',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: patentCategories.value.map(item => ({
          name: item.name,
          value: item.value
        }))
      }
    ]
  }
  
  patentPieChart.value.setOption(option)
}

// 初始化专利趋势图
const initPatentTrendChart = () => {
  if (!patentTrendRef.value) return
  
  patentTrendChart.value = echarts.init(patentTrendRef.value)
  
  const historicalData = patentTrends.value.filter(item => !item.predicted)
  const predictedData = patentTrends.value.filter(item => item.predicted)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['历史专利数', '预测专利数'],
      bottom: 'bottom'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: patentTrends.value.map(item => item.year)
    },
    yAxis: {
      type: 'value',
      name: '专利数量',
      nameLocation: 'end'
    },
    series: [
      {
        name: '历史专利数',
        type: 'bar',
        data: patentTrends.value.map(item => 
          item.predicted ? null : item.count
        )
      },
      {
        name: '预测专利数',
        type: 'bar',
        itemStyle: {
          color: '#91cc75'
        },
        data: patentTrends.value.map(item => 
          item.predicted ? item.count : null
        )
      },
      {
        name: '趋势线',
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 0
        },
        showSymbol: false,
        areaStyle: {
          opacity: 0.8,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(128, 255, 165, 0.3)'
            },
            {
              offset: 1,
              color: 'rgba(1, 191, 236, 0)'
            }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: patentTrends.value.map(item => item.count)
      }
    ]
  }
  
  patentTrendChart.value.setOption(option)
}

// 初始化关键词云图
const initKeywordCloudChart = () => {
  if (!keywordCloudRef.value) return
  
  keywordCloudChart.value = echarts.init(keywordCloudRef.value)
  
  const option: EChartsOption = {
    tooltip: {
      show: true
    },
    series: [{
      type: 'wordCloud',
      shape: 'circle',
      left: 'center',
      top: 'center',
      width: '90%',
      height: '90%',
      right: null,
      bottom: null,
      sizeRange: [12, 30],
      rotationRange: [-90, 90],
      rotationStep: 45,
      gridSize: 8,
      drawOutOfBound: false,
      textStyle: {
        fontFamily: 'sans-serif',
        fontWeight: 'bold',
        color: function() {
          return 'rgb(' + [
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160)
          ].join(',') + ')'
        }
      },
      emphasis: {
        focus: 'self',
        textStyle: {
          shadowBlur: 10,
          shadowColor: '#333'
        }
      },
      data: keywords.value.map(item => ({
        name: item.text,
        value: item.value
      }))
    }]
  }
  
  keywordCloudChart.value.setOption(option)
}

// 更新趋势预测
const refreshTrendData = () => {
  ElMessage.success('趋势数据更新中...')
  // 实际项目中应当调用后端API重新预测
  setTimeout(() => {
    patentTrends.value = [
      ...patentTrends.value.filter(item => !item.predicted),
      { year: 2024, count: 38, predicted: true },
      { year: 2025, count: 46, predicted: true },
      { year: 2026, count: 55, predicted: true }
    ]
    initPatentTrendChart()
    ElMessage.success('趋势数据已更新')
  }, 1500)
}

// 导出报告
const exportReport = () => {
  ElMessage.success(`正在生成${companyInfo.name}的分析报告，格式: ${exportForm.format}`)
  // 实际项目中应当调用后端API生成报告
  setTimeout(() => {
    ElMessage.success('报告生成成功，正在下载...')
  }, 2000)
}

// 窗口大小变化时重置图表大小
const handleResize = () => {
  patentPieChart.value?.resize()
  patentTrendChart.value?.resize()
  keywordCloudChart.value?.resize()
}

// 组件挂载时初始化数据
onMounted(() => {
  initData()
  window.addEventListener('resize', handleResize)
})

// 组件卸载时移除事件监听
const onUnmounted = () => {
  window.removeEventListener('resize', handleResize)
  patentPieChart.value?.dispose()
  patentTrendChart.value?.dispose()
  keywordCloudChart.value?.dispose()
}
</script>

<style scoped>
.company-portrait {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.company-info-card {
  margin-bottom: 20px;
}

.chart-card {
  height: 100%;
}

.company-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.company-logo {
  width: 100px;
  height: 100px;
  margin-right: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.company-logo img {
  max-width: 100%;
  max-height: 100%;
}

.company-basic {
  flex: 1;
}

.company-basic h2 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #303133;
}

.info-row {
  display: flex;
  margin-bottom: 10px;
}

.info-row span {
  margin-right: 20px;
  color: #606266;
}

.company-score {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.score-label {
  margin-right: 10px;
  font-weight: bold;
}

.company-desc {
  border-top: 1px solid #ebeef5;
  padding-top: 15px;
}

.company-desc h3 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 16px;
  color: #303133;
}

.company-desc p {
  margin: 0;
  line-height: 1.6;
  color: #606266;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header span {
  font-size: 16px;
  font-weight: bold;
}
</style> 