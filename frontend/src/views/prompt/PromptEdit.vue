<template>
  <div class="prompt-edit">
    <a-card title="编辑提示词">
      <a-form :model="formState" :rules="rules" ref="formRef" layout="vertical">
        <a-form-item label="标题" name="title">
          <a-input v-model:value="formState.title" placeholder="请输入提示词标题"/>
        </a-form-item>

        <a-form-item label="内容" name="content">
          <a-textarea
              v-model:value="formState.content"
              placeholder="请输入提示词内容"
              :auto-size="{ minRows: 5, maxRows: 15 }"
          />
        </a-form-item>

        <a-form-item label="标签" name="tagIds">
          <a-select
              v-model:value="formState.tagIds"
              mode="tags"
              placeholder="选择或创建标签"
              :max-tag-count="5"
              :options="tagOptions"
              :tokenSeparators="[',']"
              show-search
              @change="handleTagChange"
          >
            <template #dropdownRender="{ menuNode: menu }">
              <v-nodes :vnodes="menu"/>
              <a-divider style="margin: 4px 0"/>
              <a-space style="padding: 4px 8px">
                <a-input ref="inputRef" v-model:value="newTagName" placeholder="自定义标签名称"/>
                <a-button type="text" @click="addTag">
                  <template #icon>
                    <plus-outlined/>
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
              :data="requestData"
              :show-upload-list="true"
              action="http://localhost:8000/api/file/upload"
              :before-upload="beforeUpload"
              @change="handleChange"
              @preview="handlePreview"
              @remove="handleRemove"
          >
            <div v-if="fileList.length < 1">
              <upload-outlined/>
              <div style="margin-top: 8px">上传图片</div>
            </div>
          </a-upload>
          <a-modal
              :visible="previewVisible"
              :title="previewTitle"
              :footer="null"
              @cancel="previewVisible = false"
          >
            <img alt="预览图片" style="width: 100%" :src="previewImage"/>
          </a-modal>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="onSubmit">保存</a-button>
          <a-button style="margin-left: 10px" @click="onCancel">取消</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, defineComponent} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import {message} from 'ant-design-vue';
import {PlusOutlined, UploadOutlined} from '@ant-design/icons-vue';
import {getPromptDetail, updatePrompt} from '@/api';
import {getTagList, createTag} from '@/api';

const router = useRouter();
const route = useRoute();
const formRef = ref();
const fileList = ref([]);
const previewVisible = ref(false);
const previewImage = ref('');
const previewTitle = ref('');
const newTagName = ref('');
const requestData = ref({directory: 'prompt'});

const tagOptions = ref([])
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
const formState = reactive({
  id: null,
  title: '',
  content: '',
  tagIds: [],
  imageUrl: ''
});

const rules = {
  title: [{required: true, message: '请输入标题', trigger: 'blur'}],
  content: [{required: true, message: '请输入内容', trigger: 'blur'}],
  tagIds: [{required: true, message: '请选择至少一个标签', type: 'array'}]
};

//添加新标签
const addTag = async () => {
  if (!newTagName.value) {
    message.warning('请输入标签名称')
    return
  }

  try {
    const res = await createTag({
      name: newTagName.value,
      type: 1 // 用户创建
    })
    if (res.code === 200) {
      const newTagId = res.data
      tagOptions.value.push({
        value: newTagId,
        label: newTagName.value
      })
      formState.tagIds.push(newTagId)
      newTagName.value = ''
      message.success('标签创建成功')
    }
  } catch (error) {
    message.error('创建标签失败，请稍后重试')
  }
}

//处理标签变化
const handleTagChange = (value) => {
  formState.tagIds = value
}
onMounted(async () => {
  try {
    // 获取标签列表
    const res = await getTagList();
    tagOptions.value = res.data.map(tag => ({
      label: tag.name,
      value: tag.id
    }))
    // 获取提示词详情
    const promptId = route.params.id;
    if (promptId) {
      const promptRes = await getPromptDetail(promptId);
      const promptData = promptRes.data;
      formState.id = promptData.id;
      formState.title = promptData.title;
      formState.content = promptData.content;
      formState.tagIds = promptData.tags.map(tag => tag.id);
      formState.imageUrl = promptData.imageUrl;

      if (promptData.imageUrl) {
        fileList.value = [
          {
            uid: '-1',
            name: 'image.png',
            status: 'done',
            url: promptData.imageUrl
          }
        ];
      }
    }
  } catch (error) {
    message.error('获取数据失败');
  }
});

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
  if (info.file.status === 'done') {
    message.success(`${info.file.name} 上传成功`)
    formState.imageUrl = info.file.response.data.url
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

// 删除文件的回调
const handleRemove = ()=>{
  formState.imageUrl = ""
}

const onSubmit = () => {
  formState.tagIds = formState.tagIds.filter(item => item !== null && item !== '');
  if (formState.tagIds.length === 0) {
    message.warning('请至少选择一个有效的标签');
    return;
  }
  formRef.value
      .validate()
      .then(async () => {
        try {
          await updatePrompt(formState);
          message.success('保存成功');
          router.push('/prompt/detail/' + formState.id);
        } catch (error) {
          message.error('保存失败');
        }
      })
      .catch(error => {
        console.log('验证失败', error);
      });
};

const onCancel = () => {
  router.back();
};
</script>

<style scoped>
.prompt-edit {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style>
