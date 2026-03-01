package com.dwes.security.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Comida;
import com.dwes.security.error.exception.ComidaNotFoundException;
import com.dwes.security.repository.ComidaRepository;

@ExtendWith(MockitoExtension.class)
class ComidasServiceImplTest {

    @Mock
    private ComidaRepository comidaRepository;

    @InjectMocks
    private ComidasServiceImpl comidasService;

    @Test
    void obtenerComidaPorId_siNoExiste_lanzaComidaNotFoundException() {
        long idInexistente = 999L;
        when(comidaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ComidaNotFoundException.class, () -> comidasService.obtenerComidaPorId(idInexistente));
    }

    @Test
    void agregarComida_guardaYDevuelveComida() {
        Comida entrada = new Comida("Tortilla", "España");

        Comida guardada = new Comida("Tortilla", "España");
        guardada.setId(1L);

        when(comidaRepository.save(entrada)).thenReturn(guardada);

        Comida resultado = comidasService.agregarComida(entrada);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tortilla", resultado.getNombre());
        assertEquals("España", resultado.getPaisOrigen());
    }

    @Test
    void actualizarComida_siExiste_actualizaCamposYGuarda() {
        long id = 10L;

        Comida existente = new Comida("Pizza", "Italia");
        existente.setId(id);

        Comida cambios = new Comida("Sushi", "Japón");

        // el service primero llama a findById
        when(comidaRepository.findById(id)).thenReturn(Optional.of(existente));
        // y luego a save(comidaActualizada)
        when(comidaRepository.save(existente)).thenReturn(existente);

        Comida resultado = comidasService.actualizarComida(id, cambios);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Sushi", resultado.getNombre());
        assertEquals("Japón", resultado.getPaisOrigen());

        verify(comidaRepository).save(existente);
    }

    @Test
    void actualizarComida_siNoExiste_lanzaComidaNotFoundException() {
        long idInexistente = 999L;
        Comida cambios = new Comida("Ramen", "Japón");

        when(comidaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ComidaNotFoundException.class, () -> comidasService.actualizarComida(idInexistente, cambios));
    }

    @Test
    void eliminarComida_llamaDeleteById() {
        long id = 5L;

        comidasService.eliminarComida(id);

        verify(comidaRepository).deleteById(id);
    }

    @Test
    void listarTodosLosComidas_devuelvePaginaDelRepositorio() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comida> pageEsperada = new PageImpl<>(
                List.of(new Comida("Paella", "España")),
                pageable,
                1
        );

        when(comidaRepository.findAll(pageable)).thenReturn(pageEsperada);

        Page<Comida> resultado = comidasService.listarTodosLosComidas(pageable);

        assertSame(pageEsperada, resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Paella", resultado.getContent().get(0).getNombre());
    }
}