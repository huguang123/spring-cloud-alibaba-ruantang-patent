<template>
  <div :id="chartId" :style="{ width: width, height: height }" class="radar-chart"></div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import type { Ref } from 'vue';
import * as echarts from 'echarts/core';
import { RadarChart } from 'echarts/charts';
import {
  TitleComponent,
  LegendComponent,
  TooltipComponent,
  GridComponent,
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { RadarChartData } from '@/types/portrait';

echarts.use([
  TitleComponent,
  LegendComponent,
  TooltipComponent,
  GridComponent,
  RadarChart,
  CanvasRenderer,
]);

const props = defineProps({
  data: {
    type: Object as () => RadarChartData,
    required: true,
  },
  title: {
    type: String,
    default: '企业能力雷达图',
  },
  height: {
    type: String,
    default: '400px',
  },
  width: {
    type: String,
    default: '100%',
  },
  theme: {
    type: String,
    default: '',
  },
  showLegend: {
    type: Boolean,
    default: true,
  },
  colorList: {
    type: Array as () => string[],
    default: () => ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de'],
  },
});

const chartInstance: Ref<echarts.ECharts | null> = ref(null);
const chartId = computed(() => `radar-chart-${Date.now()}`);

const initChart = () => {
  const chartDom = document.getElementById(chartId.value);
  if (!chartDom) return;

  chartInstance.value = echarts.init(chartDom, props.theme);
  updateChart();
};

const updateChart = () => {
  if (!chartInstance.value || !props.data) return;

  const option = {
    title: {
      text: props.title,
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      show: props.showLegend,
      orient: 'vertical',
      right: '5%',
      top: 'middle',
    },
    radar: {
      indicator: props.data.indicators,
      splitNumber: 5,
      axisName: {
        color: '#999',
        fontSize: 12,
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(255, 255, 255, 0.5)'],
        },
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(0, 0, 0, 0.1)',
        },
      },
      axisLine: {
        lineStyle: {
          color: 'rgba(0, 0, 0, 0.1)',
        },
      },
    },
    series: [
      {
        type: 'radar',
        data: props.data.series.map((item, index) => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: item.color || props.colorList[index % props.colorList.length],
          },
          lineStyle: {
            width: 2,
          },
          areaStyle: {
            opacity: 0.3,
          },
        })),
      },
    ],
  };

  chartInstance.value.setOption(option);
};

const handleResize = () => {
  chartInstance.value?.resize();
};

onMounted(() => {
  initChart();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  chartInstance.value?.dispose();
});

watch(
  () => props.data,
  () => {
    updateChart();
  },
  { deep: true }
);

// 暴露方法供父组件调用
const refreshChart = () => {
  if (chartInstance.value) {
    chartInstance.value.resize();
    updateChart();
  }
};

defineExpose({
  chartInstance,
  refreshChart,
});
</script>

<style scoped>
.radar-chart {
  margin: 0 auto;
}
</style> 