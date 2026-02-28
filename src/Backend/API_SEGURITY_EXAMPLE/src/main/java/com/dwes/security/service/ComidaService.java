package com.dwes.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Usuario;
import com.dwes.security.entities.Comida;
import com.dwes.security.entities.Prestamo;

public interface ComidaService {

    Comida agregarComida(Comida comida);

    Page<Comida> listarTodosLosComidas(Pageable pageable);

    Comida obtenerComidaPorId(Long id);

    Comida actualizarComida(Long id, Comida comida);

    void eliminarComida(Long id);

    Page<Comida> listarComidasPrestadosPorUsuario(Integer usuarioId, Pageable pageable);

    Prestamo prestarComidaAPrestamo(Comida comida, Usuario usuario);

    void devolverComida(Long prestamoId);

}
