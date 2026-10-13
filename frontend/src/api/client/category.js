import request from '@/utils/request';

export function getCategoryTree() {
  return request.get('/client/categories/tree');
}

export function getCategories() {
  return request.get('/client/categories');
}

export function getSubCategories(parentId) {
  return request.get(`/client/categories/${parentId}/children`);
}