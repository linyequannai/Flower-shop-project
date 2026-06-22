import request from './request'

export function createOrder(data) {
  return request.post('/orders', data)
}

export function getUserOrders(params) {
  return request.get('/orders', { params })
}

export function getOrderDetail(id) {
  return request.get(`/orders/${id}`)
}

export function cancelOrder(id) {
  return request.put(`/orders/${id}/cancel`)
}

export function confirmOrder(id) {
  return request.put(`/orders/${id}/confirm`)
}

export function getAdminOrders(params) {
  return request.get('/admin/orders', { params })
}

export function updateOrderStatus(id, status) {
  return request.put(`/admin/orders/${id}/status`, null, { params: { status } })
}

export function shipOrder(id) {
  return request.put(`/admin/orders/${id}/ship`)
}
