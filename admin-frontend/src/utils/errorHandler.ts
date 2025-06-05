import { ElMessage } from 'element-plus'

/**
 * 统一错误处理函数
 * @param error 错误对象
 * @param defaultMessage 默认错误消息
 * @param showMessage 是否显示错误消息，默认为true
 * @returns 处理后的错误消息
 */
export const handleApiError = (error: any, defaultMessage: string, showMessage: boolean = true): string => {
  console.error('=== 错误处理开始 ===');
  console.error('默认消息:', defaultMessage);
  console.error('错误对象:', error);
  console.error('错误对象类型:', typeof error);
  console.error('是否有response:', !!error?.response);
  console.error('response状态码:', error?.response?.status);
  console.error('response.data:', error?.response?.data);
  
  let errorMessage = defaultMessage;
  
  // 尝试从不同的错误格式中提取错误信息
  if (error && typeof error === 'object') {
    console.log('✓ 错误是对象类型');
    
    // 优先从response.data中提取错误信息（axios错误格式）
    if (error.response && error.response.data) {
      const responseData = error.response.data;
      console.log('✓ 找到 error.response.data:', responseData);
      console.log('✓ responseData 类型:', typeof responseData);
      
      // 直接检查message字段
      if (responseData.message) {
        errorMessage = String(responseData.message);
        console.log('✅ 成功从responseData.message提取错误信息:', errorMessage);
      }
      // 检查msg字段
      else if (responseData.msg) {
        errorMessage = String(responseData.msg);
        console.log('✅ 成功从responseData.msg提取错误信息:', errorMessage);
      }
      // 检查error字段
      else if (responseData.error) {
        errorMessage = String(responseData.error);
        console.log('✅ 成功从responseData.error提取错误信息:', errorMessage);
      }
      // 如果responseData直接是字符串
      else if (typeof responseData === 'string') {
        errorMessage = responseData;
        console.log('✅ responseData是字符串:', errorMessage);
      }
      else {
        console.log('❌ responseData中没有找到message/msg/error字段');
        console.log('❌ responseData的所有属性:', Object.keys(responseData));
      }
    }
    else {
      console.log('❌ 没有找到 error.response.data');
      
      // 检查error.data.message
      if (error.data?.message) {
        errorMessage = String(error.data.message);
        console.log('✅ 从error.data.message提取错误信息:', errorMessage);
      }
      // 检查error.message（排除HTTP状态码错误）
      else if (error.message && typeof error.message === 'string') {
        console.log('检查 error.message:', error.message);
        if (!error.message.includes('status code') && 
            !error.message.includes('Network Error') && 
            !error.message.includes('timeout') &&
            !error.message.includes('ECONNABORTED')) {
          errorMessage = error.message;
          console.log('✅ 从error.message提取错误信息:', errorMessage);
        } else {
          console.log('❌ error.message 包含HTTP状态码或网络错误，跳过');
        }
      }
      else {
        console.log('❌ 无法从错误对象中提取错误信息');
      }
    }
  }
  else {
    console.log('❌ 错误不是对象类型');
  }
  
  // 如果是字符串错误
  if (typeof error === 'string') {
    errorMessage = error;
    console.log('✅ 错误是字符串:', errorMessage);
  }
  
  console.log('=== 最终错误信息 ===:', errorMessage);
  console.log('=== 错误处理结束 ===');
  
  // 显示错误消息
  if (showMessage) {
    ElMessage.error(errorMessage);
  }
  
  return errorMessage;
}

/**
 * 统一成功响应处理函数
 * @param response API响应对象
 * @param successMessage 成功消息
 * @param errorMessage 错误消息
 * @param showMessage 是否显示消息，默认为true
 * @returns 是否成功
 */
export const handleApiResponse = (response: any, successMessage: string, errorMessage: string, showMessage: boolean = true): boolean => {
  // 检查响应是否成功
  if (response && (response.code === 200 || response.code === 0 || response.success === true)) {
    if (showMessage) {
      ElMessage.success(successMessage);
    }
    return true;
  } else {
    // 提取错误信息
    let actualErrorMessage = errorMessage;
    if (response?.message) {
      actualErrorMessage = response.message;
    } else if (response?.msg) {
      actualErrorMessage = response.msg;
    } else if (response?.error) {
      actualErrorMessage = response.error;
    }
    
    if (showMessage) {
      ElMessage.error(actualErrorMessage);
    }
    return false;
  }
}

/**
 * 日期格式化函数
 * @param timestamp 时间戳（字符串或数字）
 * @returns 格式化后的日期字符串
 */
export const formatDateTime = (timestamp: string | number) => {
  try {
    // 尝试将时间戳转换为数字（毫秒）
    const numericTimestamp = typeof timestamp === 'string' ? parseInt(timestamp) : timestamp;
    const date = new Date(numericTimestamp);
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
      return '无效日期';
    }
    
    return date.toLocaleString();
  } catch (error) {
    console.error('日期格式化失败:', error, timestamp);
    return '无效日期';
  }
} 