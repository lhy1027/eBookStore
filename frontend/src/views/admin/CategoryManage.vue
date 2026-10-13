<template>
  <div>
    <h2>分类管理</h2>
    <el-button type="primary" @click="showAddDialog" style="margin-bottom: 16px;">新增分类</el-button>
    <el-table :data="categories" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="parentId" label="父级ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="editCategory(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editing ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="父级ID"><el-input-number v-model="form.parentId" :min="0" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
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
import { getCategories, addCategory, updateCategory, deleteCategory } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';

const categories = ref([]);
const dialogVisible = ref(false);
const editing = ref(false);
const form = ref({});

onMounted(() => fetchCategories());
async function fetchCategories() {
  const res = await getCategories();
  categories.value = res.data || [];
}

function showAddDialog() { editing.value = false; form.value = {}; dialogVisible.value = true; }
function editCategory(row) { editing.value = true; form.value = { ...row }; dialogVisible.value = true; }

async function handleSave() {
  if (!form.value.name || !form.value.name.trim()) { ElMessage.warning('分类名称不能为空'); return; }
  if (editing.value) { await updateCategory(form.value.id, form.value); } else { await addCategory(form.value); }
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  fetchCategories();
}

async function handleDelete(id) {
  try { await deleteCategory(id); ElMessage.success('删除成功'); fetchCategories(); } catch (e) { ElMessage.error(e.msg); }
}
</script>