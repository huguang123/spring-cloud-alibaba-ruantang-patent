<template>
  <div class="org-container">
    <PageHeader 
      title="组织架构管理" 
      subtitle="管理公司部门与人员的层级结构"
    >
      <template #actions>
        <el-button type="primary" @click="handleAddOrg">
          <el-icon><Plus /></el-icon> 添加组织
        </el-button>
      </template>
    </PageHeader>

    <el-row :gutter="20" class="org-content">
      <!-- 左侧组织树 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="5" class="org-col">
        <el-card class="tree-card">
          <template #header>
            <div class="card-header">
              <span>组织架构树</span>
              <el-input
                v-model="filterText"
                placeholder="输入关键字过滤"
                clearable
                prefix-icon="Search"
                class="filter-input"
              />
            </div>
          </template>
          <div class="tree-wrapper">
            <el-tree
              ref="orgTreeRef"
              :data="organizationTree"
              :props="{ label: 'orgName', children: 'children' }"
              node-key="id"
              :filter-node-method="filterNode"
              highlight-current
              @node-click="handleNodeClick"
            >
              <template #default="{ node, data }">
                <div class="custom-tree-node">
                  <span class="tree-node-label">
                    <el-icon v-if="!data.parentId"><OfficeBuilding /></el-icon>
                    <el-icon v-else><Folder /></el-icon>
                    {{ node.label }}
                  </span>
                  <span class="tree-node-count">({{ data.memberCount || data.staffCount || 0 }}人)</span>
                  <div class="tree-node-actions">
                    <el-button 
                      type="primary" 
                      link 
                      @click.stop="handleEditOrg(data)"
                    >
                      <el-icon><Edit /></el-icon>
                    </el-button>
                    <el-button 
                      type="danger" 
                      link 
                      v-if="data.children && data.children.length === 0"
                      @click.stop="handleDeleteOrg(data)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                </div>
              </template>
            </el-tree>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧详情 -->
      <el-col :xs="24" :sm="24" :md="16" :lg="19" class="org-col">
        <div v-if="!currentOrg.id" class="empty-state">
          <el-empty description="请选择一个组织查看详情"></el-empty>
        </div>
        
        <template v-else>
          <!-- 组织详情卡片 -->
          <el-card class="detail-card">
            <template #header>
              <div class="card-header">
                <span>组织详情</span>
                <div>
                  <el-button type="primary" @click="handleEditOrg(currentOrg)">
                    <el-icon><Edit /></el-icon> 编辑
                  </el-button>
                </div>
              </div>
            </template>
            
            <el-descriptions :column="2" border>
              <el-descriptions-item label="组织名称" :span="1">{{ currentOrg.orgName }}</el-descriptions-item>
              <el-descriptions-item label="组织编码" :span="1">{{ currentOrg.orgCode }}</el-descriptions-item>
              <el-descriptions-item label="创建时间" :span="1">{{ formatTimestamp(currentOrg.createTime) }}</el-descriptions-item>
              <el-descriptions-item label="最后更新" :span="1">{{ formatTimestamp(currentOrg.updateTime) }}</el-descriptions-item>
              <el-descriptions-item label="联系电话" :span="1">{{ currentOrg.orgPhone || '-' }}</el-descriptions-item>
              <el-descriptions-item label="联系邮箱" :span="1">{{ currentOrg.orgMail || '-' }}</el-descriptions-item>
              <el-descriptions-item label="状态" :span="2">
                <el-tag :type="currentOrg.state === 1 ? 'success' : 'danger'">
                  {{ currentOrg.state === 1 ? '启用' : '禁用' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="描述" :span="2">
                {{ currentOrg.description || '-' }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>

          <!-- 组织成员列表 -->
          <el-card class="members-card">
            <template #header>
              <div class="card-header">
                <span>部门成员</span>
                <el-button type="primary" @click="handleAddMember">
                  <el-icon><Plus /></el-icon> 添加成员
                </el-button>
              </div>
            </template>
            
            <div class="members-content">
              <el-table 
                :data="membersList" 
                v-loading="membersLoading"
                border
                style="width: 100%"
                max-height="500"
              >
                <el-table-column label="姓名" prop="realName" min-width="100" />
                <el-table-column label="账号" prop="username" min-width="120" />
                <el-table-column label="角色" min-width="140">
                  <template #default="{ row }">
                    <el-tag v-for="role in row.roles" :key="role.id" class="role-tag">
                      {{ role.rolesName }}
                    </el-tag>
                    <span v-if="!row.roles || row.roles.length === 0">-</span>
                  </template>
                </el-table-column>
                <el-table-column label="邮箱" prop="email" min-width="180" />
                <el-table-column label="电话" prop="phone" min-width="120" />
                <el-table-column label="入职日期" min-width="120">
                  <template #default="{ row }">
                    {{ row.entryDate ? formatTimestamp(row.entryDate, 'YYYY-MM-DD') : '-' }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template #default="{ row }">
                    <el-button type="primary" link @click="handleEditMember(row)">
                      <el-icon><Edit /></el-icon>
                    </el-button>
                    <el-popconfirm
                      title="确定要移除此成员吗？"
                      @confirm="handleRemoveMember(row)"
                    >
                      <template #reference>
                        <el-button type="danger" link>
                          <el-icon><Delete /></el-icon>
                        </el-button>
                      </template>
                    </el-popconfirm>
                  </template>
                </el-table-column>
              </el-table>
              
              <div class="pagination-container" v-if="totalMembers > 0">
                <el-pagination
                  v-model:current-page="currentPage"
                  v-model:page-size="pageSize"
                  :page-sizes="[10, 20, 50]"
                  background
                  layout="total, sizes, prev, pager, next"
                  :total="totalMembers"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                />
              </div>
              
              <el-empty v-if="membersList.length === 0 && !membersLoading" description="暂无成员" />
            </div>
          </el-card>
        </template>
      </el-col>
    </el-row>

    <!-- 组织表单对话框 -->
    <el-dialog
      v-model="orgDialogVisible"
      :title="isEdit ? '编辑组织' : '新增组织'"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="orgFormRef"
        :model="orgForm"
        :rules="orgRules"
        label-width="100px"
      >
        <el-form-item label="组织名称" prop="orgName">
          <el-input v-model="orgForm.orgName" placeholder="请输入组织名称" />
        </el-form-item>

        <el-form-item label="组织编码" prop="orgCode">
          <el-input v-model="orgForm.orgCode" placeholder="请输入组织编码" :disabled="isEdit" />
        </el-form-item>

        <el-form-item label="上级组织" prop="parentId">
          <el-cascader
            v-model="orgForm.parentId"
            :options="organizationTree"
            :props="{
              checkStrictly: true,
              label: 'orgName',
              value: 'id',
              emitPath: false,
              expandTrigger: 'hover'
            }"
            placeholder="请选择上级组织"
            clearable
            :disabled="orgForm.id === 1" 
            :show-all-levels="false"
          >
            <template #default="{ node, data }">
              <span>{{ data.orgName }}</span>
            </template>
          </el-cascader>
        </el-form-item>

        <el-form-item label="联系电话" prop="orgPhone">
          <el-input v-model="orgForm.orgPhone" placeholder="请输入联系电话" />
        </el-form-item>

        <el-form-item label="联系邮箱" prop="orgMail">
          <el-input v-model="orgForm.orgMail" placeholder="请输入联系邮箱" />
        </el-form-item>

        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="orgForm.state">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="orgForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入组织描述信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="orgDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOrgForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加成员对话框 -->
    <el-dialog
      v-model="memberDialogVisible"
      :title="isEditMember ? '编辑成员' : '添加成员'"
      width="650px"
      destroy-on-close
    >
      <div v-if="!isEditMember" class="search-user-section">
        <div class="search-header">
          <h4>搜索已注册用户</h4>
        </div>
        <el-input
          v-model="userSearchKeyword"
          placeholder="输入姓名、电话或邮箱搜索用户..."
          clearable
          class="search-input"
          @keyup.enter="searchUser"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button @click="searchUser">
              搜索
            </el-button>
          </template>
        </el-input>
        
        <el-table
          v-if="userSearchResults.length > 0"
          :data="userSearchResults"
          height="200px"
          @row-click="handleSelectUser"
          highlight-current-row
          class="search-results-table"
          border
        >
          <el-table-column label="姓名" prop="realName" min-width="100" />
          <el-table-column label="账号" prop="username" min-width="120" />
          <el-table-column label="邮箱" prop="email" min-width="180" />
          <el-table-column label="电话" prop="phone" min-width="120" />
          <el-table-column label="操作" width="80" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click.stop="handleSelectUser(row)">
                选择
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <el-empty v-else-if="userSearchKeyword && !userSearchLoading" description="未找到匹配的用户" />
      </div>

      <el-divider v-if="!isEditMember && selectedUser.id">用户信息</el-divider>

      <el-form
        ref="memberFormRef"
        :model="memberForm"
        :rules="memberRules"
        label-width="100px"
        v-loading="userSearchLoading"
      >
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="memberForm.realName" placeholder="请输入姓名" disabled />
        </el-form-item>

        <el-form-item label="账号" prop="username">
          <el-input v-model="memberForm.username" placeholder="请输入账号" disabled />
        </el-form-item>

        <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="memberForm.roleIds"
            multiple
            filterable
            placeholder="请选择角色"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="role in rolesList"
              :key="role.id"
              :label="role.rolesName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="memberForm.email" placeholder="请输入邮箱" disabled />
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model="memberForm.phone" placeholder="请输入电话" disabled />
        </el-form-item>

        <el-form-item label="入职日期" prop="entryDate">
          <el-date-picker
            v-model="memberForm.entryDate"
            type="date"
            placeholder="选择入职日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="memberDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="submitMemberForm" 
            :disabled="!isEditMember && !selectedUser.id"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch, nextTick } from 'vue';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus';
import { Plus, Edit, Delete, Search, OfficeBuilding, Folder } from '@element-plus/icons-vue';
import { formatTimestamp } from '@/utils/format';
import PageHeader from '@/components/PageHeader.vue';
import { ApiResponse } from '@/utils/request';
import {
  Organization,
  OrganizationUser,
  Role,
  getOrganizationTree,
  getOrganizationDetail,
  createOrganization,
  updateOrganization,
  deleteOrganization,
  getOrganizationUsers,
  addOrganizationUser,
  removeOrganizationUser,
  updateOrganizationUserRoles,
  searchUsers,
  getRoleList,
  getCurrentTenantId,
  // 模拟数据
  mockOrganizationTree,
  mockFinanceUsers,
  mockRoleList,
  mockSearchUsers,
} from '@/api/organization';
import { handleApiError, handleApiResponse } from '@/utils/errorHandler';

// 组织树数据和筛选
const organizationTree = ref<Organization[]>([]);
const filterText = ref('');
const orgTreeRef = ref();
const treeLoading = ref(false);

// 当前选中的组织
const currentOrg = ref<Organization>({} as Organization);

// 组织表单
const orgDialogVisible = ref(false);
const isEdit = ref(false);
const orgFormRef = ref<FormInstance>();

// 修改Organization接口的类型定义，允许tenantId为string
interface OrgFormData extends Partial<Omit<Organization, 'tenantId'>> {
  tenantId?: string | number;
}

const orgForm = reactive<OrgFormData>({
  id: undefined,
  orgName: '',
  orgCode: '',
  parentId: undefined,
  tenantId: '',
  orgPhone: '',
  orgMail: '',
  state: 1,
  description: ''
});

// 组织表单校验规则
const orgRules: FormRules = {
  orgName: [
    { required: true, message: '请输入组织名称', trigger: 'blur' },
    { max: 50, message: '长度不能超过50个字符', trigger: 'blur' }
  ],
  orgCode: [
    { required: true, message: '请输入组织编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' },
    { max: 30, message: '长度不能超过30个字符', trigger: 'blur' }
  ],
  parentId: [
    { required: false, message: '请选择上级组织', trigger: 'change' }
  ],
  orgPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  orgMail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
};

// 成员列表数据
const membersList = ref<OrganizationUser[]>([]);
const membersLoading = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const totalMembers = ref(0);

// 成员对话框
const memberDialogVisible = ref(false);
const isEditMember = ref(false);
const memberFormRef = ref<FormInstance>();
const selectedUser = ref<OrganizationUser>({} as OrganizationUser);
const memberForm = reactive({
  id: undefined as number | undefined,
  userId: undefined as number | undefined,
  realName: '',
  username: '',
  email: '',
  phone: '',
  roleIds: [] as number[], // 角色ID列表
  entryDate: null as Date | null,
  orgId: undefined as number | undefined
});

// 用户搜索
const userSearchKeyword = ref('');
const userSearchLoading = ref(false);
const userSearchResults = ref<OrganizationUser[]>([]);

// 角色列表
const rolesList = ref<Role[]>([]);

// 成员表单校验规则
const memberRules: FormRules = {
  roleIds: [
    { type: 'array', required: true, message: '请至少选择一个角色', trigger: 'change' }
  ],
  entryDate: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ]
};

// 组织树筛选方法
const filterNode = (value: string, data: Organization) => {
  if (!value) return true;
  return data.orgName.includes(value) || data.orgCode.includes(value);
};

// 监听筛选文本变化
watch(filterText, (val) => {
  orgTreeRef.value?.filter(val);
});

// 加载组织树数据
const loadOrganizationTree = async () => {
  treeLoading.value = true;
  try {
    // 调用真实API获取组织树
    const res = await getOrganizationTree() as unknown as ApiResponse<Organization[]>;
    console.log('组织树API返回:', res);
    
    // 正确处理API返回结构：{ code: 200, message: 'success', data: [...] }
    if (res && res.code === 200 && Array.isArray(res.data)) {
      organizationTree.value = res.data;
      console.log('获取组织树成功:', organizationTree.value);
    } else {
      console.warn('获取组织树返回格式异常，使用模拟数据:', res);
      // 使用模拟数据作为备选
      organizationTree.value = mockOrganizationTree;
    }
  } catch (error) {
    handleApiError(error, '获取组织架构数据失败');
    // 使用模拟数据作为备选
    organizationTree.value = mockOrganizationTree;
  } finally {
    treeLoading.value = false;
  }
};

// 加载角色列表
const loadRoleList = async () => {
  try {
    // 调用真实API获取角色列表
    const res = await getRoleList();
    if (res && res.data) {
      rolesList.value = res.data.map((role: any) => ({
        id: role.roleId,
        rolesCode: role.rolesCode,
        rolesName: role.rolesName,
        rolesType: role.rolesType
      }));
      console.log('获取角色列表成功:', rolesList.value);
    } else {
      console.warn('获取角色列表失败，使用模拟数据:', res);
      // 使用模拟数据作为备选
      rolesList.value = mockRoleList;
    }
  } catch (error) {
    handleApiError(error, '获取角色列表失败');
    // 使用模拟数据作为备选
    rolesList.value = mockRoleList;
  }
};

// 处理组织节点点击
const handleNodeClick = (data: Organization) => {
  if (data.id === currentOrg.value.id) return;
  currentOrg.value = { ...data };
  currentPage.value = 1;
  // 清空当前成员列表，避免显示上一个组织的成员
  membersList.value = [];
  // 加载组织详情和成员
  loadOrgDetail();
  loadOrgMembers();
};

// 加载组织详情
const loadOrgDetail = async () => {
  if (!currentOrg.value.id) return;
  
  try {
    console.log('开始加载组织详情，组织ID:', currentOrg.value.id);
    // 调用真实API获取组织详情
    const res = await getOrganizationDetail(currentOrg.value.id) as unknown as ApiResponse<Organization>;
    console.log('获取组织详情API返回:', res);
    
    // 正确处理API响应结构：{ code: 200, message: 'success', data: {...} }
    if (res && res.code === 200 && res.data) {
      // 更新当前组织的详细信息
      currentOrg.value = {
        ...currentOrg.value,
        ...res.data,
        // 确保保留树结构中的children信息
        children: currentOrg.value.children
      };
      console.log('获取组织详情成功:', currentOrg.value);
    } else {
      console.warn('获取组织详情失败，保持当前数据:', res);
      // 如果API失败，保持当前从树结构中获取的基本信息
    }
  } catch (error) {
    console.error('获取组织详情失败', error);
    handleApiError(error, '获取组织详情失败');
    // 如果API失败，保持当前从树结构中获取的基本信息
  }
};

// 加载组织成员
const loadOrgMembers = async () => {
  if (!currentOrg.value.id) return;
  
  membersLoading.value = true;
  try {
    // 调用真实API获取组织成员
    const res = await getOrganizationUsers(currentOrg.value.id, {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }) as unknown as ApiResponse<{records: any[], total: number}>;
    
    console.log('获取组织成员API返回:', res);
    
    // 正确处理API响应结构：{ code: 200, message: 'success', data: { records: [], total: 0 } }
    if (res && res.code === 200 && res.data && res.data.records) {
      // 处理API返回的数据
      membersList.value = res.data.records.map((item: any) => {
        console.log('处理组织成员数据:', item);
        
        // 确保角色数据正确处理
        let roles = [];
        if (item.roles && Array.isArray(item.roles)) {
          // 如果角色是对象数组，直接使用
          if (item.roles.length > 0 && typeof item.roles[0] === 'object') {
            roles = item.roles.map((role: any) => ({
              id: role.roleId || role.id,
              rolesCode: role.rolesCode || '',
              rolesName: role.rolesName || '',
              rolesType: role.rolesType || 1
            }));
          } 
          // 如果角色是字符串数组，需要与roleIds对应起来
          else if (item.roles.length > 0 && typeof item.roles[0] === 'string') {
            if (item.roleIds && Array.isArray(item.roleIds)) {
              // 如果有roleIds，将角色名与ID对应
              roles = item.roles.map((roleName: string, index: number) => {
                const roleId = item.roleIds[index] || 0;
                return {
                  id: roleId,
                  rolesCode: '',
                  rolesName: roleName,
                  rolesType: 1
                };
              });
            } else {
              // 没有roleIds时，创建临时ID
              roles = item.roles.map((roleName: string, index: number) => ({
                id: index + 1,
                rolesCode: '',
                rolesName: roleName,
                rolesType: 1
              }));
            }
          }
        } else if (item.roles && typeof item.roles === 'string') {
          // 如果roles是字符串，尝试解析JSON
          try {
            const rolesData = JSON.parse(item.roles);
            if (Array.isArray(rolesData)) {
              if (rolesData.length > 0 && typeof rolesData[0] === 'object') {
                roles = rolesData.map((role: any) => ({
                  id: role.roleId || role.id,
                  rolesCode: role.rolesCode || '',
                  rolesName: role.rolesName || '',
                  rolesType: role.rolesType || 1
                }));
              } else if (rolesData.length > 0 && typeof rolesData[0] === 'string') {
                if (item.roleIds && Array.isArray(item.roleIds)) {
                  // 有roleIds，将角色名与ID对应
                  roles = rolesData.map((roleName: string, index: number) => {
                    const roleId = item.roleIds[index] || 0;
                    return {
                      id: roleId,
                      rolesCode: '',
                      rolesName: roleName,
                      rolesType: 1
                    };
                  });
                } else {
                  roles = rolesData.map((roleName: string, index: number) => ({
                    id: index + 1,
                    rolesCode: '',
                    rolesName: roleName,
                    rolesType: 1
                  }));
                }
              }
            }
          } catch (e) {
            console.error('解析角色JSON失败:', e);
          }
        }
        
        console.log('处理后的角色数据:', roles);
        
        return {
          id: item.id,
          username: item.loginName || '',
          realName: item.userName || '',
          email: item.userMail || '',
          phone: item.userPhone || '',
          roleIds: item.roleIds || roles.map((role: any) => role.id),
          roles: roles,
          entryDate: item.joinDate || Date.now(),
          orgId: currentOrg.value.id
        };
      });
      totalMembers.value = res.data.total || 0;
      console.log('获取组织成员成功:', membersList.value);
    } else {
      console.warn('获取组织成员失败，使用模拟数据:', res);
      // 使用模拟数据（根据组织ID选择不同的模拟数据）
      let mockData: OrganizationUser[] = [];
      switch (currentOrg.value.id) {
        case 5: // 财务部
          mockData = mockFinanceUsers;
          break;
        case 2: // 研发中心
          mockData = mockFinanceUsers.slice(0, 4).map(user => ({
            ...user,
            orgId: 2,
            realName: user.realName.replace('员工', '研发'),
            roles: [{ id: 4, rolesCode: 'developer', rolesName: '开发工程师', rolesType: 1 }]
          }));
          break;
        case 3: // 市场部
          mockData = mockFinanceUsers.slice(0, 3).map(user => ({
            ...user,
            orgId: 3,
            realName: user.realName.replace('员工', '市场'),
            roles: [{ id: 3, rolesCode: 'marketing', rolesName: '市场专员', rolesType: 1 }]
          }));
          break;
        case 4: // 人力资源部
          mockData = mockFinanceUsers.slice(0, 2).map(user => ({
            ...user,
            orgId: 4,
            realName: user.realName.replace('员工', 'HR'),
            roles: [{ id: 2, rolesCode: 'hr', rolesName: '人力资源', rolesType: 1 }]
          }));
          break;
        default:
          // 根节点或其他组织，显示少量成员
          if (currentOrg.value.id === 1) { // 总公司
            mockData = mockFinanceUsers.slice(0, 2).map(user => ({
              ...user,
              orgId: 1,
              realName: user.realName.replace('员工', '总监'),
              roles: [{ id: 1, rolesCode: 'director', rolesName: '总监', rolesType: 0 }]
            }));
          } else {
            mockData = [];
          }
      }
      
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      membersList.value = mockData.slice(start, end);
      totalMembers.value = mockData.length;
    }
  } catch (error) {
    console.error('获取组织成员失败', error);
    handleApiError(error, '获取组织成员失败');
    membersList.value = [];
    totalMembers.value = 0;
  } finally {
    membersLoading.value = false;
  }
};

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  loadOrgMembers();
};

