// src/components/MessageList.jsx
import React, { useEffect, useState } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./MessageList.module.css";
import EditMessage from "./EditMessage"; // assumes you have this

export default function MessageList() {
  const [messages, setMessages] = useState([]);
  const [selectedMessageId, setSelectedMessageId] = useState(null);
  const [yearFilter, setYearFilter] = useState("");
  const [sortOrder, setSortOrder] = useState("asc");
  const [error, setError] = useState("");

  const fetchMessages = async () => {
    try {
      const response = await axiosPrivate.get("/api/user/messages/filter", {
        params: {
          year: yearFilter || undefined,
          sort: sortOrder,
        },
      });
      setMessages(response.data);
    } catch (err) {
      console.error(err);
      setError("Failed to fetch messages.");
    }
  };

  useEffect(() => {
    fetchMessages();
  }, [yearFilter, sortOrder]);

  const handleDelete = async (id) => {
    try {
      await axiosPrivate.delete(`/api/user/messages/${id}`);
      fetchMessages(); // Refresh list
    } catch (err) {
      console.error(err);
      setError("Delete failed: " + err?.response?.data || err.message);
    }
  };

  const handleEditClick = (id) => {
    setSelectedMessageId(id);
  };

  const handleEditSuccess = () => {
    setSelectedMessageId(null);
    fetchMessages();
  };

  return (
    <div className={styles.container}>
      <h2>Your Messages</h2>

      <div className={styles.filters}>
        <input
          type="number"
          placeholder="Filter by year"
          value={yearFilter}
          onChange={(e) => setYearFilter(e.target.value)}
        />
        <select
          value={sortOrder}
          onChange={(e) => setSortOrder(e.target.value)}
        >
          <option value="asc">Sort Ascending</option>
          <option value="desc">Sort Descending</option>
        </select>
      </div>

      {selectedMessageId ? (
        <EditMessage
          messageId={selectedMessageId}
          onSuccess={handleEditSuccess}
          onCancel={() => setSelectedMessageId(null)}
        />
      ) : (
        <ul className={styles.messageList}>
          {messages.map((msg) => {
            const isLocked =
              msg.unlockDate && new Date(msg.unlockDate) > new Date();

            return (
              <li key={msg.id} className={styles.messageItem}>
                <h3>{msg.title}</h3>
                <p>{msg.content}</p>
                <p>
                  <strong>Delivery Date:</strong>{" "}
                  {msg.messageDateTime?.split("T")[0]}
                </p>

                {msg.unlockDate && (
                  <p className={styles.locked}>
                    🔒 Locked until: {msg.unlockDate.split("T")[0]}
                  </p>
                )}

                {msg.imageUrl && (
                  <img
                    src={msg.imageUrl}
                    alt="Attached"
                    className={styles.preview}
                  />
                )}

                {msg.videoUrl && (
                  <video controls className={styles.preview}>
                    <source src={msg.videoUrl} />
                  </video>
                )}

                {/* 🔐 Conditional rendering based on lock status */}
                {!isLocked ? (
                  <>
                    <button onClick={() => handleEditClick(msg.id)}>Edit</button>
                    <button onClick={() => handleDelete(msg.id)}>Delete</button>
                  </>
                ) : (
                  <p className={styles.lockedNote}>✋ Message is locked</p>
                )}
              </li>
            );
          })}
        </ul>

      )}

      {error && <p className={styles.error}>{error}</p>}
    </div>
  );
}
