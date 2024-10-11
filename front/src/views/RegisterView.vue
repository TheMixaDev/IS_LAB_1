<template>
	<div class="separated">
		<h1>Регистрация</h1>
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
				<button type="submit" class="large-width">Зарегистрироваться</button>
			</div>
		</form>
	</div>
</template>

<script>
import router from '@/router/index.js';
import AuthService from '../services/AuthService.js';

export default {
	name: 'RegisterView',
	data() {
		return {
			username: '',
			password: ''
		};
	},
	methods: {
		async handleSubmit() {
			try {
				const response = await AuthService.register({
					username: this.username,
					password: this.password
				});

				console.log('Успешная регистрация:', response.data);
				this.$notify({ type: 'success', text: 'Регистрация успешна' });
				router.push('/auth');
			} catch (error) {
				console.error('Ошибка при регистрации:', error);
				this.$notify({ type: 'error', text: 'Пользователь с данным именем уже существует' });
			}
		}
	}
};
</script>
