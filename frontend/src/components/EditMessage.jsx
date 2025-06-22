import React, { useState, useEffect } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './EditMessage.module.css';

export default function EditMessage({ messageId, onMessageUpdated }) {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [deliveryDate, setDeliveryDate] = useState('');
  const [unlockDate, setUnlockDate] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isLocked, setIsLocked] = useState(false);

  useEffect(() => {
    const fetchMessage = async () => {
      try {
        const res = await axiosPrivate.get(`/api/user/messages/${messageId}`);
        const data = res.data;

        setTitle(data.title);
        setContent(data.content);
        setDeliveryDate(data.messageDateTime.split('T')[0]);

        if (data.unlockDate) {
          setUnlockDate(data.unlockDate.split('T')[0]);

          const now = new Date();
          const unlock = new Date(data.unlockDate);
          if (unlock > now) {
            setIsLocked(true);
          }
        }
      } catch (err) {
        console.error(err);
        setError('Failed to load message details.');
      }
    };

    fetchMessage();
  }, [messageId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (isLocked) {
      setError("This message is locked and cannot be edited.");
      return;
    }

    try {
      await axiosPrivate.put(`/api/user/messages/${messageId}`, {
        title,
        content,
        deliveryDate,
        unlockDate
      });
      setSuccess('Message updated successfully.');
      onMessageUpdated && onMessageUpdated();
    } catch (err) {
      console.error(err);
      setError(err?.response?.data || 'Failed to update message.');
    }
  };


  return (
    <div className={styles.container}>
      <h2>Edit Message</h2>

      {isLocked ? (
        <p className={styles.lockedWarning}>
          🔒 This message is locked until {unlockDate} and cannot be edited.
        </p>
      ) : (
        <form className={styles.form} onSubmit={handleSubmit}>
          <label>Title</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />

          <label>Content</label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />

          <label>Delivery Date</label>
          <input
            type="date"
            value={deliveryDate}
            onChange={(e) => setDeliveryDate(e.target.value)}
            required
          />

          <label>Unlock Date (Optional)</label>
          <input
            type="date"
            value={unlockDate}
            onChange={(e) => setUnlockDate(e.target.value)}
          />

          <button type="submit">Update Message</button>
        </form>
      )}

      {success && <p className={styles.success}>{success}</p>}
      {error && <p className={styles.error}>{error}</p>}
    </div>
  );
}
