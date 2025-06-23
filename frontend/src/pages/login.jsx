import React, { useState } from 'react';
import axiosAuth from '../api/axiosAuth';
import { useNavigate, Link } from 'react-router-dom';
import styles from './Login.module.css';

export default function Login() {
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const response = await axiosAuth.post('/login', formData);
      const { token, username } = response.data;
      localStorage.setItem('token', token);
      localStorage.setItem('username', username);
      alert('Login successful!');
      navigate('/dashboard');
    } catch (error) {
      console.error('Login error:', error);
      setError('Login failed. Please check your username or password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.loginContainer}>
      <h2 className={styles.heading}>🔐 Login</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        {error && <div className={styles.error}>{error}</div>}
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          required
          className={styles.input}
        />

        <div className={styles.passwordWrapper}>
          <input
            type={showPassword ? 'text' : 'password'}
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
            className={`${styles.input}`}
          />
          <button
            type="button"
            className={styles.togglePasswordBtn}
            onClick={() => setShowPassword(!showPassword)}
          >
            {showPassword ? 'Hide' : '👁 Show'}
          </button>
        </div>


        <button
          type="submit"
          className={styles.button}
          disabled={loading}
        >
          {loading ? 'Logging in...' : 'Login'}
        </button>

        <div className={styles.links}>
          <Link to="/forgot-password" className={styles.link}>
            Forgot Password?
          </Link>
          <span className={styles.separator}>|</span>
          <Link to="/register" className={styles.link}>
            Register
          </Link>
        </div>
      </form>
    </div>
  );
}
