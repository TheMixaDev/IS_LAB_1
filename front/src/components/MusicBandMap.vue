<template>
	<div id="music-band-map" ref="mapContainer"></div>
	<Modal v-if="showModal" @close="showModal = false">
		<template v-slot:header>
			<h3>Информация о лейбле</h3>
		</template>
		<template v-slot:body>
			<MusicBandPopup :music-band="selectedMusicBand" />
		</template>
	</Modal>
</template>

<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import MusicBandPopup from './MusicBandPopup.vue';
import Modal from './ModalComponent.vue';

export default {
	name: 'MusicBandMap',
	components: {
		MusicBandPopup,
		Modal
	},
	props: {
		musicBands: {
			type: Array,
			required: true
		},
		isLoading: {
			type: Boolean,
			default: false
		},
		currentUser: {
			type: Object,
			default: null
		}
	},
	data() {
		return {
			map: null,
			mapInitialized: false,
			layerGroup: L.layerGroup(),
			showModal: false,
			selectedMusicBand: null
		};
	},
	mounted() {
		this.$nextTick(() => {
			if (!this.mapInitialized) {
				this.initMap();
				this.mapInitialized = true;
			}
		});
	},
	watch: {
		musicBands(newMusicBands) {
			if (this.mapInitialized) {
				this.updateMarkers(newMusicBands);
			}
		},
		isLoading(isLoading) {
			if (!isLoading && this.mapInitialized) {
				this.updateMarkers(this.musicBands);
			}
		},
		currentUser(newUser) {
			if (this.mapInitialized && newUser) {
				this.updateMarkers(this.musicBands);
			}
		}
	},
	methods: {
		initMap() {
			if (this.$refs.mapContainer) {
				this.map = L.map(this.$refs.mapContainer).setView([51.505, -0.09], 2);
				L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
					attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
				}).addTo(this.map);
				this.map.on('load', () => {
					this.updateMarkers(this.musicBands);
				});
			} else {
				console.error('Элемент #music-band-map не найден!');
				setTimeout(() => this.initMap(), 10);
			}
		},
		generateColor(userId) {
			const random = Math.sin(userId * 10000);
			return '#' + Math.abs(Math.floor(random * 16777215)).toString(16);
		},
		updateMarkers(musicBands) {
			if (this.map && this.mapInitialized) {
				if (this.map.hasLayer(this.layerGroup)) {
					this.layerGroup.clearLayers();
				}

				musicBands.forEach((musicBand) => {
					if (musicBand.coordinates) {
						const color = this.generateColor(musicBand.createdBy.id);

						const marker = L.marker([musicBand.coordinates.y, musicBand.coordinates.x], {
							icon: L.divIcon({
								className: 'custom-marker',
								html: `<div style="background-color: ${color};width: 100%;height: 100%;border-radius: 50%;border: 1px black solid;" class="custom-marker"></div>`,
								iconSize: [20, 20],
								iconAnchor: [10, 10]
							})
						}).on('click', () => {
							this.selectedMusicBand = musicBand;
							this.showModal = true;
						});
						this.layerGroup.addLayer(marker);
					}
				});
				this.map.addLayer(this.layerGroup);
			} else {
				setTimeout(() => this.updateMarkers(musicBands), 10);
			}
		},
		getMusicBandsBounds(musicBands) {
			const coordinates = musicBands.filter((band) => band.coordinates).map((band) => [band.coordinates.y, band.coordinates.x]);
			return L.latLngBounds(coordinates);
		}
	}
};
</script>

<style scoped>
#music-band-map {
	height: 400px;
	width: 100%;
	border-radius: 30px;
}
</style>
