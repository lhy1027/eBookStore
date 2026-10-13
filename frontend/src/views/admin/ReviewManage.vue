<template>
  <div>
    <h2>评价管理</h2>
    <el-table :data="reviews" border stripe>
      <el-table-column prop="username" label="用户" width="120" />
      <el-table-column prop="bookName" label="图书" width="180" />
      <el-table-column prop="content" label="评价内容" />
      <el-table-column label="评分" width="100">
        <template #default="{ row }"><el-rate :model-value="row.rating" disabled /></template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }"><el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchReviews" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getReviews, deleteReview } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const reviews = ref([]);
const page = ref(1);
const total = ref(0);

onMounted(() => fetchReviews());
async function fetchReviews() {
  const res = await getReviews({ page: page.value, size: 10 });
  reviews.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

async function handleDelete(id) {
  await deleteReview(id);
  ElMessage.success('删除成功');
  fetchReviews();
}
</script>