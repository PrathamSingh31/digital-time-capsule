import React, { useState } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./CreateMessage.module.css";

const CreateMessage = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [deliveryDate, setDeliveryDate] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMessage("");
    setErrorMessage("");

    try {
      const response = await axiosPrivate.post("/api/user/messages", {
        title,
        content,
        deliveryDate,
      });

      setSuccessMessage("✅ Message saved successfully!");
      setTitle("");
      setContent("");
      setDeliveryDate("");
      console.log("Message saved:", response.data);
    } catch (error) {
      console.error("Error saving message:", error);
      setErrorMessage("❌ Failed to save message. Please try again.");
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Create Time Capsule Message</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className={styles.input}
          required
        />
        <textarea
          placeholder="Message Content"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className={styles.textarea}
          rows={4}
          required
        />
        <input
          type="date"
          value={deliveryDate}
          onChange={(e) => setDeliveryDate(e.target.value)}
          className={styles.input}
          required
        />
        <button type="submit" className={styles.button}>
          Save Message
        </button>
      </form>

      {successMessage && <p className={styles.success}>{successMessage}</p>}
      {errorMessage && <p className={styles.error}>{errorMessage}</p>}
    </div>
  );
};

export default CreateMessage;
