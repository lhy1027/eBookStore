import request from '@/utils/request';

export function getCartList() {
  return request.get('/client/cart');
}

export function addToCart(bookId, quantity) {
  return request.post('/client/cart', { bookId, quantity });
}

export function updateCartQuantity(id, quantity) {
  return request.put(`/client/cart/${id}`, { quantity });
}

export function removeFromCart(id) {
  return request.delete(`/client/cart/${id}`);
}

export function toggleCartItem(id, selected) {
  return request.put(`/client/cart/${id}/select`, { selected });
}

export function clearCart() {
  return request.delete('/client/cart/clear');
}