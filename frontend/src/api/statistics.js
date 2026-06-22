import request from './request'

export function getDashboard() {
  return request.get('/admin/statistics/dashboard')
}

export function getSalesTrend(period = 'month') {
  return request.get('/admin/statistics/sales-trend', { params: { period } })
}

export function getCategorySales() {
  return request.get('/admin/statistics/category-sales')
}

export function getTopFlowers(limit = 10) {
  return request.get('/admin/statistics/top-flowers', { params: { limit } })
}
