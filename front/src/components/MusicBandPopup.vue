<template>
	<div>
		<b>
			<a href="#" @click="viewMusicBand(musicBand.id)">{{ musicBand.name }}</a>
		</b>
		<br />Жанр: {{ musicBand.genre }}
		<br />
		<button v-if="canEdit" @click="editMusicBand(musicBand.id)">Редактировать</button>
	</div>
</template>

<script>
export default {
	name: 'MusicBandPopup',
	props: {
		musicBand: {
			type: Object,
			required: true
		},
		currentUser: {
			type: Object,
			default: null
		}
	},
	computed: {
		canEdit() {
			return this.currentUser && (this.musicBand.createdBy.id === this.currentUser.id || this.currentUser.role === 'ADMIN');
		}
	},
	methods: {
		viewMusicBand(id) {
			window.location.href = `/music-bands/${id}`;
		},
		editMusicBand(id) {
			window.location.href = `/music-bands/${id}/edit`;
		}
	}
};
</script>
