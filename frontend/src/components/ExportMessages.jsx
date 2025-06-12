import React, { useState } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./ExportMessages.module.css";

const ExportMessages = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleExport = async () => {
    setLoading(true);
    setError("");
    setSuccess("");
    try {
      console.log("Authorization header sent:", localStorage.getItem("token"));

      const response = await axiosPrivate.get("/api/user/messages/export", {
        responseType: "blob",
      });

      const blob = new Blob([response.data], { type: "application/json" });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "exported-messages.json";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);

      setSuccess("✅ Export successful!");
    } catch (error) {
      console.error("Export error:", error);
      setError("❌ Failed to export messages. Please login or try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <button
        onClick={handleExport}
        disabled={loading}
        className={styles.button}
      >
        {loading ? "Exporting..." : "📤 Export Messages as JSON"}
      </button>
      {error && <p className={styles.errorMessage}>{error}</p>}
      {success && <p className={styles.successMessage}>{success}</p>}
    </div>
  );
};

export default ExportMessages;
