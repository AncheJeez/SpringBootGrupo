package com.dwes.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dwes.security.entities.Comida;
import com.dwes.security.entities.Prestamo;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.ComidaNotFoundException;
import com.dwes.security.repository.ComidaRepository;
import com.dwes.security.service.ComidaService;

import jakarta.validation.Valid;

@Service
public class ComidasServiceImpl implements ComidaService {

    @Autowired
    private ComidaRepository comidaRepository;

    @Override
    public Comida agregarComida(@Valid Comida comida) {
        // Validación a través de las anotaciones JPA/Bean Validation
        return comidaRepository.save(comida);
    }

    @Override
    public Page<Comida> listarTodosLosComidas(Pageable pageable) {
        return comidaRepository.findAll(pageable);
    }

    @Override
    public Comida obtenerComidaPorId(Long id) {
        return comidaRepository.findById(id)
                .orElseThrow(() -> new ComidaNotFoundException("Comida no encontrada"));
    }

    @Override
    public Comida actualizarComida(Long id, @Valid Comida detallesComida) {
        Comida comida = obtenerComidaPorId(id);
        comida.setNombre(detallesComida.getNombre());
        comida.setPaisOrigen(detallesComida.getPaisOrigen());
        return comidaRepository.save(comida);
    }

    @Override
    public void eliminarComida(Long id) {
        comidaRepository.deleteById(id);
    }

    /*
     * Métodos relacionados con préstamos y usuarios.
     * Dejamos los stubs similares a LibrosServiceImpl, puedes implementarlos
     * cuando definas la entidad Prestamo y la lógica de negocio.
     */
    @Override
    public Page<Comida> listarComidasPrestadosPorUsuario(Integer usuarioId, Pageable pageable) {
        // TODO: implementar si hay relación entre Prestamo y Comida
        return Page.empty();
    }

    @Override
    public Prestamo prestarComidaAPrestamo(Comida comida, Usuario usuario) {
        // TODO: crear un prestamo que vincule comida y usuario
        return null;
    }

    @Override
    public void devolverComida(Long prestamoId) {
        // TODO: marcar el préstamo como devuelto
    }
}
