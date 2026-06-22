import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'
import { ElMessage } from 'element-plus'

const routes = [
  // ── 公开页面 ──
  { path: '/', name: 'Home', component: () => import('../views/HomeView.vue') },
  { path: '/flowers', name: 'FlowerList', component: () => import('../views/FlowerListView.vue') },
  { path: '/flowers/:id', name: 'FlowerDetail', component: () => import('../views/FlowerDetailView.vue') },
  {
    path: '/login', name: 'Login', component: () => import('../views/LoginView.vue'),
    meta: { guestOnly: true }
  },
  {
    path: '/register', name: 'Register', component: () => import('../views/RegisterView.vue'),
    meta: { guestOnly: true }
  },

  // ── 用户页面 (需登录) ──
  { path: '/cart', name: 'Cart', component: () => import('../views/CartView.vue'), meta: { auth: true } },
  { path: '/checkout', name: 'Checkout', component: () => import('../views/CheckoutView.vue'), meta: { auth: true } },
  { path: '/orders', name: 'Orders', component: () => import('../views/OrderListView.vue'), meta: { auth: true } },
  { path: '/orders/:id', name: 'OrderDetail', component: () => import('../views/OrderDetailView.vue'), meta: { auth: true } },
  { path: '/profile', name: 'Profile', component: () => import('../views/ProfileView.vue'), meta: { auth: true } },
  { path: '/favorites', name: 'Favorites', component: () => import('../views/FavoritesView.vue'), meta: { auth: true } },
  { path: '/coupons', name: 'MyCoupons', component: () => import('../views/MyCouponsView.vue'), meta: { auth: true } },
  { path: '/reviews', name: 'MyReviews', component: () => import('../views/MyReviewsView.vue'), meta: { auth: true } },

  // ── 管理端 ──
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { auth: true, role: 'admin' },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/admin/DashboardView.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/admin/UserListView.vue') },
      { path: 'categories', name: 'AdminCategories', component: () => import('../views/admin/CategoryListView.vue') },
      { path: 'flowers', name: 'AdminFlowers', component: () => import('../views/admin/FlowerListView.vue') },
      { path: 'flowers/add', name: 'FlowerAdd', component: () => import('../views/admin/FlowerFormView.vue') },
      { path: 'flowers/:id/edit', name: 'FlowerEdit', component: () => import('../views/admin/FlowerFormView.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderListView.vue') },
      { path: 'orders/:id', name: 'AdminOrderDetail', component: () => import('../views/admin/OrderDetailView.vue') },
      { path: 'coupons', name: 'AdminCoupons', component: () => import('../views/admin/CouponListView.vue') },
      { path: 'reviews', name: 'AdminReviews', component: () => import('../views/admin/ReviewListView.vue') },
    ]
  },

  // 404
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: { template: '<div class="page-container" style="text-align:center;padding-top:100px"><h1>404</h1><p>页面不存在</p></div>' } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

// 导航守卫
router.beforeEach(async (to, from, next) => {
  const token = getToken()

  // 公开页面直接放行
  if (!to.meta.auth) {
    if (to.meta.guestOnly && token) {
      return next('/')
    }
    return next()
  }

  // 需要登录
  if (!token) {
    ElMessage.warning('请先登录')
    return next('/login')
  }

  // 检查管理员权限
  if (to.meta.role === 'admin') {
    // 简单从 token payload 中判断 (也可以请求后端)
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      if (payload.role !== 'ADMIN') {
        ElMessage.error('权限不足')
        return next('/')
      }
    } catch {
      return next('/login')
    }
  }

  next()
})

export default router
