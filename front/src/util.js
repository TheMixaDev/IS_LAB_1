const genreKeys = ['ROCK', 'PSYCHEDELIC_CLOUD_RAP', 'JAZZ', 'PUNK_ROCK', 'BRIT_POP'];
const genreLabels = ['Рок', 'Психоделик-клауд-рэп', 'Джаз', 'Панк-рок', 'Брит-поп'];
export const getGenreLabel = (genreKey) => {
	const index = genreKeys.indexOf(genreKey);
	return index !== -1 ? genreLabels[index] : genreKey;
};
