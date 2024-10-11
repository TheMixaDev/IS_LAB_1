<template>
	<div v-if="isLoading" class="loading">Загрузка...</div>
	<div v-else-if="musicBand" class="music-band-details separated">
		<h1 class="band-name">{{ musicBand.name }}</h1>
		<div class="band-info">
			<p><span class="info-label">ID:</span> {{ musicBand.id }}</p>
			<p><span class="info-label">Координаты:</span> ({{ musicBand.coordinates.x }}, {{ musicBand.coordinates.y }})</p>
			<p><span class="info-label">Дата создания:</span> {{ formatDate(musicBand.creationDate) }}</p>
			<p><span class="info-label">Жанр:</span> {{ getGenreLabel(musicBand.genre) }}</p>
			<p>
				<span class="info-label">Количество участников:</span>
				{{ musicBand.numberOfParticipants }}
			</p>
			<p>
				<span class="info-label">Количество синглов:</span>
				{{ musicBand.singlesCount }}
			</p>
			<p><span class="info-label">Описание:</span> {{ musicBand.description }}</p>
		</div>
		<div v-if="musicBand.bestAlbum" class="best-album">
			<h3>Лучший альбом: {{ musicBand.bestAlbum.name }}</h3>
			<p><span class="info-label">Длина:</span> {{ musicBand.bestAlbum.length }}</p>
			<p><span class="info-label">Продажи:</span> {{ musicBand.bestAlbum.sales }}</p>
		</div>
		<div class="band-info">
			<p><span class="info-label">Количество альбомов:</span> {{ musicBand.albumsCount }}</p>
			<p>
				<span class="info-label">Дата основания:</span>
				{{ formatDate(musicBand.establishmentDate) }}
			</p>
			<p v-if="musicBand.label"><span class="info-label">Лейбл:</span> {{ musicBand.label.sales }}</p>
			<p>
				<span class="info-label">Создатель:</span>
				{{ musicBand.createdBy ? musicBand.createdBy.username : '-' }}
			</p>
		</div>
	</div>
	<div v-else class="not-found">Группа не найдена.</div>
</template>

<script>
import NetworkService from '../services/NetworkService.js';
import { getGenreLabel } from '@/util.js';

export default {
	name: 'MusicBandView',
	data() {
		return {
			musicBand: null,
			isLoading: true
		};
	},
	mounted() {
		this.fetchMusicBand();
		this.$eventBus.on('musicBandEvent', (payload) => {
			if (payload.eventType === 'CREATE' || payload.eventType === 'UPDATE' || payload.eventType === 'DELETE') {
				this.fetchMusicBand();
			}
		});
	},
	beforeUnmount() {
		this.$eventBus.off('musicBandEvent');
	},
	computed: {
		getGenreLabel() {
			return getGenreLabel;
		}
	},
	methods: {
		async fetchMusicBand() {
			this.isLoading = true;
			try {
				const response = await NetworkService.get(`/music-bands/${this.$route.params.id}`);
				this.musicBand = response.data;
			} catch (error) {
				console.error('Ошибка при получении данных:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить данные' });
				this.musicBand = null;
			} finally {
				this.isLoading = false;
			}
		},
		formatDate(dateString) {
			if (!dateString) return '-';
			const date = new Date(dateString);
			return date.toLocaleString();
		}
	}
};
</script>
