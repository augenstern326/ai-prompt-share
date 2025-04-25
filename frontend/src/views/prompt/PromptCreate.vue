<template>
  <div class="prompt-create">
    <div class="container">
      <div class="prompt-create-header">
        <h1>{{ isEdit ? '编辑提示词' : '创建提示词' }}</h1>
        <p>{{ isEdit ? '修改您的提示词内容和信息' : '分享您的AI提示词，帮助更多人创作' }}</p>
      </div>

      <div class="prompt-create-form">
        <a-form
          :model="formData"
          :rules="rules"
          layout="vertical"
          @finish="handleSubmit"
        >
          <a-form-item label="标题" name="title">
            <a-input v-model:value="formData.title" placeholder="请输入提示词标题" />
          </a-form-item>

          <a-form-item label="提示词内容" name="content">
            <a-textarea
              v-model:value="formData.content"
              placeholder="请输入提示词内容"
              :rows="8"
              :maxlength="1000"
              :showCount="true"
              :countProps="{ separator: '/' }"
            />
          </a-form-item>

          <a-form-item label="标签" name="tagIds">
            <a-select
                mode="tags"
                placeholder="选择或创建标签"
                :max-tag-count="5"
                :options="tagOptions"
                :loading="tagsLoading"
                :tokenSeparators="[',']"
                show-search
                @change="handleTagChange"
            >
              <template #dropdownRender="{ menuNode: menu }">
                <v-nodes :vnodes="menu" />
                <a-divider style="margin: 4px 0" />
                <a-space style="padding: 4px 8px">
                  <a-input ref="inputRef" v-model:value="newTagName" placeholder="自定义标签名称" />
                  <a-button type="text" @click="addTag">
                    <template #icon>
                      <plus-outlined />
                    </template>
                    自定义标签
                  </a-button>
                </a-space>
              </template>
            </a-select>
          </a-form-item>


          <a-form-item label="效果图片" name="imageUrl">
            <a-upload
                :maxCount="1"
                v-model:fileList="fileList"
                list-type="picture-card"
                :data = "requestData"
                :show-upload-list="true"
                action="http://localhost:8000/api/file/upload"
                :before-upload="beforeUpload"
                @change="handleChange"
                @preview="handlePreview"
            >
              <div v-if="fileList.length < 1">
                <upload-outlined />
                <div style="margin-top: 8px">上传图片</div>
              </div>
            </a-upload>
            <a-modal
                :visible="previewVisible"
                :title="previewTitle"
                :footer="null"
                @cancel="previewVisible = false"
            >
              <img alt="预览图片" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" html-type="submit" :loading="submitLoading">
              {{ isEdit ? '保存修改' : '创建提示词' }}
            </a-button>
            <a-button style="margin-left: 10px" @click="goBack">取消</a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, computed, onMounted, defineComponent} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UploadOutlined,PlusOutlined } from '@ant-design/icons-vue'
import * as api from '../../api'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => route.path.includes('/edit/'))
const promptId = computed(() => isEdit.value ? route.params.id : null)
const submitLoading = ref(false)
const tagsLoading = ref(false)
const tagOptions = ref([])
const requestData = ref({directory:"prompt"})
const newTagName = ref('')
const inputRef = ref();
const fileList = ref([])
const previewVisible = ref(false)
const previewImage = ref('')
const previewTitle = ref('')

const VNodes = defineComponent({
  props: {
    vnodes: {
      type: Object,
      required: true,
    },
  },
  render() {
    return this.vnodes;
  },
});
// 表单数据
const formData = reactive({
  title: '',
  content: '',
  tagIds: [],
  imageUrl: ''
})



// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入提示词标题', trigger: 'blur' },
    { max: 100, message: '标题长度不能超过100个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入提示词内容', trigger: 'blur' }
  ],
  tagIds: [
    { required: true, type: 'array', min: 1, message: '请至少选择一个标签', trigger: 'change' }
  ]
}


