<template>
  <div class="user-favorite">
    <div class="container">
      <div class="user-favorite-header">
        <h1>我的收藏</h1>
        <p>您收藏的所有提示词</p>
      </div>
      
      <!-- 提示词列表 -->
      <div class="favorite-list">
        <a-spin :spinning="loading">
          <template v-if="prompts.length > 0">
            <div class="prompt-cards">
              <div v-for="prompt in prompts" :key="prompt.id" class="prompt-card-wrapper">
                <router-link :to="`/prompt/detail/${prompt.id}`">
                  <div class="prompt-card">
                    <div class="prompt-card-image">
                      <img :src="prompt.imageUrl || '/default-prompt.jpg'" :alt="prompt.title" />
                    </div>
                    <div class="prompt-card-content">
                      <h3 class="prompt-card-title">{{ prompt.title }}</h3>
                      <div class="prompt-card-tags">
                        <span v-for="tag in prompt.tags.slice(0, 3)" :key="tag.id" class="tag">{{ tag.name }}</span>
                      </div>
                      <div class="prompt-card-footer">
                        <div class="prompt-card-stats">
                          <span><a-icon type="like" /> {{ prompt.likeCount }}</span>
                          <span><a-icon type="dislike" /> {{ prompt.dislikeCount }}</span>
                          <span><a-icon type="star" /> {{ prompt.favoriteCount }}</span>
                        </div>
                        <div class="prompt-card-user">
                          <a-avatar :src="prompt.userAvatar || '/avatar-default.png'" size="small" />
                          <span>{{ prompt.username }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </router-link>
                <div class="card-actions">
                  <a-button type="primary" @click="cancelFavorite(prompt.id)">
                    取消收藏
                  </a-button>
                </div>
              </div>
            </div>
            
            <!-- 分页 -->
            <div class="pagination">
              <a-pagination
                v-model:current="current"
                :total="total"
                :pageSize="size"
                show-quick-jumper
                @change="handlePageChange"
              />
            </div>
          </template>
          <a-empty v-else description="暂无收藏的提示词" />
        </a-spin>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import api from '../../api'

const loading = ref(false)
const prompts = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(12)

// 获取收藏列表
const fetchFavorites = async () => {
  loading.value = true
  try {
    const res = await api.prompt.getFavoriteList({
      current: current.value,
      size: size.value
    })
    if (res.code === 200) {
      prompts.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取收藏列表失败', error)
    message.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 取消收藏
const cancelFavorite = async (promptId) => {
  try {
    const res = await api.prompt.cancelFavorite(promptId)
    if (res.code === 200) {
      message.success('已取消收藏')
      // 从列表中移除
      prompts.value = prompts.value.filter(item => item.id !== promptId)
      total.value--
    } else {
      message.error('操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  }
}

// 分页
const handlePageChange = (page) => {
  current.value = page
  fetchFavorites()
}

onMounted(() => {
  fetchFavorites()
})
</script>

<style scoped>
.user-favorite {
  padding-bottom: 40px;
}

.user-favorite-header {
  text-align: center;
  margin-bottom: 32px;
}

.user-favorite-header h1 {
  font-size: 32px;
  margin-bottom: 8px;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.user-favorite-header p {
  color: var(--text-color-light);
  font-size: 16px;
}

.favorite-list {
  margin-top: 24px;
}

.prompt-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.prompt-card-wrapper {
  width: 100%;
  position: relative;
}

.prompt-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.prompt-card-image {
  height: 180px;
  overflow: hidden;
}

.prompt-card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.prompt-card:hover .prompt-card-image img {
  transform: scale(1.05);
}

.prompt-card-content {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.prompt-card-title {
  font-size: 16px;
  margin: 0 0 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.prompt-card-tags {
  margin-bottom: 12px;
}

.prompt-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.prompt-card-stats {
  display: flex;
  gap: 12px;
  color: var(--text-color-light);
}

.prompt-card-user {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-color-light);
  font-size: 12px;
}

.card-actions {
  position: absolute;
  bottom: 16px;
  right: 16px;
  opacity: 0;
  transition: opacity 0.3s;
}

.prompt-card-wrapper:hover .card-actions {
  opacity: 1;
}

.pagination {
  text-align: center;
  margin-top: 32px;
}

@media (max-width: 1200px) {
  .prompt-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .prompt-cards {
    grid-template-columns: 1fr;
  }
  
  .card-actions {
    opacity: 1;
  }
}
</style>
