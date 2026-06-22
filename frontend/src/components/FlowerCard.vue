<template>
  <router-link :to="'/flowers/' + flower.id" class="flower-card">
    <div class="card-img">
      <img :src="flower.coverImage || '/placeholder.jpg'" :alt="flower.name" />
    </div>
    <div class="card-info">
      <h3 class="card-name">{{ flower.name }}</h3>
      <p class="card-category">{{ flower.categoryName }}</p>
      <div class="card-price">
        <span class="price">¥{{ flower.price }}</span>
        <span v-if="flower.originalPrice && flower.originalPrice > flower.price" class="original-price">
          ¥{{ flower.originalPrice }}
        </span>
      </div>
      <span class="card-sales">已售 {{ flower.sales }}</span>
    </div>
  </router-link>
</template>

<script setup>
defineProps({
  flower: { type: Object, required: true }
})
</script>

<style scoped>
.flower-card {
  display: block;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}
.flower-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
}
.card-img {
  width: 100%;
  height: 220px;
  overflow: hidden;
  background: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.card-info { padding: 12px 14px; }
.card-name { font-size: 15px; font-weight: 600; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-category { font-size: 12px; color: #999; margin-bottom: 8px; }
.card-price { display: flex; align-items: baseline; gap: 6px; margin-bottom: 4px; }
.price { color: #e74c8b; font-size: 18px; font-weight: bold; }
.original-price { color: #ccc; font-size: 13px; text-decoration: line-through; }
.card-sales { font-size: 12px; color: #999; }
</style>
