<template>
  <div class="role-list-container p-4">
    <PageHeader title="角色列表" subtitle="管理系统中的所有角色信息" />

    <!-- Filter Section -->
    <el-card shadow="never" class="mb-4 filter-card">
      <el-form :inline="true" :model="filters" @submit.prevent="handleSearch">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="关键字">
              <el-input v-model="filters.keyword" placeholder="搜索角色名称或编码..." clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="5">
            <el-form-item label="角色类型">
              <el-select v-model="filters.roleType" placeholder="所有类型" clearable style="width: 100%;">
                <el-option label="平台角色" :value="RoleType.PLATFORM" />
                <el-option label="企业角色" :value="RoleType.ENTERPRISE" />
                <el-option label="代理所角色" :value="RoleType.AGENCY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="8" :lg="8" class="text-right mt-0 md:mt-0 lg:mt-0">
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">筛选</el-button>
              <el-button :icon="Refresh" @click="handleReset">重置</el-button>
              <el-button type="primary" :icon="Plus" @click="handleAddNewRole" class="ml-auto">新增角色</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- Role List Table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="roleList" v-loading="loading" style="width: 100%">
        <el-table-column prop="rolesName" label="角色名称" min-width="180" />
        <el-table-column prop="rolesCode" label="角色编码" min-width="150" />
        <el-table-column label="角色类型" min-width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleTypeTag(row.rolesType as RoleType)">{{ RoleTypeMap[row.rolesType as RoleType] || row.rolesTypeName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rolesDescription" label="角色描述" min-width="250" show-overflow-tooltip />
        <el-table-column label="创建时间" min-width="150">
          <template #default="{ row }">{{ formatTimestamp(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" size="small" @click="handleAssignPermission(row)">分配权限</el-button>
            <el-button text type="primary" size="small" @click="handleEditRole(row)">编辑</el-button>
            <el-popconfirm title="确定删除该角色吗?" @confirm="handleDeleteRole(row.id)">
              <template #reference>
                <el-button text type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="mt-4 justify-end"
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- Add/Edit Role Dialog -->
    <RoleFormDialog 
      v-model:visible="roleFormVisible"
      :is-editing="isEditing"
      :initial-data="currentRole"
      @submit="onRoleFormSubmit"
    />

    <!-- Assign Permission Dialog -->
    <AssignPermissionsDialog
      v-model:visible="assignPermissionDialogVisible"
      :role-data="currentRoleForPermission"
      @permissions-saved="handlePermissionsSaved"
    />

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElCard, ElButton, ElTable, ElTableColumn, ElPagination, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElMessage, ElMessageBox, ElRow, ElCol, ElTag, ElPopconfirm } from 'element-plus';
import { Plus, Edit, Delete, Search, Refresh } from '@element-plus/icons-vue';
import type { FormInstance, FormRules } from 'element-plus';
import PageHeader from '@/components/PageHeader.vue';
import RoleFormDialog from './components/RoleFormDialog.vue';
import AssignPermissionsDialog from './components/AssignPermissionsDialog.vue';
import { formatTimestamp } from '@/utils/format';
import type { RoleInfo, GetRoleListParams, CreateRolePayload, UpdateRolePayload } from '@/api/role';
import { getRoleList, createRole, updateRole, deleteRole, RoleType, RoleTypeMap } from '@/api/role';
import { handleApiError, handleApiResponse } from '@/utils/errorHandler';

const filters = reactive<Partial<GetRoleListParams>>({
  keyword: '',
  roleType: undefined as unknown as RoleType | undefined,
});

const roleList = ref<RoleInfo[]>([]);
const loading = ref(false);
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
});

const roleFormVisible = ref(false);
const isEditing = ref(false);
const currentRole = ref<RoleInfo | null>(null);

const assignPermissionDialogVisible = ref(false);
const currentRoleForPermission = ref<RoleInfo | null>(null);

const fetchData = async () => {
  loading.value = true;
  try {
    const params: GetRoleListParams = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      keyword: filters.keyword,
      roleType: filters.roleType === '' ? undefined : filters.roleType,
    };
    const response = await getRoleList(params);
    
    if (response) {
      if (response.records) {
        roleList.value = response.records;
      } else if (Array.isArray(response)) {
        roleList.value = response;
      } else if (response.data && response.data.records) {
        roleList.value = response.data.records;
      }
      
      pagination.total = response.total || (response.data && response.data.total) || 0;
    } else {
      roleList.value = [];
      pagination.total = 0;
    }
  } catch (error) {
    handleApiError(error, '获取角色列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(fetchData);

const handleSearch = () => {
  pagination.pageNum = 1;
  fetchData();
};

const handleReset = () => {
  filters.keyword = '';
  filters.roleType = undefined;
  handleSearch();
};

const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  fetchData();
};

const handleCurrentChange = (current: number) => {
  pagination.pageNum = current;
  fetchData();
};

const getRoleTypeTag = (type: RoleType) => {
  switch (type) {
    case RoleType.PLATFORM:
      return 'primary';
    case RoleType.ENTERPRISE:
      return 'success';
    case RoleType.AGENCY:
      return 'warning';
    default:
      return 'info';
  }
};

const handleAddNewRole = () => {
  isEditing.value = false;
  currentRole.value = null;
  roleFormVisible.value = true;
};

const handleEditRole = (row: RoleInfo) => {
  isEditing.value = true;
  currentRole.value = { ...row };
  roleFormVisible.value = true;
};

const onRoleFormSubmit = async (formData: CreateRolePayload | UpdateRolePayload) => {
  try {
    if (isEditing.value && (formData as UpdateRolePayload).id) {
      await updateRole(formData as UpdateRolePayload);
      ElMessage.success('角色更新成功');
    } else {
      await createRole(formData as CreateRolePayload);
      ElMessage.success('角色创建成功');
    }
    roleFormVisible.value = false;
    fetchData();
  } catch (error) {
    handleApiError(error, '保存角色失败');
  }
};

const handleDeleteRole = async (id: number) => {
  try {
    await deleteRole(id);
    ElMessage.success('角色删除成功');
    fetchData();
  } catch (error) {
    handleApiError(error, '删除角色失败');
  }
};

const handleAssignPermission = (role: RoleInfo) => {
  currentRoleForPermission.value = role;
  assignPermissionDialogVisible.value = true;
};

const handlePermissionsSaved = (roleId: string | number) => {
  console.log(`Permissions saved for role ${roleId} via dialog.`);
  fetchData();
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 