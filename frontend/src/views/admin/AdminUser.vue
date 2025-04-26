<template>
  <div class="admin-user">
    <h1>用户管理</h1>
    
    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索用户名或邮箱"
            enter-button
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="role"
            style="width: 100%"
            @change="handleRoleChange"
          >
            <a-select-option :value="-1">全部角色</a-select-option>
            <a-select-option :value="0">普通用户</a-select-option>
            <a-select-option :value="1">管理员</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="status"
            style="width: 100%"
            @change="handleStatusChange"
          >
            <a-select-option :value="-1">全部状态</a-select-option>
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">已禁用</a-select-option>
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
    
    <!-- 用户列表 -->
    <a-table
      :columns="columns"
      :data-source="users"
      :loading="loading"
      :pagination="pagination"
      @change="handleTableChange"
      row-key="id"
    >
      <!-- 用户信息列 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'user'">
          <div class="user-info">
            <a-avatar :src="record.avatar || '/avatar-default.png'" />
            <span>{{ record.username }}</span>
          </div>
        </template>
        
        <!-- 角色列 -->
        <template v-if="column.key === 'role'">
          <a-tag :color="record.role === 'admin' ? 'purple' : 'blue'">
            {{ record.role === 'admin' ? '管理员' : '普通用户' }}
          </a-tag>
        </template>
        
        <!-- 状态列 -->
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? '正常' : '已禁用' }}
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
              @click="handleDisable(record)"
            >
              禁用
            </a-button>
            <a-button 
              v-else 
              type="link" 
              size="small" 
              @click="handleEnable(record)"
            >
              启用
            </a-button>
            <a-button 
              v-if="record.role !== 'admin'" 
              type="link" 
              size="small" 
              @click="handleSetAdmin(record)"
            >
              设为管理员
            </a-button>
            <a-button 
              v-else 
              type="link" 
              size="small" 
              @click="handleRemoveAdmin(record)"
            >
              取消管理员
            </a-button>
          </div>
        </template>
      </template>
    </a-table>
    
    <!-- 编辑用户弹窗 -->
    <a-modal
      v-model:visible="editModalVisible"
      title="编辑用户信息"
      @ok="submitEdit"
      :confirmLoading="submitLoading"
    >
      <a-form
        :model="editForm"
        layout="vertical"
      >
        <a-form-item label="用户名" name="username">
          <a-input v-model:value="editForm.username" disabled />
        </a-form-item>
        
        <a-form-item label="邮箱" name="email" :rules="[{ required: true, type: 'email', message: '请输入有效的邮箱地址' }]">
          <a-input v-model:value="editForm.email" />
        </a-form-item>
        
        <a-form-item label="角色" name="role">
          <a-select v-model:value="editForm.role">
            <a-select-option value="user">普通用户</a-select-option>
            <a-select-option value="admin">管理员</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="状态" name="status">
          <a-select v-model:value="editForm.status">
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
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
    title: '用户信息',
    key: 'user'
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email'
  },
  {
    title: '角色',
    key: 'role',
    width: 100
  },
  {
    title: '状态',
    key: 'status',
    width: 100
  },
  {
    title: '注册时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    width: 300
  }
]

// 状态变量
const loading = ref(false)
const users = ref([])
const searchKeyword = ref('')
const role = ref(-1)
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
  username: '',
  email: '',
  role: 'user',
  status: 1
})

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await api.getUserList({
      current: pagination.current,
      size: pagination.pageSize,
      keyword: searchKeyword.value || undefined,
      role: role.value >= 0 ? (role.value === 1 ? 'admin' : 'user') : undefined,
      status: status.value >= 0 ? status.value : undefined
    })
    if (res.code === 200) {
      users.value = res.data.records
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error('获取用户列表失败', error)
    message.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchUsers()
}

// 角色筛选
const handleRoleChange = () => {
  pagination.current = 1
  fetchUsers()
}

// 状态筛选
const handleStatusChange = () => {
  pagination.current = 1
  fetchUsers()
}

// 刷新
const handleRefresh = () => {
  fetchUsers()
}

// 表格变化（分页、排序等）
const handleTableChange = (pag) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchUsers()
}

// 编辑用户
const handleEdit = (record) => {
  editForm.id = record.id
  editForm.username = record.username
  editForm.email = record.email
  editForm.role = record.role
  editForm.status = record.status
  editModalVisible.value = true
}

// 提交编辑
const submitEdit = async () => {
  if (!editForm.email) {
    message.warning('请填写邮箱')
    return
  }
  
  submitLoading.value = true
  try {
    const res = await api.updateUser({
      id: editForm.id,
      email: editForm.email,
      role: editForm.role,
      status: editForm.status
    })
    if (res.code === 200) {
      message.success('更新成功')
      editModalVisible.value = false
      fetchUsers()
    } else {
      message.error(res.message || '更新失败')
    }
  } catch (error) {
    message.error('更新失败，请稍后重试')
  } finally {
    submitLoading.value = false
  }
}

// 禁用用户
const handleDisable = async (record) => {
  try {
    const res = await api.updateUserStatus(record.id, 0)
    if (res.code === 200) {
      message.success('用户已禁用')
      fetchUsers()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 启用用户
const handleEnable = async (record) => {
  try {
    const res = await api.updateUserStatus(record.id, 1)
    if (res.code === 200) {
      message.success('用户已启用')
      fetchUsers()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 设为管理员
const handleSetAdmin = async (record) => {
  try {
    const res = await api.updateUserRole(record.id, 'admin')
    if (res.code === 200) {
      message.success('已设为管理员')
      fetchUsers()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 取消管理员
const handleRemoveAdmin = async (record) => {
  try {
    const res = await api.updateUserRole(record.id, 'user')
    if (res.code === 200) {
      message.success('已取消管理员权限')
      fetchUsers()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.admin-user {
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
  flex-wrap: wrap;
}
</style>
