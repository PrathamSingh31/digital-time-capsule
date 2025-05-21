import React, { useEffect, useState } from 'react';
import axios from '../api/axios';
import { useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('/user/messages', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        const today = new Date();
        const filtered = response.data.filter(msg => new Date(msg.unlockDate) <= today);
        setMessages(filtered);
        setLoading(false);
      } catch (err) {
        setError('Failed to load messages');
        setLoading(false);
      }
    };

    fetchMessages();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const handleCreate = () => {
    navigate('/create');
  };

  return (
    <div style={{ maxWidth: 600, margin: '40px auto' }}>
      <h2>Welcome to Your Dashboard</h2>

      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 20 }}>
        <button onClick={handleCreate}>+ Create New Message</button>
        <button onClick={handleLogout}>Logout</button>
      </div>

      {loading ? (
        <p>Loading messages...</p>
      ) : error ? (
        <p style={{ color: 'red' }}>{error}</p>
      ) : messages.length === 0 ? (
        <p>No unlocked messages yet.</p>
      ) : (
        <div>
          {messages.map((msg, index) => (
            <div key={index} style={{ border: '1px solid #ccc', padding: 15, marginBottom: 10 }}>
              <p><strong>Message:</strong> {msg.message}</p>
              <p><strong>Unlock Date:</strong> {new Date(msg.unlockDate).toLocaleDateString()}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
