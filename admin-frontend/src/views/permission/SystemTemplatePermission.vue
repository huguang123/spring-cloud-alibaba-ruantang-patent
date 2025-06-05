<template>
  <div class="system-template-permission-container p-4">
    <PageHeader title="系统模板权限配置" subtitle="配置平台、企业、代理所的默认权限模板" />

    <SystemTemplateTypeSelector 
      v-model:active-template-type="currentTemplateType"
      @type-selected="handleTemplateTypeSelected"
    />

    <el-card v-if="selectedTemplateInfo" class="mt-4">
      <SystemTemplateVersionConfig 
        :template-info="selectedTemplateInfo"
        v-model:current-version="selectedVersionString"
        @version-changed="handleVersionChanged"
        @save-base-info="handleSaveBaseInfo"
      />

      <div v-if="selectedTemplateInfo && selectedVersionString" class="mt-6">
        <h3 class="text-lg font-semibold mb-3">
          配置模板: {{ selectedTemplateInfo.name }} (版本: {{ selectedVersionString }})
        </h3>
        <PermissionModuleEditor 
          :template-id="selectedTemplateId" 
          :template-version-id="selectedVersionId"
          :key="`${selectedTemplateId || 0}-${selectedVersionId || 0}`" 
        />
      </div>
       <el-empty v-else-if="selectedTemplateInfo && !selectedVersionString" description="请选择或创建一个版本进行配置" />
    </el-card>
    <el-empty v-else description="请先选择一个系统模板类型开始配置" />

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import PageHeader from '@/components/PageHeader.vue';
import SystemTemplateTypeSelector from './components/SystemTemplateTypeSelector.vue';
import SystemTemplateVersionConfig from './components/SystemTemplateVersionConfig.vue';
import PermissionModuleEditor from './components/PermissionModuleEditor.vue';
import { ElCard, ElEmpty, ElMessage } from 'element-plus';
import { getVersionsPage, getTemplateByType, getModulesByVersion, getDefaultVersion } from '@/api/templatePermission';
import { handleApiError, handleApiResponse } from '@/utils/errorHandler';

// 当前选择的模板类型
const currentTemplateType = ref<string>('enterprise'); // 默认选择企业模板

// 选择的模板信息
const selectedTemplateInfo = ref<any>(null);

// 选择的版本
const selectedVersionString = ref<string>('');
const selectedVersionId = ref<number | string | null>(null);
const selectedTemplateId = ref<number | string | null>(null);

// 存储已加载的版本模块数据
const moduleCache = ref<Record<string, any[]>>({});

// 处理模板类型选择
const handleTemplateTypeSelected = async (template: any) => {
  console.log('模板类型选择:', template);
  selectedTemplateInfo.value = template;
  
  // 先清空版本数据
  selectedVersionString.value = '';
  selectedVersionId.value = null;
  selectedTemplateId.value = null;
  
  // 当选择新的模板类型时，立即获取模板ID
  if (template && template.apiType) {
    try {
      console.log('获取模板详情, 类型:', template.apiType);
      const response = await getTemplateByType(template.apiType);
      console.log('获取模板详情响应:', response);
      
      if (response && response.data) {
        selectedTemplateId.value = response.data.id;
        console.log('设置模板ID:', selectedTemplateId.value);
        
        // 获取该模板的版本列表和默认版本
        await fetchTemplateVersions();
      } else if (response && typeof response === 'object' && 'id' in response) {
        selectedTemplateId.value = (response as any).id as number;
        console.log('设置模板ID (直接从响应):', selectedTemplateId.value);
        
        // 获取该模板的版本列表和默认版本
        await fetchTemplateVersions();
      } else {
        console.warn('未获取到模板详情');
      }
    } catch (error) {
      console.error('获取模板详情失败:', error);
      handleApiError(error, '获取模板详情失败');
    }
  }
};

