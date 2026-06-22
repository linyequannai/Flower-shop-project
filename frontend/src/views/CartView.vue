<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <h1 class="page-title">购物车</h1>
      <div v-if="cart.items.length > 0">
        <el-table :data="cart.items" style="width:100%">
          <el-table-column label="商品" width="60">
            <template #default="{ row }">
              <el-checkbox :model-value="row.selected === 1" @change="toggleSelect(row)" />
            </template>
          </el-table-column>
          <el-table-column label="商品信息" width="400">
            <template #default="{ row }">
              <div style="display:flex;align-items:center;gap:12px">
                <img :src="row.flowerImage" style="width:60px;height:60px;object-fit:cover;border-radius:4px" @error="e => e.target.src='data:image/svg+xml,' + encodeURIComponent('<svg xmlns=%22http://www.w3.org/2000/svg%22 width=%22100%22 height=%22100%22><rect fill=%22%23fce4ec%22 width=%22100%22 height=%22100%22/><text fill=%22%23e74c8b%22 font-size=%2230%22 text-anchor=%22middle%22 dy=%22.35em%22 x=%2250%22 y=%2250%22>🌸</text></svg>')" />
                <span>{{ row.flowerName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template #default="{ row }">¥{{ row.price }}</template>
          </el-table-column>
          <el-table-column label="数量" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.quantity" :min="1" :max="row.stock" size="small"
                               @change="onQuantityChange(row)" />
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120">
            <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button type="danger" size="small" @click="onRemove(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align:right;padding:20px 0">
          <span style="font-size:18px">合计：<span style="color:#e74c8b;font-weight:bold">¥{{ cart.totalPrice.toFixed(2) }}</span></span>
          <el-button type="primary" size="large" style="margin-left:20px" @click="$router.push('/checkout')"
                     :disabled="cart.selectedItems.length === 0">去结算 ({{ cart.selectedItems.length }})</el-button>
        </div>
      </div>
      <el-empty v-else description="购物车是空的">
        <el-button type="primary" @click="$router.push('/flowers')">去逛逛</el-button>
      </el-empty>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { useCartStore } from '../stores/cart'
import { updateCartSelected } from '../api/cart'

const cart = useCartStore()

onMounted(() => cart.fetchCart())

async function toggleSelect(row) {
  const newVal = row.selected === 1 ? 0 : 1
  await updateCartSelected(row.id, newVal)
  await cart.fetchCart()
}

async function onQuantityChange(row) {
  await cart.updateQuantity(row.id, row.quantity)
}

async function onRemove(row) {
  await cart.remove(row.id)
}
</script>
