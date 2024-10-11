<template>
	<div class="pagination">
		<button :disabled="currentPage === 1" @click="onPageChange(currentPage - 1)" class="pagination-button">
			<font-awesome-icon :icon="['fas', 'chevron-left']" />
		</button>

		<span v-for="page in pageNumbers" :key="page">
			<button :class="{ active: currentPage === page, 'pagination-button': true }" @click="onPageChange(page)">
				{{ page }}
			</button>
		</span>

		<button :disabled="currentPage === totalPages" @click="onPageChange(currentPage + 1)" class="pagination-button">
			<font-awesome-icon :icon="['fas', 'chevron-right']" />
		</button>
	</div>
</template>

<script>
export default {
	name: 'PaginationComponent',
	props: {
		totalPages: {
			type: Number,
			required: true
		}
	},
	data() {
		return {
			currentPage: 1,
			maxVisibleButtons: 5
		};
	},
	computed: {
		pageNumbers() {
			let startPage = Math.max(1, this.currentPage - Math.floor(this.maxVisibleButtons / 2));
			let endPage = Math.min(this.totalPages, startPage + this.maxVisibleButtons - 1);

			if (endPage - startPage + 1 < this.maxVisibleButtons && this.totalPages > this.maxVisibleButtons) {
				startPage = Math.max(1, endPage - this.maxVisibleButtons + 1);
			}

			const pages = [];
			for (let i = startPage; i <= endPage; i++) {
				pages.push(i);
			}
			return pages;
		}
	},
	methods: {
		onPageChange(page) {
			if (page >= 1 && page <= this.totalPages) {
				this.currentPage = page;
				this.$emit('page-changed', page);
			}
		}
	}
};
</script>

<style scoped>
.pagination {
	display: flex;
	justify-content: center;
	margin-top: 20px;
}

.pagination-button {
	margin: 0 5px;
	padding: 8px 12px;
	border: none;
	background-color: #333;
	cursor: pointer;
	border-radius: 4px;
	transition: background-color 0.3s ease;
}

.pagination-button:disabled {
	opacity: 0.5;
	cursor: not-allowed;
}

.pagination-button.active {
	background-color: #554dd8;
	color: white;
}

.pagination-button:hover {
	background-color: #000;
}
</style>
