import request from './request'

export function getAvailableCoupons() {
  return request.get('/coupons/available')
}

export function getMyCoupons(status) {
  return request.get('/coupons/mine', { params: { status } })
}

export function claimCoupon(id) {
  return request.post(`/coupons/${id}/claim`)
}

export function getAdminCoupons(page = 1, size = 10) {
  return request.get('/admin/coupons', { params: { page, size } })
}

export function createCoupon(data) {
  return request.post('/admin/coupons', data)
}

export function updateCoupon(id, data) {
  return request.put(`/admin/coupons/${id}`, data)
}

export function deleteCoupon(id) {
  return request.delete(`/admin/coupons/${id}`)
}
