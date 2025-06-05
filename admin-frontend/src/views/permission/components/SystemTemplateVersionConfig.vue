<template>
  <div class="system-template-version-config mb-6">
    <el-descriptions :title="`模板基础信息: ${templateInfo ? templateInfo.name : ''}`" :column="1" border>
      <template #extra>
        <el-button type="primary" link @click="openEditBaseInfoDialog">编辑描述</el-button>
      </template>
      <el-descriptions-item label="模板编码">{{ templateInfo ? templateInfo.templateCode : '' }}</el-descriptions-item>
      <el-descriptions-item label="当前描述">{{ templateInfo ? templateInfo.description : '' }}</el-descriptions-item>
    </el-descriptions>

    <el-card shadow="never" class="mt-4 version-management-card">
      <template #header>
        <div class="flex justify-between items-center">
          <span>版本管理</span>
          <div>
            <el-tooltip content="选择后，新租户创建时将默认使用此版本的权限配置" placement="top">
              <el-checkbox 
                v-model="isCurrentVersionDefault"
                label="作为默认版本"
                class="mr-4"
                @change="toggleDefaultVersion"
                :disabled="!selectedVersionDetails || versions.length === 0"
              />
            </el-tooltip>
            <el-button :icon="Plus" @click="promptNewVersionName">新增版本</el-button>
            <el-button 
              type="danger" 
              plain 
              :icon="Delete" 
              @click="confirmDeleteCurrentVersion" 
              :disabled="!selectedVersionDetails || versions.length <= 1"
            >
              删除当前版本 ({{ currentVersion || '' }})
            </el-button>
          </div>
        </div>
      </template>
      <el-form inline>
        <el-form-item label="选择配置版本">
          <el-select 
            v-model="versionSelectValue"
            placeholder="选择版本"
            style="width: 220px;"
            clearable
            @clear="clearVersionSelection"
            v-loading="versionsLoading"
          >
            <el-option 
              v-for="v in versions"
              :key="v.version"
              :label="`${v.version}${v.isDefault === 1 ? ' (默认)' : ''} - ${v.versionDescription || '无描述'}`"
              :value="v.version"
            />
            <template #dropdown>
              <el-scrollbar 
                @scroll="handleVersionListScroll" 
                height="200px" 
                ref="versionScrollbar"
              >
                <el-option 
                  v-for="v in versions"
                  :key="v.version"
                  :label="`${v.version}${v.isDefault === 1 ? ' (默认)' : ''} - ${v.versionDescription || '无描述'}`"
                  :value="v.version"
                />
                <div 
                  v-if="versionsLoading" 
                  class="text-center py-2 text-gray-400"
                >加载中...</div>
                <div 
                  v-if="!versionsLoading && hasMoreVersions" 
                  class="text-center py-2 text-gray-400 cursor-pointer"
                  @click="loadMoreVersions"
                >加载更多</div>
                <div 
                  v-if="!versionsLoading && !hasMoreVersions && versions.length > 0" 
                  class="text-center py-2 text-gray-400"
                >已加载全部</div>
              </el-scrollbar>
            </template>
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Dialog for editing base template info -->
    <el-dialog v-model="editDialogVisible" title="编辑模板描述" width="500px" :close-on-click-modal="false">
      <el-form label-width="80px">
        <el-form-item label="描述">
          <el-input type="textarea" :rows="3" v-model="editableDescription" placeholder="请输入模板描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveDescription">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted } from 'vue';
import { ElCard, ElForm, ElFormItem, ElSelect, ElOption, ElCheckbox, ElButton, ElTooltip, ElIcon, ElDialog, ElInput, ElMessage, ElMessageBox, ElDescriptions, ElDescriptionsItem } from 'element-plus';
import { Plus, Delete, InfoFilled } from '@element-plus/icons-vue';
import type { PropType } from 'vue';
import { 
  getTemplateByType, 
  updateTemplate,
  getVersionsPage, 
  createVersion, 
  updateVersion, 
  deleteVersion,
  getDefaultVersion,
  TemplateVersion
} from '@/api/templatePermission';

