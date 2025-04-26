<template>
  <div class="admin-report">
    <h1>举报管理</h1>
    
    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索举报内容"
            enter-button
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="status"
            style="width: 100%"
            @change="handleStatusChange"
          >
            <a-select-option :value="-1">全部状态</a-select-option>
            <a-select-option :value="0">待处理</a-select-option>
            <a-select-option :value="1">已处理</a-select-option>
            <a-select-option :value="2">已忽略</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-button type="primary" @click="handleRefresh">
            <template #icon><reload-outlined /></template>
            刷新
          </a-button>
        </a-col>
      </a-row>
    </div>
    
    <!-- 举报列表 -->
    <a-table
      :columns="columns"
      :data-source="reports"
      :loading="loading"
      :pagination="pagination"
      @change="handleTableChange"
      row-key="id"
    >
      <!-- 自定义列 -->
      <template #bodyCell="{ column, record }">
        <!-- 提示词列 -->
        <template v-if="column.key === 'prompt'">
          <router-link :to="`/prompt/detail/${record.promptId}`" target="_blank">
            {{ record.promptTitle }}
          </router-link>
        </template>
        
        <!-- 举报者列 -->
        <template v-if="column.key === 'reporter'">
          <div class="user-info">
            <a-avatar :src="record.reporterAvatar || '/avatar-default.png'" size="small" />
            <span>{{ record.reporterName }}</span>
          </div>
        </template>
        
        <!-- 举报理由列 -->
        <template v-if="column.key === 'reason'">
          <a-tooltip :title="record.reason">
            <div class="reason-text">{{ record.reason }}</div>
          </a-tooltip>
        </template>
        
        <!-- 状态列 -->
        <template v-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.status)">
            {{ getStatusText(record.status) }}
          </a-tag>
        </template>
        
        <!-- 操作列 -->
        <template v-if="column.key === 'action'">
          <div class="action-buttons">
            <a-button 
              v-if="record.status === 0" 
              type="link" 
              size="small" 
              @click="handleProcess(record)"
            >
              处理
            </a-button>
            <a-button 
              v-if="record.status === 0" 
              type="link" 
              size="small" 
              @click="handleIgnore(record)"
            >
              忽略
            </a-button>
            <a-button 
              type="link" 
              size="small" 
              @click="handleViewDetail(record)"
            >
              查看详情
            </a-button>
          </div>
        </template>
      </template>
    </a-table>
    
    <!-- 处理举报弹窗 -->
    <a-modal
      v-model:visible="processModalVisible"
      title="处理举报"
      @ok="submitProcess"
      :confirmLoading="submitLoading"
    >
      <a-form
        :model="processForm"
        layout="vertical"
      >
        <a-form-item label="举报ID">
          <a-input v-model:value="processForm.id" disabled />
        </a-form-item>
        
        <a-form-item label="提示词标题">
          <a-input v-model:value="processForm.promptTitle" disabled />
        </a-form-item>
        
        <a-form-item label="举报理由">
          <a-textarea v-model:value="processForm.reason" disabled :rows="3" />
        </a-form-item>
        
        <a-form-item label="处理方式" name="action" :rules="[{ required: true, message: '请选择处理方式' }]">
          <a-radio-group v-model:value="processForm.action">
            <a-radio :value="1">删除提示词</a-radio>
            <a-radio :value="2">保留提示词</a-radio>
            <a-radio :value="3">警告用户</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item label="处理备注" name="handleNote">
          <a-textarea v-model:value="processForm.handleNote" placeholder="请输入处理备注" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>
    
    <!-- 查看详情弹窗 -->
    <a-modal
      v-model:visible="detailModalVisible"
      title="举报详情"
      footer={null}
      width="700px"
    >
      <a-descriptions bordered>
        <a-descriptions-item label="举报ID" span={3}>
          {{ detailForm.id }}
        </a-descriptions-item>
        <a-descriptions-item label="提示词标题" span={3}>
          <router-link :to="`/prompt/detail/${detailForm.promptId}`" target="_blank">
            {{ detailForm.promptTitle }}
          </router-link>
        </a-descriptions-item>
        <a-descriptions-item label="举报者" span={3}>
          {{ detailForm.reporterName }}
        </a-descriptions-item>
        <a-descriptions-item label="举报理由" span={3}>
          {{ detailForm.reason }}
        </a-descriptions-item>
        <a-descriptions-item label="举报时间" span={3}>
          {{ detailForm.createTime }}
        </a-descriptions-item>
        <a-descriptions-item label="状态" span={3}>
          <a-tag :color="getStatusColor(detailForm.status)">
            {{ getStatusText(detailForm.status) }}
          </a-tag>
        </a-descriptions-item>
        <template v-if="detailForm.status !== 0">
          <a-descriptions-item label="处理人" span={3}>
            {{ detailForm.handlerName }}
          </a-descriptions-item>
          <a-descriptions-item label="处理时间" span={3}>
            {{ detailForm.handleTime }}
          </a-descriptions-item>
          <a-descriptions-item label="处理方式" span={3}>
            {{ getActionText(detailForm.action) }}
          </a-descriptions-item>
          <a-descriptions-item label="处理备注" span={3}>
            {{ detailForm.handleNote }}
          </a-descriptions-item>
        </template>
      </a-descriptions>
      <div style="margin-top: 16px; text-align: right;">
        <a-button @click="detailModalVisible = false">关闭</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { ReloadOutlined } from '@ant-design/icons-vue'
