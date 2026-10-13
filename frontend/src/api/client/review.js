import request from '@/utils/request';

export function addReview(data) {
  return request.post('/client/reviews', data);
}

export function getBookReviews(bookId) {
  return request.get(`/client/reviews/book/${bookId}`);
}