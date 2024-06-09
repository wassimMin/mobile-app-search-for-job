import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React from 'react';
import LoginPage from './pages/LoginPage';
import ControlePanel from './pages/ControlePanel';
import AddUserPage from './pages/AddUserPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path='/ControlePanel' element={<ControlePanel/>}/>
        <Route path="/AddUserPage" element={<AddUserPage />} />
      </Routes>
    </Router>
  );
}

export default App;
