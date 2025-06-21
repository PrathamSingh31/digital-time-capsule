import React, { useState } from 'react';
import axios from 'axios';
import styles from './ForgotPassword.module.css';

export default function ForgotPassword() {
  const [email, setEmail] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/auth/request-reset', {
        email: email.trim(),
      });
      alert('✅ Password reset link sent. Check your email.');
    } catch (error) {
      console.error('Reset request failed:', error);
      alert('❌ Failed to send reset link.');
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Forgot Password</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Enter your email"
          required
          className={styles.input}
        />
        <button type="submit" className={styles.button}>
          Send Reset Link
        </button>
      </form>
    </div>
  );
}
