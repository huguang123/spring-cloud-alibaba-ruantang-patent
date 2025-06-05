<template>
  <div class="company-detail-container">
    <el-card class="header-card">
      <div class="company-header">
        <div class="company-logo">
          <img :src="companyInfo.logo || '/default-company.png'" alt="企业Logo" />
        </div>
        <div class="company-info">
          <h2>{{ companyInfo.name }}</h2>
          <div class="info-row">
            <span class="label">统一社会信用代码：</span>
            <span>{{ companyInfo.code }}</span>
          </div>
          <div class="info-row">
            <span class="label">所属行业：</span>
            <span>{{ companyInfo.industry }}</span>
          </div>
          <div class="info-row">
            <span class="label">企业规模：</span>
            <span>{{ companyInfo.size }}</span>
          </div>
          <div class="info-row">
            <span class="label">成立时间：</span>
            <span>{{ companyInfo.establishedYear }}年</span>
          </div>
        </div>
        <div class="actions">
          <el-button type="primary" @click="handleExportReport">导出画像报告</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>企业能力雷达图</span>
              <el-tooltip content="展示企业在6个维度的能力评估，并与行业平均水平对比">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container">
            <radar-chart :data="radarData" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>专利类型统计</span>
              <el-tooltip content="展示企业各类专利的数量分布">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container">
            <pie-chart :data="patentCategories" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>专利申请趋势及预测</span>
              <el-tooltip content="展示企业历史专利申请趋势及未来12个月预测">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container large">
            <trend-forecast-chart :trend-data="patentTrend" :forecast-data="patentForecast" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>技术关键词云</span>
              <el-tooltip content="基于企业专利数据自动提取的技术关键词">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container">
            <word-cloud :data="techKeywords" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>行业对比分析</span>
              <el-tooltip content="企业核心指标与行业平均水平的对比">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </div>
          </template>
          <div class="chart-container">
            <industry-compare-chart :data="industryCompareData" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="exportDialogVisible" title="导出企业画像报告" width="500px">
      <el-form :model="exportForm" label-width="100px">
        <el-form-item label="报告标题">
          <el-input v-model="exportForm.title" placeholder="请输入报告标题" />
        </el-form-item>
        <el-form-item label="报告格式">
          <el-radio-group v-model="exportForm.format">
            <el-radio label="PDF">PDF</el-radio>
            <el-radio label="WORD">Word</el-radio>
            <el-radio label="PPT">PPT</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="报告内容">
          <el-checkbox-group v-model="exportForm.sections">
            <el-checkbox label="BASIC_INFO">基本信息</el-checkbox>
            <el-checkbox label="CAPABILITY_RADAR">能力雷达图</el-checkbox>
            <el-checkbox label="PATENT_ANALYSIS">专利分析</el-checkbox>
            <el-checkbox label="TECH_KEYWORDS">技术关键词</el-checkbox>
            <el-checkbox label="INDUSTRY_COMPARE">行业对比</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="exportForm.remarks" type="textarea" rows="3" placeholder="可选填写" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="exportDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmExport" :loading="exporting">确认导出</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { QuestionFilled } from '@element-plus/icons-vue'
import type { Company, RadarData, PatentCategory, TrendData, ForecastData, TagItem, IndustryCompareData, ExportReportParams } from '@/types/portrait'
import { getCompanyInfo, getCompanyRadarData, getPatentCategories, getPatentTrend, getPatentForecast, getTechKeywords, getIndustryCompare, exportReport } from '@/api/portrait'
import RadarChart from './components/RadarChart.vue'
import PieChart from './components/PieChart.vue'
import TrendForecastChart from './components/TrendForecastChart.vue'
import WordCloud from './components/WordCloud.vue'
import IndustryCompareChart from './components/IndustryCompareChart.vue'

const route = useRoute()
const companyId = ref<string>(route.params.id as string)

// 企业基本信息
const companyInfo = ref<Company>({
  id: '',
  name: '',
  code: '',
  industry: '',
  location: '',
  size: '',
  establishedYear: 0,
  logo: ''
})