// 定义模板信息接口
interface TemplateInfo {
  id?: string | number;
  type: string;
  name: string;
  apiType: number;
  templateCode: string;
  description?: string;
  baseDescription?: string;
}

const props = defineProps({
  templateInfo: {
    type: Object as PropType<TemplateInfo>,
    required: true,
  },
  currentVersion: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['update:currentVersion', 'version-changed', 'save-base-info']);

const versions = ref<TemplateVersion[]>([]);
const versionsLoading = ref(false);
const editableDescription = ref('');
const editDialogVisible = ref(false);
const templateId = ref<string | number | null>(null);
const currentPage = ref<number>(1);
const pageSize = ref<number>(20);
const totalVersions = ref<number>(0);
const hasMoreVersions = ref<boolean>(false);
const versionScrollbar = ref<any>(null);

// 用于处理el-select的v-model绑定问题
const versionSelectValue = computed({
  get: () => props.currentVersion || '',
  set: (val: string) => {
    emit('update:currentVersion', val);
    emit('version-changed', val);
  }
});

// 计算当前选中的版本详情
const selectedVersionDetails = computed(() => {
  // 1. 首先尝试匹配版本字符串
  if (props.currentVersion && versions.value.length > 0) {
    const foundVersion = versions.value.find(v => v.version === props.currentVersion);
    if (foundVersion) {
      return foundVersion;
    }
  }
  
  // 2. 如果没有找到匹配版本，使用默认版本
  if (versions.value.length > 0) {
    const defaultVersion = versions.value.find(v => v.isDefault === 1);
    if (defaultVersion) {
      return defaultVersion;
    }
  }
  
  // 3. 如果仍然没有版本，返回null
  return null;
});

// 计算当前版本是否是默认版本
const isCurrentVersionDefault = computed({
  get: () => selectedVersionDetails.value?.isDefault === 1,
  set: (value) => {
    // This setter is primarily for the checkbox state. Actual default setting is handled by toggleDefaultVersion
  }
});

// 加载模板详情和版本列表
const fetchTemplateAndVersions = async () => {
  if (!props.templateInfo) return;
  try {
    // 根据模板类型获取模板详情
    const templateResponse = await getTemplateByType(props.templateInfo.apiType);
    console.log('获取到的模板详情:', templateResponse);
    
    // 确保能够正确获取模板ID
    if (templateResponse) {
      if (typeof templateResponse === 'object' && 'id' in templateResponse) {
        // 直接从响应中获取id，确保是字符串类型
        templateId.value = typeof templateResponse.id === 'number' 
          ? templateResponse.id.toString() 
          : templateResponse.id as string;
        console.log('设置模板ID (直接):', templateId.value);
      } else if (templateResponse.data && typeof templateResponse.data === 'object' && 'id' in templateResponse.data) {
        // 从data中获取id，确保是字符串类型
        templateId.value = typeof templateResponse.data.id === 'number' 
          ? templateResponse.data.id.toString() 
          : templateResponse.data.id as string;
        console.log('设置模板ID (data):', templateId.value);
      }
      
      // 先尝试获取默认版本
      if (templateId.value) {
        try {
          const defaultVersionResponse = await getDefaultVersion(templateId.value);
          console.log('获取默认版本响应:', defaultVersionResponse);
          
          if (defaultVersionResponse && defaultVersionResponse.data) {
            // 如果存在默认版本且当前未选择版本，则自动选择默认版本
            const defaultVersionInfo = defaultVersionResponse.data;
            console.log('找到默认版本:', defaultVersionInfo);
            
            if (!props.currentVersion) {
              emit('update:currentVersion', defaultVersionInfo.version);
              emit('version-changed', defaultVersionInfo.version);
            }
          }
        } catch (error) {
          console.warn('获取默认版本失败，将尝试从版本列表中查找:', error);
        }
      }
      
      // 获取模板版本列表
      await fetchVersions();
    }
  } catch (error) {
    console.error('获取模板详情失败:', error);
    ElMessage.error('获取模板详情失败');
  }
};

// 加载版本列表
const fetchVersions = async (loadMore = false) => {
  if (!templateId.value) {
    console.log('fetchVersions: 模板ID不存在，无法加载版本');
    return;
  }
  
  // 如果是加载更多，则增加页码；如果是重新加载，则重置页码
  if (!loadMore) {
    currentPage.value = 1;
    versions.value = [];
  }
  
  versionsLoading.value = true;
  try {
    console.log('获取版本列表 - 请求参数:', {
      templateId: templateId.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    });
    
    const response = await getVersionsPage({
      templateId: templateId.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    });
    
    console.log('获取版本列表 - 响应:', response);
    
    // 直接处理响应对象，考虑不同的后端返回结构
    if (response && typeof response === 'object') {
      // 可能是直接返回的数据对象
      const anyResponse = response as any; // 使用any类型避免TypeScript错误
      let newVersions: TemplateVersion[] = [];
      let total = 0;
      
      if (anyResponse.records && Array.isArray(anyResponse.records)) {
        newVersions = anyResponse.records;
        total = anyResponse.total || 0;
      } 
      // 也可能是包裹在data对象中
      else if (anyResponse.data && typeof anyResponse.data === 'object') {
        if (anyResponse.data.records && Array.isArray(anyResponse.data.records)) {
          newVersions = anyResponse.data.records;
          total = anyResponse.data.total || 0;
        } else if (Array.isArray(anyResponse.data)) {
          // 直接是数组的情况
          newVersions = anyResponse.data;
          total = anyResponse.data.length; // 假设全部返回
        }
      }
      
      // 更新版本列表
      if (loadMore) {
        versions.value = [...versions.value, ...newVersions];
      } else {
        versions.value = newVersions;
      }
      
      // 更新总数和是否有更多数据
      totalVersions.value = total;
      hasMoreVersions.value = versions.value.length < totalVersions.value;
      
      console.log('获取到的版本列表:', versions.value);
      console.log('版本总数:', totalVersions.value);
      console.log('是否有更多:', hasMoreVersions.value);
      
      // 如果没有选择版本或选择的版本不在列表中，自动选择默认版本
      if (!props.currentVersion || !versions.value.some(v => v.version === props.currentVersion)) {
        const defaultVersion = versions.value.find(v => v.isDefault === 1);
        const versionToSelect = defaultVersion ? defaultVersion.version : 
                               (versions.value.length > 0 ? versions.value[0].version : '');
        
        console.log('自动选择版本:', versionToSelect, defaultVersion);
        
        if (versionToSelect) {
          emit('update:currentVersion', versionToSelect);
          emit('version-changed', versionToSelect);
        }
      }
    } else {
      console.warn('获取版本列表: 服务器返回数据格式不符合预期', response);
    }
  } catch (error) {
    console.error('获取版本列表失败:', error);
    ElMessage.error('获取版本列表失败');
  } finally {
    versionsLoading.value = false;
  }
};

// 加载更多版本
const loadMoreVersions = () => {
  if (versionsLoading.value || !hasMoreVersions.value) return;
  
  currentPage.value += 1;
  fetchVersions(true);
};

// 处理版本列表滚动
const handleVersionListScroll = (e: any) => {
  if (versionsLoading.value || !hasMoreVersions.value) return;
  
  const { scrollTop, clientHeight, scrollHeight } = e.target;
  // 当滚动到接近底部时加载更多
  if (scrollTop + clientHeight >= scrollHeight - 20) {
    loadMoreVersions();
  }
};

// 监听模板信息变化
watch(() => props.templateInfo, (newInfo) => {
  if (newInfo) {
    fetchTemplateAndVersions();
  }
}, { immediate: true });

// 监听versionSelectValue变化，确保事件触发
watch(() => versionSelectValue.value, (newVersion) => {
  console.log('版本选择值变化:', newVersion);
  if (newVersion !== props.currentVersion) {
    emit('update:currentVersion', newVersion);
    emit('version-changed', newVersion);
  }
});

// 监听currentVersion变化，确保下拉框显示正确的选项
watch(() => props.currentVersion, (newVersion) => {
  if (newVersion && !versionSelectValue.value) {
    console.log('外部更新了版本值:', newVersion);
    // 不直接更新versionSelectValue，因为它是计算属性
    // 但需确保选项数据存在
    if (versions.value.length === 0 || !versions.value.some(v => v.version === newVersion)) {
      // 如果版本列表为空或没有这个版本，尝试重新加载版本列表
      console.log('需要重新加载版本列表以显示当前版本');
      if (templateId.value) {
        fetchVersions();
      }
    }
  }
}, { immediate: true });

// 清除版本选择
const clearVersionSelection = () => {
  emit('update:currentVersion', '');
  emit('version-changed', '');
};

// 打开编辑基本信息对话框
const openEditBaseInfoDialog = () => {
  // 检查是否获取到了模板信息
  console.log('打开编辑对话框，当前模板信息:', props.templateInfo);
  
  // 从模板信息中获取描述，兼容baseDescription和description字段
  editableDescription.value = props.templateInfo?.baseDescription || props.templateInfo?.description || '';
  
  console.log('设置初始描述值:', editableDescription.value);
  editDialogVisible.value = true;
};

// 保存描述信息
const saveDescription = async () => {
  console.log('开始保存描述 - 检查模板信息', {
    templateInfo: props.templateInfo,
    templateId: templateId.value
  });

  // 确保有模板ID，尝试多种方式获取
  let effectiveTemplateId = templateId.value || 
                           props.templateInfo?.id || 
                           (props.templateInfo && typeof props.templateInfo === 'object' && 'id' in props.templateInfo 
                            ? (props.templateInfo.id) : null);
  
  // 确保ID是字符串类型
  if (effectiveTemplateId !== null) {
    effectiveTemplateId = String(effectiveTemplateId);
  }
  
  if (!effectiveTemplateId) {
    console.error('无法保存描述: 模板ID不存在', { 
      templateInfo: props.templateInfo, 
      templateId: templateId.value, 
      effectiveTemplateId 
    });
    ElMessage.error('模板信息不完整，无法保存描述');
    return;
  }
  
  try {
    // 打印调试信息
    const requestData = {
      id: effectiveTemplateId,
      templateCode: props.templateInfo.templateCode,
      templateType: props.templateInfo.apiType, // 注意：templateType与apiType对应
      baseDescription: editableDescription.value
    };
    
    console.log('保存模板描述 - 请求参数:', requestData);
    
    // 开发环境额外调试
    console.log('请求URL:', '/perm/api/perm/templates');
    console.log('请求方法:', 'PUT');
    
    // 发送请求更新模板
    const response = await updateTemplate(requestData);
    
    console.log('模板描述更新响应:', response);
    
    // 请求成功后更新本地状态
    // 更新基本信息并通知父组件
    emit('save-base-info', { 
      ...props.templateInfo, 
      description: editableDescription.value,
      baseDescription: editableDescription.value // 同时更新两个字段确保一致性
    });
    
    ElMessage.success('模板描述更新成功');
    editDialogVisible.value = false;
  } catch (error) {
    console.error('更新模板描述失败:', error);
    ElMessage.error(`更新模板描述失败: ${(error as any)?.message || '未知错误'}`);
  }
};

// 新增版本
const promptNewVersionName = async () => {
  console.log('开始创建版本 - 检查模板信息', {
    templateInfo: props.templateInfo,
    templateId: templateId.value
  });

  // 确保有模板ID，尝试多种方式获取
  let effectiveTemplateId = templateId.value || 
                           props.templateInfo?.id || 
                           (props.templateInfo && typeof props.templateInfo === 'object' && 'id' in props.templateInfo 
                            ? (props.templateInfo.id) : null);
  
  // 确保ID是字符串类型
  if (effectiveTemplateId !== null) {
    effectiveTemplateId = String(effectiveTemplateId);
  }
  
  if (!effectiveTemplateId) {
    console.error('无法创建版本: 模板ID不存在', { 
      templateInfo: props.templateInfo, 
      templateId: templateId.value, 
      effectiveTemplateId 
    });
    ElMessage.error('模板信息不完整，无法创建版本');
    return;
  }
  
  console.log('确认模板ID有效:', effectiveTemplateId);
    
  try {
    // 使用element-plus的对话框获取用户输入
    const result = await ElMessageBox.prompt('请输入新版本号 (例如: 1.0.1)', '新增版本', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /^\S+$/,
      inputErrorMessage: '版本号不能为空',
      distinguishCancelAndClose: true // 区分取消和关闭
    });
    
    const value = result.value;
    console.log('用户输入的版本号:', value);
    
    if (!value || value.trim() === '') {
      console.log('用户输入为空，不创建版本');
      return;
    }
    
    // 检查版本号是否已存在
    if (versions.value.some(v => v.version === value)) {
      console.warn('版本号已存在:', value);
      ElMessage.warning('版本号已存在，请使用唯一的版本号');
      return;
    }
    
    // 构建请求参数
    const newVersion = {
      templateId: effectiveTemplateId,
      version: value,
      isDefault: 0,
      versionDescription: `基于 ${props.currentVersion || '最新配置'} 创建的新版本`
    };
    
    console.log('创建版本请求参数:', newVersion);
    console.log('请求URL:', '/perm/api/perm/versions');
    console.log('请求方法:', 'POST');
    
    // 调用API创建版本
    try {
      const response = await createVersion(newVersion);
      console.log('创建版本响应:', response);
      
      // 创建成功
      await fetchVersions(); // 重新获取版本列表
      
      // 选中新创建的版本
      emit('update:currentVersion', value);
      emit('version-changed', value);
      
      ElMessage.success(`版本 ${value} 创建成功`);
    } catch (apiError: any) {
      console.error('API调用失败:', apiError);
      ElMessage.error(`创建版本失败: ${apiError.message || '请求异常'}`);
    }
  } catch (error: any) {
    // 处理对话框异常（如取消）
    console.log('对话框操作:', error);
    
    // 只有非取消的错误才显示错误信息
    if (error !== 'cancel' && error !== 'close') {
      console.error('创建版本过程异常:', error);
      ElMessage.error('创建版本失败');
    }
  }
};

