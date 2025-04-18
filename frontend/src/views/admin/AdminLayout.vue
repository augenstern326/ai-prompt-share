<template>
  <div class="admin-layout">
    <a-layout style="min-height: 100vh">
      <!-- 侧边栏 -->
      <a-layout-sider
        v-model:collapsed="collapsed"
        collapsible
        class="admin-sider"
      >
        <div class="admin-logo">
          <h2 v-if="!collapsed">管理后台</h2>
          <h2 v-else>后台</h2>
        </div>
        <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="inline"
        >
          <a-menu-item key="prompt">
            <router-link to="/admin/prompt">
              <template #icon><file-outlined /></template>
              <span>提示词管理</span>
            </router-link>
          </a-menu-item>
          <a-menu-item key="user">
            <router-link to="/admin/user">
              <template #icon><user-outlined /></template>
              <span>用户管理</span>
            </router-link>
          </a-menu-item>
          <a-menu-item key="report">
            <router-link to="/admin/report">
              <template #icon><flag-outlined /></template>
              <span>举报管理</span>
            </router-link>
          </a-menu-item>
        </a-menu>
      </a-layout-sider>
      
      <!-- 内容区域 -->
      <a-layout>
        <a-layout-header class="admin-header">
          <div class="header-left">
            <menu-unfold-outlined
              v-if="collapsed"
              class="trigger"
              @click="() => (collapsed = !collapsed)"
            />
            <menu-fold-outlined
              v-else
              class="trigger"
              @click="() => (collapsed = !collapsed)"
            />
            <span class="header-title">{{ getHeaderTitle() }}</span>
          </div>
          <div class="header-right">
            <a-dropdown>
              <a class="user-dropdown" @click.prevent>
                <a-avatar :src="userStore.userInfo.avatar || '/avatar-default.png'" />
                <span class="username">{{ userStore.userInfo.username }}</span>
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <router-link to="/">返回前台</router-link>
                  </a-menu-item>
                  <a-menu-item @click="handleLogout">退出登录</a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
        </a-layout-header>
        
        <a-layout-content class="admin-content">
          <router-view />
        </a-layout-content>
        
        <a-layout-footer class="admin-footer">
          AI提示词分享平台 管理后台 &copy; {{ new Date().getFullYear() }}
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  FileOutlined,
  UserOutlined,
  FlagOutlined
} from '@ant-design/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const collapsed = ref(false)
const selectedKeys = ref([route.name === 'Admin' ? 'prompt' : route.path.split('/').pop()])

// 监听路由变化，更新选中的菜单项
watch(
  () => route.path,
  (path) => {
    const key = path.split('/').pop()
    selectedKeys.value = [key]
  }
)

// 获取页面标题
const getHeaderTitle = () => {
  const key = selectedKeys.value[0]
  switch (key) {
    case 'prompt':
      return '提示词管理'
    case 'user':
      return '用户管理'
    case 'report':
      return '举报管理'
    default:
      return '管理后台'
  }
}

// 退出登录
const handleLogout = async () => {
  await userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-sider {
  overflow: auto;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
}

.admin-logo {
  height: 64px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
}

.admin-logo h2 {
  color: white;
  margin: 0;
  font-size: 18px;
}

.admin-header {
  background: white;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-title {
  margin-left: 16px;
  font-size: 18px;
  font-weight: bold;
}

.trigger {
  font-size: 18px;
  cursor: pointer;
  transition: color 0.3s;
}

.trigger:hover {
  color: var(--primary-color);
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
}

.admin-content {
  margin: 24px 16px;
  padding: 24px;
  background: white;
  border-radius: var(--border-radius-base);
  min-height: 280px;
  margin-left: 200px;
  transition: margin-left 0.3s;
}

.admin-content.collapsed {
  margin-left: 80px;
}

.admin-footer {
  text-align: center;
  margin-left: 200px;
  transition: margin-left 0.3s;
}

.admin-footer.collapsed {
  margin-left: 80px;
}
</style>
