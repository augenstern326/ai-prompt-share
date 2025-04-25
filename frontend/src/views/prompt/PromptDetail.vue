<template>
  <div class="prompt-detail">
    <div class="container">
      <a-spin :spinning="loading">
        <template v-if="prompt">
          <div class="prompt-detail-card">
            <div class="prompt-header">
              <h1 class="prompt-title">{{ prompt.title }}</h1>
              <div class="prompt-tags">
                <span v-for="tag in prompt.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
              </div>
              <div class="prompt-meta">
                <div class="prompt-author">
                  <a-avatar :src="prompt.userAvatar || '/avatar-default.png'" />
                  <span>{{ prompt.username }}</span>
                </div>
                <div class="prompt-date">
                  发布于 {{ formatDate(prompt.createTime) }}
                </div>
              </div>
            </div>
            
            <div class="prompt-content-wrapper">
              <div class="prompt-content">
                <h3>提示词内容</h3>
                <div class="prompt-text">
                  <pre>{{ prompt.content }}</pre>
                  <a-button type="primary" @click="copyPrompt" size="small">
                    <template #icon><copy-outlined /></template>
                    复制提示词
                  </a-button>
                </div>
              </div>
              
              <div class="prompt-image" v-if="prompt.imageUrl">
                <h3>效果展示</h3>
                <div class="image-wrapper">
                  <img :src="prompt.imageUrl" :alt="prompt.title" />
                </div>
              </div>
            </div>
            
            <div class="prompt-actions">
              <div class="action-buttons">
                <a-button 
                  :type="prompt.liked ? 'primary' : 'default'" 
                  @click="handleLike"
                  :disabled="!userStore.isLogin"
                >
                  <template #icon><like-outlined /></template>
                  {{ prompt.likeCount }}
                </a-button>
                <a-button 
                  :type="prompt.disliked ? 'primary' : 'default'" 
                  @click="handleDislike"
                  :disabled="!userStore.isLogin"
                >
                  <template #icon><dislike-outlined /></template>
                  {{ prompt.dislikeCount }}
                </a-button>
                <a-button 
                  :type="prompt.favorited ? 'primary' : 'default'" 
                  @click="handleFavorite"
                  :disabled="!userStore.isLogin"
                >
                  <template #icon><star-outlined /></template>
                  {{ prompt.favoriteCount }}
                </a-button>
                <a-button 
                  @click="showReportModal"
                  :disabled="!userStore.isLogin"
                >
                  <template #icon><flag-outlined /></template>
                  举报
                </a-button>
              </div>
              
              <div class="edit-buttons" v-if="canEdit">
                <a-button type="primary" @click="handleEdit">
                  <template #icon><edit-outlined /></template>
                  编辑
                </a-button>
                <a-popconfirm
                  title="确定要删除这个提示词吗？"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="handleDelete"
                >
                  <a-button danger>
                    <template #icon><delete-outlined /></template>
                    删除
                  </a-button>
                </a-popconfirm>
                <a-button type="primary" @click="handleReturn">
                  <template #icon><edit-outlined /></template>
                  返回
                </a-button>
              </div>
            </div>
          </div>
        </template>
        <a-empty v-else-if="!loading" description="提示词不存在或已被删除" />
      </a-spin>
    </div>
    
    <!-- 举报弹窗 -->
    <a-modal
      v-model:visible="reportModalVisible"
      title="举报提示词"
      @ok="submitReport"
      :confirmLoading="reportLoading"
    >
      <a-form :model="reportForm" layout="vertical">
        <a-form-item label="举报理由" name="reason" :rules="[{ required: true, message: '请输入举报理由' }]">
          <a-textarea v-model:value="reportForm.reason" :rows="4" placeholder="请详细描述举报理由" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { message } from 'ant-design-vue'
import * as api from '../../api'
import { 
  LikeOutlined, 
  DislikeOutlined, 
  StarOutlined, 
  FlagOutlined,
  EditOutlined,
  DeleteOutlined,
  CopyOutlined
} from '@ant-design/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const prompt = ref(null)
const reportModalVisible = ref(false)
const reportLoading = ref(false)
const reportForm = reactive({
  reason: ''
})

// 获取提示词详情
const fetchPromptDetail = async () => {
  loading.value = true
  try {
    const res = await api.getPromptDetail(route.params.id)
    if (res.code === 200) {
      prompt.value = res.data
      console.log(res.data)
    } else {
      message.error('获取提示词详情失败')
    }
  } catch (error) {
    console.error('获取提示词详情失败', error)
    message.error('获取提示词详情失败')
  } finally {
    loading.value = false
  }
}

// 判断是否可以编辑
const canEdit = computed(() => {
  if (!prompt.value || !userStore.isLogin) return false
  return userStore.isAdmin || prompt.value.userId === userStore.userInfo.id
})

