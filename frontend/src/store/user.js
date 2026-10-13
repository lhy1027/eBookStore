import { defineStore } from 'pinia';
import { ref } from 'vue';
import { login as loginApi, register as registerApi } from '@/api/client/auth';
import { adminLogin as adminLoginApi } from '@/api/admin/adminBook';

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '');
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'));
  const role = ref(localStorage.getItem('role') || '');

  const isLoggedIn = () => !!token.value;
  const isAdmin = () => role.value === 'ADMIN';

  async function login(req) {
    // 先尝试管理员登录（避免客户端登录失败先弹出错误消息）
    try {
      const res = await adminLoginApi(req);
      token.value = res.data.token;
      user.value = res.data;
      role.value = res.data.role;
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('user', JSON.stringify(res.data));
      localStorage.setItem('role', res.data.role);
      return res;
    } catch (e) {
      // 管理员登录失败，尝试用户登录
      try {
        const res = await loginApi(req);
        token.value = res.data.token;
        user.value = res.data;
        role.value = res.data.role;
        localStorage.setItem('token', res.data.token);
        localStorage.setItem('user', JSON.stringify(res.data));
        localStorage.setItem('role', res.data.role);
        return res;
      } catch (e2) {
        throw e2;
      }
    }
  }

  async function register(req) {
    const res = await registerApi(req);
    token.value = res.data.token;
    user.value = res.data;
    role.value = res.data.role;
    localStorage.setItem('token', res.data.token);
    localStorage.setItem('user', JSON.stringify(res.data));
    localStorage.setItem('role', res.data.role);
    return res;
  }

  function logout() {
    token.value = '';
    user.value = null;
    role.value = '';
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
  }

  return { token, user, role, isLoggedIn, isAdmin, login, register, logout };
});