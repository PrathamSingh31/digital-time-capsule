import { useState } from "react";
import axios from "axios";

export default function Dashboard() {
  const [message, setMessage] = useState("");
  const [unlockDate, setUnlockDate] = useState("");
  const [status, setStatus] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/api/user/create",
        {
          message,
          unlockDate,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setStatus("✅ Message saved successfully!");
      setMessage("");
      setUnlockDate("");
    } catch (error) {
      console.error("Error submitting message:", error);
      setStatus("❌ Failed to save message.");
    }
  };

  return (
    <div className="p-4 max-w-xl mx-auto mt-10 bg-white shadow-lg rounded-lg">
      <h2 className="text-2xl font-bold mb-4 text-center">Create Time Capsule</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <textarea
          placeholder="Enter your message..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          required
          className="w-full p-2 border rounded"
        />
        <input
          type="date"
          value={unlockDate}
          onChange={(e) => setUnlockDate(e.target.value)}
          required
          className="w-full p-2 border rounded"
        />
        <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
        >
          Save Message
        </button>
      </form>
      {status && <p className="mt-4 text-center text-sm text-gray-700">{status}</p>}
    </div>
  );
}
