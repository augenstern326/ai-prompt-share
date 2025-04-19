import axios from 'axios';

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8000/api', // API的base_url
  timeout: 15000 // 请求超时时间
});

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token');
    if (token) {
      // 设置请求头
      config.headers['Authorization'] = token;
    }
    return config;
  },
  error => {
    console.log(error);
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data;
    // 如果返回的状态码不是200，说明接口请求有误
    if (res.code !== 200) {
      // 401: 未登录或token过期
      if (res.code === 401) {
        // 清除token
        localStorage.removeItem('token');
        // 跳转到登录页
        window.location.href = '/login';
      }
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      return res;
    }
  },
  error => {
    console.log('err' + error);
    // 处理网络错误
    if (error.response) {
      const status = error.response.status;
      switch (status) {
        case 401:
          // 清除token
          localStorage.removeItem('token');
          // 跳转到登录页
          window.location.href = '/login';
          break;
        case 403:
        case 404:
        case 500:
        default:
          // 错误处理已在控制台记录
          break;
      }
    }
    return Promise.reject(error);
  }
);

export default request;
