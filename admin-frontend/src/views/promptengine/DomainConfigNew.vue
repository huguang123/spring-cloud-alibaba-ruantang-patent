<template>
  <div class="container mx-auto p-4 max-w-7xl flex flex-col flex-grow">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">技术领域配置</h1>
      <div class="flex space-x-2">
        <el-button 
          @click="refreshDomains" 
          class="bg-gray-200 text-gray-800 flex items-center"
        >
          <el-icon class="mr-1"><Refresh /></el-icon>
          <span>刷新</span>
        </el-button>
        <el-button 
          @click="showDomainModal = true" 
          type="primary"
          class="flex items-center"
        >
          <el-icon class="mr-1"><Plus /></el-icon>
          <span>新建领域</span>
        </el-button>
      </div>
    </div>
    
    <div class="flex flex-col md:flex-row gap-6 flex-grow">
      <!-- 左侧技术领域树形结构 -->
      <el-card class="w-full md:w-1/3 lg:w-1/3" shadow="never">
        <template #header>
          <div class="flex justify-between items-center">
            <h2 class="text-lg font-medium text-gray-900">技术领域树</h2>
            <div class="flex items-center">
              <el-input 
                v-model="searchQuery" 
                placeholder="搜索领域名称..." 
                class="mr-2 w-48"
                clearable
                @keyup.enter="searchDomainWithParents(searchQuery)"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button 
                @click="expandAllDomains"
                circle
                text
                title="展开全部"
              >
                <el-icon><ArrowDown /></el-icon>
              </el-button>
              <el-button 
                @click="collapseAllDomains"
                circle
                text
                title="折叠全部"
              >
                <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
          </div>
        </template>
        
        <div class="domain-tree-container custom-scrollbar">
          <div v-if="loading" class="flex justify-center items-center p-12">
            <el-icon class="is-loading mr-2"><Loading /></el-icon>
            <span class="text-gray-600">加载中...</span>
          </div>
          
          <div v-else-if="filteredDomains.length === 0" class="py-12 text-center">
            <el-empty description="没有找到技术领域">
              <template #description>
                <span>{{ searchQuery ? '没有符合搜索条件的技术领域' : '请点击"新建领域"按钮创建技术领域' }}</span>
              </template>
            </el-empty>
          </div>
          
          <div v-else class="space-y-1">
            <div 
              v-for="domain in filteredDomains.filter(d => d && !d.parentId)" 
              :key="domain.id" 
              class="tree-node border border-gray-200 rounded-md overflow-hidden"
              :class="{'active': selectedDomain && selectedDomain.id === domain.id}"
            >
              <!-- 一级领域 -->
              <div 
                @click="selectDomain(domain)" 
                class="flex items-center p-2 cursor-pointer"
              >
                <el-button 
                  v-if="hasChildren(domain.id)" 
                  @click.stop="toggleExpand(domain.id)" 
                  text
                  circle
                  class="mr-2"
                >
                  <el-icon>
                    <component :is="isExpanded(domain.id) ? 'ArrowDown' : 'ArrowRight'" />
                  </el-icon>
                </el-button>
                <span v-else class="mr-6"></span>
                
                <div class="flex-grow">
                  <div class="font-medium text-gray-900">{{ domain.domainName }}</div>
                </div>
                
                <div class="flex items-center">
                  <el-button 
                    @click.stop="addChildDomain(domain)" 
                    text
                    circle
                    title="添加子领域"
                  >
                    <el-icon><Plus /></el-icon>
                  </el-button>
                  <el-button 
                    @click.stop="editDomain(domain, $event)" 
                    text
                    circle
                    title="编辑"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button 
                    @click.stop="deleteDomain(domain)" 
                    text
                    circle
                    title="删除"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
              
              <!-- 二级领域 -->
              <div v-if="isExpanded(domain.id)" class="pl-5 pb-1">
                <div 
                  v-for="child in getDomainChildren(domain.id)" 
                  :key="child.id" 
                  class="mt-1 border-l-2 border-gray-200 pl-2"
                >
                  <div 
                    @click="selectDomain(child)" 
                    class="flex items-center p-2 rounded-md cursor-pointer hover:bg-gray-50"
                    :class="{'bg-blue-50': selectedDomain && selectedDomain.id === child.id}"
                  >
                    <el-button 
                      v-if="hasChildren(child.id)" 
                      @click.stop="toggleExpand(child.id)" 
                      text
                      circle
                      class="mr-2"
                    >
                      <el-icon>
                        <component :is="isExpanded(child.id) ? 'ArrowDown' : 'ArrowRight'" />
                      </el-icon>
                    </el-button>
                    <span v-else class="mr-6"></span>
                    
                    <div class="flex-grow">
                      <div class="font-medium text-gray-900">{{ child.domainName }}</div>
                    </div>
                    
                    <div class="flex items-center">
                      <el-button 
                        @click.stop="addChildDomain(child)" 
                        text
                        circle
                        title="添加子领域"
                      >
                        <el-icon><Plus /></el-icon>
                      </el-button>
                      <el-button 
                        @click.stop="editDomain(child, $event)" 
                        text
                        circle
                        title="编辑"
                      >
                        <el-icon><Edit /></el-icon>
                      </el-button>
                      <el-button 
                        @click.stop="deleteDomain(child)" 
                        text
                        circle
                        title="删除"
                      >
                        <el-icon><Delete /></el-icon>
                      </el-button>
                    </div>
                  </div>
                  
                  <!-- 三级领域 -->
                  <div v-if="isExpanded(child.id)" class="pl-5 mt-1">
                    <div 
                      v-for="grandchild in getDomainChildren(child.id)" 
                      :key="grandchild.id" 
                      @click="selectDomain(grandchild)" 
                      class="flex items-center p-2 rounded-md cursor-pointer hover:bg-gray-50 border-l-2 border-gray-200 pl-2"
                      :class="{'bg-blue-50': selectedDomain && selectedDomain.id === grandchild.id}"
                    >
                      <div class="flex-grow">
                        <div class="font-medium text-gray-900">{{ grandchild.domainName }}</div>
                      </div>
                      
                      <div class="flex items-center">
                        <el-button 
                          @click.stop="editDomain(grandchild, $event)" 
                          text
                          circle
                          title="编辑"
                        >
                          <el-icon><Edit /></el-icon>
                        </el-button>
                        <el-button 
                          @click.stop="deleteDomain(grandchild)" 
                          text
                          circle
                          title="删除"
                        >
                          <el-icon><Delete /></el-icon>
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 右侧详情信息 -->
      <div class="w-full md:w-2/3 lg:w-2/3">
        <div v-if="!selectedDomain" class="bg-white rounded-lg shadow-sm p-12 text-center">
          <el-empty description="请选择技术领域">
            <template #description>
              <p>从左侧技术领域树中选择一个领域，查看详细信息</p>
            </template>
          </el-empty>
        </div>
        
        <div v-else>
          <el-card class="mb-6" shadow="never">
            <template #header>
              <div class="card-header">
                <h2 class="text-lg font-medium text-gray-900">领域详细信息</h2>
              </div>
            </template>
            
            <div class="grid grid-cols-2 gap-6">
              <div>
                <h3 class="text-sm font-medium text-gray-700 mb-2">基本信息</h3>
                <div class="bg-gray-50 p-4 rounded border border-gray-200">
                  <div class="mb-3">
                    <label class="block text-xs text-gray-500">领域名称</label>
                    <div class="font-medium">{{ selectedDomain.domainName }}</div>
                  </div>
                  <div class="mb-3">
                    <label class="block text-xs text-gray-500">层级深度</label>
                    <div class="font-medium">{{ selectedDomain.level }} 级</div>
                  </div>
                  <div>
                    <label class="block text-xs text-gray-500">父级领域</label>
                    <div class="font-medium">
                      {{ selectedDomain.parentId ? (getDomainById(selectedDomain.parentId)?.domainName || '未知') : '无（顶级领域）' }}
                    </div>
                  </div>
                </div>
              </div>
              
              <div>
                <h3 class="text-sm font-medium text-gray-700 mb-2">领域描述</h3>
                <div class="bg-gray-50 p-4 rounded border border-gray-200 h-full">
                  <p class="text-gray-600">{{ selectedDomain.description || '暂无描述' }}</p>
                </div>
              </div>
            </div>
          </el-card>
          
          <el-card shadow="never">
            <template #header>
              <div class="flex justify-between items-center">
                <h2 class="text-lg font-medium text-gray-900">文档模板绑定</h2>
                <el-button 
                  @click="addDocTemplate" 
                  type="primary"
                  size="small"
                  class="flex items-center"
                >
                  <el-icon class="mr-1"><Plus /></el-icon>
                  <span>添加模板</span>
                </el-button>
              </div>
            </template>
            
            <div class="template-container custom-scrollbar">
              <div v-if="!domainTemplates || domainTemplates.length === 0" class="text-center py-8">
                <el-empty description="暂无绑定模板">
                  <template #description>
                    <p>点击"添加模板"按钮为当前领域绑定文档模板</p>
                  </template>
                </el-empty>
              </div>
              
              <div v-else class="space-y-4">
                <el-card 
                  v-for="(template, index) in domainTemplates" 
                  :key="index" 
                  class="border-gray-200 !border" 
                  shadow="hover"
                >
                  <div class="flex justify-between items-start mb-3">
                    <div>
                      <el-tag size="small" class="mb-1">{{ template.template_type }}</el-tag>
                      <h4 class="font-medium text-gray-900 mt-1">{{ template.template_name || '未命名模板' }}</h4>
                    </div>
                    <div class="flex space-x-2">
                      <el-button 
                        @click="editDocTemplate(template)" 
                        text
                        circle
                        title="编辑模板"
                      >
                        <el-icon><Edit /></el-icon>
                      </el-button>
                      <el-button 
                        @click="viewTemplateDetail(template)" 
                        text
                        circle
                        title="查看模板"
                        type="primary"
                      >
                        <el-icon><View /></el-icon>
                      </el-button>
                      <el-button 
                        @click="removeDomainTemplate(index)" 
                        text
                        circle
                        title="解除绑定"
                        type="danger"
                      >
                        <el-icon><Link /></el-icon>
                      </el-button>
                    </div>
                  </div>
                  
                  <div class="text-sm text-gray-600 mb-3">
                    <div class="flex items-center">
                      <i class="fas fa-tag text-gray-400 mr-2"></i>
                      <span>版本: {{ template.version || '1.0' }}</span>
                      <span class="mx-2">|</span>
                      <i class="fas fa-sitemap text-gray-400 mr-2"></i>
                      <span>绑定部分: {{ template.sections && Array.isArray(template.sections) ? template.sections.length : 0 }}个</span>
                    </div>
                  </div>
                  
                  <div
                    v-if="template.sections && Array.isArray(template.sections) && template.sections.length > 0"
                    class="bg-white p-3 border border-gray-200 rounded-md"
                  >
                    <div class="flex justify-between items-center mb-2">
                      <div class="text-xs font-medium text-gray-500">已绑定的章节：</div>
                      <el-button
                        @click="addNewChapter(template)"
                        type="primary"
                        size="small"
                        plain
                        class="!px-2 !py-1 text-xs"
                      >
                        <el-icon class="mr-1"><Plus /></el-icon> 添加章节
                      </el-button>
                    </div>
                    <div class="flex flex-wrap gap-2">
                      <div
                        v-for="section in sortedSections(template)"
                        :key="section.id"
                        class="group flex items-center px-2 py-1 bg-gray-100 text-gray-700 rounded-md text-xs"
                      >
                        <span>{{ section.section_type }}</span>
                        <div class="ml-2 flex space-x-1">
                          <el-button
                            @click.stop="viewChapterDetail(template, section)"
                            text
                            circle
                            size="small"
                            class="opacity-0 group-hover:opacity-100"
                            title="查看详情"
                            type="primary"
                          >
                            <el-icon><View /></el-icon>
                          </el-button>
                          <el-button 
                            @click.stop="editChapter(template, section)"
                            text
                            circle
                            size="small"
                            class="opacity-0 group-hover:opacity-100"
                            title="编辑章节"
                          >
                            <el-icon><Edit /></el-icon>
                          </el-button>
                          <el-button 
                            @click.stop="bindPromptTemplate(template, section)"
                            text
                            circle
                            size="small"
                            class="opacity-0 group-hover:opacity-100"
                            title="绑定提示词模板"
                            type="primary"
                          >
                            <el-icon><Link /></el-icon>
                          </el-button>
                          <el-button 
                            @click.stop="deleteChapter(template, section.id)"
                            text
                            circle
                            size="small"
                            class="opacity-0 group-hover:opacity-100"
                            title="删除章节"
                            type="danger"
                          >
                            <el-icon><Delete /></el-icon>
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-else class="flex justify-center mt-2">
                    <el-button 
                      @click="addNewChapter(template)"
                      class="bg-gray-100 hover:bg-gray-200 text-gray-700 flex items-center"
                    >
                      <el-icon class="mr-1"><Plus /></el-icon> 添加章节
                    </el-button>
                  </div>
                </el-card>
              </div>
              
              <div class="flex justify-end mt-6">
                <el-button 
                  @click="saveTemplateBindings" 
                  type="primary"
                >
                  保存模板绑定
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
    
    <!-- 各种模态框将在后续添加 -->
    <el-dialog
      v-model="showDomainModal"
      :title="editingDomain.id ? '编辑技术领域' : '创建技术领域'"
      width="500px"
      destroy-on-close
    >
      <el-form :model="editingDomain" label-position="top">
        <el-form-item 
          label="领域名称" 
          required
        >
          <el-input 
            v-model="editingDomain.domainName" 
            placeholder="请输入领域名称"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="父级领域">
          <el-select 
            v-model="editingDomain.parentId" 
            placeholder="无（顶级领域）"
            class="w-full"
            clearable
          >
            <el-option :value="null" label="无（顶级领域）"></el-option>
            <el-option 
              v-for="domain in domains.filter(d => d && d.id && d.id !== editingDomain.id && (!editingDomain.id || !isChildOf(d.id, editingDomain.id)))" 
              :key="domain.id" 
              :value="domain.id"
              :label="getIndent(domain.level) + domain.domainName"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="领域描述">
          <el-input 
            v-model="editingDomain.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入领域描述"
          ></el-input>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDomainModal = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="saveDomain"
            :disabled="!editingDomain.domainName"
          >
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 模板详情弹窗 -->
    <el-dialog
      v-model="showTemplateModal"
      title="模板详情"
      width="650px"
    >
      <div v-if="selectedTemplateDetail">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <div>
            <div class="text-sm font-medium text-gray-500 mb-1">模板类型</div>
            <el-tag>{{ selectedTemplateDetail.template_type }}</el-tag>
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div>
              <div class="text-sm font-medium text-gray-500 mb-1">模板名称</div>
              <div class="text-gray-900">{{ selectedTemplateDetail.template_name }}</div>
            </div>
            <div>
              <div class="text-sm font-medium text-gray-500 mb-1">版本</div>
              <div class="text-gray-900">{{ selectedTemplateDetail.version }}</div>
            </div>
          </div>
        </div>
        
        <div class="mb-6">
          <div class="text-sm font-medium text-gray-500 mb-1">包含章节</div>
          <div class="bg-gray-50 p-4 rounded-md border border-gray-200">
            <div v-if="!selectedTemplateDetail.sections || selectedTemplateDetail.sections.length === 0" class="text-gray-500 text-sm italic">
              模板未配置任何章节
            </div>
            <div v-else class="space-y-2">
              <div 
                v-for="section in selectedTemplateDetail.sections" 
                :key="section.id"
                class="flex items-center p-2 bg-white border border-gray-200 rounded-md"
              >
                <div class="flex-grow">
                  <span class="text-sm font-medium">{{ section.section_type }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div>
          <div class="text-sm font-medium text-gray-500 mb-1">关联信息</div>
          <div class="bg-gray-50 p-3 rounded-md border border-gray-200">
            <div class="flex items-center text-sm">
              <i class="fas fa-sitemap text-gray-400 mr-2"></i>
              <span>当前关联领域: {{ selectedDomain ? selectedDomain.domainName : '无' }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
    
    <!-- 添加模板弹窗 -->
    <el-dialog
      v-model="showTemplateAddModal"
      :title="editingTemplateId ? '编辑文档模板' : '添加文档模板'"
      width="500px"
      destroy-on-close
    >
      <el-form :model="editingTemplate" label-position="top">
        <el-form-item 
          label="模板类型" 
          required
        >
          <el-select 
            v-model="editingTemplate.template_type" 
            placeholder="请选择模板类型"
            class="w-full"
          >
            <el-option 
              v-for="type in templateTypes" 
              :key="type.id" 
              :value="type.typeName"
              :label="type.typeName"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item 
          label="模板名称" 
          required
        >
          <el-input 
            v-model="editingTemplate.template_name" 
            placeholder="请输入模板名称"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="版本">
          <el-input 
            v-model="editingTemplate.version" 
            placeholder="请输入版本号，如1.0"
          ></el-input>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showTemplateAddModal = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="saveDocTemplate"
            :disabled="!isTemplateFormValid"
          >
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 章节编辑模态框 -->
    <el-dialog
      v-model="showChapterModal"
      :title="isChapterEditing ? '编辑章节' : '添加章节'"
      width="500px"
      destroy-on-close
    >
      <div v-if="selectedDocTemplate" class="bg-blue-50 p-3 rounded-md border border-blue-200 mb-4">
        <div class="text-sm font-medium text-blue-800">当前模板：{{ selectedDocTemplate.template_name }}</div>
      </div>
      
      <el-form :model="editingSection" label-position="top">
        <el-form-item 
          label="章节名称" 
          required
        >
          <el-input 
            v-model="editingSection.section_type" 
            placeholder="请输入章节名称"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="排序顺序">
          <el-input-number
            v-model="editingSection.sort_order"
            :min="1"
            placeholder="请输入排序顺序（数字越小越靠前）"
            class="w-full"
          ></el-input-number>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showChapterModal = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="saveChapter"
            :disabled="!editingSection.section_type"
          >
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 章节详情模态框 -->
    <el-dialog
      v-model="showChapterDetailModal"
      title="章节详情"
      width="550px"
    >
      <div v-if="chapterDetailData">
        <div class="grid grid-cols-2 gap-4 mb-4">
          <div>
            <div class="text-sm font-medium text-gray-500 mb-1">章节名称</div>
            <div class="text-gray-900">{{ chapterDetailData.section_type }}</div>
          </div>
          <div>
            <div class="text-sm font-medium text-gray-500 mb-1">排序顺序</div>
            <div class="text-gray-900">{{ chapterDetailData.sort_order || '-' }}</div>
          </div>
        </div>
        
        <div class="mb-4">
          <div class="text-sm font-medium text-gray-500 mb-1">绑定状态</div>
          <div class="text-gray-900">
            <el-tag v-if="chapterDetailData.prompt_id" type="success" size="small">
              已绑定提示词模板
            </el-tag>
            <el-tag v-else type="info" size="small">
              未绑定提示词模板
            </el-tag>
          </div>
        </div>
        
        <div v-if="chapterDetailData.prompt_template" class="bg-gray-50 p-4 rounded-md border border-gray-200">
          <div class="text-sm font-medium text-gray-700 mb-2">绑定的提示词模板信息</div>
          <div class="grid grid-cols-2 gap-2 mb-2 text-sm">
            <div>
              <div class="text-xs text-gray-500">模板名称</div>
              <div>{{ chapterDetailData.prompt_template.template_name }}</div>
            </div>
            <div>
              <div class="text-xs text-gray-500">版本</div>
              <div>{{ chapterDetailData.prompt_template.version }}</div>
            </div>
          </div>
          <div class="text-xs text-gray-500 mb-1">模板内容</div>
          <div class="text-xs bg-white p-2 border border-gray-200 rounded max-h-32 overflow-y-auto">
            {{ chapterDetailData.prompt_template.content }}
          </div>
        </div>
      </div>
    </el-dialog>
    
    <!-- 提示词模板绑定模态框 -->
    <el-dialog
      v-model="showPromptBindModal"
      title="绑定提示词模板"
      width="600px"
      class="max-h-[90vh]"
    >
      <div class="bg-blue-50 p-3 rounded-md border border-blue-200 mb-4">
        <div class="text-sm">
          <div class="font-medium text-blue-800">当前章节：{{ editingSection.section_type }}</div>
          <div class="text-blue-600 text-xs mt-1">文档模板：{{ selectedDocTemplate ? selectedDocTemplate.template_name : '' }}</div>
        </div>
      </div>
      
      <el-form label-position="top" @submit.prevent>
        <el-form-item label="选择提示词模板">
          <!-- 已选择的固定模板 -->
          <div v-if="editingSection.prompt_id" class="mb-3 p-3 bg-blue-50 rounded-md border border-blue-200">
            <div class="flex justify-between items-start">
              <div>
                <div class="text-sm font-medium text-blue-800">
                  {{ getSelectedPromptTemplateName() }}
                </div>
                <div class="text-xs text-blue-600 mt-1">
                  <el-tag size="small" type="info">{{ getSelectedPromptTemplateType() }}</el-tag>
                  <span class="ml-2">版本 {{ getSelectedPromptTemplateVersion() }}</span>
                </div>
              </div>
              <el-button 
                @click="removeSelectedPromptTemplate" 
                text
                type="danger"
                circle
                title="移除绑定"
              >
                <el-icon><Close /></el-icon>
              </el-button>
            </div>
          </div>
          
          <div class="mb-3">
            <el-input 
              v-model="promptTemplateSearchQuery" 
              placeholder="搜索提示词模板名称..." 
              clearable
              @input="searchPromptTemplates"
              @keydown.enter.prevent="searchPromptTemplatesFromAPI"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
              <template #append>
                <el-button @click.prevent="searchPromptTemplatesFromAPI">搜索</el-button>
              </template>
            </el-input>
          </div>
          
          <div v-if="loading" class="flex justify-center items-center py-4">
            <el-icon class="is-loading mr-2"><Loading /></el-icon>
            <span class="text-gray-600">加载中...</span>
          </div>
          <div v-else-if="filteredPromptTemplates.length === 0" class="py-4 text-center text-gray-500">
            没有可用的提示词模板
          </div>
          <div v-else class="border border-gray-300 rounded-md overflow-hidden max-h-80 overflow-y-auto">
            <div 
              v-for="template in filteredPromptTemplates" 
              :key="template.id" 
              @click="editingSection.prompt_id = template.id"
              class="p-3 border-b border-gray-200 last:border-b-0 hover:bg-gray-50 cursor-pointer"
              :class="{'bg-blue-50': editingSection.prompt_id === template.id}"
            >
              <div class="flex items-center">
                <el-radio v-model="editingSection.prompt_id" :label="template.id" class="!m-0 mr-3"></el-radio>
                <div class="flex-grow">
                  <div class="font-medium text-sm">{{ template.template_name || '未命名模板' }}</div>
                  <div class="flex items-center text-xs text-gray-500 mt-1">
                    <el-tag size="small" type="info">{{ template.section_type }}</el-tag>
                    <span class="mx-1.5">·</span>
                    <span>版本 {{ template.version }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPromptBindModal = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="savePromptBinding"
            :disabled="!editingSection.prompt_id"
          >
            保存绑定
          </el-button>
        </span>
      </template>
    </el-dialog>



  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElTree, ElButton, ElInput, ElForm, ElFormItem, ElDialog, ElCard, ElTable, ElTableColumn, ElPagination, ElTag, ElPopconfirm, ElSelect, ElOption, ElSwitch, ElTooltip, ElIcon, ElRow, ElCol, ElDivider, ElEmpty, ElLoading } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh, FolderOpened, Folder, Document, Setting, View, ArrowRight } from '@element-plus/icons-vue'
import { handleApiError, handleApiResponse } from '@/utils/errorHandler'

// 将在后续步骤添加状态和方法逻辑

// API基础路径
const baseUrl = '/prom/api/tech-domains'
const docTemplateUrl = '/prom/api/doc-templates'
const docSectionTemplateUrl = '/prom/api/doc-section-templates'
const docTemplateTypeUrl = '/prom/api/doc-template-types'
const promptTemplateUrl = '/prom/api/prom-templates'

// 状态变量
const loading = ref(false)
const domains = ref<any[]>([])
const selectedDomain = ref<any>(null)
const showDomainModal = ref(false)
const searchQuery = ref('')
const expandedDomains = ref<string[]>([])

// 领域相关变量
const domainTemplates = ref<any[]>([])
const showTemplateModal = ref(false)
const selectedTemplateDetail = ref<any>(null)
const templateTypes = ref<any[]>([])

// 模板编辑相关状态
const showTemplateAddModal = ref(false)
const editingTemplateId = ref<string | null>(null)
const editingTemplate = reactive({
  template_type: '',
  template_name: '',
  version: '1.0',
  sections: [] as any[]
})

// 章节编辑相关状态
const isChapterEditing = ref(false)
const showChapterModal = ref(false)
const showChapterDetailModal = ref(false)
const showPromptBindModal = ref(false)
const chapterDetailData = ref<any>(null)

// 章节编辑数据对象
const editingSection = reactive({
  id: null as string | null,
  doc_template_id: null as string | null,
  section_type: '',
  prompt_id: null as string | null,
  sort_order: 10
})

// 提示词模板相关
const selectedDocTemplate = ref<any>(null)
const availablePromptTemplates = ref<any[]>([])
const promptTemplateSearchQuery = ref('')
const filteredPromptTemplates = ref<any[]>([])

// 编辑域对象
const editingDomain = reactive({
  id: null as string | null,
  domainName: '',
  parentId: null as string | null,
  level: 1,
  description: ''
})

// 计算属性
// 过滤后的域列表
const filteredDomains = computed(() => {
  if (!domains.value || !Array.isArray(domains.value)) {
    return []
  }
  
  if (!searchQuery.value) return domains.value
  
  const query = searchQuery.value.toLowerCase()
  return domains.value.filter(domain => 
    domain.domainName.toLowerCase().includes(query) || 
    (domain.description && domain.description.toLowerCase().includes(query))
  )
})

// 模板表单验证
const isTemplateFormValid = computed(() => {
  return Boolean(editingTemplate.template_type && editingTemplate.template_name)
})

// 获取HTTP请求头
function getRequestHeaders() {
  // 获取token
  const token = localStorage.getItem('auth_token') || sessionStorage.getItem('token')
  
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  }
}

