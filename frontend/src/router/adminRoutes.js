export default [
  {
    path: '/admin',
    component: () => import('@/components/AdminSidebar.vue'),
    meta: { requireAdmin: true },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/Dashboard.vue') },
      { path: 'books', name: 'AdminBookManage', component: () => import('@/views/admin/BookManage.vue') },
      { path: 'categories', name: 'AdminCategoryManage', component: () => import('@/views/admin/CategoryManage.vue') },
      { path: 'orders', name: 'AdminOrderManage', component: () => import('@/views/admin/OrderManage.vue') },
      { path: 'users', name: 'AdminUserManage', component: () => import('@/views/admin/UserManage.vue') },
      { path: 'reviews', name: 'AdminReviewManage', component: () => import('@/views/admin/ReviewManage.vue') },
      { path: 'messages', name: 'AdminMessageManage', component: () => import('@/views/admin/MessageManage.vue') },
      { path: 'news', name: 'AdminNewsManage', component: () => import('@/views/admin/NewsManage.vue') },
      { path: 'purchases', name: 'AdminPurchaseManage', component: () => import('@/views/admin/PurchaseManage.vue') },
      { path: 'coupons', name: 'AdminCouponManage', component: () => import('@/views/admin/CouponManage.vue') },
      { path: 'statistics', name: 'AdminStatistics', component: () => import('@/views/admin/Statistics.vue') },
      { path: 'logs', name: 'AdminLogManage', component: () => import('@/views/admin/LogManage.vue') }
    ]
  }
];