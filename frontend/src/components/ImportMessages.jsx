// src/components/ImportMessages.jsx
import React, { useState } from 'react';
import axiosPrivate from "../api/axiosPrivate";
import styles from './ImportMessages.module.css';

const ImportMessages = () => {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setMessage('');
  };

  const handleImport = async () => {
    if (!file) {
      setMessage('Please select a file first.');
      return;
    }

    try {
      const text = await file.text();
      const messages = JSON.parse(text);

      if (!Array.isArray(messages)) {
        setMessage('Invalid file format. Expected an array of messages.');
        return;
      }

      const response = await axiosPrivate.post('/api/user/messages/import', messages);
      setMessage(response.data);
    } catch (error) {
      console.error('Import failed:', error);
      setMessage('Failed to import messages.');
    }
  };

  return (
    <div className={styles.container}>
      <h2>Import Messages</h2>
      <input type="file" accept=".json" onChange={handleFileChange} />
      <button onClick={handleImport}>Import</button>
      {message && <p className={styles.message}>{message}</p>}
    </div>
  );
};

export default ImportMessages;
