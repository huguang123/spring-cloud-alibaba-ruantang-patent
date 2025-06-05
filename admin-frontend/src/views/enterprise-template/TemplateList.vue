<template>
  <div class="enterprise-template-list-container p-4">
    <PageHeader title="企业模板列表管理" subtitle="企业模板，不仅可以提供给不同行业使用，而且包括对应权限，企业基础数据等" />

    <!-- Filter Section -->
    <el-card shadow="never" class="mb-4 filter-card">
      <el-form :inline="true" :model="filters" @submit.prevent="handleSearch">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="模板名称">
              <el-input v-model="filters.templateName" placeholder="搜索模板名称..." clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="模板编码">
              <el-input v-model="filters.templateCode" placeholder="搜索模板编码..." clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="5">
            <el-form-item label="模板类型">
              <el-select v-model="filters.templateType" placeholder="选择模板类型" clearable style="width: 100%;">
                <el-option
                  v-for="(name, value) in TemplateTypeMap"
                  :key="value"
                  :label="name"
                  :value="Number(value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="5">
            <el-form-item label="行业类型">
              <el-select v-model="filters.industryType" placeholder="选择行业类型" clearable style="width: 100%;">
                <el-option
                  v-for="(name, value) in IndustryTypeMap"
                  :key="value"
                  :label="name"
                  :value="Number(value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="5">
            <el-form-item label="系统模板">
              <el-select v-model="filters.isSystem" placeholder="是否系统模板" clearable style="width: 100%;">
                <el-option label="是" :value="1" />
                <el-option label="否" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="8" class="text-right mt-0 md:mt-0 lg:mt-0">
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">筛选</el-button>
              <el-button :icon="Refresh" @click="handleReset">重置</el-button>
              <el-button type="primary" :icon="Plus" @click="handleAddNewTemplate" class="ml-auto">新增模板</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- Template List Table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="templateList" v-loading="loading" style="width: 100%">
        <el-table-column label="模板名称" min-width="220">
          <template #default="{ row }">
            <div class="font-medium">{{ row.templateName }}</div>
            <div class="text-sm text-gray-500">{{ row.templateDesc || '暂无描述' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="templateCode" label="模板编码" min-width="150" />
        <el-table-column label="模板类型" min-width="120">
          <template #default="{ row }">
            <el-tag>{{ TemplateTypeMap[row.templateType as TemplateType] || '未知类型' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="行业类型" min-width="100">
          <template #default="{ row }">
            <el-tag>{{ IndustryTypeMap[row.industryType as IndustryType] || '未知类型' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否系统模板" min-width="120">
          <template #default="{ row }">
            <el-tag :type="row.isSystem ? 'success' : 'info'">{{ row.isSystem ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="150">
          <template #default="{ row }">{{ formatTimestamp(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" size="small" @click="handleEditTemplate(row)">编辑</el-button>
            <el-button text type="primary" size="small" @click="handleBindRoles(row)">绑定角色</el-button>
            <el-popconfirm title="确定删除该模板吗?" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button text type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="mt-4 justify-end"
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <TemplateFormDialog 
      v-model:visible="templateFormVisible"
      :is-editing="isEditing"
      :initial-data="currentTemplate"
      @submit="onTemplateFormSubmit"
    />

    <BindRolesDialog
      v-model:visible="bindRolesVisible"
      :template-data="currentTemplateForRoles"
      @submit="onBindRolesSubmit"
    />

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElCard, ElButton, ElTable, ElTableColumn, ElPagination, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElMessage, ElMessageBox, ElRow, ElCol, ElTag, ElPopconfirm } from 'element-plus';
import { Plus, Edit, Delete, Search, Refresh, Setting } from '@element-plus/icons-vue';
import type { FormInstance, FormRules } from 'element-plus';
import PageHeader from '@/components/PageHeader.vue';
import TemplateFormDialog from './components/TemplateFormDialog.vue';
import BindRolesDialog from './components/BindRolesDialog.vue';
import { formatTimestamp } from '@/utils/format';
import { 
  TenantTemplateInfo, 
  GetTenantTemplateListParams, 
  CreateTenantTemplatePayload, 
  UpdateTenantTemplatePayload,
  getTenantTemplateList, 
  createTenantTemplate, 
  updateTenantTemplate, 
  deleteTenantTemplate,
  IndustryTypeMap,
  TemplateTypeMap,
  TemplateType,
  IndustryType
} from '@/api/enterpriseTemplate';
import { handleApiError, handleApiResponse } from '@/utils/errorHandler';

// 过滤条件，确保与API接口参数一致
const filters = reactive<Partial<GetTenantTemplateListParams>>({
  templateName: '',
  templateCode: '',
  templateType: undefined as TemplateType | undefined,
  industryType: undefined as IndustryType | undefined,
  isSystem: undefined as 0 | 1 | undefined,
});

const templateList = ref<TenantTemplateInfo[]>([]);
const loading = ref(false);
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0,
});

// 获取模板列表数据
const fetchData = async () => {
  loading.value = true;
  try {
    // 构建请求参数，按接口文档要求设置
    const params: GetTenantTemplateListParams = {
      pageNum: pagination.page,
      pageSize: pagination.pageSize,
      templateName: filters.templateName,
      templateCode: filters.templateCode,
      templateType: filters.templateType,
      industryType: filters.industryType,
      isSystem: filters.isSystem,
    };
    
    // 调用API获取数据
    const response = await getTenantTemplateList(params);
    
    // 处理响应结果
    if (response.records) {
      // 直接获取数据
      templateList.value = response.records;
      pagination.total = response.total;
    } else if (response.data && response.data.records) {
      // 数据嵌套在data字段中
      templateList.value = response.data.records;
      pagination.total = response.data.total || 0;
    } else {
      // 处理可能的空响应
      templateList.value = [];
      pagination.total = 0;
    }
  } catch (error) {
    handleApiError(error, '获取模板列表失败');
    templateList.value = [];
    pagination.total = 0;
  } finally {
    loading.value = false;
  }
};

onMounted(fetchData);

const handleSearch = () => {
  pagination.page = 1;
  fetchData();
};

const handleReset = () => {
  // 重置所有过滤条件
  filters.templateName = '';
  filters.templateCode = '';
  filters.templateType = undefined;
  filters.industryType = undefined;
  filters.isSystem = undefined;
  handleSearch();
};

const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  fetchData();
};

const handleCurrentChange = (current: number) => {
  pagination.page = current;
  fetchData();
};

// Dialogs visibility and data
const templateFormVisible = ref(false);
const isEditing = ref(false);
const currentTemplate = ref<TenantTemplateInfo | null>(null);

const bindRolesVisible = ref(false);
const currentTemplateForRoles = ref<TenantTemplateInfo | null>(null);

const handleAddNewTemplate = () => {
  isEditing.value = false;
  currentTemplate.value = null;
  templateFormVisible.value = true;
};

const handleEditTemplate = (row: TenantTemplateInfo) => {
  isEditing.value = true;
  currentTemplate.value = { ...row };
  templateFormVisible.value = true;
};

const onTemplateFormSubmit = async (formData: CreateTenantTemplatePayload | UpdateTenantTemplatePayload) => {
  try {
    if (isEditing.value && (formData as UpdateTenantTemplatePayload).id) {
      await updateTenantTemplate(formData as UpdateTenantTemplatePayload);
      ElMessage.success('模板更新成功');
    } else {
      await createTenantTemplate(formData as CreateTenantTemplatePayload);
      ElMessage.success('模板创建成功');
    }
    templateFormVisible.value = false;
    fetchData();
  } catch (error) {
    handleApiError(error, '保存模板失败');
  }
};

const handleDelete = async (id: string | number) => {
  try {
    await deleteTenantTemplate(id);
    ElMessage.success('模板删除成功');
    fetchData();
  } catch (error) {
    handleApiError(error, '删除模板失败');
  }
};

const handleBindRoles = (row: TenantTemplateInfo) => {
  currentTemplateForRoles.value = row;
  bindRolesVisible.value = true;
};

const onBindRolesSubmit = () => {
  ElMessage.success('角色绑定已更新');
  bindRolesVisible.value = false;
  
  // 绑定角色后，刷新当前模板数据
  if (currentTemplateForRoles.value) {
    // 可以选择刷新模板列表或只刷新当前模板详情
    fetchData();
  }
};

</script>

<style scoped>
.filter-card .el-form-item {
  margin-bottom: 10px; 
}
.table-card {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}
:deep(.table-card .el-card__body) {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
:deep(.table-card .el-table) {
  flex-grow: 1;
  overflow-y: auto;
}
</style> 