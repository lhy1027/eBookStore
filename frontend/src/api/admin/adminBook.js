import request from '@/utils/request';

export function adminLogin(data) {
  return request.post('/admin/auth/login', data);
}

export function getBooks(params) {
  return request.get('/admin/books', { params });
}
export function getBook(id) { return request.get(`/admin/books/${id}`); }
export function addBook(data) { return request.post('/admin/books', data); }
export function updateBook(id, data) { return request.put(`/admin/books/${id}`, data); }
export function toggleBookStatus(id, status) { return request.put(`/admin/books/${id}/status`, { status }); }
export function toggleBookRecommend(id, recommendStatus) { return request.put(`/admin/books/${id}/recommend`, { recommendStatus }); }
export function updateBookStock(id, quantity) { return request.put(`/admin/books/${id}/stock`, { quantity }); }
export function getStockWarning() { return request.get('/admin/books/warning'); }

export function uploadCover(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/upload/cover', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
}

export function getCategories() { return request.get('/admin/categories'); }
export function addCategory(data) { return request.post('/admin/categories', data); }
export function updateCategory(id, data) { return request.put(`/admin/categories/${id}`, data); }
export function deleteCategory(id) { return request.delete(`/admin/categories/${id}`); }

export function getOrders(params) { return request.get('/admin/orders', { params }); }
export function shipOrder(id) { return request.put(`/admin/orders/${id}/ship`); }
export function cancelAdminOrder(id) { return request.put(`/admin/orders/${id}/cancel`); }

export function getUsers(params) { return request.get('/admin/users', { params }); }
export function toggleUserStatus(id, status) { return request.put(`/admin/users/${id}/status`, { status }); }

export function getReviews(params) { return request.get('/admin/reviews', { params }); }
export function deleteReview(id) { return request.delete(`/admin/reviews/${id}`); }

export function getMessages(params) { return request.get('/admin/messages', { params }); }
export function replyMessage(id, reply) { return request.put(`/admin/messages/${id}/reply`, { reply }); }
export function deleteMessage(id) { return request.delete(`/admin/messages/${id}`); }

export function getNews(params) { return request.get('/admin/news', { params }); }
export function addNews(data) { return request.post('/admin/news', data); }
export function updateNews(id, data) { return request.put(`/admin/news/${id}`, data); }
export function deleteNews(id) { return request.delete(`/admin/news/${id}`); }

export function getPurchases(params) { return request.get('/admin/purchases', { params }); }
export function addPurchase(data) { return request.post('/admin/purchases', data); }

export function getCoupons(params) { return request.get('/admin/coupons', { params }); }
export function addCoupon(data) { return request.post('/admin/coupons', data); }
export function updateCoupon(id, data) { return request.put(`/admin/coupons/${id}`, data); }
export function deleteCoupon(id) { return request.delete(`/admin/coupons/${id}`); }

export function getSalesStatistics(params) { return request.get('/admin/statistics/sales', { params }); }
export function getHotBooks() { return request.get('/admin/statistics/hot-books'); }
export function getCategorySales() { return request.get('/admin/statistics/category-sales'); }

export function getLogs(params) { return request.get('/admin/logs', { params }); }

export function getDashboardStats() { return request.get('/admin/statistics/dashboard'); }

export function getConfig(key) { return request.get(`/admin/config/${key}`); }
export function setConfig(key, value) { return request.put(`/admin/config/${key}`, { value }); }

export function exportBooks(keyword) { return request.get('/admin/export/books', { params: { keyword }, responseType: 'blob' }); }
export function exportOrders() { return request.get('/admin/export/orders', { responseType: 'blob' }); }
export function exportUsers() { return request.get('/admin/export/users', { responseType: 'blob' }); }