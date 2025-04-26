<template>
  <div class="admin-prompt">
    <h1>提示词管理</h1>
    
    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索提示词标题"
            enter-button
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="8">
          <a-select
            v-model:value="selectedTags"
            mode="multiple"
            placeholder="选择标签筛选"
            style="width: 100%"
            :options="tagOptions"
            @change="handleTagChange"
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="status"
            style="width: 100%"
            @change="handleStatusChange"
          >
            <a-select-option :value="-1">全部状态</a-select-option>
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">已删除</a-select-option>
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
    
    <!-- 提示词列表 -->
    <a-table
      :columns="columns"
      :data-source="prompts"
      :loading="loading"
      :pagination="pagination"
      @change="handleTableChange"
      row-key="id"
    >
      <!-- 标题列 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'title'">
          <router-link :to="`/prompt/detail/${record.id}`" target="_blank">
            {{ record.title }}
          </router-link>
        </template>
        
        <!-- 标签列 -->
        <template v-if="column.key === 'tags'">
          <a-tag v-for="tag in record.tags" :key="tag.id" color="blue">
            {{ tag.name }}
          </a-tag>
        </template>
        
        <!-- 用户列 -->
        <template v-if="column.key === 'user'">
          <div class="user-info">
            <a-avatar :src="record.userAvatar || '/avatar-default.png'" size="small" />
            <span>{{ record.username }}</span>
          </div>
        </template>
        
        <!-- 状态列 -->
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? '正常' : '已删除' }}
          </a-tag>
        </template>
        
        <!-- 操作列 -->
        <template v-if="column.key === 'action'">
          <div class="action-buttons">
            <a-button type="link" size="small" @click="handleEdit(record)">
              编辑
            </a-button>
            <a-button 
              v-if="record.status === 1" 
              type="link" 
              size="small" 
              danger 
              @click="handleDelete(record)"
            >
              删除
            </a-button>
            <a-button 
              v-else 
              type="link" 
              size="small" 
              @click="handleRestore(record)"
            >
              恢复
            </a-button>
          </div>
        </template>
      </template>
    </a-table>
    
    <!-- 编辑提示词弹窗 -->
    <a-modal
      v-model:visible="editModalVisible"
      title="编辑提示词"
      @ok="submitEdit"
      :confirmLoading="submitLoading"
      width="800px"
    >
      <a-form
        :model="editForm"
        layout="vertical"
      >
        <a-form-item label="标题" name="title" :rules="[{ required: true, message: '请输入标题' }]">
          <a-input v-model:value="editForm.title" />
        </a-form-item>
        
        <a-form-item label="提示词内容" name="content" :rules="[{ required: true, message: '请输入提示词内容' }]">
          <a-textarea v-model:value="editForm.content" :rows="6" />
        </a-form-item>
        
        <a-form-item label="标签" name="tagIds" :rules="[{ required: true, type: 'array', message: '请选择标签' }]">
          <a-select
            v-model:value="editForm.tagIds"
            mode="multiple"
            placeholder="选择标签"
            :options="tagOptions"
          />
        </a-form-item>
      </a-form>
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
    title: '标题',
    dataIndex: 'title',
    key: 'title'
  },
  {
    title: '标签',
    key: 'tags'
  },
  {
    title: '发布者',
    key: 'user'
  },
  {
    title: '点赞数',
    dataIndex: 'likeCount',
    key: 'likeCount',
    width: 100
  },
  {
    title: '收藏数',
    dataIndex: 'favoriteCount',
    key: 'favoriteCount',
    width: 100
  },
  {
    title: '状态',
    key: 'status',
    width: 100
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    width: 150
  }
]

// 状态变量
const loading = ref(false)
const prompts = ref([])
const tagOptions = ref([])
const searchKeyword = ref('')
const selectedTags = ref([])
const status = ref(-1)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true
})

// 编辑相关
const editModalVisible = ref(false)
const submitLoading = ref(false)
const editForm = reactive({
  id: null,
  title: '',
  content: '',
  tagIds: []
})

// 获取标签列表
const fetchTags = async () => {
  try {
    const res = await api.getTagList()
    if (res.code === 200) {
      tagOptions.value = res.data.map(tag => ({
        label: tag.name,
        value: tag.id
      }))
    }
  } catch (error) {
    console.error('获取标签列表失败', error)
  }
}

// 获取提示词列表
const fetchPrompts = async () => {
  loading.value = true
  try {
    const res = await api.getPromptList({
      current: pagination.current,
      size: pagination.pageSize,
      keyword: searchKeyword.value || undefined,
      tagIds: selectedTags.value.length > 0 ? selectedTags.value : undefined,
      status: status.value >= 0 ? status.value : undefined
    })
    if (res.code === 200) {
      prompts.value = res.data.records
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error('获取提示词列表失败', error)
    message.error('获取提示词列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchPrompts()
}

// 标签筛选
const handleTagChange = () => {
  pagination.current = 1
  fetchPrompts()
}

// 状态筛选
const handleStatusChange = () => {
  pagination.current = 1
  fetchPrompts()
}

// 刷新
const handleRefresh = () => {
  fetchPrompts()
}

// 表格变化（分页、排序等）
const handleTableChange = (pag) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchPrompts()
}

// 编辑提示词
const handleEdit = (record) => {
  editForm.id = record.id
  editForm.title = record.title
  editForm.content = record.content
  editForm.tagIds = record.tags.map(tag => tag.id)
  editModalVisible.value = true
}

// 提交编辑
const submitEdit = async () => {
  if (!editForm.title || !editForm.content || editForm.tagIds.length === 0) {
    message.warning('请填写完整信息')
    return
  }
  
  submitLoading.value = true
  try {
    const res = await api.updatePrompt({
      id: editForm.id,
      title: editForm.title,
      content: editForm.content,
      tagIds: editForm.tagIds
    })
    if (res.code === 200) {
      message.success('更新成功')
      editModalVisible.value = false
      fetchPrompts()
    } else {
      message.error(res.message || '更新失败')
    }
  } catch (error) {
    message.error('更新失败，请稍后重试')
  } finally {
    submitLoading.value = false
  }
}

// 删除提示词
const handleDelete = async (record) => {
  try {
    const res = await api.deletePrompt(record.id)
    if (res.code === 200) {
      message.success('删除成功')
      fetchPrompts()
    } else {
      message.error(res.message || '删除失败')
    }
  } catch (error) {
    message.error('删除失败，请稍后重试')
  }
}

// 恢复提示词
const handleRestore = async (record) => {
  try {
    const res = await api.restorePrompt(record.id)
    if (res.code === 200) {
      message.success('恢复成功')
      fetchPrompts()
    } else {
      message.error(res.message || '恢复失败')
    }
  } catch (error) {
    message.error('恢复失败，请稍后重试')
  }
}

onMounted(() => {
  fetchTags()
  fetchPrompts()
})
</script>

<style scoped>
.admin-prompt {
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

.action-buttons {
  display: flex;
}
</style>
