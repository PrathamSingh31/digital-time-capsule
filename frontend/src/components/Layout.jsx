// src/components/Layout.jsx
import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import styles from './Layout.module.css';

export default function Layout() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('jwt');
    navigate('/login');
  };

  return (
    <div className={styles.pageWrapper}>
      <header className={styles.header}>
        <div className={styles.logo}>⏳ Time Capsule</div>
        <nav className={styles.nav}>
          <Link to="/dashboard" className={styles.navLink}>Dashboard</Link>
          <Link to="/create" className={styles.navLink}>Create</Link>
          <Link to="/import" className={styles.navLink}>Import</Link>
          <Link to="/export" className={styles.navLink}>Export</Link>
          <Link to="/profile" className={styles.navLink}>Profile</Link>
          <button onClick={handleLogout} className={styles.logoutButton}>Logout</button>
        </nav>
      </header>

      <main className={styles.main}>
        <Outlet />
      </main>

      <footer className={styles.footer}>
        © {new Date().getFullYear()} Digital Time Capsule
      </footer>
    </div>
  );
}
