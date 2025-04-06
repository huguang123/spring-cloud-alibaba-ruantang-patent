// 消息提示组件

/**
 * 创建并显示一个提示消息
 * @param {string} message - 提示消息内容
 * @param {string} type - 提示类型 (success, error, warning, info)
 * @param {number} duration - 显示时长(ms)，默认3000ms
 */
const showToast = (message, type = 'info', duration = 3000) => {
  if (!message) {
    console.warn('Toast message is empty');
    return;
  }
  
  try {
    // 如果已有toast，则先移除
    const existingToast = document.querySelector('.toast-container');
    if (existingToast) {
      document.body.removeChild(existingToast);
    }
    
    // 创建toast容器
    const toastContainer = document.createElement('div');
    toastContainer.className = 'toast-container fixed top-4 right-4 z-50 flex flex-col items-end space-y-2';
    
    // 设置图标和颜色
    let iconClass = '';
    let bgColor = '';
    
    switch (type) {
      case 'success':
        iconClass = 'fa-check-circle';
        bgColor = 'bg-green-500';
        break;
      case 'error':
        iconClass = 'fa-times-circle';
        bgColor = 'bg-red-500';
        break;
      case 'warning':
        iconClass = 'fa-exclamation-triangle';
        bgColor = 'bg-yellow-500';
        break;
      case 'info':
      default:
        iconClass = 'fa-info-circle';
        bgColor = 'bg-blue-500';
        break;
    }
    
    // 创建toast元素
    const toastElement = document.createElement('div');
    toastElement.className = `toast flex items-center p-3 ${bgColor} text-white rounded-md shadow-lg transform transition-all duration-300 ease-in-out translate-x-0`;
    toastElement.innerHTML = `
      <i class="fas ${iconClass} mr-2"></i>
      <span>${message}</span>
    `;
    
    // 添加到容器
    toastContainer.appendChild(toastElement);
    document.body.appendChild(toastContainer);
    
    // 淡出动画
    setTimeout(() => {
      if (toastElement && toastElement.classList) {
        toastElement.classList.add('opacity-0');
      }
      setTimeout(() => {
        // 如果toast容器仍然存在，则移除
        if (document.body.contains(toastContainer)) {
          document.body.removeChild(toastContainer);
        }
      }, 300);
    }, duration);
  } catch (err) {
    console.error('Toast error:', err);
    // 出错时使用原生alert作为备选
    alert(message);
  }
};

// 便捷方法
const success = (message, duration) => showToast(message, 'success', duration);
const error = (message, duration) => showToast(message, 'error', duration);
const warning = (message, duration) => showToast(message, 'warning', duration);
const info = (message, duration) => showToast(message, 'info', duration);

// 检查窗口对象是否存在（防止服务器端渲染错误）
if (typeof window !== 'undefined') {
  // 初始化toast对象
  window.toast = {
    show: showToast,
    success,
    error,
    warning,
    info
  };
}

// 导出方法
export default {
  show: showToast,
  success,
  error,
  warning,
  info
}; 