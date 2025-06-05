<template>
  <el-dialog
    :title="isEditing ? '编辑节点' : '新增节点'"
    v-model="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <el-form 
      ref="formRef" 
      :model="form" 
      :rules="rules" 
      label-width="120px" 
      @submit.prevent
    >
      <el-form-item label="所属模块">
        <el-tag>{{ selectedModuleName }}</el-tag>
      </el-form-item>
      
      <el-form-item label="节点名称" prop="nodeName">
        <el-input v-model="form.nodeName" placeholder="请输入节点名称" />
      </el-form-item>
      
      <el-form-item label="节点类型" prop="nodeType">
        <el-select v-model="form.nodeType" placeholder="请选择节点类型" style="width: 100%;">
          <el-option :label="'菜单项'" :value="1" />
          <el-option :label="'操作按钮'" :value="2" />
          <el-option :label="'数据字段'" :value="3" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="权限类型" prop="permType">
        <el-radio-group v-model="form.permType" @change="handlePermTypeChange">
          <el-radio :label="0">操作权限</el-radio>
          <el-radio :label="1">数据权限</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <!-- 操作权限相关选择 -->
      <el-form-item 
        v-if="form.permType === 0" 
        label="选择操作权限" 
        prop="permId"
      >
        <el-select 
          v-model="form.permId" 
          filterable 
          remote
          reserve-keyword
          :remote-method="handleSearchOpPerm"
          :loading="opPermsLoading"
          placeholder="请输入权限名称搜索" 
          style="width: 100%;"
          clearable
          @visible-change="handleOpPermSelectOpen"
        >
          <el-option 
            v-for="perm in operationPermOptions" 
            :key="String(perm.id)" 
            :label="perm.permsName + ' (' + perm.permsCode + ')'" 
            :value="String(perm.id)" 
          />
          <template #empty>
            <div class="el-select-dropdown__empty">
              <span v-if="opPermsLoading">正在加载数据...</span>
              <span v-else>没有找到匹配的操作权限</span>
            </div>
          </template>
        </el-select>
      </el-form-item>
      
      <!-- 数据权限相关选择 -->
      <el-form-item 
        v-if="form.permType === 1" 
        label="选择数据策略" 
        prop="dataPolicyId"
      >
        <el-select 
          v-model="form.dataPolicyId" 
          filterable
          remote
          reserve-keyword
          :remote-method="handleSearchDataPolicy"
          :loading="dataPolicyLoading"
          placeholder="请输入策略名称搜索" 
          style="width: 100%;"
          clearable
          @visible-change="handleDataPolicySelectOpen"
        >
          <el-option 
            v-for="policy in dataPolicyOptions" 
            :key="String(policy.id)" 
            :label="policy.policyName + ' (' + policy.policyCode + ')'" 
            :value="String(policy.id)" 
          />
          <template #empty>
            <div class="el-select-dropdown__empty">
              <span v-if="dataPolicyLoading">正在加载数据...</span>
              <span v-else>没有找到匹配的数据策略</span>
            </div>
          </template>
        </el-select>
      </el-form-item>
      
      <!-- 数据字段类型时的数据范围选择 -->
      <el-form-item 
        v-if="form.nodeType === 3" 
        label="数据范围" 
        prop="dataScope"
      >
        <el-select v-model="form.dataScope" placeholder="请选择数据范围" style="width: 100%;">
          <el-option :label="'查看权限'" :value="1" />
          <el-option :label="'编辑权限'" :value="2" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="是否基础权限">
        <el-switch v-model="form.isBasic" active-text="是" inactive-text="否" />
      </el-form-item>
      
      <el-form-item label="排序号">
        <el-input-number v-model="form.nodeSort" :min="1" :max="999" placeholder="排序号（数字越小排序越靠前）" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed, onMounted } from 'vue';
import type { FormInstance } from 'element-plus';
import { ElDialog, ElForm, ElFormItem, ElInput, ElInputNumber, ElRadioGroup, ElRadio, ElButton, ElSelect, ElOption, ElTag, ElSwitch, ElMessage } from 'element-plus';
import type { PropType } from 'vue';
import { PermissionNode } from '@/api/templatePermission';
import { 
  OperationPermission, 
  DataStrategy, 
  getOperationPermissionsPage, 
  getDataStrategiesPage,
  PermQueryParams,
  DataStrategyQueryParams,
  getOperationPermissionDetail,
  getDataStrategyDetail
} from '@/api/permission';

