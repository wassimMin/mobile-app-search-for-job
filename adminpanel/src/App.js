import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React from 'react';
import LoginPage from './pages/LoginPage';
import ControlePanel from './pages/ControlePanel';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path='/ControlePanel' element={<ControlePanel/>}/>
      </Routes>
    </Router>
  );
}

export default App;
