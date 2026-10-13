<template>
  <div class="container">
    <h2>确认订单</h2>
    <h3>收货地址</h3>
    <el-select v-model="addressId" placeholder="请选择收货地址" style="width: 100%;">
      <el-option v-for="addr in addresses" :key="addr.id" :label="`${addr.receiverName} ${addr.phone} ${addr.region || ''} ${addr.detailAddress || ''}`" :value="addr.id" />
    </el-select>

    <h3 style="margin-top: 20px;">优惠券</h3>
    <el-select v-model="couponId" placeholder="请选择优惠券（可选）" clearable style="width: 100%;">
      <el-option v-for="c in coupons" :key="c.id" :label="`${c.name}（满${c.threshold}减${c.discount}）`" :value="c.id" />
    </el-select>
    <p v-if="couponId && discountAmount === 0 && parseFloat(totalAmount) > 0" style="color:#f56c6c;font-size:13px;margin-top:6px;">
      未达到该优惠券使用门槛（满{{ coupons.find(c => c.id === couponId)?.threshold }}元）
    </p>

    <h3 style="margin-top: 20px;">订单商品</h3>
    <el-table :data="cartStore.items" style="width: 100%">
      <el-table-column label="图书" prop="bookName" />
      <el-table-column label="单价" width="120">
        <template #default="{ row }">¥{{ row.salePrice }}</template>
      </el-table-column>
      <el-table-column label="数量" prop="quantity" width="100" />
      <el-table-column label="小计" width="120">
        <template #default="{ row }">¥{{ (row.salePrice * row.quantity).toFixed(2) }}</template>
      </el-table-column>
    </el-table>

    <div style="text-align:right;margin-top:20px;">
      <p>商品总价：¥{{ totalAmount }}</p>
      <p>优惠：-¥{{ discountAmount }}</p>
      <p style="font-size:20px;color:#f56c6c;">应付：¥{{ payAmount }}</p>
      <el-button type="primary" size="large" @click="submitOrder" :loading="submitting">提交订单</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/store/cart';
import { getAddresses } from '@/api/client/user';
import { submitOrder as submitOrderApi } from '@/api/client/order';
import { getAvailableCoupons } from '@/api/client/coupon';
import { ElMessage } from 'element-plus';

const router = useRouter();
const cartStore = useCartStore();
const addresses = ref([]);
const coupons = ref([]);
const addressId = ref(null);
const couponId = ref(null);
const submitting = ref(false);

const totalAmount = computed(() => cartStore.items.reduce((s, i) => s + i.salePrice * i.quantity, 0).toFixed(2));
const discountAmount = computed(() => {
  const c = coupons.value.find(c => c.id === couponId.value);
  if (!c) return 0;
  // 检查是否达到使用门槛
  if (parseFloat(totalAmount.value) < c.threshold) return 0;
  return c.discount;
});
const payAmount = computed(() => (totalAmount.value - discountAmount.value).toFixed(2));

onMounted(async () => {
  await cartStore.fetchCart();
  const [addrRes, couponRes] = await Promise.all([
    getAddresses(),
    getAvailableCoupons().catch(() => ({ data: [] }))
  ]);
  addresses.value = addrRes.data || [];
  coupons.value = couponRes.data || [];
});

async function submitOrder() {
  if (!addressId.value) { ElMessage.warning('请选择收货地址'); return; }
  if (cartStore.items.length === 0) { ElMessage.warning('购物车为空'); return; }
  submitting.value = true;
  try {
    const selectedBookIds = cartStore.items.map(i => i.id);
    await submitOrderApi({ addressId: addressId.value, couponId: couponId.value || undefined, selectedBookIds });
    ElMessage.success('下单成功');
    router.push('/orders');
  } catch (e) {
    ElMessage.error(e.msg || '下单失败');
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
h3 { margin-bottom: 10px; font-size: 16px; }
</style>