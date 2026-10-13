<template>
  <div class="container">
    <h2>登录</h2>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" style="max-width: 400px; margin: 20px auto;">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
        <router-link to="/register" style="margin-left: 10px;">没有账号？去注册</router-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const formRef = ref(null);
const loading = ref(false);
const form = ref({ username: '', password: '' });
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false);
  if (!valid) return;
  loading.value = true;
  try {
    await userStore.login(form.value);
    ElMessage.success('登录成功');
    // 管理员直接跳转后台管理，普通用户跳转首页
    if (userStore.isAdmin()) {
      router.push('/admin');
    } else {
      router.push('/');
    }
  } catch (e) {
    ElMessage.error(e.msg || '登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { text-align: center; margin-bottom: 20px; }
</style>