import React, { useEffect, useState } from 'react';
import axiosPrivate from '../api/axiosPrivate';

export default function Dashboard() {
  const [messages, setMessages] = useState([]);
  const [editingMessageId, setEditingMessageId] = useState(null);
  const [editedTitle, setEditedTitle] = useState('');
  const [editedContent, setEditedContent] = useState('');
  const [editedDate, setEditedDate] = useState('');

  const fetchMessages = async () => {
    try {
      const response = await axiosPrivate.get('/user/messages');
      setMessages(response.data);
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axiosPrivate.delete(`/user/messages/${id}`);
      setMessages(messages.filter((msg) => msg.id !== id));
    } catch (error) {
      console.error('Error deleting message:', error);
    }
  };

  const handleEdit = (id, title, content, deliveryDate) => {
    setEditingMessageId(id);
    setEditedTitle(title);
    setEditedContent(content);
    setEditedDate(deliveryDate);
  };

  const handleUpdate = async () => {
    try {
      await axiosPrivate.put(`/user/messages/${editingMessageId}`, {
        title: editedTitle,
        content: editedContent,
        deliveryDate: editedDate,
      });
      setEditingMessageId(null);
      setEditedTitle('');
      setEditedContent('');
      setEditedDate('');
      fetchMessages(); // reload after update
    } catch (error) {
      console.error('Error updating message:', error);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, []);

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Your Messages</h2>
      <ul className="space-y-4">
        {messages.map((msg) => (
          <li key={msg.id} className="bg-gray-100 p-4 rounded shadow">
            {editingMessageId === msg.id ? (
              <>
                <input
                  type="text"
                  value={editedTitle}
                  onChange={(e) => setEditedTitle(e.target.value)}
                  className="w-full p-2 border rounded mb-2"
                  placeholder="Title"
                />
                <textarea
                  value={editedContent}
                  onChange={(e) => setEditedContent(e.target.value)}
                  className="w-full p-2 border rounded mb-2"
                  placeholder="Content"
                />
                <input
                  type="date"
                  value={editedDate}
                  onChange={(e) => setEditedDate(e.target.value)}
                  className="w-full p-2 border rounded mb-2"
                />
                <button
                  onClick={handleUpdate}
                  className="px-4 py-1 bg-blue-600 text-white rounded"
                >
                  Save
                </button>
              </>
            ) : (
              <>
                <h3 className="font-semibold">{msg.title}</h3>
                <p>{msg.content}</p>
                <p className="text-sm text-gray-600">Deliver on: {msg.deliveryDate}</p>
                <div className="mt-2 space-x-2">
                  <button
                    onClick={() => handleEdit(msg.id, msg.title, msg.content, msg.deliveryDate)}
                    className="px-3 py-1 bg-yellow-500 text-white rounded"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(msg.id)}
                    className="px-3 py-1 bg-red-600 text-white rounded"
                  >
                    Delete
                  </button>
                </div>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
