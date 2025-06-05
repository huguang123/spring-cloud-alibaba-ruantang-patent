import request from '@/utils/request';

/**
 * 获取技术领域树
 * @returns {Promise<any>}
 */
export const getDomains = () => {
  return request({
    url: '/prom/api/tech-domains',
    method: 'get'
  });
};

/**
 * 创建技术领域
 * @param {Object} domain 领域数据
 * @returns {Promise<any>}
 */
export const createDomain = (domain: any) => {
  return request({
    url: '/prom/api/tech-domains',
    method: 'post',
    data: domain
  });
};

/**
 * 更新技术领域
 * @param {Object} domain 领域数据
 * @returns {Promise<any>}
 */
export const updateDomain = (domain: any) => {
  return request({
    url: `/prom/api/tech-domains/${domain.id}`,
    method: 'put',
    data: domain
  });
};

/**
 * 删除技术领域
 * @param {string} id 领域ID
 * @returns {Promise<any>}
 */
export const deleteDomain = (id: string) => {
  return request({
    url: `/prom/api/tech-domains/${id}`,
    method: 'delete'
  });
};

/**
 * 获取技术领域详情
 * @param {string} id 领域ID
 * @returns {Promise<any>}
 */
export const getDomainDetail = (id: string) => {
  return request({
    url: `/prom/api/tech-domains/${id}`,
    method: 'get'
  });
};

/**
 * 根据父级ID获取子技术领域
 * @param {string|null} parentId 父级ID
 * @returns {Promise<any>}
 */
export const getChildDomains = (parentId: string | null = null) => {
  return request({
    url: '/prom/api/tech-domains/children',
    method: 'get',
    params: parentId ? { parentId } : {}
  });
};

/**
 * 搜索技术领域
 * @param {string} domainName 领域名称
 * @returns {Promise<any>}
 */
export const searchDomains = (domainName: string) => {
  return request({
    url: '/prom/api/tech-domains/search',
    method: 'get',
    params: { domainName }
  });
};

/**
 * 获取领域的文档模板列表
 * @param {string} domainId 领域ID
 * @returns {Promise<any>}
 */
export const getDomainTemplates = (domainId: string) => {
  return request({
    url: '/prom/api/doc-templates/by-domain',
    method: 'get',
    params: { domainId }
  });
};

/**
 * 创建文档模板
 * @param {Object} template 模板数据
 * @returns {Promise<any>}
 */
export const createDocTemplate = (template: any) => {
  return request({
    url: '/prom/api/doc-templates',
    method: 'post',
    data: template
  });
};

/**
 * 更新文档模板
 * @param {Object} template 模板数据
 * @returns {Promise<any>}
 */
export const updateDocTemplate = (template: any) => {
  return request({
    url: `/prom/api/doc-templates/${template.id}`,
    method: 'put',
    data: template
  });
};

/**
 * 删除文档模板
 * @param {string} id 模板ID
 * @returns {Promise<any>}
 */
export const deleteDocTemplate = (id: string) => {
  return request({
    url: `/prom/api/doc-templates/${id}`,
    method: 'delete'
  });
};

/**
 * 保存领域章节
 * @param {string} domainId 领域ID
 * @param {Object} section 章节数据
 * @returns {Promise<any>}
 */
export const saveDomainSection = (domainId: string, section: any) => {
  return request({
    url: `/prom/api/doc-templates/${domainId}/sections`,
    method: section.id ? 'put' : 'post',
    data: section
  });
};

/**
 * 删除领域章节
 * @param {string} templateId 模板ID
 * @param {string} sectionId 章节ID
 * @returns {Promise<any>}
 */
export const deleteDomainSection = (templateId: string, sectionId: string) => {
  return request({
    url: `/prom/api/doc-templates/${templateId}/sections/${sectionId}`,
    method: 'delete'
  });
};

/**
 * 获取提示词模板列表
 * @param {Object} params 查询参数
 * @returns {Promise<any>}
 */
export const getPromptTemplates = (params: any = {}) => {
  return request({
    url: '/prom/api/prompt-templates',
    method: 'get',
    params
  });
};

/**
 * 创建提示词模板
 * @param {Object} template 模板数据
 * @returns {Promise<any>}
 */
export const createPromptTemplate = (template: any) => {
  return request({
    url: '/prom/api/prompt-templates',
    method: 'post',
    data: template
  });
};

/**
 * 更新提示词模板
 * @param {Object} template 模板数据
 * @returns {Promise<any>}
 */
export const updatePromptTemplate = (template: any) => {
  return request({
    url: `/prom/api/prompt-templates/${template.id}`,
    method: 'put',
    data: template
  });
};

/**
 * 删除提示词模板
 * @param {string} id 模板ID
 * @returns {Promise<any>}
 */
export const deletePromptTemplate = (id: string) => {
  return request({
    url: `/prom/api/prompt-templates/${id}`,
    method: 'delete'
  });
};

/**
 * 生成文档
 * @param {Object} params 生成参数
 * @returns {Promise<any>}
 */
export const generateDocument = (params: any) => {
  return request({
    url: '/prom/api/doc-generate',
    method: 'post',
    data: params
  });
};

/**
 * 保存文档章节内容
 * @param {string} docId 文档ID
 * @param {string} sectionId 章节ID
 * @param {string} content 章节内容
 * @returns {Promise<any>}
 */
export const saveDocumentSection = (docId: string, sectionId: string, content: string) => {
  return request({
    url: `/prom/api/documents/${docId}/sections/${sectionId}`,
    method: 'put',
    data: { content }
  });
};

/**
 * 重新生成文档章节
 * @param {string} docId 文档ID
 * @param {string} sectionId 章节ID
 * @returns {Promise<any>}
 */
export const regenerateDocumentSection = (docId: string, sectionId: string) => {
  return request({
    url: `/prom/api/documents/${docId}/sections/${sectionId}/regenerate`,
    method: 'post'
  });
};

/**
 * 获取文档历史记录
 * @param {string} docId 文档ID
 * @returns {Promise<any>}
 */
export const getDocumentHistory = (docId: string) => {
  return request({
    url: `/prom/api/documents/${docId}/history`,
    method: 'get'
  });
};

/**
 * 导出文档
 * @param {string} docId 文档ID
 * @param {string} format 导出格式(docx, pdf)
 * @returns {Promise<any>}
 */
export const exportDocument = (docId: string, format: string = 'docx') => {
  return request({
    url: `/prom/api/documents/${docId}/export`,
    method: 'get',
    params: { format },
    responseType: 'blob'
  });
};

/**
 * 导出Word文档
 * @param {Object} data 导出数据
 * @returns {Promise<any>}
 */
export const exportWordDocument = (data: {
  title?: string;
  fileName?: string;
  sections: Array<{
    sectionName: string;
    content: string;
    sortOrder: number;
  }>;
}) => {
  return request({
    url: '/prom/api/doc-generate/export-word',
    method: 'post',
    data,
    responseType: 'blob'
  });
}; 