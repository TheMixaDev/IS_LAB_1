<template>
	<div class="music-map-container">
		<h1 class="map-title">Карта музыкальных групп</h1>
		<MusicBandMap :music-bands="musicBands" :is-loading="isLoading" :current-user="user" />
	</div>
</template>

<script>
import NetworkService from '../services/NetworkService.js';
import MusicBandMap from '../components/MusicBandMap.vue';
import { mapState } from 'vuex';

export default {
	name: 'MusicBandMapView',
	components: {
		MusicBandMap
	},
	data() {
		return {
			musicBands: [],
			isLoading: true
		};
	},
	computed: {
		...mapState(['user'])
	},
	mounted() {
		this.fetchMusicBands();
		this.$eventBus.on('musicBandEvent', (payload) => {
			if (payload.eventType === 'CREATE' || payload.eventType === 'UPDATE' || payload.eventType === 'DELETE') {
				this.fetchMusicBands();
			}
		});
	},
	beforeUnmount() {
		this.$eventBus.off('musicBandEvent');
	},
	methods: {
		async fetchMusicBands() {
			this.isLoading = true;
			try {
				const response = await NetworkService.get('/music-bands');
				this.musicBands = response.data;
			} catch (error) {
				console.error('Ошибка при получении данных:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить данные' });
			} finally {
				this.isLoading = false;
			}
		}
	}
};
</script>

<style scoped>
.music-map-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 100%;
	height: 100vh;
}

.map-title {
	font-size: 2em;
	margin-bottom: 20px;
	color: #ffffff;
}
</style>
