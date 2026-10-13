<template>
  <div class="container" v-loading="loading" element-loading-text="加载中...">
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索书名/作者/出版社/ISBN" clearable style="width: 300px; margin-right: 10px;" />
      <el-input v-model="minPrice" type="number" placeholder="最低价" style="width: 120px; margin-right: 10px;" />
      <el-input v-model="maxPrice" type="number" placeholder="最高价" style="width: 120px; margin-right: 10px;" />
      <el-select v-model="sortBy" placeholder="排序" style="width: 130px; margin-right: 10px;" clearable>
        <el-option label="默认" value="" />
        <el-option label="销量" value="sales" />
        <el-option label="价格升序" value="price_asc" />
        <el-option label="价格降序" value="price_desc" />
        <el-option label="最新" value="newest" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>

    <div v-if="!loading" class="book-grid">
      <div v-for="book in books" :key="book.id" class="book-card" @click="$router.push(`/books/${book.id}`)">
        <img :src="book.coverUrl || '/placeholder.jpg'" :alt="book.name" />
        <div class="book-info">
          <h3>{{ book.name }}</h3>
          <p class="author">{{ book.author }}</p>
          <p class="price"><span class="sale-price">¥{{ book.salePrice }}</span><span class="orig-price">¥{{ book.price }}</span></p>
          <p class="sales">已售 {{ book.salesCount || 0 }}</p>
        </div>
      </div>
    </div>
    <div v-if="!loading && books.length === 0" style="text-align:center;padding:40px;color:#999;">暂无数据</div>

    <Pagination v-if="!loading" v-model="page" :total="total" :page-size="size" @change="fetchBooks" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getBookList } from '@/api/client/book';
import Pagination from '@/components/Pagination.vue';

const route = useRoute();
const keyword = ref('');
const minPrice = ref(null);
const maxPrice = ref(null);
const sortBy = ref('');
const books = ref([]);
const page = ref(1);
const size = ref(15);
const total = ref(0);
const loading = ref(false);

onMounted(() => {
  keyword.value = route.query.keyword || '';
  fetchBooks();
});

// 监听路由参数变化（点击分类链接时重新加载）
watch(() => route.query, () => {
  keyword.value = route.query.keyword || '';
  page.value = 1;
  fetchBooks();
});

async function fetchBooks() {
  loading.value = true;
  try {
    const res = await getBookList({
      page: page.value, size: size.value,
      keyword: keyword.value || undefined,
      categoryId: route.query.categoryId || undefined,
      minPrice: minPrice.value || undefined,
      maxPrice: maxPrice.value || undefined,
      sortBy: sortBy.value || undefined
    });
    books.value = res.data?.records || [];
    total.value = res.data?.total || 0;
  } catch (e) {
    books.value = [];
  } finally {
    loading.value = false;
  }
}

function search() { page.value = 1; fetchBooks(); }
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; min-height: 400px; }
.search-bar { display: flex; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 8px; }
.book-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; min-height: 300px; }
.book-card { cursor: pointer; border: 1px solid #eee; border-radius: 8px; overflow: hidden; transition: box-shadow 0.3s; }
.book-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.book-card img { width: 100%; height: 200px; object-fit: cover; }
.book-info { padding: 10px; }
.book-info h3 { font-size: 14px; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.author { color: #999; font-size: 12px; margin-bottom: 6px; }
.price { display: flex; align-items: center; gap: 8px; }
.sale-price { color: #f56c6c; font-size: 16px; font-weight: bold; }
.orig-price { color: #999; font-size: 12px; text-decoration: line-through; }
.sales { color: #999; font-size: 12px; }
</style>