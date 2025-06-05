import { useUserStore } from '@/store/user'
import { Directive, DirectiveBinding } from 'vue'

// 检查是否有权限
export function hasPermission(permission: string | string[]): boolean {
  const userStore = useUserStore()
  const userPermissions = userStore.userInfo.permissions
  
  if (typeof permission === 'string') {
    return userPermissions.includes(permission)
  }
  
  if (Array.isArray(permission)) {
    return permission.some(p => userPermissions.includes(p))
  }
  
  return false
}

// 权限指令
export const vPermission: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding
    
    if (!hasPermission(value)) {
      // 移除元素
      el.parentNode?.removeChild(el)
    }
  }
}

// 权限回收监听
export class PermissionWatcher {
  static observer: MutationObserver | null = null
  
  static watchDOM(selector: string, permissions: string | string[]) {
    if (!this.observer) {
      this.observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
          if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
            this.checkNewNodes(mutation.addedNodes, selector, permissions)
          }
        })
      })
      
      this.observer.observe(document.body, {
        childList: true,
        subtree: true
      })
    }
  }
  
  static checkNewNodes(nodes: NodeList, selector: string, permissions: string | string[]) {
    nodes.forEach((node) => {
      if (node.nodeType === Node.ELEMENT_NODE) {
        const element = node as HTMLElement
        
        // 检查当前节点
        if (element.matches(selector) && !hasPermission(permissions)) {
          element.remove()
        }
        
        // 检查子节点
        const matchingElements = element.querySelectorAll(selector)
        matchingElements.forEach((matchingElement) => {
          if (!hasPermission(permissions)) {
            matchingElement.remove()
          }
        })
      }
    })
  }
  
  static disconnect() {
    if (this.observer) {
      this.observer.disconnect()
      this.observer = null
    }
  }
} 