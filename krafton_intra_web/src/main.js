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

Vue.config.productionTip = false
Vue.prototype.$apiUrls = API_URLS;
Vue.use(VueAxios, axios)

new Vue({
  router,
  store,
  vuetify,
  axios,
  API_URLS,
  VueAxios,
  render: h => h(App)
}).$mount('#app')