// 使用消息提示封装ElMessage
function showMessage(message: string, type: 'success' | 'warning' | 'error' = 'success') {
  ElMessage({
    message,
    type,
    duration: 2000
  })
}

// 域相关方法
// 获取域的子节点
function getDomainChildren(parentId: string) {
  return domains.value.filter(domain => domain.parentId === parentId)
}

// 检查是否有子节点
function hasChildren(parentId: string) {
  return domains.value.some(domain => domain.parentId === parentId)
}

// 根据ID获取域
function getDomainById(id: string) {
  if (!id || !Array.isArray(domains.value)) return null
  return domains.value.find(d => d.id === id)
}

// 切换展开状态
function toggleExpand(domainId: string) {
  const index = expandedDomains.value.indexOf(domainId)
  if (index === -1) {
    expandedDomains.value.push(domainId)
  } else {
    expandedDomains.value.splice(index, 1)
  }
}

// 判断是否展开
function isExpanded(domainId: string) {
  return expandedDomains.value.includes(domainId)
}

// 选择域
function selectDomain(domain: any) {
  if (!domain) return
  
  // 先清空当前选中的领域下的模板数据
  domainTemplates.value = []
  
  selectedDomain.value = domain
  
  // 如果是子领域，需要获取父级信息
  if (domain.parentId) {
    const parentDomain = getDomainById(domain.parentId)
    if (parentDomain) {
      selectedDomain.value = {
        ...domain,
        parentDomain
      }
    }
  }
  
  // 展开选中的领域
  if (hasChildren(domain.id)) {
    if (!expandedDomains.value.includes(domain.id)) {
      expandedDomains.value.push(domain.id)
    }
  }
  
  // 加载该领域下的文档模板
  loadDomainTemplates(domain.id)
}

