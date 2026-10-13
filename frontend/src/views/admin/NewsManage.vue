<template>
  <div>
    <h2>公告管理</h2>
    <el-button type="primary" @click="showAddDialog" style="margin-bottom: 16px;">发布公告</el-button>
    <el-table :data="newsList" border stripe>
      <el-table-column prop="title" label="标题" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">{{ row.status === 1 ? '已发布' : '未发布' }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="editNews(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchNews" />

    <el-dialog v-model="dialogVisible" :title="editing ? '编辑公告' : '发布公告'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="6" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">发布</el-radio>
            <el-radio :value="0">不发布</el-radio>
          </el-radio-group>
        </el-form-item>
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
import { getNews, addNews, updateNews, deleteNews } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const newsList = ref([]);
const page = ref(1);
const total = ref(0);
const dialogVisible = ref(false);
const editing = ref(false);
const form = ref({ title: '', content: '', status: 1 });

onMounted(() => fetchNews());
async function fetchNews() {
  const res = await getNews({ page: page.value, size: 10 });
  newsList.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

function showAddDialog() { editing.value = false; form.value = { title: '', content: '', status: 1 }; dialogVisible.value = true; }
function editNews(row) { editing.value = true; form.value = { ...row }; dialogVisible.value = true; }

async function handleSave() {
  if (editing.value) { await updateNews(form.value.id, form.value); } else { await addNews(form.value); }
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  fetchNews();
}

async function handleDelete(id) {
  await deleteNews(id);
  ElMessage.success('删除成功');
  fetchNews();
}
</script>