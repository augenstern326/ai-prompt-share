<template>
  <div class="login-page">
    <div class="container">
      <div class="login-card">
        <div class="login-header">
          <h2>{{ isLogin ? '登录' : '注册' }}</h2>
          <p>{{ isLogin ? '欢迎回来，请登录您的账号' : '创建一个新账号，开始分享AI提示词' }}</p>
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
          
          <a-form-item v-if="!isLogin" label="邮箱" name="email">
            <a-input v-model:value="formData.email" placeholder="请输入邮箱" />
          </a-form-item>
          
          <a-form-item label="密码" name="password">
            <a-input-password v-model:value="formData.password" placeholder="请输入密码" />
          </a-form-item>
          
          <a-form-item v-if="!isLogin" label="确认密码" name="confirmPassword">
            <a-input-password v-model:value="formData.confirmPassword" placeholder="请再次输入密码" />
          </a-form-item>
          
          <a-form-item>
            <a-button type="primary" html-type="submit" :loading="loading" block>
              {{ isLogin ? '登录' : '注册' }}
            </a-button>
          </a-form-item>
        </a-form>
        
        <div class="login-footer">
          <p v-if="isLogin">
            还没有账号？
            <router-link to="/register">立即注册</router-link>
          </p>
          <p v-else>
            已有账号？
            <router-link to="/login">立即登录</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { message } from 'ant-design-vue'

export default {
  name: 'LoginPage',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    const loading = ref(false)

    // 判断是登录还是注册
    const isLogin = computed(() => route.path === '/login')

    // 表单数据
    const formData = reactive({
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    })

    // 表单验证规则
    const rules = computed(() => {
      const baseRules = {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度必须在3-20个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度必须在6-20个字符之间', trigger: 'blur' }
        ]
      }
      
      // 注册时需要额外的验证规则
      if (!isLogin.value) {
        return {
          ...baseRules,
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
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
      }
      
      return baseRules
    })

    // 提交表单
    const handleSubmit = async () => {
      loading.value = true
      try {
        let success
        
        if (isLogin.value) {
          // 登录
          success = await userStore.login({
            username: formData.username,
            password: formData.password
          })
        } else {
          // 注册
          success = await userStore.register({
            username: formData.username,
            email: formData.email,
            password: formData.password
          })
        }
        
        if (success) {
          // 获取重定向地址
          const redirect = route.query.redirect || '/'
          router.push(redirect)
        }
      } catch (error) {
        message.error('操作失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }

    return {
      isLogin,
      formData,
      rules,
      loading,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - var(--header-height) - var(--footer-height));
  padding: 40px 0;
}

.login-card {
  background: white;
  border-radius: var(--border-radius-large);
  box-shadow: var(--box-shadow);
  padding: 32px;
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-header h2 {
  font-size: 24px;
  margin-bottom: 8px;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.login-header p {
  color: var(--text-color-light);
}

.login-footer {
  text-align: center;
  margin-top: 16px;
}

.login-footer a {
  color: var(--primary-color);
  font-weight: bold;
}
</style>
