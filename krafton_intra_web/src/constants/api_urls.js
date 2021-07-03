const API_URL = 'http://localhost:8888'

const GET_PTO_INFO = API_URL+'/pto/info/id/{id}'
const GET_PTO_TYPE = API_URL+'/pto/types'
const POST_PTO = API_URL+'/pto'

export const API_URLS = {
  'GET_PTO_INFO': GET_PTO_INFO,
  'GET_PTO_TYPE': GET_PTO_TYPE,
  'POST_PTO': POST_PTO,
}

export default API_URLS
