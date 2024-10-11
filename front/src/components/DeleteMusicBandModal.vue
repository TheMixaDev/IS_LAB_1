<template>
	<Modal @close="$emit('close')">
		<template v-slot:header>
			<h3>Подтверждение удаления</h3>
		</template>
		<template v-slot:body>
			<p>Вы уверены, что хотите удалить группу "{{ musicBandName }}"?</p>
			<p>Связанные с группой объекты (альбом, лейбл, координаты) будут удалены, если не связаны с другими группами.</p>
			<div class="centered">
				<button @click="deleteMusicBand" class="standart-width red">Удалить</button>
			</div>
		</template>
	</Modal>
</template>

<script>
import NetworkService from '../services/NetworkService.js';
import Modal from '../components/ModalComponent.vue';

export default {
	name: 'DeleteMusicBandModal',
	components: {
		Modal
	},
	props: {
		musicBandId: {
			type: Number,
			required: true
		},
		musicBandName: {
			type: String,
			required: true
		}
	},
	methods: {
		async deleteMusicBand() {
			try {
				await NetworkService.delete(`/music-bands/${this.musicBandId}`);
				this.$notify({ type: 'success', text: 'Группа удалена' });
				console.log('Группа успешно удалена');
				this.$emit('music-band-deleted');
				this.$emit('close');
			} catch (error) {
				this.$notify({ type: 'error', text: 'Не удалось обновить группу' });
				console.error('Ошибка при удалении группы:', error);
			}
		}
	}
};
</script>