// 处理页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  loadOrgMembers();
};

// 添加组织
const handleAddOrg = () => {
  isEdit.value = false;
  orgForm.id = undefined;
  orgForm.orgName = '';
  orgForm.orgCode = '';
  orgForm.parentId = undefined;
  orgForm.orgPhone = '';
  orgForm.orgMail = '';
  orgForm.state = 1;
  orgForm.description = '';
  
  // 设置当前租户ID为字符串，避免精度丢失
  const tenantId = getCurrentTenantId();
  console.log('添加组织设置租户ID:', tenantId);
  orgForm.tenantId = tenantId;
  
  nextTick(() => {
    if (orgFormRef.value) {
      orgFormRef.value.clearValidate();
    }
  });
  
  orgDialogVisible.value = true;
};

// 编辑组织
const handleEditOrg = (org: Organization) => {
  isEdit.value = true;
  orgForm.id = org.id;
  orgForm.orgName = org.orgName;
  orgForm.orgCode = org.orgCode;
  orgForm.parentId = org.parentId;
  orgForm.orgPhone = org.orgPhone || '';
  orgForm.orgMail = org.orgMail || '';
  orgForm.state = org.state;
  orgForm.description = org.description || '';
  
  // 确保设置租户ID为字符串
  const tenantId = org.tenantId ? String(org.tenantId) : getCurrentTenantId();
  console.log('编辑组织设置租户ID:', tenantId);
  orgForm.tenantId = tenantId;
  
  nextTick(() => {
    if (orgFormRef.value) {
      orgFormRef.value.clearValidate();
    }
  });
  
  orgDialogVisible.value = true;
};

