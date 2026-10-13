<template>
  <div>
    <h2>会员管理</h2>
    <div style="margin-bottom: 16px; display: flex; gap: 10px;">
      <el-input v-model="keyword" placeholder="用户名/手机号" clearable style="width: 200px;" />
      <el-button type="primary" @click="fetchUsers">搜索</el-button>
      <el-button @click="handleExport">导出</el-button>
    </div>
    <el-table :data="users" border stripe>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-switch :model-value="row.status === 1" @change="toggleStatus(row)" /></template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180" />
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchUsers" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getUsers, toggleUserStatus, exportUsers } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const users = ref([]);
const keyword = ref('');
const page = ref(1);
const total = ref(0);

onMounted(() => fetchUsers());
async function fetchUsers() {
  const res = await getUsers({ page: page.value, size: 10, keyword: keyword.value || undefined });
  users.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1;
  await toggleUserStatus(row.id, newStatus);
  ElMessage.success('操作成功');
  fetchUsers();
}

async function handleExport() {
  try {
    const res = await exportUsers();
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const a = document.createElement('a');
    a.href = url;
    a.download = 'users.csv';
    a.click();
    window.URL.revokeObjectURL(url);
    ElMessage.success('导出成功');
  } catch (e) { ElMessage.error('导出失败'); }
}
</script>