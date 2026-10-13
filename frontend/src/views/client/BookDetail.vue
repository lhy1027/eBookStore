<template>
  <div class="container">
    <div class="book-detail" v-if="book">
      <div class="detail-left">
        <img :src="book.coverUrl || '/placeholder.jpg'" :alt="book.name" />
      </div>
      <div class="detail-right">
        <h1>{{ book.name }}</h1>
        <p class="meta">作者：{{ book.author }} | 出版社：{{ book.publisher }} | ISBN：{{ book.isbn }}</p>
        <p class="meta">出版日期：{{ book.publishDate }} | 库存：{{ book.stock }} | 销量：{{ book.salesCount }}</p>
        <div class="price-box">
          <span class="sale-price">¥{{ book.salePrice }}</span>
          <span class="orig-price">¥{{ book.price }}</span>
        </div>
        <div class="actions">
          <el-input-number v-model="quantity" :min="1" :max="book.stock" style="margin-right: 10px;" />
          <el-button type="danger" @click="addToCart">加入购物车</el-button>
          <el-button :type="favorited ? 'warning' : 'default'" @click="toggleFavorite">
            {{ favorited ? '取消收藏' : '收藏' }}
          </el-button>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h3>图书详情</h3>
      <div v-html="book?.detail" class="detail-content"></div>
    </div>

    <div class="detail-section">
      <h3>用户评价</h3>
      <div v-if="reviews.length === 0" style="color:#999;">暂无评价</div>
      <div v-for="r in reviews" :key="r.id" class="review-item">
        <div class="review-header">
          <span class="review-user">{{ r.username }}</span>
          <el-rate :model-value="r.rating" disabled show-score />
        </div>
        <p>{{ r.content }}</p>
        <p class="review-time">{{ r.createTime }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getBookDetail, getBookReviews } from '@/api/client/book';
import { addToCart as addToCartApi } from '@/api/client/cart';
import { addFavorite, removeFavorite, checkFavorite } from '@/api/client/favorite';
import { useUserStore } from '@/store/user';
import { ElMessage } from 'element-plus';

const route = useRoute();
const userStore = useUserStore();
const book = ref(null);
const reviews = ref([]);
const quantity = ref(1);
const favorited = ref(false);

onMounted(async () => {
  const [detailRes, reviewRes] = await Promise.all([
    getBookDetail(route.params.id),
    getBookReviews(route.params.id)
  ]);
  book.value = detailRes.data;
  reviews.value = reviewRes.data || [];
  if (userStore.isLoggedIn()) {
    try {
      const res = await checkFavorite(route.params.id);
      favorited.value = res.data;
    } catch (e) { /* ignore */ }
  }
});

async function addToCart() {
  try {
    await addToCartApi(book.value.id, quantity.value);
    ElMessage.success('已加入购物车');
  } catch (e) {
    ElMessage.error(e.msg || '加入失败');
  }
}

async function toggleFavorite() {
  if (!userStore.isLoggedIn()) {
    ElMessage.warning('请先登录');
    return;
  }
  try {
    if (favorited.value) {
      await removeFavorite(book.value.id);
      favorited.value = false;
      ElMessage.success('已取消收藏');
    } else {
      await addFavorite(book.value.id);
      favorited.value = true;
      ElMessage.success('已收藏');
    }
  } catch (e) {
    ElMessage.error(e.msg || '操作失败');
  }
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
.book-detail { display: flex; gap: 30px; margin-bottom: 30px; }
.detail-left img { width: 300px; height: 400px; object-fit: cover; border-radius: 8px; }
.detail-right { flex: 1; }
.detail-right h1 { font-size: 22px; margin-bottom: 10px; }
.meta { color: #666; font-size: 13px; margin-bottom: 6px; }
.price-box { margin: 20px 0; }
.sale-price { color: #f56c6c; font-size: 28px; font-weight: bold; margin-right: 10px; }
.orig-price { color: #999; font-size: 16px; text-decoration: line-through; }
.actions { margin-top: 20px; }
.detail-section { margin-bottom: 30px; }
.detail-section h3 { font-size: 18px; margin-bottom: 12px; padding-left: 10px; border-left: 3px solid #409eff; }
.detail-content { line-height: 1.8; color: #333; }
.review-item { border-bottom: 1px solid #eee; padding: 12px 0; }
.review-header { display: flex; align-items: center; gap: 10px; margin-bottom: 6px; }
.review-user { font-weight: bold; }
.review-time { color: #999; font-size: 12px; margin-top: 4px; }
</style>