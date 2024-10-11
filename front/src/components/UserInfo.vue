<template>
	<div class="panel">
		<div v-if="currentUser" class="user-info">
			<div class="menu-buttons">
				<button class="spaced-right standart-width" @click="router.push('/')">Общий список</button>
				<button class="spaced-right standart-width" @click="router.push('/special')">Специальное</button>
				<button class="spaced-right standart-width" @click="router.push('/map')">Карта</button>

				<RequestAdminForm v-if="currentUser.role === 'USER'" />
				<button v-else @click="router.push('/admin-requests')" class="red standart-width">Админ-панель</button>
			</div>
			<font-awesome-icon :icon="['fas', 'user']" class="spaced-right no-select" />
			<span class="spaced-right no-select">{{ currentUser.username }}</span>
			<button @click="logout" class="spaced-left red standart-width">
				<font-awesome-icon :icon="['fas', 'door-open']" class="spaced-right no-select" />
				Выйти
			</button>
		</div>
		<div v-else>
			<button class="spaced-right standart-width" @click="router.push('/auth')">Войти</button>
			<button class="standart-width" @click="router.push('/register')">Регистрация</button>
		</div>
	</div>
</template>

<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import AuthService from '../services/AuthService.js';
import router from '@/router';
import RequestAdminForm from './forms/RequestAdminForm.vue';

export default {
	name: 'UserInfo',
	components: {
		RequestAdminForm
	},
	computed: {
		router() {
			return router;
		}
	},
	setup() {
		const store = useStore();
		const currentUser = computed(() => store.state.user);

		const logout = () => {
			AuthService.logout();
			store.commit('logout');
			router.push('/auth');
		};

		return { currentUser, logout };
	}
};
</script>

<style scoped>
.panel {
	padding: 10px;
	text-align: right;
	background-color: #26527c;
}
.menu-buttons {
	display: flex;
	position: absolute;
}
</style>