// 删除组织
const handleDeleteOrg = (org: Organization) => {
  if (org.children && org.children.length > 0) {
    ElMessage.warning('该组织下有子组织，不能删除');
    return;
  }
  
  ElMessageBox.confirm(
    `确定要删除组织 "${org.orgName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 调用真实API删除组织
      const res = await deleteOrganization(org.id);
      if (res && res.data === true) {
        ElMessage.success('删除组织成功');
        // 重新加载组织树
        await loadOrganizationTree();
        // 如果当前选中的是被删除的组织，则清空选中状态
        if (currentOrg.value.id === org.id) {
          currentOrg.value = {} as Organization;
        }
      } else {
        console.error('删除组织失败:', res);
        handleApiError(res, '删除组织失败');
      }
    } catch (error) {
      console.error('删除组织失败', error);
      handleApiError(error, '删除组织失败');
    }
  }).catch(() => {
    // 取消删除，不做处理
  });
};

// 提交组织表单
const submitOrgForm = async () => {
  if (!orgFormRef.value) return;
  
  await orgFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 创建表单数据的副本，确保ID字段为字符串
        const formData: Record<string, any> = { ...orgForm };
        
        // 确保ID字段为字符串类型
        if (formData.id) formData.id = String(formData.id);
        if (formData.parentId) formData.parentId = String(formData.parentId);
        if (formData.tenantId) formData.tenantId = String(formData.tenantId);
        
        console.log('提交组织表单数据:', JSON.stringify(formData));
        
        if (isEdit.value && formData.id) {
          // 调用真实API更新组织
          const res = await updateOrganization(formData) as unknown as ApiResponse<boolean>;
          console.log('更新组织API返回:', res);
          
          // 正确处理更新API响应：{ code: 200, message: 'success', data: true }
          if (res && res.code === 200) {
            ElMessage.success('更新组织成功');
            orgDialogVisible.value = false;
            // 重新加载组织树
            await loadOrganizationTree();
            
            // 如果当前选中的是被更新的组织，刷新详情
            if (currentOrg.value.id === formData.id) {
              // 使用统一的loadOrgDetail方法来刷新组织详情
              await loadOrgDetail();
            }
          } else {
            console.error('更新组织失败:', res);
            handleApiError(res, '更新组织失败');
          }
        } else {
          // 调用真实API创建组织
          const res = await createOrganization(formData) as unknown as ApiResponse<Organization>;
          console.log('创建组织API返回:', res);
          
          // 正确处理创建API响应：{ code: 200, message: 'success', data: {...} }
          if (res && res.code === 200) {
            ElMessage.success('创建组织成功');
            orgDialogVisible.value = false;
            // 重新加载组织树
            await loadOrganizationTree();
          } else {
            console.error('创建组织失败:', res);
            handleApiError(res, '创建组织失败');
          }
        }
      } catch (error) {
        console.error('保存组织失败', error);
        handleApiError(error, '保存组织失败');
      }
    }
  });
};

// 添加成员
const handleAddMember = () => {
  if (!currentOrg.value.id) {
    ElMessage.warning('请先选择一个组织');
    return;
  }
  
  isEditMember.value = false;
  selectedUser.value = {} as OrganizationUser;
  memberForm.id = undefined;
  memberForm.userId = undefined;
  memberForm.realName = '';
  memberForm.username = '';
  memberForm.email = '';
  memberForm.phone = '';
  memberForm.roleIds = [];
  memberForm.entryDate = new Date();
  memberForm.orgId = currentOrg.value.id;
  
  userSearchKeyword.value = '';
  userSearchResults.value = [];
  
  nextTick(() => {
    if (memberFormRef.value) {
      memberFormRef.value.clearValidate();
    }
  });
  
  memberDialogVisible.value = true;
};

// 编辑成员
const handleEditMember = (member: OrganizationUser) => {
  isEditMember.value = true;
  selectedUser.value = { ...member };
  
  // 确保角色信息正确处理
  console.log('编辑成员，角色信息:', member.roles);
  
  memberForm.id = member.id;
  memberForm.userId = member.id;
  memberForm.realName = member.realName;
  memberForm.username = member.username;
  memberForm.email = member.email || '';
  memberForm.phone = member.phone || '';
  // 确保从角色中提取正确的角色ID
  memberForm.roleIds = member.roles ? member.roles.map(role => role.id) : [];
  memberForm.entryDate = member.entryDate ? new Date(member.entryDate) : new Date();
  memberForm.orgId = currentOrg.value.id;
  
  nextTick(() => {
    if (memberFormRef.value) {
      memberFormRef.value.clearValidate();
    }
  });
  
  memberDialogVisible.value = true;
};

// 移除成员
const handleRemoveMember = (member: OrganizationUser) => {
  if (!currentOrg.value.id) return;
  
  // 先以普通方式确认
  ElMessageBox.confirm(
    `确定要从该组织中移除成员 "${member.realName}" 吗？是否同时解绑用户与租户关系？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    // 再次确认是否同时解绑租户关系
    return ElMessageBox.confirm(
      '是否同时解绑用户与租户关系？',
      '提示',
      {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'info'
      }
    ).then(() => true).catch(() => false);
  }).then(async (unbindTenant) => {
    try {
      // 调用真实API移除组织成员
      const res = await removeOrganizationUser(currentOrg.value.id, member.id, !!unbindTenant);
      if (res && res.data === true) {
        ElMessage.success('移除成员成功');
        // 重新加载成员列表
        loadOrgMembers();
      } else {
        console.error('移除成员失败:', res);
        handleApiError(res, '移除成员失败');
      }
    } catch (error) {
      console.error('移除成员失败', error);
      handleApiError(error, '移除成员失败');
    }
  }).catch(() => {
    // 取消删除，不做处理
  });
};

