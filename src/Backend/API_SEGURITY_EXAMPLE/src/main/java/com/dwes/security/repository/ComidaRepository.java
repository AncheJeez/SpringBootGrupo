package com.dwes.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dwes.security.entities.Comida;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Long> {
    // métodos adicionales (si hace falta) pueden ir aquí
}