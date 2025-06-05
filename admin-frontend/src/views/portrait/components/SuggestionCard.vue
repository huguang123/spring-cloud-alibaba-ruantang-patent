<template>
  <el-card class="suggestion-card" shadow="hover">
    <div class="suggestion-header">
      <div class="priority-badge" :class="priorityClass">
        {{ priorityText }}
      </div>
      <h4 class="suggestion-title">
        <i :class="suggestionIcon" class="suggestion-icon"></i>
        {{ suggestion.dimension }}维度优化
      </h4>
    </div>
    
    <div class="suggestion-content">
      {{ suggestion.content }}
    </div>
    
    <div class="suggestion-actions">
      <h5>建议措施：</h5>
      <ul class="action-list">
        <li v-for="(action, index) in suggestion.actions" :key="index">
          <i class="fas fa-check-circle action-icon"></i>
          <span>{{ action }}</span>
        </li>
      </ul>
    </div>
    
    <template #footer v-if="suggestion.impactScore !== undefined">
      <div class="suggestion-footer">
        <div class="impact-score">
          <span class="label">预期提升：</span>
          <el-rate 
            v-model="suggestion.impactScore" 
            disabled 
            :max="5"
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
          />
        </div>
        <div class="difficulty" v-if="suggestion.implementationDifficulty">
          <span class="label">实施难度：</span>
          <el-tag 
            :type="difficultyType" 
            size="small"
          >
            {{ difficultyText }}
          </el-tag>
        </div>
      </div>
    </template>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { Suggestion } from '@/types/portrait';

const props = defineProps<{
  suggestion: Suggestion;
}>();

// 建议图标
const suggestionIcon = computed(() => {
  // 如果有自定义图标就使用自定义图标
  if (props.suggestion.icon) {
    return props.suggestion.icon;
  }
  
  // 根据不同维度设置默认图标
  const dimension = props.suggestion.dimension.toLowerCase();
  
  if (dimension.includes('技术') || dimension.includes('研发')) {
    return 'fas fa-flask';
  } else if (dimension.includes('专利') || dimension.includes('知识产权')) {
    return 'fas fa-scroll';
  } else if (dimension.includes('投资') || dimension.includes('资金')) {
    return 'fas fa-coins';
  } else if (dimension.includes('营销') || dimension.includes('市场')) {
    return 'fas fa-bullhorn';
  } else if (dimension.includes('人才') || dimension.includes('团队')) {
    return 'fas fa-users';
  } else if (dimension.includes('管理') || dimension.includes('组织')) {
    return 'fas fa-sitemap';
  } else {
    return 'fas fa-lightbulb';
  }
});

// 优先级样式类
const priorityClass = computed(() => {
  const priority = props.suggestion.priority;
  return {
    'high': priority === 'high',
    'medium': priority === 'medium',
    'low': priority === 'low'
  };
});

// 优先级文本
const priorityText = computed(() => {
  const priority = props.suggestion.priority;
  return priority === 'high' ? '高优先级' : 
         priority === 'medium' ? '中优先级' : '低优先级';
});

// 难度类型
const difficultyType = computed(() => {
  const difficulty = props.suggestion.implementationDifficulty;
  return difficulty === 'easy' ? 'success' :
         difficulty === 'moderate' ? 'warning' : 'danger';
});

// 难度文本
const difficultyText = computed(() => {
  const difficulty = props.suggestion.implementationDifficulty;
  return difficulty === 'easy' ? '容易' :
         difficulty === 'moderate' ? '中等' : '困难';
});
</script>

<style scoped>
.suggestion-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.suggestion-card:hover {
  transform: translateY(-3px);
}

.suggestion-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.priority-badge {
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  color: white;
  margin-right: 10px;
}

.priority-badge.high {
  background-color: #f56c6c;
}

.priority-badge.medium {
  background-color: #e6a23c;
}

.priority-badge.low {
  background-color: #67c23a;
}

.suggestion-title {
  margin: 0;
  flex: 1;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
}

.suggestion-icon {
  margin-right: 8px;
  color: #409EFF;
}

.suggestion-content {
  margin-bottom: 16px;
  color: #606266;
  line-height: 1.6;
}

.suggestion-actions h5 {
  margin-top: 0;
  margin-bottom: 10px;
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.action-list {
  padding-left: 10px;
  margin: 0;
}

.action-list li {
  list-style: none;
  margin-bottom: 8px;
  display: flex;
  align-items: flex-start;
}

.action-icon {
  color: #67c23a;
  margin-right: 8px;
  font-size: 14px;
  margin-top: 3px;
}

.suggestion-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.impact-score, .difficulty {
  display: flex;
  align-items: center;
}

.label {
  margin-right: 8px;
  font-size: 13px;
  color: #606266;
}
</style> 