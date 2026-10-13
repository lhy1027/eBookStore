import request from '@/utils/request';

export function getFavorites() {
  return request.get('/client/favorites');
}

export function addFavorite(bookId) {
  return request.post('/client/favorites', { bookId });
}

export function removeFavorite(bookId) {
  return request.delete(`/client/favorites/${bookId}`);
}

export function checkFavorite(bookId) {
  return request.get(`/client/favorites/check/${bookId}`);
}