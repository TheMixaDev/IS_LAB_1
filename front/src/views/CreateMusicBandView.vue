<template>
	<div class="separated">
		<h1>{{ isEditing ? 'Редактирование группы' : 'Создание группы' }}</h1>
		<form @submit.prevent="createMusicBand" class="flex flex-column">
			<div class="form-group spaced-bottom">
				<label for="name" class="form-label">Название:</label>
				<input type="text" id="name" v-model="musicBand.name" required class="form-input" />
			</div>

			<div class="form-group spaced-bottom flex items-center">
				<label for="coordinates" class="form-label spaced-right">Координаты:</label>
				<select id="coordinates" v-model="musicBand.coordinatesId" required class="form-select">
					<option v-for="coordinate in coordinates" :key="coordinate.id" :value="coordinate.id">({{ coordinate.x }}, {{ coordinate.y }})</option>
				</select>
				<button type="button" @click="showCreateCoordinatesModal = true" class="add-button">+</button>
			</div>

			<div class="form-group spaced-bottom">
				<label for="genre" class="form-label">Жанр:</label>
				<select id="genre" v-model="musicBand.genre" required class="form-select">
					<option value="ROCK">Рок</option>
					<option value="PSYCHEDELIC_CLOUD_RAP">Психоделик-клауд-рэп</option>
					<option value="JAZZ">Джаз</option>
					<option value="PUNK_ROCK">Панк-рок</option>
					<option value="BRIT_POP">Брит-поп</option>
				</select>
			</div>

			<div class="form-group spaced-bottom">
				<label for="numberOfParticipants" class="form-label">Количество участников:</label>
				<input type="number" id="numberOfParticipants" v-model.number="musicBand.numberOfParticipants" min="1" required class="form-input" />
			</div>

			<div class="form-group spaced-bottom">
				<label for="singlesCount" class="form-label">Количество синглов:</label>
				<input type="number" id="singlesCount" v-model.number="musicBand.singlesCount" min="0" class="form-input" />
			</div>

			<div class="form-group spaced-bottom">
				<label for="description" class="form-label">Описание:</label>
				<textarea id="description" v-model="musicBand.description" required class="form-textarea"></textarea>
			</div>

			<div class="form-group spaced-bottom flex items-center">
				<label for="bestAlbum" class="form-label spaced-right">Лучший альбом:</label>
				<select id="bestAlbum" v-model="musicBand.bestAlbumId" class="form-select">
					<option v-for="album in albums" :key="album.id" :value="album.id">
						{{ album.name }}
					</option>
				</select>
				<button type="button" @click="showCreateAlbumModal = true" class="add-button">+</button>
			</div>

			<div class="form-group spaced-bottom">
				<label for="albumsCount" class="form-label">Количество альбомов:</label>
				<input type="number" id="albumsCount" v-model.number="musicBand.albumsCount" min="0" required class="form-input" />
			</div>

			<div class="form-group spaced-bottom">
				<label for="establishmentDate" class="form-label">Дата основания:</label>
				<input type="date" id="establishmentDate" v-model="musicBand.establishmentDate" required class="form-input" />
			</div>

			<div class="form-group spaced-bottom flex items-center">
				<label for="label" class="form-label spaced-right">Лейбл:</label>
				<select id="label" v-model="musicBand.labelId" class="form-select">
					<option v-for="label in labels" :key="label.id" :value="label.id">
						{{ label.sales }}
					</option>
				</select>
				<button type="button" @click="showCreateLabelModal = true" class="add-button">+</button>
			</div>

			<div class="centered">
				<button type="submit" class="standart-width">
					{{ isEditing ? 'Сохранить' : 'Создать' }}
				</button>
			</div>
		</form>

		<Modal v-if="showCreateCoordinatesModal" @close="showCreateCoordinatesModal = false">
			<template v-slot:header>
				<h3>Создать координаты</h3>
			</template>
			<template v-slot:body>
				<CreateCoordinatesForm @coordinates-created="onCoordinatesCreated" />
			</template>
		</Modal>

		<Modal v-if="showCreateAlbumModal" @close="showCreateAlbumModal = false">
			<template v-slot:header>
				<h3>Создать альбом</h3>
			</template>
			<template v-slot:body>
				<CreateAlbumForm @album-created="onAlbumCreated" />
			</template>
		</Modal>

		<Modal v-if="showCreateLabelModal" @close="showCreateLabelModal = false">
			<template v-slot:header>
				<h3>Создать лейбл</h3>
			</template>
			<template v-slot:body>
				<CreateLabelForm @label-created="onLabelCreated" />
			</template>
		</Modal>
	</div>
</template>

<script>
import NetworkService from '../services/NetworkService.js';
import Modal from '../components/ModalComponent.vue';
import { useRouter } from 'vue-router';
import CreateCoordinatesForm from '../components/forms/CreateCoordinatesForm.vue';
import CreateAlbumForm from '../components/forms/CreateAlbumForm.vue';
import CreateLabelForm from '../components/forms/CreateLabelForm.vue';

