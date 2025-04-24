<template>
  <div class="user-profile">
    <div class="container">
      <div class="user-profile-header">
        <h1>个人信息</h1>
        <p>管理您的账号信息</p>
      </div>
      
      <div class="profile-card">
        <a-form
          :model="formData"
          :rules="rules"
          layout="vertical"
          @finish="handleSubmit"
        >
          <div class="avatar-section">
            <a-avatar  :src="avatarUrl" :size="100" />
            <div class="avatar-upload">
              <a-upload
                name="avatar"
                :data = "requestData"
                :show-upload-list="false"
                action="http://localhost:8000/api/file/upload"
                :before-upload="beforeUpload"
                @change="handleAvatarChange"
              >
                <a-button>
                  <template #icon><upload-outlined /></template>
                  更换头像
                </a-button>
              </a-upload>
            </div>
          </div>
          
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="formData.username" disabled />
          </a-form-item>
          
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="formData.email" />
          </a-form-item>
          
          <a-form-item label="新密码" name="newPassword">
            <a-input-password v-model:value="formData.newPassword" placeholder="留空表示不修改密码" />
          </a-form-item>
          
          <a-form-item label="确认新密码" name="confirmPassword">
            <a-input-password v-model:value="formData.confirmPassword" placeholder="留空表示不修改密码" />
          </a-form-item>
          
          <a-form-item>
            <a-button type="primary" html-type="submit" :loading="loading">
              保存修改
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { UploadOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/user'
import * as api from '../../api'

const userStore = useUserStore()
const loading = ref(false)
const requestData = ref({directory:"avatar"})
const avatarUrl = ref(userStore.userInfo.avatar || '/avatar-default.png')
// 表单数据
const formData = reactive({
  username: userStore.userInfo.username || '',
  email: userStore.userInfo.email || '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  newPassword: [
    { 
      validator: (_, value) => {
        if (!value || value.length === 0) return Promise.resolve()
        if (value.length < 6 || value.length > 20) {
          return Promise.reject(new Error('密码长度必须在6-20个字符之间'))
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    {
      validator: (_, value) => {
        if (!formData.newPassword || formData.newPassword.length === 0) return Promise.resolve()
        if (value !== formData.newPassword) {
          return Promise.reject(new Error('两次输入的密码不一致'))
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

// 上传头像前的处理
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件!')
    return false;
  }
  
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('图片大小不能超过2MB!')
    return false;
  }
  requestData.value.file = file;
  return true; // 阻止自动上传，改为手动上传
}

// 处理头像变化
const handleAvatarChange = (info) => {
  if (info.file.status !== 'uploading') {
    console.log(info.file, info.fileList)
  }
  if (info.file.status === 'done') {
    message.success(`${info.file.name} 上传成功`)
    // 更新头像URL
    avatarUrl.value = info.file.response.data.url
    formData.avatar = info.file.response.data.url
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} 上传失败`)
  }
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const res = await api.getUserInfo()
    console.log(res)
    if (res.code === 200) {
      formData.username = res.data.username
      formData.email = res.data.email
      avatarUrl.value = res.data.avatar
    }
  } catch (error) {
    message.error('获取用户信息失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  loading.value = true
  console.log(formData)
  try {
    // 构建更新数据
    const updateData = {
      email: formData.email
    }
    
    // 如果有修改密码
    if (formData.newPassword) {
      updateData.newPassword = formData.newPassword
    }
    
    // 如果有上传头像
    if (formData.avatar) {
      updateData.avatar = formData.avatar
    }
    
    const res = await api.updateUserInfo(updateData)
    if (res.code === 200) {
      message.success('个人信息更新成功')
      // 清空密码字段
      formData.newPassword = ''
      formData.confirmPassword = ''
      // 刷新用户信息
      await userStore.getUserInfo()
    } else {
      message.error(res.message || '更新失败')
    }
  } catch (error) {
    message.error('更新失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.user-profile {
  padding-bottom: 40px;
}

.user-profile-header {
  text-align: center;
  margin-bottom: 32px;
}

.user-profile-header h1 {
  font-size: 32px;
  margin-bottom: 8px;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.user-profile-header p {
  color: var(--text-color-light);
  font-size: 16px;
}

.profile-card {
  background: white;
  border-radius: var(--border-radius-large);
  box-shadow: var(--box-shadow);
  padding: 32px;
  max-width: 500px;
  margin: 0 auto;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.avatar-upload {
  margin-top: 16px;
}
</style>
