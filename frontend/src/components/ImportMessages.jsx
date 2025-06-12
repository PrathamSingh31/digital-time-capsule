import React, { useState } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./ImportMessages.module.css";

const ImportMessages = () => {
  const [file, setFile] = useState(null);
  const [fileType, setFileType] = useState("json"); // default to JSON
  const [message, setMessage] = useState("");

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setMessage("");
  };

  const handleUpload = async () => {
    if (!file) {
      setMessage("❌ Please select a file.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("type", fileType); // example: 'json'

    try {
      await axiosPrivate.post("/api/user/messages/import", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      setMessage("✅ Messages imported successfully!");
    } catch (err) {
      console.error("Import error:", err);
      setMessage("❌ Import failed. Check file format or try again.");
    }
  };

  return (
    <div className={styles.importContainer}>
      <h3>📥 Import Messages (.json)</h3>
      <input type="file" accept=".json" onChange={handleFileChange} />
      <button onClick={handleUpload} className={styles.uploadButton}>
        Upload
      </button>
      {message && <p className={styles.message}>{message}</p>}

      <div className={styles.sampleBlock}>
        <h4>📝 Sample Format:</h4>
        <pre className={styles.codeBlock}>
{`[
  {
    "title": "Time Capsule 2025",
    "content": "Remember to smile!",
    "deliveryDate": "2025-12-31"
  },
  {
    "title": "New Year",
    "content": "Happy 2026!",
    "deliveryDate": "2026-01-01"
  }
]`}
        </pre>
        <a href="/sample-template.json" download className={styles.downloadLink}>
          📄 Download Sample Template
        </a>
      </div>
    </div>
  );
};

export default ImportMessages;
