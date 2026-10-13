import request from '@/utils/request';

export function login(data) {
  return request.post('/client/auth/login', data);
}

export function register(data) {
  return request.post('/client/auth/register', data);
}

export function logout() {
  return request.post('/client/auth/logout');
}