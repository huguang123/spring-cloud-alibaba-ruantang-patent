<template>
  <el-card class="permission-config-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span v-if="currentRoleInfo">配置角色权限: <strong>{{ currentRoleInfo.name }}</strong></span>
        <span v-else>请先选择一个角色或等待数据加载</span>
      </div>
    </template>

    <div v-if="currentRoleInfo" v-loading="loading" class="config-content">
      <div class="role-details">
        <p><strong>角色类型:</strong> <el-tag size="small" effect="plain">{{ RoleTypeMap[currentRoleInfo.type as RoleType] || '未知类型' }}</el-tag></p>
      </div>

      <el-divider />

      <div class="permission-section">
         <div class="permission-header">
           <h4>权限分配</h4>
         </div>

         <div class="permission-controls">
            <el-radio-group v-model="activeTab" style="margin-bottom: 15px;">
              <el-radio-button label="功能权限" value="functional" />
              <el-radio-button label="数据权限" value="data" />
            </el-radio-group>
            
            <div class="filter-save-container">
              <el-input
                v-model="filterText"
                placeholder="筛选权限节点..."
                clearable
                :prefix-icon="Search"
                class="filter-input"
              />
              <el-button 
                type="primary" 
                :icon="Check" 
                @click="handleSavePermissions" 
                :disabled="loading || (activeTab === 'functional' ? filteredFunctionalPermissions.length === 0 : filteredDataPermissions.length === 0)"
                class="save-button"
               >
                 保存配置
               </el-button>
            </div>
         </div>

        <div v-show="activeTab === 'functional'" class="modules-container custom-scrollbar">
          <template v-if="filteredFunctionalPermissions.length > 0">
            <div v-for="module in filteredFunctionalPermissions" :key="module.id" class="module-block">
              <div class="module-header">
                <el-checkbox
                  :model-value="getModuleCheckState(module).checked"
                  :indeterminate="getModuleCheckState(module).indeterminate"
                  @change="(checked) => toggleModuleCheck(module, checked as boolean)"
                >
                   <el-icon class="module-icon"><component :is="module.icon || Menu" /></el-icon>
                   <span class="module-name">{{ module.label }}</span>
                </el-checkbox>
                 <el-tag effect="light" size="small">{{ module.children?.length || 0 }} 项</el-tag> 
              </div>
              <div class="module-nodes">
                 <el-checkbox
                   v-for="node in module.children"
                   :key="node.id"
                   :model-value="checkedNodeIds.has(node.id)"
                   :label="node.label"
                   @change="(checked) => toggleNodeCheck(node.id, checked as boolean)"
                   class="node-checkbox"
                 />
              </div>
            </div>
          </template>
          <el-empty v-else description="未找到匹配的功能权限" :image-size="80" />
        </div>

        <div v-show="activeTab === 'data'" class="modules-container custom-scrollbar">
           <template v-if="filteredDataPermissions.length > 0">
            <div v-for="module in filteredDataPermissions" :key="module.id" class="module-block">
              <div class="module-header">
                <el-checkbox
                  :model-value="getModuleCheckState(module).checked"
                  :indeterminate="getModuleCheckState(module).indeterminate"
                  @change="(checked) => toggleModuleCheck(module, checked as boolean)"
                >
                   <span class="module-name">{{ module.label }}</span>
                </el-checkbox>
                 <el-tag effect="light" size="small">{{ module.children?.length || 0 }} 项</el-tag> 
              </div>
              <div class="module-nodes">
                 <el-checkbox
                   v-for="node in module.children"
                   :key="node.id"
                   :model-value="checkedNodeIds.has(node.id)"
                   :label="node.label"
                   @change="(checked) => toggleNodeCheck(node.id, checked as boolean)"
                   class="node-checkbox"
                 />
              </div>
            </div>
          </template>
           <el-empty v-else description="未找到匹配的数据权限" :image-size="80" />
        </div>

      </div>

    </div>
    <el-empty v-else description="请等待角色信息加载或从列表选择角色进行配置" :image-size="100" />
  </el-card>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, computed } from 'vue';
import { 
  ElCard, ElButton, ElCheckbox, ElTag, ElEmpty, ElInput, ElIcon, ElDivider, ElRadioGroup, ElRadioButton
} from 'element-plus';
import { Check, Search, Menu } from '@element-plus/icons-vue';
import type { PermissionTree, PermissionNode, RolePermissionSavePayload } from '@/api/permission'; 
import { RoleType, RoleTypeMap } from '@/api/role'; // Import RoleType and RoleTypeMap

// Define a more generic role info type for the prop
interface CurrentRoleInfo {
  id: number | string;
  name: string;
  type: RoleType; // Use the imported RoleType
  // Add other common fields if necessary, e.g., code, description, but keep it minimal for this component
}

const props = defineProps<{ 
  currentRoleInfo: CurrentRoleInfo | null; // Changed from PlatformRole
  permissions: PermissionTree; 
  initialSelectedNodeIds: string[];
  loading: boolean;
}>();

const emit = defineEmits<{ 
  (e: 'save-permissions', payload: RolePermissionSavePayload): void;
  // Removed 'edit-role-info' and 'delete-role' emits
}>();

