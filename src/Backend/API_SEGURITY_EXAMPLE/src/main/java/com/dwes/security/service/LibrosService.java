package com.dwes.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Prestamo;
import com.dwes.security.entities.Usuario;

public interface LibrosService {

    Libro agregarLibro(Libro libro);

    Page<Libro> listarTodosLosLibros(Pageable pageable);

    Libro obtenerLibroPorId(Long id);

    Libro actualizarLibro(Long id, Libro libro);

    void eliminarLibro(Long id);

    Page<Libro> listarLibrosPrestadosPorUsuario(Integer usuarioId, Pageable pageable);

    Prestamo prestarLibroAPrestamo(Libro libro, Usuario usuario);

    void devolverLibro(Long prestamoId);

   
}