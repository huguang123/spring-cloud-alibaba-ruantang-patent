<template>
  <el-card class="role-list-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span>角色列表</span>
        <div>
          <el-input
            v-model="localKeyword"
            placeholder="搜索角色..."
            :prefix-icon="Search"
            clearable
            @input="onSearchInput"
            @clear="onSearchClear"
            style="width: 180px; margin-right: 10px"
          />
          <el-button type="primary" :icon="Plus" @click="handleAddRole">新增角色</el-button>
        </div>
      </div>
    </template>
    <div v-loading="loading" class="role-list-container custom-scrollbar">
      <div
        v-for="role in filteredRoles"
        :key="role.id"
        class="role-item"
        :class="{ 'is-active': selectedRoleId === role.id }"
        @click="handleSelectRole(role)"
      >
        <div class="role-info">
          <span class="role-name">{{ role.rolesName }}</span>
          <span class="role-description">{{ role.rolesDescription || '暂无描述' }}</span>
        </div>
        <el-tag size="small" effect="plain" type="info">平台角色</el-tag>
      </div>
      <el-empty v-if="!loading && filteredRoles.length === 0" description="未找到角色" :image-size="80" />
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, computed, watch, defineProps, defineEmits } from 'vue';
import { ElCard, ElInput, ElButton, ElTag, ElEmpty } from 'element-plus';
import { Search, Plus } from '@element-plus/icons-vue';
import type { PlatformRole } from '@/types/permission';
import { debounce } from 'lodash-es'; // Using lodash for debouncing search

const props = defineProps<{ 
  roles: PlatformRole[];
  loading: boolean;
  selectedRoleId: number | string | null;
}>();

const emit = defineEmits<{ 
  (e: 'select-role', role: PlatformRole): void;
  (e: 'add-role'): void;
  (e: 'search', keyword: string): void;
}>();

const localKeyword = ref('');

const filteredRoles = computed(() => {
  // Filtering is handled by the parent via the search event
  return props.roles;
});

const handleSelectRole = (role: PlatformRole) => {
  emit('select-role', role);
};

const handleAddRole = () => {
  emit('add-role');
};

const debouncedSearch = debounce((keyword: string) => {
  emit('search', keyword);
}, 300); // 300ms delay

const onSearchInput = (value: string | number) => {
  debouncedSearch(value as string);
};

const onSearchClear = () => {
  debouncedSearch('');
};

// Watch external changes to keyword if needed, though usually driven by local input
// watch(() => props.keyword, (newKeyword) => {
//   localKeyword.value = newKeyword || '';
// });

</script>

<style scoped>
.role-list-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.el-card__header) {
  flex-shrink: 0;
  padding: 12px 15px; /* Adjust padding */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-card__body) {
  flex-grow: 1;
  padding: 0; /* Remove body padding */
  overflow: hidden; /* Prevent body overflow */
}

.role-list-container {
  height: 100%;
  overflow-y: auto;
  padding: 10px; /* Add padding inside the scrollable area */
}

.role-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid var(--el-border-color-lighter);
  transition: background-color 0.2s, border-color 0.2s;
}

.role-item:hover {
  background-color: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary-light-7);
}

.role-item.is-active {
  background-color: var(--el-color-primary-light-8);
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}

.role-info {
  display: flex;
  flex-direction: column;
}

.role-name {
  font-weight: 500;
  margin-bottom: 2px;
}

.role-description {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #eee #f5f5f5;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent; /* Make track transparent */
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #ccc;
}
</style> 