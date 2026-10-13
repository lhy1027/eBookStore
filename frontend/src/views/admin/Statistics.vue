<template>
  <div>
    <h2>销售统计</h2>

    <div style="margin-bottom: 16px; display: flex; gap: 10px; align-items: center;">
      <el-date-picker v-model="dateRange" type="daterange" range-separator="至"
        start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD"
        style="width: 280px;" />
      <el-button type="primary" @click="fetchData">查询</el-button>
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">总销售额</div>
          <div class="stat-value">¥{{ stats.totalAmount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">总订单数</div>
          <div class="stat-value">{{ stats.totalOrders || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-label">销售图书数</div>
          <div class="stat-value">{{ stats.totalBooks || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card warning">
          <div class="stat-label">库存预警</div>
          <div class="stat-value">{{ warningCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header><span>日销售额趋势</span></template>
          <div ref="dailyChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>月销售额趋势</span></template>
          <div ref="monthlyChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header><span>热门图书排行</span></template>
          <div ref="hotChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>分类销售占比</span></template>
          <div ref="categoryChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue';
import * as echarts from 'echarts';
import { getSalesStatistics, getHotBooks, getCategorySales, getStockWarning } from '@/api/admin/adminBook';

const dateRange = ref([]);
const stats = ref({ totalAmount: 0, totalOrders: 0, totalBooks: 0 });
const warningCount = ref(0);

const dailyChartRef = ref(null);
const monthlyChartRef = ref(null);
const hotChartRef = ref(null);
const categoryChartRef = ref(null);

let dailyChart = null;
let monthlyChart = null;
let hotChart = null;
let categoryChart = null;

onMounted(() => fetchData());

async function fetchData() {
  const params = {};
  if (dateRange.value && dateRange.value.length === 2) {
    params.startDate = dateRange.value[0];
    params.endDate = dateRange.value[1];
  }
  try {
    const [salesRes, hotRes, catRes, warnRes] = await Promise.all([
      getSalesStatistics(params),
      getHotBooks(),
      getCategorySales(),
      getStockWarning()
    ]);
    const data = salesRes.data || {};
    stats.value = {
      totalAmount: data.totalAmount || 0,
      totalOrders: data.totalOrders || 0,
      totalBooks: data.totalBooks || 0
    };
    warningCount.value = warnRes.data?.count || 0;

    await nextTick();
    renderDailyChart(data.dailySales || []);
    renderMonthlyChart(data.monthlySales || []);
    renderHotChart(hotRes.data || []);
    renderCategoryChart(catRes.data || []);
  } catch (e) {
    /* ignore */
  }
}

function renderDailyChart(data) {
  if (!dailyChartRef.value) return;
  if (!dailyChart) dailyChart = echarts.init(dailyChartRef.value);
  dailyChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.date) },
    yAxis: { type: 'value', name: '销售额(元)' },
    series: [{
      name: '销售额', type: 'line', data: data.map(d => d.amount),
      smooth: true, areaStyle: { opacity: 0.3 }
    }]
  });
}

function renderMonthlyChart(data) {
  if (!monthlyChartRef.value) return;
  if (!monthlyChart) monthlyChart = echarts.init(monthlyChartRef.value);
  monthlyChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.month) },
    yAxis: { type: 'value', name: '销售额(元)' },
    series: [{
      name: '销售额', type: 'bar', data: data.map(d => d.amount),
      itemStyle: { color: '#67c23a' }
    }]
  });
}

function renderHotChart(data) {
  if (!hotChartRef.value) return;
  if (!hotChart) hotChart = echarts.init(hotChartRef.value);
  hotChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: { type: 'value', name: '销量' },
    yAxis: { type: 'category', data: data.map(d => d.name).reverse(), inverse: true },
    series: [{
      name: '销量', type: 'bar', data: data.map(d => d.sales).reverse(),
      itemStyle: { color: '#409eff' }
    }]
  });
}

function renderCategoryChart(data) {
  if (!categoryChartRef.value) return;
  if (!categoryChart) categoryChart = echarts.init(categoryChartRef.value);
  categoryChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie', radius: ['40%', '70%'],
      data: data.map(d => ({ name: d.name, value: d.value })),
      label: { formatter: '{b}: {d}%' }
    }]
  });
}
</script>

<style scoped>
h2 { margin-bottom: 20px; }
.stat-card { text-align: center; }
.stat-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: bold; }
.stat-card.warning .stat-value { color: #f56c6c; }
</style>