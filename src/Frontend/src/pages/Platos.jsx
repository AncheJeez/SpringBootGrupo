import { useEffect, useState } from 'react'
import api from '../api/api'
import Layout from '../layout/Layout'

export default function Libros(){
  const [rows,setRows]=useState([])
  useEffect(()=>{ api.get('/libros').then(r=>setRows(r.data)) },[])
  return (
    <Layout>
      <h2>Libros</h2>
      <table>
        <thead><tr><th>ID</th><th>Título</th></tr></thead>
        <tbody>
          {rows.map(r=><tr key={r.id}><td>{r.id}</td><td>{r.titulo}</td></tr>)}
        </tbody>
      </table>
    </Layout>
  )
}
