<template>
  <div>
    <h2>进货管理</h2>
    <el-button type="primary" @click="showAddDialog" style="margin-bottom: 16px;">新增进货</el-button>
    <el-table :data="purchases" border stripe>
      <el-table-column prop="bookName" label="图书" />
      <el-table-column prop="quantity" label="数量" width="100" />
      <el-table-column label="单价" width="100"><template #default="{ row }">¥{{ row.price }}</template></el-table-column>
      <el-table-column prop="supplier" label="供应商" />
      <el-table-column prop="operator" label="经办人" width="100" />
      <el-table-column prop="createTime" label="时间" width="180" />
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchPurchases" />

    <el-dialog v-model="dialogVisible" title="新增进货" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="图书">
          <el-select v-model="form.bookId" filterable placeholder="请选择图书" style="width: 100%;">
            <el-option v-for="b in books" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量"><el-input-number v-model="form.quantity" :min="1" /></el-form-item>
        <el-form-item label="单价"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="供应商"><el-input v-model="form.supplier" /></el-form-item>
        <el-form-item label="经办人"><el-input v-model="form.operator" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getPurchases, addPurchase, getBooks } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';
import { useUserStore } from '@/store/user';

const userStore = useUserStore();
const purchases = ref([]);
const books = ref([]);
const page = ref(1);
const total = ref(0);
const dialogVisible = ref(false);
const form = ref({ bookId: null, quantity: 1, price: 0, supplier: '', operator: '' });

onMounted(() => { fetchPurchases(); fetchBooks(); });

async function fetchPurchases() {
  const res = await getPurchases({ page: page.value, size: 10 });
  purchases.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

async function fetchBooks() {
  const res = await getBooks({ page: 1, size: 1000 });
  books.value = res.data?.records || [];
}

function showAddDialog() {
  form.value = { bookId: null, quantity: 1, price: 0, supplier: '', operator: userStore.user?.username || '' };
  dialogVisible.value = true;
}

async function handleSave() {
  await addPurchase(form.value);
  ElMessage.success('进货成功');
  dialogVisible.value = false;
  fetchPurchases();
}
</script>