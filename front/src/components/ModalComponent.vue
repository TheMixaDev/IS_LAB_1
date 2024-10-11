<template>
	<transition name="modal">
		<div class="modal-overlay" @click="$emit('close')" v-if="true">
			<div class="modal-content" @click.stop>
				<slot name="header"></slot>
				<slot name="body"></slot>
				<div class="centered">
					<button @click="$emit('close')" class="close-button spaced-top standart-width">Закрыть</button>
				</div>
			</div>
		</div>
	</transition>
</template>

<style scoped>
.modal-overlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.5);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.modal-content {
	background-color: #fff;
	color: black;
	padding: 20px;
	border-radius: 5px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
}

.modal-enter-active,
.modal-leave-active {
	transition: opacity 0.3s ease;
}

.modal-enter,
.modal-leave-to {
	opacity: 0;
}
</style>
