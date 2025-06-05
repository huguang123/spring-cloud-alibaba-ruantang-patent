<template>
  <el-dialog
    :model-value="visible"
    title="绑定角色"
    width="750px"
    @close="handleClose"
    :close-on-click-modal="false"
    class="bind-roles-dialog"
  >
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>
    <div v-else>
      <p class="mb-2">正在为模板 "<strong>{{ templateData?.templateName }}</strong>" 配置角色。</p>
      <el-alert
        title="关于角色绑定"
        type="info"
        description="请选择需要绑定到此模板的角色。绑定后，使用此模板创建的企业将默认拥有这些角色的权限。对于每个已选角色，您可以指定其权限是否随平台对该角色的权限定义变更而自动更新。"
        show-icon
        class="mb-4"
        :closable="false"
      />

      <el-row :gutter="20">
        <el-col :span="14">
          <h4 class="mb-2 text-sm font-medium">选择角色:</h4>
          <el-transfer
            v-model="selectedRoleIdsForTransfer"
            :data="allRolesForTransfer"
            :titles="['可选角色', '已选角色']"
            :props="{
              key: 'transferKey',
              label: 'displayName'
            }"
            filterable
            filter-placeholder="搜索角色名称"
            target-order="push"
            class="role-transfer"
            @change="handleTransferChange"
          >
            <template #default="{ option }">
              <span>{{ option.displayName }} <el-tag size="small" type="info" class="ml-1">{{ getRoleTypeName(option.rolesType) }}</el-tag></span>
            </template>
          </el-transfer>
        </el-col>
        <el-col :span="10">
          <h4 class="mb-2 text-sm font-medium">配置继承权限:</h4>
          <div v-if="detailedSelectedRoles.length === 0" class="text-gray-500 text-sm pt-2">
            请先从左侧选择角色。
          </div>
          <div v-else class="selected-roles-config thin-scrollbar" style="max-height: 350px; overflow-y: auto;">
            <el-card shadow="never" v-for="role in detailedSelectedRoles" :key="role.transferKey" class="mb-2 role-config-item">
              <div class="flex justify-between items-center">
                <span class="text-sm">{{ role.displayName }}</span>
                <el-tooltip content="是否继承权限变更" placement="top">
                  <el-switch
                    v-model="role.isInherit"
                    size="small"
                    inline-prompt
                    active-text="继承"
                    inactive-text="不继承"
                    :active-value="1"
                    :inactive-value="0"
                  />
                </el-tooltip>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose" :disabled="saving">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="saving" :disabled="loading">确定保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch, PropType, nextTick } from 'vue';
import { ElDialog, ElButton, ElTransfer, ElMessage, ElSkeleton, ElAlert, ElTag, ElRow, ElCol, ElSwitch, ElCard, ElTooltip } from 'element-plus';
import { TenantTemplateInfo, bindTemplateRoles, RoleBinding, getTemplateRoles } from '@/api/enterpriseTemplate';
import { getRolesByType, RoleInfo, RoleType, RoleTypeMap } from '@/api/role';

// 扩展角色信息接口，添加后端返回的可能字段
interface ExtendedRoleInfo extends RoleInfo {
  roleId?: string | number; // /{id}/roles接口返回的roleId
  role?: {
    roleName?: string;
    roleId?: string | number;
    [key: string]: any;
  };
}

// 扩展DetailedRoleBinding接口，添加用于Transfer组件的辅助字段
interface DetailedRoleBinding extends ExtendedRoleInfo {
  isInherit: 0 | 1; // 0: 不继承, 1: 继承
  transferKey: string; // 用于transfer组件的唯一key
  displayName: string; // 用于显示的角色名称
  actualRoleId: string | number; // 实际的roleId，用于提交
}

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  templateData: {
    type: Object as PropType<TenantTemplateInfo | null>,
    default: null,
  },
});

const emit = defineEmits(['update:visible', 'submit']);

const loading = ref(false);
const saving = ref(false);

// 所有可选角色列表（transfer组件数据源）
const allRolesForTransfer = ref<any[]>([]);
// transfer组件选中的角色ID列表（使用transferKey）
const selectedRoleIdsForTransfer = ref<any[]>([]);
// 已选角色的详细绑定信息（包含继承标志）
const detailedSelectedRoles = ref<any[]>([]);

// 获取角色类型名称
const getRoleTypeName = (type: RoleType | number | undefined): string => {
  if (type === undefined) return '未知类型';
  return RoleTypeMap[type as RoleType] || '自定义类型';
};