// 搜索用户
const searchUser = async () => {
  if (!userSearchKeyword.value) {
    ElMessage.warning('请输入搜索关键字');
    return;
  }
  
  userSearchLoading.value = true;
  try {
    // 调用真实API进行用户搜索，不再传入tenantId参数
    const res = await searchUsers({
      keyword: userSearchKeyword.value,
      pageNum: 1,
      pageSize: 10
      // 移除tenantId参数，允许搜索所有已注册用户
    });
    
    if (res && res.data && res.data.records) {
      // 处理API返回的数据
      userSearchResults.value = res.data.records.map((user: any) => ({
        id: user.id,
        username: user.loginName || '',
        realName: user.userName || '',
        email: user.userMail || '',
        phone: user.userPhone || '',
        roles: [],
        orgId: user.orgId || 0
      }));
      console.log('搜索用户成功:', userSearchResults.value);
    } else {
      console.warn('搜索用户失败，使用模拟数据:', res);
      // 使用模拟数据
      const results = await mockSearchUsers(userSearchKeyword.value);
      userSearchResults.value = results;
    }
  } catch (error) {
    console.error('搜索用户失败', error);
    handleApiError(error, '搜索用户失败');
    userSearchResults.value = [];
  } finally {
    userSearchLoading.value = false;
  }
};