// 扩展节点接口，添加排序字段
interface ExtendedNode extends Omit<PermissionNode, 'sort'> {
  nodeSort?: number;
}

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  },
  isEditing: {
    type: Boolean,
    default: false
  },
  initialData: {
    type: Object as PropType<Partial<ExtendedNode> | null>,
    default: null
  },
  selectedModuleName: {
    type: String,
    default: ''
  },
  allOperationPermissions: {
    type: Array as PropType<OperationPermission[]>,
    default: () => []
  },
  allDataPolicies: {
    type: Array as PropType<DataStrategy[]>,
    default: () => []
  }
});

const emit = defineEmits(['update:visible', 'submit']);

const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
});

const formRef = ref<FormInstance>();
const submitting = ref(false);
const opPermsLoading = ref(false);
const dataPolicyLoading = ref(false);

// 本地存储操作权限选项，确保响应式
const operationPermOptions = ref<OperationPermission[]>([]);
// 本地存储数据策略选项，确保响应式
const dataPolicyOptions = ref<DataStrategy[]>([]);

// 表单数据
const form = reactive({
  id: undefined as number | string | undefined,
  nodeName: '',
  moduleId: undefined as number | string | undefined,
  nodeType: 1, // 1:菜单项, 2:操作按钮, 3:数据字段
  permType: 0, // 0:操作权限, 1:数据权限
  permId: undefined as number | string | undefined, // 操作权限ID
  dataPolicyId: undefined as number | string | undefined, // 数据策略ID
  dataScope: 1, // 1:查看权限, 2:编辑权限
  isBasic: false, // 是否基础权限
  nodeSort: 0
});

// 当权限类型改变时，重置对应的选择值
const handlePermTypeChange = () => {
  if (form.permType === 0) {
    form.dataPolicyId = undefined;
    // 加载操作权限数据
    loadOperationPermissions();
  } else {
    form.permId = undefined;
    // 加载数据策略数据
    loadDataPolicies();
  }
};

// 操作权限下拉框打开时
const handleOpPermSelectOpen = (visible: boolean) => {
  if (visible) {
    loadOperationPermissions();
  }
};

// 数据策略下拉框打开时
const handleDataPolicySelectOpen = (visible: boolean) => {
  if (visible) {
    loadDataPolicies();
  }
};

// 搜索操作权限
const handleSearchOpPerm = (query: string) => {
  if (query === '') {
    loadOperationPermissions();
  } else {
    loadOperationPermissions(query);
  }
};

// 搜索数据策略
const handleSearchDataPolicy = (query: string) => {
  if (query === '') {
    loadDataPolicies();
  } else {
    loadDataPolicies(query);
  }
};

// 加载操作权限数据
const loadOperationPermissions = async (keyword?: string) => {
  opPermsLoading.value = true;
  
  try {
    console.log('加载操作权限列表，关键字:', keyword);
    const params: PermQueryParams = {
      pageNum: 1,
      pageSize: 100
    };
    
    if (keyword && keyword.trim() !== '') {
      params.permsName = keyword.trim();
    }
    
    const response = await getOperationPermissionsPage(params);
    console.log('操作权限加载响应:', response);
    
    let perms: OperationPermission[] = [];
    
    // 使用any类型断言避免类型错误
    const anyResponse = response as any;
    
    // 处理不同的响应结构
    if (anyResponse && anyResponse.data && Array.isArray(anyResponse.data.records)) {
      perms = anyResponse.data.records;
    } else if (Array.isArray(anyResponse)) {
      perms = anyResponse;
    } else if (anyResponse && Array.isArray(anyResponse.records)) {
      perms = anyResponse.records;
    } else if (anyResponse && typeof anyResponse === 'object') {
      // 尝试从其他属性中获取数据
      for (const key in anyResponse) {
        if (Array.isArray(anyResponse[key])) {
          perms = anyResponse[key];
          break;
        }
      }
    }
    
    // 确保ID是字符串类型
    perms = perms.map(perm => {
      const perm2 = { ...perm };
      if (perm2.id && typeof perm2.id !== 'string') {
        // 使用类型断言避免TypeScript错误
        (perm2 as any).id = String(perm2.id);
      }
      return perm2;
    });
    
    operationPermOptions.value = perms;
    console.log('操作权限选项:', operationPermOptions.value);
    
    if (form.permId && !operationPermOptions.value.some(p => String(p.id) === String(form.permId))) {
      // 如果当前选中的ID不在加载的数据中，需要单独获取详情
      try {
        // 使用Number转换可能会导致大整数精度丢失，但API可能接受字符串ID
        const permId = typeof form.permId === 'string' ? form.permId : String(form.permId);
        const detailResponse = await getOperationPermissionDetail(Number(permId));
        console.log('操作权限详情响应:', detailResponse);
        
        if (detailResponse) {
          // 使用any类型断言
          const anyDetailResponse = detailResponse as any;
          const permData = anyDetailResponse.data || anyDetailResponse;
          if (permData && permData.id) {
            const permWithStringId = { 
              ...permData, 
              id: String(permData.id) 
            };
            operationPermOptions.value = [
              permWithStringId, 
              ...operationPermOptions.value.filter(p => String(p.id) !== String(form.permId))
            ];
          }
        }
      } catch (error) {
        console.error('获取操作权限详情失败:', error);
      }
    }
    
    if (perms.length === 0 && !keyword) {
      ElMessage.info('未加载到操作权限数据，请检查接口');
    }
  } catch (error) {
    console.error('加载操作权限失败:', error);
    ElMessage.error('加载操作权限失败: ' + (error instanceof Error ? error.message : String(error)));
  } finally {
    opPermsLoading.value = false;
  }
};

