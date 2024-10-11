<template>
	<div v-if="isLoading">Загрузка запросов...</div>
	<div v-else-if="isAdmin">
		<h2>Запросы на повышение роли</h2>
		<table v-if="requests.length">
			<thead>
				<tr>
					<th>ID</th>
					<th>Пользователь</th>
					<th>Статус</th>
					<th>Действия</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="request in requests" :key="request.id">
					<td>{{ request.id }}</td>
					<td>{{ request.user.username }}</td>
					<td>{{ request.status }}</td>
					<td>
						<button @click="approveRequest(request.id)" :disabled="request.status !== 'PENDING'">Одобрить</button>
						<button @click="rejectRequest(request.id)" :disabled="request.status !== 'PENDING'">Отклонить</button>
					</td>
				</tr>
			</tbody>
		</table>
		<p v-else>Нет новых запросов.</p>
	</div>
	<div v-else>У вас нет прав доступа к этой странице.</div>
</template>

<script>
import NetworkService from '../services/NetworkService.js';
import { mapState } from 'vuex';

export default {
	name: 'AdminRequestsView',
	data() {
		return {
			requests: [],
			isLoading: true
		};
	},
	computed: {
		...mapState(['user']),
		isAdmin() {
			return this.user && this.user.role === 'ADMIN';
		}
	},
	mounted() {
		if (this.isAdmin) {
			this.fetchRequests();
		}
	},
	methods: {
		async fetchRequests() {
			this.isLoading = true;
			try {
				const response = await NetworkService.get('/admin-requests');
				this.requests = response.data;
			} catch (error) {
				console.error('Ошибка при получении запросов:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить запросы' });
			} finally {
				this.isLoading = false;
			}
		},
		async approveRequest(requestId) {
			try {
				await NetworkService.put(`/admin-requests/${requestId}?approved=true`);
				this.fetchRequests();
			} catch (error) {
				console.error('Ошибка при одобрении запроса:', error);
				this.$notify({ type: 'error', text: 'Не удалось одобрить запрос' });
			}
		},
		async rejectRequest(requestId) {
			try {
				await NetworkService.put(`/admin-requests/${requestId}?approved=false`);
				this.fetchRequests();
			} catch (error) {
				console.error('Ошибка при отклонении запроса:', error);
				this.$notify({ type: 'error', text: 'Не удалось отклонить запрос' });
			}
		}
	}
};
</script>
