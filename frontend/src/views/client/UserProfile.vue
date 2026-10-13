<template>
  <div class="container">
    <h2>个人中心</h2>
    <el-form :model="form" label-width="80px" style="max-width: 400px;">
      <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
      <el-form-item><el-button type="primary" @click="handleSave">保存</el-button></el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getProfile, updateProfile } from '@/api/client/user';
import { ElMessage } from 'element-plus';

const form = ref({ username: '', phone: '', email: '' });

onMounted(async () => {
  const res = await getProfile();
  form.value = res.data || {};
});

async function handleSave() {
  await updateProfile(form.value);
  ElMessage.success('保存成功');
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
</style>