import axios from 'axios';

// Konfigurasi dasar Axios mengarah ke Spring Boot
export const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
  // Izinkan pengiriman kredensial/cookies jika diperlukan
  withCredentials: true, 
});

export default api;