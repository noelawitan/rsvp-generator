import {createRouter, createWebHistory} from 'vue-router';
import TheLogin from '@/pages/TheLogin.vue';
import TheDashboard from '@/pages/TheDashboard.vue';
import EventDetail from "@/pages/EventDetail.vue";
import PublicInvitationDetail from "@/pages/PublicInvitationDetail.vue";
import PublicEventDetail from "@/pages/PublicEventDetail.vue";

const routes = [
    {path: '/login', component: TheLogin, meta: {guestOnly: true}},
    {path: '/dashboard', component: TheDashboard, meta: {requiresAuth: true}},
    {path: '/event-detail/:id', component: EventDetail, meta: {requiresAuth: true}},
    {path: '/public/inv/:publicId', component: PublicInvitationDetail, meta: {requiresAuth: false}},
    {path: '/public/event/:publicId', component: PublicEventDetail, meta: {requiresAuth: false}},
    {path: '/', redirect: '/login'}
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
