import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [message, setMessage] = useState("");
  const [unlockDate, setUnlockDate] = useState("");
  const [messages, setMessages] = useState([]);
  const [username, setUsername] = useState("");
  const navigate = useNavigate();

  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    axios
      .get("http://localhost:8080/api/user/profile", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setUsername(res.data.username);
      })
      .catch((err) => {
        console.error("Error fetching profile:", err);
        navigate("/login");
      });

    axios
      .get("http://localhost:8080/api/user/messages", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setMessages(res.data);
      })
      .catch((err) => {
        console.error("Error fetching messages:", err);
      });
  }, [token, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await axios.post(
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

      alert("Message successfully saved!");
      setMessage("");
      setUnlockDate("");

      // Refresh messages
      const res = await axios.get("http://localhost:8080/api/user/messages", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMessages(res.data);
    } catch (error) {
      alert("Failed to save message");
      console.error("Save error:", error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div className="container mx-auto p-4">
      <h2 className="text-2xl font-bold mb-4">Welcome, {username}!</h2>

      <form onSubmit={handleSubmit} className="mb-6">
        <div className="mb-2">
          <label className="block font-medium">Your Message</label>
          <textarea
            className="w-full p-2 border rounded"
            rows="4"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label className="block font-medium">Unlock Date</label>
          <input
            type="date"
            className="w-full p-2 border rounded"
            value={unlockDate}
            onChange={(e) => setUnlockDate(e.target.value)}
            required
          />
        </div>
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Save Message
        </button>
      </form>

      <div className="mb-4">
        <h3 className="text-xl font-semibold mb-2">Your Messages</h3>
        {messages.length > 0 ? (
          <ul className="space-y-2">
            {messages.map((msg, index) => (
              <li
                key={index}
                className="border p-3 rounded shadow-sm bg-gray-50"
              >
                <p className="text-gray-800">{msg.message}</p>
                <small className="text-gray-500">
                  Unlocks on: {msg.unlockDate}
                </small>
              </li>
            ))}
          </ul>
        ) : (
          <p>No messages yet.</p>
        )}
      </div>

      <button
        onClick={handleLogout}
        className="mt-4 bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
      >
        Logout
      </button>
    </div>
  );
};

export default Dashboard;
