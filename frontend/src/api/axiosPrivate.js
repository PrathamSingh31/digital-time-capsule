// src/api/axiosPrivate.js
import axios from 'axios';

const axiosPrivate = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

// Inject token into each request
axiosPrivate.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // or your storage logic
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default axiosPrivate;