// 删除当前版本
const confirmDeleteCurrentVersion = async () => {
  if (!props.currentVersion || !selectedVersionDetails.value || !selectedVersionDetails.value.id) return;
  
  try {
    await ElMessageBox.confirm(
      `确定要删除版本 "${props.currentVersion}" 吗？此操作不可恢复。`,
      '确认删除版本',
      { type: 'warning' }
    );
    
    // 确保ID是字符串类型
    const versionIdString = String(selectedVersionDetails.value.id);
    
    await deleteVersion(versionIdString);
    ElMessage.success(`版本 ${props.currentVersion} 已删除`);
    
    emit('update:currentVersion', '');
    emit('version-changed', '');
    
    // 重新加载版本列表
    await fetchVersions();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除版本失败:', error);
      ElMessage.error('删除版本失败');
    }
  }
};

// 设置/取消默认版本
const toggleDefaultVersion = async () => {
  if (!selectedVersionDetails.value || !templateId.value) return;
  
  const versionId = selectedVersionDetails.value.id;
  if (!versionId) return;
  
  // 确保ID是字符串类型
  const versionIdString = String(versionId);
  
  const newDefaultState = selectedVersionDetails.value.isDefault === 1 ? 0 : 1;
  
  try {
    await updateVersion({
      id: versionIdString,
      version: selectedVersionDetails.value.version,
      isDefault: newDefaultState,
      versionDescription: selectedVersionDetails.value.versionDescription
    });
    
    // 重新加载版本列表以更新状态
    await fetchVersions();
    
    ElMessage.success(`版本 ${selectedVersionDetails.value.version} 已${newDefaultState === 1 ? '设为' : '取消'}默认`);
  } catch (error) {
    console.error('设置默认版本失败:', error);
    ElMessage.error('设置默认版本失败');
  }
};

onMounted(() => {
  if (!props.templateInfo && versions.value.length === 0) {
    console.warn('SystemTemplateVersionConfig: templateInfo not available on mount, or versions already loaded.');
  }
});

</script>

<style scoped>
.version-management-card .el-form-item {
  margin-bottom: 0; /* Remove default bottom margin for inline form */
}

.version-management-card :deep(.el-form-item__label) {
  font-weight: bold;
}

/* Add highlight for default version in the dropdown */
:deep(.el-select-dropdown__item.selected) {
  font-weight: bold;
  color: var(--el-color-primary);
}
</style> 