<template>
  <div class="container">
    <h2>图书分类</h2>
    <div v-for="cat in categoryTree" :key="cat.name" class="category-group">
      <h3>{{ cat.name }}</h3>
      <div class="sub-cats">
        <el-tag v-for="sub in cat.children" :key="sub.id" style="margin: 4px; cursor: pointer;" @click="$router.push(`/books?categoryId=${sub.id}`)">{{ sub.name }}</el-tag>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getCategoryTree } from '@/api/client/category';

const categoryTree = ref([]);

onMounted(async () => {
  const res = await getCategoryTree();
  const data = res.data || {};
  categoryTree.value = Object.keys(data).map(key => ({ name: key, children: data[key] }));
});
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
.category-group { margin-bottom: 20px; }
.category-group h3 { margin-bottom: 8px; font-size: 18px; padding-left: 10px; border-left: 3px solid #409eff; }
</style>