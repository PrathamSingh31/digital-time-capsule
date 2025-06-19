import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './Profile.module.css';

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axiosPrivate.get('/api/auth/profile');
        setProfile(response.data);
      } catch (error) {
        console.error("Error loading profile:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  const sendTestReminder = async () => {
    setSending(true);
    setMessage('');
    try {
      const response = await axiosPrivate.post('/api/auth/send-test-reminder');
      setMessage(response.data);
    } catch (error) {
      console.error("Error sending reminder:", error);
      setMessage("❌ Failed to send test reminder.");
    } finally {
      setSending(false);
    }
  };

  if (loading) return <p className={styles.status}>Loading profile...</p>;

  if (!profile) return <p className={styles.status}>Failed to load profile</p>;

  return (
    <div className={styles.profileContainer}>
      <h2 className={styles.heading}>User Profile</h2>
      <p className={styles.detail}><strong>Username:</strong> {profile.username}</p>
      <p className={styles.detail}><strong>Email:</strong> {profile.email}</p>

      <button onClick={sendTestReminder} className={styles.testButton} disabled={sending}>
        {sending ? 'Sending...' : '📧 Send Test Reminder'}
      </button>

      {message && <p className={styles.message}>{message}</p>}
    </div>
  );
}
