import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import MenuList from './pages/MenuList'; // <--- IMPORTANTE
import { getUser } from './auth/auth';

// Componente para proteger rutas
const Private = ({ children }) => {
  const u = getUser();
  return u ? children : <Navigate to="/" />;
};

export default function App() {
  return (
    <BrowserRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/menu" element={<Private><MenuList /></Private>} />
      </Routes>
    </BrowserRouter>
  );
}