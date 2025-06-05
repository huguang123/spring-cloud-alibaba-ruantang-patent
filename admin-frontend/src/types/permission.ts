/**
 * sys_roles 表对应的平台角色类型 (roles_type = 0)
 */
export interface PlatformRole {
  id: number | string; // 使用 string 以便 el-tree node-key
  rolesCode: string;
  rolesName: string;
  tenantId: number; // Should be 0 for platform roles
  rolesType: 0;
  rolesDescription?: string;
  createTime?: number;
  updateTime?: number;
}

/**
 * 前端权限节点 (perm_node) 映射到 ElTree 的节点结构
 * 使用 string 类型的 id 作为 node-key
 */
export interface PermissionNode {
  id: string; // Unique ID for el-tree (e.g., `module-${moduleId}-node-${nodeId}`)
  label: string; // nodeName
  nodeType: 0 | 1 | 2 | 3; // 0: Module Root, 1: Menu, 2: Button, 3: Field
  permType?: 0 | 1; // 0: Operation, 1: Data (relevant for leaves)
  permId?: number | string; // sys_perms.id (if permType=0)
  dataPolicyId?: number | string; // sys_data_policy.id (if permType=1)
  dataScope?: 1 | 2; // 1: View, 2: Edit (if nodeType=3)
  isBasic?: 0 | 1;
  rawModuleId: number | string; // Original perm_module.id
  rawNodeId?: number | string; // Original perm_node.id (for leaf nodes)
  children?: PermissionNode[];
}

/**
 * 权限树类型，通常是模块作为顶级节点
 */
export type PermissionTree = PermissionNode[];

/**
 * 保存角色权限时发送到后端的数据结构
 */
export interface RolePermissionSavePayload {
  roleId: number | string;
  functionalNodeIds: string[]; // 选中的功能权限节点 ID (perm_node.id for permType=0)
  dataNodeIds?: string[]; // 选中的数据权限节点 ID (perm_node.id for permType=1, optional based on module types)
  // version?: string; // If versioning is used
} 