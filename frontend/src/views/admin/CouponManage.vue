<template>
  <div>
    <h2>优惠券管理</h2>
    <el-button type="primary" @click="showAddDialog" style="margin-bottom: 16px;">创建优惠券</el-button>
    <el-table :data="coupons" border stripe>
      <el-table-column prop="name" label="名称" />
      <el-table-column label="满减" width="180">
        <template #default="{ row }">满¥{{ row.threshold }}减¥{{ row.discount }}</template>
      </el-table-column>
      <el-table-column prop="expireStart" label="开始时间" width="180" />
      <el-table-column prop="expireEnd" label="结束时间" width="180" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">{{ row.status === 1 ? '有效' : '无效' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="editCoupon(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchCoupons" />

    <el-dialog v-model="dialogVisible" :title="editing ? '编辑优惠券' : '创建优惠券'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="满减门槛"><el-input-number v-model="form.threshold" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="优惠金额"><el-input-number v-model="form.discount" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.expireStart" type="datetime" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.expireEnd" type="datetime" /></el-form-item>
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
import { getCoupons, addCoupon, updateCoupon, deleteCoupon } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const coupons = ref([]);
const page = ref(1);
const total = ref(0);
const dialogVisible = ref(false);
const editing = ref(false);
const form = ref({});

onMounted(() => fetchCoupons());
async function fetchCoupons() {
  const res = await getCoupons({ page: page.value, size: 10 });
  coupons.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

function showAddDialog() { editing.value = false; form.value = {}; dialogVisible.value = true; }
function editCoupon(row) { editing.value = true; form.value = { ...row }; dialogVisible.value = true; }

async function handleSave() {
  if (editing.value) { await updateCoupon(form.value.id, form.value); } else { await addCoupon(form.value); }
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  fetchCoupons();
}

async function handleDelete(id) {
  await deleteCoupon(id);
  ElMessage.success('删除成功');
  fetchCoupons();
}
</script>