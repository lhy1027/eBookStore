<template>
  <div class="page">
    <div class="banner">
      <h1>欢迎来到 eBookStore</h1>
      <p>海量图书，精选推荐</p>
    </div>
    <div class="container" v-loading="loading" element-loading-text="加载中...">
      <div v-if="!loading" class="sections-wrapper">
      <section class="section">
        <h2 class="section-title">推荐图书</h2>
        <div class="book-grid">
          <div v-for="book in recommendedBooks" :key="book.id" class="book-card" @click="$router.push(`/books/${book.id}`)">
            <img :src="book.coverUrl || '/placeholder.jpg'" :alt="book.name" />
            <div class="book-info">
              <h3>{{ book.name }}</h3>
              <p class="author">{{ book.author }}</p>
              <p class="price"><span class="sale-price">¥{{ book.salePrice }}</span><span class="orig-price">¥{{ book.price }}</span></p>
            </div>
          </div>
        </div>
      </section>

      <section class="section">
        <h2 class="section-title">热门图书</h2>
        <div class="book-grid">
          <div v-for="book in hotBooks" :key="book.id" class="book-card" @click="$router.push(`/books/${book.id}`)">
            <img :src="book.coverUrl || '/placeholder.jpg'" :alt="book.name" />
            <div class="book-info">
              <h3>{{ book.name }}</h3>
              <p class="author">{{ book.author }}</p>
              <p class="price"><span class="sale-price">¥{{ book.salePrice }}</span></p>
            </div>
          </div>
        </div>
      </section>

      <section class="section">
        <h2 class="section-title">网站公告</h2>
        <el-card v-for="n in newsList" :key="n.id" class="news-item" @click="$router.push(`/news/${n.id}`)">
          <h4>{{ n.title }}</h4>
          <p class="news-time">{{ n.createTime }}</p>
        </el-card>
      </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getBookList, getRecommendedBooks } from '@/api/client/book';
import { getNewsList } from '@/api/client/news';

const recommendedBooks = ref([]);
const hotBooks = ref([]);
const newsList = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const [recRes, hotRes, newsRes] = await Promise.all([
      getRecommendedBooks({ size: 8 }),
      getBookList({ sortBy: 'sales', size: 8 }),
      getNewsList()
    ]);
    recommendedBooks.value = (recRes.data?.records || []).slice(0, 8);
    hotBooks.value = (hotRes.data?.records || []).slice(0, 8);
    newsList.value = newsRes.data || [];
  } catch (e) { /* ignore */ }
  loading.value = false;
});
</script>

<style scoped>
.page { width: 100%; }
.banner { width: 100%; background: linear-gradient(135deg, #409eff, #67c23a); color: #fff; text-align: center; padding: 50px 20px; }
.banner h1 { font-size: 36px; margin-bottom: 10px; font-weight: bold; }
.banner p { font-size: 18px; opacity: 0.9; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; min-height: 400px; padding-top: 30px; }
.sections-wrapper { display: flex; flex-direction: column; gap: 40px; margin-bottom: 40px; }
.section-title { font-size: 20px; margin-bottom: 16px; padding-left: 10px; border-left: 3px solid #409eff; }
.book-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.book-card { cursor: pointer; border: 1px solid #eee; border-radius: 8px; overflow: hidden; transition: box-shadow 0.3s; }
.book-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.book-card img { width: 100%; height: 200px; object-fit: cover; }
.book-info { padding: 10px; }
.book-info h3 { font-size: 14px; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.author { color: #999; font-size: 12px; margin-bottom: 6px; }
.price { display: flex; align-items: center; gap: 8px; }
.sale-price { color: #f56c6c; font-size: 16px; font-weight: bold; }
.orig-price { color: #999; font-size: 12px; text-decoration: line-through; }
.news-item { cursor: pointer; margin-bottom: 10px; }
.news-time { color: #999; font-size: 12px; }
</style>