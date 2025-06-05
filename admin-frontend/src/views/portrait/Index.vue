<template>
  <div class="portrait-container">
    <el-card class="header-card">
      <div class="page-header">
        <div class="title">
          <h2>企业专利能力智能画像</h2>
          <span class="subtitle">专利数据智能分析 · 能力评估 · 趋势预测</span>
        </div>
        <div class="company-selector">
          <el-select 
            v-model="currentCompanyId" 
            placeholder="请选择企业" 
            @change="loadCompanyData"
            clearable
          >
            <el-option 
              v-for="item in companyList" 
              :key="item.id" 
              :label="item.name" 
              :value="item.id"
            />
          </el-select>
        </div>
      </div>
    </el-card>

    <div v-if="!currentCompanyId" class="empty-state">
      <el-empty description="请选择企业以查看智能画像分析" />
    </div>

    <template v-else>
      <el-row :gutter="20" class="chart-row">
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>专利能力雷达分析</span>
                <el-select 
                  v-model="radarTimeRange" 
                  placeholder="时间范围" 
                  size="small"
                  @change="loadRadarData"
                >
                  <el-option label="近1年" value="1_year" />
                  <el-option label="近3年" value="3_years" />
                  <el-option label="近5年" value="5_years" />
                  <el-option label="全部" value="all" />
                </el-select>
              </div>
            </template>
            <div ref="radarChartRef" class="chart-container radar-chart"></div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>专利类型分布</span>
              </div>
            </template>
            <div ref="pieChartRef" class="chart-container pie-chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="24">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>专利趋势分析</span>
                <div class="chart-actions">
                  <el-radio-group v-model="trendType" size="small" @change="loadTrendData">
                    <el-radio-button label="application">申请量</el-radio-button>
                    <el-radio-button label="granted">授权量</el-radio-button>
                    <el-radio-button label="valid">有效专利</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
            </template>
            <div ref="trendChartRef" class="chart-container trend-chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>未来趋势预测</span>
                <span class="info-tag">AI预测</span>
              </div>
            </template>
            <div ref="forecastChartRef" class="chart-container forecast-chart"></div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>智能标签分析</span>
                <span class="info-tag">NLP提取</span>
              </div>
            </template>
            <div ref="tagCloudRef" class="chart-container tag-cloud"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="24">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>行业对比分析</span>
                <el-select 
                  v-model="selectedIndustry" 
                  placeholder="选择行业" 
                  size="small"
                  @change="loadIndustryCompareData"
                >
                  <el-option 
                    v-for="item in industryList" 
                    :key="item.code" 
                    :label="item.name" 
                    :value="item.code"
                  />
                </el-select>
              </div>
            </template>
            <div ref="industryCompareChartRef" class="chart-container industry-chart"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <div class="action-footer">
        <el-button type="primary" @click="showExportDialog">导出分析报告</el-button>
      </div>

      <!-- 导出报告对话框 -->
      <el-dialog 
        v-model="exportDialogVisible" 
        title="导出分析报告" 
        width="500px"
        append-to-body
      >
        <el-form :model="exportForm" label-width="120px">
          <el-form-item label="报告标题">
            <el-input v-model="exportForm.title" placeholder="请输入报告标题" />
          </el-form-item>
          <el-form-item label="导出格式">
            <el-radio-group v-model="exportForm.format">
              <el-radio label="pdf">PDF文档</el-radio>
              <el-radio label="docx">Word文档</el-radio>
              <el-radio label="xlsx">Excel表格</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="导出内容">
            <el-checkbox-group v-model="exportForm.sections">
              <el-checkbox label="radar">能力雷达</el-checkbox>
              <el-checkbox label="category">类型分布</el-checkbox>
              <el-checkbox label="trend">趋势分析</el-checkbox>
              <el-checkbox label="forecast">预测分析</el-checkbox>
              <el-checkbox label="tags">标签分析</el-checkbox>
              <el-checkbox label="industry">行业对比</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="附加说明">
            <el-input 
              v-model="exportForm.remarks" 
              type="textarea" 
              :rows="3" 
              placeholder="请输入附加说明内容（可选）"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="exportDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleExportReport" :loading="exporting">
              导出报告
            </el-button>
          </span>
        </template>
      </el-dialog>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, onUnmounted, reactive } from 'vue'
