import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import axios from 'axios'
import VueAxios from 'vue-axios'
import '@babel/polyfill'
import '@mdi/font/css/materialdesignicons.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import API_URLS from './constants/api_urls.js'
import holiday from '@/constants/holiday'

Vue.config.productionTip = false
Vue.prototype.$apiUrls = API_URLS;
Vue.prototype.$holiday = holiday;
Vue.use(VueAxios, axios)
Vue.use(require('vue-moment'))


new Vue({
  router,
  store,
  vuetify,
  axios,
  API_URLS,
  VueAxios,
  render: h => h(App)
}).$mount('#app')
