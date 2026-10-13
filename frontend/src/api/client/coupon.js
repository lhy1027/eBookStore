import request from '@/utils/request';

export function getAvailableCoupons() {
  return request.get('/client/coupons/available');
}