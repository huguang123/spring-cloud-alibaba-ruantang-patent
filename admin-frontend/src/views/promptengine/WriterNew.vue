<template>
  <div class="container mx-auto px-4 py-6 max-w-7xl">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">技术交底书智能撰写</h1>
    
    <!-- 输入区域 -->
    <el-card class="mb-8">
      <template #header>
        <h2 class="text-lg font-semibold">技术描述输入</h2>
      </template>
      
      <div class="grid grid-cols-1 gap-4 mb-4">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-gray-700 mb-2">一级技术领域</label>
            <el-select 
              v-model="selectedDomainIds.level1" 
              class="w-full"
              placeholder="-- 请选择一级领域 --"
              @change="handleLevel1Change"
            >
              <el-option 
                v-for="domain in domains" 
                :key="domain.id" 
                :label="domain.domain_name" 
                :value="domain.id"
              />
            </el-select>
          </div>
          
          <div>
            <label class="block text-gray-700 mb-2">二级技术领域</label>
            <el-select 
              v-model="selectedDomainIds.level2" 
              class="w-full"
              placeholder="-- 请选择二级领域 --"
              :disabled="!selectedDomainIds.level1"
              @change="handleLevel2Change"
            >
              <el-option 
                v-for="domain in level2Domains" 
                :key="domain.id" 
                :label="domain.domain_name" 
                :value="domain.id"
              />
            </el-select>
          </div>
          
          <div>
            <label class="block text-gray-700 mb-2">三级技术领域</label>
            <el-select 
              v-model="selectedDomainIds.level3" 
              class="w-full"
              placeholder="-- 请选择三级领域 --"
              :disabled="!selectedDomainIds.level2"
              @change="handleLevel3Change"
            >
              <el-option 
                v-for="domain in level3Domains" 
                :key="domain.id" 
                :label="domain.domain_name" 
                :value="domain.id"
              />
            </el-select>
          </div>
        </div>
        
        <div>
          <label class="block text-gray-700 mb-2">文档模板</label>
          <el-select 
            v-model="selectedTemplate" 
            class="w-full"
            placeholder="-- 请选择文档模板 --"
            :disabled="!selectedDomainIds.level3 && !selectedDomainIds.level2 && !selectedDomainIds.level1"
          >
            <el-option 
              v-for="template in docTemplates" 
              :key="template.id" 
              :label="template.template_name" 
              :value="template.id"
            />
          </el-select>
        </div>
      </div>
      
      <div class="mb-4">
        <label class="block text-gray-700 mb-2">技术概述</label>
        <el-input
          v-model="userInput"
          type="textarea"
          :rows="6"
          placeholder="请输入您的技术描述..."
          resize="vertical"
        />
      </div>
      
      <div class="flex justify-end items-center space-x-4">
        <div class="flex items-center">
          <el-checkbox v-model="useAI">使用AI辅助生成</el-checkbox>
        </div>
        <el-button 
          type="primary"
          @click="generateContent"
          :loading="loading"
        >
          <el-icon v-if="!loading"><Document /></el-icon>
          <span>{{ loading ? '生成中...' : '生成文档' }}</span>
        </el-button>
      </div>
    </el-card>
    
    <!-- 加载状态显示 -->
    <el-card v-if="loading" class="text-center">
      <div class="py-10">
        <el-icon class="is-loading mb-4" :size="30"><Loading /></el-icon>
        <p class="text-gray-700">正在生成技术交底书，请稍候...</p>
      </div>
    </el-card>
    
    <!-- 结果预览区 -->
    <el-card v-if="hasContent && !loading">
      <template #header>
        <div class="flex justify-between">
          <h2 class="text-lg font-semibold">技术交底书预览</h2>
          <div class="flex space-x-2">
            <el-button size="small" @click="exportToWord" :loading="exportLoading">
              <el-icon><Download /></el-icon> 导出Word
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 动态循环展示所有sections -->
      <div class="space-y-6">
        <el-card
          v-for="section in sections"
          :key="section.sectionName"
          class="mb-4"
          shadow="hover"
        >
          <template #header>
            <div class="flex justify-between items-center">
              <h3 class="font-medium">{{ section.sectionName }}</h3>
              <div class="flex space-x-2">
                <el-button 
                  text
                  @click="toggleEdit(section.sectionName)" 
                  :type="isEditing[section.sectionName] ? 'success' : 'primary'"
                  size="small"
                >
                  <el-icon>
                    <component :is="isEditing[section.sectionName] ? 'Check' : 'EditPen'" />
                  </el-icon>
                  <span>{{ isEditing[section.sectionName] ? '保存' : '编辑' }}</span>
                </el-button>
                <el-button 
                  text
                  type="primary"
                  @click="regenerateSection(section.sectionName)" 
                  :disabled="isEditing[section.sectionName]"
                  size="small"
                >
                  <el-icon><RefreshRight /></el-icon> 重新生成
                </el-button>
              </div>
            </div>
          </template>
          
          <div v-if="!isEditing[section.sectionName]" class="p-4 text-gray-700" v-html="formatContent(section.content)"></div>
          <div v-else class="p-4">
            <el-input
              v-model="editingSections[section.sectionName]"
              type="textarea"
              :rows="6"
              resize="vertical"
            />
          </div>
        </el-card>
      </div>
    </el-card>
    
    <!-- 欢迎提示 -->
    <el-card v-if="!hasContent && !loading" class="text-center">
      <div class="py-10">
        <el-icon class="text-primary mb-4" :size="40"><Document /></el-icon>
        <h2 class="text-xl font-semibold mb-2">开始创建您的技术交底书</h2>
        <p class="text-gray-600 mb-6">选择技术领域并输入技术描述，系统将自动为您生成专业的技术交底书</p>
        <el-button type="primary" @click="scrollToInput">
          <el-icon><EditPen /></el-icon> 开始撰写
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox, ElButton, ElInput, ElForm, ElFormItem, ElDialog, ElCard, ElSelect, ElOption, ElSwitch, ElTooltip, ElIcon, ElRow, ElCol, ElDivider, ElEmpty, ElLoading, ElProgress, ElSteps, ElStep } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh, View, Document, Setting, Download, Upload } from '@element-plus/icons-vue'
import { handleApiError, handleApiResponse } from '@/utils/errorHandler'

