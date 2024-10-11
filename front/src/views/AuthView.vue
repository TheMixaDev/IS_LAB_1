<template>
	<div class="separated">
		<h1>Авторизация</h1>
		<form @submit.prevent="handleSubmit" class="flex flex-column">
			<div class="form-group spaced-bottom">
				<label for="username" class="form-label">Имя пользователя:</label>
				<input type="text" id="username" v-model="username" required class="form-input" />
			</div>

			<div class="form-group spaced-bottom">
				<label for="password" class="form-label">Пароль:</label>
				<input type="password" id="password" v-model="password" required class="form-input" />
			</div>

			<div class="centered spaced-top">
				<button type="submit" class="large-width">Войти</button>
			</div>
		</form>
	</div>
</template>

<script>
import router from '@/router/index.js';
import AuthService from '../services/AuthService.js';

export default {
	name: 'LoginView',
	data() {
		return {
			username: '',
			password: ''
		};
	},
	methods: {
		async handleSubmit() {
			try {
				const response = await AuthService.login({
					username: this.username,
					password: this.password
				});
				this.$notify({ type: 'success', text: 'Успешный вход' });
				console.log('Успешный вход:', response.data);
				localStorage.setItem('token', response.data.token);
				router.push('/');
			} catch (error) {
				console.error('Ошибка при входе:', error);
				this.$notify({ type: 'error', text: 'Неверный логин или пароль' });
			}
		}
	}
};
</script>
