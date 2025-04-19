import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'
import * as api from '@/api'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const isLogin = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value.role === 'admin')

  // 登录
  const login = async (loginData) => {
    try {
      const res = await api.login(loginData)
      console.log(res)
      if (res.code === 200) {
        token.value = res.data.token
        userInfo.value = res.data
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('userInfo', JSON.stringify(res.data))
        message.success('登录成功')
        return true
      } else {
        message.error(res.message || '登录失败')
        return false
      }
    } catch (error) {
      message.error('登录失败，请稍后重试')
      return false
    }
  }

  // 注册
  const register = async (registerData) => {
    try {
      const res = await api.register(registerData)
      if (res.code === 200) {
        token.value = res.data.token
        userInfo.value = res.data
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('userInfo', JSON.stringify(res.data))
        message.success('注册成功')
        return true
      } else {
        message.error(res.message || '注册失败')
        return false
      }
    } catch (error) {
      message.error('注册失败，请稍后重试')
      return false
    }
  }

  // 获取用户信息
  const getUserInfo = async () => {
    try {
      const res = await api.getUserInfo()
      if (res.code === 200) {
        userInfo.value = res.data
        localStorage.setItem('userInfo', JSON.stringify(res.data))
        return true
      } else {
        return false
      }
    } catch (error) {
      return false
    }
  }

  // 更新用户信息
  const updateUserInfo = async (userData) => {
    try {
      const res = await api.updateUserInfo(userData)
      if (res.code === 200) {
        await getUserInfo()
        message.success('更新成功')
        return true
      } else {
        message.error(res.message || '更新失败')
        return false
      }
    } catch (error) {
      message.error('更新失败，请稍后重试')
      return false
    }
  }

  // 登出
  const logout = async () => {
    try {
      const res = await api.logout()
      if(res.code===200){
        token.value = ''
        userInfo.value = {}
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        message.success('已退出登录')
        return true
      }else {
        message.error(res.message)
        return false
      }
    } catch (error) {
      return false
    }
  }

  return {
    token,
    userInfo,
    isLogin,
    isAdmin,
    login,
    register,
    getUserInfo,
    updateUserInfo,
    logout
  }
})
