import React, { useEffect, useState } from "react";
import axios from "axios";

function MessagesPage() {
  const [messages, setMessages] = useState([]);
  const [year, setYear] = useState("");
  const [sort, setSort] = useState("desc");

  const fetchMessages = async () => {
    try {
      const token = localStorage.getItem("token");
      const params = {};
      if (year) params.year = year;
      if (sort) params.sort = sort;

      const response = await axios.get("http://localhost:8080/api/user/messages", {
        headers: { Authorization: `Bearer ${token}` },
        params: params
      });

      setMessages(response.data);
    } catch (error) {
      console.error("Error fetching messages:", error);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, [year, sort]);

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Messages</h2>

      {/* Filter and Sort Controls */}
      <div className="flex gap-4 mb-4">
        <input
          type="number"
          placeholder="Filter by Year"
          value={year}
          onChange={(e) => setYear(e.target.value)}
          className="border p-2 rounded"
        />

        <select value={sort} onChange={(e) => setSort(e.target.value)} className="border p-2 rounded">
          <option value="desc">Newest First</option>
          <option value="asc">Oldest First</option>
        </select>
      </div>

      {/* Message List */}
      <div className="grid gap-4">
        {messages.map((msg) => (
          <div key={msg.id} className="border p-4 rounded shadow">
            <h4 className="text-lg font-semibold">{msg.title}</h4>
            <p>{msg.content}</p>
            <small>Created: {new Date(msg.createdAt).toLocaleDateString()}</small><br />
            <small>Delivery: {new Date(msg.deliveryDate).toLocaleDateString()}</small>
          </div>
        ))}
      </div>
    </div>
  );
}

export default MessagesPage;
