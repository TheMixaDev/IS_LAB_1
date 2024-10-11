import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import mitt from 'mitt';
import Notifications from '@kyvg/vue3-notification';
import '@/assets/style.css';

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';

import { faUser, faPlus, faDoorOpen, faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
library.add(faUser, faPlus, faDoorOpen, faChevronLeft, faChevronRight);

const app = createApp(App);

app.component('font-awesome-icon', FontAwesomeIcon);
app.config.globalProperties.$eventBus = mitt();

app.use(router);
app.use(store);
app.use(Notifications);

app.mount('#app');
