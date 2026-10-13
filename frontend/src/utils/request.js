import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
});

request.interceptors.request.use(config => {
  const userStore = useUserStore();
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`;
  }
  return config;
}, error => Promise.reject(error));

request.interceptors.response.use(response => {
  // blob 响应（如导出）直接返回，不做 JSON 解析
  if (response.config.responseType === 'blob') {
    return response;
  }
  const res = response.data;
  if (res.code !== 200) {
    // 登录相关接口的错误由调用方自行处理，不全局弹窗
    if (!response.config.url.includes('/auth/login')) {
      ElMessage.error(res.msg || '请求失败');
    }
    return Promise.reject(res);
  }
  return res;
}, error => {
  ElMessage.error(error.message);
  return Promise.reject(error);
});

export default request;