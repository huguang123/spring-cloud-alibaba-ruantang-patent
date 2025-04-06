// utils.js - 工具函数模块

/**
 * 工具函数集合
 */

/**
 * 防抖函数
 * @param {Function} func 要执行的函数
 * @param {number} wait 延迟时间（毫秒）
 * @returns {Function} 防抖处理后的函数
 */
export function debounce(func, wait = 300) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

/**
 * 显示错误消息
 * @param {string} message 错误消息内容
 */
export function showErrorMessage(message) {
  alert(`错误: ${message}`);
  // 实际项目中可使用更美观的提示，如Element UI的Message组件
  // ElementUI.Message.error(message);
}

/**
 * 显示成功消息
 * @param {string} message 成功消息内容
 */
export function showSuccessMessage(message) {
  alert(`成功: ${message}`);
  // 实际项目中可使用更美观的提示，如Element UI的Message组件
  // ElementUI.Message.success(message);
}

/**
 * 显示确认对话框
 * @param {string} message 确认消息内容
 * @returns {boolean} 用户确认结果
 */
export function confirmAction(message) {
  return window.confirm(message);
  // 实际项目中可使用更美观的对话框，如Element UI的MessageBox组件
  // return ElementUI.MessageBox.confirm(message, '提示', {
  //   confirmButtonText: '确定',
  //   cancelButtonText: '取消',
  //   type: 'warning'
  // }).then(() => true).catch(() => false);
}

/**
 * 检查领域层级是否超过限制
 * @param {number} level 当前层级
 * @param {number} maxLevel 最大层级限制
 * @returns {boolean} 是否超过限制
 */
export function checkLevelLimit(level, maxLevel = 5) {
  if (level >= maxLevel) {
    showErrorMessage(`最多支持${maxLevel}级技术领域结构，无法添加更深层级的子领域`);
    return true;
  }
  return false;
}

/**
 * 处理BigInt类型的ID
 * @param {String|Number} id ID值
 * @returns {BigInt} 转换后的BigInt
 */
export function toBigInt(id) {
  try {
    if (id === null || id === undefined) return null;
    return BigInt(id);
  } catch (error) {
    console.error('ID转换为BigInt失败:', error);
    return null;
  }
}

/**
 * 安全解析JSON，处理BigInt
 * @param {String} jsonStr JSON字符串
 * @returns {Object} 解析结果
 */
export function safeJSONParse(jsonStr) {
  try {
    return JSON.parse(jsonStr);
  } catch (error) {
    console.error('JSON解析失败:', error);
    return null;
  }
}

export default {
  debounce,
  showErrorMessage,
  showSuccessMessage,
  confirmAction,
  toBigInt,
  safeJSONParse,
  checkLevelLimit
}; 