// 获取标签列表
const fetchTags = async () => {
  tagsLoading.value = true
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
  } finally {
    tagsLoading.value = false
  }
}

//添加新标签
const addTag = async () => {
  if (!newTagName.value) {
    message.warning('请输入标签名称')
    return
  }

  try {
    const res = await api.createTag({
      name: newTagName.value,
      type: 1 // 用户创建
    })
    if (res.code === 200) {
      const newTagId = res.data
      tagOptions.value.push({
        value:newTagId,
        label:newTagName.value
      })
      formData.tagIds.push(newTagId)
      newTagName.value = ''
      message.success('标签创建成功')
      console.log(formData.tagIds)
    }
  } catch (error) {
    message.error('创建标签失败，请稍后重试')
  }
}

//处理标签变化
const handleTagChange = (value) => {
  formData.tagIds = value
  console.log(formData.tagIds)
}


// 上传图片前的处理
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件!')
    return false
  }

  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('图片大小不能超过2MB!')
  }
  requestData.value.file = file;
  return true;
}

// 处理图片预览
const handlePreview = async (file) => {
  if (!file.url && !file.preview) {
    file.preview = await getBase64(file.originFileObj)
  }
  previewImage.value = file.url || file.preview
  previewVisible.value = true
  previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1)
}

// 处理图片变化
const handleChange = (info) => {
  if (info.file.status !== 'uploading') {
    console.log(info.file, info.fileList)
  }
  if (info.file.status === 'done') {
    message.success(`${info.file.name} 上传成功`)
    formData.imageUrl = info.file.response.data.url
  } else if (info.file.status === 'error') {
    message.error(`${info.file.name} 上传失败`)
  }
}

// 将文件转为Base64
const getBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}


// 获取提示词详情（编辑模式）
const fetchPromptDetail = async () => {
  if (!isEdit.value) return

  try {
    const res = await api.getPromptDetail(promptId.value)
    if (res.code === 200) {
      const { title, content, imageUrl, tags } = res.data
      formData.title = title
      formData.content = content
      formData.imageUrl = imageUrl
      formData.tagIds = tags.map(tag => tag.id)

      if (imageUrl) {
        fileList.value = [{
          uid: '-1',
          name: imageUrl.substring(imageUrl.lastIndexOf('/') + 1),
          status: 'done',
          url: imageUrl
        }]
      }
    }
  } catch (error) {
    message.error('获取提示词详情失败')
    router.push('/prompt/square')
  }
}

// 提交表单
const handleSubmit = async () => {
  formData.tagIds = formData.tagIds.filter(item => item !== null && item !== '');
  if (formData.tagIds.length === 0) {
    message.warning('请至少选择一个有效的标签');
    return;
  }
  submitLoading.value = true
  try {
    let res
    if (isEdit.value) {
      // 更新提示词
      res = await api.updatePrompt({
        id: promptId.value,
        ...formData
      })
    } else {
      // 创建提示词
      res = await api.createPrompt(formData)
    }

    if (res.code === 200) {
      message.success(isEdit.value ? '提示词更新成功' : '提示词创建成功')
      if (isEdit.value) {
        router.push(`/prompt/detail/${promptId.value}`)
      } else {
        router.push(`/prompt/detail/${res.data}`)
      }
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    message.error('操作失败，请稍后重试')
  } finally {
    submitLoading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchTags()
  fetchPromptDetail()
})
</script>

<style scoped>
.prompt-create {
  padding-bottom: 40px;
}

.prompt-create-header {
  text-align: center;
  margin-bottom: 32px;
}

.prompt-create-header h1 {
  font-size: 32px;
  margin-bottom: 8px;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.prompt-create-header p {
  color: var(--text-color-light);
  font-size: 16px;
}

.prompt-create-form {
  background: white;
  border-radius: var(--border-radius-large);
  box-shadow: var(--box-shadow);
  padding: 32px;
  max-width: 800px;
  margin: 0 auto;
}

</style>
