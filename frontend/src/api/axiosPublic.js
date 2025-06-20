// src/api/axiosPublic.js
import axios from "axios";

const axiosPublic = axios.create({
  baseURL: "http://localhost:8080", // ✅ Adjust if your backend runs elsewhere
});

export default axiosPublic;
