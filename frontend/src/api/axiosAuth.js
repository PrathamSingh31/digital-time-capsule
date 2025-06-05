import axios from 'axios';

const axiosAuth = axios.create({
  baseURL: 'http://localhost:8080/api/auth',
});

export default axiosAuth;