// 类型定义
interface Domain {
  id: string;
  domain_name: string;
  parent_id?: string;
  level: number;
}

interface Template {
  id: string;
  template_name: string;
}

interface DocumentSection {
  sectionName: string;
  content: string;
}

interface SelectedDomains {
  level1: string;
  level2: string;
  level3: string;
}

// API配置
const API_BASE_URL = '/prom/api';

// 数据状态
const userInput = ref('');
const selectedDomainIds = reactive<SelectedDomains>({
  level1: '',
  level2: '',
  level3: ''
});
const selectedTemplate = ref('');
const loading = ref(false);
const exportLoading = ref(false);
const domains = ref<Domain[]>([]);
const level2Domains = ref<Domain[]>([]);
const level3Domains = ref<Domain[]>([]);
const docTemplates = ref<Template[]>([]);
const useAI = ref(true);
const sections = ref<DocumentSection[]>([]);
const isEditing = reactive<Record<string, boolean>>({});
const editingSections = reactive<Record<string, string>>({});

// 获取HTTP请求头
function getRequestHeaders() {
  // 获取token
  const token = localStorage.getItem('auth_token') || 
                (window as any).auth?.getToken?.();
  if (!token) {
    console.warn('未找到认证token');
    return {
      'Content-Type': 'application/json'
    };
  }
  
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };
}

// 计算属性
const hasContent = computed(() => {
  return sections.value && sections.value.length > 0;
});

// 格式化内容
function formatContent(text: string) {
  if (!text) return '';
  return text.replace(/\n/g, '<br>');
}

// 滚动到输入区域
function scrollToInput() {
  const inputSection = document.querySelector('.mb-8');
  if (inputSection) {
    inputSection.scrollIntoView({ behavior: 'smooth' });
  }
}

