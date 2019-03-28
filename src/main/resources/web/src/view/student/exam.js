import Vue from 'vue'
import App from './exam.vue'

Vue.config.productionTip = false;

new Vue({
    render: h => h(App),
}).$mount('#app');