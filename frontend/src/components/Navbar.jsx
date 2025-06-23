import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import styles from './Navbar.module.css';

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("jwt"); // or your auth method
    navigate("/login");
  };

  return (
    <nav className={styles.navbar}>
      <h1 className={styles.logo}>⏳ Digital Capsule</h1>
      <ul className={styles.navLinks}>
        <li><NavLink to="/dashboard" activeClassName={styles.active}>Dashboard</NavLink></li>
        <li><NavLink to="/create" activeClassName={styles.active}>Create</NavLink></li>
        <li><NavLink to="/import" activeClassName={styles.active}>Import</NavLink></li>
        <li><NavLink to="/export" activeClassName={styles.active}>Export</NavLink></li>
        <li><NavLink to="/profile" activeClassName={styles.active}>Profile</NavLink></li>
        <li><button onClick={handleLogout} className={styles.logoutBtn}>Logout</button></li>
      </ul>
    </nav>
  );
}
