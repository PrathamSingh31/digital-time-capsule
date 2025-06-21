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
  const [editedImage, setEditedImage] = useState(null);
  const [editedVideo, setEditedVideo] = useState(null);
  const [sharedLinks, setSharedLinks] = useState({});
  const [revealedMessages, setRevealedMessages] = useState({});

  const normalizeMessages = (messages) =>
    messages.map(msg => ({
      ...msg,
      deliveryDate: msg.messageDateTime
        ? msg.messageDateTime.split('T')[0]
        : '',
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
    setMessages(normalizeMessages(filteredMessages));
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this message?")) return;
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
    setEditedImage(null);
    setEditedVideo(null);
  };

  const handleUpdate = async () => {
    try {
      const formData = new FormData();
      formData.append('title', editedTitle);
      formData.append('content', editedContent);
      formData.append('deliveryDate', editedDate);
      if (editedImage) formData.append('image', editedImage);
      if (editedVideo) formData.append('video', editedVideo);

      await axiosPrivate.put(`/api/user/messages/${editingMessageId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      setEditingMessageId(null);
      setEditedTitle('');
      setEditedContent('');
      setEditedDate('');
      setEditedImage(null);
      setEditedVideo(null);
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
          {messages.map((msg) => {
            const isFuture = new Date(msg.messageDateTime) > new Date();
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

                    <label>Update Image:</label>
                    <input
                      type="file"
                      accept="image/*"
                      onChange={(e) => setEditedImage(e.target.files[0])}
                      className={styles.input}
                    />
                    {editedImage && (
                      <img
                        src={URL.createObjectURL(editedImage)}
                        alt="New Preview"
                        className={styles.image}
                      />
                    )}

                    <label>Update Video:</label>
                    <input
                      type="file"
                      accept="video/*"
                      onChange={(e) => setEditedVideo(e.target.files[0])}
                      className={styles.input}
                    />
                    {editedVideo && (
                      <video controls className={styles.previewVideo}>
                        <source
                          src={URL.createObjectURL(editedVideo)}
                          type={editedVideo.type}
                        />
                      </video>
                    )}

                    <button onClick={handleUpdate} className={styles.saveButton}>
                      💾 Save
                    </button>
                  </>
                ) : (
                  <>
                    <h3 className={styles.title}>{msg.title}</h3>

                    {isFuture && !isRevealed ? (
                      <div className={styles.lockedMessage}>
                        <p className={styles.lockedText}>
                          🔒 This message is locked until {new Date(msg.messageDateTime).toLocaleDateString()}
                        </p>
                        <button
                          className={styles.revealButton}
                          onClick={() =>
                            setRevealedMessages((prev) => ({ ...prev, [msg.id]: true }))
                          }
                        >
                          🔓 Reveal Now
                        </button>
                      </div>
                    ) : (
                      <>
                        <p className={styles.content}>{msg.content}</p>

                        {msg.imageUrl && (
                          <img
                            src={`http://localhost:8080${msg.imageUrl}`}
                            alt="Message Visual"
                            className={styles.image}
                          />
                        )}

                        {msg.videoUrl && (
                          <video controls className={styles.previewVideo}>
                            <source src={`http://localhost:8080${msg.videoUrl}`} type="video/mp4" />
                          </video>
                        )}
                      </>
                    )}

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
                        <a
                          href={sharedLinks[msg.id]}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
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
