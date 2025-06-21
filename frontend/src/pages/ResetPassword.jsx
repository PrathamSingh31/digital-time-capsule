import React, { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import axiosAuth from '../api/axiosAuth';
import styles from './ResetPassword.module.css';

export default function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');

  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    try {
      const response = await axiosAuth.post('/reset-password', {
        token,
        newPassword
      });

      alert(response.data);
    } catch (error) {
      console.error('Password reset error:', error);
      alert('Failed to reset password.');
    }
  };

  return (
    <div className={styles.resetContainer}>
      <h2 className={styles.heading}>Reset Password</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="password"
          placeholder="New Password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          required
          className={styles.input}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
          className={styles.input}
        />
        <button type="submit" className={styles.button}>
          Reset Password
        </button>
      </form>
    </div>
  );
}