// 选择用户
const handleSelectUser = (user: OrganizationUser) => {
  selectedUser.value = { ...user };
  
  memberForm.userId = user.id;
  memberForm.realName = user.realName || user.username;
  memberForm.username = user.username;
  memberForm.email = user.email || '';
  memberForm.phone = user.phone || '';
  memberForm.roleIds = []; // 默认清空角色，需要用户选择
  memberForm.entryDate = new Date(); // 默认为当前日期
  
  console.log('已选择用户:', user);
};

// 提交成员表单
const submitMemberForm = async () => {
  if (!memberFormRef.value || !currentOrg.value.id) return;
  
  await memberFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEditMember.value) {
          // 更新成员角色
          const res = await updateOrganizationUserRoles(
            currentOrg.value.id,
            memberForm.userId!,
            memberForm.roleIds as number[]
          );
          
          if (res && res.data === true) {
            ElMessage.success('更新成员成功');
            memberDialogVisible.value = false;
            // 重新加载成员列表
            loadOrgMembers();
          } else {
            console.error('更新成员失败:', res);
            handleApiError(res, '更新成员失败');
          }
        } else {
          // 添加成员
          const res = await addOrganizationUser(currentOrg.value.id, {
            userId: memberForm.userId!,
            roleIds: memberForm.roleIds as number[]
          });
          
          if (res && res.data === true) {
            ElMessage.success('添加成员成功');
            memberDialogVisible.value = false;
            // 重新加载成员列表
            loadOrgMembers();
          } else {
            console.error('添加成员失败:', res);
            handleApiError(res, '添加成员失败');
          }
        }
      } catch (error) {
        console.error('保存成员失败', error);
        handleApiError(error, '保存成员失败');
      }
    }
  });
};

