<template>
	<form @submit.prevent="createAlbum" class="creation-form">
		<div class="form-group">
			<label for="albumName" class="form-label">Название:</label>
			<input type="text" id="albumName" v-model="album.name" required class="form-input" />
		</div>
		<div class="form-group">
			<label for="length" class="form-label">Длина:</label>
			<input type="number" id="length" v-model.number="album.length" min="1" required class="form-input" />
		</div>
		<div class="form-group">
			<label for="sales" class="form-label">Продажи:</label>
			<input type="number" id="sales" v-model.number="album.sales" min="0" required class="form-input" />
		</div>
		<div class="centered">
			<button type="submit" class="submit-button standart-width spaced-top">Создать</button>
		</div>
	</form>
</template>

<script>
import NetworkService from '@/services/NetworkService.js';

export default {
	name: 'CreateAlbumForm',
	emits: ['album-created'],
	data() {
		return {
			album: {
				name: '',
				length: 0,
				sales: 0
			}
		};
	},
	methods: {
		async createAlbum() {
			try {
				const response = await NetworkService.post('/albums', this.album);
				this.$emit('album-created', response.data);
			} catch (error) {
				console.error('Ошибка при создании альбома:', error);
				this.$notify({ type: 'error', text: 'Не удалось создать альбом' });
			}
		}
	}
};
</script>
