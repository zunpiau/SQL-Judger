import Vue from 'vue'
import App from './student.vue'

Vue.config.productionTip = false;

new Vue({
    render: h => h(App),
}).$mount('#app');