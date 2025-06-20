import React, { useEffect, useState } from "react";
import axios from "axios";
import styles from "./MessagesPage.module.css";

function MessagesPage() {
  const [messages, setMessages] = useState([]);
  const [year, setYear] = useState("");
  const [sort, setSort] = useState("desc");
  const [copiedToken, setCopiedToken] = useState(null);

  const fetchMessages = async () => {
    try {
      const token = localStorage.getItem("token");
      const params = {};
      if (year) params.year = year;
      if (sort) params.sort = sort;

      const response = await axios.get("http://localhost:8080/api/user/messages", {
        headers: { Authorization: `Bearer ${token}` },
        params: params,
      });

      setMessages(response.data);
    } catch (error) {
      console.error("Error fetching messages:", error);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, [year, sort]);

  const handleShare = (token) => {
    const shareUrl = `${window.location.origin}/shared/${token}`;
    navigator.clipboard.writeText(shareUrl);
    setCopiedToken(token);
    setTimeout(() => setCopiedToken(null), 2000);
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>My Messages</h2>

      {/* Filter and Sort Controls */}
      <div className={styles.controls}>
        <input
          type="number"
          placeholder="Filter by Year"
          value={year}
          onChange={(e) => setYear(e.target.value)}
          className={styles.input}
        />

        <select
          value={sort}
          onChange={(e) => setSort(e.target.value)}
          className={styles.select}
        >
          <option value="desc">Newest First</option>
          <option value="asc">Oldest First</option>
        </select>
      </div>

      {/* Message List */}
      <div className={styles.messageGrid}>
        {messages.map((msg) => (
          <div key={msg.id} className={styles.messageCard}>
            <h4 className={styles.title}>{msg.title}</h4>
            <p className={styles.content}>{msg.content}</p>
            <small className={styles.date}>
              Created:{" "}
              {msg.createdAt
                ? new Date(msg.createdAt).toLocaleDateString()
                : "N/A"}
            </small>
            <br />
            <small className={styles.date}>
              Delivery:{" "}
              {msg.messageDateTime
                ? new Date(msg.messageDateTime).toLocaleDateString()
                : "N/A"}
            </small>

            {/* Share button */}
            <button
              className={styles.shareButton}
              onClick={() => handleShare(msg.shareToken)}
            >
              🔗 {copiedToken === msg.shareToken ? "Copied!" : "Share"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default MessagesPage;
