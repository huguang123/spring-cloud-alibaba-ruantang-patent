<template>
  <el-card class="permission-config-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span v-if="role">配置角色权限: <strong>{{ role.rolesName }}</strong></span>
        <span v-else>请先选择一个角色</span>
        <div v-if="role">
          <el-button :icon="EditPen" @click="handleEditRoleInfo">编辑基本信息</el-button>
          <el-popconfirm
             title="确定要删除此角色吗?"
             width="220"
             @confirm="handleDeleteRole"
          >
            <template #reference>
                <el-button type="danger" :icon="Delete" plain>删除角色</el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </template>

    <div v-if="role" v-loading="loading" class="config-content">
      <div class="role-details">
        <p><strong>角色编码:</strong> {{ role.rolesCode }}</p>
        <p><strong>角色描述:</strong> {{ role.rolesDescription || '-' }}</p>
        <p><strong>角色类型:</strong> <el-tag size="small" effect="plain" type="info">平台角色</el-tag></p>
      </div>

      <el-divider />

      <div class="permission-section">
         <div class="permission-header">
           <h4>权限分配</h4>
           <el-button type="primary" :icon="Check" @click="handleSavePermissions">保存配置</el-button>
         </div>

        <el-tabs v-model="activeTab" class="permission-tabs">
          <el-tab-pane label="功能权限" name="functional">
            <div class="tree-toolbar">
              <el-input
                v-model="filterText"
                placeholder="筛选权限节点..."
                clearable
                :prefix-icon="Search"
                style="width: 240px; margin-bottom: 10px;"
              />
              <!-- Add Expand/Collapse All if needed -->
            </div>
            <el-tree
              ref="functionalTreeRef"
              :data="permissions"
              show-checkbox
              node-key="id" 
              :props="treeProps"
              :default-checked-keys="defaultCheckedKeys"
              :filter-node-method="filterNode"
              default-expand-all
              class="permission-tree custom-scrollbar"
            >
              <template #default="{ node, data }">
                <span class="custom-tree-node">
                  <span>{{ node.label }}</span>
                  <el-tag v-if="data.nodeType === 1" size="small" type="info" effect="light" style="margin-left: 8px;">菜单</el-tag>
                  <el-tag v-else-if="data.nodeType === 2" size="small" type="success" effect="light" style="margin-left: 8px;">按钮</el-tag>
                  <el-tag v-else-if="data.nodeType === 3" size="small" type="warning" effect="light" style="margin-left: 8px;">字段</el-tag>
                </span>
              </template>
            </el-tree>
          </el-tab-pane>
          <!-- Data Permission Tab (Optional based on your needs) -->
          <!--
          <el-tab-pane label="数据权限" name="data">
            <p>数据权限配置区域（如果需要单独配置数据权限节点）</p>
            <el-tree ... />
          </el-tab-pane>
          -->
        </el-tabs>
      </div>

    </div>
    <el-empty v-else description="请从左侧选择一个角色进行配置" :image-size="100" />
  </el-card>
</template>

<script setup lang="ts">
import { ref, computed, watch, defineProps, defineEmits, nextTick } from 'vue';
import { 
  ElCard, ElButton, ElPopconfirm, ElDivider, ElTabs, ElTabPane, 
  ElTree, ElTag, ElEmpty, ElInput 
} from 'element-plus';
import { EditPen, Delete, Check, Search } from '@element-plus/icons-vue';
import type { PlatformRole, PermissionTree, PermissionNode, RolePermissionSavePayload } from '@/types/permission';
import type { ElTree as ElTreeType } from 'element-plus'; // Import ElTree type

const props = defineProps<{ 
  role: PlatformRole | null;
  permissions: PermissionTree;
  initialSelectedNodeIds: string[];
  loading: boolean;
}>();

const emit = defineEmits<{ 
  (e: 'save-permissions', payload: RolePermissionSavePayload): void;
  (e: 'edit-role-info', role: PlatformRole): void;
  (e: 'delete-role', roleId: number | string): void;
}>();