// 监听对话框显示状态
watch(() => props.visible, async (newVal) => {
  if (newVal && props.templateData) {
    loading.value = true;
    // 重置数据
    selectedRoleIdsForTransfer.value = [];
    detailedSelectedRoles.value = [];
    allRolesForTransfer.value = [];
    
    try {
      // 根据模板类型获取对应的角色列表
      let roleType: RoleType;
      switch (props.templateData.templateType) {
        case 1: // 平台租户模板
          roleType = RoleType.PLATFORM;
          break;
        case 2: // 企业租户模板
          roleType = RoleType.ENTERPRISE;
          break;
        case 3: // 代理所租户模板
          roleType = RoleType.AGENCY;
          break;
        default:
          roleType = RoleType.ENTERPRISE; // 默认企业角色
      }
      
      // 1. 先获取模板已绑定的角色
      const boundRolesResponse = await getTemplateRoles(props.templateData.id);
      console.log('获取模板绑定角色API返回:', boundRolesResponse);
      
      // 处理标准响应格式 { code: 200, message: 'success', data: [...] }
      let boundRoles = boundRolesResponse as any[];
      if (boundRolesResponse && (boundRolesResponse as any).code === 200) {
        boundRoles = (boundRolesResponse as any).data || [];
      }
      
      // 2. 加载该类型的所有角色
      const allRolesResponse = await getRolesByType(roleType);
      console.log('获取角色列表API返回:', allRolesResponse);
      
      // 处理标准响应格式 { code: 200, message: 'success', data: [...] }
      let allRoles = allRolesResponse as any[];
      if (allRolesResponse && (allRolesResponse as any).code === 200) {
        allRoles = (allRolesResponse as any).data || [];
      }
      
      // 3. 处理已绑定角色，准备transfer数据
      const boundRoleDetails: any[] = [];
      const boundRoleTransferKeys: any[] = [];
      
      boundRoles.forEach((boundRole: any) => {
        // 使用role对象中的rolesName字段作为角色名称
        const roleName = boundRole.role?.rolesName || '未命名角色';
        // 获取正确的roleId
        const actualRoleId = boundRole.roleId || boundRole.id;
        // 创建transfer key - 添加前缀'bound_'以区分已绑定角色
        const transferKey = `bound_${actualRoleId}`;
        
        // 添加到已绑定角色列表
        boundRoleDetails.push({
          ...boundRole,
          displayName: roleName,
          transferKey,
          actualRoleId,
          isInherit: boundRole.isInherit !== undefined ? boundRole.isInherit : 1
        });
        
        boundRoleTransferKeys.push(transferKey);
      });
      
      // 4. 处理可选角色（排除已绑定的角色）
      const availableRoleDetails: any[] = [];
      const boundRoleIds = boundRoles.map((role: any) => role.roleId || role.id);
      
      allRoles.forEach((role: any) => {
        // 排除已绑定的角色
        if (!boundRoleIds.includes(role.id)) {
          // 创建transfer key - 添加前缀'avail_'以区分可选角色
          const transferKey = `avail_${role.id}`;
          
          availableRoleDetails.push({
            ...role,
            displayName: role.rolesName || '未命名角色',
            transferKey,
            actualRoleId: role.id,
            isInherit: 1 // 默认继承
          });
        }
      });
      
      // 5. 合并所有角色，设置数据
      allRolesForTransfer.value = [...availableRoleDetails, ...boundRoleDetails];
      detailedSelectedRoles.value = boundRoleDetails;
      selectedRoleIdsForTransfer.value = boundRoleTransferKeys;
    } catch (error) {
      console.error('加载角色数据失败:', error);
      ElMessage.error('加载角色数据失败');
    } finally {
      loading.value = false;
    }
  } else if (!newVal) {
    // 对话框关闭时重置数据
    allRolesForTransfer.value = [];
    selectedRoleIdsForTransfer.value = [];
    detailedSelectedRoles.value = [];
  }
});

// 处理transfer组件选择变化
const handleTransferChange = (newSelectedTransferKeys: any[], direction: 'left' | 'right', movedKeys: any[]) => {
  // 更新detailedSelectedRoles
  const newDetailedSelectedRoles: any[] = [];
  
  newSelectedTransferKeys.forEach(transferKey => {
    // 查找对应的角色
    const roleInfo = allRolesForTransfer.value.find(r => r.transferKey === transferKey);
    if (roleInfo) {
      newDetailedSelectedRoles.push(roleInfo);
    }
  });
  
  detailedSelectedRoles.value = newDetailedSelectedRoles;
};

// 关闭对话框
const handleClose = () => {
  if (saving.value) return;
  emit('update:visible', false);
};

// 提交角色绑定
const handleSubmit = async () => {
  if (!props.templateData) {
    ElMessage.error('模板数据不存在，无法保存');
    return;
  }
  
  saving.value = true;
  try {
    // 构建请求数据 - 使用actualRoleId作为roleId
    const roleBinds: RoleBinding[] = detailedSelectedRoles.value.map((role: any) => ({
      roleId: role.actualRoleId,
      isInherit: role.isInherit,
    }));
    
    // 调用绑定接口
    await bindTemplateRoles({
      templateId: props.templateData.id,
      roleBinds,
    });
    
    ElMessage.success('角色绑定成功');
    emit('submit', { templateId: props.templateData.id, roleBinds });
    emit('update:visible', false);
  } catch (error) {
    console.error('保存角色绑定失败:', error);
    ElMessage.error('保存角色绑定失败');
  } finally {
    saving.value = false;
  }
};
</script>

<style scoped>
.bind-roles-dialog :deep(.el-dialog__body) {
  padding-top: 10px;
  padding-bottom: 20px; 
}
.loading-container {
  padding: 20px;
}
.role-transfer {
  display: flex;
  justify-content: center; 
  align-items: center;
}

.bind-roles-dialog :deep(.el-transfer-panel) {
  width: 250px; 
  height: 350px;
}
.bind-roles-dialog :deep(.el-transfer-panel__list.is-filterable) {
  height: calc(100% - 42px - 20px); /* header - footer padding */
}
.bind-roles-dialog :deep(.el-transfer__buttons) {
  padding: 0 15px;
}

.selected-roles-config {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  min-height: 310px; /* Match transfer panel list area roughly */
}

.role-config-item :deep(.el-card__body) {
  padding: 10px 15px;
}

.ml-1 {
  margin-left: 0.25rem;
}
.mb-2 {
  margin-bottom: 0.5rem;
}
.mb-4 {
  margin-bottom: 1rem;
}

.flex {
  display: flex;
}
.justify-between {
  justify-content: space-between;
}
.items-center {
  align-items: center;
}
.text-sm {
  font-size: 0.875rem;
}
.text-gray-500 {
  color: #6b7280;
}
.pt-2 {
  padding-top: 0.5rem;
}
.font-medium {
  font-weight: 500;
}
</style> 