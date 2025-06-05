/**
 * 将时间戳格式化为日期字符串
 * @param timestamp 时间戳（毫秒）或日期字符串
 * @param format 格式化模板，默认为'YYYY-MM-DD HH:mm:ss'
 * @returns 格式化后的日期字符串
 */
export const formatTimestamp = (timestamp: number | string | undefined | null, format: string = 'YYYY-MM-DD HH:mm:ss'): string => {
  if (!timestamp) return '-';

  let date: Date;
  
  try {
    // 处理字符串类型的时间戳或日期字符串
    if (typeof timestamp === 'string') {
      // 尝试检查是否是数字字符串（时间戳）
      if (/^\d+$/.test(timestamp)) {
        date = new Date(parseInt(timestamp));
      } 
      // 尝试识别 ISO 8601 或其他标准日期格式
      else if (timestamp.includes('T') || timestamp.includes('-') || timestamp.includes('/')) {
        date = new Date(timestamp);
      } 
      // 如果无法识别，则返回原样
      else {
        return timestamp;
      }
    } 
    // 处理数字类型的时间戳
    else {
      date = new Date(timestamp);
    }
    
    // 检验日期是否有效
    if (isNaN(date.getTime())) {
      return String(timestamp);
    }
    
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return format
      .replace('YYYY', String(year))
      .replace('MM', month)
      .replace('DD', day)
      .replace('HH', hours)
      .replace('mm', minutes)
      .replace('ss', seconds);
  } catch (error) {
    console.error('时间格式化错误:', error);
    return String(timestamp);
  }
};

/**
 * 将时间戳格式化为相对时间（如：1天前，2小时前）
 * @param timestamp 时间戳（毫秒）
 * @returns 相对时间字符串
 */
export const formatRelativeTime = (timestamp: number): string => {
  if (!timestamp) return '-';
  
  const now = Date.now();
  const diff = now - timestamp;
  const minute = 60 * 1000;
  const hour = minute * 60;
  const day = hour * 24;
  const month = day * 30;
  const year = day * 365;
  
  if (diff < minute) {
    return '刚刚';
  } else if (diff < hour) {
    return Math.floor(diff / minute) + '分钟前';
  } else if (diff < day) {
    return Math.floor(diff / hour) + '小时前';
  } else if (diff < month) {
    return Math.floor(diff / day) + '天前';
  } else if (diff < year) {
    return Math.floor(diff / month) + '个月前';
  } else {
    return Math.floor(diff / year) + '年前';
  }
};

/**
 * 将数字格式化为带千分位的字符串
 * @param num 需要格式化的数字
 * @returns 带千分位的字符串
 */
export const formatNumber = (num: number): string => {
  if (num === null || num === undefined) return '-';
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

/**
 * 格式化文件大小
 * @param bytes 字节数
 * @returns 格式化后的文件大小字符串
 */
export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B';
  
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}; 