<template>
	<form @submit.prevent="createCoordinates" class="creation-form">
		<div class="form-group">
			<label for="x" class="form-label">X:</label>
			<input type="number" id="x" v-model.number="coordinates.x" min="-882" required class="form-input" />
		</div>
		<div class="form-group">
			<label for="y" class="form-label">Y:</label>
			<input type="number" id="y" v-model.number="coordinates.y" required class="form-input" />
		</div>
		<div class="centered">
			<button type="submit" class="submit-button standart-width spaced-top">Создать</button>
		</div>
	</form>
</template>

<script>
import NetworkService from '@/services/NetworkService.js';

export default {
	name: 'CreateCoordinatesForm',
	emits: ['coordinates-created'],
	data() {
		return {
			coordinates: {
				x: 0,
				y: 0
			}
		};
	},
	methods: {
		async createCoordinates() {
			try {
				const response = await NetworkService.post('/coordinates', this.coordinates);
				this.$emit('coordinates-created', response.data);
			} catch (error) {
				console.error('Ошибка при создании координат:', error);
				this.$notify({ type: 'error', text: 'Не удалось создать координаты' });
			}
		}
	}
};
</script>
