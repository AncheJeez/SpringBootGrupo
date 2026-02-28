package com.dwes.security.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Prestamo {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Otros campos y anotaciones
	
}
