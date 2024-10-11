import { createStore } from 'vuex';
import NetworkService from '@/services/NetworkService';

export default createStore({
	state: {
		user: null
	},
	mutations: {
		setUser(state, user) {
			state.user = user;
		},
		logout(state) {
			state.user = null;
		}
	},
	actions: {
		async fetchUser({ commit }) {
			try {
				const response = await NetworkService.get('/user');
				commit('setUser', response.data, { root: true });
			} catch (error) {
				console.log('Ошибка при получении данных пользователя:', error);
				commit('setUser', null, { root: true });
			}
		}
	},
	modules: {}
});