// 加载域关联的文档模板
async function loadDomainTemplates(domainId: string) {
  if (!domainId) return
  
  try {
    loading.value = true
    
    // 调用实际API获取领域关联的文档模板
    const response = await fetch(`${docTemplateUrl}/by-domain?domainId=${domainId}`, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 格式化返回数据
      domainTemplates.value = result.data.map((template: any) => ({
        id: template.id,
        template_type: template.templateTypeName,
        template_name: template.templateName,
        templateTypeId: template.templateTypeId,
        domainId: template.domainId,
        version: template.version || '1.0',
        sections: template.sections?.map((section: any) => ({
          id: section.id,
          section_type: section.sectionName,
          prompt_id: section.promptId,
          sort_order: section.sortOrder
        })) || []
      }))
    } else {
      throw new Error(result.message || '获取领域模板失败')
    }
  } catch (error: any) {
    handleApiError(error, '加载领域关联模板失败');
    // 确保在出错时至少是空数组
    domainTemplates.value = []
  } finally {
    loading.value = false
  }
}

// 添加子域
function addChildDomain(parentDomain: any) {
  editingDomain.id = null
  editingDomain.domainName = ''
  editingDomain.parentId = parentDomain.id
  editingDomain.level = parentDomain.level + 1
  editingDomain.description = ''
  
  showDomainModal.value = true
}

