import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './Profile.module.css';

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axiosPrivate.get('/api/auth/profile');
 // ✅ New - matches backend

        setProfile(response.data);
      } catch (error) {
        console.error("Error loading profile:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) return <p className={styles.status}>Loading profile...</p>;

  if (!profile) return <p className={styles.status}>Failed to load profile</p>;

  return (
    <div className={styles.profileContainer}>
      <h2 className={styles.heading}>User Profile</h2>
      <p className={styles.detail}><strong>Username:</strong> {profile.username}</p>
      <p className={styles.detail}><strong>Email:</strong> {profile.email}</p>
    </div>
  );
}
