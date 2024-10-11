<template>
	<form @submit.prevent="sendRequest">
		<p v-if="requestSent">Запрос отправлен. Ожидайте подтверждения.</p>
		<button type="submit" v-else>Запросить повышение роли</button>
	</form>
</template>

<script>
import NetworkService from '@/services/NetworkService.js';

export default {
	name: 'RequestAdminForm',
	data() {
		return {
			requestSent: false
		};
	},
	methods: {
		async sendRequest() {
			try {
				await NetworkService.post('/admin-requests');
				this.requestSent = true;
			} catch (error) {
				this.requestSent = true;
			}
		}
	}
};
</script>