// 编辑域
function editDomain(domain: any, event?: Event) {
  // 阻止事件冒泡和默认行为
  if (event) {
    event.preventDefault()
    event.stopPropagation()
  }
  
  // 填充编辑表单
  editingDomain.id = domain.id
  editingDomain.domainName = domain.domainName
  editingDomain.parentId = domain.parentId
  editingDomain.level = domain.level
  editingDomain.description = domain.description || ''
  
  // 显示模态框
  showDomainModal.value = true
}

// 保存域
async function saveDomain() {
  if (!editingDomain.domainName) {
    showMessage('请输入领域名称', 'warning')
    return
  }
  
  try {
    loading.value = true
    
    // 更新level
    if (editingDomain.parentId) {
      const parentDomain = getDomainById(editingDomain.parentId)
      editingDomain.level = parentDomain ? parentDomain.level + 1 : 1
    } else {
      editingDomain.level = 1
    }
    
    const requestBody = {
      domainName: editingDomain.domainName,
      parentId: editingDomain.parentId,
      description: editingDomain.description || ''
    }
    
    let response
    let result
    
    if (editingDomain.id) {
      // 更新现有领域
      response = await fetch(`${baseUrl}/${editingDomain.id}`, {
        method: 'PUT',
        headers: getRequestHeaders(),
        body: JSON.stringify(requestBody)
      })
    } else {
      // 创建新领域
      response = await fetch(baseUrl, {
        method: 'POST',
        headers: getRequestHeaders(),
        body: JSON.stringify(requestBody)
      })
    }
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    result = await response.json()
    
    if (result.code === 200) {
      // 如果是创建新领域，服务器会返回新ID
      if (!editingDomain.id && result.data) {
        const newDomain = {...requestBody, id: result.data, level: editingDomain.level}
        domains.value.push(newDomain)
        
        // 如果是新建子领域，自动展开父级
        if (requestBody.parentId && !isExpanded(requestBody.parentId)) {
          toggleExpand(requestBody.parentId)
        }
      } else if (editingDomain.id) {
        // 更新现有领域
        const index = domains.value.findIndex(d => d.id === editingDomain.id)
        if (index !== -1) {
          const updatedDomain = {...domains.value[index], ...requestBody}
          domains.value[index] = updatedDomain
          
          // 如果更新的是当前选中的领域，更新选中的领域
          if (selectedDomain.value && selectedDomain.value.id === editingDomain.id) {
            selectedDomain.value = updatedDomain
          }
        }
      }
      
      showMessage(editingDomain.id ? '技术领域更新成功' : '技术领域创建成功')
    } else {
      throw new Error(result.message || (editingDomain.id ? '更新失败' : '创建失败'))
    }
    
    showDomainModal.value = false
  } catch (error: any) {
    handleApiError(error, editingDomain.id ? '更新技术领域失败' : '创建技术领域失败');
  } finally {
    loading.value = false
  }
}

