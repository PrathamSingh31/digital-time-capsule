import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axiosPrivate.get('/user/profile');
        setProfile(response.data);
      } catch (error) {
        console.error("Error loading profile:", error);
      } finally {
        setLoading(false); // ✅ Important!
      }
    };

    fetchProfile();
  }, []);

  if (loading) return <p>Loading profile...</p>;

  if (!profile) return <p>Failed to load profile</p>;

  return (
    <div>
      <h2>User Profile</h2>
      <p><strong>Username:</strong> {profile.username}</p>
      <p><strong>Email:</strong> {profile.email}</p>
    </div>
  );
}
