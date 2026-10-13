<template>
  <div>
    <h2>订单管理</h2>
    <div style="margin-bottom: 16px; display: flex; gap: 10px;">
      <el-input v-model="orderNo" placeholder="订单号" clearable style="width: 200px;" />
      <el-input v-model="memberName" placeholder="会员名" clearable style="width: 150px;" />
      <el-select v-model="orderStatus" placeholder="订单状态" clearable style="width: 150px;">
        <el-option label="待付款" value="待付款" />
        <el-option label="已付款" value="已付款" />
        <el-option label="待发货" value="待发货" />
        <el-option label="已发货" value="已发货" />
        <el-option label="已签收" value="已签收" />
        <el-option label="已取消" value="已取消" />
        <el-option label="退款申请中" value="退款申请中" />
        <el-option label="已退款" value="已退款" />
      </el-select>
      <el-button type="primary" @click="fetchOrders">搜索</el-button>
      <el-button @click="handleExport">导出</el-button>
    </div>
    <el-table :data="orders" border stripe>
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column label="金额" width="120"><template #default="{ row }">¥{{ row.totalAmount }}</template></el-table-column>
      <el-table-column prop="orderStatus" label="状态" width="100" />
      <el-table-column prop="logisticsStatus" label="物流" width="90" />
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="440">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">详情</el-button>
          <el-button v-if="row.orderStatus === '已付款'" size="small" type="success" @click="handlePay(row.id)">确认收款</el-button>
          <el-button v-if="row.orderStatus === '待发货'" size="small" type="primary" @click="handleShip(row.id)">发货</el-button>
          <el-button v-if="row.orderStatus === '已发货' || row.orderStatus === '已签收'" size="small" type="warning" @click="showLogisticsDialog(row)">物流</el-button>
          <el-button v-if="row.orderStatus === '待付款' || row.orderStatus === '待发货'" size="small" type="danger" @click="handleCancel(row.id)">取消</el-button>
          <el-button v-if="row.orderStatus === '退款申请中'" size="small" type="success" @click="handleRefundApprove(row.id)">同意退款</el-button>
          <el-button v-if="row.orderStatus === '退款申请中'" size="small" type="danger" @click="handleRefundReject(row.id)">拒绝退款</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchOrders" />

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <p><strong>订单号：</strong>{{ detailOrder.orderNo }}</p>
      <p><strong>状态：</strong>{{ detailOrder.orderStatus }}</p>
      <p><strong>总金额：</strong>¥{{ detailOrder.totalAmount }}</p>
      <p v-if="detailOrder.discountAmount > 0"><strong>优惠金额：</strong>-¥{{ detailOrder.discountAmount }}</p>
      <p><strong>实付金额：</strong>¥{{ detailOrder.payAmount || detailOrder.totalAmount }}</p>
      <p><strong>下单时间：</strong>{{ detailOrder.createTime }}</p>
      <p v-if="detailOrder.payTime"><strong>支付时间：</strong>{{ detailOrder.payTime }}</p>
      <p v-if="detailOrder.shipTime"><strong>发货时间：</strong>{{ detailOrder.shipTime }}</p>
      <p v-if="detailOrder.logisticsStatus"><strong>物流状态：</strong>{{ detailOrder.logisticsStatus }}</p>
      <p v-if="detailOrder.receiptTime"><strong>签收时间：</strong>{{ detailOrder.receiptTime }}</p>
    </el-dialog>

    <!-- 物流状态更新对话框 -->
    <el-dialog v-model="logisticsVisible" title="更新物流状态" width="400px">
      <el-form label-width="80px">
        <el-form-item label="当前状态"><el-tag>{{ logisticsOrder.logisticsStatus || '未发货' }}</el-tag></el-form-item>
        <el-form-item label="更新为">
          <el-select v-model="logisticsStatus" placeholder="选择物流状态" style="width: 100%;">
            <el-option label="已出库" value="已出库" />
            <el-option label="配送中" value="配送中" />
            <el-option label="已送达" value="已送达" />
            <el-option label="已签收" value="已签收" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="logisticsVisible = false">取消</el-button>
        <el-button type="primary" @click="handleLogisticsUpdate">确认更新</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getOrders, payOrder, shipOrder, cancelOrder, updateLogistics, refundApprove, refundReject } from '@/api/admin/adminOrder';
import { exportOrders } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const orders = ref([]);
const orderNo = ref('');
const memberName = ref('');
const orderStatus = ref('');
const page = ref(1);
const total = ref(0);
const detailVisible = ref(false);
const detailOrder = ref({});
const logisticsVisible = ref(false);
const logisticsOrder = ref({});
const logisticsStatus = ref('');

onMounted(() => fetchOrders());
async function fetchOrders() {
  const res = await getOrders({ page: page.value, size: 10, orderNo: orderNo.value || undefined, orderStatus: orderStatus.value || undefined, memberName: memberName.value || undefined });
  orders.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

function viewDetail(row) {
  detailOrder.value = row;
  detailVisible.value = true;
}

async function handlePay(id) {
  try { await payOrder(id); ElMessage.success('确认收款成功'); fetchOrders(); } catch (e) { ElMessage.error(e.msg || '操作失败'); }
}
async function handleShip(id) {
  try { await shipOrder(id); ElMessage.success('发货成功'); fetchOrders(); } catch (e) { ElMessage.error(e.msg || '操作失败'); }
}
async function handleCancel(id) {
  try { await cancelOrder(id); ElMessage.success('已取消'); fetchOrders(); } catch (e) { ElMessage.error(e.msg || '操作失败'); }
}

function showLogisticsDialog(row) {
  logisticsOrder.value = row;
  logisticsStatus.value = '';
  logisticsVisible.value = true;
}

async function handleLogisticsUpdate() {
  if (!logisticsStatus.value) { ElMessage.warning('请选择物流状态'); return; }
  try {
    await updateLogistics(logisticsOrder.value.id, logisticsStatus.value);
    ElMessage.success('物流状态已更新');
    logisticsVisible.value = false;
    fetchOrders();
  } catch (e) { ElMessage.error(e.msg || '操作失败'); }
}

async function handleExport() {
  try {
    const res = await exportOrders();
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const a = document.createElement('a');
    a.href = url;
    a.download = 'orders.csv';
    a.click();
    window.URL.revokeObjectURL(url);
    ElMessage.success('导出成功');
  } catch (e) { ElMessage.error('导出失败'); }
}

async function handleRefundApprove(id) {
  try { await refundApprove(id); ElMessage.success('退款已处理'); fetchOrders(); } catch (e) { ElMessage.error(e.msg || '操作失败'); }
}
async function handleRefundReject(id) {
  try { await refundReject(id); ElMessage.success('已拒绝退款'); fetchOrders(); } catch (e) { ElMessage.error(e.msg || '操作失败'); }
}
</script>