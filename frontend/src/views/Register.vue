<template>
  <div class="register-page">
    <div class="container">
      <div class="register-card">
        <div class="register-header">
          <h2>注册</h2>
          <p>创建一个新账号，开始分享AI提示词</p>
        </div>
        
        <a-form
          :model="formData"
          :rules="rules"
          @finish="handleSubmit"
          layout="vertical"
        >
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="formData.username" placeholder="请输入用户名" />
          </a-form-item>
          
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="formData.email" placeholder="请输入邮箱" />
          </a-form-item>
          
          <a-form-item label="密码" name="password">
            <a-input-password v-model:value="formData.password" placeholder="请输入密码" />
          </a-form-item>
          
          <a-form-item label="确认密码" name="confirmPassword">
            <a-input-password v-model:value="formData.confirmPassword" placeholder="请再次输入密码" />
          </a-form-item>
          
          <a-form-item>
            <a-button type="primary" html-type="submit" :loading="loading" block>
              注册
            </a-button>
          </a-form-item>
        </a-form>
        
        <div class="register-footer">
          <p>
            已有账号？
            <router-link to="/login">立即登录</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { message } from 'ant-design-vue'

export default {
  name: 'RegisterPage',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const loading = ref(false)

// 表单数据
const formData = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度必须在3-20个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_, value) => {
        if (value === formData.password) {
          return Promise.resolve()
        }
        return Promise.reject(new Error('两次输入的密码不一致'))
      },
      trigger: 'blur'
    }
  ]
}

// 提交表单
const handleSubmit = async () => {
  loading.value = true
  try {
    const success = await userStore.register({
      username: formData.username,
      email: formData.email,
      password: formData.password
    })
    
    if (success) {
      router.push('/')
    }
  } catch (error) {
    message.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

return {
  formData,
  rules,
  loading,
  handleSubmit
}
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - var(--header-height) - var(--footer-height));
  padding: 40px 0;
}

.register-card {
  background: white;
  border-radius: var(--border-radius-large);
  box-shadow: var(--box-shadow);
  padding: 32px;
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.register-header {
  text-align: center;
  margin-bottom: 24px;
}

.register-header h2 {
  font-size: 24px;
  margin-bottom: 8px;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.register-header p {
  color: var(--text-color-light);
}

.register-footer {
  text-align: center;
  margin-top: 16px;
}

.register-footer a {
  color: var(--primary-color);
  font-weight: bold;
}
</style>
