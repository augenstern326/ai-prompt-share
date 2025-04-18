<template>
  <div class="prompt-edit">
    <a-card title="编辑提示词">
      <a-form :model="formState" :rules="rules" ref="formRef" layout="vertical">
        <a-form-item label="标题" name="title">
          <a-input v-model:value="formState.title" placeholder="请输入提示词标题" />
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
            mode="multiple"
            placeholder="请选择标签"
            style="width: 100%"
          >
            <a-select-option v-for="tag in tagList" :key="tag.id" :value="tag.id">
              {{ tag.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="效果图" name="imageUrl">
          <a-upload
            v-model:file-list="fileList"
            list-type="picture-card"
            :before-upload="beforeUpload"
            @change="handleChange"
          >
            <div v-if="fileList.length < 1">
              <upload-outlined />
              <div style="margin-top: 8px">上传</div>
            </div>
          </a-upload>
        </a-form-item>
        
        <a-form-item>
          <a-button type="primary" @click="onSubmit">保存</a-button>
          <a-button style="margin-left: 10px" @click="onCancel">取消</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import { UploadOutlined } from '@ant-design/icons-vue';
import { getPromptDetail, updatePrompt } from '@/api';
import { getTagList } from '@/api';
import { uploadFile } from '@/api';

export default {
  name: 'PromptEditPage',
  components: {
    UploadOutlined
  },
  setup() {
    const router = useRouter();
    const route = useRoute();
    const formRef = ref();
    const tagList = ref([]);
    const fileList = ref([]);
    
    const formState = reactive({
      id: null,
      title: '',
      content: '',
      tagIds: [],
      imageUrl: ''
    });
    
    const rules = {
      title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
      content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
      tagIds: [{ required: true, message: '请选择至少一个标签', type: 'array' }]
    };
    
    onMounted(async () => {
      try {
        // 获取标签列表
        const res = await getTagList();
        tagList.value = res.data.list;
        
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
    
    const beforeUpload = (file) => {
      const isImage = file.type.startsWith('image/');
      if (!isImage) {
        message.error('只能上传图片文件!');
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        message.error('图片必须小于2MB!');
      }
      return false;
    };
    
    const handleChange = async (info) => {
      if (info.file.status !== 'uploading') {
        const formData = new FormData();
        formData.append('file', info.file.originFileObj);
        formData.append('directory', 'prompt');
        
        try {
          const res = await uploadFile(info.file.originFileObj, 'prompt');
          formState.imageUrl = res.data.objectName;
          message.success('上传成功');
        } catch (error) {
          message.error('上传失败');
        }
      }
    };
    
    const onSubmit = () => {
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
    
    return {
      formRef,
      formState,
      rules,
      tagList,
      fileList,
      beforeUpload,
      handleChange,
      onSubmit,
      onCancel
    };
  }
};
</script>

<style scoped>
.prompt-edit {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style>
