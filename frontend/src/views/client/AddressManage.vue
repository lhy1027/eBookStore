<template>
  <div class="container">
    <h2>收货地址</h2>
    <el-button type="primary" @click="showDialog = true" style="margin-bottom: 20px;">新增地址</el-button>
    <el-table :data="addresses" style="width: 100%">
      <el-table-column label="收货人" prop="receiverName" />
      <el-table-column label="电话" prop="phone" />
      <el-table-column label="地区" width="300">
        <template #default="{ row }">{{ row.region }}</template>
      </el-table-column>
      <el-table-column label="详细地址" prop="detailAddress" />
      <el-table-column label="默认" width="80">
        <template #default="{ row }">{{ row.isDefault ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="editAddress(row)">编辑</el-button>
          <el-button size="small" @click="setDefault(row.id)">设为默认</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showDialog" :title="editing ? '编辑地址' : '新增地址'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="收货人"><el-input v-model="form.receiverName" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="地区"><el-input v-model="form.region" placeholder="省/市/区" /></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="form.detailAddress" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getAddresses, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/client/user';
import { ElMessage } from 'element-plus';

const addresses = ref([]);
const showDialog = ref(false);
const editing = ref(false);
const form = ref({ receiverName: '', phone: '', region: '', detailAddress: '' });
let editId = null;

onMounted(() => fetchAddresses());
async function fetchAddresses() {
  const res = await getAddresses();
  addresses.value = res.data || [];
}

function editAddress(row) {
  editing.value = true;
  editId = row.id;
  form.value = { ...row };
  showDialog.value = true;
}

async function handleSave() {
  if (editing.value) {
    await updateAddress(editId, form.value);
  } else {
    await addAddress(form.value);
  }
  ElMessage.success('保存成功');
  showDialog.value = false;
  editing.value = false;
  form.value = { receiverName: '', phone: '', region: '', detailAddress: '' };
  fetchAddresses();
}

async function handleDelete(id) {
  await deleteAddress(id);
  ElMessage.success('删除成功');
  fetchAddresses();
}

async function setDefault(id) {
  await setDefaultAddress(id);
  ElMessage.success('设置成功');
  fetchAddresses();
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
</style>