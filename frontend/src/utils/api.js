import axios from 'axios';

// Konfigurasi dasar Axios mengarah ke Spring Boot
export const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  }
});

export default api;