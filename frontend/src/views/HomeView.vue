<template>
  <div class="home-page">
    <AppHeader />
    <!-- Banner -->
    <section class="banner">
      <div class="banner-content">
        <h1>欢迎来到 FlowerShop 🌸</h1>
        <p>每一束花，都是心意的传递</p>
      </div>
    </section>
    <!-- 分类 -->
    <section class="section">
      <h2 class="section-title">花卉分类</h2>
      <div class="category-list">
        <span v-for="cat in categories" :key="cat.id" class="category-tag"
              @click="$router.push('/flowers?categoryId=' + cat.id)">
          {{ cat.name }}
        </span>
      </div>
    </section>
    <!-- 热销 -->
    <section class="section">
      <h2 class="section-title">🔥 热销花卉</h2>
      <div class="flower-grid">
        <FlowerCard v-for="f in hotFlowers" :key="f.id" :flower="f" />
      </div>
      <div class="more-link">
        <el-button type="primary" @click="$router.push('/flowers')">查看更多</el-button>
      </div>
    </section>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import FlowerCard from '../components/FlowerCard.vue'
import { getCategories } from '../api/category'
import { getHotFlowers } from '../api/flower'

const categories = ref([])
const hotFlowers = ref([])

onMounted(async () => {
  categories.value = await getCategories()
  hotFlowers.value = await getHotFlowers(8)
})
</script>

<style scoped>
.banner {
  background: linear-gradient(135deg, #fce4ec 0%, #f8bbd0 50%, #e1bee7 100%);
  padding: 80px 20px;
  text-align: center;
}
.banner-content h1 { font-size: 36px; color: #c2185b; margin-bottom: 12px; }
.banner-content p { font-size: 18px; color: #666; }
.section { max-width: 1200px; margin: 40px auto; padding: 0 20px; }
.section-title { font-size: 22px; font-weight: bold; margin-bottom: 20px; }
.category-list { display: flex; gap: 12px; flex-wrap: wrap; }
.category-tag {
  display: inline-block; padding: 8px 20px; background: #fce4ec;
  border-radius: 20px; color: #c2185b; cursor: pointer; transition: all 0.2s;
}
.category-tag:hover { background: #e74c8b; color: #fff; }
.flower-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
@media (max-width: 900px) { .flower-grid { grid-template-columns: repeat(2, 1fr); } }
.more-link { text-align: center; margin-top: 24px; }
</style>
