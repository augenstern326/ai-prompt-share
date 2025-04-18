import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/prompt/square',
    name: 'PromptSquare',
    component: () => import('../views/prompt/PromptSquare.vue'),
    meta: { title: '提示词广场' }
  },
  {
    path: '/prompt/detail/:id',
    name: 'PromptDetail',
    component: () => import('../views/prompt/PromptDetail.vue'),
    meta: { title: '提示词详情' }
  },
  {
    path: '/prompt/create',
    name: 'PromptCreate',
    component: () => import('../views/prompt/PromptCreate.vue'),
    meta: { title: '创建提示词', requireAuth: true }
  },
  {
    path: '/prompt/edit/:id',
    name: 'PromptEdit',
    component: () => import('../views/prompt/PromptEdit.vue'),
    meta: { title: '编辑提示词', requireAuth: true }
  },
  {
    path: '/user/favorite',
    name: 'UserFavorite',
    component: () => import('../views/user/UserFavorite.vue'),
    meta: { title: '我的收藏', requireAuth: true }
  },
  {
    path: '/user/profile',
    name: 'UserProfile',
    component: () => import('../views/user/UserProfile.vue'),
    meta: { title: '个人信息', requireAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { title: '管理后台', requireAuth: true, requireAdmin: true },
    children: [
      {
        path: 'prompt',
        name: 'AdminPrompt',
        component: () => import('../views/admin/AdminPrompt.vue'),
        meta: { title: '提示词管理', requireAuth: true, requireAdmin: true }
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('../views/admin/AdminUser.vue'),
        meta: { title: '用户管理', requireAuth: true, requireAdmin: true }
      },
      {
        path: 'report',
        name: 'AdminReport',
        component: () => import('../views/admin/AdminReport.vue'),
        meta: { title: '举报管理', requireAuth: true, requireAdmin: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue'),
    meta: { title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由前置守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - AI提示词分享平台` : 'AI提示词分享平台'
  
  // 获取token
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  
  // 需要登录权限
  if (to.meta.requireAuth) {
    if (!token) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
    
    // 需要管理员权限
    if (to.meta.requireAdmin && userInfo.role !== 'admin') {
      next({ name: 'Home' })
      return
    }
  }
  
  next()
})

export default router
