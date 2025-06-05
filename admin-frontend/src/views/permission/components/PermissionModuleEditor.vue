<template>
  <div class="permission-module-editor">
    <p class="p-4 bg-gray-100 rounded-md">
      模块与节点编辑器 (模板ID: {{ templateId }}, 版本ID: {{ templateVersionId }})
    </p>
    <div class="mt-4">
      <!-- Module and Node Configuration Area (Reused from TemplateManagement) -->
      <el-row :gutter="20">
        <!-- Left Panel: Module List -->
        <el-col :xs="24" :sm="24" :md="8" class="config-col">
          <div class="module-panel">
            <div class="panel-header">
              <h4>模块列表</h4>
              <el-button type="primary" link :icon="Plus" @click="handleAddModule">新增模块</el-button>
            </div>
            <el-input placeholder="搜索模块..." v-model="moduleSearchKeyword" clearable class="search-input"/>
            <div class="module-list custom-scrollbar" v-loading="modulesLoading">
              <div 
                v-for="mod in filteredModules" 
                :key="mod.id" 
                class="module-item"
                :class="{ 'is-active': selectedModule?.id === mod.id }"
                :data-id="mod.id"
                @click="handleSelectModule(mod)"
              >
                <el-icon><Menu /></el-icon>
                <span>{{ mod.moduleName }}</span>
                <span class="module-type-badge">{{ mod.moduleType === 1 ? '功能' : '数据' }}</span>
                <span class="module-actions">
                  <el-button link :icon="EditPen" size="small" @click.stop="handleEditModule(mod)"></el-button>
                  <el-popconfirm title="确定删除此模块？" @confirm="handleDeleteModule(mod.id)">
                    <template #reference>
                      <el-button link :icon="Delete" size="small" @click.stop></el-button>
                    </template>
                  </el-popconfirm>
                </span>
              </div>
              <el-empty v-if="!modulesLoading && filteredModules.length === 0" description="暂无模块" :image-size="60"/>
            </div>
          </div>
        </el-col>

        <!-- Right Panel: Node List & Details -->
        <el-col :xs="24" :sm="24" :md="16" class="config-col">
          <div class="node-panel">
            <div class="panel-header">
              <h4>
                节点列表 
                <span v-if="selectedModule" class="text-muted">({{ selectedModule.moduleName }})</span>
                 <el-tag v-if="selectedModule" type="info" size="small" class="ml-2">排序: {{ selectedModule.sort ?? 0 }}</el-tag>
              </h4>
              <div>
                <el-button 
                  type="primary" 
                  plain
                  size="small"
                  class="mr-2"
                  @click="saveModuleSortAndNodes" 
                  :disabled="!selectedModule || !nodesChanged"
                >
                  保存节点排序
                </el-button>
                <el-button type="primary" link :icon="Plus" @click="handleAddNode" :disabled="!selectedModule">
                  新增节点
                </el-button>
              </div>
            </div>
            <div v-if="selectedModule" class="node-content">
              <el-input placeholder="搜索节点..." v-model="nodeSearchKeyword" clearable class="search-input"/>
              <el-alert
                title='节点可拖拽排序。修改排序后请点击上方的"保存节点排序"按钮。'
                type="info"
                show-icon
                :closable="false"
                class="mx-[15px] mb-[10px]"
              />
              <el-table 
                :data="filteredNodes" 
                style="width: 100%" 
                size="small" 
                border 
                class="node-list-table node-list-table-fixed-layout"
                row-key="id"
                ref="nodeTableRef"
                :height="400"
                v-loading="moduleNodesLoading"
                empty-text="暂无节点数据"
              >
                <el-table-column label="排序" width="60" align="center">
                  <template #default="{ row }">
                    <span>{{ row.sort || 0 }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="拖拽" width="60" align="center">
                  <template #default>
                    <el-icon class="drag-handle cursor-grab"><Rank /></el-icon>
                  </template>
                </el-table-column>
                <el-table-column prop="nodeName" label="节点名称" min-width="150"/>
                <el-table-column label="节点类型" width="90">
                  <template #default="{ row }">{{ getNodeTypeLabel(row.nodeType) || row.nodeTypeName || '-' }}</template>
                </el-table-column>
                <el-table-column label="权限类型" width="100">
                  <template #default="{ row }">{{ row.permTypeName || (row.permType === 0 ? '操作权限' : '数据权限') || '-' }}</template>
                </el-table-column>
                <el-table-column label="绑定权限/策略" min-width="180">
                  <template #default="{ row }">
                    <span v-if="row.permType === 0">{{ getOperationPermissionName(row.permId) || (row.permDetail && row.permDetail.permsName) || '-' }}</span>
                    <span v-else>{{ getDataStrategyName(row.dataPolicyId) || (row.dataPolicyDetail && row.dataPolicyDetail.policyName) || '-' }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="80" align="center" fixed="right">
                  <template #default="{ row }">
                    <el-dropdown trigger="click" @command="(command) => handleNodeCommand(command, row)">
                      <el-button text type="primary" :icon="MoreFilled" />
                      <template #dropdown>
                        <el-dropdown-menu>
                          <el-dropdown-item command="edit">编辑</el-dropdown-item>
                          <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                        </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <el-empty v-else description="请先选择一个模块" :image-size="100" class="node-list-empty"/>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- Add/Edit Module & Node Modals would be invoked here -->
    <ModuleFormDialog 
      v-model:visible="moduleFormVisible"
      :is-editing="isEditingModule"
      :initial-data="currentModuleData"
      :existing-modules="templateModules" 
      @submit="handleModuleFormSubmit"
    />

    <NodeFormDialog 
      v-model:visible="nodeFormVisible"
      :is-editing="isEditingNode"
      :initial-data="currentNodeData"
      :selected-module-name="selectedModule?.moduleName || ''"
      :all-operation-permissions="allOperationPermissions"
      :all-data-policies="allDataPolicies"
      @submit="handleNodeFormSubmit"
    />

  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, nextTick } from 'vue';
import type { PropType } from 'vue';
import { ElRow, ElCol, ElInput, ElButton, ElIcon, ElTable, ElTableColumn, ElTag, ElEmpty, ElPopconfirm, ElDropdown, ElDropdownMenu, ElDropdownItem, ElMessage, ElAlert, ElMessageBox } from 'element-plus';
import { Plus, EditPen, Delete, Menu, MoreFilled, Rank } from '@element-plus/icons-vue';
import { 
  getModulesByVersion, 
  createModule, 
  updateModule, 
  deleteModule,
  getNodesByModule,
  createNode,
  updateNode,
  deleteNode,
  PermissionModule,
  PermissionNode
} from '@/api/templatePermission';
import { 
  getOperationPermissionsPage, 
  getDataStrategiesPage, 
  OperationPermission, 
  DataStrategy,
  PermQueryParams,
  DataStrategyQueryParams  
} from '@/api/permission';
import ModuleFormDialog from './ModuleFormDialog.vue';
import NodeFormDialog from './NodeFormDialog.vue';
import Sortable, { type SortableEvent } from 'sortablejs';

// 定义扩展的节点类型，添加排序字段
interface ExtendedPermissionNode extends PermissionNode {
  nodeSort?: number;
  sort?: number;
}

const props = defineProps({
  templateId: {
    type: [Number, String] as PropType<number | string | null>,
    default: null
  },
  templateVersionId: {
    type: [Number, String] as PropType<number | string | null>,
    default: null
  }
});

const modulesLoading = ref(false);
const templateModules = ref<PermissionModule[]>([]);
const selectedModule = ref<PermissionModule | null>(null);
const moduleNodes = ref<ExtendedPermissionNode[]>([]);
const lastFetchedVersionId = ref<string>(''); // 用于缓存最后请求的版本ID
const lastFetchedModuleId = ref<string>(''); // 用于缓存最后请求的模块ID

const moduleSearchKeyword = ref('');
const nodeSearchKeyword = ref('');
const nodesChanged = ref(false);
const nodeTableRef = ref<any>(null);
const isSavingFullConfig = ref(false);

// Module & Node Dialogs state
const moduleFormVisible = ref(false);
const isEditingModule = ref(false);
const currentModuleData = ref<Partial<PermissionModule> | null>(null);

const nodeFormVisible = ref(false);
const isEditingNode = ref(false);
const currentNodeData = ref<Partial<ExtendedPermissionNode> | null>(null);

const allOperationPermissions = ref<OperationPermission[]>([]);
const allDataPolicies = ref<DataStrategy[]>([]);

// 添加加载状态变量
const moduleNodesLoading = ref(false);

// 获取有效的模板版本ID（处理字符串类型的ID）
const getEffectiveVersionId = (): string | null => {
  if (!props.templateVersionId) return null;
  
  // 始终返回字符串类型的ID，避免转换为数字
  return String(props.templateVersionId);
};

// 加载模块列表
const fetchModules = async () => {
  const versionId = getEffectiveVersionId();
  if (!versionId) {
    console.log('fetchModules: 版本ID不存在或无效，无法加载模块列表');
    return;
  }
  
  // 确保ID作为字符串处理，避免大整数精度丢失
  const versionIdString = versionId;
  console.log('获取模块列表 - 版本ID:', versionIdString, '类型:', typeof versionIdString, '长度:', versionIdString.length);
  
  modulesLoading.value = true;
  try {
    // 始终强制请求新数据，避免重复请求
    // 不要将ID转换为数字，直接使用字符串
    const response = await getModulesByVersion(versionIdString);
    console.log('获取模块列表响应:', response);
    
    // 记录最后请求的版本ID
    lastFetchedVersionId.value = versionIdString;
    
    // 处理不同的响应结构
    if (response) {
      const anyResponse = response as any;
      let modules = [];
      
      // 直接是数组的情况
      if (Array.isArray(anyResponse)) {
        modules = anyResponse;
      }
      // data字段是数组的情况
      else if (anyResponse.data && Array.isArray(anyResponse.data)) {
        modules = anyResponse.data;
      } 
      // 嵌套在对象中的情况
      else if (anyResponse.data && typeof anyResponse.data === 'object') {
        if (anyResponse.data.records && Array.isArray(anyResponse.data.records)) {
          modules = anyResponse.data.records;
        } else if (Array.isArray(anyResponse.data)) {
          modules = anyResponse.data;
        }
      }
      
      // 保持ID作为字符串，而不是转换为数字
      templateModules.value = modules.map((mod: any) => {
        // 确保ID字段是字符串
        if (mod.id && typeof mod.id !== 'string') {
          mod.id = mod.id.toString();
        }
        if (mod.templateVersionId && typeof mod.templateVersionId !== 'string') {
          mod.templateVersionId = mod.templateVersionId.toString();
        }
        return mod;
      });
      
      // 添加调试日志
      console.log('解析后的模块列表数据:', templateModules.value);
      console.log('模块列表长度:', templateModules.value.length);
      
      // 如果有模块数据，默认选中第一个
      if (templateModules.value.length > 0 && !selectedModule.value) {
        console.log('默认选中第一个模块:', templateModules.value[0]);
        selectedModule.value = templateModules.value[0];
        // 加载该模块的节点数据
        if (templateModules.value[0].id) {
          handleSelectModule(templateModules.value[0]);
        }
      }
    } else {
      templateModules.value = [];
      console.log('没有获取到模块数据');
    }
  } catch (error) {
    console.error('加载模块列表失败:', error);
    ElMessage.error('加载模块列表失败');
  } finally {
    modulesLoading.value = false;
  }
};

// 监听模板和版本ID变化，重新加载模块
watch([() => props.templateId, () => props.templateVersionId], () => {
  if (props.templateVersionId) {
    fetchModules();
    
    // 不在此处加载权限和策略数据，仅在需要时加载
    // fetchOperationPermissions();
    // fetchDataPolicies();
  } else {
    templateModules.value = [];
    selectedModule.value = null;
    moduleNodes.value = [];
  }
}, { immediate: true });

const filteredModules = computed(() => {
  const sorted = [...templateModules.value].sort((a, b) => (a.sort || 0) - (b.sort || 0));
  if (!moduleSearchKeyword.value) return sorted;
  return sorted.filter(m => 
    m.moduleName.toLowerCase().includes(moduleSearchKeyword.value.toLowerCase())
  );
});

// 过滤节点列表
const filteredNodes = computed(() => {
  if (!moduleNodes.value.length) return [];
  
  // 先按排序值排序，使用sort字段
  const sorted = [...moduleNodes.value].sort((a, b) => ((a.sort || 0) - (b.sort || 0)));
  
  // 如果有搜索关键词，过滤结果
  if (!nodeSearchKeyword.value) return sorted;
  
  return sorted.filter(node => 
    node.nodeName && node.nodeName.toLowerCase().includes(nodeSearchKeyword.value.toLowerCase())
  );
});

// 完全重写handleSelectModule函数
const handleSelectModule = async (module: PermissionModule) => {
  console.log('选择模块:', module);
  selectedModule.value = module;
  moduleNodes.value = [];
  nodeSearchKeyword.value = '';
  nodesChanged.value = false;
  
  // 加载该模块下的节点
  if (module.id) {
    try {
      // 设置加载状态
      moduleNodesLoading.value = true;
      
      // 确保模块ID是字符串类型
      const moduleId = String(module.id);
      console.log('加载模块节点 - 模块ID:', moduleId);
      
      // 添加await确保正确等待API响应
      const response = await getNodesByModule(moduleId);
      console.log('节点API响应原始数据:', response);
      
      // 更全面的解析逻辑，处理所有可能的返回格式
      let nodesList: any[] = [];
      
      // 使用any类型处理各种可能的响应格式
      const anyResponse = response as any;
      
      // 考虑所有可能的响应结构
      if (anyResponse === null || anyResponse === undefined) {
        console.log('API返回了null或undefined');
        nodesList = [];
      } 
      else if (Array.isArray(anyResponse)) {
        console.log('返回值是数组');
        nodesList = anyResponse;
      } 
      else if (typeof anyResponse === 'object') {
        // 处理各种嵌套结构
        if (anyResponse.code === 200 && Array.isArray(anyResponse.data)) {
          console.log('标准响应格式: code=200 + data数组');
          nodesList = anyResponse.data;
        } 
        else if (anyResponse.code === 200 && anyResponse.data && typeof anyResponse.data === 'object') {
          if (Array.isArray(anyResponse.data.list)) {
            console.log('标准响应格式: code=200 + data.list数组');
            nodesList = anyResponse.data.list;
          } 
          else if (Array.isArray(anyResponse.data.records)) {
            console.log('标准响应格式: code=200 + data.records数组');
            nodesList = anyResponse.data.records;
          }
          else if (anyResponse.data.nodeList && Array.isArray(anyResponse.data.nodeList)) {
            console.log('标准响应格式: code=200 + data.nodeList数组');
            nodesList = anyResponse.data.nodeList;
          }
        }
        else if (Array.isArray(anyResponse.list)) {
          console.log('响应直接包含list数组');
          nodesList = anyResponse.list;
        }
        else if (Array.isArray(anyResponse.records)) {
          console.log('响应直接包含records数组');
          nodesList = anyResponse.records;
        }
        else if (anyResponse.data && !Array.isArray(anyResponse.data) && typeof anyResponse.data === 'object') {
          // 如果data是单个对象而不是数组，将其包装成数组
          console.log('单个对象，包装为数组');
          nodesList = [anyResponse.data];
        }
        else if (anyResponse.nodes && Array.isArray(anyResponse.nodes)) {
          console.log('响应包含nodes数组字段');
          nodesList = anyResponse.nodes;
        }
        else if (anyResponse.nodeList && Array.isArray(anyResponse.nodeList)) {
          console.log('响应包含nodeList数组字段');
          nodesList = anyResponse.nodeList;
        }
        else {
          // 如果是其他对象结构，尝试直接使用
          console.log('使用响应本身作为数据');
          nodesList = [anyResponse];
        }
      }
      
      console.log(`原始解析出的节点列表(${nodesList.length}个):`, nodesList);
      
      // 确保每个节点对象格式正确
      const validNodes = nodesList.filter(node => {
        // 过滤无效节点
        if (!node || typeof node !== 'object') {
          console.warn('过滤掉非对象节点');
          return false;
        }
        return true;
      }).map((node, index) => {
        // 规范化节点对象字段，确保必要的属性存在
        const normalizedNode = {
          id: node.id?.toString() || `temp-${index}`,
          nodeName: node.nodeName || '未命名节点',
          moduleId: node.moduleId?.toString() || moduleId,
          nodeType: typeof node.nodeType !== 'undefined' ? node.nodeType : 1,
          nodeTypeName: node.nodeTypeName || getNodeTypeLabel(node.nodeType || 1),
          permType: typeof node.permType !== 'undefined' ? node.permType : 0,
          permTypeName: node.permTypeName || (node.permType === 1 ? '数据权限' : '操作权限'),
          permId: node.permId?.toString() || undefined,
          dataPolicyId: node.dataPolicyId?.toString() || undefined,
          isBasic: typeof node.isBasic !== 'undefined' ? node.isBasic : 0,
          dataScope: node.dataScope,
          permDetail: node.permDetail,
          sort: node.sort || (index + 1),
          nodeSort: node.nodeSort || node.sort || (index + 1)
        };
        
        console.log(`节点 ${index+1} 规范化:`, normalizedNode);
        return normalizedNode;
      });
      
      console.log(`有效节点数量: ${validNodes.length}`);
      
      // 设置节点数据
      moduleNodes.value = validNodes;
      
      // 提示信息更友好
      if (validNodes.length > 0) {
        ElMessage.success(`已加载 ${validNodes.length} 个节点`);
      } else {
        ElMessage.info('该模块暂无节点数据');
      }
      
      // 在DOM更新后初始化排序
      nextTick(() => initSortableNodes());
    } catch (error) {
      console.error('加载节点列表失败:', error);
      ElMessage.error('加载节点列表失败: ' + (error instanceof Error ? error.message : String(error)));
      moduleNodes.value = [];
    } finally {
      moduleNodesLoading.value = false;
    }
  }
};

// 初始化节点排序功能
const initSortableNodes = () => {
  if (!nodeTableRef.value || !nodeTableRef.value.$el) return;
  
  // 等待DOM更新完成再初始化Sortable
  setTimeout(() => {
    const tbody = nodeTableRef.value.$el.querySelector('.el-table__body-wrapper tbody');
    if (!tbody) {
      console.warn('未找到表格tbody元素，无法初始化排序功能');
      return;
    }
    
    console.log('初始化节点拖拽排序...');
    Sortable.create(tbody, {
      handle: '.drag-handle',
      animation: 150,
      onEnd: (evt: SortableEvent) => {
        const { newIndex, oldIndex } = evt;
        if (newIndex === undefined || oldIndex === undefined) return;
        
        console.log('拖拽排序:', { oldIndex, newIndex });
        
        // 直接操作当前过滤后的数组
        const currentNodes = [...filteredNodes.value];
        
        // 执行拖拽操作
        const [movedNode] = currentNodes.splice(oldIndex, 1);
        currentNodes.splice(newIndex, 0, movedNode);
        
        // 重新分配排序号，确保是连续的
        currentNodes.forEach((node, index) => {
          node.sort = index + 1;
        });
        
        // 更新原始数组
        moduleNodes.value = currentNodes;
        
        console.log('拖拽后的节点排序:', moduleNodes.value.map(n => ({ id: n.id, name: n.nodeName, sort: n.sort })));
        
        nodesChanged.value = true;
      }
    });
  }, 100); // 短暂延迟确保DOM已更新
};

// 处理添加模块
const handleAddModule = () => {
  if (!props.templateVersionId) {
    ElMessage.warning('请先选择一个模板版本');
    return;
  }
  
  isEditingModule.value = false;
  currentModuleData.value = {
    templateVersionId: props.templateVersionId.toString(), // 转为字符串
    moduleType: 1,
    moduleName: '',
    sort: templateModules.value.length * 10 + 10
  };
  moduleFormVisible.value = true;
};

// 处理编辑模块
const handleEditModule = (module: PermissionModule) => {
  isEditingModule.value = true;
  currentModuleData.value = { ...module };
  moduleFormVisible.value = true;
};

// 处理删除模块
const handleDeleteModule = async (moduleId?: number | string) => {
  if (!moduleId) return;
  
  try {
    await ElMessageBox.confirm(
      '确定要删除此模块？此操作将同时删除模块下的所有节点，且不可恢复。',
      '确认删除',
      { type: 'warning' }
    );
    
    // 确保moduleId是字符串类型，防止精度丢失
    const effectiveModuleId = moduleId.toString();
    
    await deleteModule(effectiveModuleId);
    ElMessage.success('删除模块成功');
    
    // 从列表中移除该模块
    templateModules.value = templateModules.value.filter(m => m.id !== moduleId);
    
    // 如果删除的是当前选中的模块，清空选择
    if (selectedModule.value && selectedModule.value.id === moduleId) {
      selectedModule.value = null;
      moduleNodes.value = [];
      
      // 如果还有其他模块，自动选择第一个
      if (templateModules.value.length > 0) {
        handleSelectModule(templateModules.value[0]);
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除模块失败:', error);
      ElMessage.error('删除模块失败');
    }
  }
};

// 处理模块表单提交
const handleModuleFormSubmit = async (moduleData: PermissionModule) => {
  try {
    let createdModuleId: string | number | undefined;
    
    if (isEditingModule.value && moduleData.id) {
      // 更新模块时，确保包含sort字段
      const submitData = {
        id: moduleData.id,
        moduleName: moduleData.moduleName,
        moduleType: moduleData.moduleType,
        sort: moduleData.sort || 0
      };
      console.log('更新模块，提交数据:', submitData);
      await updateModule(submitData);
      ElMessage.success('更新模块成功');
      createdModuleId = moduleData.id;
    } else {
      // 创建模块，确保包含所有必要字段
      const createData = {
        templateVersionId: props.templateVersionId?.toString() || '',
        moduleName: moduleData.moduleName,
        moduleType: moduleData.moduleType,
        sort: moduleData.sort || 0
      };
      console.log('创建模块，提交数据:', createData);
      const response = await createModule(createData);
      console.log('创建模块响应:', response);
      
      if (response) {
        // 尝试从不同响应结构中获取创建的模块ID
        const anyResponse = response as any;
        if (anyResponse.data && anyResponse.data.id) {
          createdModuleId = anyResponse.data.id;
        } else if (anyResponse.id) {
          createdModuleId = anyResponse.id;
        }
        
        ElMessage.success('创建模块成功');
      }
    }
    
    // 重新加载模块列表
    console.log('正在重新加载模块列表...');
    
    // 关闭表单对话框
    moduleFormVisible.value = false;
    
    // 强制重新加载数据
    lastFetchedVersionId.value = ''; // 清除缓存的版本ID，强制刷新
    await fetchModules();
    console.log('模块列表重新加载完成，模块数量:', templateModules.value.length);
    
    // 如果有新创建的模块ID，尝试选中并滚动到该模块
    if (createdModuleId) {
      console.log('查找新创建的模块，ID:', createdModuleId);
      // 等待DOM更新完成
      nextTick(() => {
        // 查找新创建的模块
        const newModule = templateModules.value.find(m => String(m.id) === String(createdModuleId));
        if (newModule) {
          console.log('找到新创建的模块，准备选中:', newModule);
          // 选中新创建的模块
          handleSelectModule(newModule);
          
          // 如果有可能，滚动到新模块的位置
          try {
            const moduleEl = document.querySelector(`.module-item[data-id="${createdModuleId}"]`);
            if (moduleEl) {
              (moduleEl as HTMLElement).scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
          } catch (e) {
            console.warn('无法滚动到新创建的模块:', e);
          }
        } else {
          console.warn('未找到新创建的模块，ID:', createdModuleId);
          console.log('当前所有模块:', templateModules.value.map(m => ({ id: m.id, name: m.moduleName })));
        }
      });
    }
  } catch (error) {
    console.error('保存模块失败:', error);
    ElMessage.error('保存模块失败: ' + (error instanceof Error ? error.message : String(error)));
  }
};

// 获取节点类型标签
const getNodeTypeLabel = (type?: number) => {
  switch (type) {
    case 1: return '菜单项';
    case 2: return '操作按钮';
    case 3: return '数据字段';
    default: return '-';
  }
};

// --- CRUD for Nodes ---
const handleAddNode = async () => {
  if (!selectedModule.value || !selectedModule.value.id) {
    ElMessage.warning('请先选择一个模块');
    return;
  }
  
  // 不再预加载权限数据，由NodeFormDialog组件内部处理
  
  isEditingNode.value = false;
  currentNodeData.value = {
    moduleId: String(selectedModule.value.id), // 确保是字符串
    nodeName: '',
    nodeType: 1, // 默认为菜单项
    permType: 0, // 默认为操作权限
    sort: moduleNodes.value.length + 1,
    nodeSort: moduleNodes.value.length + 1
  };
  nodeFormVisible.value = true;
};

const handleEditNode = async (node: ExtendedPermissionNode) => {
  // 不再预加载权限数据，由NodeFormDialog组件内部处理
  
  isEditingNode.value = true;
  
  // 确保节点ID是字符串
  const nodeData = { ...node };
  if (nodeData.id && typeof nodeData.id !== 'string') {
    nodeData.id = String(nodeData.id);
  }
  if (nodeData.moduleId && typeof nodeData.moduleId !== 'string') {
    nodeData.moduleId = String(nodeData.moduleId);
  }
  if (nodeData.permId && typeof nodeData.permId !== 'string') {
    nodeData.permId = String(nodeData.permId);
  }
  if (nodeData.dataPolicyId && typeof nodeData.dataPolicyId !== 'string') {
    nodeData.dataPolicyId = String(nodeData.dataPolicyId);
  }
  
  currentNodeData.value = nodeData;
  nodeFormVisible.value = true;
};

const handleDeleteNode = async (nodeId?: number | string) => {
  if (!nodeId) return;
  
  try {
    await ElMessageBox.confirm(
      '确定要删除此节点吗？',
      '确认删除',
      { type: 'warning' }
    );
    
    // 确保nodeId是字符串类型，防止精度丢失
    const effectiveNodeId = nodeId.toString();
    
    await deleteNode(effectiveNodeId);
    ElMessage.success('删除节点成功');
    
    // 重新加载节点列表
    if (selectedModule.value && selectedModule.value.id) {
      await handleSelectModule(selectedModule.value);
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除节点失败:', error);
      ElMessage.error('删除节点失败');
    }
  }
};

const handleNodeFormSubmit = async (formData: any) => {
  try {
    console.log('节点表单提交数据:', formData);
    
    if (isEditingNode.value && formData.id) {
      // 更新节点时确保包含sort字段
      const submitData: any = {
        id: formData.id,
        nodeName: formData.nodeName,
        nodeType: formData.nodeType,
        permType: formData.permType,
        isBasic: formData.isBasic,
        sort: formData.sort || 0
      };
      
      // 根据权限类型添加相应的ID
      if (formData.permType === 0 && formData.permId) {
        submitData.permId = formData.permId;
      } else if (formData.permType === 1 && formData.dataPolicyId) {
        submitData.dataPolicyId = formData.dataPolicyId;
      }
      
      // 数据字段类型时的数据范围
      if (formData.nodeType === 3 && formData.dataScope) {
        submitData.dataScope = formData.dataScope;
      }
      
      console.log('发送更新节点请求，数据:', submitData);
      await updateNode(submitData);
      ElMessage.success('更新节点成功');
    } else {
      // 创建节点时确保包含sort字段
      console.log('发送创建节点请求，数据:', formData);
      await createNode(formData);
      ElMessage.success('创建节点成功');
    }
    
    // 重新加载节点列表
    if (selectedModule.value && selectedModule.value.id) {
      await handleSelectModule(selectedModule.value);
    }
    
    nodeFormVisible.value = false;
  } catch (error) {
    console.error('保存节点失败:', error);
    ElMessage.error('保存节点失败: ' + (error instanceof Error ? error.message : String(error)));
  }
};

// --- Helper Functions & Others ---
const getOperationPermissionName = (id?: number | string) => {
  if (!id) return '-';
  
  // 转为字符串进行比较
  const idStr = String(id);
  const perm = allOperationPermissions.value.find(p => String(p.id) === idStr);
  
  return perm ? perm.permsName : idStr;
};

const getDataStrategyName = (id?: number | string) => {
  if (!id) return '-';
  
  // 转为字符串进行比较
  const idStr = String(id);
  const policy = allDataPolicies.value.find(p => String(p.id) === idStr);
  
  return policy ? policy.policyName : idStr;
};

// 保存节点排序
const saveModuleSortAndNodes = async () => {
  if (!selectedModule.value) return;
  
  try {
    // 处理节点排序，保存时使用sort字段
    for (const node of moduleNodes.value) {
      if (node.id) {
        try {
          // 创建一个新的对象，只包含需要的字段
          const updateData: any = {
            id: node.id,
            nodeName: node.nodeName,
            nodeType: node.nodeType,
            permType: node.permType,
            isBasic: typeof node.isBasic === 'number' ? node.isBasic : (node.isBasic ? 1 : 0),
            sort: node.sort || 0  // 使用sort字段而不是nodeSort
          };
          
          // 根据权限类型添加相应的ID
          if (node.permType === 0 && node.permId) {
            updateData.permId = node.permId;
          } else if (node.permType === 1 && node.dataPolicyId) {
            updateData.dataPolicyId = node.dataPolicyId;
          }
          
          // 数据字段类型时的数据范围
          if (node.nodeType === 3 && node.dataScope) {
            updateData.dataScope = node.dataScope;
          }
          
          console.log(`更新节点 ${node.nodeName} 排序，请求数据:`, updateData);
          
          // 发送更新请求
          await updateNode(updateData);
        } catch (e) {
          console.error(`更新节点 ${node.nodeName} 失败:`, e);
        }
      }
    }
    
    // 显示成功消息
    ElMessage.success('节点排序已保存');
    nodesChanged.value = false;
  } catch (error) {
    console.error('保存节点排序失败:', error);
    ElMessage.error('保存节点排序失败');
  }
};

// 保存完整配置
const saveFullConfiguration = async () => {
  if (!props.templateId || !props.templateVersionId) return;
  
  isSavingFullConfig.value = true;
  try {
    // 1. 保存所有模块的变更
    for (const module of templateModules.value) {
      await updateModule(module);
    }
    
    // 2. 对于所有模块加载并保存节点
    for (const module of templateModules.value) {
      if (!module.id) continue;
      
      const response = await getNodesByModule(module.id);
      if (response && response.data) {
        for (const node of response.data) {
          await updateNode(node);
        }
      }
    }
    
    ElMessage.success('当前版本的所有配置已保存成功');
  } catch (error) {
    console.error('保存配置失败:', error);
    ElMessage.error('保存配置失败');
  } finally {
    isSavingFullConfig.value = false;
  }
};

// 处理节点命令
const handleNodeCommand = async (command: string, node: ExtendedPermissionNode) => {
  switch (command) {
    case 'edit':
      await handleEditNode(node);
      break;
    case 'delete':
      await handleDeleteNode(node.id ? node.id.toString() : undefined);
      break;
  }
};

// 加载操作权限列表
const fetchOperationPermissions = async (searchKeyword?: string) => {
  try {
    console.log('加载操作权限列表，关键字:', searchKeyword);
    
    // 构建请求参数，添加搜索关键词
    const params: PermQueryParams = {
      pageNum: 1,
      pageSize: 100
    };
    
    // 根据搜索关键词设置查询条件
    if (searchKeyword) {
      params.permsName = searchKeyword;
    }
    
    const response = await getOperationPermissionsPage(params);
    console.log('操作权限列表加载响应:', response);
    
    // 处理响应数据
    if (response && response.data && response.data.records) {
      allOperationPermissions.value = response.data.records;
      // 确保ID是字符串
      allOperationPermissions.value = allOperationPermissions.value.map(perm => {
        if (perm.id && typeof perm.id !== 'string') {
          perm.id = String(perm.id);
        }
        return perm;
      });
      console.log('操作权限列表:', allOperationPermissions.value);
    } else if (response && Array.isArray(response)) {
      allOperationPermissions.value = response;
      console.log('操作权限列表(数组形式):', allOperationPermissions.value);
    } else if (response && response.records && Array.isArray(response.records)) {
      allOperationPermissions.value = response.records;
      console.log('操作权限列表(records):', allOperationPermissions.value);
    } else {
      console.warn('未获取到操作权限数据');
    }
  } catch (error) {
    console.error('加载操作权限列表失败:', error);
  }
};

// 加载数据策略列表
const fetchDataPolicies = async (searchKeyword?: string) => {
  try {
    console.log('加载数据策略列表，关键字:', searchKeyword);
    
    // 构建请求参数，添加搜索关键词
    const params: DataStrategyQueryParams = {
      pageNum: 1,
      pageSize: 100
    };
    
    // 根据搜索关键词设置查询条件
    if (searchKeyword) {
      params.policyName = searchKeyword;
    }
    
    const response = await getDataStrategiesPage(params);
    console.log('数据策略列表加载响应:', response);
    
    // 处理响应数据
    if (response && response.data && response.data.records) {
      allDataPolicies.value = response.data.records;
      // 确保ID是字符串
      allDataPolicies.value = allDataPolicies.value.map(policy => {
        if (policy.id && typeof policy.id !== 'string') {
          policy.id = String(policy.id);
        }
        return policy;
      });
      console.log('数据策略列表:', allDataPolicies.value);
    } else if (response && Array.isArray(response)) {
      allDataPolicies.value = response;
      console.log('数据策略列表(数组形式):', allDataPolicies.value);
    } else if (response && response.records && Array.isArray(response.records)) {
      allDataPolicies.value = response.records;
      console.log('数据策略列表(records):', allDataPolicies.value);
    } else {
      console.warn('未获取到数据策略数据');
    }
  } catch (error) {
    console.error('加载数据策略列表失败:', error);
  }
};

// 当组件挂载时，如果已经有模板版本ID，则加载模块
onMounted(() => {
  if (props.templateVersionId) {
    // 不在组件挂载时加载权限数据，改为按需加载
    // fetchOperationPermissions();
    // fetchDataPolicies();
    
    // 加载模块列表
    fetchModules();
  }
});

</script>

<style scoped>
.permission-module-editor {
  /* Styles for the editor component */
}
.module-panel, .node-panel {
  border: 1px solid var(--el-border-color-lighter);
  border-radius: var(--el-border-radius-base);
  height: 600px; /* Adjust as needed or make flexible */
  display: flex;
  flex-direction: column;
  background-color: #fff;
}
.panel-header { padding: 10px 15px; border-bottom: 1px solid var(--el-border-color-lighter); display: flex; justify-content: space-between; align-items: center; flex-shrink: 0; }
.panel-header h4 { margin: 0; font-size: 16px; }
.search-input { padding: 10px 15px; flex-shrink: 0; }
.module-list { flex-grow: 1; overflow-y: auto; padding: 5px; }
.module-item { display: flex; align-items: center; padding: 8px 15px; cursor: pointer; border-radius: var(--el-border-radius-base); margin-bottom: 2px; position: relative; }
.module-item:hover { background-color: var(--el-color-primary-light-9); }
.module-item.is-active { background-color: var(--el-color-primary-light-8); color: var(--el-color-primary); font-weight: 500; }
.module-item .el-icon { margin-right: 8px; }
.module-actions { position: absolute; right: 10px; top: 50%; transform: translateY(-50%); display: none; }
.module-item:hover .module-actions { display: inline-flex; }
.node-panel .node-content { flex-grow: 1; display: flex; flex-direction: column; overflow: hidden; }
.node-list-table { flex-grow: 1; margin: 0 15px 15px 15px; width: calc(100% - 30px); overflow: auto; }
.node-list-empty { flex-grow: 1; display: flex; justify-content: center; align-items: center; }
.text-muted { font-size: 0.9em; color: #909399; }
.cursor-grab { cursor: grab; }
.node-list-table-fixed-layout { table-layout: fixed; }

.module-type-badge { 
  background-color: #f0f0f0; 
  padding: 2px 6px; 
  border-radius: 10px; 
  font-size: 12px; 
  margin-left: 6px;
}
</style> 