<template>
  <el-dialog
    :model-value="visible"
    :title="computedTitle"
    width="80%" 
    top="5vh"
    @close="handleClose"
    :close-on-click-modal="false"
    destroy-on-close
    append-to-body
  >
    <div v-if="!roleData" class="p-4 text-center">
      <el-empty description="未选择角色" />
    </div>
    <div v-else class="permission-config-container" v-loading="loading">
      <div class="permission-tabs">
        <el-tabs v-model="activeTab" type="border-card">
          <el-tab-pane label="功能权限" name="operation">
            <div class="module-container">
              <template v-if="permissionData.operationModules && permissionData.operationModules.length > 0">
                <div v-for="module in permissionData.operationModules" :key="module.id" class="module-block">
                  <div class="module-header">
                    <el-checkbox
                      :model-value="isModuleAllSelected(module)"
                      :indeterminate="isModuleIndeterminate(module)"
                      @change="(val) => toggleModule(module, val)"
                    >
                      {{ module.moduleName }}
                    </el-checkbox>
                    <el-tag size="small">{{ module.moduleTypeName }}</el-tag>
                  </div>
                  <div class="module-nodes">
                    <el-checkbox-group v-model="selectedOperationNodes">
                      <el-checkbox 
                        v-for="node in module.nodes" 
                        :key="node.id" 
                        :label="node.id.toString()"
                      >
                        {{ node.nodeName }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                </div>
              </template>
              <el-empty v-else description="无可用的功能权限" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="数据权限" name="data">
            <div class="module-container">
              <template v-if="permissionData.dataModules && permissionData.dataModules.length > 0">
                <div v-for="module in permissionData.dataModules" :key="module.id" class="module-block">
                  <div class="module-header">
                    <el-checkbox
                      :model-value="isModuleAllSelected(module)"
                      :indeterminate="isModuleIndeterminate(module)"
                      @change="(val) => toggleModule(module, val)"
                    >
                      {{ module.moduleName }}
                    </el-checkbox>
                    <el-tag size="small" type="success">{{ module.moduleTypeName }}</el-tag>
                  </div>
                  <div class="module-nodes">
                    <el-checkbox-group v-model="selectedDataNodes">
                      <el-checkbox 
                        v-for="node in module.nodes" 
                        :key="node.id" 
                        :label="node.id.toString()"
                      >
                        {{ node.nodeName }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                </div>
              </template>
              <el-empty v-else description="无可用的数据权限" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose" :disabled="saving || loading">取消</el-button>
        <el-button 
          type="primary"
          @click="handleSavePermissions"
          :loading="saving"
          :disabled="!roleData || loading"
        >
          保存权限
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch, defineProps, defineEmits, onMounted } from 'vue';
import { ElDialog, ElButton, ElEmpty, ElTabs, ElTabPane, ElMessage, ElTag, ElCheckbox, ElCheckboxGroup } from 'element-plus';
import { handleApiError, handleApiResponse } from '@/utils/errorHandler';
import type { RoleInfo } from '@/api/role';
import { getRolePermissionTree, assignPermissions, type PermissionTreeResponse, type PermissionModuleNode } from '@/api/role';

interface Props {
  visible: boolean;
  roleData: RoleInfo | null;
}

const props = defineProps<Props>();
const emit = defineEmits(['update:visible', 'permissions-saved']);

// 权限数据
const permissionData = ref<PermissionTreeResponse>({
  operationModules: [],
  dataModules: []
});

// 已选择的节点ID
const selectedOperationNodes = ref<string[]>([]);
const selectedDataNodes = ref<string[]>([]);

// UI状态
const activeTab = ref('operation');
const loading = ref(false);
const saving = ref(false);

// 计算对话框标题
const computedTitle = computed(() => {
  return props.roleData 
    ? `为角色 '${props.roleData.rolesName}' 分配权限` 
    : '分配权限';
});

// 监听对话框显示状态和角色数据变化
watch(() => [props.visible, props.roleData], ([visible, roleData]) => {
  if (visible && roleData && typeof roleData === 'object' && 'id' in roleData) {
    loadPermissionTree(roleData.id);
  }
}, { immediate: true });

// 加载权限树
const loadPermissionTree = async (roleId: string) => {
  loading.value = true;
  selectedOperationNodes.value = [];
  selectedDataNodes.value = [];
  
  try {
    const response = await getRolePermissionTree(roleId);
    console.log('获取角色权限树API返回:', response);
    
    // 处理标准返回格式 { code: 200, message: 'success', data: {...} }
    let result: PermissionTreeResponse;
    if (response && (response as any).code === 200) {
      result = (response as any).data;
    } else {
      result = response as PermissionTreeResponse;
    }
    
    // 确保模块和节点是有效数组
    permissionData.value = {
      operationModules: Array.isArray(result.operationModules) ? result.operationModules : [],
      dataModules: Array.isArray(result.dataModules) ? result.dataModules : []
    };
    
    console.log('处理后的权限树数据:', permissionData.value);
    
    // 初始化已选择的节点
    initSelectedNodes();
  } catch (error) {
    handleApiError(error, '加载权限数据失败');
  } finally {
    loading.value = false;
  }
};

// 初始化选中的节点
const initSelectedNodes = () => {
  // 处理操作权限
  permissionData.value.operationModules?.forEach(module => {
    module.nodes.forEach(node => {
      if (node.selected) {
        selectedOperationNodes.value.push(node.id.toString());
      }
    });
  });
  
  // 处理数据权限
  permissionData.value.dataModules?.forEach(module => {
    module.nodes.forEach(node => {
      if (node.selected) {
        selectedDataNodes.value.push(node.id.toString());
      }
    });
  });
};

// 判断模块是否全部选中
const isModuleAllSelected = (module: PermissionModuleNode) => {
  if (!module.nodes || module.nodes.length === 0) return false;
  
  const isOperationModule = permissionData.value.operationModules.includes(module);
  const selectedNodes = isOperationModule ? selectedOperationNodes.value : selectedDataNodes.value;
  
  return module.nodes.every(node => selectedNodes.includes(node.id.toString()));
};

// 判断模块是否部分选中
const isModuleIndeterminate = (module: PermissionModuleNode) => {
  if (!module.nodes || module.nodes.length === 0) return false;
  
  const isOperationModule = permissionData.value.operationModules.includes(module);
  const selectedNodes = isOperationModule ? selectedOperationNodes.value : selectedDataNodes.value;
  
  const selectedCount = module.nodes.filter(node => selectedNodes.includes(node.id.toString())).length;
  return selectedCount > 0 && selectedCount < module.nodes.length;
};

// 切换模块选中状态
const toggleModule = (module: PermissionModuleNode, checked: unknown) => {
  const isChecked = Boolean(checked);
  const isOperationModule = permissionData.value.operationModules.includes(module);
  const nodeIds = module.nodes.map(node => node.id.toString());
  
  if (isOperationModule) {
    if (isChecked) {
      // 添加所有节点
      selectedOperationNodes.value = [...new Set([...selectedOperationNodes.value, ...nodeIds])];
    } else {
      // 移除所有节点
      selectedOperationNodes.value = selectedOperationNodes.value.filter(id => !nodeIds.includes(id));
    }
  } else {
    if (isChecked) {
      // 添加所有节点
      selectedDataNodes.value = [...new Set([...selectedDataNodes.value, ...nodeIds])];
    } else {
      // 移除所有节点
      selectedDataNodes.value = selectedDataNodes.value.filter(id => !nodeIds.includes(id));
    }
  }
};

// 保存权限配置
const handleSavePermissions = async () => {
  if (!props.roleData) {
    handleApiError(new Error('没有选择角色'), '没有选择角色');
    return;
  }
  
  saving.value = true;
  
  try {
    // 收集操作权限ID
    const permIds = collectPermIds();
    
    // 收集数据权限ID
    const dataPolicyIds = collectDataPolicyIds();
    
    // 调用API保存权限
    const response = await assignPermissions({
      roleId: props.roleData.id,
      permIds,
      dataPolicyIds
    });
    
    console.log('保存权限API返回:', response);
    
    // 处理标准响应格式
    let success = false;
    if (response && (response as any).code === 200) {
      success = true;
    } else if (response === true || (typeof response === 'object' && response !== null && response.data === true)) {
      success = true;
    }
    
    if (success) {
      ElMessage.success('权限分配成功');
      emit('permissions-saved', props.roleData.id);
      handleClose();
    } else {
      throw new Error('API返回格式异常');
    }
  } catch (error) {
    handleApiError(error, '保存权限失败');
  } finally {
    saving.value = false;
  }
};

// 收集操作权限ID (permId)
const collectPermIds = () => {
  const permIds: (string | number)[] = [];
  
  permissionData.value.operationModules?.forEach(module => {
    module.nodes.forEach(node => {
      if (selectedOperationNodes.value.includes(node.id.toString()) && node.permId) {
        permIds.push(node.permId);
      }
    });
  });
  
  return permIds;
};

// 收集数据权限ID (dataPolicyId)
const collectDataPolicyIds = () => {
  const dataPolicyIds: (string | number)[] = [];
  
  permissionData.value.dataModules?.forEach(module => {
    module.nodes.forEach(node => {
      if (selectedDataNodes.value.includes(node.id.toString()) && node.dataPolicyId) {
        dataPolicyIds.push(node.dataPolicyId);
      }
    });
  });
  
  return dataPolicyIds;
};

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false);
};
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 10px;
}

.permission-config-container {
  height: 60vh;
  overflow: auto;
}

.module-container {
  padding: 10px;
  max-height: 50vh;
  overflow-y: auto;
}

.module-block {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.module-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 15px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.module-nodes {
  padding: 15px;
}

.el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.p-4 {
  padding: 1rem;
}

.text-center {
  text-align: center;
}
</style> 