import * as echarts from 'echarts'
import 'echarts-wordcloud'
import { ElMessage, ElNotification } from 'element-plus'
import {
  getCompanyList,
  getIndustryList,
  getCompanyRadar,
  getPatentCategories,
  getPatentTrend,
  getPatentForecast,
  getTechKeywords,
  getIndustryCompare,
  exportReport
} from '@/api/portrait'

// 响应式数据
const companyList = ref([])
const industryList = ref([])
const currentCompanyId = ref('')
const loading = ref(false)
const exporting = ref(false)

// 图表相关配置
const radarTimeRange = ref('3_years')
const trendType = ref('application')
const selectedIndustry = ref('')

// 图表DOM引用
const radarChartRef = ref<HTMLElement | null>(null)
const pieChartRef = ref<HTMLElement | null>(null)
const trendChartRef = ref<HTMLElement | null>(null)
const forecastChartRef = ref<HTMLElement | null>(null)
const tagCloudRef = ref<HTMLElement | null>(null)
const industryCompareChartRef = ref<HTMLElement | null>(null)

// 图表实例
const charts = {
  radar: null as echarts.ECharts | null,
  pie: null as echarts.ECharts | null,
  trend: null as echarts.ECharts | null,
  forecast: null as echarts.ECharts | null,
  tagCloud: null as echarts.ECharts | null,
  industryCompare: null as echarts.ECharts | null
}

// 导出报告对话框
const exportDialogVisible = ref(false)
const exportForm = reactive({
  title: '',
  format: 'pdf',
  sections: ['radar', 'category', 'trend', 'forecast', 'tags', 'industry'],
  remarks: ''
})

// 初始化
onMounted(async () => {
  initCharts()
  await fetchInitialData()
  window.addEventListener('resize', handleResize)
})

// 组件销毁时清理
onUnmounted(() => {
  disposeCharts()
  window.removeEventListener('resize', handleResize)
})

// 获取初始数据
const fetchInitialData = async () => {
  try {
    // 获取企业列表
    const companyData = await getCompanyList()
    companyList.value = companyData.data || []

    // 获取行业列表
    const industryData = await getIndustryList()
    industryList.value = industryData.data || []
    
    if (industryList.value.length > 0) {
      selectedIndustry.value = industryList.value[0].code
    }
  } catch (error) {
    console.error('获取初始数据失败:', error)
    ElMessage.error('获取初始数据失败，请刷新页面重试')
  }
}

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    // 初始化各个图表
    if (radarChartRef.value) {
      charts.radar = echarts.init(radarChartRef.value)
    }
    if (pieChartRef.value) {
      charts.pie = echarts.init(pieChartRef.value)
    }
    if (trendChartRef.value) {
      charts.trend = echarts.init(trendChartRef.value)
    }
    if (forecastChartRef.value) {
      charts.forecast = echarts.init(forecastChartRef.value)
    }
    if (tagCloudRef.value) {
      charts.tagCloud = echarts.init(tagCloudRef.value)
    }
    if (industryCompareChartRef.value) {
      charts.industryCompare = echarts.init(industryCompareChartRef.value)
    }
  })
}

// 处理窗口大小变化
const handleResize = () => {
  Object.values(charts).forEach(chart => {
    chart && chart.resize()
  })
}

// 清理图表实例
const disposeCharts = () => {
  Object.values(charts).forEach(chart => {
    chart && chart.dispose()
  })
}

// 加载企业数据
const loadCompanyData = async () => {
  if (!currentCompanyId.value) return
  
  try {
    loading.value = true
    exportForm.title = companyList.value.find(item => item.id === currentCompanyId.value)?.name + '专利能力分析报告'
    
    // 并行加载各模块数据
    await Promise.all([
      loadRadarData(),
      loadCategoryData(),
      loadTrendData(),
      loadForecastData(),
      loadTagCloudData(),
      loadIndustryCompareData()
    ])
    
    ElNotification({
      title: '数据加载完成',
      message: '企业专利能力智能画像已生成',
      type: 'success'
    })
  } catch (error) {
    console.error('加载企业数据失败:', error)
    ElMessage.error('数据加载失败，请重试')
  } finally {
    loading.value = false
  }
}

