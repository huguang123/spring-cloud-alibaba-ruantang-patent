import type { PlatformRole, PermissionTree, RolePermissionSavePayload, PermissionNode } from '@/api/permission';

// --- Mock Data ---

const mockPlatformRoles: PlatformRole[] = [
  {
    id: 1,
    rolesCode: 'SYSADMIN',
    rolesName: '系统管理员',
    tenantId: 0,
    rolesType: 0,
    rolesDescription: '系统级超级管理员',
    createTime: 1672531200000,
  },
  {
    id: 2,
    rolesCode: 'SPECIALIST_MANAGER',
    rolesName: '专利管理员',
    tenantId: 0,
    rolesType: 0,
    rolesDescription: '负责专利申请、管理的角色',
    createTime: 1672531200000,
  },
  {
    id: 3,
    rolesCode: 'PRODUCT_MANAGER',
    rolesName: '产品经理',
    tenantId: 0,
    rolesType: 0,
    rolesDescription: '负责产品规划和设计的角色',
    createTime: 1672531200000,
  },
  {
    id: 4,
    rolesCode: 'RND_ENGINEER',
    rolesName: '研发工程师',
    tenantId: 0,
    rolesType: 0,
    rolesDescription: '技术研发和实施的角色',
    createTime: 1672531200000,
  },
  {
    id: 5,
    rolesCode: 'AUDITOR',
    rolesName: '审计员',
    tenantId: 0,
    rolesType: 0,
    rolesDescription: '负责系统审计和流程的角色',
    createTime: 1672531200000,
  },
];

// Helper to generate unique node IDs for el-tree
const generateNodeId = (type: string, id: string | number): string => {
  // Simple prefixing to ensure uniqueness across types
  return `${type}-${id}`;
};