// 加载数据策略数据
const loadDataPolicies = async (keyword?: string) => {
  dataPolicyLoading.value = true;
  
  try {
    console.log('加载数据策略列表，关键字:', keyword);
    const params: DataStrategyQueryParams = {
      pageNum: 1,
      pageSize: 100
    };
    
    if (keyword && keyword.trim() !== '') {
      params.policyName = keyword.trim();
    }
    
    const response = await getDataStrategiesPage(params);
    console.log('数据策略加载响应:', response);
    
    let policies: DataStrategy[] = [];
    
    // 使用any类型断言避免类型错误
    const anyResponse = response as any;
    
    // 处理不同的响应结构
    if (anyResponse && anyResponse.data && Array.isArray(anyResponse.data.records)) {
      policies = anyResponse.data.records;
    } else if (Array.isArray(anyResponse)) {
      policies = anyResponse;
    } else if (anyResponse && Array.isArray(anyResponse.records)) {
      policies = anyResponse.records;
    } else if (anyResponse && typeof anyResponse === 'object') {
      // 尝试从其他属性中获取数据
      for (const key in anyResponse) {
        if (Array.isArray(anyResponse[key])) {
          policies = anyResponse[key];
          break;
        }
      }
    }
    
    // 确保ID是字符串类型
    policies = policies.map(policy => {
      const policy2 = { ...policy };
      if (policy2.id && typeof policy2.id !== 'string') {
        // 使用类型断言避免TypeScript错误
        (policy2 as any).id = String(policy2.id);
      }
      return policy2;
    });
    
    dataPolicyOptions.value = policies;
    console.log('数据策略选项:', dataPolicyOptions.value);
    
    if (form.dataPolicyId && !dataPolicyOptions.value.some(p => String(p.id) === String(form.dataPolicyId))) {
      // 如果当前选中的ID不在加载的数据中，需要单独获取详情
      try {
        // 使用Number转换可能会导致大整数精度丢失，但API可能接受字符串ID
        const policyId = typeof form.dataPolicyId === 'string' ? form.dataPolicyId : String(form.dataPolicyId);
        const detailResponse = await getDataStrategyDetail(Number(policyId));
        console.log('数据策略详情响应:', detailResponse);
        
        if (detailResponse) {
          // 使用any类型断言
          const anyDetailResponse = detailResponse as any;
          const policyData = anyDetailResponse.data || anyDetailResponse;
          if (policyData && policyData.id) {
            const policyWithStringId = { 
              ...policyData, 
              id: String(policyData.id) 
            };
            dataPolicyOptions.value = [
              policyWithStringId, 
              ...dataPolicyOptions.value.filter(p => String(p.id) !== String(form.dataPolicyId))
            ];
          }
        }
      } catch (error) {
        console.error('获取数据策略详情失败:', error);
      }
    }
    
    if (policies.length === 0 && !keyword) {
      ElMessage.info('未加载到数据策略数据，请检查接口');
    }
  } catch (error) {
    console.error('加载数据策略失败:', error);
    ElMessage.error('加载数据策略失败: ' + (error instanceof Error ? error.message : String(error)));
  } finally {
    dataPolicyLoading.value = false;
  }
};

