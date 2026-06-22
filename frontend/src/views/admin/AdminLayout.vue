<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <div class="admin-sidebar">
      <h2 class="sidebar-title">🌸 管理后台</h2>
      <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#e74c8b">
        <el-menu-item index="/admin/dashboard"><el-icon><DataAnalysis /></el-icon> 仪表盘</el-menu-item>
        <el-menu-item index="/admin/flowers"><el-icon><Flower /></el-icon> 花卉管理</el-menu-item>
        <el-menu-item index="/admin/categories"><el-icon><Menu /></el-icon> 分类管理</el-menu-item>
        <el-menu-item index="/admin/orders"><el-icon><Tickets /></el-icon> 订单管理</el-menu-item>
        <el-menu-item index="/admin/users"><el-icon><UserFilled /></el-icon> 用户管理</el-menu-item>
        <el-menu-item index="/admin/coupons"><el-icon><Discount /></el-icon> 优惠券管理</el-menu-item>
        <el-menu-item index="/admin/reviews"><el-icon><ChatDotSquare /></el-icon> 评价管理</el-menu-item>
        <el-menu-item><a href="/" style="color:inherit;text-decoration:none;display:block"><el-icon><HomeFilled /></el-icon> 返回前台</a></el-menu-item>
      </el-menu>
    </div>
    <!-- 内容区 -->
    <div class="admin-main">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
const route = useRoute()
</script>

<style scoped>
.admin-layout { display: flex; min-height: 100vh; }
.admin-sidebar { width: 220px; background: #304156; flex-shrink: 0; }
.sidebar-title { color: #fff; text-align: center; padding: 20px 0; font-size: 18px; border-bottom: 1px solid #4a5064; }
.admin-main { flex: 1; padding: 20px; overflow-y: auto; }
</style>
