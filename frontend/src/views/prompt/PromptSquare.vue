<template>
  <div class="prompt-square">
    <div class="container">
      <div class="prompt-square-header">
        <h1>提示词广场</h1>
        <p>发现和探索高质量的AI提示词</p>
      </div>
      
      <!-- 搜索和筛选 -->
      <div class="filter-section">
        <div class="search-box">
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索提示词"
            enter-button
            @search="handleSearch"
          />
        </div>
        
        <div class="tag-filter">
          <a-select
            v-model:value="selectedTags"
            mode="multiple"
            placeholder="选择标签筛选"
            style="width: 100%"
            :options="tagOptions"
            @change="handleTagChange"
          />
        </div>
        
        <div class="sort-filter">
          <a-select
            v-model:value="sortType"
            style="width: 120px"
            @change="handleSortChange"
          >
            <a-select-option :value="0">最新</a-select-option>
            <a-select-option :value="1">热门</a-select-option>
            <a-select-option :value="2">收藏</a-select-option>
          </a-select>
        </div>
      </div>
      
      <!-- 提示词列表 -->
      <div class="prompt-list">
        <a-spin :spinning="loading">
          <template v-if="prompts.length > 0">
            <div class="prompt-cards">
              <div v-for="prompt in prompts" :key="prompt.id" class="prompt-card-wrapper">
                <router-link :to="`/prompt/detail/${prompt.id}`">
                  <div class="prompt-card">
                    <div class="prompt-card-image">
                      <img :src="prompt.imageUrl" :alt="prompt.title" />
                    </div>
                    <div class="prompt-card-content">
                      <h3 class="prompt-card-title">{{ prompt.title }}</h3>
                      <div class="prompt-card-tags">
                        <span v-for="tag in prompt.tags.slice(0, 3)" :key="tag.id" class="tag">{{ tag.name }}</span>
                      </div>
                      <div class="prompt-card-footer">
                        <div class="prompt-card-stats">
                          <span><like-outlined /> {{ prompt.likeCount }}</span>
                          <span><dislike-outlined /> {{ prompt.dislikeCount }}</span>
                          <span><star-outlined /> {{ prompt.favoriteCount }}</span>
                        </div>
                        <div class="prompt-card-user">
                          <a-avatar :src="prompt.userAvatar" size="small" />
                          <span>{{ prompt.username }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </router-link>
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
          <a-empty v-else description="暂无数据" />
        </a-spin>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as api from '../../api'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const prompts = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(12)
const searchKeyword = ref('')
const selectedTags = ref([])
const tagOptions = ref([])
const sortType = ref(0)

import {
  LikeOutlined,
  DislikeOutlined,
  StarOutlined

} from '@ant-design/icons-vue'
// 获取标签列表
const fetchTags = async () => {
  try {
    const res = await api.getTagList()
    if (res.code === 200) {
      tagOptions.value = res.data.map(tag => ({
        label: tag.name,
        value: tag.id
      }))
    }
  } catch (error) {
    console.error('获取标签列表失败', error)
  }
}

// 获取提示词列表
const fetchPrompts = async () => {
  loading.value = true
  try {
    const res = await api.getPromptList({
      current: current.value,
      size: size.value,
      keyword: searchKeyword.value,
      tagIds: selectedTags.value.length > 0 ? selectedTags.value : undefined,
      sortType: sortType.value
    })
    if (res.code === 200) {
      prompts.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取提示词列表失败', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  current.value = 1
  fetchPrompts()
}

// 标签筛选
const handleTagChange = () => {
  current.value = 1
  fetchPrompts()
}

// 排序方式
const handleSortChange = () => {
  current.value = 1
  fetchPrompts()
}

// 分页
const handlePageChange = (page) => {
  current.value = page
  fetchPrompts()
}

// 从URL参数中获取筛选条件
const getParamsFromUrl = () => {
  const { keyword, tags, sort, page } = route.query
  
  if (keyword) {
    searchKeyword.value = keyword
  }
  
  if (tags) {
    selectedTags.value = tags.split(',').map(id => Number(id))
  }
  
  if (sort !== undefined) {
    sortType.value = Number(sort)
  }
  
  if (page) {
    current.value = Number(page)
  }
}

// 更新URL参数
const updateUrlParams = () => {
  const query = {}
  
  if (searchKeyword.value) {
    query.keyword = searchKeyword.value
  }
  
  if (selectedTags.value.length > 0) {
    query.tags = selectedTags.value.join(',')
  }
  
  if (sortType.value !== 0) {
    query.sort = sortType.value
  }
  
  if (current.value !== 1) {
    query.page = current.value
  }
  
  router.push({ query })
}

// 监听筛选条件变化，更新URL
watch([searchKeyword, selectedTags, sortType, current], () => {
  updateUrlParams()
})

onMounted(() => {
  getParamsFromUrl()
  fetchTags()
  fetchPrompts()
})
</script>

<style scoped>
.prompt-square {
  padding-bottom: 40px;
}

.prompt-square-header {
  text-align: center;
  margin-bottom: 32px;
}

.prompt-square-header h1 {
  font-size: 32px;
  margin-bottom: 8px;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.prompt-square-header p {
  color: var(--text-color-light);
  font-size: 16px;
}

.filter-section {
  display: grid;
  grid-template-columns: 1fr 2fr 120px;
  gap: 16px;
  margin-bottom: 24px;
}

.prompt-list {
  margin-top: 24px;
}

.prompt-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 32px;
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

.pagination {
  text-align: center;
  margin-top: 32px;
}

@media (max-width: 1200px) {
  .prompt-cards {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 992px) {
  .prompt-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .filter-section {
    grid-template-columns: 1fr 1fr;
  }
  
  .sort-filter {
    grid-column: 1 / -1;
    justify-self: end;
  }
}

@media (max-width: 576px) {
  .prompt-cards {
    grid-template-columns: 1fr;
  }
  
  .filter-section {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .sort-filter {
    justify-self: start;
  }
}
</style>
