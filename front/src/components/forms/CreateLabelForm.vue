<template>
	<form @submit.prevent="createLabel" class="creation-form">
		<div class="form-group">
			<label for="labelSales" class="form-label">Продажи:</label>
			<input type="number" id="labelSales" v-model.number="label.sales" min="0" required class="form-input" />
		</div>
		<div class="centered">
			<button type="submit" class="submit-button standart-width spaced-top">Создать</button>
		</div>
	</form>
</template>

<script>
import NetworkService from '@/services/NetworkService.js';

export default {
	name: 'CreateLabelForm',
	emits: ['label-created'],
	data() {
		return {
			label: {
				sales: 0
			}
		};
	},
	methods: {
		async createLabel() {
			try {
				const response = await NetworkService.post('/labels', this.label);
				this.$emit('label-created', response.data);
			} catch (error) {
				console.error('Ошибка при создании лейбла:', error);
				this.$notify({ type: 'error', text: 'Не удалось создать лейбл' });
			}
		}
	}
};
</script>
