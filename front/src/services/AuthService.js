import NetworkService from './NetworkService.js';

export default {
	login(user) {
		return NetworkService.post('/auth/login', user, { ignoreToken: true });
	},
	register(user) {
		return NetworkService.post('/auth/register', user, { ignoreToken: true });
	},
	isAuthenticated() {
		return !!localStorage.getItem('token');
	},
	logout() {
		localStorage.removeItem('token');
	}
};
