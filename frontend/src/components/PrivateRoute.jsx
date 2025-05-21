// src/components/PrivateRoute.jsx

import { Navigate, Outlet } from "react-router-dom";

export default function PrivateRoute() {
  const isAuthenticated = localStorage.getItem("token") !== null;

  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
}
