import request from '@/utils/request';

export function getNewsList() {
  return request.get('/client/news');
}

export function getNewsDetail(id) {
  return request.get(`/client/news/${id}`);
}