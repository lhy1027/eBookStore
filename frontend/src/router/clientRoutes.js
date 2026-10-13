export default [
  {
    path: '/',
    component: () => import('@/components/ClientHeader.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('@/views/client/Home.vue') },
      { path: 'books', name: 'BookList', component: () => import('@/views/client/BookList.vue') },
      { path: 'books/:id', name: 'BookDetail', component: () => import('@/views/client/BookDetail.vue'), props: true },
      { path: 'categories', name: 'CategoryList', component: () => import('@/views/client/CategoryList.vue') },
      { path: 'cart', name: 'Cart', component: () => import('@/views/client/Cart.vue'), meta: { requireAuth: true } },
      { path: 'checkout', name: 'Checkout', component: () => import('@/views/client/Checkout.vue'), meta: { requireAuth: true } },
      { path: 'orders', name: 'OrderList', component: () => import('@/views/client/OrderList.vue'), meta: { requireAuth: true } },
      { path: 'orders/:id', name: 'OrderDetail', component: () => import('@/views/client/OrderDetail.vue'), meta: { requireAuth: true }, props: true },
      { path: 'addresses', name: 'AddressManage', component: () => import('@/views/client/AddressManage.vue'), meta: { requireAuth: true } },
      { path: 'profile', name: 'UserProfile', component: () => import('@/views/client/UserProfile.vue'), meta: { requireAuth: true } },
      { path: 'messages', name: 'MessageList', component: () => import('@/views/client/MessageList.vue'), meta: { requireAuth: true } },
      { path: 'favorites', name: 'FavoriteList', component: () => import('@/views/client/FavoriteList.vue'), meta: { requireAuth: true } },
      { path: 'news/:id', name: 'NewsDetail', component: () => import('@/views/client/NewsDetail.vue'), props: true },
      { path: 'login', name: 'Login', component: () => import('@/views/client/Login.vue') },
      { path: 'register', name: 'Register', component: () => import('@/views/client/Register.vue') }
    ]
  }
];