// 处理一级领域选择变化
function handleLevel1Change() {
  selectedDomainIds.level2 = '';
  selectedDomainIds.level3 = '';
  level2Domains.value = [];
  level3Domains.value = [];
  docTemplates.value = [];
  
  if (selectedDomainIds.level1) {
    loadLevel2Domains();
    loadDocTemplates();
  }
}

// 处理二级领域选择变化
function handleLevel2Change() {
  selectedDomainIds.level3 = '';
  level3Domains.value = [];
  docTemplates.value = [];
  
  if (selectedDomainIds.level2) {
    loadLevel3Domains();
    loadDocTemplates();
  }
}

// 处理三级领域选择变化
function handleLevel3Change() {
  docTemplates.value = [];
  
  if (selectedDomainIds.level3) {
    loadDocTemplates();
  }
}

// 加载技术领域数据
async function loadDomains() {
  try {
    const response = await fetch(`${API_BASE_URL}/tech-domains/children`, {
      headers: getRequestHeaders() as HeadersInit
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }
    
    const result = await response.json();
    console.log('技术领域数据:', result);
    
    if (result.code === 200 && result.data) {
      // 确保返回的是数组
      if (Array.isArray(result.data)) {
        domains.value = result.data.map((domain: any) => ({
          id: domain.id,
          domain_name: domain.domainName || domain.domain_name,
          parent_id: domain.parentId || domain.parent_id,
          level: domain.level
        }));
      } else if (result.data.records && Array.isArray(result.data.records)) {
        // 如果数据在records字段中
        domains.value = result.data.records.map((domain: any) => ({
          id: domain.id,
          domain_name: domain.domainName || domain.domain_name,
          parent_id: domain.parentId || domain.parent_id,
          level: domain.level
        }));
      } else {
        console.error('意外的数据格式:', result.data);
        domains.value = [];
      }
    } else {
      throw new Error(result.message || '加载技术领域失败');
    }
    
    console.log('处理后的领域数据:', domains.value);
  } catch (error) {
    handleApiError(error, '加载技术领域失败');
    domains.value = [];
  }
}

// 加载二级领域数据
async function loadLevel2Domains() {
  if (!selectedDomainIds.level1) return;
  
  try {
    const response = await fetch(
      `${API_BASE_URL}/tech-domains/children?parentId=${selectedDomainIds.level1}`,
      { headers: getRequestHeaders() as HeadersInit }
    );
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }
    
    const result = await response.json();
    console.log('二级技术领域数据:', result);
    
    if (result.code === 200 && result.data) {
      // 确保返回的是数组
      if (Array.isArray(result.data)) {
        level2Domains.value = result.data.map((domain: any) => ({
          id: domain.id,
          domain_name: domain.domainName || domain.domain_name,
          parent_id: domain.parentId || domain.parent_id,
          level: domain.level
        }));
      } else if (result.data.records && Array.isArray(result.data.records)) {
        // 如果数据在records字段中
        level2Domains.value = result.data.records.map((domain: any) => ({
          id: domain.id,
          domain_name: domain.domainName || domain.domain_name,
          parent_id: domain.parentId || domain.parent_id,
          level: domain.level
        }));
      } else {
        console.error('意外的二级领域数据格式:', result.data);
        level2Domains.value = [];
      }
      
      console.log('处理后的二级领域数据:', level2Domains.value);
    } else {
      throw new Error(result.message || '加载二级领域失败');
    }
  } catch (error) {
    handleApiError(error, '加载二级领域失败');
    level2Domains.value = [];
  }
}

