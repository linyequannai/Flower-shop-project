<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <!-- 搜索筛选 -->
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="搜索花卉..." class="search-input" clearable @keyup.enter="search" @clear="search">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-select v-model="categoryId" placeholder="全部分类" clearable class="filter-select" @change="search">
          <el-option label="全部分类" :value="null" />
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-select v-model="sort" placeholder="默认排序" class="filter-select" @change="search">
          <el-option label="默认排序" value="" />
          <el-option label="销量优先" value="sales" />
          <el-option label="价格从低到高" value="price_asc" />
          <el-option label="价格从高到低" value="price_desc" />
        </el-select>
      </div>
      <!-- 列表 -->
      <div v-loading="loading" class="flower-grid">
        <FlowerCard v-for="f in flowers" :key="f.id" :flower="f" />
      </div>
      <el-empty v-if="!loading && flowers.length === 0" description="没有找到相关花卉" />
      <Pagination v-if="total > 0" :total="total" :page="page" :size="size"
                  @change="onPageChange" @size-change="onSizeChange" />
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import FlowerCard from '../components/FlowerCard.vue'
import Pagination from '../components/Pagination.vue'
import { getFlowers } from '../api/flower'
import { getCategories } from '../api/category'

const route = useRoute()
const router = useRouter()

const flowers = ref([])
const categories = ref([])
const loading = ref(false)
const keyword = ref('')
const categoryId = ref(null)
const sort = ref('')
const page = ref(1)
const size = ref(12)
const total = ref(0)

onMounted(async () => {
  categories.value = await getCategories()
  // 读取 URL 参数
  if (route.query.keyword) keyword.value = route.query.keyword
  if (route.query.categoryId) categoryId.value = Number(route.query.categoryId)
  if (route.query.sort) sort.value = route.query.sort
  fetchData()
})

async function fetchData() {
  loading.value = true
  try {
    const data = await getFlowers({
      keyword: keyword.value || undefined,
      categoryId: categoryId.value || undefined,
      sort: sort.value || undefined,
      page: page.value,
      size: size.value,
    })
    flowers.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

function search() { page.value = 1; fetchData() }
function onPageChange(p) { page.value = p; fetchData(); window.scrollTo(0, 0) }
function onSizeChange(s) { size.value = s; page.value = 1; fetchData() }
</script>

<style scoped>
.filter-bar {
  display: flex; gap: 12px; margin-bottom: 24px; flex-wrap: wrap;
}
.search-input { width: 280px; }
.filter-select { width: 180px; }
.flower-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
@media (max-width: 900px) { .flower-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
