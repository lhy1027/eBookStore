import request from '@/utils/request';

export function submitOrder(data) {
  return request.post('/client/orders', data);
}

export function getOrderList(params) {
  return request.get('/client/orders', { params });
}

export function getOrderDetail(id) {
  return request.get(`/client/orders/${id}`);
}

export function payOrder(id) {
  return request.post(`/client/orders/${id}/pay`);
}

export function cancelOrder(id) {
  return request.post(`/client/orders/${id}/cancel`);
}

export function confirmOrder(id) {
  return request.post(`/client/orders/${id}/confirm`);
}

export function getOrderItems(id) {
  return request.get(`/client/orders/${id}/items`);
}

export function refundOrder(id) {
  return request.post(`/client/orders/${id}/refund`);
}