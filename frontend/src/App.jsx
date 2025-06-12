// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Profile from './pages/Profile';
import CreateMessage from './pages/CreateMessage';
import Layout from './components/Layout';
import ImportMessages from './components/ImportMessages';
import ExportMessages from './components/ExportMessages';
import ScheduledMessages from './components/ScheduledMessages';
function PrivateRoute({ children }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" />;
}

export default function App() {
  return (
    <Router>
      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/import" element={<ImportMessages />} />
        <Route path="/export" element={<ExportMessages />} />
        <Route path="/scheduled" element={<ScheduledMessages />} />

        {/* Protected layout with Navbar */}
        <Route
          path="/"
          element={
            <PrivateRoute>
              <Layout />
            </PrivateRoute>
          }
        >
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="profile" element={<Profile />} />
          <Route path="create" element={<CreateMessage />} />
        </Route>

        {/* Redirect anything else to login */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}
