<template>
  <div>
    <AppHeader />
    <div class="page-container" v-loading="loading">
      <div v-if="flower" class="detail">
        <div class="detail-image">
          <img :src="flower.coverImage" :alt="flower.name" @error="e => e.target.src='data:image/svg+xml,' + encodeURIComponent('<svg xmlns=%22http://www.w3.org/2000/svg%22 width=%22450%22 height=%22450%22><rect fill=%22%23fce4ec%22 width=%22450%22 height=%22450%22/><text fill=%22%23e74c8b%22 font-size=%22100%22 text-anchor=%22middle%22 dy=%22.35em%22 x=%22225%22 y=%22225%22>🌸</text></svg>')" />
        </div>
        <div class="detail-info">
          <h1>{{ flower.name }}</h1>
          <p class="detail-category">{{ flower.categoryName }}</p>
          <div class="detail-price">
            <span class="price">¥{{ flower.price }}</span>
            <span v-if="flower.originalPrice > flower.price" class="original-price">¥{{ flower.originalPrice }}</span>
          </div>
          <p class="detail-stock">库存: {{ flower.stock }} | 已售: {{ flower.sales }}</p>
          <p class="detail-desc">{{ flower.description }}</p>
          <div class="detail-actions">
            <el-input-number v-model="quantity" :min="1" :max="flower.stock" />
            <el-button type="primary" size="large" @click="addToCart">加入购物车</el-button>
            <el-button size="large" @click="toggleFavorite">
              <el-icon><StarFilled v-if="favorited" style="color:#e74c8b" /><Star v-else /></el-icon>
              {{ favorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </div>
      <!-- 评价 -->
      <div class="reviews-section">
        <h3>用户评价 ({{ reviewTotal }})</h3>
        <div v-for="r in reviews" :key="r.id" class="review-item">
          <div class="review-header">
            <span class="review-user">{{ r.username }}</span>
            <el-rate :model-value="r.rating" disabled show-score size="small" />
            <span class="review-date">{{ formatDate(r.createdAt) }}</span>
          </div>
          <p class="review-content">{{ r.content }}</p>
        </div>
        <el-empty v-if="reviews.length === 0" description="暂无评价" />
      </div>
      <!-- 写评价 -->
      <div v-if="auth.token" class="review-form">
        <h3>写评价</h3>
        <el-rate v-model="reviewForm.rating" show-score />
        <el-input v-model="reviewForm.content" type="textarea" :rows="3" placeholder="分享您的感受..." class="mt-10" />
        <el-button type="primary" class="mt-10" @click="submitReview" :loading="submitting">提交评价</el-button>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { getFlowerDetail } from '../api/flower'
import { getReviews, createReview } from '../api/review'
import { addFavorite, removeFavorite, checkFavorite } from '../api/favorite'
import { addToCart as addCartAPI } from '../api/cart'
import { useAuthStore } from '../stores/auth'
import { useCartStore } from '../stores/cart'
import { formatDate } from '../utils/format'

const route = useRoute()
const auth = useAuthStore()
const cartStore = useCartStore()

const flower = ref(null)
const loading = ref(false)
const quantity = ref(1)
const favorited = ref(false)
const reviews = ref([])
const reviewTotal = ref(0)
const reviewForm = ref({ rating: 5, content: '' })
const submitting = ref(false)

onMounted(() => loadDetail())

async function loadDetail() {
  loading.value = true
  try {
    flower.value = await getFlowerDetail(route.params.id)
    // 加载评价
    const reviewData = await getReviews(flower.value.id)
    reviews.value = reviewData.records
    reviewTotal.value = reviewData.total
    // 检查收藏
    if (auth.token) {
      try {
        const res = await checkFavorite(flower.value.id)
        favorited.value = res.favorited
      } catch {}
    }
  } finally {
    loading.value = false
  }
}

async function addToCart() {
  if (!auth.token) { ElMessage.warning('请先登录'); return }
  await cartStore.add(flower.value.id, quantity.value)
  ElMessage.success('已加入购物车')
}

async function toggleFavorite() {
  if (!auth.token) { ElMessage.warning('请先登录'); return }
  if (favorited.value) {
    await removeFavorite(flower.value.id)
    favorited.value = false
    ElMessage.success('已取消收藏')
  } else {
    await addFavorite(flower.value.id)
    favorited.value = true
    ElMessage.success('已收藏')
  }
}

async function submitReview() {
  if (!reviewForm.value.content) { ElMessage.warning('请输入评价内容'); return }
  submitting.value = true
  try {
    await createReview({ flowerId: flower.value.id, rating: reviewForm.value.rating, content: reviewForm.value.content })
    ElMessage.success('评价成功')
    reviewForm.value = { rating: 5, content: '' }
    loadDetail()
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.detail { display: flex; gap: 40px; margin-bottom: 40px; }
.detail-image { width: 450px; height: 450px; flex-shrink: 0; }
.detail-image img { width: 100%; height: 100%; object-fit: cover; border-radius: 8px; }
.detail-info h1 { font-size: 24px; margin-bottom: 8px; }
.detail-category { color: #999; margin-bottom: 12px; }
.detail-price { margin-bottom: 12px; display: flex; align-items: baseline; gap: 8px; }
.price { color: #e74c8b; font-size: 28px; font-weight: bold; }
.original-price { color: #ccc; text-decoration: line-through; }
.detail-stock { color: #666; margin-bottom: 12px; }
.detail-desc { color: #555; line-height: 1.8; margin-bottom: 20px; }
.detail-actions { display: flex; gap: 12px; align-items: center; }
.reviews-section { margin-top: 40px; border-top: 1px solid #eee; padding-top: 30px; }
.review-item { padding: 16px 0; border-bottom: 1px solid #f0f0f0; }
.review-header { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; }
.review-user { font-weight: 600; }
.review-date { color: #999; font-size: 12px; }
.review-content { color: #555; }
.review-form { margin-top: 24px; }
.mt-10 { margin-top: 10px; }
@media (max-width: 768px) { .detail { flex-direction: column; } .detail-image { width: 100%; height: 300px; } }
</style>
