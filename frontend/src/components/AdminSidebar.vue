<template>
  <el-container class="admin-layout">
    <el-aside width="200px">
      <div class="admin-logo" @click="$router.push('/admin')">后台管理</div>
      <el-menu :default-active="$route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409eff">
        <el-menu-item index="/admin/dashboard"><el-icon><DataAnalysis /></el-icon>首页</el-menu-item>
        <el-menu-item index="/admin/books"><el-icon><Reading /></el-icon>图书管理</el-menu-item>
        <el-menu-item index="/admin/categories"><el-icon><Grid /></el-icon>分类管理</el-menu-item>
        <el-menu-item index="/admin/orders"><el-icon><Document /></el-icon>订单管理</el-menu-item>
        <el-menu-item index="/admin/users"><el-icon><User /></el-icon>会员管理</el-menu-item>
        <el-menu-item index="/admin/reviews"><el-icon><Star /></el-icon>评价管理</el-menu-item>
        <el-menu-item index="/admin/messages"><el-icon><ChatDotRound /></el-icon>留言管理</el-menu-item>
        <el-menu-item index="/admin/news"><el-icon><Bell /></el-icon>公告管理</el-menu-item>
        <el-menu-item index="/admin/purchases"><el-icon><Box /></el-icon>进货管理</el-menu-item>
        <el-menu-item index="/admin/coupons"><el-icon><Discount /></el-icon>优惠券管理</el-menu-item>
        <el-menu-item index="/admin/statistics"><el-icon><TrendCharts /></el-icon>销售统计</el-menu-item>
        <el-menu-item index="/admin/logs"><el-icon><List /></el-icon>操作日志</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="admin-header">
        <span class="admin-title">eBookStore 后台管理系统</span>
        <el-button text @click="handleLogout">退出</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

function handleLogout() {
  userStore.logout();
  router.push('/login');
}
</script>

<style scoped>
.admin-layout { height: 100vh; }
.admin-logo { height: 60px; line-height: 60px; text-align: center; color: #fff; background: #263445; font-size: 18px; font-weight: bold; cursor: pointer; }
.admin-header { background: #fff; border-bottom: 1px solid #eee; display: flex; align-items: center; justify-content: space-between; }
.admin-title { font-size: 16px; }
</style>