import axios from 'axios';

const axiosAuth = axios.create({
  baseURL: 'http://localhost:8080/api/auth',  // auth endpoints base
  headers: {
    'Content-Type': 'application/json',
  },
});

export default axiosAuth;
