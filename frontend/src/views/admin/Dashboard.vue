<template>
  <div>
    <h2>管理首页</h2>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6"><el-card><h3>图书总数</h3><p style="font-size:24px;">{{ stats.bookCount }}</p></el-card></el-col>
      <el-col :span="6"><el-card><h3>订单总数</h3><p style="font-size:24px;">{{ stats.orderCount }}</p></el-card></el-col>
      <el-col :span="6"><el-card><h3>会员总数</h3><p style="font-size:24px;">{{ stats.userCount }}</p></el-card></el-col>
      <el-col :span="6"><el-card><h3>库存预警</h3><p style="font-size:24px;color:#f56c6c;">{{ stats.warningCount }}</p></el-card></el-col>
    </el-row>

    <el-card style="margin-top: 20px; max-width: 400px;">
      <template #header><span>系统设置</span></template>
      <el-form label-width="120px" :inline="true">
        <el-form-item label="库存预警阈值">
          <el-input-number v-model="warningThreshold" :min="1" :max="999" style="width: 120px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveThreshold" :loading="saving">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getDashboardStats, getConfig, setConfig } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';

const stats = ref({ bookCount: 0, orderCount: 0, userCount: 0, warningCount: 0 });
const warningThreshold = ref(5);
const saving = ref(false);

onMounted(async () => {
  try {
    const [statsRes, configRes] = await Promise.all([
      getDashboardStats(),
      getConfig('stock_warning_threshold')
    ]);
    stats.value = { ...stats.value, ...statsRes.data };
    warningThreshold.value = parseInt(configRes.data) || 5;
  } catch (e) { /* ignore */ }
});

async function saveThreshold() {
  saving.value = true;
  try {
    await setConfig('stock_warning_threshold', String(warningThreshold.value));
    ElMessage.success('预警阈值已更新');
    const res = await getDashboardStats();
    stats.value = { ...stats.value, ...res.data };
  } catch (e) {
    ElMessage.error('保存失败');
  } finally {
    saving.value = false;
  }
}
</script>