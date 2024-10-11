<template>
	<div>
		<h1>Список музыкальных групп</h1>
		<div v-if="isLoading" class="loading">Загрузка данных...</div>
		<div v-else class="music-bands-list">
			<div class="table-controls flex items-center spaced-bottom">
				<input type="text" placeholder="Поиск..." v-model="searchQuery" class="search-input" />
				<select v-model="sortBy" class="select-input spaced-left">
					<option value="">Сортировка</option>
					<option value="id">ID</option>
					<option value="name">Название</option>
					<option value="creationDate">Дата создания</option>
					<option value="numberOfParticipants">Число участников</option>
					<option value="singlesCount">Количество синглов</option>
					<option value="albumsCount">Количество альбомов</option>
					<option value="establishmentDate">Дата основания</option>
				</select>
				<select v-model="sortOrder" class="select-input spaced-left">
					<option value="asc">По возрастанию</option>
					<option value="desc">По убыванию</option>
				</select>
				<button @click="router.push('/music-bands/create')" class="create-button spaced-left">
					<font-awesome-icon :icon="['fas', 'plus']" class="icon" />
					Создать группу
				</button>
			</div>

			<table class="music-band-table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Название</th>
						<th>Координаты (x, y)</th>
						<th>Дата создания</th>
						<th>Жанр</th>
						<th>Участников</th>
						<th>Синглов</th>
						<th>Описание</th>
						<th>Лучший Альбом</th>
						<th>Альбомов</th>
						<th>Дата основания</th>
						<th>Лейбл</th>
						<th>Создатель</th>
						<th>Действия</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="musicBand in filteredMusicBands" :key="musicBand.id" @click="router.push(`/music-bands/${musicBand.id}`)">
						<td>{{ musicBand.id }}</td>
						<td>{{ musicBand.name }}</td>
						<td>({{ musicBand.coordinates.x }}, {{ musicBand.coordinates.y }})</td>
						<td>{{ formatDate(musicBand.creationDate) }}</td>
						<td>{{ getGenreLabel(musicBand.genre) }}</td>
						<td>{{ musicBand.numberOfParticipants }}</td>
						<td>{{ musicBand.singlesCount }}</td>
						<td>{{ musicBand.description }}</td>
						<td>{{ musicBand.bestAlbum ? musicBand.bestAlbum.name : '-' }}</td>
						<td>{{ musicBand.albumsCount }}</td>
						<td>{{ formatDate(musicBand.establishmentDate) }}</td>
						<td>{{ musicBand.label ? musicBand.label.sales : '-' }}</td>
						<td>
							{{ musicBand.createdBy ? musicBand.createdBy.username : '-' }}
						</td>
						<td class="standart-width">
							<template v-if="canEditOrDelete(musicBand)">
								<button
									@click="
										$event.stopPropagation();
										router.push(`/music-bands/${musicBand.id}/edit`);
									"
									class="standart-width"
								>
									Редактировать
								</button>
								<button
									@click="
										$event.stopPropagation();
										showDeleteModal(musicBand.id, musicBand.name);
									"
									class="red standart-width spaced-top"
								>
									Удалить
								</button>
							</template>
						</td>
					</tr>
				</tbody>
			</table>
			<PaginationComponent :total-pages="totalPages" @page-changed="onPageChange" />
		</div>
	</div>
	<DeleteMusicBandModal v-if="showDeleteModalVisible" :music-band-id="musicBandToDeleteId" :music-band-name="musicBandToDeleteName" @close="showDeleteModalVisible = false" @music-band-deleted="fetchMusicBands" />
</template>

<script>
import NetworkService from '../services/NetworkService.js';
import PaginationComponent from '../components/PaginationComponent.vue';
import DeleteMusicBandModal from '../components/DeleteMusicBandModal.vue';
import { mapState } from 'vuex';
import router from '@/router/index.js';
import { getGenreLabel } from '@/util.js';

export default {
	name: 'MusicBandList',
	components: {
		PaginationComponent,
		DeleteMusicBandModal
	},
	data() {
		return {
			musicBands: [],
			isLoading: true,
			searchQuery: '',
			currentPage: 1,
			pageSize: 10,
			sortBy: '',
			sortOrder: 'asc',

			showDeleteModalVisible: false,
			musicBandToDeleteId: null,
			musicBandToDeleteName: null
		};
	},
	computed: {
		getGenreLabel() {
			return getGenreLabel;
		},
		filteredMusicBands() {
			let filtered = this.musicBands;
			if (this.searchQuery) {
				const query = this.searchQuery.toLowerCase();
				filtered = filtered.filter((musicBand) => {
					return Object.values(musicBand).some((value) => {
						if (typeof value === 'string') {
							return value.toLowerCase().includes(query);
						} else if (typeof value === 'object' && value !== null) {
							return Object.values(value).some((innerValue) => {
								if (typeof innerValue === 'string') {
									return innerValue.toLowerCase().includes(query);
								}
								return false;
							});
						}
						return false;
					});
				});
			}

			if (this.sortBy) {
				filtered = filtered.sort((a, b) => {
					let modifier = 1;
					if (this.sortOrder === 'desc') modifier = -1;

					if (a[this.sortBy] < b[this.sortBy]) return -1 * modifier;
					if (a[this.sortBy] > b[this.sortBy]) return 1 * modifier;
					return 0;
				});
			}
			const startIndex = (this.currentPage - 1) * this.pageSize;
			const endIndex = startIndex + this.pageSize;
			return filtered.slice(startIndex, endIndex);
		},
		totalPages() {
			return Math.ceil(this.musicBands.length / this.pageSize);
		},
		router() {
			return router;
		},
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
		},
		viewMusicBand(id) {
			this.$router.push({ name: 'MusicBandView', params: { id } });
		},
		onPageChange(page) {
			this.currentPage = page;
		},
		formatDate(dateString) {
			if (!dateString) return '-';
			const date = new Date(dateString);
			return date.toLocaleString();
		},
		showDeleteModal(id, name) {
			this.musicBandToDeleteId = id;
			this.musicBandToDeleteName = name;
			this.showDeleteModalVisible = true;
		},
		canEditOrDelete(musicBand) {
			if (!this.user) {
				return false;
			}
			return musicBand.createdBy.id === this.user.id || this.user.role === 'ADMIN';
		}
	}
};
</script>

<style scoped>
.music-bands-list {
	background-color: #26527c;
	padding: 20px;
	border-radius: 8px;
	color: white;
}

.table-controls {
	justify-content: space-between;
	align-items: center;
}

.search-input {
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	flex: 1;
}

.select-input {
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-left: 10px;
}

.create-button {
	background-color: #554dd8;
	color: white;
	padding: 8px 16px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	display: flex;
	align-items: center;
}

.create-button .icon {
	margin-right: 5px;
}

.music-band-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.music-band-table th,
.music-band-table td {
	padding: 8px;
	border: 1px solid #ccc;
	text-align: left;
}

.band-link {
	color: #554dd8;
	text-decoration: none;
}

.band-link:hover {
	text-decoration: underline;
}

.edit-link {
	color: #4caf50;
	text-decoration: none;
	margin-right: 10px;
}

.edit-link:hover {
	text-decoration: underline;
}

.delete-button {
	background-color: #f44336;
	color: white;
	border: none;
	padding: 6px 10px;
	border-radius: 4px;
	cursor: pointer;
}

.loading {
	text-align: center;
	font-size: 1.2em;
	margin-top: 20px;
}
</style>