// 动态计算表单验证规则
const rules = computed(() => {
  const baseRules = {
    nodeName: [
      { required: true, message: '请输入节点名称', trigger: 'blur' },
      { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    nodeType: [
      { required: true, message: '请选择节点类型', trigger: 'change' }
    ],
    permType: [
      { required: true, message: '请选择权限类型', trigger: 'change' }
    ]
  };
  
  // 根据权限类型添加不同的验证规则
  if (form.permType === 0) {
    return {
      ...baseRules,
      permId: [
        { required: true, message: '请选择操作权限', trigger: 'change' }
      ]
    };
  } else {
    return {
      ...baseRules,
      dataPolicyId: [
        { required: true, message: '请选择数据策略', trigger: 'change' }
      ]
    };
  }
});

// 监听初始数据变化，重置表单
watch(() => props.initialData, (newData) => {
  if (newData) {
    // 复制初始数据到表单
    form.id = newData.id;
    form.nodeName = newData.nodeName || '';
    form.moduleId = newData.moduleId;
    form.nodeType = newData.nodeType || 1;
    form.permType = newData.permType || 0;
    form.permId = newData.permId;
    form.dataPolicyId = newData.dataPolicyId;
    form.dataScope = newData.dataScope || 1;
    form.isBasic = newData.isBasic === 1;
    // 使用nodeSort替代sort
    form.nodeSort = props.initialData?.nodeSort || 0;
    
    // 根据权限类型预加载数据
    if (form.permType === 0) {
      loadOperationPermissions();
    } else {
      loadDataPolicies();
    }
  } else {
    // 重置表单
    form.id = undefined;
    form.nodeName = '';
    form.moduleId = undefined;
    form.nodeType = 1;
    form.permType = 0;
    form.permId = undefined;
    form.dataPolicyId = undefined;
    form.dataScope = 1;
    form.isBasic = false;
    form.nodeSort = 0;
    
    // 默认加载操作权限
    loadOperationPermissions();
  }
}, { immediate: true });

// 当对话框显示状态改变时
watch(() => props.visible, (visible) => {
  if (visible) {
    // 对话框打开时，根据当前权限类型加载对应数据
    if (form.permType === 0) {
      loadOperationPermissions();
    } else {
      loadDataPolicies();
    }
  }
});

// 当组件挂载时，初始化权限数据
onMounted(() => {
  // 初始化权限数据
  if (form.permType === 0) {
    loadOperationPermissions();
  } else {
    loadDataPolicies();
  }
});

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true;
      try {
        // 根据接口文档构建符合要求的请求数据
        const formData: any = {
          nodeName: form.nodeName,
          nodeType: form.nodeType,
          permType: form.permType,
          isBasic: form.isBasic ? 1 : 0,
          sort: form.nodeSort || 0  // 将nodeSort作为sort字段传递
        };

        // 创建操作时需要moduleId
        if (!props.isEditing) {
          formData.moduleId = form.moduleId;
        }
        // 编辑操作时需要id字段
        else if (form.id) {
          formData.id = form.id;
        }

        // 根据权限类型添加相应的ID字段
        if (form.permType === 0 && form.permId) {
          // 操作权限
          formData.permId = form.permId;
          // 数据策略ID置为undefined，确保不传
          formData.dataPolicyId = undefined;
        } else if (form.permType === 1 && form.dataPolicyId) {
          // 数据权限
          formData.dataPolicyId = form.dataPolicyId;
          // 操作权限ID置为undefined，确保不传
          formData.permId = undefined;
        }

        // 数据字段类型时的数据范围
        if (form.nodeType === 3 && form.dataScope) {
          formData.dataScope = form.dataScope;
        }

        console.log(`准备${props.isEditing ? '更新' : '创建'}节点:`, formData);
        
        // 提交给父组件
        emit('submit', formData);
      } finally {
        submitting.value = false;
      }
    }
  });
};
</script>

<style scoped>
.el-form-item {
  margin-bottom: 20px;
}

.el-tag {
  font-size: 14px;
  padding: 5px 10px;
}
</style> 