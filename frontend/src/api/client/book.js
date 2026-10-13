import request from '@/utils/request';

export function getBookList(params) {
  return request.get('/client/books', { params });
}

export function getBookDetail(id) {
  return request.get(`/client/books/${id}`);
}

export function getBookReviews(bookId) {
  return request.get(`/client/books/${bookId}/reviews`);
}

export function getRecommendedBooks(params) {
  return request.get('/client/books/recommended', { params });
}