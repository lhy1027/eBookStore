import request from '@/utils/request';

export function getOrders(params) {
  return request.get('/admin/orders', { params });
}

export function payOrder(id) {
  return request.put(`/admin/orders/${id}/pay`);
}

export function shipOrder(id) {
  return request.put(`/admin/orders/${id}/ship`);
}

export function updateOrderStatus(id, orderStatus) {
  return request.put(`/admin/orders/${id}/status`, { orderStatus });
}

export function cancelOrder(id) {
  return request.put(`/admin/orders/${id}/cancel`);
}

export function updateLogistics(id, logisticsStatus) {
  return request.put(`/admin/orders/${id}/logistics`, { logisticsStatus });
}

export function refundApprove(id) {
  return request.put(`/admin/orders/${id}/refund-approve`);
}

export function refundReject(id) {
  return request.put(`/admin/orders/${id}/refund-reject`);
}