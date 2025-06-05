<template>
  <div class="system-template-type-selector mb-4">
    <el-tabs 
      :model-value="activeTemplateType" 
      type="card" 
      @tab-change="onTabChange"
    >
      <el-tab-pane
        v-for="template in templateTypes"
        :key="template.type"
        :label="template.name"
        :name="template.type"
      />
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ElTabs, ElTabPane } from 'element-plus';
import { ref, watch, computed, onMounted } from 'vue';

// 定义模板类型映射关系
const templateTypes = ref([
  { type: 'platform', name: '平台超级租户模板', apiType: 1, templateCode: 'SYS_PLATFORM_DEFAULT', description: '平台超级管理员使用的权限模板' },
  { type: 'enterprise', name: '标准企业模板套件', apiType: 2, templateCode: 'SYS_ENT_DEFAULT', description: '新企业租户默认分配的权限模板' },
  { type: 'agent', name: '标准代理所模板套件', apiType: 3, templateCode: 'SYS_AGENT_DEFAULT', description: '新代理所租户默认分配的权限模板' },
]);

const props = defineProps({
  activeTemplateType: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(['update:activeTemplateType', 'type-selected']);

const onTabChange = (name: string | number) => {
  const selectedType = name as string;
  emit('update:activeTemplateType', selectedType);
  const selectedTemplate = templateTypes.value.find(t => t.type === selectedType);
  if (selectedTemplate) {
    emit('type-selected', selectedTemplate);
  }
};

// 初始化时，通知父组件当前选中的模板类型
onMounted(() => {
  const selectedTemplate = templateTypes.value.find(t => t.type === props.activeTemplateType);
  if (selectedTemplate) {
    emit('type-selected', selectedTemplate);
  }
});
</script>

<style scoped>
/* Styles for the selector if needed */
</style> 