// 获取模板版本列表并选择默认版本
const fetchTemplateVersions = async () => {
  if (!selectedTemplateId.value) {
    console.warn('未获取到模板ID，无法加载版本列表');
    return;
  }
  
  try {
    // 1. 先获取默认版本
    console.log('获取默认版本，模板ID:', selectedTemplateId.value);
    const defaultVersionResponse = await getDefaultVersion(selectedTemplateId.value);
    console.log('默认版本响应:', defaultVersionResponse);
    
    // 记录默认版本ID和版本号
    let defaultVersionId: string | null = null;
    let defaultVersionString: string | null = null;
    
    if (defaultVersionResponse && defaultVersionResponse.data) {
      const defaultVersionInfo = defaultVersionResponse.data;
      // 确保ID是字符串类型
      defaultVersionId = defaultVersionInfo.id ? defaultVersionInfo.id.toString() : null;
      defaultVersionString = defaultVersionInfo.version;
      console.log('获取到默认版本:', defaultVersionInfo);
    }
    
    // 2. 获取第一页版本列表（主要用于展示）
    console.log('获取版本列表，模板ID:', selectedTemplateId.value);
    const versionsResponse = await getVersionsPage({
      templateId: selectedTemplateId.value,
      pageNum: 1,
      pageSize: 20
    });
    
    console.log('版本列表响应:', versionsResponse);
    
    // 如果没有获取到默认版本，尝试从版本列表中找出默认版本或第一个版本
    if (!defaultVersionId && versionsResponse) {
      const anyResponse = versionsResponse as any;
      let versionsList: any[] = [];
      
      if (anyResponse && anyResponse.data && anyResponse.data.records) {
        versionsList = anyResponse.data.records;
      } else if (anyResponse && anyResponse.records) {
        versionsList = anyResponse.records;
      } else if (anyResponse && Array.isArray(anyResponse)) {
        versionsList = anyResponse;
      } else if (anyResponse && Array.isArray(anyResponse.data)) {
        versionsList = anyResponse.data;
      }
      
      if (versionsList.length > 0) {
        // 尝试从列表中找出默认版本
        const defaultVersion = versionsList.find((v: any) => v.isDefault === 1);
        if (defaultVersion) {
          console.log('从列表中找到默认版本:', defaultVersion);
          // 确保ID是字符串类型
          defaultVersionId = defaultVersion.id ? defaultVersion.id.toString() : null;
          defaultVersionString = defaultVersion.version;
        } else {
          // 找不到默认版本则使用第一个版本
          console.log('未找到默认版本，使用第一个版本:', versionsList[0]);
          // 确保ID是字符串类型
          defaultVersionId = versionsList[0].id ? versionsList[0].id.toString() : null;
          defaultVersionString = versionsList[0].version;
        }
      }
    }
    
    // 设置选中的版本和版本ID
    if (defaultVersionId && defaultVersionString) {
      selectedVersionId.value = defaultVersionId;
      selectedVersionString.value = defaultVersionString;
      console.log('自动选择版本:', defaultVersionString, defaultVersionId);
      
      // 加载该版本的模块列表
      await loadModulesByVersion(defaultVersionId);
    } else {
      console.warn('未获取到任何版本数据');
    }
  } catch (error) {
    console.error('获取版本数据失败:', error);
    handleApiError(error, '获取版本数据失败');
  }
};

// 根据版本ID加载模块列表
const loadModulesByVersion = async (versionId: number | string) => {
  if (!versionId) {
    console.warn('无效的版本ID，无法加载模块列表');
    return;
  }
  
  // 直接使用字符串ID，不做任何处理
  const versionIdStr = String(versionId);
  
  // 记录详细的调试信息
  console.log('loadModulesByVersion - 版本ID信息:', {
    originalId: versionId,
    idType: typeof versionId,
    stringId: versionIdStr,
    length: versionIdStr.length,
    hasCached: Boolean(moduleCache.value[versionIdStr])
  });
  
  // 检查缓存中是否有此版本的模块数据
  if (moduleCache.value[versionIdStr]) {
    console.log(`使用缓存中的模块数据 - 版本ID: ${versionIdStr}, 模块数量: ${moduleCache.value[versionIdStr].length}`);
    return;
  }
  
  try {
    // 在发起请求前，先创建一个空缓存，防止重复请求
    moduleCache.value[versionIdStr] = [];
    
    // 直接使用字符串ID进行请求，避免任何转换
    const response = await getModulesByVersion(versionIdStr);
    console.log('模块列表加载响应:', response);
    
    if (response) {
      // 提取模块数据
      let modules: any[] = [];
      
      if (Array.isArray(response)) {
        modules = response;
      } else if (response.data && Array.isArray(response.data)) {
        modules = response.data;
      } else if (response.data && response.data.records && Array.isArray(response.data.records)) {
        modules = response.data.records;
      }
      
      // 确保所有ID都是字符串格式
      modules = modules.map(mod => {
        if (mod.id && typeof mod.id !== 'string') {
          mod.id = String(mod.id);
        }
        if (mod.templateVersionId && typeof mod.templateVersionId !== 'string') {
          mod.templateVersionId = String(mod.templateVersionId);
        }
        return mod;
      });
      
      // 存储到缓存中
      if (modules.length > 0) {
        moduleCache.value[versionIdStr] = modules;
        console.log(`成功加载模块 - 版本ID: ${versionIdStr}, 模块数量: ${modules.length}`);
      } else {
        console.log(`该版本无模块 - 版本ID: ${versionIdStr}`);
      }
    }
  } catch (error) {
    console.error('加载模块列表失败:', error);
    handleApiError(error, '加载模块列表失败');
    // 清除空缓存
    delete moduleCache.value[versionIdStr];
  }
};

