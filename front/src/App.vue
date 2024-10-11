<script setup>
import { RouterView } from 'vue-router';
import NotificationModal from './components/NotificationModal.vue';
</script>

<template>
	<UserInfo />
	<div class="main-content">
		<RouterView />
	</div>
	<notifications position="bottom right" style="padding: 6px">
		<template #body="props">
			<NotificationModal :data="props" />
		</template>
	</notifications>
</template>

<script>
import UserInfo from './components/UserInfo.vue';
import * as WebSocketClient from 'websocket';
import config from './config.js';

export default {
	components: {
		UserInfo
	},
	data() {
		return {
			webSocket: null
		};
	},
	mounted() {
		this.connectToWebSocket();
	},
	beforeUnmount() {
		this.disconnectFromWebSocket();
	},
	methods: {
		connectToWebSocket() {
			this.webSocket = new WebSocketClient.w3cwebsocket(config.websocketUrl);

			this.webSocket.onopen = () => {
				console.log('WebSocket connection opened');
			};

			this.webSocket.onmessage = (event) => {
				this.handleWebSocketMessage(event.data);
			};

			this.webSocket.onerror = (error) => {
				console.error('WebSocket connection error:', error);
			};

			this.webSocket.onclose = () => {
				console.log('WebSocket connection closed');
			};
		},
		disconnectFromWebSocket() {
			if (this.webSocket !== null) {
				this.webSocket.close();
				this.webSocket = null;
			}
		},
		handleWebSocketMessage(message) {
			console.log(message);
			const payload = JSON.parse(message);
			setTimeout(() => this.$eventBus.emit('musicBandEvent', payload), 200);
		}
	}
};
</script>
