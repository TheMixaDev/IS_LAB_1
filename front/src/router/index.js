import { createRouter, createWebHistory } from 'vue-router';
import MusicBandList from '../views/MusicBandList.vue';
import MusicBandView from '../views/MusicBandView.vue';
import CreateMusicBand from '../views/CreateMusicBandView.vue';
import SpecialOperationsView from '../views/SpecialOperationsView.vue';
import MusicBandMapView from '@/views/MusicBandMapView.vue';
import AdminRequestsView from '@/views/AdminRequestsView.vue';
import AuthView from '../views/AuthView.vue';
import RegisterView from '../views/RegisterView.vue';
import store from '../store';

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: 'MusicBandList',
			component: MusicBandList
		},
		{
			path: '/music-bands/:id',
			name: 'MusicBandView',
			component: MusicBandView
		},
		{
			path: '/music-bands/create',
			name: 'CreateMusicBand',
			component: CreateMusicBand
		},
		{
			path: '/music-bands/:id/edit',
			name: 'EditMusicBand',
			component: CreateMusicBand
		},
		{
			path: '/special',
			name: 'SpecialOperations',
			component: SpecialOperationsView
		},
		{
			path: '/map',
			name: 'MusicBandMapView',
			component: MusicBandMapView
		},
		{
			path: '/admin-requests',
			name: 'AdminRequestsView',
			component: AdminRequestsView
		},
		{
			path: '/auth',
			name: 'AuthView',
			component: AuthView
		},
		{
			path: '/register',
			name: 'RegisterView',
			component: RegisterView
		}
	]
});

router.beforeEach((to, from, next) => {
	if (to.name === 'AuthView' || to.name === 'RegisterView') {
		next();
		return;
	}
	store
		.dispatch('fetchUser')
		.then(() => {
			next();
		})
		.catch(() => {
			next();
		});
});

export default router;
