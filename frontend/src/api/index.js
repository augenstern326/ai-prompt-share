import request from '@/utils/request';

// 用户登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  });
}

// 用户注册
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  });
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  });
}

// 更新用户信息
export function updateUserInfo(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  });
}

// 获取提示词列表
export function getPromptList(params) {
  return request({
    url: '/prompt/list',
    method: 'get',
    params
  });
}

// 获取提示词详情
export function getPromptDetail(id) {
  return request({
    url: `/prompt/detail/${id}`,
    method: 'get'
  });
}

// 创建提示词
export function createPrompt(data) {
  return request({
    url: '/prompt/create',
    method: 'post',
    data
  });
}

// 更新提示词
export function updatePrompt(data) {
  return request({
    url: '/prompt/update',
    method: 'put',
    data
  });
}

// 删除提示词
export function deletePrompt(id) {
  return request({
    url: `/prompt/delete/${id}`,
    method: 'delete'
  });
}

// 点赞提示词
export function likePrompt(id) {
  return request({
    url: `/prompt/like/${id}`,
    method: 'post'
  });
}

// 点踩提示词
export function dislikePrompt(id) {
  return request({
    url: `/prompt/dislike/${id}`,
    method: 'post'
  });
}

// 取消点赞/点踩
export function cancelVote(id) {
  return request({
    url: `/prompt/cancel-vote/${id}`,
    method: 'post'
  });
}

// 收藏提示词
export function favoritePrompt(id) {
  return request({
    url: `/prompt/favorite/${id}`,
    method: 'post'
  });
}

// 取消收藏
export function cancelFavorite(id) {
  return request({
    url: `/prompt/cancel-favorite/${id}`,
    method: 'post'
  });
}

// 获取收藏列表
export function getFavoriteList(params) {
  return request({
    url: '/prompt/favorite/list',
    method: 'get',
    params
  });
}

// 获取标签列表
export function getTagList() {
  return request({
    url: '/tag/list',
    method: 'get'
  });
}

// 创建标签
export function createTag(data) {
  return request({
    url: '/tag/create',
    method: 'post',
    data
  });
}

// 上传文件
export function uploadFile(file, directory) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('directory', directory);
  return request({
    url: '/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 获取文件URL
export function getFileUrl(objectName) {
  return request({
    url: '/file/url',
    method: 'get',
    params: { objectName }
  });
}

// 删除文件
export function deleteFile(objectName) {
  return request({
    url: '/file/delete',
    method: 'delete',
    params: { objectName }
  });
}

// 举报提示词
export function reportPrompt(data) {
  return request({
    url: '/report/create',
    method: 'post',
    data
  });
}

// 管理员API
// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params
  });
}

// 更新用户信息（管理员）
export function adminUpdateUser(data) {
  return request({
    url: '/admin/user/update',
    method: 'put',
    data
  });
}

// 更新用户状态
export function updateUserStatus(id, status) {
  return request({
    url: `/admin/user/status/${id}/${status}`,
    method: 'put'
  });
}

// 更新用户角色
export function updateUserRole(id, role) {
  return request({
    url: `/admin/user/role/${id}/${role}`,
    method: 'put'
  });
}

// 获取提示词列表（管理员）
export function getAdminPromptList(params) {
  return request({
    url: '/admin/prompt/list',
    method: 'get',
    params
  });
}

// 恢复已删除的提示词
export function restorePrompt(id) {
  return request({
    url: `/admin/prompt/restore/${id}`,
    method: 'put'
  });
}

// 获取举报列表
export function getReportList(params) {
  return request({
    url: '/admin/report/list',
    method: 'get',
    params
  });
}

// 处理举报
export function handleReport(id, data) {
  return request({
    url: `/admin/report/handle/${id}`,
    method: 'post',
    data
  });
}
