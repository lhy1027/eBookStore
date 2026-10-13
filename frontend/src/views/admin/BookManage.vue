<template>
  <div>
    <h2>图书管理</h2>
    <div style="margin-bottom: 16px; display: flex; gap: 10px;">
      <el-input v-model="keyword" placeholder="搜索图书" clearable style="width: 200px;" />
      <el-button type="primary" @click="fetchBooks">搜索</el-button>
      <el-button type="success" @click="showAddDialog">新增图书</el-button>
      <el-button @click="handleExport">导出</el-button>
    </div>
    <el-table :data="books" border stripe>
      <el-table-column prop="bookNo" label="编号" width="100" />
      <el-table-column prop="name" label="书名" />
      <el-table-column prop="author" label="作者" width="100" />
      <el-table-column prop="price" label="定价" width="80" />
      <el-table-column prop="salePrice" label="会员价" width="90" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="上架" width="80">
        <template #default="{ row }"><el-switch :model-value="row.status === 1" @change="toggleStatus(row)" /></template>
      </el-table-column>
      <el-table-column label="推荐" width="80">
        <template #default="{ row }"><el-switch :model-value="row.recommendStatus === 1" @change="toggleRecommend(row)" /></template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="editBook(row)">编辑</el-button>
          <el-button size="small" @click="showStockDialog(row)">库存</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchBooks" />

    <el-dialog v-model="dialogVisible" :title="editing ? '编辑图书' : '新增图书'" width="700px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="图书编号" prop="bookNo"><el-input v-model="form.bookNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="ISBN"><el-input v-model="form.isbn" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="书名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="作者"><el-input v-model="form.author" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="出版社"><el-input v-model="form.publisher" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="定价" prop="price"><el-input-number v-model="form.price" :min="0" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="会员价" prop="salePrice"><el-input-number v-model="form.salePrice" :min="0" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="库存" prop="stock"><el-input-number v-model="form.stock" :min="0" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="出版日期"><el-date-picker v-model="form.publishDate" type="date" placeholder="选择日期" style="width: 100%;" value-format="YYYY-MM-DD" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="分类" prop="categoryId">
          <el-cascader
            v-model="categoryCascader"
            :options="categoryOptions"
            :props="{ value: 'id', label: 'name', children: 'children', checkStrictly: false, emitPath: false }"
            placeholder="请选择分类"
            clearable
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            :auto-upload="false"
            :show-file-list="false"
            accept="image/jpeg,image/png,image/gif,image/webp"
            @change="handleCoverChange"
          >
            <el-button size="small">选择图片</el-button>
            <template #tip>
              <span style="font-size:12px;color:#999;margin-left:8px;">建议尺寸 300x400，支持jpg/png/gif</span>
            </template>
          </el-upload>
          <img v-if="form.coverUrl" :src="form.coverUrl" style="width:80px;height:100px;object-fit:cover;border-radius:4px;margin-top:8px;" />
        </el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="详情"><el-input v-model="form.detail" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="stockDialogVisible" title="调整库存" width="400px">
      <el-form-item label="调整数量"><el-input-number v-model="stockChange" /></el-form-item>
      <template #footer>
        <el-button @click="stockDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStockUpdate">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { getBooks, addBook, updateBook, toggleBookStatus, toggleBookRecommend, updateBookStock, uploadCover, exportBooks, getCategories } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const books = ref([]);
const keyword = ref('');
const page = ref(1);
const total = ref(0);
const dialogVisible = ref(false);
const editing = ref(false);
const form = ref({});
const stockDialogVisible = ref(false);
const stockChange = ref(0);
const categoryOptions = ref([]);
const categoryCascader = ref(null);
const formRef = ref(null);
let stockBookId = null;

const rules = {
  bookNo: [{ required: true, message: '请输入图书编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入图书名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入定价', trigger: 'blur' }],
  salePrice: [{ required: true, message: '请输入会员价', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
};

onMounted(() => { fetchBooks(); fetchCategories(); });
async function fetchBooks() {
  const res = await getBooks({ page: page.value, size: 10, keyword: keyword.value || undefined });
  books.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}
async function fetchCategories() {
  const res = await getCategories();
  const cats = res.data || [];
  const tree = [];
  const map = {};
  cats.forEach(c => { map[c.id] = { ...c, children: [] }; });
  cats.forEach(c => {
    if (c.parentId === 0) { tree.push(map[c.id]); }
    else if (map[c.parentId]) { map[c.parentId].children.push(map[c.id]); }
  });
  categoryOptions.value = tree;
}

// 编辑时同步分类选择器
watch(() => form.value.categoryId, (val) => {
  categoryCascader.value = val || null;
});
// 选择分类时同步到 form.categoryId 用于校验
watch(categoryCascader, (val) => {
  form.value.categoryId = val || undefined;
});

async function handleCoverChange(uploadFile) {
  try {
    const res = await uploadCover(uploadFile.raw);
    form.value.coverUrl = res.data;
    ElMessage.success('封面上传成功');
  } catch (e) {
    ElMessage.error('封面上传失败');
  }
}

function showAddDialog() { editing.value = false; form.value = {}; categoryCascader.value = null; dialogVisible.value = true; }
function editBook(row) { editing.value = true; form.value = { ...row }; categoryCascader.value = row.categoryId || null; dialogVisible.value = true; }

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false);
  if (!valid) return;
  form.value.categoryId = categoryCascader.value;
  if (editing.value) { await updateBook(form.value.id, form.value); } else { await addBook(form.value); }
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  fetchBooks();
}

async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1;
  await toggleBookStatus(row.id, newStatus);
  ElMessage.success('操作成功');
  fetchBooks();
}

async function toggleRecommend(row) {
  const rs = row.recommendStatus === 1 ? 0 : 1;
  await toggleBookRecommend(row.id, rs);
  ElMessage.success('操作成功');
  fetchBooks();
}

function showStockDialog(row) { stockBookId = row.id; stockChange.value = 0; stockDialogVisible.value = true; }
async function handleStockUpdate() {
  await updateBookStock(stockBookId, stockChange.value);
  ElMessage.success('库存调整成功');
  stockDialogVisible.value = false;
  fetchBooks();
}

async function handleExport() {
  try {
    const res = await exportBooks(keyword.value || undefined);
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const a = document.createElement('a');
    a.href = url;
    a.download = 'books.csv';
    a.click();
    window.URL.revokeObjectURL(url);
    ElMessage.success('导出成功');
  } catch (e) { ElMessage.error('导出失败'); }
}
</script>