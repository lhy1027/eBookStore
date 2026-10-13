<template>
  <div class="container">
    <h2>购物车</h2>
    <el-table :data="cartStore.items" v-loading="cartStore.loading" style="width: 100%">
      <el-table-column label="图书" width="400">
        <template #default="{ row }">
          <div style="display:flex;align-items:center;gap:10px;">
            <img :src="row.coverUrl || '/placeholder.jpg'" style="width:60px;height:80px;object-fit:cover;" />
            <span>{{ row.bookName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="单价" width="120">
        <template #default="{ row }">¥{{ row.salePrice }}</template>
      </el-table-column>
      <el-table-column label="数量" width="150">
        <template #default="{ row }">
          <el-input-number v-model="row.quantity" :min="1" size="small" @change="handleQuantityChange(row)" />
        </template>
      </el-table-column>
      <el-table-column label="小计" width="120">
        <template #default="{ row }">¥{{ (row.salePrice * row.quantity).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="handleRemove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="cartStore.items.length === 0 && !cartStore.loading" style="text-align:center;padding:40px;color:#999;">购物车是空的</div>

    <div v-if="cartStore.items.length > 0" style="text-align:right;margin-top:20px;">
      <p style="font-size:18px;margin-bottom:10px;">总价：<span style="color:#f56c6c;font-weight:bold;">¥{{ totalPrice }}</span></p>
      <el-button type="primary" size="large" @click="$router.push('/checkout')">去结算</el-button>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useCartStore } from '@/store/cart';

const cartStore = useCartStore();

const totalPrice = computed(() =>
  cartStore.items.reduce((s, i) => s + i.salePrice * i.quantity, 0).toFixed(2)
);

onMounted(() => cartStore.fetchCart());

async function handleQuantityChange(row) {
  await cartStore.updateQuantity(row.id, row.quantity);
}

async function handleRemove(id) {
  await cartStore.remove(id);
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
</style>
</｜DSML｜tool_calls