// 各图表数据
const radarData = ref<RadarData>({
  companyName: '',
  companyScore: [],
  industryAvg: []
})
const patentCategories = ref<PatentCategory[]>([])
const patentTrend = ref<TrendData>({
  xAxis: [],
  series: []
})
const patentForecast = ref<ForecastData>({
  historical: [],
  forecast: []
})
const techKeywords = ref<TagItem[]>([])
const industryCompareData = ref<IndustryCompareData>({
  companyName: '',
  industryName: '',
  dimensions: [],
  companyData: [],
  industryData: []
})

// 导出报告相关
const exportDialogVisible = ref(false)
const exporting = ref(false)
const exportForm = reactive<ExportReportParams>({
  title: '',
  format: 'PDF',
  sections: ['BASIC_INFO', 'CAPABILITY_RADAR', 'PATENT_ANALYSIS', 'TECH_KEYWORDS', 'INDUSTRY_COMPARE']
})

// 加载所有数据
onMounted(async () => {
  if (!companyId.value) {
    ElMessage.error('公司ID不能为空')
    return
  }
  
  try {
    // 并行请求各个接口数据
    const [
      companyInfoRes,
      radarDataRes,
      patentCategoriesRes,
      patentTrendRes,
      patentForecastRes,
      techKeywordsRes,
      industryCompareRes
    ] = await Promise.all([
      getCompanyInfo(companyId.value),
      getCompanyRadarData(companyId.value),
      getPatentCategories(companyId.value),
      getPatentTrend(companyId.value),
      getPatentForecast(companyId.value),
      getTechKeywords(companyId.value, { limit: 50 }),
      getIndustryCompare(companyId.value)
    ])

    // 设置数据
    companyInfo.value = companyInfoRes.data
    radarData.value = radarDataRes.data
    patentCategories.value = patentCategoriesRes.data
    patentTrend.value = patentTrendRes.data
    patentForecast.value = patentForecastRes.data
    techKeywords.value = techKeywordsRes.data
    industryCompareData.value = industryCompareRes.data

    // 设置导出报告默认标题
    exportForm.title = `${companyInfo.value.name}企业智能画像报告`
  } catch (error) {
    console.error('加载企业画像数据失败', error)
    ElMessage.error('加载企业画像数据失败')
  }
})

// 打开导出报告弹窗
const handleExportReport = () => {
  exportDialogVisible.value = true
}

// 确认导出报告
const confirmExport = async () => {
  if (!exportForm.title) {
    ElMessage.warning('请输入报告标题')
    return
  }
  if (exportForm.sections.length === 0) {
    ElMessage.warning('请至少选择一个报告内容')
    return
  }

  try {
    exporting.value = true
    const res = await exportReport(companyId.value, exportForm)
    
    // 创建下载链接并触发下载
    const a = document.createElement('a')
    const blob = new Blob([res.data as any])
    const url = window.URL.createObjectURL(blob)
    a.href = url
    a.download = `${exportForm.title}.${exportForm.format.toLowerCase()}`
    a.click()
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('报告导出成功')
    exportDialogVisible.value = false
  } catch (error) {
    console.error('导出报告失败', error)
    ElMessage.error('导出报告失败')
  } finally {
    exporting.value = false
  }
}
</script>

<style scoped>
.company-detail-container {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.company-header {
  display: flex;
  align-items: center;
}

.company-logo {
  width: 100px;
  height: 100px;
  margin-right: 20px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #eaeaea;
}

.company-logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.company-info {
  flex: 1;
}

.company-info h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 22px;
  color: #303133;
}

.info-row {
  margin-bottom: 8px;
  font-size: 14px;
  color: #606266;
}

.label {
  font-weight: bold;
  margin-right: 8px;
}

.actions {
  margin-left: 20px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 100%;
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: bold;
}

.card-header .el-icon {
  margin-left: 8px;
  font-size: 16px;
  color: #909399;
  cursor: help;
}

.chart-container {
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chart-container.large {
  height: 400px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 