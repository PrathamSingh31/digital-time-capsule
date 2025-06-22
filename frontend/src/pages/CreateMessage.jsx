import React, { useState } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./CreateMessage.module.css";

export default function CreateMessage() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [deliveryDate, setDeliveryDate] = useState("");
  const [unlockDate, setUnlockDate] = useState(""); // ✅ New state
  const [imageFile, setImageFile] = useState(null);
  const [videoFile, setVideoFile] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      const formData = new FormData();
      formData.append("title", title);
      formData.append("content", content);
      formData.append("deliveryDate", deliveryDate);
      if (unlockDate) formData.append("unlockDate", unlockDate); // ✅ Append unlock date
      if (imageFile) formData.append("image", imageFile);
      if (videoFile) formData.append("video", videoFile);

      await axiosPrivate.post("/api/user/messages/upload-media", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setMessage("Message saved successfully!");
      setTitle("");
      setContent("");
      setDeliveryDate("");
      setUnlockDate("");
      setImageFile(null);
      setVideoFile(null);
    } catch (err) {
      console.error(err);
      setError(err?.response?.data || "Failed to create message.");
    }
  };

  return (
    <div className={styles.container}>
      <h2>Create a New Message</h2>
      <form className={styles.form} onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />

        <textarea
          placeholder="Message Content"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />

        <label>Delivery Date:</label>
        <input
          type="date"
          value={deliveryDate}
          onChange={(e) => setDeliveryDate(e.target.value)}
          required
        />

        <label>Unlock Date (Optional):</label>
        <input
          type="date"
          value={unlockDate}
          onChange={(e) => setUnlockDate(e.target.value)}
        />

        <label>Attach Image:</label>
        <input
          type="file"
          accept="image/*"
          onChange={(e) => setImageFile(e.target.files[0])}
        />
        {imageFile && (
          <img
            src={URL.createObjectURL(imageFile)}
            alt="Preview"
            className={styles.preview}
          />
        )}

        <label>Attach Video:</label>
        <input
          type="file"
          accept="video/*"
          onChange={(e) => setVideoFile(e.target.files[0])}
        />
        {videoFile && (
          <video controls className={styles.preview}>
            <source src={URL.createObjectURL(videoFile)} type={videoFile.type} />
            Your browser does not support the video tag.
          </video>
        )}

        <button type="submit">Save Message</button>
      </form>

      {message && <p className={styles.success}>{message}</p>}
      {error && <p className={styles.error}>{error}</p>}
    </div>
  );
}
