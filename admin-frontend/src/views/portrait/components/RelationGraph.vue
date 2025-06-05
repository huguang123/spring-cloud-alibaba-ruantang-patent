<template>
  <div class="relation-graph-container">
    <div ref="graphRef" class="graph-wrapper"></div>
    <div class="graph-controls">
      <el-radio-group v-model="layout" size="small" @change="updateLayout">
        <el-radio-button label="force">力导向图</el-radio-button>
        <el-radio-button label="circular">环形布局</el-radio-button>
      </el-radio-group>
      <el-slider 
        v-model="zoomLevel" 
        :min="50" 
        :max="200" 
        :step="10" 
        :format-tooltip="(val) => `${val}%`"
        @change="handleZoom"
        class="zoom-slider"
      />
    </div>
    <div class="graph-legend">
      <div class="legend-title">图例说明</div>
      <div class="legend-items">
        <div class="legend-item" v-for="(cat, index) in categories" :key="index">
          <span 
            class="legend-color" 
            :style="{ backgroundColor: getCategoryColor(index) }"
          ></span>
          <span class="legend-label">{{ cat.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watchEffect, computed } from 'vue';
import * as echarts from 'echarts';
import type { EChartsOption } from 'echarts';
import type { EnterpriseRelationGraph } from '@/types/portrait';

const props = defineProps<{
  graphData: EnterpriseRelationGraph;
  enterpriseId: string;
  height?: number;
}>();

// DOM引用
const graphRef = ref<HTMLElement | null>(null);
// 图表实例
let chartInstance: echarts.ECharts | null = null;
// 布局方式
const layout = ref('force');
// 缩放级别
const zoomLevel = ref(100);

// 节点分类
const categories = computed(() => 
  props.graphData.categories || [
    { name: '企业' },
    { name: '个人' },
    { name: '专利' },
    { name: '项目' }
  ]
);

// 分类颜色
const categoryColors = [
  '#5470c6', // 企业 - 蓝色
  '#91cc75', // 个人 - 绿色
  '#ee6666', // 专利 - 红色
  '#fac858', // 项目 - 黄色
  '#73c0de', // 备用 - 浅蓝色
  '#9a60b4', // 备用 - 紫色
];

// 获取分类颜色
const getCategoryColor = (index: number) => {
  return categoryColors[index % categoryColors.length];
};

// 计算主体企业在节点中的索引
const mainNodeIndex = computed(() => {
  return props.graphData.nodes.findIndex(node => node.id === props.enterpriseId);
});

/**
 * 初始化图表
 */
const initChart = () => {
  if (!graphRef.value) return;

  // 创建图表实例
  chartInstance = echarts.init(graphRef.value);
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize);
  
  // 渲染图表
  updateChart();
};

/**
 * 更新图表数据
 */
