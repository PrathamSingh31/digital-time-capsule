import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './ScheduledMessages.module.css';

export default function ScheduledMessages() {
  const [scheduledMessages, setScheduledMessages] = useState([]);

  const fetchScheduledMessages = async () => {
    try {
      const response = await axiosPrivate.get('/api/user/messages/scheduled');
      setScheduledMessages(response.data || []);
    } catch (error) {
      console.error('Error fetching scheduled messages:', error);
    }
  };

  useEffect(() => {
    fetchScheduledMessages();
  }, []);

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>⏳ Scheduled Messages</h2>
      {scheduledMessages.length === 0 ? (
        <p className={styles.empty}>No scheduled messages.</p>
      ) : (
        <ul className={styles.messageList}>
          {scheduledMessages.map((msg) => (
            <li key={msg.id} className={styles.messageCard}>
              <h3 className={styles.title}>{msg.title}</h3>
              <p className={styles.content}>{msg.content}</p>
              <p className={styles.date}>
                📅 Deliver on: {new Date(msg.messageDateTime).toLocaleDateString()}
              </p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
