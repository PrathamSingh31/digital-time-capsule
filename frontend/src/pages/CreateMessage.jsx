import React, { useState } from "react";
import axios from "axios";

const CreateMessage = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [deliveryDate, setDeliveryDate] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/api/user/messages",
        {
          title,
          content,
          deliveryDate,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      setSuccessMessage("Message saved successfully!");
      setTitle("");
      setContent("");
      setDeliveryDate("");
      console.log("Message saved:", response.data);
    } catch (error) {
      console.error("Error saving message:", error);
      setErrorMessage("Failed to save message. Please try again.");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-4 border rounded shadow">
      <h2 className="text-xl font-bold mb-4">Create Time Capsule Message</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="w-full p-2 border rounded"
          required
        />
        <textarea
          placeholder="Message Content"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className="w-full p-2 border rounded"
          rows={4}
          required
        />
        <input
          type="date"
          value={deliveryDate}
          onChange={(e) => setDeliveryDate(e.target.value)}
          className="w-full p-2 border rounded"
          required
        />
        <button
          type="submit"
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
        >
          Save Message
        </button>
      </form>

      {successMessage && (
        <p className="text-green-600 mt-4">{successMessage}</p>
      )}
      {errorMessage && (
        <p className="text-red-600 mt-4">{errorMessage}</p>
      )}
    </div>
  );
};

export default CreateMessage;
