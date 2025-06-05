<template>
  <div :id="chartId" :style="{ height, width }" class="pie-chart"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, computed } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'
import type { PieChartData } from '@/types/portrait'

const props = defineProps<{
  data: PieChartData[]
  title?: string
  height?: string
  width?: string
  showLegend?: boolean
  theme?: 'light' | 'dark'
  colorList?: string[]
}>()

// 默认值
const height = props.height || '300px'
const width = props.width || '100%'
const showLegend = props.showLegend !== false
const theme = props.theme || 'light'
const title = props.title || ''

// 默认颜色列表
const defaultColors = [
  '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
  '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#ff9c4d'
]

// 生成唯一ID
const chartId = computed(() => `pie-chart-${Math.random().toString(36).substring(2, 9)}`)

// 图表实例
const chartInstance = ref<echarts.ECharts | null>(null)

// 初始化图表
const initChart = () => {
  const chartDom = document.getElementById(chartId.value)
  if (!chartDom) return
  
  // 根据主题初始化
  chartInstance.value = echarts.init(chartDom, props.theme === 'dark' ? 'dark' : undefined)
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
  
  // 更新图表数据
  updateChart()
}

// 更新图表数据
const updateChart = () => {
  if (!chartInstance.value) return
  
  const colorList = props.colorList || defaultColors
  
  // 处理数据
  const seriesData = props.data.map(item => ({
    name: item.name,
    value: item.value
  }))
  
  // 饼图配置
  const option: EChartsOption = {
    title: props.title ? {
      text: props.title,
      left: 'center',
      top: 0,
      textStyle: {
        fontSize: 14
      }
    } : undefined,
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      show: props.showLegend,
      orient: 'horizontal',
      bottom: 0,
      type: 'scroll',
      itemWidth: 10,
      itemHeight: 10,
      textStyle: {
        fontSize: 12
      }
    },
    color: colorList,
    series: [
      {
        name: title || '数据分布',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', props.showLegend ? '45%' : '50%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 12,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: seriesData
      }
    ]
  }
  
  chartInstance.value.setOption(option)
}

// 监听数据变化更新图表
watch(() => props.data, () => {
  updateChart()
}, { deep: true })

// 窗口大小变化时重置图表大小
const handleResize = () => {
  chartInstance.value?.resize()
}

// 组件挂载时初始化图表
onMounted(() => {
  initChart()
})

// 组件卸载时销毁图表实例
onUnmounted(() => {
  if (chartInstance.value) {
    chartInstance.value.dispose()
    window.removeEventListener('resize', handleResize)
  }
})

// 导出方法
defineExpose({
  // 获取图表实例
  getChartInstance: () => chartInstance.value,
  // 手动触发更新
  refreshChart: () => updateChart()
})
</script>

<style scoped>
.pie-chart {
  width: 100%;
}
</style> 