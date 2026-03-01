import React, { useEffect, useState } from 'react';
import '../App.css';

const MenuList = () => {
  const [comidas, setComidas] = useState([]);
  const token = localStorage.getItem('token'); 

  const cargarComidas = async () => {
    try {
      const response = await fetch('http://localhost:9091/api/v1/comidas', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      const data = await response.json();
      setComidas(data.content || []); 
    } catch (error) {
      console.error("Error cargando platos:", error);
    }
  };

  const eliminarPlato = async (id) => {
    if (!window.confirm("¿Seguro que quieres eliminar este plato?")) return;
    try {
      await fetch(`http://localhost:9091/api/v1/comidas/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      cargarComidas(); // Recargar lista
    } catch (error) {
      console.error("Error al eliminar:", error);
    }
  };

  useEffect(() => { cargarComidas(); }, []);

  return (
    <div className="admin-layout fade-in">
      {/* Sidebar lateral estilo industrial */}
      <aside className="sidebar">
        <div className="sidebar-header">FOOD<span>ADMIN</span></div>
        <nav className="sidebar-nav">
          <div className="nav-item active">🍴 Gestión Menú</div>
          <div className="nav-item">📊 Estadísticas</div>
          <div className="nav-item">👥 Usuarios</div>
        </nav>
      </aside>

      <main className="main-content">
        <header className="content-header">
          <h1 style={{ fontFamily: 'var(--serif)' }}>Panel de Comidas</h1>
          <p className="form-text">Listado oficial del restaurante</p>
        </header>

        <div className="console-panel">
          <table className="custom-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>NOMBRE DEL PLATO</th>
                <th>PAÍS ORIGEN</th>
                <th>ACCIONES</th>
              </tr>
            </thead>
            <tbody>
              {comidas.map((c) => (
                <tr key={c.id}>
                  <td style={{ fontFamily: 'var(--mono)', fontSize: '0.8rem' }}>#{c.id}</td>
                  <td style={{ fontWeight: '600' }}>{c.nombre}</td>
                  <td><span className="badge-origin">{c.paisOrigen}</span></td>
                  <td>
                    <button onClick={() => eliminarPlato(c.id)} className="btn-delete">Eliminar</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </main>
    </div>
  );
};

export default MenuList;