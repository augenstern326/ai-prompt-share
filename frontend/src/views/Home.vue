<template>
  <div class="home-page">
    <!-- 顶部横幅 -->
    <div class="banner gradient-bg">
      <div class="container banner-content">
        <div class="banner-text">
          <h1>AI提示词分享平台</h1>
          <p>发现、分享和收藏高质量的AI提示词，激发创作灵感</p>
          <div class="banner-buttons">
            <router-link to="/prompt/square" class="banner-btn">浏览提示词</router-link>
            <router-link v-if="userStore.isLogin" to="/prompt/create" class="banner-btn">上传提示词</router-link>
            <router-link v-else to="/login" class="banner-btn">立即登录</router-link>
          </div>
        </div>
        <div class="banner-image">
          <img src="../assets/images/banner-illustration.svg" alt="AI提示词" />
        </div>
      </div>
    </div>

    <!-- 热门提示词 -->
    <div class="container section">
      <div class="section-header">
        <h2>热门提示词</h2>
        <router-link to="/prompt/square" class="view-more">查看更多</router-link>
      </div>
      <div class="prompt-grid">
        <a-spin :spinning="loading">
          <template v-if="hotPrompts.length > 0">
            <div class="prompt-cards">
              <div v-for="prompt in hotPrompts" :key="prompt.id" class="prompt-card-wrapper">
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
              </div>
            </div>
          </template>
          <a-empty v-else description="暂无数据" />
        </a-spin>
      </div>
    </div>

    <!-- 最新提示词 -->
    <div class="container section">
      <div class="section-header">
        <h2>最新提示词</h2>
        <router-link to="/prompt/square" class="view-more">查看更多</router-link>
      </div>
      <div class="prompt-grid">
        <a-spin :spinning="loading">
          <template v-if="latestPrompts.length > 0">
            <div class="prompt-cards">
              <div v-for="prompt in latestPrompts" :key="prompt.id" class="prompt-card-wrapper">
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
              </div>
            </div>
          </template>
          <a-empty v-else description="暂无数据" />
        </a-spin>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import * as api from '../api'

export default {
  name: 'HomePage',
  setup() {
    const userStore = useUserStore()
    const loading = ref(false)
    const hotPrompts = ref([])
    const latestPrompts = ref([])

    // 获取热门提示词
    const fetchHotPrompts = async () => {
      try {
        loading.value = true
        const res = await api.getPromptList({
          current: 1,
          size: 6,
          sortType: 1 // 按热度排序
        })
        if (res.code === 200) {
          hotPrompts.value = res.data.records
        }
      } catch (error) {
        console.error('获取热门提示词失败', error)
      } finally {
        loading.value = false
      }
    }

    // 获取最新提示词
    const fetchLatestPrompts = async () => {
      try {
        loading.value = true
        const res = await api.getPromptList({
          current: 1,
          size: 6,
          sortType: 0 // 按最新排序
        })
        if (res.code === 200) {
          latestPrompts.value = res.data.records
        }
      } catch (error) {
        console.error('获取最新提示词失败', error)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      fetchHotPrompts()
      fetchLatestPrompts()
    })

    return {
      userStore,
      loading,
      hotPrompts,
      latestPrompts
    }
  }
}
</script>

<style scoped>
.home-page {
  padding-bottom: 40px;
}

.banner {
  padding: 60px 0;
  margin-bottom: 40px;
}

.banner-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.banner-text {
  flex: 1;
}

.banner-text h1 {
  font-size: 36px;
  margin-bottom: 16px;
  color: white;
}

.banner-text p {
  font-size: 18px;
  margin-bottom: 24px;
  color: rgba(255, 255, 255, 0.9);
}

.banner-buttons {
  display: flex;
  gap: 16px;
}

.banner-btn {
  display: inline-block;
  padding: 10px 24px;
  border-radius: var(--border-radius-base);
  font-weight: bold;
  transition: all 0.3s;
}

.banner-btn:first-child {
  background: white;
  color: var(--primary-color);
}

.banner-btn:first-child:hover {
  background: rgba(255, 255, 255, 0.9);
}

.banner-btn:last-child {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.banner-btn:last-child:hover {
  background: rgba(255, 255, 255, 0.3);
}

.banner-image {
  flex: 1;
  text-align: right;
}

.banner-image img {
  max-width: 100%;
  height: auto;
}

.section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 24px;
  margin: 0;
}

.view-more {
  color: var(--primary-color);
}

.prompt-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.prompt-card-wrapper {
  width: 100%;
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

@media (max-width: 992px) {
  .prompt-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .banner-content {
    flex-direction: column;
    text-align: center;
  }
  
  .banner-image {
    margin-top: 24px;
    text-align: center;
  }
  
  .banner-buttons {
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .prompt-cards {
    grid-template-columns: 1fr;
  }
}
</style>
