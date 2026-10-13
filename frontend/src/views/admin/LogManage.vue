<template>
  <div>
    <h2>操作日志</h2>
    <el-table :data="logs" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="operator" label="操作人" width="120" />
      <el-table-column prop="operation" label="操作类型" width="120">
        <template #default="{ row }">
          <el-tag :type="typeTag(row.operation)">{{ row.operation }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="target" label="操作对象" min-width="200" />
      <el-table-column prop="result" label="结果" width="100">
        <template #default="{ row }">
          <el-tag :type="row.result === '成功' ? 'success' : 'danger'" size="small">{{ row.result }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="create_time" label="操作时间" width="180">
        <template #default="{ row }">{{ row.create_time || row.createTime }}</template>
      </el-table-column>
    </el-table>
    <div v-if="logs.length === 0 && !loading" style="text-align:center;padding:40px;color:#999;">暂无日志</div>
    <Pagination v-model="page" :total="total" @change="fetchLogs" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getLogs } from '@/api/admin/adminBook';
import Pagination from '@/components/Pagination.vue';

const logs = ref([]);
const page = ref(1);
const total = ref(0);
const loading = ref(false);

onMounted(() => fetchLogs());

async function fetchLogs() {
  loading.value = true;
  try {
    const res = await getLogs({ page: page.value, size: 10 });
    logs.value = res.data?.records || [];
    total.value = res.data?.total || 0;
  } finally {
    loading.value = false;
  }
}

function typeTag(type) {
  const map = {
    '登录': 'info',
    '新增': 'success',
    '修改': 'warning',
    '删除': 'danger',
    '调整': 'primary'
  };
  return map[type] || 'info';
}
</script>

<style scoped>
h2 { margin-bottom: 20px; }
</style>