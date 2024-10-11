<template>
	<div class="special-operations separated">
		<h1>Специальные операции</h1>
		<div class="operation-group">
			<div class="form-group">
				<label for="establishmentDate" class="form-label">Дата основания:</label>
				<input type="date" id="establishmentDate" v-model="establishmentDate" class="form-input" />
				<button @click="countByEstablishmentDate" class="operation-button">Подсчитать</button>
			</div>
			<div v-if="establishmentDateCount !== null" class="result">Найдено групп: {{ establishmentDateCount }}</div>
			<div v-if="establishmentDateError" class="error">{{ establishmentDateError }}</div>
		</div>

		<div class="operation-group">
			<div class="form-group">
				<label for="labelSales" class="form-label">Продажи лейбла больше чем:</label>
				<input type="number" id="labelSales" v-model.number="labelSales" class="form-input" />
				<button @click="countByLabelSales" class="operation-button">Подсчитать</button>
			</div>
			<div v-if="labelSalesCount !== null" class="result">Найдено групп: {{ labelSalesCount }}</div>
		</div>

		<div class="operation-group">
			<div class="form-group">
				<label for="namePrefix" class="form-label">Название начинается с:</label>
				<input type="text" id="namePrefix" v-model="namePrefix" class="form-input" />
				<button @click="findByNamePrefix" class="operation-button">Найти</button>
			</div>
			<ul v-if="namePrefixMusicBands.length" class="result-list">
				<li v-for="band in namePrefixMusicBands" :key="band.id">{{ band.name }}</li>
			</ul>
		</div>

		<div class="operation-group">
			<div class="form-group">
				<label for="singleMusicBandId" class="form-label">ID группы для сингла:</label>
				<input type="number" id="singleMusicBandId" v-model.number="singleMusicBandId" class="form-input" />
				<button @click="addSingleToMusicBand" class="operation-button">Добавить</button>
			</div>
		</div>

		<div class="operation-group">
			<div class="form-group">
				<label for="rewardGenre" class="form-label">Жанр для награждения:</label>
				<select id="rewardGenre" v-model="rewardGenre" class="form-select">
					<option value="ROCK">Рок</option>
					<option value="PSYCHEDELIC_CLOUD_RAP">Психоделик-клауд-рэп</option>
					<option value="JAZZ">Джаз</option>
					<option value="PUNK_ROCK">Панк-рок</option>
					<option value="BRIT_POP">Брит-поп</option>
				</select>
				<button @click="rewardBestMusicBand" class="operation-button">Наградить</button>
			</div>
		</div>
	</div>
</template>

<script>
import NetworkService from '../services/NetworkService.js';

export default {
	name: 'SpecialOperationsView',
	data() {
		return {
			establishmentDate: null,
			establishmentDateCount: null,
			establishmentDateError: null,
			labelSales: null,
			labelSalesCount: null,
			namePrefix: '',
			namePrefixMusicBands: [],
			singleMusicBandId: null,
			rewardGenre: 'ROCK'
		};
	},
	methods: {
		async countByEstablishmentDate() {
			this.establishmentDateError = null;
			try {
				const response = await NetworkService.get(`/music-bands/count/establishment-date`, {
					params: { date: this.establishmentDate }
				});
				this.establishmentDateCount = response.data;
			} catch (error) {
				console.error('Ошибка при подсчете групп:', error);
				this.$notify({ type: 'error', text: 'Не удалось подсчитать группы' });
				if (error.response && error.response.status === 400) {
					this.establishmentDateError = 'Неверный формат даты.';
				} else {
					this.establishmentDateError = 'Произошла ошибка при подсчете групп.';
				}
			}
		},
		async countByLabelSales() {
			try {
				const response = await NetworkService.get(`/music-bands/count/label-sales`, {
					params: { sales: this.labelSales }
				});
				this.labelSalesCount = response.data;
			} catch (error) {
				this.$notify({ type: 'error', text: 'Не удалось подсчитать продажи' });
				console.error('Ошибка при подсчете групп:', error);
			}
		},
		async findByNamePrefix() {
			try {
				const response = await NetworkService.get(`/music-bands/name/${this.namePrefix}`);
				this.namePrefixMusicBands = response.data;
			} catch (error) {
				this.$notify({ type: 'error', text: 'Не удалось найти группы' });
				console.error('Ошибка при поиске групп:', error);
			}
		},
		async addSingleToMusicBand() {
			try {
				await NetworkService.post(`/music-bands/${this.singleMusicBandId}/add-single`);
				console.log('Сингл успешно добавлен');
				this.$notify({ type: 'success', text: 'Сингл добавлен' });
			} catch (error) {
				console.error('Ошибка при добавлении сингла:', error);
				if (error.response && error.response.status === 404) {
					this.$notify({ type: 'error', text: 'Группа не найдена' });
				} else {
					this.$notify({ type: 'error', text: 'Группа не найдена' });
				}
			}
		},
		async rewardBestMusicBand() {
			try {
				await NetworkService.post(`/music-bands/reward-best/${this.rewardGenre}`);
				this.$notify({ type: 'success', text: 'Группа награждена' });
			} catch (error) {
				this.$notify({ type: 'error', text: 'Группа не найдена' });
				console.error('Ошибка при награждении группы:', error);
			}
		}
	}
};
</script>

<style scoped>
.special-operations {
	padding: 20px;
}

.operation-group {
	margin-bottom: 20px;
	border: 1px solid #522881;
	padding: 15px;
	border-radius: 8px;
}

.form-group {
	display: flex;
	align-items: center;
}

.form-label {
	width: 200px;
	text-align: right;
	margin-right: 10px;
}

.form-input,
.form-select {
	flex: 1;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.operation-button {
	padding: 8px 16px;
	background-color: #554dd8;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin-left: 10px;
}

.result {
	margin-top: 10px;
	font-weight: bold;
	text-align: center;
}

.result-list {
	list-style: none;
	padding: 0;
	margin-top: 10px;
	text-align: center;
}

.result-list li {
	margin-bottom: 5px;
}

.error {
	color: red;
	margin-top: 10px;
}
</style>