const updateChart = () => {
  if (!chartInstance || !props.graphData) return;
  
  const { nodes, edges } = props.graphData;
  
  // 处理节点数据
  const formattedNodes = nodes.map((node, index) => {
    const isMainNode = node.id === props.enterpriseId;
    const categoryIndex = node.category !== undefined ? node.category : (
      node.type === 'enterprise' ? 0 : 
      node.type === 'person' ? 1 : 
      node.type === 'patent' ? 2 : 3
    );
    
    return {
      ...node,
      name: node.name,
      symbolSize: isMainNode ? 35 : (node.size || 20),
      category: categoryIndex,
      itemStyle: {
        color: isMainNode ? '#ff9900' : undefined,
        borderColor: isMainNode ? '#ff7700' : undefined,
        borderWidth: isMainNode ? 3 : 1
      },
      label: {
        show: true,
        position: 'right',
        formatter: '{b}',
        fontSize: 12,
        color: '#333'
      }
    };
  });
  
  // 配置选项
  const option: EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        if (params.dataType === 'node') {
          return `<div style="font-weight:bold">${params.data.name}</div>
                  <div>类型: ${categories.value[params.data.category]?.name || '未知'}</div>`;
        } else if (params.dataType === 'edge') {
          return `<div>${params.data.label || '关联关系'}</div>
                  <div>强度: ${params.data.value}</div>`;
        }
        return '';
      }
    },
    legend: {
      data: categories.value.map(cat => cat.name),
      orient: 'vertical',
      right: 10,
      top: 20,
      textStyle: {
        color: '#666'
      }
    },
    animationDuration: 1500,
    animationEasingUpdate: 'quinticInOut',
    series: [
      {
        name: '企业关系图谱',
        type: 'graph',
        layout: layout.value,
        data: formattedNodes,
        links: edges.map(edge => ({
          ...edge,
          lineStyle: {
            color: '#999',
            width: Math.max(1, Math.min(edge.value, 5)) // 根据关系强度设置线宽
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: Math.max(2, Math.min(edge.value, 7))
            }
          }
        })),
        categories: categories.value.map((cat, index) => ({
          name: cat.name,
          itemStyle: {
            color: getCategoryColor(index)
          }
        })),
        roam: true,
        focusNodeAdjacency: true,
        // 力导向图配置
        force: {
          repulsion: 200,
          gravity: 0.1,
          edgeLength: 100,
          layoutAnimation: true
        },
        // 环形布局配置
        circular: {
          rotateLabel: true
        },
        // 节点样式
        itemStyle: {
          borderWidth: 1,
          borderColor: '#fff',
          shadowColor: 'rgba(0, 0, 0, 0.3)',
          shadowBlur: 2
        },
        lineStyle: {
          color: '#aaa',
          width: 1,
          curveness: 0.3,
          opacity: 0.7
        },
        emphasis: {
          scale: true,
          focus: 'adjacency'
        }
      }
    ]
  };
  
  // 设置初始缩放和中心
  if (mainNodeIndex.value >= 0) {
    option.series = option.series || [];
    if (Array.isArray(option.series)) {
      option.series[0].center = ['40%', '50%'];
      if (layout.value === 'force') {
        option.series[0].zoom = 1;
      }
    }
  }
  
  // 渲染图表
  chartInstance.setOption(option);
  
  // 自动聚焦到主体企业
  if (mainNodeIndex.value >= 0) {
    setTimeout(() => {
      chartInstance?.dispatchAction({
        type: 'focusNodeAdjacency',
        seriesIndex: 0,
        dataIndex: mainNodeIndex.value
      });
    }, 1000);
  }
};

/**
 * 更新布局方式
 */
const updateLayout = () => {
  if (!chartInstance) return;
  
  chartInstance.setOption({
    series: [{
      layout: layout.value
    }]
  });
};

/**
 * 处理缩放
 */
const handleZoom = (value: number) => {
  if (!chartInstance) return;
  
  chartInstance.setOption({
    series: [{
      zoom: value / 100
    }]
  });
};

/**
 * 处理窗口大小变化
 */
const handleResize = () => {
  chartInstance?.resize();
};

// 监听数据变化
watchEffect(() => {
  if (props.graphData && chartInstance) {
    updateChart();
  }
});

// 组件挂载完成后初始化图表
onMounted(() => {
  initChart();
});

// 组件销毁前清理资源
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  chartInstance?.dispose();
  chartInstance = null;
});
</script>

<style scoped>
.relation-graph-container {
  position: relative;
  width: 100%;
  height: v-bind('props.height ? `${props.height}px` : "600px"');
  border-radius: 4px;
  overflow: hidden;
}

.graph-wrapper {
  width: 100%;
  height: 100%;
}

.graph-controls {
  position: absolute;
  top: 10px;
  left: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: rgba(255, 255, 255, 0.8);
  padding: 10px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.zoom-slider {
  width: 150px;
  margin-top: 10px;
}

.graph-legend {
  position: absolute;
  bottom: 20px;
  left: 20px;
  background: rgba(255, 255, 255, 0.8);
  padding: 10px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.legend-title {
  font-weight: bold;
  margin-bottom: 8px;
}

.legend-items {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.legend-color {
  display: block;
  width: 12px;
  height: 12px;
  border-radius: 2px;
}

.legend-label {
  font-size: 12px;
  color: #666;
}
</style> 