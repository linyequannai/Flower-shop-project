import request from './request'

export function getCartList() {
  return request.get('/cart')
}

export function addToCart(flowerId, quantity = 1) {
  return request.post('/cart', { flowerId, quantity })
}

export function updateCartQuantity(id, quantity) {
  return request.put(`/cart/${id}/quantity`, { quantity })
}

export function updateCartSelected(id, selected) {
  return request.put(`/cart/${id}/select`, { selected })
}

export function selectAllCart(selected) {
  return request.put('/cart/select-all', { selected })
}

export function removeFromCart(id) {
  return request.delete(`/cart/${id}`)
}

export function clearSelectedCart() {
  return request.delete('/cart')
}
