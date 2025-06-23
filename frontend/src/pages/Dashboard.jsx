import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';
import styles from './Dashboard.module.css';
import FilterMessages from "../components/FilterMessages";

// ...imports remain the same

export default function Dashboard() {
  const [messages, setMessages] = useState([]);
  const [editingMessageId, setEditingMessageId] = useState(null);
  const [editedTitle, setEditedTitle] = useState('');
  const [editedContent, setEditedContent] = useState('');
  const [editedDate, setEditedDate] = useState('');
  const [editedImage, setEditedImage] = useState(null);
  const [editedVideo, setEditedVideo] = useState(null);
  const [sharedLinks, setSharedLinks] = useState({});
  const [editedUnlockDate, setEditedUnlockDate] = useState('');
  const [revealedMessages, setRevealedMessages] = useState({});

  const normalizeMessages = (messages) =>
    messages.map(msg => ({
      ...msg,
      deliveryDate: msg.messageDateTime?.split('T')[0] || '',
      unlockDate: msg.unlockDate?.split('T')[0] || ''
    }));

  const fetchMessages = async () => {
    try {
      const response = await axiosPrivate.get('/api/user/messages');
      if (Array.isArray(response.data)) {
        setMessages(normalizeMessages(response.data));
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
    setMessages(filteredMessages);
  };

  const handleEdit = (id, title, content, deliveryDate, unlockDate) => {
    setEditingMessageId(id);
    setEditedTitle(title);
    setEditedContent(content);
    setEditedDate(deliveryDate);
    setEditedImage(null);
    setEditedVideo(null);
    setEditedUnlockDate(unlockDate || '');
  };

  const handleUpdate = async () => {
    try {
      const formData = new FormData();
      formData.append('title', editedTitle);
      formData.append('content', editedContent);
      formData.append('deliveryDate', editedDate);
      if (editedImage) formData.append('image', editedImage);
      if (editedVideo) formData.append('video', editedVideo);
      if (editedUnlockDate) {
        const formatted = new Date(editedUnlockDate).toISOString().split('T')[0];
        formData.append('unlockDate', formatted);
      }

      await axiosPrivate.put(`/api/user/messages/${editingMessageId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      setEditingMessageId(null);
      fetchMessages();
    } catch (error) {
      console.error('Error updating message:', error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this message?")) return;
    try {
      await axiosPrivate.delete(`/api/user/messages/${id}`);
      setMessages(prev => prev.filter(msg => msg.id !== id));
    } catch (error) {
      console.error('Error deleting message:', error);
    }
  };

  const handleShare = async (id) => {
    try {
      const res = await axiosPrivate.put(`/api/user/messages/${id}/share`);
      await navigator.clipboard.writeText(res.data);
      setSharedLinks(prev => ({ ...prev, [id]: res.data }));
      alert(`✅ Shareable link copied to clipboard:\n${res.data}`);
    } catch (e) {
      alert("❌ Failed to generate share link.");
    }
  };



const handleExportPDF = async (messageId) => {
  try {
    const response = await axiosPrivate.get(`/api/user/messages/${messageId}/export/pdf`, {
      responseType: 'blob',
    });

    const blob = new Blob([response.data], { type: 'application/pdf' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `message_${messageId}.pdf`;
    a.click();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('Failed to export PDF:', error);
    alert('Error exporting message as PDF');
  }
};


  useEffect(() => {
    fetchMessages();
  }, []);

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>📬 Your Messages</h2>
      <FilterMessages onMessagesUpdate={handleFilterUpdate} />

      {messages.length > 0 ? (
        <ul className={styles.messageList}>
          {messages.map((msg) => {
            const now = new Date();
            const deliveryDate = new Date(msg.messageDateTime);
            const unlockDate = msg.unlockDate ? new Date(msg.unlockDate) : null;

            const isVisible = deliveryDate <= now;
            const isLocked = !isVisible;
            const canEdit = !unlockDate || unlockDate <= now;
            const isRevealed = revealedMessages[msg.id];

            return (
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
                    <input
                      type="date"
                      value={editedUnlockDate}
                      onChange={(e) => setEditedUnlockDate(e.target.value)}
                      className={styles.input}
                    />
                    <input
                      type="file"
                      accept="image/*"
                      onChange={(e) => setEditedImage(e.target.files[0])}
                      className={styles.input}
                    />
                    <input
                      type="file"
                      accept="video/*"
                      onChange={(e) => setEditedVideo(e.target.files[0])}
                      className={styles.input}
                    />
                    <button onClick={handleUpdate} className={styles.saveButton}>
                      💾 Save
                    </button>
                  </>
                ) : (
                  <>
                    <h3 className={styles.title}>{msg.title}</h3>

                    {isLocked && !isRevealed ? (
                      <div className={styles.lockedMessage}>
                        <p className={styles.lockedText}>
                          🔒 This message is locked until {deliveryDate.toLocaleDateString()}
                        </p>
                        {isVisible && (
                          <button
                            className={styles.revealButton}
                            onClick={() =>
                              setRevealedMessages(prev => ({ ...prev, [msg.id]: true }))
                            }
                          >
                            🔓 Reveal Now
                          </button>
                        )}
                      </div>
                    ) : (
                      <>
                        <p className={styles.content}>{msg.content}</p>
                        {msg.imageUrl && (
                          <img
                            src={`http://localhost:8080${msg.imageUrl}`}
                            alt="Visual"
                            className={styles.image}
                          />
                        )}
                        {msg.videoUrl && (
                          <video controls className={styles.previewVideo}>
                            <source
                              src={`http://localhost:8080${msg.videoUrl}`}
                              type="video/mp4"
                            />
                          </video>
                        )}
                      </>
                    )}

                    <p className={styles.date}>📅 Deliver on: {deliveryDate.toLocaleDateString()}</p>
                    {msg.unlockDate && (
                      <p className={styles.date}>🔓 Unlock on: {new Date(msg.unlockDate).toLocaleDateString()}</p>
                    )}

                    <div className={styles.actions}>
                      {canEdit ? (
                        <>
                          <button
                            onClick={() =>
                              handleEdit(
                                msg.id,
                                msg.title,
                                msg.content,
                                msg.deliveryDate,
                                msg.unlockDate
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
                        </>
                      ) : (
                        <p className={styles.lockedText}>
                          🔒 Editing disabled until {new Date(msg.unlockDate).toLocaleDateString()}
                        </p>
                      )}
                      <button
                        onClick={() => handleShare(msg.id)}
                        className={styles.shareButton}
                      >
                        🔗 Share
                      </button>

                       {/* ✅ NEW EXPORT PDF BUTTON */}
                        <button
                          onClick={() => handleExportPDF(msg.id)}
                          className={styles.exportButton}
                        >
                          🧾 Export PDF
                        </button>
                    </div>

                    {sharedLinks[msg.id] && (
                      <p className={styles.sharedLink}>
                        <a href={sharedLinks[msg.id]} target="_blank" rel="noreferrer">
                          📎 View Shared Link
                        </a>
                      </p>
                    )}
                  </>
                )}
              </li>
            );
          })}
        </ul>
      ) : (
        <p className={styles.empty}>No messages yet.</p>
      )}
    </div>
  );
}
