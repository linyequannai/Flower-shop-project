<template>
  <div class="pagination-wrap">
    <el-pagination
      v-model:current-page="current"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[12, 24, 48]"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @current-change="$emit('change', $event)"
      @size-change="$emit('sizeChange', $event)"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  total: { type: Number, default: 0 },
  page: { type: Number, default: 1 },
  size: { type: Number, default: 12 },
})

defineEmits(['change', 'sizeChange'])

const current = computed({ get: () => props.page, set: () => {} })
const pageSize = computed({ get: () => props.size, set: () => {} })
</script>

<style scoped>
.pagination-wrap {
  display: flex;
  justify-content: center;
  padding: 30px 0;
}
</style>
