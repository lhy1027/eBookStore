<template>
  <div>
    <header class="header">
      <div class="container">
        <router-link to="/" class="logo">eBookStore</router-link>
        <div class="search-box">
          <el-input v-model="keyword" placeholder="搜索图书..." @keyup.enter="search">
            <template #append><el-button @click="search"><el-icon><Search /></el-icon></el-button></template>
          </el-input>
        </div>
        <div class="nav-links">
          <router-link to="/cart"><el-icon><ShoppingCart /></el-icon>购物车</router-link>
          <router-link to="/orders" v-if="userStore.isLoggedIn()">我的订单</router-link>
          <router-link to="/messages" v-if="userStore.isLoggedIn()">留言</router-link>
          <template v-if="userStore.isLoggedIn()">
            <el-dropdown>
              <span class="user-name">{{ userStore.user?.username }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/favorites')">我的收藏</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/addresses')">收货地址</el-dropdown-item>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login">登录</router-link>
            <router-link to="/register">注册</router-link>
          </template>
        </div>
      </div>
    </header>
    <nav class="category-nav">
      <div class="container">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/books" class="nav-item">全部图书</router-link>
        <router-link v-for="cat in categories" :key="cat.id" :to="`/books?categoryId=${cat.id}`" class="nav-item">{{ cat.name }}</router-link>
      </div>
    </nav>
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 eBookStore 网上书店. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import { getCategories } from '@/api/client/category';

const router = useRouter();
const userStore = useUserStore();
const keyword = ref('');
const categories = ref([]);

onMounted(async () => {
  try {
    const res = await getCategories();
    categories.value = res.data || [];
  } catch (e) { /* ignore */ }
});

function search() {
  if (keyword.value.trim()) {
    router.push({ path: '/books', query: { keyword: keyword.value.trim() } });
  }
}

function handleLogout() {
  userStore.logout();
  router.push('/');
}
</script>

<style scoped>
.header { background: #409eff; color: #fff; padding: 12px 0; }
.header .container { max-width: 1200px; margin: 0 auto; padding: 0 20px; display: flex; align-items: center; gap: 20px; }
.logo { font-size: 24px; font-weight: bold; color: #fff; }
.search-box { flex: 1; max-width: 400px; }
.nav-links { display: flex; align-items: center; gap: 16px; }
.nav-links a { color: #fff; font-size: 14px; display: flex; align-items: center; gap: 4px; }
.user-name { color: #fff; cursor: pointer; }
.category-nav { background: #fff; border-bottom: 1px solid #eee; padding: 8px 0; }
.category-nav .container { display: flex; gap: 24px; max-width: 1200px; margin: 0 auto; padding: 0 20px; }
.nav-item { color: #333; font-size: 14px; padding: 4px 0; }
.nav-item:hover { color: #409eff; }
.main-content { min-height: calc(100vh - 180px); padding: 20px 0; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.15s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.footer { background: #f5f5f5; text-align: center; padding: 20px 0; color: #999; font-size: 13px; }
.footer .container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }
</style>