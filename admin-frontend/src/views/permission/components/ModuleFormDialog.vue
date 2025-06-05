<template>
  <el-dialog
    :title="isEditing ? '编辑模块' : '新增模块'"
    v-model="dialogVisible"
    width="500px"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <el-form 
      ref="formRef" 
      :model="form" 
      :rules="rules" 
      label-width="100px" 
      @submit.prevent
    >
      <el-form-item label="模块名称" prop="moduleName">
        <el-input v-model="form.moduleName" placeholder="请输入模块名称" />
      </el-form-item>
      
      <el-form-item label="模块类型" prop="moduleType">
        <el-radio-group v-model="form.moduleType">
          <el-radio :label="1">功能权限模块</el-radio>
          <el-radio :label="2">数据权限模块</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="排序号" prop="sort">
        <el-input-number v-model="form.sort" :min="1" :max="999" placeholder="排序号（数字越小排序越靠前）" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue';
import type { FormInstance } from 'element-plus';
import { ElDialog, ElForm, ElFormItem, ElInput, ElInputNumber, ElRadioGroup, ElRadio, ElButton, ElMessage } from 'element-plus';
import type { PropType } from 'vue';
import { PermissionModule } from '@/api/templatePermission';

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
    type: Object as PropType<Partial<PermissionModule> | null>,
    default: null
  },
  existingModules: {
    type: Array as PropType<PermissionModule[]>,
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

// 表单数据
const form = reactive({
  id: undefined as number | undefined,
  moduleName: '',
  moduleType: 1,
  templateVersionId: undefined as number | string | undefined,
  sort: 1
});

// 表单验证规则
const rules = {
  moduleName: [
    { required: true, message: '请输入模块名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  moduleType: [
    { required: true, message: '请选择模块类型', trigger: 'change' }
  ],
  sort: [
    { required: true, message: '请输入排序号', trigger: 'blur' }
  ]
};

// 监听初始数据变化，重置表单
watch(() => props.initialData, (newData) => {
  if (newData) {
    // 复制初始数据到表单
    form.id = newData.id;
    form.moduleName = newData.moduleName || '';
    form.moduleType = newData.moduleType || 1;
    form.templateVersionId = newData.templateVersionId;
    form.sort = typeof newData.sort === 'number' ? newData.sort : (newData.sort ? Number(newData.sort) : 1);
  } else {
    // 重置表单
    form.id = undefined;
    form.moduleName = '';
    form.moduleType = 1;
    form.templateVersionId = undefined;
    form.sort = 1;
  }
}, { immediate: true });

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true;
      try {
        // 检查模块名称是否重复
        const nameExists = props.existingModules.some(
          m => m.moduleName === form.moduleName && m.id !== form.id
        );
        
        if (nameExists) {
          ElMessage.warning('模块名称已存在，请使用其他名称');
          return;
        }
        
        // 提交表单数据
        emit('submit', {
          id: form.id,
          moduleName: form.moduleName,
          moduleType: form.moduleType,
          templateVersionId: form.templateVersionId,
          sort: form.sort
        });
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
</style> 