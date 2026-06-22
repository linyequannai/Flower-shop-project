import request from './request'

export function getReviews(flowerId, page = 1, size = 10) {
  return request.get('/reviews', { params: { flowerId, page, size } })
}

export function createReview(data) {
  return request.post('/reviews', data)
}

export function getMyReviews() {
  return request.get('/reviews/mine')
}

export function deleteReview(id) {
  return request.delete(`/reviews/${id}`)
}

export function adminDeleteReview(id) {
  return request.delete(`/admin/reviews/${id}`)
}
