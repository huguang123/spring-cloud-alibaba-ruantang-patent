import request from '@/utils/request'
import type { 
  Company, 
  CompanyListParams, 
  CompanyListResult,
  RadarData,
  PatentCategory,
  TrendData,
  ForecastData,
  TagItem,
  TechKeywordsParams,
  IndustryCompareData,
  ExportReportParams,
  ExportReportResult,
  EnterprisePortrait,
  EnterpriseRelationGraph
} from '@/types/portrait'

/**
 * 获取企业列表
 * @param params 查询参数
 */
export function getCompanyList(params: CompanyListParams) {
  return request<CompanyListResult>({
    url: '/api/portrait/company/list',
    method: 'get',
    params
  })
}

/**
 * 获取企业详情
 * @param id 企业ID
 */
export function getCompanyDetail(id: string) {
  return request<Company>({
    url: `/api/portrait/company/${id}`,
    method: 'get'
  })
}

/**
 * 获取企业能力雷达图数据
 * @param id 企业ID
 */
export function getCompanyRadar(id: string) {
  return request<RadarData>({
    url: `/api/portrait/company/${id}/radar`,
    method: 'get'
  })
}

/**
 * 获取企业专利类别分布
 * @param id 企业ID
 */
export function getPatentCategories(id: string) {
  return request<PatentCategory[]>({
    url: `/api/portrait/company/${id}/patent-categories`,
    method: 'get'
  })
}

/**
 * 获取企业专利申请趋势数据
 * @param id 企业ID
 */
export function getPatentTrend(id: string) {
  return request<TrendData>({
    url: `/api/portrait/company/${id}/patent-trend`,
    method: 'get'
  })
}

/**
 * 获取企业专利申请预测数据
 * @param id 企业ID
 */
export function getPatentForecast(id: string) {
  return request<ForecastData>({
    url: `/api/portrait/company/${id}/patent-forecast`,
    method: 'get'
  })
}

/**
 * 获取企业技术关键词
 * @param id 企业ID
 * @param params 关键词参数
 */
export function getTechKeywords(id: string, params?: TechKeywordsParams) {
  return request<TagItem[]>({
    url: `/api/portrait/company/${id}/tech-keywords`,
    method: 'get',
    params
  })
}

/**
 * 获取企业行业对比数据
 * @param id 企业ID
 */
export function getIndustryCompare(id: string) {
  return request<IndustryCompareData>({
    url: `/api/portrait/company/${id}/industry-compare`,
    method: 'get'
  })
}

/**
 * 导出企业分析报告
 * @param id 企业ID
 * @param data 导出参数
 */
export function exportReport(id: string, data: ExportReportParams) {
  return request<ExportReportResult>({
    url: `/api/portrait/company/${id}/export-report`,
    method: 'post',
    data
  })
}

/**
 * 获取行业列表
 */
export function getIndustryList() {
  return request({
    url: '/api/portrait/industries',
    method: 'get'
  })
}

/**
 * 获取企业画像完整数据
 * @param enterpriseId 企业ID
 */
export function getEnterprisePortrait(enterpriseId: string) {
  return request<EnterprisePortrait>({
    url: `/api/portrait/enterprise/${enterpriseId}/portrait`,
    method: 'get'
  })
}

/**
 * 获取企业关系图谱
 * @param enterpriseId 企业ID
 * @param depth 关系深度，默认为1
 */
export function getEnterpriseRelationGraph(enterpriseId: string, depth: number = 1) {
  return request<EnterpriseRelationGraph>({
    url: `/api/portrait/enterprise/${enterpriseId}/relation-graph`,
    method: 'get',
    params: { depth }
  })
}

/**
 * 导出企业画像报告
 * @param enterpriseId 企业ID
 * @param format 导出格式 'pdf' | 'xlsx'
 * @param sections 导出的章节，可选
 */
export function exportEnterpriseReport(enterpriseId: string, format: 'pdf' | 'xlsx', sections?: string[]) {
  return request<Blob>({
    url: `/api/portrait/enterprise/${enterpriseId}/export`,
    method: 'post',
    data: { format, sections },
    responseType: 'blob'
  })
}

/**
 * 获取企业标签
 * @param enterpriseId 企业ID
 */
export function getEnterpriseTags(enterpriseId: string) {
  return request<TagItem[]>({
    url: `/api/portrait/enterprise/${enterpriseId}/tags`,
    method: 'get'
  })
}

/**
 * 获取企业能力趋势
 * @param enterpriseId 企业ID
 * @param years 年限，默认为5年
 */
export function getEnterpriseTrend(enterpriseId: string, years: number = 5) {
  return request<TrendData>({
    url: `/api/portrait/enterprise/${enterpriseId}/trend`,
    method: 'get',
    params: { years }
  })
}

/**
 * 更新企业画像
 * @param enterpriseId 企业ID
 */
export function updateEnterprisePortrait(enterpriseId: string) {
  return request({
    url: `/api/portrait/enterprise/${enterpriseId}/update`,
    method: 'post'
  })
} 