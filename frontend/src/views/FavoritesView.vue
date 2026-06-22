<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <h1 class="page-title">我的收藏</h1>
      <div v-loading="loading" class="flower-grid">
        <FlowerCard v-for="f in favorites" :key="f.id" :flower="{ id: f.flowerId, name: f.flowerName, coverImage: f.flowerImage, price: f.price, originalPrice: f.originalPrice, sales: f.sales }" />
      </div>
      <el-empty v-if="!loading && favorites.length === 0" description="还没有收藏商品">
        <el-button type="primary" @click="$router.push('/flowers')">去逛逛</el-button>
      </el-empty>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import FlowerCard from '../components/FlowerCard.vue'
import { getFavorites } from '../api/favorite'

const favorites = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { favorites.value = await getFavorites() } finally { loading.value = false }
})
</script>

<style scoped>
.flower-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
@media (max-width: 900px) { .flower-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
