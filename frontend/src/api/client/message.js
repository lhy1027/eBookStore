import request from '@/utils/request';

export function getMessages() {
  return request.get('/client/messages');
}

export function addMessage(content) {
  return request.post('/client/messages', { content });
}