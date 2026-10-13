<template>
  <div class="pagination-wrap" v-if="total > 0">
    <el-pagination
      v-model:current-page="current"
      :page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="$emit('change', $event)"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  modelValue: { type: Number, default: 1 },
  total: { type: Number, default: 0 },
  pageSize: { type: Number, default: 10 }
});

const emit = defineEmits(['update:modelValue', 'change']);

const current = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
});
</script>

<style scoped>
.pagination-wrap { display: flex; justify-content: center; margin-top: 20px; }
</style>