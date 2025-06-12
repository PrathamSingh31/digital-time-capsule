import React from "react";
import { Link, useNavigate } from "react-router-dom";
import styles from "./Navbar.module.css";

export default function Navbar() {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem("token");

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav className={styles.navbar}>
      <h1 className={styles.logo}>Digital Time Capsule</h1>
      <div className={styles.navLinks}>
        {isAuthenticated ? (
          <>
            <Link to="/dashboard" className={styles.link}>Dashboard</Link>
            <Link to="/create" className={styles.link}>Create</Link>
            <Link to="/profile" className={styles.link}>Profile</Link>
            <Link to="/import" className={styles.link}>Import</Link> {/* ✅ Import Link */}
            <Link to="/export" className={styles.link}>Export</Link>
            <Link to="/scheduled" className={styles.link}>Scheduled</Link>

            <button onClick={handleLogout} className={styles.logoutBtn}>
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className={styles.link}>Login</Link>
            <Link to="/register" className={styles.link}>Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}
