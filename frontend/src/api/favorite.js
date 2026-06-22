import request from './request'

export function getFavorites() {
  return request.get('/favorites')
}

export function addFavorite(flowerId) {
  return request.post('/favorites', { flowerId })
}

export function removeFavorite(flowerId) {
  return request.delete(`/favorites/${flowerId}`)
}

export function checkFavorite(flowerId) {
  return request.get(`/favorites/check/${flowerId}`)
}
