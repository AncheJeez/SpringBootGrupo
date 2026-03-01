import React, { useState } from 'react';
import '../App.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Aquí conectas con tu backend de comida
    console.log("Login en FoodApp:", { email, password });
  };

  return (
    <div className="login-page-wrapper">
      <div className="login-card">
        <div className="login-header">
          <h2>FoodApp Admin</h2>
          <p>Gestión de menú y pedidos</p>
        </div>

        <form className="login-body" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">API Restaurante URL</label>
            <input 
              className="form-control" 
              defaultValue="http://localhost:9091" 
              readOnly
            />
            <span className="form-text">Endpoint actual del backend</span>
          </div>

          <div className="form-group">
            <label className="form-label">Email del Personal</label>
            <input 
              className="form-control" 
              type="email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="chef@restaurante.com"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Contraseña</label>
            <input 
              className="form-control" 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <span className="form-text">
              Demo Chef: <span style={{ fontFamily: 'var(--mono)' }}>password123</span>
            </span>
          </div>

          <button type="submit" className="btn-primary">
            Entrar al Sistema
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;