// 加载三级领域数据
async function loadLevel3Domains() {
  if (!selectedDomainIds.level2) return;
  
  try {
    const response = await fetch(
      `${API_BASE_URL}/tech-domains/children?parentId=${selectedDomainIds.level2}`,
      { headers: getRequestHeaders() as HeadersInit }
    );
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }
    
    const result = await response.json();
    console.log('三级技术领域数据:', result);
    
    if (result.code === 200 && result.data) {
      // 确保返回的是数组
      if (Array.isArray(result.data)) {
        level3Domains.value = result.data.map((domain: any) => ({
          id: domain.id,
          domain_name: domain.domainName || domain.domain_name,
          parent_id: domain.parentId || domain.parent_id,
          level: domain.level
        }));
      } else if (result.data.records && Array.isArray(result.data.records)) {
        // 如果数据在records字段中
        level3Domains.value = result.data.records.map((domain: any) => ({
          id: domain.id,
          domain_name: domain.domainName || domain.domain_name,
          parent_id: domain.parentId || domain.parent_id,
          level: domain.level
        }));
      } else {
        console.error('意外的三级领域数据格式:', result.data);
        level3Domains.value = [];
      }
      
      console.log('处理后的三级领域数据:', level3Domains.value);
    } else {
      throw new Error(result.message || '加载三级领域失败');
    }
  } catch (error) {
    handleApiError(error, '加载三级领域失败');
    level3Domains.value = [];
  }
}

// 加载文档模板
async function loadDocTemplates() {
  const currentDomain = selectedDomainIds.level3 || selectedDomainIds.level2 || selectedDomainIds.level1;
  if (!currentDomain) {
    docTemplates.value = [];
    selectedTemplate.value = '';
    return;
  }
  
  try {
    const response = await fetch(
      `${API_BASE_URL}/doc-templates/by-domain?domainId=${currentDomain}`,
      { headers: getRequestHeaders() as HeadersInit }
    );
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }
    
    const result = await response.json();
    console.log('文档模板数据:', result);
    
    if (result.code === 200 && result.data) {
      docTemplates.value = result.data.map((template: any) => ({
        id: template.id,
        template_name: template.templateName
      }));
      console.log('处理后的模板数据:', docTemplates.value);
    } else {
      throw new Error(result.message || '加载文档模板失败');
    }
  } catch (error) {
    handleApiError(error, '加载文档模板失败');
    docTemplates.value = [];
  }
}

// 生成文档内容
async function generateContent() {
  console.log('generateContent 函数被调用');
  const currentDomain = selectedDomainIds.level3 || selectedDomainIds.level2 || selectedDomainIds.level1;
  
  // 参数验证
  if (!currentDomain) {
    ElMessage.warning('请选择技术领域');
    return;
  }
  if (!selectedTemplate.value) {
    ElMessage.warning('请选择文档模板');
    return;
  }
  if (!userInput.value.trim()) {
    ElMessage.warning('请输入技术描述');
    return;
  }

  loading.value = true;
  sections.value = [];

  try {
    const params = {
      techDomainId: currentDomain,
      docTemplateId: selectedTemplate.value,
      techDescription: userInput.value.trim(),
      useAiGenerate: useAI.value
    };
    
    console.log('开始生成文档，参数：', params);

    // 使用fetch API替代axios
    const response = await fetch(`${API_BASE_URL}/doc-generate`, {
      method: 'POST',
      headers: getRequestHeaders() as HeadersInit,
      body: JSON.stringify(params)
    });

    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }

    const data = await response.json();
    console.log('生成文档响应：', data);

    if (data.code === 200) {
      // 直接使用返回的sections数组
      sections.value = data.data.sections;
      // 初始化编辑状态
      sections.value.forEach(section => {
        isEditing[section.sectionName] = false;
        editingSections[section.sectionName] = section.content;
      });
      
      // 滚动到结果区域
      setTimeout(() => {
        const resultSection = document.querySelector('.bg-white.p-6.rounded-lg.shadow-md');
        if (resultSection) {
          resultSection.scrollIntoView({ behavior: 'smooth' });
        }
      }, 100);
    } else {
      throw new Error(data.message || '生成文档失败');
    }
  } catch (error) {
    console.error('生成文档错误：', error);
    handleApiError(error, '生成文档失败');
    sections.value = [];
  } finally {
    loading.value = false;
  }
}

