<template>
  <div class="tenant-container">
    <PageHeader title="租户管理中心" subtitle="管理平台中的所有租户及其权限模板" />

    <el-row :gutter="20">
      <!-- Left Panel: Filters and Tenant List -->
      <el-col :xs="24" :sm="10" :md="8" class="responsive-col">
        <el-card class="left-panel">
          <!-- Tenant Type Tabs -->
          <el-tabs v-model="activeTenantType" @tab-click="handleTenantTypeChange">
            <el-tab-pane label="全部租户" name="all"></el-tab-pane>
            <el-tab-pane label="平台超级租户" name="platform"></el-tab-pane>
            <el-tab-pane label="企业商户端" name="enterprise"></el-tab-pane>
            <el-tab-pane label="代理所客户端" name="agent"></el-tab-pane>
          </el-tabs>

          <!-- Filters -->
          <div class="filter-section">
            <el-select v-model="filters.dataIsolationMode" placeholder="数据隔离模式" clearable style="width: 48%; margin-bottom: 10px;">
              <el-option label="全部" value="" />
              <el-option label="行级隔离" value="1" />
              <el-option label="Schema隔离" value="2" />
              <el-option label="独立库" value="3" />
            </el-select>
            <el-input 
              v-model="filters.keyword" 
              placeholder="搜索租户名称或编码..." 
              clearable
              style="width: 48%; float: right; margin-bottom: 10px;"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="handleAddTenant" style="width: 100%;">
              <el-icon><Plus /></el-icon> 新增租户
            </el-button>
          </div>

          <!-- Tenant List -->
          <div class="tenant-list-container">
            <div class="tenant-list-header">
              <h3>租户列表</h3>
              <span>显示 {{ (currentPage - 1) * pageSize + 1 }} 到 {{ Math.min(currentPage * pageSize, total) }}，共 {{ total }} 条</span>
            </div>
            <el-table 
              :data="tenantList" 
              highlight-current-row
              @row-click="handleTenantSelect"
              style="width: 100%"
              v-loading="loading"
              height="calc(100vh - 450px)" 
            >
              <el-table-column prop="tenantName" label="租户名称">
                <template #default="{ row }">
                  <div class="tenant-list-item">
                    <div class="tenant-info">
                      <span class="tenant-status" :class="getStatusClass(row)"></span>
                      <span>{{ row.tenantName }}</span>
                      <el-tag size="small" :type="getTenantTypeTag(row.tenantType)" style="margin-left: 8px;">
                        {{ getTenantTypeLabel(row.tenantType).substring(0,2) }}
                      </el-tag>
                    </div>
                    <div class="tenant-meta">
                      <span>编码: {{ row.tenantCode }} | 创建: {{ formatDate(row.createTime) }} | 有效期: {{ formatDate(row.expireTime) }}</span> 
                    </div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container-left">
              <el-pagination
                small
                layout="prev, pager, next"
                :total="total"
                :page-size="pageSize"
                v-model:current-page="currentPage"
                @current-change="handleCurrentChange"
              />
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- Right Panel: Tenant Details -->
      <el-col :span="16">
        <el-card class="right-panel">
          <div v-if="selectedTenant">
            <div class="tenant-detail-header">
              <h2>
                {{ selectedTenant.tenantName }}
                <el-tag :type="getStatusTag(selectedTenant)" size="small" style="margin-left: 10px;">{{ getStatusLabel(selectedTenant) }}</el-tag>
              </h2>
              <div class="tenant-meta">
                <span>创建于 {{ formatDate(selectedTenant.createTime) }}，有效期至 {{ formatDate(selectedTenant.expireTime) }}</span>
              </div>
              <div class="header-actions">
                <el-button type="primary" @click="handleEditTenant(selectedTenant.id)">编辑</el-button>
                <el-popconfirm
                  title="确定要删除该租户吗?"
                  @confirm="handleDeleteTenant(selectedTenant.id)"
                >
                  <template #reference>
                    <el-button type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </div>

            <!-- Basic Info -->
            <el-descriptions title="基本信息" :column="2" border class="description-section">
              <el-descriptions-item label="租户编码">{{ selectedTenant.tenantCode }}</el-descriptions-item>
              <el-descriptions-item label="租户类型">{{ getTenantTypeLabel(selectedTenant.tenantType) }}</el-descriptions-item>
              <el-descriptions-item label="数据隔离模式">{{ getDataIsolationModeLabel(selectedTenant.dataIsolationMode) }}</el-descriptions-item>
              <el-descriptions-item label="管理员">{{ selectedTenant.adminUsername || (selectedTenant.adminUserId ? `User ID: ${selectedTenant.adminUserId}`: '-') }}</el-descriptions-item>
              <el-descriptions-item label="联系电话">{{ selectedTenant.tenantPhone || '-' }}</el-descriptions-item>
              <el-descriptions-item label="联系邮箱">{{ selectedTenant.tenantEmail || '-' }}</el-descriptions-item>
              <el-descriptions-item label="地址" :span="2">{{ selectedTenant.tenantAddress || '-' }}</el-descriptions-item>
            </el-descriptions>

            <!-- Permission Template Binding -->
            <div class="permission-template-section">
              <div class="section-header">
                <h3>企业模板绑定</h3>
                <el-button 
                  type="primary" 
                  @click="handleBindTemplate" 
                  :disabled="!selectedTenant || (selectedTenant.tenantType === 0 && hasBoundEnterpriseTemplate)"
                >
                  <el-icon><Plus /></el-icon> 绑定企业模板
                </el-button>
              </div>
              <el-table :data="boundTemplates" stripe style="width: 100%">
                <el-table-column prop="templateName" label="模板名称" />
                <el-table-column prop="templateCode" label="模板编码" />
                <el-table-column prop="moduleCount" label="模块数量" />
                <el-table-column label="绑定时间">
                   <template #default="{ row }">
                      {{ formatDate(row.bindTime || row.createTime) }}
                   </template>
                </el-table-column>
                <el-table-column label="生效时间">
                  <template #default="{ row }">
                    {{ formatDate(row.effectiveTime) }}
                  </template>
                </el-table-column>
                <el-table-column label="绑定模式">
                  <template #default="{ row }">
                    {{ getBindModeLabel(row.bindMode) }}
                  </template>
                </el-table-column>
                <el-table-column label="状态">
                  <template #default="{ row }">
                    <el-tag :type="row.status === '已启用' ? 'success' : 'info'">{{ row.status }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="{ row }">
                    <el-button text type="primary" size="small" @click="handleViewTemplate(row.templateId || row.id)">查看</el-button>
                    <el-popconfirm
                      title="确定要解绑该模板吗?"
                      @confirm="handleUnbindTemplate(row)"
                    >
                      <template #reference>
                        <el-button 
                          text 
                          type="danger" 
                          size="small" 
                          :disabled="row.status === '未启用'"
                        >
                          解绑
                        </el-button>
                      </template>
                    </el-popconfirm>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
          <el-empty v-else description="请在左侧选择一个租户查看详情" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑租户对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑租户' : '新增租户'" width="600px">
       <el-form 
        ref="tenantFormRef"
        :model="tenantForm"
        :rules="tenantRules"
        label-width="120px"
      >
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="tenantForm.tenantName" placeholder="请输入租户名称" />
        </el-form-item>
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="tenantForm.tenantCode" placeholder="请输入租户编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="租户类型" prop="tenantType">
          <el-select v-model="tenantForm.tenantType" placeholder="请选择租户类型" style="width: 100%">
            <el-option label="企业商户端" :value="0" />
            <el-option label="代理所客户端" :value="1" />
            <el-option label="平台超级租户" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据隔离模式" prop="dataIsolationMode">
          <el-select v-model="tenantForm.dataIsolationMode" placeholder="请选择数据隔离模式" style="width: 100%">
            <el-option label="行级隔离" :value="1" />
            <el-option label="Schema隔离" :value="2" />
            <el-option label="独立库" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="管理员账号" prop="adminUserId">
          <el-select
            v-model="tenantForm.adminUserId"
            filterable
            remote
            reserve-keyword
            placeholder="输入用户名或邮箱搜索"
            :remote-method="remoteSearchAdminUsers"
            :loading="adminUserSearchLoading"
            clearable
            style="width: 100%"
            @change="handleAdminUserSelect"
          >
            <el-option
              v-for="item in adminUserOptions"
              :key="item.id"
              :label="`${item.username} (${item.email})`"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务有效期至" prop="expireTime">
          <el-date-picker
            v-model="tenantForm.expireTime"
            type="date"
            placeholder="选择有效期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="地址" prop="tenantAddress">
          <el-input v-model="tenantForm.tenantAddress" placeholder="请输入地址" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitTenantForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 绑定企业模板对话框 -->
    <el-dialog v-model="bindDialogVisible" title="绑定企业模板" width="800px">
      <p>选择要绑定到 <strong>{{ selectedTenant?.tenantName }}</strong> 的企业模板：</p>
      <el-input 
        v-model="templateSearchKeyword"
        placeholder="搜索模板名称或编码..."
        clearable
        style="margin-bottom: 15px;"
        @keyup.enter="handleTemplateSearch"
        @clear="fetchAvailableTemplates"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
        <template #append>
          <el-button @click="handleTemplateSearch">搜索</el-button>
        </template>
      </el-input>
      <el-table
        :data="availableTemplatesFiltered"
        ref="availableTemplatesTableRef"
        @selection-change="handleTemplateSelectionChange"
        max-height="300px"
      >
        <el-table-column type="selection" width="55" :selectable="isTemplateSelectable" />
        <el-table-column prop="templateName" label="模板名称" />
        <el-table-column prop="templateCode" label="模板编码" />
        <el-table-column prop="moduleCount" label="模块数量" />
        <el-table-column label="创建时间">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
         <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === '已启用' ? 'success' : 'info'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <el-form label-width="100px" style="margin-top: 20px;" :disabled="selectedTemplates.length === 0">
        <el-form-item label="生效时间" prop="bindEffectiveTime">
            <el-date-picker
              v-model="bindEffectiveTime"
              type="datetime"
              placeholder="选择生效时间"
              style="width: 100%"
              :disabled-date="disabledBindEffectiveDate"
            />
        </el-form-item>
        <el-form-item label="绑定模式" prop="bindMode">
           <el-radio-group v-model="bindMode">
              <el-radio :value="1">继承</el-radio>
              <el-radio :value="2">快照</el-radio>
            </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="bindDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmBindTemplate" :disabled="selectedTemplates.length === 0">确认绑定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看模板详情对话框 -->
    <el-dialog v-model="viewTemplateDialogVisible" title="查看模板详情" width="600px">
      <div v-if="viewingTemplate">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="模板名称">{{ viewingTemplate.templateName }}</el-descriptions-item>
          <el-descriptions-item label="模板编码">{{ viewingTemplate.templateCode }}</el-descriptions-item>
          <el-descriptions-item label="模块数量">{{ viewingTemplate.moduleCount }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(viewingTemplate.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="viewingTemplate.status === '已启用' ? 'success' : 'info'">{{ viewingTemplate.status }}</el-tag>
          </el-descriptions-item>
          <template v-if="isBoundTemplate(viewingTemplate)">
            <el-descriptions-item label="绑定时间">{{ formatDate(viewingTemplate.bindTime) }}</el-descriptions-item>
            <el-descriptions-item label="生效时间">{{ formatDate(viewingTemplate.effectiveTime) }}</el-descriptions-item>
            <el-descriptions-item label="绑定模式">{{ getBindModeLabel(viewingTemplate.bindMode) }}</el-descriptions-item>
          </template>
        </el-descriptions>
      </div>
      <el-empty v-else description="没有可显示的模板信息" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewTemplateDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { 
  ElCard, ElRow, ElCol, ElTabs, ElTabPane, ElSelect, ElOption, ElInput, ElButton, ElTable, ElTableColumn, 
  ElPagination, ElTag, ElDescriptions, ElDescriptionsItem, ElDialog, ElForm, ElFormItem, ElDatePicker,
  ElPopconfirm, ElEmpty, ElMessage, ElMessageBox, ElIcon, ElBadge, ElTooltip, ElSwitch, ElCheckbox
} from 'element-plus'
import { Search, Plus, Edit, Delete, View, Setting } from '@element-plus/icons-vue'
import type { FormInstance, FormRules, TabsPaneContext, TableInstance } from 'element-plus'
import PageHeader from '@/components/PageHeader.vue'
import { 
  getTenantList, createTenant, updateTenant, deleteTenant, getTenantDetail, 
  getTenantTemplates, bindTemplateToTenant, unbindTemplateFromTenant, 
  TenantInfo, PermTemplate, searchUsers, UserForSelect, TenantTemplateBindRequest
} from '@/api/tenant'
import { getPermissionTemplates, PermissionTemplate as AvailablePermTemplate } from '@/api/permission' 
import { formatTimestamp } from '@/utils/format'
import { handleApiError, handleApiResponse, formatDateTime } from '@/utils/errorHandler'

interface BoundPermTemplate extends PermTemplate {
  bindTime?: number;
  effectiveTime?: number;
  bindMode?: number;
}

interface DetailTenantInfo extends TenantInfo {
  adminAccount?: string;
  phone?: string;
  tenantAddress?: string;
}

const router = useRouter()

// --- State --- 
const loading = ref(false)
const tenantList = ref<TenantInfo[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const activeTenantType = ref('all')
const selectedTenantId = ref<number | null>(null)
const selectedTenant = ref<DetailTenantInfo | null>(null)

const filters = reactive({
  keyword: '',
  dataIsolationMode: ''
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const tenantFormRef = ref<FormInstance>()
const tenantForm = reactive({
  id: undefined as number | undefined,
  tenantName: '',
  tenantCode: '',
  tenantType: 2, // 默认为企业商户租户
  dataIsolationMode: 1,
  adminUserId: null as number | string | null,
  adminUserDisplay: '',
  tenantPhone: '',
  tenantEmail: '',
  expireTime: null as Date | null,
  tenantAddress: ''
})
const adminUserSearchLoading = ref(false)
const adminUserOptions = ref<UserForSelect[]>([])
const tenantRules = reactive<FormRules>({
  tenantName: [{ required: true, message: '请输入租户名称', trigger: 'blur' }],
  tenantCode: [
    { required: true, message: '请输入租户编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  tenantType: [{ required: true, message: '请选择租户类型', trigger: 'change' }],
  dataIsolationMode: [{ required: true, message: '请选择数据隔离模式', trigger: 'change' }],
  adminUserId: [{ required: true, message: '请选择或输入管理员账号', trigger: 'change' }],
  expireTime: [{ required: true, message: '请选择有效期', trigger: 'change' }],
  tenantAddress: [{ required: false, message: '请输入地址', trigger: 'blur' }]
})

const boundTemplates = ref<BoundPermTemplate[]>([])
const bindDialogVisible = ref(false)
const availableTemplates = ref<AvailablePermTemplate[]>([])
const availableTemplatesTableRef = ref<TableInstance>()
const selectedTemplates = ref<AvailablePermTemplate[]>([])
const templateSearchKeyword = ref('')

const bindEffectiveTime = ref<Date | null>(null)
const bindMode = ref(1)

// 查看模板详情
const viewTemplateDialogVisible = ref(false)
const viewingTemplate = ref<BoundPermTemplate | AvailablePermTemplate | null>(null)

// --- Computed --- 
const isEnterpriseTemplate = (template: any): boolean => {
  // 根据模板类型判断
  return template.templateType === 2 || 
         (template.templateCode && (template.templateCode.startsWith('ENT_') || template.templateCode.includes('企业'))) ||
         (template.templateName && template.templateName.includes('企业'));
};

const hasBoundEnterpriseTemplate = computed(() => {
  if (selectedTenant.value?.tenantType === 2) {
    return boundTemplates.value.some(isEnterpriseTemplate);
  }
  return false;
});

const availableTemplatesFiltered = computed(() => {
  let templates = availableTemplates.value.filter(t => t.status === '已启用');

  if (selectedTenant.value?.tenantType === 2) {
    if (hasBoundEnterpriseTemplate.value) {
      templates = templates.filter(t => !isEnterpriseTemplate(t) || boundTemplates.value.some(bt => bt.id === t.id && isEnterpriseTemplate(bt)));
    }
  }
  
  if (!templateSearchKeyword.value) {
    return templates;
  }
  const keyword = templateSearchKeyword.value.toLowerCase();
  return templates.filter(t => 
    t.templateName.toLowerCase().includes(keyword) || 
    t.templateCode.toLowerCase().includes(keyword)
  );
});

const isTemplateSelectable = (row: any) => {
  if (selectedTenant.value?.tenantType === 2) {
    if (hasBoundEnterpriseTemplate.value && !boundTemplates.value.some(bt => bt.id === row.id && isEnterpriseTemplate(bt))) {
       if(isEnterpriseTemplate(row)) return false;
    }
    if (selectedTemplates.value.length > 0 && selectedTemplates.value.some(isEnterpriseTemplate) && hasBoundEnterpriseTemplate.value && !boundTemplates.value.some(bt => bt.id === row.id && isEnterpriseTemplate(bt))) {
      if(isEnterpriseTemplate(row) && !selectedTemplates.value.find(st => st.id === row.id && isEnterpriseTemplate(st))) return false;
    }
    if (isEnterpriseTemplate(row) && selectedTemplates.value.some(st => st.id !== row.id && isEnterpriseTemplate(st))) {
        return false;
    }
  }
  return true;
};

const disabledBindEffectiveDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7; // 不允许选择昨天及之前的日期
};

// --- Methods --- 

const fetchTenantList = async () => {
  loading.value = true
  selectedTenant.value = null 
  selectedTenantId.value = null
  try {
    // 根据选项卡值设置类型过滤条件
    let tenantType: number | undefined = undefined;
    if (activeTenantType.value === 'platform') tenantType = 1;
    else if (activeTenantType.value === 'enterprise') tenantType = 2;
    else if (activeTenantType.value === 'agent') tenantType = 3;

    // 构建请求参数
    const params = {
      tenantType,
      tenantName: filters.keyword || undefined,
      tenantCode: filters.keyword || undefined,
      dataIsolationMode: filters.dataIsolationMode || undefined,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    };

    // 调用API获取租户列表
    const res: any = await getTenantList(params);
    console.log('获取租户列表API返回:', res);
    
    // 处理标准响应格式 { code: 200, message: 'success', data: {...} }
    let result = res;
    if (res && res.code === 200 && res.data) {
      result = res.data;
    }
    
    // 处理分页数据
    if (result && result.records) {
      tenantList.value = result.records;
      total.value = result.total || 0;
    } else if (Array.isArray(result)) {
      tenantList.value = result;
      total.value = result.length;
    } else {
      console.warn('无法识别的租户列表响应格式:', result);
      tenantList.value = [];
      total.value = 0;
    }
  } catch (error) {
    handleApiError(error, '获取租户列表失败');
    tenantList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const fetchTenantDetails = async (id: number) => {
  try {
    const response: any = await getTenantDetail(id);
    console.log('获取租户详情API返回:', response);
    
    // 处理标准返回格式 { code: 200, message: 'success', data: {...} }
    let tenantData;
    if (response && response.code === 200 && response.data) {
      tenantData = response.data;
    } else {
      tenantData = response;
    }
    
    if (tenantData) {
      selectedTenant.value = {
        ...tenantData,
        adminAccount: tenantData.adminUsername || '',
        phone: tenantData.tenantPhone || '',
        tenantAddress: tenantData.tenantAddress || ''
      }
      fetchBoundTemplates(id)
    } else {
      selectedTenant.value = null;
    }
  } catch (error) {
     handleApiError(error, '获取租户详情失败');
     selectedTenant.value = null
  }
}

const fetchBoundTemplates = async (tenantId: number) => {
  try {
    const response: any = await getTenantTemplates(tenantId);
    console.log('获取已绑定模板API返回:', response);
    
    // 处理标准返回格式 { code: 200, message: 'success', data: [...] }
    let templatesData;
    if (response && response.code === 200 && response.data) {
      templatesData = response.data;
    } else {
      templatesData = response;
    }
    
    if (templatesData && Array.isArray(templatesData)) {
      // 处理已绑定模板数据
      boundTemplates.value = templatesData.map(template => ({
        ...template,
        status: '已启用', // 默认状态
        moduleCount: template.moduleCount || 0
      }));
    } else {
      boundTemplates.value = [];
    }
  } catch (error) {
    handleApiError(error, '获取已绑定模板失败');
    boundTemplates.value = []
  }
}

const fetchAvailableTemplates = async () => {
  try {
    const params = {
      pageNum: 1,
      pageSize: 100, // 获取足够多的模板
      // 根据租户类型过滤
      templateType: selectedTenant.value ? selectedTenant.value.tenantType : undefined,
      // 如果有搜索关键词，添加到请求参数
      templateName: templateSearchKeyword.value || undefined
    };
    
    const response: any = await getPermissionTemplates(params);
    console.log('获取可用模板列表API返回:', response);
    
    // 处理标准返回格式 { code: 200, message: 'success', data: {records: [...]} }
    let result: any = {};
    if (response && response.code === 200 && response.data) {
      result = response.data;
    } else {
      result = response;
    }
    
    if (result && result.records) {
      availableTemplates.value = result.records.map((template: any) => ({
        ...template,
        status: template.isSystem === 1 ? '已启用' : '未启用'
      }));
    } else {
      availableTemplates.value = [];
    }
  } catch (error) {
    handleApiError(error, '获取可用模板列表失败');
    availableTemplates.value = [];
  }
}

const handleTenantTypeChange = (tab: TabsPaneContext) => {
  currentPage.value = 1
  fetchTenantList()
}

const handleTenantSelect = (row: TenantInfo) => {
  if (selectedTenantId.value !== row.id) {
    selectedTenantId.value = row.id
    fetchTenantDetails(row.id)
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchTenantList()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchTenantList()
}

const handleAddTenant = () => {
  resetTenantForm()
  isEdit.value = false
  dialogVisible.value = true
}

const handleEditTenant = (id: number | undefined) => {
  if (!id) return;
  const tenant = selectedTenant.value;
  if (tenant) {
    resetTenantForm(); 
    isEdit.value = true
    dialogVisible.value = true
    
    Object.assign(tenantForm, {
      id: tenant.id,
      tenantName: tenant.tenantName,
      tenantCode: tenant.tenantCode,
      tenantType: tenant.tenantType,
      dataIsolationMode: tenant.dataIsolationMode,
      adminUserId: tenant.adminUserId || null,
      tenantPhone: tenant.tenantPhone || '',
      tenantEmail: tenant.tenantEmail || '',
      expireTime: tenant.expireTime ? new Date(tenant.expireTime) : null,
      tenantAddress: tenant.tenantAddress || ''
    });
    
    if (tenant.adminUserId && tenant.adminUsername) {
        adminUserOptions.value = [{
          id: tenant.adminUserId, 
          username: tenant.adminUsername, 
          email: ''
        }];
        tenantForm.adminUserDisplay = tenant.adminUsername;
    }
  } else {
    ElMessage.warning('未找到租户信息');
  }
}

const handleDeleteTenant = async (id: number | undefined) => {
  if (!id) return;
  try {
    await deleteTenant(id);
    ElMessage.success('租户删除成功')
    if (selectedTenantId.value === id) {
        selectedTenantId.value = null;
        selectedTenant.value = null;
    }
    fetchTenantList()
  } catch (error) {
    handleApiError(error, '删除租户失败');
  }
}

const resetTenantForm = () => {
  tenantForm.id = undefined
  tenantForm.tenantName = ''
  tenantForm.tenantCode = ''
  tenantForm.tenantType = 2 // 默认为企业商户租户
  tenantForm.dataIsolationMode = 1
  tenantForm.adminUserId = null
  tenantForm.adminUserDisplay = ''
  tenantForm.tenantPhone = ''
  tenantForm.tenantEmail = ''
  tenantForm.expireTime = null
  tenantForm.tenantAddress = ''
  adminUserOptions.value = []
  if (tenantFormRef.value) {
    tenantFormRef.value.resetFields()
  }
}

const submitTenantForm = async () => {
  if (!tenantFormRef.value) return
  await tenantFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const formData: any = { 
          id: tenantForm.id,
          tenantName: tenantForm.tenantName,
          tenantCode: tenantForm.tenantCode,
          tenantType: tenantForm.tenantType,
          dataIsolationMode: tenantForm.dataIsolationMode,
          adminUserId: tenantForm.adminUserId,
          tenantPhone: tenantForm.tenantPhone,
          tenantEmail: tenantForm.tenantEmail,
          expireTime: tenantForm.expireTime ? tenantForm.expireTime.getTime() : undefined,
          tenantAddress: tenantForm.tenantAddress
        }
        
        if (formData.expireTime === undefined) {
          delete formData.expireTime;
        }
        
        if (isEdit.value && formData.id) {
          await updateTenant(formData.id, formData)
          ElMessage.success('租户更新成功')
          if (selectedTenantId.value === formData.id) {
             fetchTenantDetails(formData.id);
          }
        } else {
          const result: any = await createTenant(formData)
          ElMessage.success('租户创建成功')
          // 如果创建成功且返回了ID，自动选中新创建的租户
          if (result && result.id) {
            selectedTenantId.value = result.id;
            fetchTenantDetails(result.id);
          }
        }
        dialogVisible.value = false
        fetchTenantList()
      } catch (error) {
        handleApiError(error, '保存租户失败，请检查输入或联系管理员');
      }
    }
  })
}

const handleBindTemplate = () => {
  if (!selectedTenant.value) return;

  // 对企业租户的特殊处理（企业租户只能绑定一个企业模板）
  if (selectedTenant.value.tenantType === 2) {
    // 检查是否已有企业模板
    const hasEnterpriseTemplate = boundTemplates.value.some(template => 
      isEnterpriseTemplate(template)
    );
    
    if (hasEnterpriseTemplate) {
      ElMessage.warning('企业商户只能绑定一个企业模板，请先解绑现有的企业模板。');
      return;
    }
  }

  templateSearchKeyword.value = ''
  selectedTemplates.value = []
  bindEffectiveTime.value = new Date(); // 默认当前时间
  bindMode.value = 1; // 默认继承
  fetchAvailableTemplates() 
  bindDialogVisible.value = true
  if (availableTemplatesTableRef.value) {
    availableTemplatesTableRef.value.clearSelection();
  }
}

const handleViewTemplate = (templateId: number) => {
  const boundTpl = boundTemplates.value.find(t => (t.templateId || t.id) === templateId);
  if (boundTpl) {
    viewingTemplate.value = { ...boundTpl };
    viewTemplateDialogVisible.value = true;
  } else {
    // 如果在已绑定模板中找不到，尝试在可用模板中查找
    const availableTpl = availableTemplates.value.find(t => t.id === templateId);
    if (availableTpl) {
      viewingTemplate.value = { ...availableTpl };
      viewTemplateDialogVisible.value = true;
    } else {
      handleApiError(new Error('未找到模板信息'), '未找到模板信息');
    }
  }
}

const handleUnbindTemplate = async (row: any) => {
  if (!selectedTenant.value) return;
  
  // 从模板数据中获取templateId字段（而不是id字段）
  const templateId = row.templateId || row.id;
  console.log('解绑模板，使用templateId:', templateId, '模板数据:', row);
  
  try {
    await unbindTemplateFromTenant(selectedTenant.value.id, templateId);
    ElMessage.success('模板解绑成功');
    fetchBoundTemplates(selectedTenant.value.id);
  } catch (error) {
    handleApiError(error, '解绑模板失败');
  }
}

const handleTemplateSelectionChange = (val: any[]) => {
   if (selectedTenant.value?.tenantType === 2) {
    const enterpriseTemplatesSelected = val.filter(isEnterpriseTemplate);
    if (enterpriseTemplatesSelected.length > 1) {
      ElMessage.warning('企业商户只能选择一个企业模板进行绑定。');
      const firstEnterprise = enterpriseTemplatesSelected[0];
      selectedTemplates.value = val.filter(t => !isEnterpriseTemplate(t) || t.id === firstEnterprise.id);
      if (availableTemplatesTableRef.value) {
          availableTemplatesTableRef.value.clearSelection();
          selectedTemplates.value.forEach(row => {
             availableTemplatesTableRef.value!.toggleRowSelection(row, true);
          });
      }
      return;
    }
  }
  selectedTemplates.value = val;
}

const confirmBindTemplate = async () => {
  if (!selectedTenant.value || selectedTemplates.value.length === 0) return;

  const tenantId = selectedTenant.value.id;
  const tenantType = selectedTenant.value.tenantType;

  // 企业租户特殊处理
  if (tenantType === 2) {
    // 筛选出所选的企业模板
    const selectedEnterpriseTemplates = selectedTemplates.value.filter(template => 
      isEnterpriseTemplate(template)
    );
    
    // 校验：企业租户不能同时选择多个企业模板
    if (selectedEnterpriseTemplates.length > 1) {
      handleApiError(new Error('企业商户一次只能绑定一个企业模板'), '企业商户一次只能绑定一个企业模板');
      return;
    }
    
    // 校验：如果已有企业模板且选择了新的企业模板（不同于现有的）
    const existingEnterpriseTemplates = boundTemplates.value.filter(template => 
      isEnterpriseTemplate(template)
    );
    
    if (existingEnterpriseTemplates.length > 0 && selectedEnterpriseTemplates.length > 0) {
      // 检查是否选择了不同的企业模板
      // 获取已有企业模板ID（处理不同字段名）
      const existingTemplateIds = existingEnterpriseTemplates.map(t => {
        // 根据实际返回数据结构选择正确的ID字段
        return t.templateId || t.id;
      });
      
      const selectedTemplateIds = selectedEnterpriseTemplates.map(t => t.id);
      
      const hasNewEnterpriseTemplate = selectedTemplateIds.some(id => !existingTemplateIds.includes(id));
      
      if (hasNewEnterpriseTemplate) {
        handleApiError(new Error('该企业租户已绑定一个企业模板，请先解绑现有企业模板'), '该企业租户已绑定一个企业模板，请先解绑现有企业模板');
        return;
      }
    }
  }
  
  try {
    // 准备模板绑定参数
    const templates: TenantTemplateBindRequest[] = selectedTemplates.value.map(template => ({
      templateId: template.id,
      bindMode: bindMode.value,
      effectiveTime: bindEffectiveTime.value ? bindEffectiveTime.value.getTime() : Date.now()
    }));
    
    // 调用绑定接口
    await bindTemplateToTenant({
      tenantId,
      templates
    });
    
    ElMessage.success(`成功绑定 ${selectedTemplates.value.length} 个模板`);
    bindDialogVisible.value = false;
    fetchBoundTemplates(tenantId); 
  } catch (error) {
    handleApiError(error, '绑定模板失败');
  }
}

const formatDate = (timestamp: number | undefined | null) => {
  if (!timestamp) return '-'
  if (timestamp > 4000000000000) return '永久'
  return formatTimestamp(timestamp, 'YYYY-MM-DD HH:mm:ss')
}

const getBindModeLabel = (mode: number | undefined) => {
  if (mode === 1) return '继承';
  if (mode === 2) return '快照';
  return '-';
}

const getStatusLabel = (tenant: TenantInfo) => {
  const now = Date.now()
  if (tenant.expireTime < now) return '已过期'
  return '正常'
}

const getStatusTag = (tenant: TenantInfo) => {
   const now = Date.now()
   if (tenant.expireTime < now) return 'danger'
   return 'success'
}

const getStatusClass = (row: TenantInfo) => {
  const now = Date.now()
  if (row.expireTime < now) {
    return 'status-expired'
  }
  return 'status-active'
}

const getTenantTypeLabel = (type: number) => {
  switch (type) {
    case 1: return '平台管理租户'
    case 2: return '企业商户租户'
    case 3: return '代理所租户'
    default: return '未知类型'
  }
}

const getTenantTypeTag = (type: number) => {
  switch (type) {
    case 2: return ''      // 企业商户 - 默认样式
    case 3: return 'warning' // 代理所 - 黄色警告样式
    case 1: return 'success' // 平台管理 - 绿色成功样式
    default: return 'info'  // 其他 - 蓝色信息样式
  }
}

const getDataIsolationModeLabel = (mode: number) => {
  switch (mode) {
    case 1: return '行级隔离'
    case 2: return 'Schema隔离'
    case 3: return '独立库'
    default: return '未知模式'
  }
}

// Type guard to check if the template is a BoundPermTemplate
const isBoundTemplate = (template: any): template is BoundPermTemplate => {
  return template && typeof template.bindTime !== 'undefined';
};

const remoteSearchAdminUsers = async (query: string) => {
  if (query) {
    adminUserSearchLoading.value = true;
    try {
      adminUserOptions.value = await searchUsers(query);
    } catch (error) {
      console.error("Failed to search users:", error);
      adminUserOptions.value = [];
    } finally {
      adminUserSearchLoading.value = false;
    }
  } else {
    adminUserOptions.value = [];
  }
};

const handleAdminUserSelect = (userId: number | string | null) => {
    if (userId) {
        const selectedUser = adminUserOptions.value.find(u => u.id === userId);
        if (selectedUser) {
            tenantForm.adminUserDisplay = `${selectedUser.username} (${selectedUser.email})`;
        }
    } else {
        tenantForm.adminUserDisplay = '';
    }
};

// 处理模板搜索
const handleTemplateSearch = () => {
  fetchAvailableTemplates();
}

onMounted(() => {
  fetchTenantList()
})

watch([() => filters.keyword, () => filters.dataIsolationMode], () => {
  currentPage.value = 1;
  fetchTenantList();
});

watch(selectedTenant, (newTenant, oldTenant) => {
  if (!bindDialogVisible.value && newTenant?.id !== oldTenant?.id) {
    bindEffectiveTime.value = null;
    bindMode.value = 1;
    selectedTemplates.value = [];
  }
});

watch(bindDialogVisible, (isVisible) => {
  if (!isVisible) {
    bindEffectiveTime.value = null;
    bindMode.value = 1;
    selectedTemplates.value = [];
    if (availableTemplatesTableRef.value) {
        availableTemplatesTableRef.value.clearSelection();
    }
  }
  if (!isVisible && viewingTemplate.value) { // Clear viewing template when its dialog closes
    viewingTemplate.value = null;
  }
})

</script>

<style scoped>
.tenant-container {
  padding: 20px;
}

.responsive-col {
  margin-bottom: 20px;
}

.left-panel,
.right-panel {
  height: calc(100vh - 120px); 
  display: flex;
  flex-direction: column;
}

.left-panel .el-card__body,
.right-panel .el-card__body {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  padding: 15px;
}

.filter-section {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 10px;
}

.tenant-list-container {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

:deep(.left-panel .el-table) {
  flex-grow: 1;
}

.tenant-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 0 5px;
}

.tenant-list-header h3 {
  margin: 0;
  font-size: 16px;
}

.tenant-list-header span {
  font-size: 12px;
  color: #909399;
}

.tenant-list-item {
  line-height: 1.4;
  cursor: pointer;
}
.tenant-info {
  display: flex;
  align-items: center;
  font-weight: 500;
  margin-bottom: 4px;
}
.tenant-meta {
  font-size: 12px;
  color: #909399;
}

.tenant-status {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
  flex-shrink: 0;
}
.status-active {
  background-color: #67c23a;
}
.status-expired {
  background-color: #f56c6c;
}

.pagination-container-left {
  margin-top: 10px;
  display: flex;
  justify-content: center; 
}

.right-panel {
  height: auto;
  min-height: 500px;
  position: relative;
  padding-bottom: 20px;
}

.right-panel .el-card__body {
  overflow-y: auto; 
}

.tenant-detail-header {
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
  position: relative;
}

.tenant-detail-header h2 {
  margin: 0 0 5px 0;
  font-size: 20px;
  display: flex;
  align-items: center;
}

.tenant-detail-header .tenant-meta {
  font-size: 13px;
  color: #606266;
}

.header-actions {
  position: absolute;
  right: 0;
  top: 0;
}

.description-section {
  margin-bottom: 30px;
}
.tenant-container :deep(.el-descriptions__label) {
  width: 120px;
}

.permission-template-section {
  margin-top: 20px;
}

.permission-template-section .section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.permission-template-section .section-header h3 {
  margin: 0;
  font-size: 18px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.text-danger {
  color: #f56c6c;
}

.tenant-container :deep(.el-tabs__header) {
  margin-bottom: 10px;
}
.tenant-container :deep(.el-tabs__item) {
  padding: 0 15px;
}

:deep(.left-panel .el-table .el-table__row) {
  cursor: pointer;
}
:deep(.left-panel .el-table td.el-table__cell) {
  padding: 8px 0;
}

@media (max-width: 1200px) {
  .el-col:not(.responsive-col) {
    width: 100% !important;
    flex: 0 0 100% !important;
    max-width: 100% !important;
    margin-bottom: 20px;
  }
  .left-panel, .right-panel {
    height: auto;
    min-height: 300px;
  }
   .left-panel .el-table {
      height: auto;
      max-height: 400px;
   }
}

:deep(.el-table) {
  width: 100%;
  margin-bottom: 15px;
}
</style> 