// 加载雷达图数据
const loadRadarData = async () => {
  if (!currentCompanyId.value) return
  
  try {
    const res = await getCompanyRadar(currentCompanyId.value, radarTimeRange.value)
    renderRadarChart(res.data)
  } catch (error) {
    console.error('加载雷达图数据失败:', error)
  }
}

// 加载专利类型分布数据
const loadCategoryData = async () => {
  if (!currentCompanyId.value) return
  
  try {
    const res = await getPatentCategories(currentCompanyId.value)
    renderPieChart(res.data)
  } catch (error) {
    console.error('加载专利类型数据失败:', error)
  }
}

// 加载趋势图数据
const loadTrendData = async () => {
  if (!currentCompanyId.value) return
  
  try {
    const res = await getPatentTrend(currentCompanyId.value, trendType.value)
    renderTrendChart(res.data)
  } catch (error) {
    console.error('加载趋势数据失败:', error)
  }
}

// 加载预测数据
const loadForecastData = async () => {
  if (!currentCompanyId.value) return
  
  try {
    const res = await getPatentForecast(currentCompanyId.value)
    renderForecastChart(res.data)
  } catch (error) {
    console.error('加载预测数据失败:', error)
  }
}

// 加载标签云数据
const loadTagCloudData = async () => {
  if (!currentCompanyId.value) return
  
  try {
    const res = await getTechKeywords(currentCompanyId.value)
    renderTagCloud(res.data)
  } catch (error) {
    console.error('加载标签云数据失败:', error)
  }
}

// 加载行业对比数据
const loadIndustryCompareData = async () => {
  if (!currentCompanyId.value || !selectedIndustry.value) return
  
  try {
    const res = await getIndustryCompare(currentCompanyId.value, selectedIndustry.value)
    renderIndustryCompareChart(res.data)
  } catch (error) {
    console.error('加载行业对比数据失败:', error)
  }
}

// 渲染雷达图
const renderRadarChart = (data: any) => {
  if (!charts.radar) return
  
  const option = {
    color: ['#5470c6', '#91cc75'],
    tooltip: {
      trigger: 'item'
    },
    legend: {
      data: [data.companyName, '行业平均'],
      bottom: 0
    },
    radar: {
      indicator: [
        { name: '技术广度', max: 100 },
        { name: '专利质量', max: 100 },
        { name: '创新活跃度', max: 100 },
        { name: '保护强度', max: 100 },
        { name: '技术深度', max: 100 },
        { name: '国际化程度', max: 100 }
      ],
      radius: '65%',
      axisName: {
        color: '#333',
        fontSize: 12
      }
    },
    series: [
      {
        name: '企业与行业对比',
        type: 'radar',
        data: [
          {
            value: data.companyScore,
            name: data.companyName,
            areaStyle: {
              opacity: 0.3
            },
            lineStyle: {
              width: 2
            }
          },
          {
            value: data.industryAvg,
            name: '行业平均',
            areaStyle: {
              opacity: 0.3
            },
            lineStyle: {
              width: 2
            }
          }
        ]
      }
    ]
  }
  
  charts.radar.setOption(option)
}

// 渲染饼图
const renderPieChart = (data: any) => {
  if (!charts.pie) return
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: data.map((item: any) => item.name)
    },
    series: [
      {
        name: '专利类型分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
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
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  }
  
  charts.pie.setOption(option)
}

// 渲染趋势图
const renderTrendChart = (data: any) => {
  if (!charts.trend) return
  
  const typeNames = {
    application: '申请量',
    granted: '授权量',
    valid: '有效专利'
  }
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: data.xAxis,
      boundaryGap: false
    },
    yAxis: {
      type: 'value',
      name: typeNames[trendType.value as keyof typeof typeNames]
    },
    series: [
      {
        name: typeNames[trendType.value as keyof typeof typeNames],
        type: 'line',
        data: data.series,
        areaStyle: {
          opacity: 0.3
        },
        smooth: true,
        markPoint: {
          data: [
            { type: 'max', name: '最大值' },
            { type: 'min', name: '最小值' }
          ]
        }
      }
    ]
  }
  
  charts.trend.setOption(option)
}

