import {createApp} from 'vue';
import {loaderState} from "@/js/loaderstate";
import App from './App.vue';
import router from '@/js/routes.js';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap';

const app = createApp(App);

app.config.globalProperties.$loader = loaderState;

app.use(router);
app.mount('#app');