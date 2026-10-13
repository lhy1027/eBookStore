<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h4>eBookStore 网上书店</h4>
        <p>海量图书，精选推荐，正版保障</p>
      </div>
      <div class="footer-section">
        <h4>快速链接</h4>
        <router-link to="/books">全部图书</router-link>
        <router-link to="/categories">图书分类</router-link>
        <router-link to="/cart">购物车</router-link>
      </div>
      <div class="footer-section">
        <h4>客户服务</h4>
        <router-link to="/orders">我的订单</router-link>
        <router-link to="/messages">在线留言</router-link>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; {{ new Date().getFullYear() }} eBookStore 网上书店. All rights reserved.</p>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer { background: #2c3e50; color: #bfc9d4; margin-top: 40px; }
.footer-content { max-width: 1200px; margin: 0 auto; padding: 40px 20px; display: flex; gap: 60px; }
.footer-section { flex: 1; }
.footer-section h4 { color: #fff; font-size: 16px; margin-bottom: 16px; }
.footer-section p { font-size: 14px; line-height: 1.8; }
.footer-section a { display: block; color: #bfc9d4; font-size: 14px; line-height: 2; text-decoration: none; }
.footer-section a:hover { color: #409eff; }
.footer-bottom { border-top: 1px solid #3a4a5a; text-align: center; padding: 16px 20px; font-size: 13px; }
</style>