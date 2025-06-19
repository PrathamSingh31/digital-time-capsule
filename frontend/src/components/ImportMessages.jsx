// src/components/ImportMessages.jsx
import React, { useState } from 'react';
import axiosPrivate from "../api/axiosPrivate";
import styles from './ImportMessages.module.css';

const ImportMessages = () => {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setMessage('');
  };

  const isValidDate = (dateStr) => /^\d{4}-\d{2}-\d{2}$/.test(dateStr);

  const handleImport = async () => {
    if (!file) {
      setMessage('❌ Please select a .json file first.');
      return;
    }

    setLoading(true);
    setMessage('');

    try {
      const text = await file.text();
      const messages = JSON.parse(text);

      if (!Array.isArray(messages)) {
        setMessage('❌ Invalid file format. Expected an array of message objects.');
        setLoading(false);
        return;
      }

      // Validate each message
      for (let i = 0; i < messages.length; i++) {
        const { title, content, deliveryDate } = messages[i];

        if (!title || !content || !deliveryDate) {
          setMessage(`❌ Message at index ${i} is missing required fields.`);
          setLoading(false);
          return;
        }

        if (!isValidDate(deliveryDate)) {
          setMessage(`❌ Invalid date format at index ${i}. Use yyyy-MM-dd.`);
          setLoading(false);
          return;
        }
      }

      const response = await axiosPrivate.post('/api/user/messages/import', messages);
      setMessage(`✅ ${response.data}`);
    } catch (error) {
      console.error('Import failed:', error);
      const serverError = error?.response?.data || '❌ Failed to import messages.';
      setMessage(typeof serverError === 'string' ? serverError : JSON.stringify(serverError));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Import Messages</h2>
      <input
        type="file"
        accept=".json"
        onChange={handleFileChange}
        className={styles.input}
      />
      <button
        onClick={handleImport}
        disabled={loading}
        className={styles.button}
      >
        {loading ? 'Importing...' : 'Import'}
      </button>
      {message && <p className={styles.message}>{message}</p>}
    </div>
  );
};

export default ImportMessages;
