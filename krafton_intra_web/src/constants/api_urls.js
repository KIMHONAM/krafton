const API_URL = process.env.VUE_APP_API_URL

const GET_PTO_INFO = API_URL+'/pto/info/id/{id}'
const GET_PTO_TYPE = API_URL+'/pto/types'
const POST_PTO = API_URL+'/pto'
const GET_CANCELLABLE_PTOS = API_URL+'/pto/cancel/id/{id}'
const PUT_CANCEL_PTO = API_URL+'/pto'
const POST_PTO_HISTORIES = API_URL+'/pto/list/id/{id}'
const GET_CALENDAR_INFO = API_URL+'/pto/calendar/department/{deptCode}/start/{start}/end/{end}'

export const API_URLS = {
  'GET_PTO_INFO': GET_PTO_INFO,
  'GET_PTO_TYPE': GET_PTO_TYPE,
  'POST_PTO': POST_PTO,
  'GET_CANCELLABLE_PTOS': GET_CANCELLABLE_PTOS,
  'PUT_CANCEL_PTO': PUT_CANCEL_PTO,
  'POST_PTO_HISTORIES': POST_PTO_HISTORIES,
  'GET_CALENDAR_INFO': GET_CALENDAR_INFO,
}

export default API_URLS
