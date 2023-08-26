import {createRouter, createWebHistory} from 'vue-router';
import TheLogin from '@/pages/TheLogin.vue';
import TheDashboard from '@/pages/TheDashboard.vue';

const routes = [
    {path: '/login', component: TheLogin, meta: {guestOnly: true}},
    {path: '/dashboard', component: TheDashboard, meta: {requiresAuth: true}},
    {path: '/', redirect: '/login'},
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

function isAuthenticated() {
    const accessToken = localStorage.getItem('access_token');
    return !!accessToken;
}

router.beforeEach((to, from, next) => {

    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!isAuthenticated()) {
            next('/login');
        } else {
            next();
        }
    } else if (to.matched.some(record => record.meta.guestOnly)) {
        if (isAuthenticated()) {
            next('/dashboard');
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
