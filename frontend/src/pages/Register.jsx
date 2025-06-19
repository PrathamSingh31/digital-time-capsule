import React, { useState } from 'react';
import axiosAuth from '../api/axiosAuth';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    emailRemindersEnabled: true, // ✅ Include this field by default
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axiosAuth.post('/register', formData); // ✅ `emailRemindersEnabled` included
      alert('Registration successful. Please log in.');
      navigate('/login');
    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        alert(`Registration failed: ${error.response.data.message}`);
      } else {
        console.error('Registration error:', error);
        alert('Registration failed. Please try again later.');
      }
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '50px auto' }}>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          required
          style={{ display: 'block', width: '100%', marginBottom: '10px' }}
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
          style={{ display: 'block', width: '100%', marginBottom: '10px' }}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
          style={{ display: 'block', width: '100%', marginBottom: '10px' }}
        />
        <button type="submit" style={{ width: '100%' }}>
          Register
        </button>
      </form>
    </div>
  );
}
