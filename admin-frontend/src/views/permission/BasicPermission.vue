<template>
  <div class="basic-permission-container">
    <PageHeader title="基础权限配置" subtitle="配置系统基础的数据策略和操作权限" />

    <el-row :gutter="20">
      <!-- Data Strategy Configuration -->
      <el-col :span="12">
        <el-card class="box-card data-strategy-card">
          <template #header>
            <div class="card-header">
              <span>数据策略配置</span>
              <el-button type="primary" :icon="Plus" @click="handleAddDataStrategy">
                新增策略
              </el-button>
            </div>
          </template>
          <!-- Filters and List for Data Strategy -->
          <div class="config-content">
            <div class="search-bar">
              <el-input 
                v-model="strategySearchKeyword" 
                placeholder="搜索策略..." 
                class="filter-input" 
                @keyup.enter="handleStrategySearch"
                clearable
                :suffix-icon="Search"
              >
                <template #append>
                  <el-button :icon="Search" @click="handleStrategySearch" />
                </template>
              </el-input>
            </div>
            <el-table 
              :data="dataStrategies" 
              style="width: 100%" 
              height="400px"
              v-loading="strategyLoading"
              empty-text="暂无数据"
            >
              <el-table-column label="策略" prop="policyName">
                <template #default="{ row }">
                  <div>{{ row.policyName }} <el-tag size="small">{{ row.priority }}</el-tag></div>
                  <div class="text-muted">{{ row.policyDescription || '' }}</div>
                </template>
              </el-table-column>
              <el-table-column label="类型" prop="conditionType" width="100">
                <template #default="{ row }">
                   {{ row.conditionType === 1 ? 'SQL片段' : 'SpEL' }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100" align="right">
                <template #default="{ row }">
                   <el-button text type="primary" size="small" @click="handleViewDataStrategy(row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination 
              v-model:current-page="strategyQueryParams.pageNum"
              v-model:page-size="strategyQueryParams.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="dataStrategyTotal"
              layout="total, sizes, prev, pager, next"
              @size-change="handleStrategySizeChange"
              @current-change="handleStrategyCurrentChange"
              class="pagination"
              small
            />
          </div>
        </el-card>
      </el-col>

      <!-- Operation Permission Configuration -->
      <el-col :span="12">
        <el-card class="box-card operation-permission-card">
          <template #header>
            <div class="card-header">
              <span>操作权限配置</span>
              <el-button type="primary" :icon="Plus" @click="handleAddOperationPermission">
                新增权限
              </el-button>
            </div>
          </template>
           <!-- Filters and List for Operation Permissions -->
          <div class="config-content">
             <div class="search-bar">
               <el-input 
                 v-model="permSearchKeyword" 
                 placeholder="搜索权限..." 
                 class="filter-input" 
                 @keyup.enter="handlePermSearch"
                 clearable
                 :suffix-icon="Search"
               >
                 <template #append>
                   <el-button :icon="Search" @click="handlePermSearch" />
                 </template>
               </el-input>
             </div>

             <el-table 
               :data="operationPermissions" 
               style="width: 100%" 
               height="400px"
               v-loading="permLoading"
               empty-text="暂无数据"
             >
               <el-table-column label="权限" prop="permsName">
                 <template #default="{ row }">
                   <div>{{ row.permsName }}</div>
                   <div class="text-muted">{{ row.apiMethod }} {{ row.apiPath }}</div>
                 </template>
               </el-table-column>
               <el-table-column label="类型" prop="permType" width="100">
                  <template #default="{ row }">
                     {{ row.permType || '功能权限'}} 
                  </template>
               </el-table-column>
               <el-table-column label="操作" width="100" align="right">
                 <template #default="{ row }">
                   <el-button text type="primary" size="small" @click="handleViewOperationPermission(row)">查看</el-button>
                 </template>
               </el-table-column>
             </el-table>
             
             <el-pagination 
               v-model:current-page="permQueryParams.pageNum"
               v-model:page-size="permQueryParams.pageSize"
               :page-sizes="[10, 20, 50, 100]"
               :total="operationPermissionTotal"
               layout="total, sizes, prev, pager, next"
               @size-change="handlePermSizeChange"
               @current-change="handlePermCurrentChange"
               class="pagination"
               small
             />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Data Strategy Details Modal -->
    <el-dialog v-model="dataStrategyDialogVisible" title="策略详情" width="600px">
      <el-descriptions v-if="currentDataStrategy" :column="1" border label-align="right">
        <el-descriptions-item label="策略名称">{{ currentDataStrategy.policyName }}</el-descriptions-item>
        <el-descriptions-item label="策略编码">{{ currentDataStrategy.policyCode }}</el-descriptions-item>
        <el-descriptions-item label="条件类型">{{ currentDataStrategy.conditionType === 1 ? 'SQL片段' : 'SpEL' }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ currentDataStrategy.priority }}</el-descriptions-item>
        <el-descriptions-item label="生效表">{{ currentDataStrategy.effectTables }}</el-descriptions-item>
        <el-descriptions-item label="策略描述">{{ currentDataStrategy.policyDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="条件表达式">
          <pre><code>{{ currentDataStrategy.conditionExpression }}</code></pre>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dataStrategyDialogVisible = false">关闭</el-button>
          <el-button type="danger" @click="handleDeleteDataStrategy(currentDataStrategy)">删除</el-button>
          <el-button type="primary" @click="handleEditDataStrategy(currentDataStrategy)">编辑</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Operation Permission Details Modal -->
    <el-dialog v-model="opPermDialogVisible" title="操作权限详情" width="600px">
      <el-descriptions v-if="currentOpPerm" :column="1" border label-align="right">
        <el-descriptions-item label="权限名称">{{ currentOpPerm.permsName }}</el-descriptions-item>
        <el-descriptions-item label="权限编码">{{ currentOpPerm.permsCode }}</el-descriptions-item>
        <el-descriptions-item label="权限类型">{{ currentOpPerm.permType }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ currentOpPerm.apiMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="接口路径">{{ currentOpPerm.apiPath || '-' }}</el-descriptions-item>
        <el-descriptions-item label="权限描述">{{ currentOpPerm.permsDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentOpPerm.createTime ? formatDateTime(currentOpPerm.createTime) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ currentOpPerm.updateTime ? formatDateTime(currentOpPerm.updateTime) : '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="opPermDialogVisible = false">关闭</el-button>
          <el-button type="danger" @click="handleDeleteOperationPermission(currentOpPerm)">删除</el-button>
          <el-button type="primary" @click="handleEditOperationPermission(currentOpPerm)">编辑</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Add/Edit Data Strategy Modal -->
    <el-dialog 
      v-model="strategyFormDialogVisible"
      :title="isEditingStrategy ? '编辑数据策略' : '新增数据策略'"
      width="700px"
      @close="resetStrategyForm"
    >
      <el-form 
        ref="strategyFormRef"
        :model="strategyForm"
        :rules="strategyRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="策略名称" prop="policyName">
              <el-input v-model="strategyForm.policyName" placeholder="请输入策略名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="策略编码" prop="policyCode">
              <el-input v-model="strategyForm.policyCode" placeholder="请输入策略编码" :disabled="isEditingStrategy"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="条件类型" prop="conditionType">
              <el-select v-model="strategyForm.conditionType" placeholder="请选择条件类型" style="width: 100%;">
                <el-option label="SQL片段" :value="1" />
                <el-option label="SpEL表达式" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
           <el-col :span="12">
             <el-form-item label="优先级" prop="priority">
               <el-slider v-model="strategyForm.priority" :min="0" :max="100" show-input />
             </el-form-item>
           </el-col>
        </el-row>
        <el-form-item label="生效表" prop="effectTables">
          <el-input v-model="strategyForm.effectTables" placeholder="请输入生效的表名，多个用逗号分隔" />
        </el-form-item>
        <el-form-item label="策略描述" prop="policyDescription">
          <el-input type="textarea" v-model="strategyForm.policyDescription" placeholder="请输入策略描述" />
        </el-form-item>
        <el-form-item label="条件表达式" prop="conditionExpression">
          <el-input type="textarea" v-model="strategyForm.conditionExpression" placeholder="请输入条件表达式 (SQL或SpEL)" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="strategyFormDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitStrategyForm">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Add/Edit Operation Permission Modal -->
     <el-dialog 
      v-model="opPermFormDialogVisible"
      :title="isEditingOpPerm ? '编辑操作权限' : '新增操作权限'"
      width="700px"
      @close="resetOpPermForm"
    >
      <el-form 
        ref="opPermFormRef"
        :model="opPermForm"
        :rules="opPermRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限名称" prop="permsName">
              <el-input v-model="opPermForm.permsName" placeholder="请输入权限名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限编码" prop="permsCode">
              <el-input v-model="opPermForm.permsCode" placeholder="请输入权限编码 (如: user:view)" :disabled="isEditingOpPerm"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限类型" prop="permType">
               <el-select v-model="opPermForm.permType" placeholder="请选择权限类型" style="width: 100%;">
                 <el-option label="API" value="API" />
                 <el-option label="BUTTON" value="BUTTON" />
               </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方法" prop="apiMethod">
              <el-select v-model="opPermForm.apiMethod" placeholder="请选择HTTP方法" style="width: 100%;" clearable>
                <el-option label="全部请求方法" value="*" />
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
                <el-option label="PUT" value="PUT" />
                <el-option label="DELETE" value="DELETE" />
                <el-option label="PATCH" value="PATCH" />
                <el-option label="OPTIONS" value="OPTIONS" />
                <el-option label="HEAD" value="HEAD" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="接口路径" prop="apiPath">
          <el-input v-model="opPermForm.apiPath" placeholder="请输入接口路径 (Ant风格, 如: /api/users/**)" />
        </el-form-item>
        <el-form-item label="权限描述" prop="permsDescription">
          <el-input type="textarea" v-model="opPermForm.permsDescription" placeholder="请输入权限描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="opPermFormDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOpPermForm">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import PageHeader from '@/components/PageHeader.vue'
import {
  ElCard, ElRow, ElCol, ElButton, ElTable, ElTableColumn, 
  ElPagination, ElInput, ElTag, ElDialog, ElDescriptions,
  ElDescriptionsItem, ElMessage, ElMessageBox, ElSlider, ElSelect, ElOption,
  ElForm, ElFormItem
} from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
// 导入操作权限相关的API
import {
  getOperationPermissionsPage,
  getOperationPermissionDetail,
  createOperationPermission,
  updateOperationPermission,
  deleteOperationPermission,
  PermQueryParams,
  PermPageResult
} from '@/api/permission'
import type { OperationPermission } from '@/api/permission'

// 导入数据策略相关的API
import {
  getDataStrategiesPage,
  getDataStrategyDetail,
  createDataStrategy,
  updateDataStrategy,
  deleteDataStrategy,
  DataStrategyQueryParams,
  DataStrategyPageResult,
  DataStrategy
} from '@/api/permission'

// 导入错误处理工具函数
import { handleApiError, handleApiResponse, formatDateTime } from '@/utils/errorHandler'

// 添加API响应的标准格式接口
interface StandardApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

// --- State ---
const dataStrategies = ref<DataStrategy[]>([])
const operationPermissions = ref<OperationPermission[]>([])
const dataStrategyTotal = ref(0)
const operationPermissionTotal = ref(0)
const dataStrategyDialogVisible = ref(false)
const opPermDialogVisible = ref(false)
const currentDataStrategy = ref<DataStrategy | null>(null)
const currentOpPerm = ref<OperationPermission | null>(null)

// 数据策略分页参数
const strategyQueryParams = reactive<DataStrategyQueryParams>({
  pageNum: 1,
  pageSize: 10,
  policyName: '',
  policyCode: '',
  conditionType: undefined
})

// 数据策略搜索关键字
const strategySearchKeyword = ref('')

// 数据策略加载状态
const strategyLoading = ref(false)

// 操作权限分页参数
const permQueryParams = reactive<PermQueryParams>({
  pageNum: 1,
  pageSize: 10,
  permsName: '',
  permsCode: '',
  apiMethod: '',
  permType: ''
})

// 操作权限搜索关键字
const permSearchKeyword = ref('')

// 操作权限加载状态
const permLoading = ref(false)

// --- Forms State ---
const strategyFormDialogVisible = ref(false)
const opPermFormDialogVisible = ref(false)
const isEditingStrategy = ref(false)
const isEditingOpPerm = ref(false)

const strategyFormRef = ref<FormInstance>()
const opPermFormRef = ref<FormInstance>()

const initialStrategyForm: Omit<DataStrategy, 'id' | 'createTime' | 'updateTime' | 'conditionTypeName'> = {
  policyName: '',
  policyCode: '',
  conditionType: 1,
  priority: 50,
  effectTables: '',
  policyDescription: '',
  conditionExpression: ''
}
const strategyForm = reactive<DataStrategy>({ ...initialStrategyForm, id: undefined })

const initialOpPermForm: Partial<OperationPermission> = {
  permsName: '',
  permsCode: '',
  permType: 'API', // 默认改为API
  apiMethod: 'GET',
  apiPath: '',
  permsDescription: ''
}
const opPermForm = reactive<Partial<OperationPermission>>({ ...initialOpPermForm })

// --- Form Rules ---
const strategyRules = reactive<FormRules>({
  policyName: [{ required: true, message: '请输入策略名称', trigger: 'blur' }],
  policyCode: [
    { required: true, message: '请输入策略编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_:]+$/, message: '只能包含字母、数字、下划线和冒号', trigger: 'blur' }
  ],
  conditionType: [{ required: true, message: '请选择条件类型', trigger: 'change' }],
  effectTables: [{ required: true, message: '请输入生效表', trigger: 'blur' }],
  conditionExpression: [{ required: true, message: '请输入条件表达式', trigger: 'blur' }],
})

const opPermRules = reactive<FormRules>({
  permsName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  permsCode: [
    { required: true, message: '请输入权限编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_:]+$/, message: '只能包含字母、数字、下划线和冒号', trigger: 'blur' }
  ],
  permType: [{ required: true, message: '请选择权限类型', trigger: 'change' }],
  apiPath: [{ required: true, message: '请输入接口路径', trigger: 'blur' }],
  apiMethod: [{ required: true, message: '请选择请求方法', trigger: 'change' }],
})

// --- 数据加载和处理方法 ---
onMounted(() => {
  loadDataStrategies()
  loadOperationPermissions()
})

// 加载数据策略
const loadDataStrategies = async () => {
  strategyLoading.value = true
  try {
    const response = await getDataStrategiesPage(strategyQueryParams) as unknown as StandardApiResponse<DataStrategyPageResult>
    console.log('数据策略API返回:', response);
    
    // 标准格式处理：处理响应可能是 { code: 200, message: 'success', data: {...} } 的情况
    let result: DataStrategyPageResult;
    if (response && response.code === 200) {
      // 新的标准格式响应
      result = response.data;
    } else {
      // 旧格式或直接返回数据（此时response直接是DataStrategyPageResult类型）
      result = response as unknown as DataStrategyPageResult;
    }
    
    // 处理可能的不同数据结构 
    if (result && result.records) {
      dataStrategies.value = result.records;
      dataStrategyTotal.value = result.total || 0;
    } else if (Array.isArray(result)) {
      dataStrategies.value = result as unknown as DataStrategy[];
      dataStrategyTotal.value = result.length;
    } else {
      console.warn('无法识别的数据策略响应格式:', result);
      dataStrategies.value = [];
      dataStrategyTotal.value = 0;
    }
  } catch (error) {
    handleApiError(error, '获取数据策略列表失败');
    dataStrategies.value = []
    dataStrategyTotal.value = 0
  } finally {
    strategyLoading.value = false
  }
}

// 处理数据策略搜索
const handleStrategySearch = () => {
  // 更新查询参数中的关键字搜索
  if (strategySearchKeyword.value) {
    strategyQueryParams.policyName = strategySearchKeyword.value
  } else {
    strategyQueryParams.policyName = ''
  }
  strategyQueryParams.pageNum = 1 // 重置到第一页
  loadDataStrategies()
}

// 重置数据策略搜索
const resetStrategySearch = () => {
  strategySearchKeyword.value = ''
  Object.assign(strategyQueryParams, {
    pageNum: 1,
    pageSize: 10,
    policyName: '',
    policyCode: '',
    conditionType: undefined
  })
  loadDataStrategies()
}

// 处理数据策略分页变化
const handleStrategyCurrentChange = (val: number) => {
  strategyQueryParams.pageNum = val
  loadDataStrategies()
}

// 处理数据策略每页显示条数变化
const handleStrategySizeChange = (val: number) => {
  strategyQueryParams.pageSize = val
  strategyQueryParams.pageNum = 1
  loadDataStrategies()
}

// 加载操作权限列表
const loadOperationPermissions = async () => {
  permLoading.value = true
  try {
    const response = await getOperationPermissionsPage(permQueryParams) as unknown as StandardApiResponse<PermPageResult>
    console.log('操作权限API返回:', response);
    
    // 标准格式处理：处理响应可能是 { code: 200, message: 'success', data: {...} } 的情况
    let result: PermPageResult;
    if (response && response.code === 200) {
      // 新的标准格式响应
      result = response.data;
    } else {
      // 旧格式或直接返回数据（此时response直接是PermPageResult类型）
      result = response as unknown as PermPageResult;
    }
    
    // 处理可能的不同数据结构
    if (result && result.records) {
      operationPermissions.value = result.records;
      operationPermissionTotal.value = result.total || 0;
    } else if (Array.isArray(result)) {
      operationPermissions.value = result as unknown as OperationPermission[];
      operationPermissionTotal.value = result.length;
    } else {
      console.warn('无法识别的操作权限响应格式:', result);
      operationPermissions.value = [];
      operationPermissionTotal.value = 0;
    }
  } catch (error) {
    handleApiError(error, '获取操作权限列表失败');
    operationPermissions.value = []
    operationPermissionTotal.value = 0
  } finally {
    permLoading.value = false
  }
}

// 处理操作权限搜索
const handlePermSearch = () => {
  // 更新查询参数中的关键字搜索
  if (permSearchKeyword.value) {
    permQueryParams.permsName = permSearchKeyword.value
    permQueryParams.permsCode = '' // 清空权限编码查询条件
  } else {
    permQueryParams.permsName = ''
    permQueryParams.permsCode = ''
  }
  permQueryParams.pageNum = 1 // 重置到第一页
  loadOperationPermissions()
}

// 重置操作权限搜索
const resetPermSearch = () => {
  permSearchKeyword.value = ''
  Object.assign(permQueryParams, {
    pageNum: 1,
    pageSize: 10,
    permsName: '',
    permsCode: '',
    apiMethod: '',
    permType: ''
  })
  loadOperationPermissions()
}

// 处理操作权限分页变化
const handlePermCurrentChange = (val: number) => {
  permQueryParams.pageNum = val
  loadOperationPermissions()
}

// 处理每页显示条数变化
const handlePermSizeChange = (val: number) => {
  permQueryParams.pageSize = val
  permQueryParams.pageNum = 1
  loadOperationPermissions()
}

// --- 数据策略相关处理方法 ---
const handleAddDataStrategy = () => {
  isEditingStrategy.value = false;
  strategyForm.id = undefined; // 清除ID，确保是新增
  Object.assign(strategyForm, initialStrategyForm);
  strategyFormDialogVisible.value = true;
}

const handleViewDataStrategy = async (strategy: DataStrategy) => {
  try {
    if (strategy.id) {
      const response = await getDataStrategyDetail(strategy.id) as unknown as StandardApiResponse<DataStrategy>
      console.log('获取数据策略详情API返回:', response);
      
      // 处理标准格式响应：{ code: 200, message: 'success', data: {...} }
      let detailData: DataStrategy;
      if (response && response.code === 200) {
        // 新的标准格式响应
        detailData = response.data;
      } else {
        // 旧格式或直接返回数据（此时response直接是DataStrategy类型）
        detailData = response as unknown as DataStrategy;
      }
      
      currentDataStrategy.value = detailData
      dataStrategyDialogVisible.value = true
    }
  } catch (error) {
    handleApiError(error, '获取数据策略详情失败');
  }
}

const handleEditDataStrategy = (strategy: DataStrategy | null) => {
  if (!strategy) return;
  isEditingStrategy.value = true;
  Object.assign(strategyForm, {
      id: strategy.id,
      policyName: strategy.policyName,
      policyCode: strategy.policyCode,
      conditionType: strategy.conditionType,
      priority: strategy.priority,
      effectTables: strategy.effectTables,
      policyDescription: strategy.policyDescription,
      conditionExpression: strategy.conditionExpression
  });
  strategyFormDialogVisible.value = true;
  dataStrategyDialogVisible.value = false; // Close detail modal
}

const handleDeleteDataStrategy = async (strategy: DataStrategy | null) => {
  if (!strategy || !strategy.id) return;
  try {
    await ElMessageBox.confirm(
      `确定要删除数据策略 "${strategy.policyName}" 吗？`, 
      '确认删除',
      { type: 'warning' }
    );
    const response = await deleteDataStrategy(strategy.id) as unknown as StandardApiResponse<boolean>;
    if (handleApiResponse(response, '数据策略删除成功', '数据策略删除失败')) {
      dataStrategyDialogVisible.value = false;
      loadDataStrategies();
    }
  } catch (e) { 
    if (e !== 'cancel') {
      handleApiError(e, '删除数据策略失败');
    } 
  }
}

const resetStrategyForm = () => {
  strategyFormRef.value?.resetFields();
  Object.assign(strategyForm, initialStrategyForm);
}

const submitStrategyForm = async () => {
  if (!strategyFormRef.value) return;
  await strategyFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let response;
        let successMessage;
        if (isEditingStrategy.value && strategyForm.id) {
          response = await updateDataStrategy(strategyForm) as unknown as StandardApiResponse<boolean>;
          successMessage = '数据策略更新成功';
        } else {
          response = await createDataStrategy(strategyForm) as unknown as StandardApiResponse<DataStrategy>;
          successMessage = '数据策略创建成功';
        }
        
        if (handleApiResponse(response, successMessage, '保存数据策略失败')) {
          strategyFormDialogVisible.value = false;
          loadDataStrategies();
        }
      } catch (error) {
        handleApiError(error, '保存数据策略失败');
      }
    }
  });
}

// --- 操作权限相关处理方法 ---
const handleAddOperationPermission = () => {
  isEditingOpPerm.value = false;
  opPermForm.id = undefined; // 清除ID，确保是新增
  Object.assign(opPermForm, initialOpPermForm);
  opPermFormDialogVisible.value = true;
}

const handleViewOperationPermission = async (permission: OperationPermission) => {
  try {
    if (permission.id) {
      const response = await getOperationPermissionDetail(permission.id) as unknown as StandardApiResponse<OperationPermission>
      console.log('获取操作权限详情API返回:', response);
      
      // 处理标准格式响应：{ code: 200, message: 'success', data: {...} }
      let detailData: OperationPermission;
      if (response && response.code === 200) {
        // 新的标准格式响应
        detailData = response.data;
      } else {
        // 旧格式或直接返回数据（此时response直接是OperationPermission类型）
        detailData = response as unknown as OperationPermission;
      }
      
      currentOpPerm.value = detailData
      opPermDialogVisible.value = true
    }
  } catch (error) {
    handleApiError(error, '获取操作权限详情失败');
  }
}

const handleEditOperationPermission = (permission: OperationPermission | null) => {
  if (!permission) return;
  isEditingOpPerm.value = true;
  Object.assign(opPermForm, {
    id: permission.id,
    permsName: permission.permsName,
    permsCode: permission.permsCode,
    permType: permission.permType,
    apiMethod: permission.apiMethod,
    apiPath: permission.apiPath,
    permsDescription: permission.permsDescription
  });
  opPermFormDialogVisible.value = true;
  opPermDialogVisible.value = false; // Close detail modal
}

const handleDeleteOperationPermission = async (permission: OperationPermission | null) => {
  if (!permission || !permission.id) return;
   try {
    await ElMessageBox.confirm(
      `确定要删除操作权限 "${permission.permsName}" 吗？`, 
      '确认删除',
      { type: 'warning' }
    );
    const response = await deleteOperationPermission(permission.id) as unknown as StandardApiResponse<boolean>;
    if (handleApiResponse(response, '操作权限删除成功', '操作权限删除失败')) {
      opPermDialogVisible.value = false;
      loadOperationPermissions(); // 重新加载列表
    }
  } catch (e) { 
    if (e !== 'cancel') {
      handleApiError(e, '删除操作权限失败');
    } 
  }
}

const resetOpPermForm = () => {
  opPermFormRef.value?.resetFields();
  Object.assign(opPermForm, initialOpPermForm);
}

const submitOpPermForm = async () => {
  if (!opPermFormRef.value) return;
  await opPermFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let response;
        let successMessage;
        if (isEditingOpPerm.value && opPermForm.id) {
          response = await updateOperationPermission(opPermForm as OperationPermission) as unknown as StandardApiResponse<boolean>;
          successMessage = '操作权限更新成功';
        } else {
          response = await createOperationPermission(opPermForm as OperationPermission) as unknown as StandardApiResponse<OperationPermission>;
          successMessage = '操作权限创建成功';
        }
        
        if (handleApiResponse(response, successMessage, '保存操作权限失败')) {
          opPermFormDialogVisible.value = false;
          loadOperationPermissions(); // 重新加载列表
        }
      } catch (error) {
        handleApiError(error, '保存操作权限失败');
      }
    }
  });
} 
</script>

<style scoped>
.basic-permission-container {
  padding: 20px;
}

.box-card {
  height: calc(100vh - 160px); /* Adjust based on header/padding */
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-weight: bold;
}

.config-content {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止内容溢出 */
}

.search-bar {
  display: flex;
  margin-bottom: 15px;
  flex-shrink: 0; /* 防止搜索栏被压缩 */
}

.filter-input {
  width: 100%;
}

.el-table {
  flex-grow: 1;
  margin-bottom: 15px; /* 为分页组件留出空间 */
}

.pagination {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
  flex-shrink: 0; /* 确保分页组件不被压缩 */
}

.text-muted {
  font-size: 12px;
  color: #909399;
}

.box-card :deep(.el-card__body) {
  padding: 15px;
  flex-grow: 1;
  display: flex; 
  flex-direction: column;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-family: monospace;
}
code {
  font-family: monospace;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 