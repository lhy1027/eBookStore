<template>
  <div class="container">
    <h2>{{ news.title }}</h2>
    <p class="news-time">{{ news.createTime }}</p>
    <div class="news-content" v-html="news.content"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getNewsDetail } from '@/api/client/news';

const route = useRoute();
const news = ref({});

onMounted(async () => {
  const res = await getNewsDetail(route.params.id);
  news.value = res.data || {};
});
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 10px; }
.news-time { color: #999; font-size: 13px; margin-bottom: 20px; }
.news-content { line-height: 1.8; }
</style>