// 页面加载时执行
onMounted(() => {
  const tenantId = getCurrentTenantId();
  if (!tenantId) {
    console.warn('未获取到租户ID，尝试从用户信息中获取');
    // 如果租户ID不存在，可能是因为没有登录或者getUserInfo没有被调用
    ElMessage.warning('未获取到租户信息，可能影响组织数据的加载');
  } else {
    console.log('当前租户ID:', tenantId);
  }

  loadOrganizationTree();
  loadRoleList();
  
  // 页面加载后自动选中第一个组织
  setTimeout(() => {
    if (organizationTree.value.length > 0) {
      const firstOrg = organizationTree.value[0];
      currentOrg.value = { ...firstOrg };
      // 高亮第一个节点
      nextTick(() => {
        if (orgTreeRef.value) {
          orgTreeRef.value.setCurrentKey(firstOrg.id);
        }
      });
      // 加载组织详情和成员
      loadOrgDetail();
      loadOrgMembers();
    }
  }, 500); // 给一点延迟，确保树已经渲染
});
</script>

<style scoped>
.org-container {
  padding: 20px;
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
}

.org-content {
  margin-top: 20px;
  flex: 1;
  overflow: visible;
}

.org-container .org-col {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.org-container .tree-card,
.org-container .detail-card,
.org-container .members-card {
  height: auto;
  margin-bottom: 15px;
}

.org-container .tree-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.org-container .tree-wrapper {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  max-height: calc(100vh - 200px);
}

.org-container .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.org-container .filter-input {
  width: 200px;
}

.org-container .custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.org-container .tree-node-label {
  display: flex;
  align-items: center;
  gap: 5px;
}

.org-container .tree-node-count {
  font-size: 12px;
  color: #909399;
}

.org-container .tree-node-actions {
  display: none;
}

.org-container .custom-tree-node:hover .tree-node-actions {
  display: flex;
  gap: 5px;
}

.org-container .detail-card {
  margin-bottom: 15px;
}

.org-container .members-card {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

.org-container .members-content {
  min-height: 300px;
  overflow: auto;
}

.org-container .pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.org-container .empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.org-container .search-user-section {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  background-color: #f9f9f9;
}

.org-container .search-header {
  margin-bottom: 10px;
}

.org-container .search-header h4 {
  margin: 0;
  font-size: 16px;
  color: #606266;
}

.org-container .search-input {
  margin-bottom: 10px;
  width: 100%;
}

.org-container .search-results-table {
  margin-bottom: 15px;
}

.org-container .role-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}

.org-container :deep(.el-descriptions__label) {
  width: 100px;
}

.org-container .tree-card :deep(.el-card__body) {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: visible;
}

.org-container .detail-card :deep(.el-card__body),
.org-container .members-card :deep(.el-card__body) {
  padding: 15px;
  overflow: visible;
}

.org-container .members-card :deep(.el-table) {
  width: 100%;
  margin-bottom: 15px;
}

.org-container :deep(.el-table__body) {
  width: 100% !important;
}

@media (max-width: 992px) {
  .org-container .filter-input {
    width: 160px;
  }
  
  .org-container .tree-wrapper {
    max-height: 400px;
  }
  
  /* 大屏时为左右布局，小屏时为上下布局 */
  .org-container .org-col {
    width: 100% !important;
  }
}

@media (max-width: 768px) {
  .org-container .filter-input {
    width: 120px;
  }
  
  .org-container .tree-wrapper {
    max-height: 300px;
  }
  
  /* 调整表格在小屏幕上的显示 */
  .org-container :deep(.el-table) {
    font-size: 12px;
  }
  
  .org-container :deep(.el-button.el-button--small) {
    padding: 6px 10px;
  }
  
  .org-container .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .org-container .card-header > * {
    width: 100%;
  }
}

@media (max-width: 480px) {
  /* 超小屏幕适配 */
  .org-container .page-header-actions {
    margin-top: 10px;
    width: 100%;
  }
  
  .org-container .page-header-actions :deep(.el-button) {
    width: 100%;
  }
  
  .org-container .tree-wrapper {
    max-height: 250px;
  }
  
  .org-container .search-user-section {
    overflow-x: auto;
  }
}
</style> 