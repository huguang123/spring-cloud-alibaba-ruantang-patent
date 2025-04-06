<template>
  <div id="app">
    <header class="main-header bg-blue-500 text-white py-4">
      <div class="container mx-auto px-4 flex justify-between items-center">
        <h1 class="text-xl font-bold">技术交底书智能撰写系统</h1>
        <div class="flex items-center">
          <span class="mr-4">欢迎使用</span>
        </div>
      </div>
    </header>
    
    <div class="main-content flex">
      <iframe id="mainFrame" src="legacy-index.html" class="page-iframe"></iframe>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      loading: false
    }
  },
  mounted() {
    // 执行初始化逻辑
    console.log('应用已初始化');
  }
}
</script>

<style>
body {
  font-family: 'Inter', sans-serif;
  margin: 0;
  padding: 0;
  overflow: hidden;
  height: 100vh;
  --header-height: 60px;
  background-color: #f9fafb;
}
.main-header {
  background: linear-gradient(135deg, #2F54EB 0%, #13C2C2 100%);
  height: var(--header-height);
}
.main-content {
  height: calc(100vh - var(--header-height));
}
.page-iframe {
  width: 100%;
  height: 100%;
  border: none;
  background-color: white;
  transition: opacity 0.3s ease;
}
</style> 