// src/api/systemTemplateApi.ts

export interface SystemTemplateIdentifier {
  type: 'platform' | 'enterprise' | 'agent';
  name: string;
  templateCode: string; // e.g., PLATFORM_DEFAULT, ENT_DEFAULT, AGENT_DEFAULT
  description: string;
  userType: 'platform' | 'enterprise' | 'agent'; // Corresponds to the general template user type concept
}

export interface TemplateVersionData {
  version: string; // e.g., "v1.0.0", "v2.1.0"
  description?: string;
  createTime: number;
  isDefault?: boolean; 
  platformConfigVersion?: string | null; // Version of platform config it's based on
  modules?: PermModule[]; // Modules for this specific version
}

export interface PermModule {
  id: string | number; // Can be string UUID or number, keep consistent with your backend
  originalModuleId?: string | number; 
  moduleName: string;
  parentId: string | number | null;
  moduleType: 1 | 2; // 1=功能权限 2=数据权限
  sort: number;
  icon?: string;
  nodes?: PermNode[];
}

export interface PermNode {
  id: string | number;
  originalNodeId?: string | number;
  nodeName: string;
  moduleId: string | number;
  nodeType: 1 | 2 | 3; // 1=菜单项 2=操作按钮 3=数据字段
  dataScope?: 1 | 2; // 1=查看 2=编辑 (when nodeType=3)
  permType: 0 | 1; // 0:操作权限 1:数据权限
  permId?: string | number; // 绑定操作权限ID (sys_perms.id)
  dataPolicyId?: string | number; // 绑定数据权限ID (sys_data_policy.id)
  isBasic?: 0 | 1; // 0=自定义 1=系统预置
  sort: number;
}

export interface BaseOperationPermission {
  id: string | number;
  permsCode: string;
  permsName: string;
}

export interface BaseDataPolicy {
  id: string | number;
  policyCode: string;
  policyName: string;
}

// --- Mock API Functions ---

// Get all base system template definitions (platform, enterprise, agent)
export const getSystemTemplateDefinitions = async (): Promise<SystemTemplateIdentifier[]> => {
  return Promise.resolve([
    { type: 'platform', name: '平台超级租户模板', templateCode: 'SYS_PLATFORM_DEFAULT', description: '平台超级管理员使用的权限模板', userType: 'platform' },
    { type: 'enterprise', name: '标准企业模板套件', templateCode: 'SYS_ENT_DEFAULT', description: '新企业租户默认分配的权限模板', userType: 'enterprise' },
    { type: 'agent', name: '标准代理所模板套件', templateCode: 'SYS_AGENT_DEFAULT', description: '新代理所租户默认分配的权限模板', userType: 'agent' },
  ]);
};

// Get versions for a specific system template type (by code)
export const getTemplateVersions = async (templateCode: string): Promise<TemplateVersionData[]> => {
  console.log(`API: Fetching versions for ${templateCode}`);
  // Mock data
  if (templateCode === 'SYS_ENT_DEFAULT') {
    return Promise.resolve([
      { version: 'v2.1.0', createTime: Date.now() - 100000, isDefault: true, description: "企业模板2.1版本", platformConfigVersion: 'v1.0_platform', modules: [] },
      { version: 'v2.0.0', createTime: Date.now() - 2000000, description: "企业模板2.0大版本", modules: [] },
    ]);
  } else if (templateCode === 'SYS_PLATFORM_DEFAULT') {
    return Promise.resolve([
      { version: 'v1.5.0', createTime: Date.now() - 50000, isDefault: true, description: "平台模板v1.5", modules: [] },
    ]);
  }
  return Promise.resolve([{ version: 'v1.0.0', createTime: Date.now(), isDefault: true, modules: [] }]);
};

// Get module/node configuration for a specific template and version
export const getTemplateVersionConfig = async (templateCode: string, version: string): Promise<PermModule[]> => {
  console.log(`API: Fetching config for ${templateCode} - ${version}`);
  // This would return the detailed PermModule[] with their PermNode[]
  // For now, returning a sample structure based on previous mock data
  let modules: PermModule[] = [];
  if (templateCode === 'SYS_ENT_DEFAULT' && version === 'v2.1.0') {
    modules = [
      { id: 'ent_mod_1', moduleName: '企业数据看板', parentId: null, moduleType: 1, sort: 1, icon: 'Monitor', nodes: [
        {id: 'ent_node_101', moduleId:'ent_mod_1', nodeName: '查看企业看板', nodeType:1, permType:0, permId:'perm_view_dashboard', sort:1}
      ]},
      { id: 'ent_mod_2', moduleName: '客户管理 (企业)', parentId: null, moduleType: 1, sort: 2, icon: 'OfficeBuilding', nodes: [
        {id: 'ent_node_201',moduleId:'ent_mod_2', nodeName: '新增客户', nodeType:2, permType:0, permId:'perm_add_client', sort:1},
        {id: 'ent_node_202',moduleId:'ent_mod_2', nodeName: '客户数据字段', nodeType:3, permType:1, dataPolicyId:'dp_client_field', dataScope:1, sort:2},
      ] },
    ];
  }
  return Promise.resolve(modules);
};

// Save (update) the module/node configuration for a specific template and version
export const saveTemplateVersionConfig = async (templateCode: string, version: string, modules: PermModule[]): Promise<{success: boolean}> => {
  console.log(`API: Saving config for ${templateCode} - ${version}`, modules);
  return Promise.resolve({ success: true });
};

// Update base info for a system template (e.g., description)
export const updateSystemTemplateBaseInfo = async (templateIdentifier: SystemTemplateIdentifier): Promise<{success: boolean}> => {
  console.log("API: Updating system template base info", templateIdentifier);
  return Promise.resolve({ success: true });
}

// Add a new version for a system template
export const addNewTemplateVersion = async (templateCode: string, newVersion: TemplateVersionData): Promise<TemplateVersionData> => {
  console.log(`API: Adding new version ${newVersion.version} for ${templateCode}`, newVersion);
  // return the created version, possibly with server-generated fields like createTime if not provided
  return Promise.resolve({ ...newVersion, createTime: Date.now() }); 
}

// Delete a specific version of a system template
export const deleteTemplateVersion = async (templateCode: string, version: string): Promise<{success: boolean}> => {
  console.log(`API: Deleting version ${version} for ${templateCode}`);
  return Promise.resolve({ success: true });
}

// Set a version as default for a system template type
export const setDefaultTemplateVersion = async (templateCode: string, version: string): Promise<{success: boolean}> => {
  console.log(`API: Setting ${version} as default for ${templateCode}`);
  return Promise.resolve({ success: true });
}

// Dummy fetch for base permissions and data policies (reuse from roleApi or define here)
export const fetchAllBaseOperationPermissions = async (): Promise<BaseOperationPermission[]> => {
    return [
        { id: 'perm_view_dashboard', permsCode: 'VIEW_DASHBOARD', permsName: '查看数据看板' },
        { id: 'perm_add_client', permsCode: 'ADD_CLIENT', permsName: '新增客户' },
    ];
};
export const fetchAllBaseDataPolicies = async (): Promise<BaseDataPolicy[]> => {
    return [
        { id: 'dp_client_field', policyCode: 'DP_CLIENT_FIELD', policyName: '客户字段数据策略' },
    ];
}; 