import axios from 'axios';

const axiosPrivate = axios.create({
  baseURL: 'http://localhost:8080',  // main API base for user messages etc
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add interceptor to attach JWT token from localStorage
axiosPrivate.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

export default axiosPrivate;