const activeTab = ref('functional');
const functionalTreeRef = ref<InstanceType<typeof ElTreeType> | null>(null);
const filterText = ref('');

const treeProps = {
  children: 'children',
  label: 'label',
};

// Separate computed property for default checked keys to handle updates
const defaultCheckedKeys = ref<string[]>([]);

watch(() => props.initialSelectedNodeIds, (newKeys) => {
  defaultCheckedKeys.value = newKeys || [];
  // Ensure the tree is updated after data changes
  nextTick(() => {
      functionalTreeRef.value?.setCheckedKeys(defaultCheckedKeys.value, false); // Use setCheckedKeys
  });
}, { immediate: true, deep: true });

watch(filterText, (val) => {
  functionalTreeRef.value?.filter(val);
});

// Correct type for filter-node-method
const filterNode = (value: string, data: any): boolean => {
  if (!value) return true;
  // Check if data has the label property before accessing it
  return data.label && data.label.toLowerCase().includes(value.toLowerCase());
};

const handleSavePermissions = () => {
  if (!props.role || !functionalTreeRef.value) return;
  
  const checkedNodes = functionalTreeRef.value.getCheckedNodes(false); // Get only leaf nodes if needed, false gets all checked
  const halfCheckedNodes = functionalTreeRef.value.getHalfCheckedNodes(); // Get parent nodes of checked nodes

  const allCheckedNodes = [...checkedNodes, ...halfCheckedNodes];

  // Extract relevant node IDs based on permType
  // Modify this logic based on how backend expects the data (leaf IDs only? specific types?)
  const functionalNodeIds = allCheckedNodes
      .filter(node => node.id && !node.children) // Filter for actual permission nodes (leaves), adjust if module selection is needed
    //   .filter(node => node.permType === 0) // Optional: filter specifically for functional perms if tree mixes types
      .map(node => node.id);

  // Assuming data permissions are also handled within the same tree for now
  // const dataNodeIds = allCheckedNodes
  //     .filter(node => node.id && !node.children)
  //     .filter(node => node.permType === 1)
  //     .map(node => node.id);

  const payload: RolePermissionSavePayload = {
    roleId: props.role.id,
    functionalNodeIds: functionalNodeIds,
    // dataNodeIds: dataNodeIds, // Uncomment if needed
  };
  emit('save-permissions', payload);
};

const handleEditRoleInfo = () => {
  if (props.role) {
    emit('edit-role-info', props.role);
  }
};

const handleDeleteRole = () => {
  if (props.role) {
    emit('delete-role', props.role.id);
  }
};

</script>

<style scoped>
.permission-config-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.el-card__header) {
  flex-shrink: 0;
  padding: 12px 15px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
}
.card-header strong {
    color: var(--el-color-primary);
    margin-left: 5px;
}

:deep(.el-card__body) {
  flex-grow: 1;
  padding: 15px;
  overflow: hidden;
  display: flex; /* Use flexbox for body */
  flex-direction: column; /* Stack children vertically */
}

.config-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* Prevent content overflow */
}

.role-details {
  flex-shrink: 0;
  font-size: 14px;
  line-height: 1.8;
  margin-bottom: 10px;
}

.role-details p {
  margin: 0 0 5px 0;
}

.permission-section {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* Contain tabs and tree */
}

.permission-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    flex-shrink: 0;
}
.permission-header h4 {
    margin: 0;
    font-size: 15px;
}

.permission-tabs {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* Ensure tabs content scrolls */
}

:deep(.el-tabs__content) {
  flex-grow: 1;
  overflow: hidden; /* Allow tab pane to control scroll */
  padding: 0; /* Remove default padding */
}

:deep(.el-tab-pane) {
    height: 100%;
    display: flex;
    flex-direction: column;
}

.tree-toolbar {
    flex-shrink: 0;
    padding-bottom: 5px; /* Space between filter and tree */
}

.permission-tree {
  flex-grow: 1;
  overflow-y: auto; /* Enable scrolling for the tree */
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 4px;
  padding: 10px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #eee #f5f5f5;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f5f5f5;
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #ccc;
}

</style> 