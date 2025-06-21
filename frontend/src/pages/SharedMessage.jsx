import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../api/axiosPublic";
import styles from "./SharedMessage.module.css";

export default function SharedMessage() {
  const { token } = useParams();
  const [message, setMessage] = useState(null);
  const [error, setError] = useState("");
  const [revealed, setRevealed] = useState(false);

  useEffect(() => {
    const fetchSharedMessage = async () => {
      try {
        const response = await axios.get(`/api/user/messages/share/${token}`);
        setMessage(response.data);
      } catch (err) {
        console.error(err);
        setError("Invalid or expired share link.");
      }
    };
    fetchSharedMessage();
  }, [token]);

  if (error) return <p className={styles.error}>{error}</p>;
  if (!message) return <p className={styles.loading}>Loading...</p>;

  const messageDate = new Date(message.date);
  const now = new Date();
  const isFuture = messageDate > now;

  const daysRemaining = Math.ceil(
    (messageDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24)
  );

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{message.title}</h2>

      {isFuture && !revealed ? (
        <div className={styles.lockedMessage}>
          <p className={styles.lockedText}>
            🔒 This message is locked until{" "}
            {messageDate.toLocaleDateString()} ({daysRemaining} day
            {daysRemaining > 1 ? "s" : ""} remaining)
          </p>
          <button
            className={styles.revealButton}
            onClick={() => setRevealed(true)}
          >
            🔓 Reveal Now
          </button>

          <div className={styles.blurredPreview}>
            <p className={styles.blurredText}>{message.content}</p>

            {message.imageUrl && (
              <img
                src={`http://localhost:8080${message.imageUrl}`}
                alt="Blurred Preview"
                className={`${styles.image} ${styles.blurred}`}
              />
            )}

            {message.videoUrl && (
              <video
                className={`${styles.video} ${styles.blurred}`}
                muted
                autoPlay
                loop
              >
                <source
                  src={`http://localhost:8080${message.videoUrl}`}
                  type="video/mp4"
                />
              </video>
            )}
          </div>
        </div>
      ) : (
        <div className={styles.revealedContent}>
          <p className={styles.content}>{message.content}</p>

          {message.imageUrl ? (
            <img
              src={`http://localhost:8080${message.imageUrl}`}
              alt="Uploaded message visual"
              className={styles.image}
            />
          ) : (
            <p className={styles.noImage}>📁 No image attached</p>
          )}

          {message.videoUrl ? (
            <video controls className={styles.video}>
              <source
                src={`http://localhost:8080${message.videoUrl}`}
                type="video/mp4"
              />
              Your browser does not support the video tag.
            </video>
          ) : (
            <p className={styles.noVideo}>📁 No video attached</p>
          )}
        </div>
      )}

      <p className={styles.date}>
        🗓️ Scheduled for:{" "}
        {message.date ? messageDate.toLocaleDateString() : "N/A"}
      </p>
    </div>
  );
}
