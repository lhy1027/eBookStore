import { createRouter, createWebHistory } from 'vue-router';
import clientRoutes from './clientRoutes';
import adminRoutes from './adminRoutes';

const routes = [
  ...clientRoutes,
  ...adminRoutes
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (to.meta.requireAuth && !token) {
    next('/login');
  } else if (to.meta.requireAdmin && role !== 'ADMIN') {
    next('/login');
  } else {
    next();
  }
});

export default router;