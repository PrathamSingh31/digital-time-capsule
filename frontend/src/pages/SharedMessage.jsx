import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../api/axiosPublic"; // 👈 this one has no auth header
import styles from "./SharedMessage.module.css";

export default function SharedMessage() {
  const { token } = useParams();
  const [message, setMessage] = useState(null);
  const [error, setError] = useState("");

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

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{message.title}</h2>
      <p className={styles.content}>{message.content}</p>
      <p className={styles.date}>
        🗓️ Scheduled for:{" "}
        {new Date(message.date).toLocaleDateString()}
      </p>
    </div>
  );
}
