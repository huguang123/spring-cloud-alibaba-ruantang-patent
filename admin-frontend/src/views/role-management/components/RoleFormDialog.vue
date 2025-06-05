<template>
  <el-dialog
    :model-value="visible"
    :title="title"
    width="600px"
    @close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form 
      ref="roleFormRef" 
      :model="formModel" 
      :rules="rules" 
      label-width="100px"
      v-loading="loading"
    >
      <el-form-item label="角色编码" prop="rolesCode">
        <el-input v-model="formModel.rolesCode" placeholder="请输入角色编码" :disabled="isEditing" />
      </el-form-item>
      <el-form-item label="角色名称" prop="rolesName">
        <el-input v-model="formModel.rolesName" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="角色类型" prop="rolesType">
        <el-select v-model="formModel.rolesType" placeholder="请选择角色类型" style="width: 100%;" :disabled="isEditing">
          <el-option 
            v-for="(name, value) in RoleTypeMap" 
            :key="value" 
            :label="name" 
            :value="Number(value)" 
          />
        </el-select>
      </el-form-item>
      <el-form-item label="角色描述" prop="rolesDescription">
        <el-input 
          v-model="formModel.rolesDescription" 
          type="textarea" 
          :rows="3" 
          placeholder="请输入角色描述"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        {{ isEditing ? '确认修改' : '确认新增' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import {
  type RoleInfo, RoleType, RoleTypeMap, 
  type CreateRolePayload, 
  // getRoleDetails, // 如果需要加载完整详情用于编辑，则取消注释
} from '@/api/role';

interface Props {
  visible: boolean;
  isEditing: boolean; // 修改为与父组件一致
  initialData?: RoleInfo | null; // 修改名称与父组件一致
}

const props = defineProps<Props>();
const emit = defineEmits(['update:visible', 'submit']);

const roleFormRef = ref<FormInstance>();
const loading = ref(false);
const submitting = ref(false);

const initialFormState: CreateRolePayload = {
  rolesCode: '',
  rolesName: '',
  rolesType: RoleType.PLATFORM, // 默认为平台角色
  rolesDescription: '',
};

const formModel = reactive<CreateRolePayload>({ ...initialFormState });

const isEdit = computed(() => props.isEditing);
const title = computed(() => (isEdit.value ? '编辑角色' : '新增角色'));

watch(() => props.visible, (newVal) => {
  if (newVal) {
    resetForm();
    if (props.initialData && isEdit.value) {
      // 编辑模式：回填数据
      // 如果列表数据不完整，可能需要调用 getRoleDetails(props.initialData.id) 获取完整数据
      // 这里假设 initialData 包含所有需要的字段
      nextTick(() => { // 确保DOM更新后再赋值
        Object.assign(formModel, props.initialData);
      });
    }
  } else {
    resetForm();
  }
});

const resetForm = () => {
  if (roleFormRef.value) {
    roleFormRef.value.resetFields(); 
  }
  Object.assign(formModel, initialFormState); 
  // 特别处理 rolesType，确保有默认值
  formModel.rolesType = RoleType.PLATFORM;
};

const rules = reactive<FormRules>({
  rolesCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' },
    // 可以添加 pattern 校验编码格式，例如：/^[a-zA-Z0-9_]+$/
  ],
  rolesName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
  ],
  rolesType: [
    { required: true, message: '请选择角色类型', trigger: 'change' },
  ],
  rolesDescription: [
    { max: 200, message: '描述最多 200 个字符', trigger: 'blur' },
  ],
});

const handleClose = () => {
  emit('update:visible', false);
};

const handleSubmit = async () => {
  if (!roleFormRef.value) return;
  try {
    await roleFormRef.value.validate();
    submitting.value = true;
    
    if (isEdit.value && props.initialData?.id) {
      // 编辑模式：只传入id、rolesName和rolesDescription字段
      emit('submit', {
        id: props.initialData.id,
        rolesName: formModel.rolesName,
        rolesDescription: formModel.rolesDescription
      });
    } else {
      // 新增模式：传入全部必要字段
      emit('submit', formModel as CreateRolePayload);
    }
    // 成功后父组件会关闭弹窗并刷新列表
  } catch (error) {
    console.log('表单校验失败或提交出错', error);
    // ElMessage.error('请检查表单填写是否正确');
  } finally {
    submitting.value = false;
  }
};

</script>

<style scoped>
/* 可以添加一些局部样式 */
</style> 