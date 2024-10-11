import axios from 'axios';
import config from '../config.js';
import router from '../router';

const axiosInstance = axios.create({
	baseURL: config.apiUrl
});

axiosInstance.interceptors.request.use(
	(config) => {
		const token = localStorage.getItem('token');

		if (token && !config.ignoreToken) {
			config.headers.Authorization = `Bearer ${token}`;
		}
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
);

axiosInstance.interceptors.response.use(
	(response) => {
		return response;
	},
	(error) => {
		if ((error.response.status === 401 || error.response.status === 403) && !error.config.ignoreToken) {
			console.log('Ошибка авторизации. Выход из системы...');
			localStorage.removeItem('token');
			router.push('/auth');
		}
		return Promise.reject(error);
	}
);

export default axiosInstance;
