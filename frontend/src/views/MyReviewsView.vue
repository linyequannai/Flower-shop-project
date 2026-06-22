<template>
  <div>
    <AppHeader />
    <div class="page-container" style="max-width:700px">
      <h1 class="page-title">我的评价</h1>
      <div v-for="r in reviews" :key="r.id" class="review-card">
        <div class="review-header">
          <el-rate :model-value="r.rating" disabled size="small" />
          <span class="review-date">{{ formatDate(r.createdAt) }}</span>
        </div>
        <p>{{ r.content }}</p>
        <el-button size="small" type="danger" @click="deleteReview(r.id)">删除</el-button>
      </div>
      <el-empty v-if="reviews.length === 0" description="还没有评价" />
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { getMyReviews, deleteReview as deleteReviewAPI } from '../api/review'
import { formatDate } from '../utils/format'

const reviews = ref([])

onMounted(async () => { reviews.value = await getMyReviews() })

async function deleteReview(id) {
  await deleteReviewAPI(id)
  ElMessage.success('删除成功')
  reviews.value = reviews.value.filter(r => r.id !== id)
}
</script>

<style scoped>
.review-card { padding: 16px; background: #fff; border-radius: 8px; margin-bottom: 12px; }
.review-header { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; }
.review-date { color: #999; font-size: 12px; }
.review-card p { color: #555; margin-bottom: 8px; }
</style>