// 点赞
const handleLike = async () => {
  if (!userStore.isLogin) {
    message.warning('请先登录')
    return
  }
  
  try {
    if (prompt.value.liked) {
      // 取消点赞
      const res = await api.cancelVote(prompt.value.id)
      if (res.code === 200) {
        prompt.value.liked = false
        prompt.value.likeCount--
        message.success('已取消点赞')
      }
    } else {
      // 点赞
      const res = await api.likePrompt(prompt.value.id)
      if (res.code === 200) {
        prompt.value.liked = true
        prompt.value.likeCount++
        
        // 如果之前点踩了，取消点踩
        if (prompt.value.disliked) {
          prompt.value.disliked = false
          prompt.value.dislikeCount--
        }
        
        message.success('点赞成功')
      }
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 点踩
const handleDislike = async () => {
  if (!userStore.isLogin) {
    message.warning('请先登录')
    return
  }
  
  try {
    if (prompt.value.disliked) {
      // 取消点踩
      const res = await api.cancelVote(prompt.value.id)
      if (res.code === 200) {
        prompt.value.disliked = false
        prompt.value.dislikeCount--
        message.success('已取消点踩')
      }
    } else {
      // 点踩
      const res = await api.dislikePrompt(prompt.value.id)
      if (res.code === 200) {
        prompt.value.disliked = true
        prompt.value.dislikeCount++
        
        // 如果之前点赞了，取消点赞
        if (prompt.value.liked) {
          prompt.value.liked = false
          prompt.value.likeCount--
        }
        
        message.success('点踩成功')
      }
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 收藏
const handleFavorite = async () => {
  if (!userStore.isLogin) {
    message.warning('请先登录')
    return
  }
  
  try {
    if (prompt.value.favorited) {
      // 取消收藏
      const res = await api.cancelFavorite(prompt.value.id)
      if (res.code === 200) {
        prompt.value.favorited = false
        prompt.value.favoriteCount--
        message.success('已取消收藏')
      }
    } else {
      // 收藏
      const res = await api.favoritePrompt(prompt.value.id)
      if (res.code === 200) {
        prompt.value.favorited = true
        prompt.value.favoriteCount++
        message.success('收藏成功')
      }
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 复制提示词
const copyPrompt = () => {
  navigator.clipboard.writeText(prompt.value.content)
    .then(() => {
      message.success('提示词已复制到剪贴板')
    })
    .catch(() => {
      message.error('复制失败，请手动复制')
    })
}

// 编辑提示词
const handleEdit = () => {
  router.push(`/prompt/edit/${prompt.value.id}`)
}

// 删除提示词
const handleDelete = async () => {
  try {
    const res = await api.deletePrompt(prompt.value.id)
    if (res.code === 200) {
      message.success('删除成功')
      router.push('/prompt/square')
    } else {
      message.error('删除失败')
    }
  } catch (error) {
    message.error('删除失败，请稍后重试')
  }
}

// 显示举报弹窗
const showReportModal = () => {
  if (!userStore.isLogin) {
    message.warning('请先登录')
    return
  }
  reportModalVisible.value = true
}

// 提交举报
const submitReport = async () => {
  if (!reportForm.reason) {
    message.warning('请输入举报理由')
    return
  }
  
  reportLoading.value = true
  try {
    const res = await api.reportPrompt({
      promptId: prompt.value.id,
      reason: reportForm.reason
    })
    if (res.code === 200) {
      message.success('举报成功，管理员会尽快处理')
      reportModalVisible.value = false
      reportForm.reason = ''
    } else {
      message.error('举报失败')
    }
  } catch (error) {
    message.error('举报失败，请稍后重试')
  } finally {
    reportLoading.value = false
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

onMounted(() => {
  fetchPromptDetail()
})
</script>

<style scoped>
.prompt-detail {
  padding-bottom: 40px;
}

.prompt-detail-card {
  background: white;
  border-radius: var(--border-radius-large);
  box-shadow: var(--box-shadow);
  padding: 32px;
  margin-bottom: 24px;
}

.prompt-header {
  margin-bottom: 24px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 24px;
}

.prompt-title {
  font-size: 28px;
  margin-bottom: 16px;
}

.prompt-tags {
  margin-bottom: 16px;
}

.prompt-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: var(--text-color-light);
}

.prompt-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.prompt-content-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.prompt-content, .prompt-image {
  background: var(--background-color);
  border-radius: var(--border-radius-base);
  padding: 16px;
}

.prompt-content h3, .prompt-image h3 {
  margin-bottom: 16px;
  font-size: 18px;
}

.prompt-text {
  position: relative;
}

.prompt-text pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  background: white;
  padding: 16px;
  border-radius: var(--border-radius-base);
  border: 1px solid var(--border-color);
  margin-bottom: 16px;
  font-family: inherit;
  font-size: 14px;
  line-height: 1.6;
}

.image-wrapper {
  text-align: center;
}

.image-wrapper img {
  max-width: 100%;
  max-height: 400px;
  border-radius: var(--border-radius-base);
}

.prompt-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid var(--border-color);
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.edit-buttons {
  display: flex;
  gap: 12px;
}

@media (max-width: 768px) {
  .prompt-content-wrapper {
    grid-template-columns: 1fr;
  }
  
  .prompt-actions {
    flex-direction: column;
    gap: 16px;
  }
  
  .action-buttons, .edit-buttons {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
