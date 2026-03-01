import { Link } from 'react-router-dom'
import { getUser } from '../auth/auth'

export default function Sidebar(){
  const u = getUser()
  return (
    <aside className="sidebar">
      <h3>API Console</h3>
      <Link to="/libros">Libros</Link>
      {u?.roles?.includes('ROLE_ADMIN') && <Link to="/admin">Admin</Link>}
    </aside>
  )
}
