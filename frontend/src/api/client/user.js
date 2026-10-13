import request from '@/utils/request';

export function getProfile() {
  return request.get('/client/user/profile');
}

export function updateProfile(data) {
  return request.put('/client/user/profile', data);
}

export function getAddresses() {
  return request.get('/client/user/addresses');
}

export function addAddress(data) {
  return request.post('/client/user/addresses', data);
}

export function updateAddress(id, data) {
  return request.put(`/client/user/addresses/${id}`, data);
}

export function deleteAddress(id) {
  return request.delete(`/client/user/addresses/${id}`);
}

export function setDefaultAddress(id) {
  return request.put(`/client/user/addresses/${id}/default`);
}