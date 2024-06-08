import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React from 'react';
import LoginPage from './pages/LoginPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
      </Routes>
    </Router>
  );
}

export default App;
