<template>
  <div class="dashboard-container">
    <!-- 概览数据 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <el-card 
        v-for="(item, index) in statisticsData" 
        :key="index"
        class="dashboard-card"
      >
        <div class="flex items-center">
          <div class="rounded-lg p-3 mr-4" :class="`bg-${item.color}-light`">
            <i :class="`fas ${item.icon} text-2xl text-${item.color}`"></i>
          </div>
          <div>
            <h3 class="text-sm text-gray-500">{{ item.title }}</h3>
            <div class="text-2xl font-bold">{{ item.value }}</div>
          </div>
        </div>
        <div class="flex items-center mt-4">
          <span :class="`text-${item.trend === 'up' ? 'success' : 'danger'}`">
            <i :class="`fas fa-arrow-${item.trend} mr-1`"></i>
            {{ item.rate }}
          </span>
          <span class="text-gray-500 text-sm ml-2">与上月相比</span>
        </div>
      </el-card>
    </div>
    
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-4 mb-6">
      <!-- 任务概览 -->
      <el-card class="col-span-1">
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold">待办任务</span>
            <el-button type="text">查看全部</el-button>
          </div>
        </template>
        <div class="task-list">
          <div 
            v-for="(task, index) in taskList" 
            :key="index"
            class="task-item p-3 mb-3 border border-gray-100 rounded hover:border-primary transition-all cursor-pointer"
          >
            <div class="flex justify-between">
              <span class="font-medium">{{ task.title }}</span>
              <el-tag :type="getTaskPriorityType(task.priority)" size="small">
                {{ task.priority }}
              </el-tag>
            </div>
            <div class="text-gray-500 text-sm mt-2">{{ task.description }}</div>
            <div class="flex justify-between mt-3">
              <span class="text-xs text-gray-400">截止日期: {{ task.deadline }}</span>
              <span class="text-xs text-gray-400">{{ task.assignee }}</span>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 企业能力雷达图 -->
      <el-card class="col-span-2">
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold">企业能力分析</span>
            <el-radio-group v-model="selectedCompany" size="small">
              <el-radio-button label="company1">企业A</el-radio-button>
              <el-radio-button label="company2">企业B</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div ref="radarChartRef" class="w-full h-80"></div>
      </el-card>
    </div>
    
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
      <!-- 专利申请趋势 -->
      <el-card>
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold">专利申请趋势</span>
            <el-select v-model="patentTimeRange" size="small" placeholder="选择时间范围">
              <el-option label="最近7天" value="7d" />
              <el-option label="最近30天" value="30d" />
              <el-option label="最近90天" value="90d" />
              <el-option label="最近一年" value="1y" />
            </el-select>
          </div>
        </template>
        <div ref="patentChartRef" class="w-full h-80"></div>
      </el-card>
      
      <!-- 最新动态 -->
      <el-card>
        <template #header>
          <div class="flex justify-between items-center">
            <span class="font-bold">系统动态</span>
            <el-button type="text">查看更多</el-button>
          </div>
        </template>
        <div class="relative">
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activityList"
              :key="index"
              :type="activity.type"
              :color="getActivityColor(activity.type)"
              :timestamp="activity.time"
            >
              <div class="font-medium">{{ activity.title }}</div>
              <div class="text-sm text-gray-500">{{ activity.content }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

// 数据统计
const statisticsData = [
  {
    title: '专利总数',
    value: '1,583',
    icon: 'fa-file-alt',
    color: 'primary',
    trend: 'up',
    rate: '8.2%'
  },
  {
    title: '本月申请数',
    value: '142',
    icon: 'fa-file-signature',
    color: 'success',
    trend: 'up',
    rate: '5.3%'
  },
  {
    title: '授权数量',
    value: '958',
    icon: 'fa-award',
    color: 'warning',
    trend: 'up',
    rate: '3.1%'
  },
  {
    title: '审核中',
    value: '287',
    icon: 'fa-hourglass-half',
    color: 'info',
    trend: 'down',
    rate: '2.5%'
  }
]

// 任务列表
const taskList = [
  {
    title: '企业A专利审核',
    description: '需要审核企业A提交的5项发明专利申请',
    priority: '高',
    deadline: '2023-08-15',
    assignee: '张工'
  },
  {
    title: '更新模板库',
    description: '根据最新法规更新技术交底书模板库',
    priority: '中',
    deadline: '2023-08-20',
    assignee: '李工'
  },
  {
    title: '系统升级',
    description: '预定于本周五进行系统维护升级',
    priority: '低',
    deadline: '2023-08-18',
    assignee: '系统管理员'
  }
]

// 活动列表
const activityList = [
  {
    title: '企业B注册成功',
    content: '企业B已完成注册，资质审核通过',
    time: '2023-08-10 15:30',
    type: 'success'
  },
  {
    title: '专利申请提交',
    content: '企业A提交了3项新的发明专利申请',
    time: '2023-08-10 13:45',
    type: 'primary'
  },
  {
    title: '系统更新',
    content: '系统完成版本更新至v2.3.1',
    time: '2023-08-09 23:30',
    type: 'info'
  },
  {
    title: '专利授权',
    content: '企业C的2项专利获得授权',
    time: '2023-08-09 10:15',
    type: 'warning'
  }
]

// 雷达图相关
const radarChartRef = ref<HTMLElement | null>(null)
const selectedCompany = ref('company1')
let radarChart: echarts.ECharts | null = null

// 专利趋势图相关
const patentChartRef = ref<HTMLElement | null>(null)
const patentTimeRange = ref('30d')
let patentChart: echarts.ECharts | null = null

// 初始化图表
onMounted(() => {
  // 初始化雷达图
  radarChart = echarts.init(radarChartRef.value!)
  renderRadarChart()
  
  // 初始化专利趋势图
  patentChart = echarts.init(patentChartRef.value!)
  renderPatentChart()
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  // 销毁图表
  radarChart?.dispose()
  patentChart?.dispose()
  
  // 移除事件监听
  window.removeEventListener('resize', handleResize)
})

// 窗口大小变化时重新渲染图表
function handleResize() {
  radarChart?.resize()
  patentChart?.resize()
}

// 企业能力雷达图配置
function renderRadarChart() {
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      data: ['企业A', '行业平均'],
      bottom: 0
    },
    radar: {
      indicator: [
        { name: '技术储备', max: 100 },
        { name: '研发投入', max: 100 },
        { name: '专利质量', max: 100 },
        { name: '创新能力', max: 100 },
        { name: '转化效率', max: 100 },
        { name: '知识产权保护', max: 100 }
      ]
    },
    series: [
      {
        type: 'radar',
        data: [
          {
            value: [85, 90, 78, 82, 75, 92],
            name: '企业A',
            itemStyle: {
              color: '#409EFF'
            },
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.2)'
            }
          },
          {
            value: [65, 60, 72, 65, 70, 68],
            name: '行业平均',
            itemStyle: {
              color: '#909399'
            },
            areaStyle: {
              color: 'rgba(144, 147, 153, 0.2)'
            }
          }
        ]
      }
    ]
  }
  
  radarChart?.setOption(option)
}

