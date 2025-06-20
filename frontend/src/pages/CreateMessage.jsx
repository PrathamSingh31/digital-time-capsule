import React, { useState } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./CreateMessage.module.css";

const CreateMessage = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [deliveryDate, setDeliveryDate] = useState("");
  const [image, setImage] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const isFutureOrTodayDate = (date) => {
    const today = new Date().toISOString().split("T")[0];
    return date >= today;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMessage("");
    setErrorMessage("");
    setLoading(true);

    const trimmedTitle = title.trim();
    const trimmedContent = content.trim();

    if (!trimmedTitle || !trimmedContent || !deliveryDate) {
      setErrorMessage("❌ All fields are required.");
      setLoading(false);
      return;
    }

    if (!isFutureOrTodayDate(deliveryDate)) {
      setErrorMessage("❌ Delivery date must be today or a future date.");
      setLoading(false);
      return;
    }

    try {
      const formData = new FormData();
      formData.append("title", trimmedTitle);
      formData.append("content", trimmedContent);
      formData.append("deliveryDate", deliveryDate);
      if (image) {
        formData.append("image", image);
      }

      const response = await axiosPrivate.post("/api/user/messages/upload-image", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });


      setSuccessMessage("✅ Message saved successfully!");
      setTitle("");
      setContent("");
      setDeliveryDate("");
      setImage(null);
      console.log("Message saved:", response.data);
    } catch (error) {
      console.error("Error saving message:", error);
     const serverMessage =
       typeof error?.response?.data === "string"
         ? error.response.data
         : error?.response?.data?.message || "❌ Failed to save message. Please try again.";
     setErrorMessage(serverMessage);

    } finally {
      setLoading(false);
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
        <input
          type="file"
          accept="image/*"
          onChange={(e) => setImage(e.target.files[0])}
          className={styles.input}
        />
        <button type="submit" className={styles.button} disabled={loading}>
          {loading ? "Saving..." : "Save Message"}
        </button>
      </form>

      {successMessage && <p className={styles.success}>{successMessage}</p>}
      {errorMessage && <p className={styles.error}>{errorMessage}</p>}
    </div>
  );
};

export default CreateMessage;
