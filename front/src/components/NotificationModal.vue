<script setup>
import CheckIcon from '@/assets/icons/CheckIcon.vue';
import CrossIcon from '@/assets/icons/CrossIcon.vue';
import ExclamationIcon from '@/assets/icons/ExclamationIcon.vue';
import CloseCrossIcon from '@/assets/icons/CloseCrossIcon.vue';
</script>
<template>
	<div
		class="notification-container"
		:class="{
			'notification-success': data.item.type === 'success',
			'notification-error': data.item.type === 'error',
			'notification-warn': data.item.type === 'warn'
		}"
		role="alert"
	>
		<div class="icon-container">
			<CheckIcon v-if="data.item.type === 'success'" />
			<CrossIcon v-if="data.item.type === 'error'" />
			<ExclamationIcon v-if="data.item.type === 'warn'" />
			<span class="sr-only">Icon</span>
		</div>
		<div class="message">
			{{ data.item.text }}
		</div>
		<button type="button" class="close-button" aria-label="Close" @click="data.close">
			<span class="sr-only">Закрыть</span>
			<CloseCrossIcon />
		</button>
	</div>
</template>

<script>
export default {
	name: 'NotificationModal',
	props: ['data']
};
</script>

<style scoped>
.notification-container {
	display: flex;
	align-items: center;
	width: 100%;
	max-width: 480px;
	padding: 16px;
	margin-bottom: 16px;
	background-color: white;
	border-radius: 8px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	color: #522881;
}

.notification-success {
	border-left: 4px solid #4caf50;
}

.notification-error {
	border-left: 4px solid #f44336;
}

.notification-warn {
	border-left: 4px solid #ff9800;
}

.icon-container {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 32px;
	height: 32px;
	border-radius: 50%;
	margin-right: 12px;
}

.message {
	font-size: 14px;
	line-height: 1.5;
}

.close-button {
	margin-left: auto;
	background: none;
	border: none;
	padding: 0;
	cursor: pointer;
}
</style>