// 专利申请趋势图配置
function renderPatentChart() {
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['发明专利', '实用新型', '外观设计'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '发明专利',
        type: 'line',
        stack: 'Total',
        data: [35, 42, 38, 45, 52, 48, 55, 62],
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#409EFF'
        },
        areaStyle: {
          color: 'rgba(64, 158, 255, 0.1)'
        }
      },
      {
        name: '实用新型',
        type: 'line',
        stack: 'Total',
        data: [28, 32, 30, 35, 42, 38, 40, 45],
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#67C23A'
        },
        areaStyle: {
          color: 'rgba(103, 194, 58, 0.1)'
        }
      },
      {
        name: '外观设计',
        type: 'line',
        stack: 'Total',
        data: [15, 18, 16, 20, 25, 22, 28, 35],
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#E6A23C'
        },
        areaStyle: {
          color: 'rgba(230, 162, 60, 0.1)'
        }
      }
    ]
  }
  
  patentChart?.setOption(option)
}

// 获取任务优先级对应的标签类型
function getTaskPriorityType(priority: string): string {
  switch (priority) {
    case '高':
      return 'danger'
    case '中':
      return 'warning'
    case '低':
      return 'info'
    default:
      return 'info'
  }
}

// 获取活动类型对应的颜色
function getActivityColor(type: string): string {
  switch (type) {
    case 'success':
      return '#67C23A'
    case 'warning':
      return '#E6A23C'
    case 'danger':
      return '#F56C6C'
    case 'primary':
      return '#409EFF'
    case 'info':
    default:
      return '#909399'
  }
}
</script>

<style scoped>
.dashboard-container {
  @apply min-h-full;
}

.dashboard-card {
  @apply transition-all duration-300;
}

.dashboard-card:hover {
  @apply shadow-md transform -translate-y-1;
}

.task-item {
  @apply transition-all duration-300;
}

.task-item:hover {
  @apply shadow-sm bg-gray-50;
}

/* 覆盖默认样式 */
:deep(.el-card__header) {
  @apply py-3 px-4;
}
</style> 