const activeTab = ref<'functional' | 'data'>('functional');
const filterText = ref('');
const checkedNodeIds = ref(new Set<string>());

watch(() => props.initialSelectedNodeIds, (newKeys) => {
    checkedNodeIds.value = new Set(newKeys || []);
}, { immediate: true, deep: true });

const filterNodes = (nodes: PermissionNode[] | undefined, keyword: string): PermissionNode[] => {
    if (!nodes) return [];
    if (!keyword) return nodes;
    const lowerKeyword = keyword.toLowerCase();
    return nodes.filter(node => node.label.toLowerCase().includes(lowerKeyword));
};

const filteredFunctionalPermissions = computed(() => {
  const functionalModules = props.permissions.filter(m => m.moduleType === 1);
  if (!filterText.value) {
    return functionalModules; 
  }
  const keyword = filterText.value.toLowerCase();
  const result: PermissionTree = [];
  functionalModules.forEach(module => {
    const moduleNameMatches = module.label.toLowerCase().includes(keyword);
    const matchingChildren = filterNodes(module.children, filterText.value);
    if (moduleNameMatches || matchingChildren.length > 0) {
      result.push({ ...module, children: matchingChildren }); 
    }
  });
  return result;
});

const filteredDataPermissions = computed(() => {
  const dataModules = props.permissions.filter(m => m.moduleType === 2);
    if (!filterText.value) {
    return dataModules; 
  }
  const keyword = filterText.value.toLowerCase();
  const result: PermissionTree = [];
  dataModules.forEach(module => {
    const moduleNameMatches = module.label.toLowerCase().includes(keyword);
    const matchingChildren = filterNodes(module.children, filterText.value);
    if (moduleNameMatches || matchingChildren.length > 0) {
      result.push({ ...module, children: matchingChildren }); 
    }
  });
  return result;
});

const toggleNodeCheck = (nodeId: string, checked: boolean) => {
  if (checked) {
    checkedNodeIds.value.add(nodeId);
  } else {
    checkedNodeIds.value.delete(nodeId);
  }
  checkedNodeIds.value = new Set(checkedNodeIds.value); 
};

const getModuleCheckState = (module: PermissionNode): { checked: boolean; indeterminate: boolean } => {
  const children = module.children?.filter(node => node.isLeaf); 
  if (!children || children.length === 0) {
    return { checked: false, indeterminate: false };
  }
  const totalLeafNodes = children.length;
  let checkedLeafCount = 0;
  children.forEach(node => {
    if (checkedNodeIds.value.has(node.id)) {
      checkedLeafCount++;
    }
  });
  const checked = checkedLeafCount === totalLeafNodes;
  const indeterminate = checkedLeafCount > 0 && checkedLeafCount < totalLeafNodes;
  return { checked, indeterminate };
};

const toggleModuleCheck = (module: PermissionNode, checked: boolean) => {
  module.children?.filter(node => node.isLeaf).forEach(node => {
    toggleNodeCheck(node.id, checked);
  });
};

const handleSavePermissions = () => {
  if (!props.currentRoleInfo) {
      console.warn('No currentRoleInfo provided.');
      return; 
  }
  const nodeIds = Array.from(checkedNodeIds.value); 
  const payload: RolePermissionSavePayload = {
    roleId: props.currentRoleInfo.id,
    nodeIds: nodeIds, 
  };
  emit('save-permissions', payload);
};

</script>

<style scoped>
.permission-config-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.el-card__header) {
  padding: 15px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
}

.config-content {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 0px 5px 5px 5px; /* Adjust padding if needed */
}

.role-details {
  margin-bottom: 15px;
  font-size: 14px;
}
.role-details p {
  margin-bottom: 5px;
  color: #606266;
}

.permission-section {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.permission-header {
  margin-bottom: 10px;
}
.permission-header h4 {
  font-size: 15px;
  font-weight: 600;
  margin: 0;
}

.permission-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap; /* Allow wrapping on smaller screens */
}

.filter-save-container {
 display: flex;
 align-items: center;
 gap: 10px; /* Adds space between filter input and save button */
}

.filter-input {
  width: 250px; /* Adjust as needed */
}

.save-button {
  /* Styles for save button if needed */
}

.modules-container {
  flex-grow: 1;
  overflow-y: auto; 
  padding-right: 10px; /* For scrollbar */
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 4px;
  padding: 15px;
}

.module-block {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px dashed var(--el-border-color-lighter);
}
.module-block:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: 500;
}

.module-header .el-checkbox__label {
  font-weight: 500; /* Make module name bold */
  font-size: 14px;
  display: flex;
  align-items: center;
}

.module-icon {
  margin-right: 6px;
  font-size: 16px;
}
.module-name {
  /* module name specific styles if needed */
}

.module-nodes {
  padding-left: 25px; /* Indent permission nodes */
  display: flex;
  flex-wrap: wrap; /* Allow nodes to wrap */
  gap: 5px 15px; /* row-gap column-gap */
}

.node-checkbox {
  min-width: 180px; /* Ensure checkboxes have enough space */
  margin-right: 0; /* Remove default margin if using gap */
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: #f0f0f0;
}

</style> 