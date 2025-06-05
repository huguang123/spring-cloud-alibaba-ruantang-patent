<template>
  <div class="template-management min-h-screen">
    <div class="container mx-auto p-4 max-w-7xl">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-800">提示词模板管理</h1>
        <el-button 
          type="primary"
          @click="openCreateModal" 
        >
          <i class="fas fa-plus mr-2"></i>
          <span>新建模板</span>
        </el-button>
      </div>
      
      <!-- 筛选区 -->
      <el-card class="mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">模板类型</label>
            <el-select 
              v-model="filters.sectionType" 
              class="w-full"
              placeholder="全部类型"
              clearable
            >
              <el-option 
                v-for="type in sectionTypes" 
                :key="type.value" 
                :label="type.label" 
                :value="type.value"
              />
            </el-select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">版本</label>
            <el-select 
              v-model="filters.version" 
              class="w-full"
              placeholder="全部版本"
              clearable
            >
              <el-option 
                v-for="version in versions" 
                :key="version" 
                :label="version" 
                :value="version"
              />
            </el-select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">状态</label>
            <el-select 
              v-model="filters.enabled" 
              class="w-full"
              placeholder="全部状态"
              clearable
            >
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
            </el-select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">关键词</label>
            <div class="relative">
              <el-input 
                v-model="filters.promName" 
                placeholder="搜索模板名称..."
                clearable
              >
                <template #prefix>
                  <i class="fas fa-search text-gray-400"></i>
                </template>
              </el-input>
            </div>
          </div>
        </div>
        
        <div class="flex justify-end mt-4">
          <el-button 
            @click="resetFilters" 
            class="mr-2"
          >
            <i class="fas fa-undo-alt mr-1"></i>
            重置
          </el-button>
          <el-button 
            type="primary"
            @click="applyFilters" 
          >
            <i class="fas fa-filter mr-1"></i>
            筛选
          </el-button>
        </div>
      </el-card>
      
      <!-- 模板列表 -->
      <el-card class="shadow-sm">
        <template #header>
          <h2 class="text-lg font-medium text-gray-900">模板列表</h2>
        </template>
        
        <div v-if="loading" class="py-32 flex justify-center items-center">
          <div class="spinner mr-3"></div>
          <span class="text-gray-600">加载中...</span>
        </div>
        
        <div v-else-if="filteredTemplates.length === 0" class="py-16 text-center">
          <div class="text-5xl text-gray-300 mb-4">
            <i class="fas fa-file-alt"></i>
          </div>
          <h3 class="text-lg font-medium text-gray-900">没有找到模板</h3>
          <p class="mt-1 text-sm text-gray-500">
            {{ hasFilters ? '没有符合筛选条件的模板，请调整筛选条件' : '请点击"新建模板"按钮创建新的模板' }}
          </p>
        </div>
        
        <div v-else>
          <el-table :data="filteredTemplates" border style="width: 100%">
            <el-table-column label="模板类型" width="130">
              <template #default="{ row }">
                <span class="badge" :class="getTemplateBadgeClass(row.sectionType)">
                  {{ row.sectionType }}
                </span>
              </template>
            </el-table-column>
            
            <el-table-column prop="promName" label="模板名称" width="180">
              <template #default="{ row }">
                {{ row.promName || '未命名模板' }}
              </template>
            </el-table-column>
            
            <el-table-column prop="content" label="内容预览">
              <template #default="{ row }">
                <div class="text-sm text-gray-900 max-w-md truncate">
                  {{ row.content }}
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="weight" label="优先级" width="100" />
            
            <el-table-column prop="version" label="版本" width="100" />
            
            <el-table-column label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag 
                  :type="row.enabled ? 'success' : 'danger'"
                  size="small"
                >
                  {{ row.enabled ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <el-button 
                  type="primary" 
                  link
                  @click="editTemplate(row)"
                >
                  <i class="fas fa-edit mr-1"></i> 编辑
                </el-button>
                <el-button 
                  :type="row.enabled ? 'danger' : 'success'" 
                  link
                  @click="toggleTemplateStatus(row)"
                >
                  <i :class="row.enabled ? 'fas fa-ban' : 'fas fa-check-circle'"></i>
                  {{ row.enabled ? '禁用' : '启用' }}
                </el-button>
                <el-button 
                  type="info" 
                  link
                  @click="viewTemplateDetail(row)"
                >
                  <i class="fas fa-eye mr-1"></i> 详情
                </el-button>
                <el-button 
                  type="danger" 
                  link
                  @click="deleteTemplate(row)"
                >
                  <i class="fas fa-trash mr-1"></i> 删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="flex justify-between items-center mt-4">
            <div class="text-sm text-gray-700">
              显示 <span class="font-medium">{{ filteredTemplates.length }}</span> 个模板中的 <span class="font-medium">{{ totalCount }}</span> 个
            </div>
            
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[5, 10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              :total="totalCount"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </el-card>
      
      <!-- 编辑模态框 -->
      <el-dialog 
        v-model="showModal" 
        :title="editingTemplate.id ? '编辑模板' : '创建模板'"
        width="60%"
        destroy-on-close
      >
        <div class="overflow-y-auto custom-scrollbar">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">模板类型 <span class="text-red-600">*</span></label>
              <el-select 
                v-model="editingTemplate.sectionType" 
                class="w-full"
                placeholder="请选择模板类型"
              >
                <el-option value="背景技术" label="背景技术" />
                <el-option value="技术问题" label="技术问题" />
                <el-option value="技术效果" label="技术效果" />
                <el-option value="技术方案" label="技术方案" />
                <el-option value="实施例" label="实施例" />
                <el-option value="附图说明" label="附图说明" />
              </el-select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">模板名称 <span class="text-red-600">*</span></label>
              <el-input 
                v-model="editingTemplate.promName" 
                placeholder="请输入模板名称，如'标准技术问题生成器'"
              />
            </div>
            
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">版本</label>
                <el-input 
                  v-model="editingTemplate.version" 
                  placeholder="如：1.0"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">优先级</label>
                <el-input-number 
                  v-model="editingTemplate.weight" 
                  :min="1" 
                  :max="10" 
                  class="w-full"
                />
              </div>
            </div>
          </div>
          
          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-1">模板内容 <span class="text-red-600">*</span></label>
            <el-input 
              v-model="editingTemplate.content" 
              type="textarea" 
              :rows="8" 
              placeholder="请输入模板内容，可以使用${variable_name}格式的变量占位符"
            />
          </div>
          
          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-1">模板变量</label>
            <div class="bg-gray-50 p-3 rounded-md border border-gray-200">
              <div v-if="extractedVariables.length === 0" class="text-gray-500 text-sm italic">
                未检测到变量。变量格式示例：${user_input}、${spark_approach}
              </div>
              <div v-else class="flex flex-wrap gap-2">
                <el-tag 
                  v-for="variable in extractedVariables" 
                  :key="variable.paramKey" 
                  type="info"
                  class="m-1"
                >
                  ${{ "{" + variable.paramKey + "}" }}
                </el-tag>
              </div>
            </div>
          </div>
          
          <div class="mb-6">
            <div class="flex justify-between items-center mb-2">
              <label class="block text-sm font-medium text-gray-700">模板变量配置</label>
              <el-button 
                type="primary" 
                size="small"
                plain
                @click="addTemplateVariable" 
              >
                <i class="fas fa-plus mr-1"></i>
                添加变量配置
              </el-button>
            </div>
            
            <div class="space-y-3">
              <div v-if="editingTemplate.parameters && editingTemplate.parameters.length === 0" class="text-gray-500 text-sm italic">
                未添加变量配置。请为模板变量添加配置信息。
              </div>
              
              <el-card
                v-for="(variable, index) in editingTemplate.parameters" 
                :key="index"
                class="mb-3"
              >
                <template #header>
                  <div class="flex justify-between items-start">
                    <div class="flex items-center">
                      <el-tag type="success" class="mr-2">
                        ${{ "{" + variable.paramKey + "}" }}
                      </el-tag>
                      <span class="text-sm text-gray-600">{{ variable.description }}</span>
                    </div>
                    <el-button 
                      type="danger" 
                      icon="Delete"
                      circle
                      plain
                      size="small"
                      @click="removeTemplateVariable(index)"
                    />
                  </div>
                </template>
                
                <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                  <div>
                    <label class="block text-xs font-medium text-gray-500 mb-1">变量名称</label>
                    <el-input 
                      v-model="variable.paramKey" 
                      size="small"
                      placeholder="变量名称"
                    />
                  </div>
                  <div>
                    <label class="block text-xs font-medium text-gray-500 mb-1">变量描述</label>
                    <el-input 
                      v-model="variable.description" 
                      size="small"
                      placeholder="变量描述"
                    />
                  </div>
                  <div>
                    <label class="block text-xs font-medium text-gray-500 mb-1">默认值</label>
                    <el-input 
                      v-model="variable.defaultValue" 
                      size="small"
                      placeholder="默认值"
                    />
                  </div>
                  <div class="col-span-3">
                    <label class="block text-xs font-medium text-gray-500 mb-1">输入提示</label>
                    <el-input 
                      v-model="variable.enterReminder" 
                      size="small"
                      placeholder="用户输入提示"
                    />
                  </div>
                  <div class="col-span-3">
                    <label class="block text-xs font-medium text-gray-500 mb-1">提示词类型</label>
                    <el-radio-group v-model="variable.promRole">
                      <el-radio :label="0">系统提示词</el-radio>
                      <el-radio :label="1">用户提示词</el-radio>
                    </el-radio-group>
                  </div>
                </div>
              </el-card>
            </div>
            
            <div v-if="extractedVariables.length > 0 && (!editingTemplate.parameters || editingTemplate.parameters.length === 0)" class="mt-2">
              <el-button 
                type="primary" 
                link
                @click="autoGenerateVariables" 
              >
                <i class="fas fa-magic mr-1"></i> 从模板内容自动提取变量配置
              </el-button>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">状态</label>
            <el-radio-group v-model="editingTemplate.enabled">
              <el-radio :label="true">启用</el-radio>
              <el-radio :label="false">禁用</el-radio>
            </el-radio-group>
          </div>
        </div>
        <template #footer>
          <div class="flex justify-end">
            <el-button @click="closeModal">取消</el-button>
            <el-button 
              type="primary" 
              @click="saveTemplate"
              :disabled="!isFormValid"
            >
              保存
            </el-button>
          </div>
        </template>
      </el-dialog>
      
      <!-- 详情模态框 -->
      <el-dialog 
        v-model="showDetailModal" 
        title="模板详情"
        width="60%"
      >
        <div v-if="selectedTemplate" class="overflow-y-auto custom-scrollbar">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
              <div class="text-sm font-medium text-gray-500 mb-1">模板类型</div>
              <div>
                <span class="badge" :class="getTemplateBadgeClass(selectedTemplate.sectionType)">
                  {{ selectedTemplate.sectionType }}
                </span>
              </div>
            </div>
            
            <div class="grid grid-cols-2 gap-4">
              <div>
                <div class="text-sm font-medium text-gray-500 mb-1">版本</div>
                <div class="text-gray-900">{{ selectedTemplate.version }}</div>
              </div>
              <div>
                <div class="text-sm font-medium text-gray-500 mb-1">优先级</div>
                <div class="text-gray-900">{{ selectedTemplate.weight }}</div>
              </div>
            </div>
          </div>
          
          <div class="mb-6">
            <div class="text-sm font-medium text-gray-500 mb-1">模板内容</div>
            <el-card class="bg-gray-50">
              <pre class="text-sm text-gray-900 whitespace-pre-wrap">{{ selectedTemplate.content }}</pre>
            </el-card>
          </div>
          
          <div>
            <div class="text-sm font-medium text-gray-500 mb-1">模板变量</div>
            <el-card class="bg-gray-50">
              <div v-if="selectedTemplateVariables.length === 0" class="text-gray-500 text-sm italic">
                未检测到变量
              </div>
              <div v-else class="flex flex-wrap gap-2">
                <el-tag 
                  v-for="variable in selectedTemplateVariables" 
                  :key="variable.paramKey" 
                  type="info"
                  class="m-1"
                >
                  ${{ "{" + variable.paramKey + "}" }}
                </el-tag>
              </div>
            </el-card>
          </div>
          
          <div v-if="selectedTemplate.parameters && selectedTemplate.parameters.length > 0" class="mt-4">
            <div class="text-sm font-medium text-gray-500 mb-1">变量配置详情</div>
            <el-card class="bg-gray-50">
              <div v-for="variable in selectedTemplate.parameters" :key="variable.paramKey" class="mb-3">
                <el-card>
                  <div class="flex items-center mb-2">
                    <el-tag type="success" class="mr-2">
                      ${{ "{" + variable.paramKey + "}" }}
                    </el-tag>
                    <span class="text-sm text-gray-700 font-medium">{{ variable.description }}</span>
                  </div>
                  
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm text-gray-600">
                    <div>
                      <span class="text-xs text-gray-500">默认值:</span>
                      <span class="ml-1">{{ variable.defaultValue || '(无)' }}</span>
                    </div>
                    <div>
                      <span class="text-xs text-gray-500">输入提示:</span>
                      <span class="ml-1">{{ variable.enterReminder || '(无)' }}</span>
                    </div>
                    <div>
                      <span class="text-xs text-gray-500">提示词类型:</span>
                      <el-tag 
                        size="small" 
                        :type="variable.promRole === 0 ? 'primary' : 'warning'"
                      >
                        {{ variable.promRole === 0 ? '系统提示词' : '用户提示词' }}
                      </el-tag>
                    </div>
                  </div>
                </el-card>
              </div>
            </el-card>
          </div>
        </div>
        <template #footer>
          <div class="flex justify-end">
            <el-button @click="showDetailModal = false">关闭</el-button>
          </div>
        </template>
      </el-dialog>
      
      <!-- 错误提示 -->
      <el-alert
        v-if="pageLoadError"
        :title="pageLoadError"
        type="error"
        show-icon
        class="mb-4"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, ElTable, ElTableColumn, ElButton, ElInput, ElForm, ElFormItem, ElDialog, ElCard, ElPagination, ElTag, ElPopconfirm, ElSelect, ElOption, ElSwitch, ElTooltip, ElIcon, ElRow, ElCol, ElDivider, ElEmpty, ElLoading } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh, View, Document, Setting } from '@element-plus/icons-vue'
import { handleApiError, handleApiResponse } from '@/utils/errorHandler'

// 类型定义
interface TemplateVariable {
  paramKey: string
  description: string
  defaultValue: string
  enterReminder: string
  promRole: number
}

interface Template {
  id: string | null
  sectionType: string
  promName: string
  content: string
  weight: number
  version: string
  enabled: boolean
  parameters: TemplateVariable[]
}

interface TemplateFilter {
  sectionType: string
  version: string
  enabled: boolean | string
  promName: string
}

// 数据状态
const loading = ref(false)
const templates = ref<Template[]>([])
const showModal = ref(false)
const showDetailModal = ref(false)
const selectedTemplate = ref<Template | null>(null)
const apiBaseUrl = '/prom/api'
const totalCount = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const pageLoadError = ref<string | null>(null)

// 编辑中的模板
const editingTemplate = reactive<Template>({
  id: null,
  sectionType: '',
  promName: '',
  content: '',
  weight: 5,
  version: '1.0',
  enabled: true,
  parameters: []
})

// 定义模板类型选项
const sectionTypes = [
  { value: '技术问题', label: '技术问题' },
  { value: '技术效果', label: '技术效果' },
  { value: '技术方案', label: '技术方案' },
  { value: '背景技术', label: '背景技术' },
  { value: '实施例', label: '实施例' },
  { value: '附图说明', label: '附图说明' }
]

// 定义版本选项
const versions = ['1.0', '1.1', '2.0']

// 筛选条件
const filters = reactive<TemplateFilter>({
  sectionType: '',
  version: '',
  enabled: '',
  promName: ''
})

// 获取HTTP请求头
function getRequestHeaders(): Record<string, string> {
  // 获取token
  const token = localStorage.getItem('auth_token') || 
    (window as any).auth?.getToken?.()
  
  const headers: Record<string, string> = {
    'Content-Type': 'application/json'
  }
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  
  return headers
}

// 计算属性
// 是否有筛选条件
const hasFilters = computed(() => {
  return !!filters.sectionType || 
         !!filters.version || 
         filters.enabled !== '' || 
         !!filters.promName
})

// 提取的变量
const extractedVariables = computed(() => {
  return extractVariables(editingTemplate.content)
})

// 选中模板的变量
const selectedTemplateVariables = computed(() => {
  return selectedTemplate.value ? extractVariables(selectedTemplate.value.content) : []
})

// 表单验证
const isFormValid = computed(() => {
  return Boolean(editingTemplate.sectionType && editingTemplate.content)
})

// 过滤后的模板
const filteredTemplates = computed(() => {
  let result = templates.value || []
  
  if (filters.sectionType) {
    result = result.filter(t => t?.sectionType === filters.sectionType)
  }
  
  if (filters.version) {
    result = result.filter(t => t?.version === filters.version)
  }
  
  if (filters.enabled !== '') {
    const status = filters.enabled
    result = result.filter(t => t?.enabled === status)
  }
  
  if (filters.promName) {
    const keyword = filters.promName.toLowerCase()
    result = result.filter(t => 
      t?.promName?.toLowerCase().includes(keyword)
    )
  }
  
  return result
})

// 方法
// 获取模板徽章样式
function getTemplateBadgeClass(type?: string) {
  if (!type) return ''
  
  const classMap: {[key: string]: string} = {
    '技术问题': 'badge-blue',
    '技术效果': 'badge-green',
    '技术方案': 'badge-purple',
    '背景技术': 'badge-yellow',
    '实施例': 'badge-red'
  }
  
  return classMap[type] || ''
}

// 打开创建模态框
function openCreateModal() {
  Object.assign(editingTemplate, {
    id: null,
    sectionType: '',
    promName: '',
    content: '',
    weight: 5,
    version: '1.0',
    enabled: true,
    parameters: []
  })
  
  showModal.value = true
}

// 获取模板详情
async function fetchTemplateDetail(id: string) {
  try {
    const response = await fetch(
      `${apiBaseUrl}/prom-templates/${id}`,
      { headers: getRequestHeaders() }
    )
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    if (result.code !== 200) {
      throw new Error(result.message || '获取模板详情失败')
    }
    
    return result.data
  } catch (error) {
    handleApiError(error, '获取模板详情失败')
    return null
  }
}

// 编辑模板
async function editTemplate(template: Template) {
  loading.value = true
  try {
    const detailTemplate = await fetchTemplateDetail(template.id as string)
    if (!detailTemplate) {
      throw new Error('获取模板详情失败')
    }
    console.log('获取到的模板详情:', detailTemplate)
    
    // 转换API响应到编辑模板格式
    Object.assign(editingTemplate, {
      id: detailTemplate.id,
      sectionType: detailTemplate.sectionType,
      promName: detailTemplate.promName,
      content: detailTemplate.content,
      weight: detailTemplate.weight || 5,
      version: detailTemplate.version || '1.0',
      enabled: detailTemplate.enabled,
      parameters: (detailTemplate.parameters || []).map((p: any) => ({
        paramKey: p.name || p.paramKey,
        type: p.type || 'string',
        defaultValue: p.defaultValue || '',
        description: p.description || '',
        enterReminder: p.enterReminder || '',
        promRole: p.promRole !== undefined ? Number(p.promRole) : 1
      }))
    })
    console.log('处理后的编辑模板数据:', editingTemplate)
    
    showModal.value = true
  } catch (error) {
    handleApiError(error, '编辑模板失败')
  } finally {
    loading.value = false
  }
}

// 关闭模态框
function closeModal() {
  showModal.value = false
}

// 查看模板详情
async function viewTemplateDetail(template: Template) {
  loading.value = true
  try {
    const detailTemplate = await fetchTemplateDetail(template.id as string)
    if (detailTemplate) {
      selectedTemplate.value = detailTemplate
      showDetailModal.value = true
    }
  } catch (error) {
    handleApiError(error, '查看模板详情失败')
  } finally {
    loading.value = false
  }
}

// 切换模板状态
async function toggleTemplateStatus(template: Template) {
  const newStatus = !template.enabled
  loading.value = true
  
  try {
    const result = await fetch(
      `${apiBaseUrl}/prom-templates/${template.id}/status?enabled=${newStatus}`,
      {
        method: 'PATCH',
        headers: getRequestHeaders()
      }
    )

    if (!result.ok) {
      throw new Error(`HTTP error: ${result.status}`)
    }

    const response = await result.json()
    if (response.code !== 200) {
      throw new Error(response.message || '状态更新失败')
    }

    const index = templates.value.findIndex(t => t.id === template.id)
    if (index !== -1) {
      templates.value[index].enabled = newStatus
    }
    ElMessage.success(response.message || '状态更新成功')
  } catch (error) {
    handleApiError(error, '更新状态失败')
  } finally {
    loading.value = false
  }
}

// 删除模板
async function deleteTemplate(template: Template) {
  ElMessageBox.confirm(
    '确定要删除该模板吗？此操作不可恢复。',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    loading.value = true
    try {
      const response = await fetch(
        `${apiBaseUrl}/prom-templates/${template.id}`,
        {
          method: 'DELETE',
          headers: getRequestHeaders()
        }
      )
  
      if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`)
      }
  
      const result = await response.json()
      if (result.code !== 200) {
        throw new Error(result.message || '删除失败')
      }
  
      ElMessage.success('删除成功')
      refreshTemplates()  // 刷新列表
    } catch (error) {
      handleApiError(error, '删除模板失败')
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 保存模板
async function saveTemplate() {
  if (!isFormValid.value) {
    ElMessage.warning('请填写必填字段')
    return
  }
  
  if (editingTemplate.parameters?.length > 0) {
    const emptyNameVar = editingTemplate.parameters.find(v => !v.paramKey?.trim())
    if (emptyNameVar) {
      ElMessage.warning('变量名称不能为空')
      return
    }
  }
  
  loading.value = true
  try {
    // 构建API所需的模板数据格式
    const templateData: any = {
      sectionType: editingTemplate.sectionType,
      promName: editingTemplate.promName || editingTemplate.sectionType + '模板',
      content: editingTemplate.content,
      weight: parseInt(String(editingTemplate.weight)) || 5,
      version: editingTemplate.version || '1.0',
      enabled: editingTemplate.enabled,
      parameters: editingTemplate.parameters.map(p => ({
        paramKey: p.paramKey,
        promRole: Number(p.promRole),
        defaultValue: p.defaultValue || '',
        enterReminder: p.enterReminder || '',
        description: p.description || ''
      }))
    }
    
    console.log('保存模板数据:', templateData)
    
    let saveResult
    
    if (editingTemplate.id) {
      // 更新现有模板
      templateData.id = editingTemplate.id
      saveResult = await fetch(
        `${apiBaseUrl}/prom-templates/${editingTemplate.id}`,
        {
          method: 'PUT',
          headers: getRequestHeaders(),
          body: JSON.stringify(templateData)
        }
      )
    } else {
      // 创建新模板
      saveResult = await fetch(
        `${apiBaseUrl}/prom-templates`,
        {
          method: 'POST',
          headers: getRequestHeaders(),
          body: JSON.stringify(templateData)
        }
      )
    }
    
    if (!saveResult.ok) {
      throw new Error(`HTTP error: ${saveResult.status}`)
    }
    
    const result = await saveResult.json()
    if (result.code !== 200) {
      throw new Error(result.message || '保存失败')
    }
    
    ElMessage.success('保存成功')
    showModal.value = false  // 关闭模态框
    refreshTemplates()  // 刷新列表
  } catch (error) {
    handleApiError(error, '保存模板失败')
  } finally {
    loading.value = false
  }
}

// 重置筛选条件
function resetFilters() {
  Object.assign(filters, {
    sectionType: '',
    version: '',
    enabled: '',
    promName: ''
  })
  refreshTemplates() // 重置后自动刷新列表
}

// 应用筛选条件
function applyFilters() {
  currentPage.value = 1 // 重置页码
  refreshTemplates()
}

// 提取变量
function extractVariables(content?: string) {
  if (!content) return []
  
  const regex = /\${([^}]+)}/g
  const matches: TemplateVariable[] = []
  let match
  
  while ((match = regex.exec(content)) !== null) {
    // 提取真实的变量名
    const varName = match[1].trim()
    // 检查是否已经存在相同名称的变量
    if (varName && !matches.some(m => m.paramKey === varName)) {
      matches.push({
        paramKey: varName,
        description: '',
        defaultValue: '',
        enterReminder: `请输入${varName}`,
        promRole: 1  // 默认为用户提示词
      })
    }
  }
  
  return matches
}

// 自动生成变量配置
function autoGenerateVariables() {
  const extractedVars = extractVariables(editingTemplate.content)
  
  // 如果没有提取到变量，提示用户
  if (extractedVars.length === 0) {
    ElMessage.warning('未在模板内容中检测到变量。变量格式示例：${user_input}')
    return
  }
  
  // 保留现有变量的描述等信息
  const updatedVariables: TemplateVariable[] = []
  
  for (const newVar of extractedVars) {
    const existingVar = editingTemplate.parameters.find(v => v.paramKey === newVar.paramKey)
    if (existingVar) {
      updatedVariables.push(existingVar)
    } else {
      updatedVariables.push({
        paramKey: newVar.paramKey,
        description: `${newVar.paramKey} 参数`,  // 默认添加简单描述
        defaultValue: '',
        enterReminder: `请输入 ${newVar.paramKey}`, // 默认添加简单提示
        promRole: 1  // 默认为用户提示词
      })
    }
  }
  
  editingTemplate.parameters = updatedVariables
}

// 添加模板变量
function addTemplateVariable() {
  if (!editingTemplate.parameters) {
    editingTemplate.parameters = []
  }
  
  editingTemplate.parameters.push({
    paramKey: '',
    description: '',
    defaultValue: '',
    enterReminder: '',
    promRole: 1  // 默认为用户提示词
  })
}

// 删除模板变量
function removeTemplateVariable(index: number) {
  editingTemplate.parameters.splice(index, 1)
}

// 分页大小改变
function handleSizeChange(val: number) {
  pageSize.value = val
  refreshTemplates()
}

// 当前页改变
function handleCurrentChange(val: number) {
  currentPage.value = val
  refreshTemplates()
}

// 加载提示词模板数据
async function refreshTemplates() {
  loading.value = true
  pageLoadError.value = null
  
  try {
    const queryParams = new URLSearchParams({
      pageNum: currentPage.value.toString(),
      pageSize: pageSize.value.toString(),
      ...(filters.sectionType && { sectionType: filters.sectionType }),
      ...(filters.promName && { promName: filters.promName }),
      ...(filters.enabled !== '' && { enabled: String(filters.enabled) }),
      ...(filters.version && { version: filters.version })
    })

    const response = await fetch(
      `${apiBaseUrl}/prom-templates?${queryParams.toString()}`,
      { 
        method: 'GET',
        headers: getRequestHeaders()
      }
    )

    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }

    const result = await response.json()
    console.log('API返回数据:', result)

    if (result.code !== 200) {
      throw new Error(result.message || '操作失败')
    }

    // 检查并处理返回的数据结构
    if (result?.data) {
      // 使用records作为模板列表数据源
      if (Array.isArray(result.data.records)) {
        templates.value = result.data.records
        totalCount.value = parseInt(String(result.data.total)) || result.data.records.length
        
        // 如果数据为空且没有筛选条件，记录日志
        if (templates.value.length === 0 && !hasFilters.value) {
          console.log('没有找到模板数据')
        }
      } else {
        console.warn('API返回的records不是数组:', result.data)
        templates.value = []
        totalCount.value = 0
      }
    } else {
      templates.value = []
      totalCount.value = 0
      console.warn('API返回的数据为空')
    }
  } catch (error) {
    templates.value = []
    totalCount.value = 0
    handleApiError(error, '加载模板列表失败')
  } finally {
    loading.value = false
  }
}

// 初始化
onMounted(() => {
  try {
    refreshTemplates()
  } catch (error) {
    console.error('页面初始化失败:', error)
    pageLoadError.value = '页面初始化失败'
  }
})
</script>

<style scoped>
.spinner {
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top: 3px solid #2F54EB;
  width: 20px;
  height: 20px;
  animation: spin 1s linear infinite;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.badge {
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 0.375rem;
}
.badge-blue {
  background-color: #EBF5FF;
  color: #1D4ED8;
}
.badge-green {
  background-color: #ECFDF5;
  color: #047857;
}
.badge-yellow {
  background-color: #FFFBEB;
  color: #B45309;
}
.badge-red {
  background-color: #FEF2F2;
  color: #B91C1C;
}
.badge-purple {
  background-color: #F5F3FF;
  color: #6D28D9;
}
.custom-scrollbar {
  max-height: 500px;
  overflow-y: auto;
  resize: vertical;
}
.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: #f7fafc;
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #cbd5e0;
  border-radius: 4px;
  border: 2px solid #f7fafc;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #a0aec0;
}
</style>
