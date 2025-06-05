<template>
  <div class="page-header-container">
    <div class="page-header-content">
      <div class="page-header-title-section">
        <h1 class="page-header-title">{{ title }}</h1>
        <p v-if="subtitle" class="page-header-subtitle">{{ subtitle }}</p>
      </div>
      <div class="page-header-actions">
        <slot name="actions"></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true
  },
  subtitle: {
    type: String,
    default: ''
  }
});
</script>

<style scoped>
.page-header-container {
  margin-bottom: 24px;
}

.page-header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
}

.page-header-title-section {
  flex: 1;
  margin-right: 16px;
}

.page-header-title {
  margin: 0;
  color: #1f2329;
  font-weight: 500;
  font-size: 24px;
  line-height: 32px;
}

.page-header-subtitle {
  margin: 8px 0 0;
  color: #646a73;
  font-size: 14px;
  line-height: 22px;
}

.page-header-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
}

@media (max-width: 768px) {
  .page-header-content {
    flex-direction: column;
  }
  
  .page-header-title-section {
    margin-right: 0;
    margin-bottom: 16px;
    width: 100%;
  }
  
  .page-header-actions {
    width: 100%;
  }
}
</style> 