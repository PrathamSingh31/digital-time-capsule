import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './Profile.module.css';

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);
  const [message, setMessage] = useState('');
  const [emailRemindersEnabled, setEmailRemindersEnabled] = useState(true);
  const [updating, setUpdating] = useState(false);

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

    const fetchReminderSetting = async () => {
      try {
        const res = await axiosPrivate.get('/api/auth/settings/email-reminders');
        setEmailRemindersEnabled(res.data);
      } catch (err) {
        console.error("Failed to fetch reminder setting", err);
      }
    };

    fetchProfile();
    fetchReminderSetting();
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

  const toggleEmailReminders = async () => {
    const newValue = !emailRemindersEnabled;
    setUpdating(true);
    try {
      await axiosPrivate.put(`/api/auth/settings/email-reminders?enabled=${newValue}`);
      setEmailRemindersEnabled(newValue);
      setMessage(`✅ Email reminders ${newValue ? 'enabled' : 'disabled'}.`);
    } catch (error) {
      console.error("Failed to update email reminders:", error);
      setMessage("❌ Failed to update reminder setting.");
    } finally {
      setUpdating(false);
    }
  };

  if (loading) return <p className={styles.status}>Loading profile...</p>;
  if (!profile) return <p className={styles.status}>Failed to load profile</p>;

  return (
    <div className={styles.profileContainer}>
      <h2 className={styles.heading}>User Profile</h2>
      <p className={styles.detail}><strong>Username:</strong> {profile.username}</p>
      <p className={styles.detail}><strong>Email:</strong> {profile.email}</p>

      <div className={styles.reminderToggle}>
        <label><strong>📩 Email Reminders:</strong></label>
        <button
          onClick={toggleEmailReminders}
          disabled={updating}
          className={styles.toggleButton}
        >
          {emailRemindersEnabled ? 'Disable' : 'Enable'}
        </button>
      </div>

      <button onClick={sendTestReminder} className={styles.testButton} disabled={sending}>
        {sending ? 'Sending...' : '📧 Send Test Reminder'}
      </button>

      {message && <p className={styles.message}>{message}</p>}
    </div>
  );
}
