import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './Dashboard.module.css';
import FilterMessages from "../components/FilterMessages";

export default function Dashboard() {
  const [messages, setMessages] = useState([]);
  const [editingMessageId, setEditingMessageId] = useState(null);
  const [editedTitle, setEditedTitle] = useState('');
  const [editedContent, setEditedContent] = useState('');
  const [editedDate, setEditedDate] = useState('');
  const [sharedLinks, setSharedLinks] = useState({});

  const fetchMessages = async () => {
    try {
      const response = await axiosPrivate.get('/api/user/messages');
      if (Array.isArray(response.data)) {
        const normalized = response.data.map(msg => ({
          ...msg,
          deliveryDate: msg.messageDateTime
            ? msg.messageDateTime.split('T')[0]
            : '',
        }));
        setMessages(normalized);
      } else {
        setMessages([]);
        console.warn('Messages data is not an array:', response.data);
      }
    } catch (error) {
      console.error('Error fetching messages:', error);
      setMessages([]);
    }
  };

  const handleFilterUpdate = (filteredMessages) => {
    const normalized = filteredMessages.map(msg => ({
      ...msg,
      deliveryDate: msg.messageDateTime
        ? msg.messageDateTime.split('T')[0]
        : '',
    }));
    setMessages(normalized);
  };

  const handleDelete = async (id) => {
    try {
      await axiosPrivate.delete(`/api/user/messages/${id}`);
      setMessages((prev) => prev.filter((msg) => msg.id !== id));
    } catch (error) {
      console.error('Error deleting message:', error);
    }
  };

  const handleEdit = (id, title, content, deliveryDate) => {
    setEditingMessageId(id);
    setEditedTitle(title);
    setEditedContent(content);
    setEditedDate(deliveryDate);
  };

  const handleUpdate = async () => {
    try {
      await axiosPrivate.put(`/api/user/messages/${editingMessageId}`, {
        title: editedTitle,
        content: editedContent,
        deliveryDate: editedDate,
      });
      setEditingMessageId(null);
      setEditedTitle('');
      setEditedContent('');
      setEditedDate('');
      fetchMessages();
    } catch (error) {
      console.error('Error updating message:', error);
    }
  };

  const handleShare = async (messageId) => {
    try {
      const response = await axiosPrivate.put(`/api/user/messages/${messageId}/share`);
      const shareUrl = response.data;
      await navigator.clipboard.writeText(shareUrl);
      setSharedLinks(prev => ({ ...prev, [messageId]: shareUrl }));
      alert(`✅ Shareable link copied to clipboard:\n${shareUrl}`);
    } catch (error) {
      console.error("Error sharing message:", error);
      alert("❌ Failed to generate share link.");
    }
  };
  useEffect(() => {
    fetchMessages();
  }, []);

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>📬 Your Messages</h2>

      <FilterMessages onMessagesUpdate={handleFilterUpdate} />

      {Array.isArray(messages) && messages.length > 0 ? (
        <ul className={styles.messageList}>
          {messages.map((msg) => (
            <li key={msg.id} className={styles.messageCard}>
              {editingMessageId === msg.id ? (
                <>
                  <input
                    type="text"
                    value={editedTitle}
                    onChange={(e) => setEditedTitle(e.target.value)}
                    className={styles.input}
                    placeholder="Title"
                  />
                  <textarea
                    value={editedContent}
                    onChange={(e) => setEditedContent(e.target.value)}
                    className={styles.textarea}
                    placeholder="Content"
                  />
                  <input
                    type="date"
                    value={editedDate}
                    onChange={(e) => setEditedDate(e.target.value)}
                    className={styles.input}
                  />
                  <button onClick={handleUpdate} className={styles.saveButton}>
                    💾 Save
                  </button>
                </>
              ) : (
                <>
                  <h3 className={styles.title}>{msg.title}</h3>
                  <p className={styles.content}>{msg.content}</p>
                  <p className={styles.date}>
                    📅 Deliver on:{' '}
                    {msg.messageDateTime
                      ? new Date(msg.messageDateTime).toLocaleDateString()
                      : 'N/A'}
                  </p>
                  <div className={styles.actions}>
                    <button
                      onClick={() =>
                        handleEdit(
                          msg.id,
                          msg.title,
                          msg.content,
                          msg.deliveryDate
                        )
                      }
                      className={styles.editButton}
                    >
                      ✏️ Edit
                    </button>
                    <button
                      onClick={() => handleDelete(msg.id)}
                      className={styles.deleteButton}
                    >
                      🗑️ Delete
                    </button>
                    <button
                      onClick={() => handleShare(msg.id)}
                      className={styles.shareButton}
                    >
                      🔗 Share
                    </button>
                  </div>
                  {sharedLinks[msg.id] && (
                    <p className={styles.sharedLink}>
                      <a href={sharedLinks[msg.id]} target="_blank" rel="noopener noreferrer">
                        📎 View Shared Link
                      </a>
                    </p>
                  )}
                </>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p className={styles.empty}>No messages yet.</p>
      )}
    </div>
  );
}