// 渲染预测图
const renderForecastChart = (data: any) => {
  if (!charts.forecast) return
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['历史数据', '预测数据'],
      bottom: 0
    },
    xAxis: {
      type: 'category',
      data: data.xAxis,
      boundaryGap: false
    },
    yAxis: {
      type: 'value',
      name: '专利数量'
    },
    series: [
      {
        name: '历史数据',
        type: 'line',
        data: data.history,
        smooth: true
      },
      {
        name: '预测数据',
        type: 'line',
        data: data.forecast,
        smooth: true,
        lineStyle: {
          type: 'dashed'
        },
        itemStyle: {
          color: '#e66'
        }
      }
    ]
  }
  
  charts.forecast.setOption(option)
}

// 渲染标签云
const renderTagCloud = (data: any) => {
  if (!charts.tagCloud) return
  
  const option = {
    tooltip: {
      show: true
    },
    series: [{
      type: 'wordCloud',
      shape: 'circle',
      left: 'center',
      top: 'center',
      width: '80%',
      height: '80%',
      right: null,
      bottom: null,
      sizeRange: [12, 45],
      rotationRange: [-45, 45],
      rotationStep: 15,
      gridSize: 8,
      drawOutOfBound: false,
      textStyle: {
        fontFamily: 'sans-serif',
        fontWeight: 'normal',
        color: function() {
          return 'rgb(' + [
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160)
          ].join(',') + ')'
        }
      },
      emphasis: {
        textStyle: {
          shadowBlur: 10,
          shadowColor: '#333'
        }
      },
      data: data
    }]
  }
  
  charts.tagCloud.setOption(option)
}

// 渲染行业对比
const renderIndustryCompareChart = (data: any) => {
  if (!charts.industryCompare) return
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: [data.companyName, data.industryName],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.dimensions,
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      name: '分值',
      max: 100
    },
    series: [
      {
        name: data.companyName,
        type: 'bar',
        data: data.companyData
      },
      {
        name: data.industryName,
        type: 'bar',
        data: data.industryData
      }
    ]
  }
  
  charts.industryCompare.setOption(option)
}

// 显示导出对话框
const showExportDialog = () => {
  if (!currentCompanyId.value) {
    ElMessage.warning('请先选择企业')
    return
  }
  
  exportDialogVisible.value = true
}

// 导出报告
const handleExportReport = async () => {
  if (!currentCompanyId.value) {
    ElMessage.warning('请先选择企业')
    return
  }
  
  if (!exportForm.title.trim()) {
    ElMessage.warning('请输入报告标题')
    return
  }
  
  if (exportForm.sections.length === 0) {
    ElMessage.warning('请至少选择一项导出内容')
    return
  }
  
  try {
    exporting.value = true
    
    const params = {
      title: exportForm.title,
      format: exportForm.format,
      sections: exportForm.sections,
      remarks: exportForm.remarks
    }
    
    const response = await exportReport(currentCompanyId.value, params)
    
    // 处理二进制流下载
    const blob = new Blob([response.data], { 
      type: response.headers['content-type'] 
    })
    
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    
    // 设置下载文件名
    const filename = response.headers['content-disposition']
      ? response.headers['content-disposition'].split('filename=')[1]
      : `${exportForm.title}.${exportForm.format}`
    
    link.download = decodeURIComponent(filename)
    link.click()
    URL.revokeObjectURL(link.href)
    
    exportDialogVisible.value = false
    ElMessage.success('报告导出成功')
  } catch (error) {
    console.error('导出报告失败:', error)
    ElMessage.error('报告导出失败，请重试')
  } finally {
    exporting.value = false
  }
}
</script>

<style scoped>
.portrait-container {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  h2 {
    margin: 0;
    font-size: 22px;
    color: #303133;
  }
  
  .subtitle {
    font-size: 14px;
    color: #909399;
    margin-top: 5px;
  }
}

.company-selector {
  width: 250px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  width: 100%;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 350px;
  width: 100%;
}

.tag-cloud {
  height: 350px;
}

.industry-chart {
  height: 400px;
}

.info-tag {
  background-color: #ecf5ff;
  color: #409eff;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.action-footer {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  margin-bottom: 20px;
}

.chart-actions {
  display: flex;
  align-items: center;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

.loading-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.7);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  
  p {
    margin-top: 10px;
    color: #409eff;
    font-size: 14px;
  }
}
</style> 