// 切换编辑状态
function toggleEdit(sectionName: string) {
  if (isEditing[sectionName]) {
    // 保存修改
    const section = sections.value.find(s => s.sectionName === sectionName);
    if (section) {
      section.content = editingSections[sectionName];
    }
    isEditing[sectionName] = false;
  } else {
    // 开始编辑
    const section = sections.value.find(s => s.sectionName === sectionName);
    if (section) {
      editingSections[sectionName] = section.content;
    }
    isEditing[sectionName] = true;
  }
}

// 重新生成某一部分
async function regenerateSection(sectionName: string) {
  if (isEditing[sectionName]) return;
  
  const currentDomain = selectedDomainIds.level3 || selectedDomainIds.level2 || selectedDomainIds.level1;
  if (!currentDomain || !selectedTemplate.value) {
    ElMessage.warning('缺少必要的参数');
    return;
  }
  
  loading.value = true;
  
  try {
    const params = {
      techDomainId: currentDomain,
      docTemplateId: selectedTemplate.value,
      techDescription: userInput.value.trim(),
      useAiGenerate: useAI.value,
      regenerateSection: sectionName
    };
    
    const response = await fetch(`${API_BASE_URL}/doc-generate`, {
      method: 'POST',
      headers: getRequestHeaders() as HeadersInit,
      body: JSON.stringify(params)
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }
    
    const data = await response.json();
    console.log('重新生成部分响应：', data);
    
    if (data.code === 200) {
      // 更新指定部分的内容
      const sectionIndex = sections.value.findIndex(s => s.sectionName === sectionName);
      if (sectionIndex !== -1) {
        const newSection = data.data.sections.find((s: DocumentSection) => s.sectionName === sectionName);
        if (newSection) {
          sections.value[sectionIndex] = newSection;
        }
      }
      ElMessage.success(`${sectionName}重新生成成功`);
    } else {
      throw new Error(data.message || '重新生成失败');
    }
  } catch (error) {
    console.error('重新生成部分错误：', error);
    handleApiError(error, '重新生成失败');
  } finally {
    loading.value = false;
  }
}

// 导出到Word
async function exportToWord() {
  if (!sections.value || sections.value.length === 0) {
    ElMessage.warning('没有可导出的内容');
    return;
  }

  exportLoading.value = true;

  try {
    const exportData = {
      title: '技术交底书',
      sections: sections.value.map((section, index) => ({
        sectionName: section.sectionName,
        content: section.content,
        sortOrder: index + 1
      }))
    };

    const response = await fetch(`${API_BASE_URL}/doc-generate/export-word`, {
      method: 'POST',
      headers: getRequestHeaders() as HeadersInit,
      body: JSON.stringify(exportData)
    });

    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }

    // 获取文件名（从响应头中获取）
    const contentDisposition = response.headers.get('Content-Disposition');
    let fileName = '技术交底书.docx';
    if (contentDisposition) {
      const match = contentDisposition.match(/filename="(.+)"/);
      if (match) {
        fileName = match[1];
      }
    }

    const blob = await response.blob();
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);

    ElMessage.success('导出Word文档成功');
  } catch (error) {
    console.error('导出Word文档失败：', error);
    handleApiError(error, '导出Word文档失败');
  } finally {
    exportLoading.value = false;
  }
}

// 初始化
onMounted(() => {
  loadDomains();
});
</script>

<style scoped>
.loading-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid rgba(59, 130, 246, 0.3);
  border-top-color: rgb(59, 130, 246);
  animation: spinner 0.6s linear infinite;
}

@keyframes spinner {
  to {transform: rotate(360deg);}
}

.shake-animation {
  animation: shake 0.5s cubic-bezier(.36,.07,.19,.97) both;
}

@keyframes shake {
  10%, 90% {transform: translate3d(-1px, 0, 0);}
  20%, 80% {transform: translate3d(2px, 0, 0);}
  30%, 50%, 70% {transform: translate3d(-4px, 0, 0);}
  40%, 60% {transform: translate3d(4px, 0, 0);}
}

.material-easing {
  transition: all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style> 