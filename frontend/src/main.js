import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

// Import Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// Tạo app
const app = createApp(App);

// Cấu hình router và axios
app.use(router);
app.config.globalProperties.$axios = axios;

// Sử dụng Element Plus
app.use(ElementPlus);

// Mount app
app.mount('#app');
