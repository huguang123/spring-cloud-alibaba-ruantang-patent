<template>
  <div class="radar-chart-container">
    <div ref="radarChartRef" class="radar-chart"></div>
    <div v-if="loading" class="chart-loading">
      <el-skeleton animated :rows="5" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';
import type { EChartsOption } from 'echarts';
import type { RadarChartData } from '@/types/portrait';

const props = defineProps<{
  chartData: RadarChartData;
  height?: number;
  loading?: boolean;
}>();

// 图表DOM引用
const radarChartRef = ref<HTMLElement | null>(null);
// 图表实例
let chartInstance: echarts.ECharts | null = null;
// 加载状态
const loading = ref(props.loading || false);

/**
 * 初始化图表
 */
const initChart = () => {
  if (!radarChartRef.value) return;
  
  // 确保DOM已加载
  nextTick(() => {
    // 销毁可能存在的旧实例
    if (chartInstance) {
      chartInstance.dispose();
    }
    
    // 创建新的图表实例
    chartInstance = echarts.init(radarChartRef.value);
    // 设置响应式
    window.addEventListener('resize', () => {
      chartInstance?.resize();
    });
    
    // 渲染图表
    updateChart();
  });
};

/**
 * 更新图表数据
 */
const updateChart = () => {
  if (!chartInstance || !props.chartData) return;
  
  const { indicators, series } = props.chartData;
  
  // 配置雷达图
  const option: EChartsOption = {
    color: ['#5470c6', '#91cc75', '#ee6666'],
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        const name = params.name;
        const values = params.value;
        const indicators = props.chartData.indicators;
        
        let htmlStr = `<div style="font-weight:bold">${name}</div>`;
        
        values.forEach((value: number, index: number) => {
          if (indicators[index]) {
            htmlStr += `<div>${indicators[index].name}: ${value}</div>`;
          }
        });
        
        return htmlStr;
      }
    },
    legend: {
      data: series.map(item => item.name),
      bottom: 0
    },
    radar: {
      indicator: indicators,
      splitNumber: 5,
      axisName: {
        color: '#666',
        fontSize: 12
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(255, 255, 255, 0.5)'],
          shadowColor: 'rgba(0, 0, 0, 0.05)',
          shadowBlur: 10
        }
      },
      axisLine: {
        show: true
      },
      splitLine: {
        show: true,
        lineStyle: {
          width: 1,
          color: '#E0E6F1'
        }
      }
    },
    series: [
      {
        type: 'radar',
        name: series[0]?.name || '企业分数',
        data: series.map(item => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: item.color
          },
          areaStyle: {
            opacity: 0.3
          }
        }))
      }
    ]
  };
  
  // 使用加载效果
  loading.value ? chartInstance.showLoading() : chartInstance.hideLoading();
  
  // 渲染图表
  chartInstance.setOption(option);
};

// 监听数据变化
watch(() => props.chartData, () => {
  updateChart();
}, { deep: true });

// 监听加载状态
watch(() => props.loading, (val) => {
  loading.value = val;
  if (chartInstance) {
    val ? chartInstance.showLoading() : chartInstance.hideLoading();
  }
});

// 组件挂载完成后初始化图表
onMounted(() => {
  initChart();
});
</script>

<style scoped>
.radar-chart-container {
  position: relative;
  width: 100%;
  height: v-bind('props.height ? `${props.height}px` : "400px"');
}

.radar-chart {
  width: 100%;
  height: 100%;
}

.chart-loading {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.7);
}
</style> 