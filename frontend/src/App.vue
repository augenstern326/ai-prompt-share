<template>
  <div id="app">
    <a-layout class="layout">
      <!-- 头部导航 -->
      <a-layout-header class="header">
        <div class="container header-content">
          <div class="logo">
            <router-link to="/">AI提示词分享平台</router-link>
          </div>
          <div class="nav">
            <router-link to="/">首页</router-link>
            <router-link to="/prompt/square">提示词广场</router-link>
            <template v-if="userStore.isLogin">
              <router-link to="/prompt/create">上传提示词</router-link>
              <router-link to="/user/favorite">我的收藏</router-link>
              <a-dropdown>
                <a class="user-dropdown" @click.prevent>
                  <a-avatar :src="userStore.userInfo.avatar || '/avatar-default.png'" />
                  <span class="username">{{ userStore.userInfo.username }}</span>
                </a>
                <template #overlay>
                  <a-menu>
                    <a-menu-item v-if="userStore.isAdmin">
                      <router-link to="/admin">管理后台</router-link>
                    </a-menu-item>
                    <a-menu-item>
                      <router-link to="/user/profile">个人信息</router-link>
                    </a-menu-item>
                    <a-menu-item @click="handleLogout">退出登录</a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
            <template v-else>
              <router-link to="/login" class="login-btn">登录</router-link>
              <router-link to="/register" class="register-btn gradient-btn">注册</router-link>
            </template>
          </div>
        </div>
      </a-layout-header>

      <!-- 内容区域 -->
      <a-layout-content class="content">
        <div class="page-container">
          <router-view />
        </div>
      </a-layout-content>

      <!-- 页脚 -->
      <a-layout-footer class="footer">
        <div class="container">
          <p>AI提示词分享平台 &copy; {{ new Date().getFullYear() }}</p>
        </div>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup>
import { useUserStore } from './stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

// 退出登录
const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  height: var(--header-height);
  line-height: var(--header-height);
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.logo a {
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav {
  display: flex;
  align-items: center;
}

.nav a {
  margin-left: 24px;
  color: var(--text-color);
}

.nav a:hover, .nav a.router-link-active {
  color: var(--primary-color);
}

.login-btn {
  color: var(--primary-color) !important;
}

.register-btn {
  color: white !important;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
}

.content {
  background: var(--background-color);
}

.footer {
  text-align: center;
  background: white;
  height: var(--footer-height);
  line-height: var(--footer-height);
  padding: 0;
  color: var(--text-color-light);
}
</style>