// Mock Permission Tree Data (Simulates backend response)
const generateMockPermissionTree = (roleId: string | number): PermissionTree => {
  console.log(`Mock: Generating combined permission tree for role: ${roleId}`);
  
  // Functional Modules (moduleType: 1)
  const functionalModules: PermissionNode[] = [
    {
      id: generateNodeId('module', 1),
      label: '数据看板', nodeType: 0, moduleType: 1, rawModuleId: 1, icon: 'Monitor', 
      children: [
        { id: generateNodeId('node', 101), label: '查看数据看板', nodeType: 1, permType: 0, permId: 1001, isLeaf: true, rawNodeId: 101, rawModuleId: 1 },
        { id: generateNodeId('node', 102), label: '导出报表', nodeType: 2, permType: 0, permId: 1002, isLeaf: true, rawNodeId: 102, rawModuleId: 1 },
      ]
    },
    {
      id: generateNodeId('module', 2),
      label: '交底书管理', nodeType: 0, moduleType: 1, rawModuleId: 2, icon: 'Document',
      children: [
        { id: generateNodeId('node', 201), label: '查看交底书列表', nodeType: 1, permType: 0, permId: 2001, isLeaf: true, rawNodeId: 201, rawModuleId: 2 },
        { id: generateNodeId('node', 202), label: '创建交底书', nodeType: 2, permType: 0, permId: 2002, isLeaf: true, rawNodeId: 202, rawModuleId: 2 },
        { id: generateNodeId('node', 203), label: '编辑交底书', nodeType: 2, permType: 0, permId: 2003, isLeaf: true, rawNodeId: 203, rawModuleId: 2 },
        { id: generateNodeId('node', 204), label: '删除交底书', nodeType: 2, permType: 0, permId: 2004, isLeaf: true, rawNodeId: 204, rawModuleId: 2 },
        // Note: Representing data scope as a functional node here, adjust if needed
        // { id: generateNodeId('node', 205), label: '交底书数据范围控制', nodeType: 3, permType: 1, dataPolicyId: 3001, dataScope: 1, isLeaf: true, rawNodeId: 205, rawModuleId: 2 },
      ]
    },
     {
      id: generateNodeId('module', 3),
      label: '组织管理', nodeType: 0, moduleType: 1, rawModuleId: 3, icon: 'OfficeBuilding',
      children: [
        { id: generateNodeId('node', 301), label: '查看组织架构', nodeType: 1, permType: 0, permId: 4001, isLeaf: true, rawNodeId: 301, rawModuleId: 3 },
        { id: generateNodeId('node', 302), label: '编辑组织信息', nodeType: 2, permType: 0, permId: 4002, isLeaf: true, rawNodeId: 302, rawModuleId: 3 },
      ]
    },
  ];
  
  // Data Modules (moduleType: 2)
  const dataModules: PermissionNode[] = [
    {
        id: generateNodeId('datamodule', 10),
        label: '专利数据', nodeType: 0, moduleType: 2, rawModuleId: 10, // Use different raw IDs if needed
        children: [
            { id: generateNodeId('datanode', 1001), label: '专利基本信息 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 5001, isLeaf: true, rawNodeId: 1001, rawModuleId: 10 },
            { id: generateNodeId('datanode', 1002), label: '专利申请信息 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 5002, isLeaf: true, rawNodeId: 1002, rawModuleId: 10 },
            { id: generateNodeId('datanode', 1003), label: '专利法律状态 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 5003, isLeaf: true, rawNodeId: 1003, rawModuleId: 10 },
            { id: generateNodeId('datanode', 1004), label: '专利费用信息 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 5004, isLeaf: true, rawNodeId: 1004, rawModuleId: 10 },
        ]
    },
    {
        id: generateNodeId('datamodule', 11),
        label: '产品数据', nodeType: 0, moduleType: 2, rawModuleId: 11,
        children: [
            { id: generateNodeId('datanode', 1101), label: '产品基本信息 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 6001, isLeaf: true, rawNodeId: 1101, rawModuleId: 11 },
            { id: generateNodeId('datanode', 1102), label: '产品技术特征 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 6002, isLeaf: true, rawNodeId: 1102, rawModuleId: 11 },
            { id: generateNodeId('datanode', 1103), label: '产品专利映射 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 6003, isLeaf: true, rawNodeId: 1103, rawModuleId: 11 },
        ]
    },
    {
        id: generateNodeId('datamodule', 12),
        label: '交底书数据', nodeType: 0, moduleType: 2, rawModuleId: 12,
        children: [
            { id: generateNodeId('datanode', 1201), label: '交底书内容 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 7001, isLeaf: true, rawNodeId: 1201, rawModuleId: 12 },
            { id: generateNodeId('datanode', 1202), label: '交底书附件 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 7002, isLeaf: true, rawNodeId: 1202, rawModuleId: 12 },
            { id: generateNodeId('datanode', 1203), label: '交底书评审记录 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 7003, isLeaf: true, rawNodeId: 1203, rawModuleId: 12 },
        ]
    },
      {
        id: generateNodeId('datamodule', 13),
        label: '组织架构数据', nodeType: 0, moduleType: 2, rawModuleId: 13,
        children: [
            { id: generateNodeId('datanode', 1301), label: '部门信息 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 8001, isLeaf: true, rawNodeId: 1301, rawModuleId: 13 },
            { id: generateNodeId('datanode', 1302), label: '人员信息 (查看/编辑)', nodeType: 10, permType: 1, dataPolicyId: 8002, isLeaf: true, rawNodeId: 1302, rawModuleId: 13 },
        ]
    },
  ];

  // Combine functional and data modules into a single tree
  const combinedTree: PermissionTree = [...functionalModules, ...dataModules];
  
  return combinedTree;
};

// Mock selected node IDs (depends on the role)
const getMockSelectedNodeIds = (roleId: string | number): string[] => {
  console.log(`Mock: Getting combined selected IDs for role: ${roleId}`);
  // Default selections - adjust based on roleId for realistic mocks
  let selectedIds: string[] = [
      // Some functional defaults
      generateNodeId('node', 101), // 查看数据看板
      generateNodeId('node', 201), // 查看交底书列表
      generateNodeId('node', 301), // 查看组织架构
      // Some data defaults
      generateNodeId('datanode', 1001), // 专利基本信息
      generateNodeId('datanode', 1101), // 产品基本信息
      generateNodeId('datanode', 1201), // 交底书内容
      generateNodeId('datanode', 1301), // 部门信息
  ]; 
  
  if (roleId === 1) { // Sysadmin - select all leaf nodes
      const tree = generateMockPermissionTree(roleId);
      selectedIds = [];
      const traverse = (nodes: PermissionNode[]) => {
          nodes.forEach(node => {
              if (node.children) {
                  traverse(node.children);
              } else if (node.isLeaf) { // Only add leaf nodes
                  selectedIds.push(node.id);
              }
          });
      };
      traverse(tree);
  } else if (roleId === 2) { // Specialist Manager - more permissions
      selectedIds = [
          generateNodeId('node', 101), generateNodeId('node', 102), // Dashboard
          generateNodeId('node', 201), generateNodeId('node', 202), generateNodeId('node', 203), generateNodeId('node', 204), // Disclosure CRUD
          generateNodeId('node', 301), // View Org
          generateNodeId('datanode', 1001), generateNodeId('datanode', 1002), generateNodeId('datanode', 1003), generateNodeId('datanode', 1004), // All Patent Data
          generateNodeId('datanode', 1201), generateNodeId('datanode', 1202), generateNodeId('datanode', 1203), // All Disclosure Data
          generateNodeId('datanode', 1301), // Dept Info
      ];
  } else if (roleId === 3) { // Product Manager
      selectedIds = [
          generateNodeId('node', 101), // View Dashboard
          generateNodeId('node', 201), // View Disclosure List
          generateNodeId('node', 301), // View Org Structure
      ];
  } // Add more role-specific selections if needed
  
  return selectedIds;
};

// --- Mock API Functions ---

export const mockGetPlatformRoles = async (params?: { keyword?: string }): Promise<{ list: PlatformRole[], total: number }> => {
  console.log('Mock API: getPlatformRoles called with params:', params);
  await new Promise(res => setTimeout(res, 300)); // Simulate network delay
  let filteredRoles = mockPlatformRoles;
  if (params?.keyword) {
    const keywordLower = params.keyword.toLowerCase();
    filteredRoles = mockPlatformRoles.filter(
      role => role.rolesName.toLowerCase().includes(keywordLower) ||
              role.rolesCode.toLowerCase().includes(keywordLower)
    );
  }
  return { list: filteredRoles, total: filteredRoles.length };
};

export const mockGetRolePermissions = async (roleId: number | string): Promise<{ treeData: PermissionTree, selectedNodeIds: string[] }> => {
  console.log('Mock API: getRolePermissions called for roleId:', roleId);
  await new Promise(res => setTimeout(res, 500));
  const treeData = generateMockPermissionTree(roleId);
  const selectedNodeIds = getMockSelectedNodeIds(roleId);
  console.log(`Mock returning ${selectedNodeIds.length} selected IDs for role ${roleId}`);
  return { treeData, selectedNodeIds };
};

export const mockSaveRolePermissions = async (payload: RolePermissionSavePayload): Promise<{ success: boolean }> => {
  console.log('Mock API: saveRolePermissions called with payload:', payload);
  await new Promise(res => setTimeout(res, 400));
  // Simulate success
  return { success: true };
};

export const mockAddPlatformRole = async (roleData: Omit<PlatformRole, 'id' | 'tenantId' | 'rolesType' | 'createTime' | 'updateTime'>): Promise<PlatformRole> => {
    console.log('Mock API: addPlatformRole called with data:', roleData);
    await new Promise(res => setTimeout(res, 300));
    const newId = Math.max(...mockPlatformRoles.map(r => Number(r.id)), 0) + 1;
    const newRole: PlatformRole = {
        ...roleData,
        id: newId,
        tenantId: 0,
        rolesType: 0,
        createTime: Date.now(),
    };
    mockPlatformRoles.push(newRole);
    console.log('Mock DB (roles):', mockPlatformRoles);
    return newRole;
};

export const mockUpdatePlatformRole = async (roleData: PlatformRole): Promise<PlatformRole> => {
    console.log('Mock API: updatePlatformRole called with data:', roleData);
    await new Promise(res => setTimeout(res, 300));
    const index = mockPlatformRoles.findIndex(r => r.id === roleData.id);
    if (index !== -1) {
        mockPlatformRoles[index] = { ...mockPlatformRoles[index], ...roleData, updateTime: Date.now() };
        console.log('Mock DB (roles):', mockPlatformRoles);
        return mockPlatformRoles[index];
    } else {
        throw new Error('Role not found');
    }
};

export const mockDeletePlatformRole = async (roleId: number | string): Promise<{ success: boolean }> => {
    console.log('Mock API: deletePlatformRole called for roleId:', roleId);
    await new Promise(res => setTimeout(res, 300));
    const index = mockPlatformRoles.findIndex(r => r.id === roleId);
    if (index !== -1) {
        mockPlatformRoles.splice(index, 1);
        console.log('Mock DB (roles):', mockPlatformRoles);
        return { success: true };
    } else {
        return { success: false }; // Or throw error
    }
}; 