export default {
	name: 'CreateMusicBand',
	components: {
		Modal,
		CreateCoordinatesForm,
		CreateAlbumForm,
		CreateLabelForm
	},
	setup() {
		const router = useRouter();
		return { router };
	},
	data() {
		return {
			musicBand: {
				name: '',
				coordinatesId: null,
				genre: 'ROCK',
				numberOfParticipants: 1,
				singlesCount: 0,
				description: '',
				bestAlbumId: null,
				albumsCount: 0,
				establishmentDate: '',
				labelId: null
			},
			coordinates: [],
			albums: [],
			labels: [],
			showCreateCoordinatesModal: false,
			showCreateAlbumModal: false,
			showCreateLabelModal: false,
			isEditing: false,
			musicBandId: null
		};
	},
	mounted() {
		this.isEditing = this.$route.name === 'EditMusicBand';
		if (this.isEditing) {
			this.musicBandId = this.$route.params.id;
			this.fetchMusicBand();
		} else {
			this.resetMusicBandData();
		}
		this.fetchCoordinates();
		this.fetchAlbums();
		this.fetchLabels();
	},
	methods: {
		async fetchMusicBand() {
			try {
				const response = await NetworkService.get(`/music-bands/${this.musicBandId}`);
				const data = response.data;

				this.musicBand = {
					...data,
					coordinatesId: data.coordinates.id,
					bestAlbumId: data.bestAlbum ? data.bestAlbum.id : null,
					labelId: data.label ? data.label.id : null,
					establishmentDate: data.establishmentDate ? this.formatDateForInput(data.establishmentDate) : ''
				};
			} catch (error) {
				console.error('Ошибка при получении данных группы:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить данные группы' });
			}
		},
		formatDateForInput(dateString) {
			const date = new Date(dateString);
			const year = date.getFullYear();
			const month = ('0' + (date.getMonth() + 1)).slice(-2);
			const day = ('0' + date.getDate()).slice(-2);
			return `${year}-${month}-${day}`;
		},
		async fetchCoordinates() {
			try {
				const response = await NetworkService.get('/coordinates');
				this.coordinates = response.data;
			} catch (error) {
				console.error('Ошибка при получении координат:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить координаты' });
			}
		},
		async fetchAlbums() {
			try {
				const response = await NetworkService.get('/albums');
				this.albums = response.data;
			} catch (error) {
				console.error('Ошибка при получении альбомов:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить альбомы' });
			}
		},
		async fetchLabels() {
			try {
				const response = await NetworkService.get('/labels');
				this.labels = response.data;
			} catch (error) {
				console.error('Ошибка при получении лейблов:', error);
				this.$notify({ type: 'error', text: 'Не удалось получить лейблы' });
			}
		},
		async createMusicBand() {
			try {
				this.musicBand.establishmentDate = new Date(this.musicBand.establishmentDate).toISOString();
				if (this.isEditing) {
					await NetworkService.put(`/music-bands/${this.musicBandId}`, this.musicBand);
					console.log('Группа успешно обновлена');
					this.$notify({ type: 'success', text: 'Группа обновлена' });
				} else {
					await NetworkService.post('/music-bands', this.musicBand);
					console.log('Группа успешно создана');
					this.$notify({ type: 'success', text: 'Группа создана' });
				}
				this.router.push('/');
			} catch (error) {
				console.error('Ошибка:', error);
				this.$notify({ type: 'error', text: 'Произошла ошибка' });
			}
		},
		onCoordinatesCreated(newCoordinates) {
			console.log('Новые координаты:', newCoordinates);
			this.$notify({ type: 'success', text: 'Координаты созданы' });
			this.coordinates.push(newCoordinates);
			this.musicBand.coordinatesId = newCoordinates.id;
			this.showCreateCoordinatesModal = false;
		},
		onAlbumCreated(newAlbum) {
			console.log('Новый альбом:', newAlbum);
			this.$notify({ type: 'success', text: 'Альбом создан' });
			this.albums.push(newAlbum);
			this.musicBand.bestAlbumId = newAlbum.id;
			this.showCreateAlbumModal = false;
		},
		onLabelCreated(newLabel) {
			console.log('Новый лейбл:', newLabel);
			this.$notify({ type: 'success', text: 'Лейбл создан' });
			this.labels.push(newLabel);
			this.musicBand.labelId = newLabel.id;
			this.showCreateLabelModal = false;
		},
		resetMusicBandData() {
			this.musicBand = {
				name: '',
				coordinatesId: null,
				genre: 'ROCK',
				numberOfParticipants: 1,
				singlesCount: 0,
				description: '',
				bestAlbumId: null,
				albumsCount: 0,
				establishmentDate: '',
				labelId: null
			};
		}
	}
};
</script>
