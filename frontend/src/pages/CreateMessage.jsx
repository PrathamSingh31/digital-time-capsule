import { useState } from "react";

export default function CreateMessage() {
  const [message, setMessage] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    // TODO: Replace with actual API call to save the message
    console.log("Message submitted:", message);

    // Clear the form
    setMessage("");
  };

  return (
    <div className="p-6 max-w-md mx-auto bg-white rounded-xl shadow-md space-y-4">
      <h1 className="text-xl font-bold">Create a New Message</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <textarea
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Write your message here..."
          className="w-full p-2 border border-gray-300 rounded-md"
          rows={6}
          required
        />
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Submit
        </button>
      </form>
    </div>
  );
}
