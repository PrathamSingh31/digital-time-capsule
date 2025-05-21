import React from 'react';

function Dashboard() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Welcome to the Dashboard</h1>
      <p>This is a protected route only visible after login.</p>
    </div>
  );
}

export default Dashboard;
