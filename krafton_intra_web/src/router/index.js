import Vue from 'vue'
import VueRouter from 'vue-router'
import PaidTimeOff from '../components/biz/pto/PaidTimeOff.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: PaidTimeOff
  },
  {
      path: '/pto',
      name: 'PaidTimeOff',
      component: PaidTimeOff
    },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