// 清除缓存的方法，在模板或版本变化时调用
const clearModuleCache = () => {
  moduleCache.value = {};
  console.log('已清除模块数据缓存');
};

// 处理版本变更
const handleVersionChanged = async (version: string | null) => {
  console.log('版本变更:', version);
  selectedVersionString.value = version || '';
  
  if (version && selectedTemplateInfo.value && selectedTemplateInfo.value.apiType) {
    // 查询版本ID
    try {
      console.log('查询版本ID - 参数:', {
        templateId: selectedTemplateId.value,
        pageNum: 1,
        pageSize: 100
      });
      
      const response = await getVersionsPage({
        templateId: selectedTemplateId.value || 0,
        pageNum: 1,
        pageSize: 100
      });
      
      console.log('查询版本响应:', response);
      
      let versionInfo = null;
      
      // 处理不同的响应结构
      if (response) {
        const anyResponse = response as any;
        let versionsList: any[] = [];
        
        // 直接是数组的情况
        if (Array.isArray(anyResponse)) {
          versionsList = anyResponse;
        }
        // data字段是数组的情况
        else if (anyResponse.data && Array.isArray(anyResponse.data)) {
          versionsList = anyResponse.data;
        }
        // 嵌套在records中的情况
        else if (anyResponse.data && anyResponse.data.records && Array.isArray(anyResponse.data.records)) {
          versionsList = anyResponse.data.records;
        }
        // 直接在records中的情况
        else if (anyResponse.records && Array.isArray(anyResponse.records)) {
          versionsList = anyResponse.records;
        }
        
        // 查找匹配的版本信息
        versionInfo = versionsList.find((v: any) => v.version === version);
      }
      
      if (versionInfo) {
        // 保持ID为字符串，避免精度丢失
        const versionId = versionInfo.id.toString();
        
        // 打印详细的ID信息，帮助调试
        console.log('获取到版本信息:', {
          id: versionInfo.id,
          idType: typeof versionInfo.id,
          stringId: versionId,
          idLength: versionId.length
        });
        
        selectedVersionId.value = versionId;
        console.log('设置版本ID为:', versionId);
        
        // 根据版本ID加载模块列表 - 确保ID是完整的字符串
        await loadModulesByVersion(versionId);
      } else {
        console.warn('未找到匹配的版本信息:', version);
        // 清空版本ID，避免使用旧的
        selectedVersionId.value = null;
      }
    } catch (error) {
      console.error('获取版本详情失败:', error);
      handleApiError(error, '获取版本详情失败');
    }
  } else {
    selectedVersionId.value = null;
    console.log('清空版本ID');
  }
};

// 处理保存基本信息
const handleSaveBaseInfo = (updatedInfo: any) => {
  if (selectedTemplateInfo.value) {
    // 在实际API调用之后，更新本地状态
    selectedTemplateInfo.value = { ...selectedTemplateInfo.value, ...updatedInfo };
  }
};

// 监听模板信息变化，但不再重复获取模板ID
watch(() => selectedTemplateInfo.value, (newInfo) => {
  // 空方法，移除了原有的getTemplateInfo调用以避免重复请求
  console.log('模板信息变化:', newInfo?.type);
}, { immediate: true });

// 初始加载
onMounted(() => {
  // 基本配置已经在SystemTemplateTypeSelector组件中处理
});
</script>

<style scoped>
.system-template-permission-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 50px); /* Adjust as needed */
}
.el-card {
  overflow: visible; /* Allow dropdowns from select to overflow if needed */
}
</style> 