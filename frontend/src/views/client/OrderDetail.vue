<template>
  <div class="container">
    <h2>订单详情</h2>
    <el-card v-if="order.id" style="margin-bottom: 20px;">
      <p><strong>订单号：</strong>{{ order.orderNo }}</p>
      <p><strong>状态：</strong>{{ order.orderStatus }}</p>
      <p><strong>订单金额：</strong>¥{{ order.totalAmount }}</p>
      <p v-if="order.discountAmount > 0"><strong>优惠金额：</strong>-¥{{ order.discountAmount }}</p>
      <p><strong>实付金额：</strong>¥{{ order.payAmount || order.totalAmount }}</p>
      <p><strong>下单时间：</strong>{{ order.createTime }}</p>
      <p v-if="order.payTime"><strong>支付时间：</strong>{{ order.payTime }}</p>
      <p v-if="order.shipTime"><strong>发货时间：</strong>{{ order.shipTime }}</p>
      <p v-if="order.logisticsStatus"><strong>物流状态：</strong>{{ order.logisticsStatus }}</p>
      <p v-if="order.receiptTime"><strong>签收时间：</strong>{{ order.receiptTime }}</p>
      <div style="margin-top: 10px;">
        <el-button v-if="order.orderStatus === '待付款'" type="success" @click="handlePay">立即支付</el-button>
        <el-button v-if="order.orderStatus === '待付款' || order.orderStatus === '待发货'" type="danger" @click="handleCancel">取消订单</el-button>
        <el-button v-if="order.orderStatus === '已发货'" type="primary" @click="handleConfirm">确认收货</el-button>
        <el-button v-if="order.orderStatus === '已发货' || order.orderStatus === '已签收'" type="warning" @click="handleRefund">申请退款</el-button>
      </div>
    </el-card>

    <h3>订单商品</h3>
    <el-table :data="orderItems" style="width: 100%">
      <el-table-column label="图书" prop="bookName" />
      <el-table-column label="单价" width="120">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column label="数量" prop="quantity" width="100" />
      <el-table-column label="小计" width="120">
        <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column v-if="order.orderStatus === '已签收'" label="评价" width="120">
        <template #default="{ row }">
          <el-button v-if="!row.reviewed" size="small" type="success" @click="showReviewDialog(row)">评价</el-button>
          <span v-else style="color:#67c23a;">已评价</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 评价对话框 -->
    <el-dialog v-model="reviewVisible" title="商品评价" width="400px">
      <el-form label-width="80px">
        <el-form-item label="商品">{{ reviewItem.bookName }}</el-form-item>
        <el-form-item label="评分">
          <el-rate v-model="reviewRating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="reviewContent" type="textarea" :rows="3" placeholder="请输入评价内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </template>
    </el-dialog>

    <div v-if="!order.id" style="text-align:center;padding:40px;color:#999;">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getOrderDetail, getOrderItems, payOrder, cancelOrder, confirmOrder, refundOrder } from '@/api/client/order';
import { addReview } from '@/api/client/review';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();
const order = ref({});
const orderItems = ref([]);
const reviewVisible = ref(false);
const reviewItem = ref({});
const reviewRating = ref(5);
const reviewContent = ref('');

onMounted(async () => {
  try {
    const [detailRes, itemsRes] = await Promise.all([
      getOrderDetail(route.params.id),
      getOrderItems(route.params.id)
    ]);
    order.value = detailRes.data;
    orderItems.value = itemsRes.data || [];
  } catch (e) {
    ElMessage.error('加载订单失败');
    router.push('/orders');
  }
});

async function handlePay() {
  try { await payOrder(route.params.id); ElMessage.success('支付成功'); window.location.reload(); } catch (e) { ElMessage.error(e.msg || '支付失败'); }
}
async function handleCancel() {
  try { await cancelOrder(route.params.id); ElMessage.success('已取消'); window.location.reload(); } catch (e) { ElMessage.error(e.msg || '取消失败'); }
}
async function handleConfirm() {
  try { await confirmOrder(route.params.id); ElMessage.success('已确认收货'); window.location.reload(); } catch (e) { ElMessage.error(e.msg || '确认失败'); }
}
async function handleRefund() {
  try { await refundOrder(route.params.id); ElMessage.success('退款申请已提交'); window.location.reload(); } catch (e) { ElMessage.error(e.msg || '申请失败'); }
}
function showReviewDialog(item) {
  reviewItem.value = item;
  reviewRating.value = 5;
  reviewContent.value = '';
  reviewVisible.value = true;
}
async function submitReview() {
  if (!reviewContent.value.trim()) { ElMessage.warning('请输入评价内容'); return; }
  try {
    await addReview({ bookId: reviewItem.value.bookId, orderItemId: reviewItem.value.id, rating: reviewRating.value, content: reviewContent.value });
    ElMessage.success('评价成功');
    reviewVisible.value = false;
    window.location.reload();
  } catch (e) { ElMessage.error(e.msg || '评价失败'); }
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
h3 { margin-bottom: 10px; }
p { margin-bottom: 8px; }
</style>