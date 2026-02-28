package com.dwes.security.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dwes.security.controller.user.AuthorizationAdminController;
import com.dwes.security.entities.Comida;
import com.dwes.security.service.ComidaService;

	@RestController
	@RequestMapping("/api/v1/comidas")
	public class ComidasController {

    	private static final Logger logger = LoggerFactory.getLogger(ComidasController.class);

	    @Autowired
	    private ComidaService comidaService;

	    // Endpoint para obtener un listado de comidas, accesible solo por ROLE_USER
	    @GetMapping
	    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	    public ResponseEntity<Page<Comida>> listarTodosLosComidas(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        
	        logger.info("ComidasController :: listarTodosLosComidas");
	        Pageable pageable = PageRequest.of(page, size);
	        return new ResponseEntity<>(comidaService.listarTodosLosComidas(pageable), HttpStatus.OK);
	    }
	    
	 // Leer un comida por ID
	    @GetMapping("/{id}")
	    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	    public Comida getComidaById(@PathVariable Long id) {
	        return comidaService.obtenerComidaPorId(id);
	    }

	    // CRUD endpoints, accesibles solo por ROLE_ADMIN
	    // Crear un nuevo comida
	    @PostMapping
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public Comida createComida(@RequestBody Comida comida) {
	        return comidaService.agregarComida(comida);
	    }

	    

	    // Actualizar un comida
	    @PutMapping("/{id}")
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public Comida updateComida(@PathVariable Long id, @RequestBody Comida comidaDetails) {
	        return comidaService.actualizarComida(id, comidaDetails);
	    }

	    // Eliminar un comida
	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public void deleteComida(@PathVariable Long id) {
	        comidaService.eliminarComida(id);
	    }
	}