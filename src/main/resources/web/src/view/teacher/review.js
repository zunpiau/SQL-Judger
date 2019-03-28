import Vue from 'vue'
import App from './review.vue'

Vue.config.productionTip = false;

new Vue({
    render: h => h(App),
}).$mount('#app');