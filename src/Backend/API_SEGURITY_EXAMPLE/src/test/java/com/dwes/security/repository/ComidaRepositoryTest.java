package com.dwes.security.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.dwes.security.entities.Comida;

@ActiveProfiles("test")
@DataJpaTest
class ComidaRepositoryTest {

    @Autowired
    private ComidaRepository comidaRepository;

    @Test
    void save_persisteYGeneraId() {
        Comida comida = new Comida("Paella", "España");

        Comida guardada = comidaRepository.save(comida);

        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getNombre()).isEqualTo("Paella");
        assertThat(guardada.getPaisOrigen()).isEqualTo("España");
    }

    @Test
    void findById_recuperaEntidadGuardada() {
        Comida comida = new Comida("Sushi", "Japón");
        Comida guardada = comidaRepository.save(comida);

        Optional<Comida> encontrada = comidaRepository.findById(guardada.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNombre()).isEqualTo("Sushi");
        assertThat(encontrada.get().getPaisOrigen()).isEqualTo("Japón");
    }
}