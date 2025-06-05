/**
 * 企业画像模块类型定义
 */

// 企业智能画像模块类型定义

// 企业基本信息
export interface CompanyInfo {
  id: string;
  name: string;
  establishmentYear: number;
  registeredCapital: string;
  legalRepresentative: string;
  location: string;
  overallScore: number;
  description: string;
  industry: string;
  logoUrl?: string;
}

// 企业列表查询参数
export interface CompanyListParams {
  keyword?: string
  industry?: string
  patentCount?: [number, number]
  page: number
  pageSize: number
  sortField?: string
  sortOrder?: 'asc' | 'desc'
}

// 企业列表查询结果
export interface CompanyListResult {
  total: number
  items: CompanyInfo[]
}

// 行业定义
export interface Industry {
  code: string
  name: string
  parentCode?: string
  level?: number
}

// 企业列表项
export interface CompanyListItem {
  id: string
  name: string
  industry: string
  patentCount: number
  establishYear: number
  location: string
  techScore: number
  lastUpdated: string
}

// 企业列表响应
export interface CompanyListResponse {
  total: number
  list: CompanyListItem[]
}

// 能力维度定义
export interface Dimension {
  name: string;
  key: string;
  score: number;
  description?: string;
}

// 雷达图数据
export interface RadarData {
  dimensions: Dimension[];
  companyScores: number[];
  industryAvg: number[];
}

// 专利分类数据
export interface PatentCategory {
  name: string;
  value: number;
  percentage?: number;
}

// 专利趋势数据（按年份）
export interface PatentTrend {
  year: number;
  count: number;
  predicted?: boolean;
}

// 关键词数据
export interface Keyword {
  text: string;
  value: number;
  category?: string;
}

// 企业详情综合数据
export interface CompanyDetail extends CompanyInfo {
  radarData: RadarData;
  patentCategories: PatentCategory[];
  patentTrends: PatentTrend[];
  keywords: Keyword[];
}

// 导出报告配置
export interface ExportConfig {
  title: string;
  sections: string[];
  format: 'pdf' | 'docx' | 'pptx';
}

// 导出报告返回结果
export interface ExportReportResult {
  url: string; // 报告下载链接
  fileUrl: string;
  expiresAt: string;
  fileName: string;
}

// API请求返回的通用数据结构
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

// 雷达图数据类型定义
export interface RadarIndicator {
  name: string;
  max: number;
}

export interface RadarSeriesItem {
  name: string;
  value: number[];
  color?: string;
}

export interface RadarChartData {
  indicators: RadarIndicator[];
  series: RadarSeriesItem[];
}

// 企业能力维度详情
export interface DimensionDetail {
  name: string;
  score: number;
  industryAvg: number;
  description: string;
  keyInsights: string[];
  icon?: string;
}

// 能力提升建议
export interface ImprovementSuggestion {
  title: string;
  content: string;
  steps: string[];
}

// 企业画像数据
export interface EnterprisePortrait {
  enterpriseId: string;
  enterpriseName: string;
  reportDate: string;
  radarData: RadarChartData;
  dimensionDetails: DimensionDetail[];
  suggestions: Suggestion[];
  industryRanking: IndustryRanking;
  overallScore?: number;
  industry?: string;
  trendData?: TrendData;
  forecastData?: ForecastData;
  tags?: TagItem[];
  relationGraph?: EnterpriseRelationGraph;
}

// 企业关系图谱节点
export interface EnterpriseRelationNode {
  id: string;
  name: string;
  type: 'enterprise' | 'person' | 'patent' | 'project';
  size?: number;
  category?: number;
  properties?: Record<string, any>;
}

// 企业关系图谱连接
export interface GraphEdge {
  source: string;
  target: string;
  value: number;
  label?: string;
  properties?: Record<string, any>;
}

// 企业关系图谱数据
export interface EnterpriseRelationGraph {
  nodes: EnterpriseRelationNode[];
  edges: GraphEdge[];
  categories?: { name: string }[];
}

// 改进建议
export interface Suggestion {
  dimension: string;
  priority: 'high' | 'medium' | 'low';
  content: string;
  actions: string[];
  icon?: string;
  impactScore?: number;
  implementationDifficulty?: 'easy' | 'moderate' | 'difficult';
}

// 企业行业排名
export interface IndustryRanking {
  percentile: number;
  baseline: string;
  position: 'leader' | 'follower' | 'laggard';
  totalCompanies?: number;
  rank?: number;
  trend?: 'up' | 'down' | 'stable';
  comparisonYear?: number;
}

// 企业趋势数据
export interface TrendData {
  timePoints: string[];
  dimensions: {
    name: string;
    data: number[];
  }[];
}

// 预测数据
export interface ForecastData {
  timePoints: string[];
  dimensions: {
    name: string;
    actual: number[];
    forecast: number[];
  }[];
  confidence: number;
  methodology?: string;
  lastUpdated?: string;
}

// 标签项
export interface TagItem {
  id: string;
  name: string;
  category: string;
  weight: number;
  source?: string;
  confidence?: number;
}

// 行业对比数据
export interface IndustryCompareData {
  industryName: string;
  averageScores: {
    dimension: string;
    score: number;
  }[];
  companyRanks: {
    dimension: string;
    rank: number;
    totalCompanies: number;
  }[];
  benchmarkCompanies?: {
    id: string;
    name: string;
    scores: {
      dimension: string;
      score: number;
    }[];
  }[];
}

// 技术关键词参数
export interface TechKeywordsParams {
  enterpriseId: string;
  limit?: number;
  category?: string;
  yearRange?: [number, number];
}

// 导出报告参数
export interface ExportReportParams {
  enterpriseId: string;
  format: 'pdf' | 'xlsx';
  sections?: string[];
  templateId?: string;
  includeComparison?: boolean;
}

// 企业关系类型
export type EnterpriseRelationType = 
  | 'customer'
  | 'supplier'
  | 'competitor'
  | 'partner'
  | 'investor'
  | 'subsidiary'
  | 'parentCompany';

// 企业关系
export interface EnterpriseRelation {
  sourceId: string;
  sourceName: string;
  targetId: string;
  targetName: string;
  relationType: EnterpriseRelationType;
  strength: number;
  establishedDate?: string;
  description?: string;
}

// 定义 Company 类型（为了兼容已有代码）
export type Company = CompanyInfo; 