import React, { useEffect, useState } from 'react';

function Dashboard() {
  const [messages, setMessages] = useState([]);
  const [messageText, setMessageText] = useState('');
  const [unlockDate, setUnlockDate] = useState('');

  // Fetch all messages on component mount
  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/user/messages', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });

        if (!response.ok) throw new Error('Failed to fetch messages');

        const data = await response.json();
        setMessages(data);
      } catch (error) {
        console.error('Error fetching messages:', error);
      }
    };

    fetchMessages();
  }, []);

  // Add new message handler
  const handleAddMessage = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/user/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({
          message: messageText,
          unlockDate,
        }),
      });

      if (!response.ok) throw new Error('Failed to save message');

      alert('Message was successfully saved');

      // Clear inputs
      setMessageText('');
      setUnlockDate('');

      // Refresh messages list
      const updatedMessagesResponse = await fetch('http://localhost:8080/api/user/messages', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!updatedMessagesResponse.ok) throw new Error('Failed to fetch messages');
      const updatedMessages = await updatedMessagesResponse.json();
      setMessages(updatedMessages);

    } catch (error) {
      alert('Failed to save message');
      console.error(error);
    }
  };

  return (
    <div className="dashboard">
      <h2>Dashboard</h2>

      <div className="add-message">
        <h3>Add New Message</h3>
        <textarea
          placeholder="Enter your message"
          value={messageText}
          onChange={(e) => setMessageText(e.target.value)}
        />
        <input
          type="date"
          value={unlockDate}
          onChange={(e) => setUnlockDate(e.target.value)}
        />
        <button onClick={handleAddMessage}>Save Message</button>
      </div>

      <div className="messages-list">
        <h3>Your Saved Messages</h3>
        {messages.length === 0 ? (
          <p>No messages found.</p>
        ) : (
          <ul>
            {messages.map((msg) => (
              <li key={msg.id}>
                <p>{msg.message}</p>
                <small>Unlock Date: {msg.unlockDate}</small>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
