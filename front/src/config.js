export default {
	apiUrl: import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : '/api',
	websocketUrl: import.meta.env.MODE === 'development' ? 'ws://localhost:8080/ws' : '/ws'
};
