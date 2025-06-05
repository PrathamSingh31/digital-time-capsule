// src/components/Layout.jsx
import React from 'react';
import Navbar from './Navbar';
import { Outlet } from 'react-router-dom';

export default function Layout() {
  return (
    <>
      <Navbar /> {/* ✅ Hook useNavigate is now within <Router> */}
      <main className="p-4">
        <Outlet />
      </main>
    </>
  );
}
