<template>
  <div class="container">
    <h2>我的收藏</h2>
    <div v-if="favorites.length === 0" style="text-align:center;padding:40px;color:#999;">暂无收藏</div>
    <div v-else class="book-grid">
      <div v-for="item in favorites" :key="item.id" class="book-card">
        <el-image :src="item.coverUrl || '/default-cover.png'" style="width:160px;height:200px;cursor:pointer;" @click="$router.push(`/books/${item.bookId}`)" />
        <div class="book-info">
          <p class="book-name" @click="$router.push(`/books/${item.bookId}`)">{{ item.bookName }}</p>
          <p class="book-author">{{ item.author }}</p>
          <p class="book-price">¥{{ item.salePrice || item.price }}</p>
          <div class="book-actions">
            <el-button size="small" type="primary" @click="addToCart(item.bookId)" :disabled="item.status !== 1">加入购物车</el-button>
            <el-button size="small" type="danger" @click="handleRemove(item.bookId)">取消收藏</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getFavorites, removeFavorite } from '@/api/client/favorite';
import { addToCart as apiAddToCart } from '@/api/client/cart';
import { ElMessage } from 'element-plus';

const favorites = ref([]);

onMounted(() => fetchFavorites());
async function fetchFavorites() {
  const res = await getFavorites();
  favorites.value = res.data || [];
}

async function handleRemove(bookId) {
  try {
    await removeFavorite(bookId);
    ElMessage.success('已取消收藏');
    fetchFavorites();
  } catch (e) {
    ElMessage.error(e.msg || '操作失败');
  }
}

async function addToCart(bookId) {
  try {
    await apiAddToCart({ bookId, quantity: 1 });
    ElMessage.success('已加入购物车');
  } catch (e) {
    ElMessage.error(e.msg || '操作失败');
  }
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
.book-grid { display: flex; flex-wrap: wrap; gap: 20px; }
.book-card { width: 200px; border: 1px solid #eee; border-radius: 8px; padding: 10px; text-align: center; }
.book-name { font-weight: bold; cursor: pointer; margin: 8px 0 4px; }
.book-name:hover { color: #409eff; }
.book-author { color: #999; font-size: 13px; margin-bottom: 4px; }
.book-price { color: #e4393c; font-size: 16px; font-weight: bold; margin-bottom: 8px; }
.book-actions { display: flex; gap: 4px; justify-content: center; }
</style>