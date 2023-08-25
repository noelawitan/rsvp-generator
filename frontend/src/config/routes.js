import { createRouter, createWebHistory } from 'vue-router';
import TheLogin from '@/pages/TheLogin.vue';
import TheDashboard from '@/pages/TheDashboard.vue';

const routes = [
    { path: '/login', component: TheLogin },
    { path: '/dashboard', component: TheDashboard, meta: { requiresAuth: true } }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

function isAuthenticated() {
    const accessToken = localStorage.getItem('access_token');

    if (accessToken) {
        //TODO: Add server side check to be sure that access_token is still valid.
        return true;
    }

    return false;
}


router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated()) {
        next('/login');
    } else {
        next();
    }
});

export default router;