import * as api from '../../api'

// 表格列定义
const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
    width: 80
  },
  {
    title: '提示词',
    key: 'prompt',
    width: 200
  },
  {
    title: '举报者',
    key: 'reporter',
    width: 150
  },
  {
    title: '举报理由',
    key: 'reason'
  },
  {
    title: '状态',
    key: 'status',
    width: 100
  },
  {
    title: '举报时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    width: 180
  }
]

// 状态变量
const loading = ref(false)
const reports = ref([])
const searchKeyword = ref('')
const status = ref(-1)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true
})

// 处理举报相关
const processModalVisible = ref(false)
const submitLoading = ref(false)
const processForm = reactive({
  id: null,
  promptId: null,
  promptTitle: '',
  reason: '',
  action: 1,
  handleNote: ''
})

// 查看详情相关
const detailModalVisible = ref(false)
const detailForm = reactive({
  id: null,
  promptId: null,
  promptTitle: '',
  reporterName: '',
  reason: '',
  createTime: '',
  status: 0,
  handlerName: '',
  handleTime: '',
  action: null,
  handleNote: ''
})

// 获取举报列表
const fetchReports = async () => {
  loading.value = true
  try {
    const res = await api.getReportList({
      current: pagination.current,
      size: pagination.pageSize,
      keyword: searchKeyword.value || undefined,
      status: status.value >= 0 ? status.value : undefined
    })
    if (res.code === 200) {
      reports.value = res.data.records
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error('获取举报列表失败', error)
    message.error('获取举报列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchReports()
}

// 状态筛选
const handleStatusChange = () => {
  pagination.current = 1
  fetchReports()
}

// 刷新
const handleRefresh = () => {
  fetchReports()
}

// 表格变化（分页、排序等）
const handleTableChange = (pag) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchReports()
}

// 处理举报
const handleProcess = (record) => {
  processForm.id = record.id
  processForm.promptId = record.promptId
  processForm.promptTitle = record.promptTitle
  processForm.reason = record.reason
  processForm.action = 1
  processForm.handleNote = ''
  processModalVisible.value = true
}

// 忽略举报
const handleIgnore = async (record) => {
  try {
    const res = await api.handleReport(record.id, {
      status: 2,
      action: 0,
      handleNote: '管理员已忽略此举报'
    })
    if (res.code === 200) {
      message.success('已忽略此举报')
      fetchReports()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 查看详情
const handleViewDetail = (record) => {
  Object.assign(detailForm, record)
  detailModalVisible.value = true
}

// 提交处理
const submitProcess = async () => {
  if (!processForm.action) {
    message.warning('请选择处理方式')
    return
  }
  
  submitLoading.value = true
  try {
    const res = await api.handleReport(processForm.id, {
      status: 1,
      action: processForm.action,
      handleNote: processForm.handleNote
    })
    if (res.code === 200) {
      message.success('处理成功')
      processModalVisible.value = false
      
      // 如果选择删除提示词，则调用删除API
      if (processForm.action === 1) {
        await api.deletePrompt(processForm.promptId)
      }
      
      fetchReports()
    } else {
      message.error(res.message || '处理失败')
    }
  } catch (error) {
    message.error('处理失败，请稍后重试')
  } finally {
    submitLoading.value = false
  }
}

// 获取状态颜色
const getStatusColor = (status) => {
  switch (status) {
    case 0: return 'orange'
    case 1: return 'green'
    case 2: return 'gray'
    default: return 'blue'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0: return '待处理'
    case 1: return '已处理'
    case 2: return '已忽略'
    default: return '未知'
  }
}

// 获取处理方式文本
const getActionText = (action) => {
  switch (action) {
    case 0: return '忽略'
    case 1: return '删除提示词'
    case 2: return '保留提示词'
    case 3: return '警告用户'
    default: return '未知'
  }
}

onMounted(() => {
  fetchReports()
})
</script>

<style scoped>
.admin-report {
  padding: 0 0 20px 0;
}

.filter-section {
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.reason-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.action-buttons {
  display: flex;
}
</style>
