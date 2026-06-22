<template>
  <header class="app-header">
    <div class="header-inner">
      <router-link to="/" class="logo">🌸 FlowerShop</router-link>
      <div class="header-right">
        <template v-if="auth.user">
          <router-link to="/cart" class="cart-link">
            <el-icon><ShoppingCart /></el-icon>
            <span v-if="cart.totalCount > 0" class="cart-badge">{{ cart.totalCount }}</span>
          </router-link>
          <el-dropdown @command="handleCommand">
            <span class="user-name">{{ auth.user.nickname || auth.user.username }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                <el-dropdown-item command="favorites">我的收藏</el-dropdown-item>
                <el-dropdown-item command="coupons">我的优惠券</el-dropdown-item>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item v-if="auth.isAdmin()" command="admin" divided>管理后台</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">登录</router-link>
          <router-link to="/register" class="nav-link">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const auth = useAuthStore()
const cart = useCartStore()

onMounted(() => {
  auth.fetchUser()
  if (auth.token) cart.fetchCart()
})

function handleCommand(cmd) {
  if (cmd === 'logout') {
    auth.logout()
    cart.clear()
    router.push('/')
  } else {
    router.push('/' + cmd)
  }
}
</script>

<style scoped>
.app-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}
.logo {
  font-size: 22px;
  font-weight: bold;
  color: #e74c8b;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
.cart-link {
  position: relative;
  font-size: 22px;
  color: #333;
}
.cart-badge {
  position: absolute;
  top: -8px;
  right: -12px;
  background: #e74c8b;
  color: #fff;
  font-size: 12px;
  border-radius: 10px;
  padding: 0 5px;
  min-width: 18px;
  text-align: center;
  line-height: 18px;
}
.user-name { cursor: pointer; color: #333; }
.nav-link { color: #333; font-size: 14px; }
.nav-link:hover { color: #e74c8b; }
</style>