// 判断是否是子节点
function isChildOf(potentialChildId: string, parentId: string) {
  if (!potentialChildId || !parentId) return false
  
  const domain = getDomainById(potentialChildId)
  if (!domain) return false
  
  if (domain.parentId === parentId) return true
  if (domain.parentId === null) return false
  
  return isChildOf(domain.parentId, parentId)
}

// 获取缩进
function getIndent(level: number) {
  if (!level || typeof level !== 'number') return ''
  return '  '.repeat(Math.max(0, level - 1))
}

// 删除领域
async function deleteDomain(domain: any) {
  if (hasChildren(domain.id)) {
    showMessage('无法删除含有子领域的领域，请先删除所有子领域', 'warning')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除技术领域"${domain.domainName}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    
    const response = await fetch(`${baseUrl}/${domain.id}`, {
      method: 'DELETE',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 从本地列表中移除
      const index = domains.value.findIndex(d => d.id === domain.id)
      if (index !== -1) {
        domains.value.splice(index, 1)
      }
      
      if (selectedDomain.value && selectedDomain.value.id === domain.id) {
        selectedDomain.value = null
      }
      
      showMessage('技术领域删除成功')
    } else {
      throw new Error(result.message || '删除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') { // 不是取消操作
      handleApiError(error, '删除技术领域失败');
    }
  } finally {
    loading.value = false
  }
}

// 搜索技术领域及其父级
async function searchDomainWithParents(domainName: string) {
  if (!domainName || domainName.trim() === '') {
    // 如果搜索为空，刷新全部数据
    refreshDomains()
    return
  }
  
  try {
    loading.value = true
    const searchUrl = `${baseUrl}/search-with-parents?domainName=${encodeURIComponent(domainName)}`
    
    const response = await fetch(searchUrl, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200 && result.data && result.data.length > 0) {
      // 处理搜索结果包括父级技术领域
      const foundDomains: any[] = []
      
      // 构建父级链
      result.data.forEach((domain: any) => {
        // 先添加当前域
        foundDomains.push({
          id: domain.id,
          domainName: domain.domainName,
          parentId: domain.parentId,
          parentName: domain.parentName,
          level: domain.level,
          description: domain.description
        })
        
        // 添加父级域
        let parent = domain.parent
        while (parent) {
          // 检查是否已存在以避免重复
          if (!foundDomains.some(d => d.id === parent.id)) {
            foundDomains.push({
              id: parent.id,
              domainName: parent.domainName,
              parentId: parent.parentId,
              parentName: parent.parentName,
              level: parent.level,
              description: parent.description
            })
          }
          parent = parent.parent
        }
      })
      
      // 更新搜索结果到域列表
      domains.value = foundDomains
      
      // 展开所有父级域
      foundDomains.forEach(domain => {
        if (domain.parentId && !expandedDomains.value.includes(domain.parentId)) {
          expandedDomains.value.push(domain.parentId)
        }
      })
      
      // 选择第一个匹配的域
      if (foundDomains.length > 0) {
        selectDomain(foundDomains[0])
      }
    } else {
      showMessage('未找到匹配的技术领域，请尝试其他关键词', 'warning')
    }
  } catch (error: any) {
    console.error('搜索技术领域失败:', error)
    showMessage(error.message || '搜索技术领域失败', 'error')
  } finally {
    loading.value = false
  }
}

// 刷新领域数据
async function refreshDomains() {
  try {
    loading.value = true
    
    // 确保domains是数组
    if (!Array.isArray(domains.value)) {
      domains.value = []
    }
    
    const response = await fetch(`${baseUrl}/tree`, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 将数据平铺为一维数组
      const rawDomains = flattenDomainTree(result.data)
      // 过滤无效数据
      domains.value = rawDomains.filter(d => d && d.id)
      
      // 默认展开一级领域
      expandedDomains.value = domains.value
        .filter(d => d.level === 1)
        .map(d => d.id)
    } else {
      // API返回错误，使用空数组
      domains.value = []
      throw new Error(result.message || '获取技术领域树失败')
    }
  } catch (error: any) {
    // 确保在出错时domains至少是空数组
    if (!Array.isArray(domains.value)) {
      domains.value = []
    }
    console.error('刷新技术领域数据失败:', error)
    showMessage(error.message || '刷新技术领域数据失败', 'error')
  } finally {
    loading.value = false
  }
}

// 将树形结构平铺为一维数组
function flattenDomainTree(tree: any[]) {
  const result: any[] = []
  
  function traverse(node: any) {
    if (!node) return
    
    result.push({
      id: node.id,
      domainName: node.domainName,
      parentId: node.parentId,
      parentName: node.parentName,
      level: node.level,
      description: node.description
    })
    
    if (node.children && node.children.length > 0) {
      node.children.forEach(traverse)
    }
  }
  
  tree.forEach(traverse)
  return result
}

// 展开所有领域
function expandAllDomains() {
  // 将所有领域ID添加到已展开列表中
  expandedDomains.value = domains.value.map(d => d.id)
}

// 折叠所有领域
function collapseAllDomains() {
  // 清空展开列表
  expandedDomains.value = []
}

// 加载模板类型数据
async function loadTemplateTypes() {
  try {
    loading.value = true
    
    const response = await fetch(docTemplateTypeUrl, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 存储模板类型数据
      templateTypes.value = result.data || []
    } else {
      throw new Error(result.message || '获取模板类型失败')
    }
  } catch (error: any) {
    console.error('加载模板类型数据出错:', error)
    showMessage(error.message || '加载模板类型失败', 'error')
    // 确保在出错时至少是空数组
    templateTypes.value = []
  } finally {
    loading.value = false
  }
}

// 添加文档模板
function addDocTemplate() {
  if (!selectedDomain.value) {
    showMessage('请先选择技术领域', 'warning')
    return
  }
  
  editingTemplateId.value = null
  editingTemplate.template_type = ''
  editingTemplate.template_name = ''
  editingTemplate.version = '1.0'
  editingTemplate.sections = []
  
  showTemplateAddModal.value = true
}

// 编辑文档模板
function editDocTemplate(template: any) {
  editingTemplateId.value = template.id
  editingTemplate.template_type = template.template_type
  editingTemplate.template_name = template.template_name
  editingTemplate.version = template.version || '1.0'
  editingTemplate.sections = [...(template.sections || [])]
  
  showTemplateAddModal.value = true
}

// 保存文档模板
async function saveDocTemplate() {
  if (!isTemplateFormValid.value) {
    showMessage('请填写必填字段', 'warning')
    return
  }
  
  try {
    loading.value = true
    
    // 获取对应模板类型ID
    const templateType = templateTypes.value.find(t => t && t.typeName === editingTemplate.template_type)
    const templateTypeId = templateType ? templateType.id : null
    
    if (!templateTypeId) {
      throw new Error('无效的模板类型')
    }
    
    const requestBody = {
      templateTypeId: templateTypeId,
      domainId: selectedDomain.value.id,
      orgId: 1, // 默认组织ID，可根据需要调整
      templateName: editingTemplate.template_name
    }
    
    let response
    let result
    
    if (editingTemplateId.value) {
      // 更新现有模板
      response = await fetch(`${docTemplateUrl}/${editingTemplateId.value}`, {
        method: 'PUT',
        headers: getRequestHeaders(),
        body: JSON.stringify(requestBody)
      })
    } else {
      // 创建新模板
      response = await fetch(docTemplateUrl, {
        method: 'POST',
        headers: getRequestHeaders(),
        body: JSON.stringify(requestBody)
      })
    }
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    result = await response.json()
    
    if (result.code === 200) {
      showMessage(editingTemplateId.value ? '模板更新成功' : '模板创建成功')
      
      // 重新加载领域模板
      loadDomainTemplates(selectedDomain.value.id)
    } else {
      throw new Error(result.message || '保存模板失败')
    }
    
    showTemplateAddModal.value = false
  } catch (error: any) {
    console.error('保存文档模板失败:', error)
    showMessage(error.message || '保存文档模板失败', 'error')
  } finally {
    loading.value = false
  }
}

// 查看模板详情
function viewTemplateDetail(template: any) {
  if (!template) {
    showMessage('无效的模板数据', 'warning')
    return
  }
  
  selectedTemplateDetail.value = { ...template }
  showTemplateModal.value = true
}

// 删除/解除文档模板绑定
async function removeDomainTemplate(index: number) {
  try {
    await ElMessageBox.confirm(
      '确定要解除此文档模板的绑定吗？',
      '解除绑定确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    
    if (!domainTemplates.value || !domainTemplates.value[index]) {
      throw new Error('无效的模板索引')
    }
    
    const templateId = domainTemplates.value[index].id
    
    if (!templateId) {
      throw new Error('无效的模板ID')
    }
    
    const response = await fetch(`${docTemplateUrl}/${templateId}`, {
      method: 'DELETE',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 从前端列表中移除
      domainTemplates.value.splice(index, 1)
      showMessage('文档模板解绑成功')
    } else {
      throw new Error(result.message || '解绑文档模板失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('解绑文档模板出错:', error)
      showMessage(error.message || '解绑文档模板失败', 'error')
    }
  } finally {
    loading.value = false
  }
}

// 保存模板绑定
function saveTemplateBindings() {
  showMessage('模板绑定保存成功')
}

// 章节排序函数
function sortedSections(template: any) {
  if (!template || !template.sections || !Array.isArray(template.sections)) {
    return []
  }
  
  return [...template.sections].sort((a, b) => {
    // 首先按排序号排序
    if (a.sort_order !== b.sort_order) {
      return a.sort_order - b.sort_order
    }
    // 如果排序号相同，按ID排序
    if (typeof a.id === 'string' && typeof b.id === 'string') {
      return a.id.localeCompare(b.id)
    }
    return 0
  })
}

// 添加新章节到模板
function addNewChapter(template: any) {
  if (!template) {
    showMessage('模板数据无效', 'warning')
    return
  }
  
  // 准备添加章节的数据
  selectedDocTemplate.value = template
  isChapterEditing.value = false
  editingSection.id = null
  editingSection.doc_template_id = template.id
  editingSection.section_type = ''
  editingSection.prompt_id = null
  editingSection.sort_order = (template.sections && Array.isArray(template.sections) && template.sections.length) ? template.sections.length + 1 : 1
  
  // 显示章节编辑模态框
  showChapterModal.value = true
}

// 查看章节详情
async function viewChapterDetail(template: any, section: any) {
  selectedDocTemplate.value = template
  
  try {
    loading.value = true
    
    const response = await fetch(`${docSectionTemplateUrl}/${section.id}`, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 转换API返回的数据为章节详情格式
      chapterDetailData.value = {
        id: result.data.id,
        section_type: result.data.sectionName,
        sort_order: result.data.sortOrder,
        prompt_id: result.data.promptId,
        prompt_template: result.data.promptTemplate ? {
          id: result.data.promptTemplate.id,
          template_name: result.data.promptTemplate.promName || '未命名模板',
          content: result.data.promptTemplate.content || '无内容',
          version: result.data.promptTemplate.version || '1.0'
        } : null
      }
      
      showChapterDetailModal.value = true
    } else {
      throw new Error(result.message || '获取章节详情失败')
    }
  } catch (error: any) {
    console.error('加载章节详情出错:', error)
    showMessage(error.message || '加载章节详情失败', 'error')
  } finally {
    loading.value = false
  }
}

// 编辑章节
function editChapter(template: any, section: any) {
  selectedDocTemplate.value = template
  isChapterEditing.value = true
  
  // 填充编辑表单
  editingSection.id = section.id
  editingSection.doc_template_id = template.id
  editingSection.section_type = section.section_type
  editingSection.prompt_id = section.prompt_id
  editingSection.sort_order = section.sort_order || 1
  
  showChapterModal.value = true
}

// 删除章节
async function deleteChapter(template: any, sectionId: string) {
  try {
    await ElMessageBox.confirm(
      '确定要删除该章节吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    
    const response = await fetch(`${docSectionTemplateUrl}/${sectionId}`, {
      method: 'DELETE',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 从前端数据中移除章节
      if (template.sections && Array.isArray(template.sections)) {
        const index = template.sections.findIndex((s: any) => s.id === sectionId)
        if (index !== -1) {
          template.sections.splice(index, 1)
        }
      }
      
      showMessage('章节删除成功')
    } else {
      throw new Error(result.message || '章节删除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除章节失败:', error)
      showMessage(error.message || '删除章节失败', 'error')
    }
  } finally {
    loading.value = false
  }
}

// 绑定提示词模板
function bindPromptTemplate(template: any, section: any) {
  try {
    // 先设置上下文数据
    selectedDocTemplate.value = template
    
    // 填充表单数据
    editingSection.id = section.id
    editingSection.doc_template_id = template.id
    editingSection.section_type = section.section_type
    editingSection.prompt_id = section.prompt_id
    editingSection.sort_order = section.sort_order || 1
    
    // 先显示模态框，避免等待API造成的延迟
    showPromptBindModal.value = true
    
    // 然后加载可用的提示词模板
    loadPromptTemplates()
  } catch (error: any) {
    console.error('绑定提示词模板出错:', error)
    showMessage(error.message || '准备绑定提示词模板失败', 'error')
  }
}

// 保存章节
async function saveChapter() {
  if (!editingSection.section_type) {
    showMessage('请输入章节名称', 'warning')
    return
  }
  
  try {
    loading.value = true
    
    const requestBody = {
      docTemplateId: selectedDocTemplate.value.id,
      promptId: editingSection.prompt_id,
      sectionName: editingSection.section_type,
      sortOrder: editingSection.sort_order || 1
    }
    
    let response
    let result
    
    if (isChapterEditing.value && editingSection.id) {
      // 更新章节
      response = await fetch(`${docSectionTemplateUrl}/${editingSection.id}`, {
        method: 'PUT',
        headers: getRequestHeaders(),
        body: JSON.stringify(requestBody)
      })
    } else {
      // 创建新章节
      response = await fetch(docSectionTemplateUrl, {
        method: 'POST',
        headers: getRequestHeaders(),
        body: JSON.stringify(requestBody)
      })
    }
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    result = await response.json()
    
    if (result.code === 200) {
      // 重新加载模板详情
      if (selectedDomain.value) {
        loadDomainTemplates(selectedDomain.value.id)
      }
      
      showMessage(isChapterEditing.value ? '章节更新成功' : '章节添加成功')
      showChapterModal.value = false
    } else {
      throw new Error(result.message || '保存章节失败')
    }
  } catch (error: any) {
    console.error('保存章节失败:', error)
    showMessage(error.message || '保存章节失败', 'error')
  } finally {
    loading.value = false
  }
}

// 加载提示词模板列表
async function loadPromptTemplates() {
  loading.value = true
  
  try {
    // 不传搜索参数，获取所有提示词模板
    let url = `${promptTemplateUrl}`
    
    const response = await fetch(url, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200 && result.data) {
      // 根据API返回的数据结构处理
      let dataArray = []
      
      if (result.data.records && Array.isArray(result.data.records)) {
        dataArray = result.data.records
      } else if (Array.isArray(result.data)) {
        dataArray = result.data
      } else {
        console.warn('未能识别的数据结构:', result.data)
        availablePromptTemplates.value = []
        filteredPromptTemplates.value = []
        return
      }
      
      // 转换API返回的数据为前端需要的格式
      availablePromptTemplates.value = dataArray.map((template: any) => ({
        id: template.id,
        section_type: template.sectionType,
        template_name: template.promName || '未命名模板',
        content: template.content || '无内容',
        version: template.version || '1.0'
      }))
      
      // 立即设置过滤后的模板列表
      filteredPromptTemplates.value = [...availablePromptTemplates.value]
    } else {
      // 如果返回代码不是200或data为null，设置为空数组
      availablePromptTemplates.value = []
      filteredPromptTemplates.value = []
    }
  } catch (error: any) {
    console.error('加载提示词模板出错:', error)
    showMessage(error.message || '加载提示词模板失败', 'error')
    // 确保在出错时至少有一个空数组
    availablePromptTemplates.value = []
    filteredPromptTemplates.value = []
  } finally {
    loading.value = false
  }
}

// 保存提示词模板绑定
async function savePromptBinding() {
  // 移除禁用属性，允许在未选择模板的情况下点击
  // 但添加前端校验，确保有选择模板
  if (!editingSection.prompt_id) {
    showMessage('请先选择一个提示词模板', 'warning')
    return
  }
  
  try {
    loading.value = true
    
    // 调用更新章节API，添加提示词模板绑定
    const requestBody = {
      docTemplateId: selectedDocTemplate.value.id,
      promptId: editingSection.prompt_id,
      sectionName: editingSection.section_type,
      sortOrder: editingSection.sort_order || 1
    }
    
    const response = await fetch(`${docSectionTemplateUrl}/${editingSection.id}`, {
      method: 'PUT',
      headers: getRequestHeaders(),
      body: JSON.stringify(requestBody)
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200) {
      // 更新前端数据
      if (selectedDocTemplate.value.sections && Array.isArray(selectedDocTemplate.value.sections)) {
        const index = selectedDocTemplate.value.sections.findIndex((s: any) => s.id === editingSection.id)
        if (index !== -1) {
          selectedDocTemplate.value.sections[index].prompt_id = editingSection.prompt_id
        }
      }
      
      showMessage('提示词模板绑定成功')
      
      // 重新加载模板数据
      if (selectedDomain.value) {
        loadDomainTemplates(selectedDomain.value.id)
      }
      
      showPromptBindModal.value = false
    } else {
      throw new Error(result.message || '保存绑定失败')
    }
  } catch (error: any) {
    console.error('保存提示词绑定出错:', error)
    showMessage(error.message || '保存提示词模板绑定失败', 'error')
  } finally {
    loading.value = false
  }
}

// 处理提示词模板搜索
function searchPromptTemplates() {
  if (!availablePromptTemplates.value || !Array.isArray(availablePromptTemplates.value)) {
    return
  }
  
  // 使用本地搜索，避免每次输入都发送请求
  if (!promptTemplateSearchQuery.value) {
    filteredPromptTemplates.value = availablePromptTemplates.value
  } else {
    const query = promptTemplateSearchQuery.value.toLowerCase()
    filteredPromptTemplates.value = availablePromptTemplates.value.filter(template => {
      return (
        (template.template_name && template.template_name.toLowerCase().includes(query)) ||
        (template.section_type && template.section_type.toLowerCase().includes(query))
      )
    })
  }
}

// 按回车搜索提示词模板
async function searchPromptTemplatesFromAPI() {
  if (!promptTemplateSearchQuery.value) {
    // 如果搜索框为空，加载所有模板
    loadPromptTemplates()
    return
  }
  
  loading.value = true
  
  try {
    // 使用搜索参数
    let url = `${promptTemplateUrl}/by-name?name=${encodeURIComponent(promptTemplateSearchQuery.value)}`
    
    const response = await fetch(url, {
      method: 'GET',
      headers: getRequestHeaders()
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.code === 200 && result.data) {
      // 根据API返回的数据结构处理
      let dataArray = []
      
      if (result.data.records && Array.isArray(result.data.records)) {
        dataArray = result.data.records
      } else if (Array.isArray(result.data)) {
        dataArray = result.data
      } else if (typeof result.data === 'object' && result.data !== null) {
        // 处理单个对象情况
        dataArray = [result.data]
      } else {
        filteredPromptTemplates.value = []
        return
      }
      
      // 转换API返回的数据并设置到全局模板列表
      availablePromptTemplates.value = dataArray.map((template: any) => ({
        id: template.id,
        section_type: template.sectionType || template.section_type || '技术问题',
        template_name: template.promName || template.template_name || '内测-生物医药-技术问题提示词',
        content: template.content || '无内容',
        version: template.version || '1.0'
      }))
      
      // 设置过滤后的提示词模板列表
      filteredPromptTemplates.value = [...availablePromptTemplates.value]
    } else {
      availablePromptTemplates.value = []
      filteredPromptTemplates.value = []
    }
  } catch (error: any) {
    console.error('搜索提示词模板出错:', error)
    showMessage(error.message || '搜索提示词模板失败', 'error')
    availablePromptTemplates.value = []
    filteredPromptTemplates.value = []
  } finally {
    loading.value = false
  }
}

// 移除已选择的固定模板
function removeSelectedPromptTemplate() {
  editingSection.prompt_id = null
}

// 获取已选择的固定模板名称
function getSelectedPromptTemplateName() {
  if (!editingSection.prompt_id || !availablePromptTemplates.value) return '未命名模板'
  
  const selectedTemplate = availablePromptTemplates.value.find(t => t.id === editingSection.prompt_id)
  return selectedTemplate ? selectedTemplate.template_name : '智能设备技术问题模板'
}

// 获取已选择的固定模板类型
function getSelectedPromptTemplateType() {
  if (!editingSection.prompt_id || !availablePromptTemplates.value) return editingSection.section_type || '技术问题'
  
  const selectedTemplate = availablePromptTemplates.value.find(t => t.id === editingSection.prompt_id)
  return selectedTemplate ? selectedTemplate.section_type : '技术问题'
}

// 获取已选择的固定模板版本
function getSelectedPromptTemplateVersion() {
  if (!editingSection.prompt_id || !availablePromptTemplates.value) return '1.0'
  
  const selectedTemplate = availablePromptTemplates.value.find(t => t.id === editingSection.prompt_id)
  return selectedTemplate ? selectedTemplate.version : '1.0'
}

// 在组件挂载时加载数据
onMounted(() => {
  refreshDomains()
  loadTemplateTypes()
})
</script>

<style scoped>
.domain-tree-container {
  max-height: calc(100vh - 200px); /* Adjust base height */
  min-height: 300px;
  overflow-y: auto;
}

.template-container {
  max-height: calc(100vh - 350px); /* Adjust base height */
  min-height: 200px;
  overflow-y: auto;
}

/* Media query example to adjust heights on larger screens if side-by-side */
@media (min-width: 768px) { /* md breakpoint */
  .domain-tree-container {
    max-height: calc(100vh - 150px); /* Adjust for side-by-side layout */
  }
  .template-container {
     max-height: calc(100vh - 300px); /* Adjust for side-by-side layout */
  }
}

.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #cbd5e0 #f7fafc;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f7fafc;
  border-radius: 4px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #cbd5e0;
  border-radius: 4px;
  border: 2px solid #f7fafc;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #a0aec0;
}

.tree-node {
  transition: all 0.2s;
}

.tree-node:hover {
  background-color: #f3f4f6;
}

.tree-node.active {
  background-color: #eff6ff;
  border-color: #3b82f6;
}
</style> 