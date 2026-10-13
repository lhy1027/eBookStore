<template>
  <div class="container">
    <h2>我的订单</h2>
    <el-table :data="orders" style="width: 100%">
      <el-table-column label="订单号" prop="orderNo" width="200" />
      <el-table-column label="总金额" width="120">
        <template #default="{ row }">¥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column label="状态" prop="orderStatus" width="100" />
      <el-table-column label="物流" prop="logisticsStatus" width="90" />
      <el-table-column label="时间" prop="createTime" width="180" />
      <el-table-column label="操作" width="320">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/orders/${row.id}`)">详情</el-button>
          <el-button v-if="row.orderStatus === '待付款'" size="small" type="success" @click="handlePay(row.id)">支付</el-button>
          <el-button v-if="row.orderStatus === '待付款' || row.orderStatus === '待发货'" size="small" type="danger" @click="handleCancel(row.id)">取消</el-button>
          <el-button v-if="row.orderStatus === '已发货'" size="small" type="primary" @click="handleConfirm(row.id)">确认收货</el-button>
          <el-button v-if="row.orderStatus === '已发货' || row.orderStatus === '已签收'" size="small" type="warning" @click="handleRefund(row.id)">申请退款</el-button>
          <el-button v-if="row.orderStatus === '已签收'" size="small" type="success" @click="$router.push(`/orders/${row.id}`)">评价</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="orders.length === 0" style="text-align:center;padding:40px;color:#999;">暂无订单</div>
    <Pagination v-model="page" :total="total" :page-size="size" @change="fetchOrders" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getOrderList, payOrder, cancelOrder, confirmOrder, refundOrder } from '@/api/client/order';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const orders = ref([]);
const page = ref(1);
const size = ref(10);
const total = ref(0);

onMounted(() => fetchOrders());
async function fetchOrders() {
  const res = await getOrderList({ page: page.value, size: size.value });
  orders.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

async function handlePay(id) {
  try { await payOrder(id); ElMessage.success('支付成功'); fetchOrders(); } catch (e) { ElMessage.error(e.msg); }
}
async function handleCancel(id) {
  try { await cancelOrder(id); ElMessage.success('已取消'); fetchOrders(); } catch (e) { ElMessage.error(e.msg); }
}
async function handleConfirm(id) {
  try { await confirmOrder(id); ElMessage.success('已确认收货'); fetchOrders(); } catch (e) { ElMessage.error(e.msg); }
}
async function handleRefund(id) {
  try { await refundOrder(id); ElMessage.success('退款申请已提交'); fetchOrders(); } catch (e) { ElMessage.error(e.msg); }
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
</style>