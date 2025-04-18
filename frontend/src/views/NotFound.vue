<template>
  <div class="not-found">
    <h1>404</h1>
    <h2>页面未找到</h2>
    <p>抱歉，您访问的页面不存在。</p>
    <router-link to="/">返回首页</router-link>
  </div>
</template>

<script>
export default {
  name: 'NotFoundPage'
}
</script>

<style scoped>
.not-found {
  text-align: center;
  padding: 50px 0;
}
h1 {
  font-size: 72px;
  color: #1890ff;
  margin-bottom: 20px;
}
h2 {
  font-size: 24px;
  margin-bottom: 20px;
}
p {
  margin-bottom: 30px;
}
a {
  color: #1890ff;
  text-decoration: none;
}
</style>
