package com.dwes.security.config;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Role;
import com.dwes.security.entities.Usuario; 
import com.dwes.security.repository.LibroRepository;
import com.dwes.security.repository.UserRepository;
import com.github.javafaker.Faker;

/**
 * Inicializador de datos de demostración para el perfil 'demo'.
 * Crea usuarios y libros de prueba automáticamente al iniciar la aplicación.
 */
@Profile("demo")
@Component
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Variables de configuración
    private static final boolean BORRAR_DATOS_EXISTENTES = true;
    private static final int CANTIDAD_LIBROS_DEMO = 10;

    @Override
    public void run(String... args) throws Exception {
        log.info("========================================");
        log.info("INICIALIZANDO DATOS DE DEMOSTRACIÓN");
        log.info("========================================");

        // Inicializar libros
        inicializarLibros();

        // Inicializar usuarios
        inicializarUsuarios();

        log.info("========================================");
        log.info("DATOS DE DEMOSTRACIÓN CARGADOS");
        log.info("Total usuarios: {}", usuarioRepository.count());
        log.info("Total libros: {}", libroRepository.count());
        log.info("========================================");
    }

    /**
     * Inicializa la base de datos con libros de prueba usando JavaFaker.
     */
    private void inicializarLibros() {
        try {
            if (BORRAR_DATOS_EXISTENTES) {
                libroRepository.deleteAll();
                log.info("✓ Libros existentes eliminados");
            }

            Faker faker = new Faker(new Locale("es"));
            
            for (int i = 0; i < CANTIDAD_LIBROS_DEMO; i++) {
            	
                Libro libro = new Libro();
                libro.setTitulo(faker.book().title());
                libro.setAutor(faker.book().author());
                libro.setIsbn(faker.code().isbn10()); // ISBN-10 válido
                
                libroRepository.save(libro);
            }
            
            log.info("✓ {} libros de demostración creados", CANTIDAD_LIBROS_DEMO);
            
        } catch (Exception e) {
            log.error("✗ Error al inicializar libros: {}", e.getMessage(), e);
        }
    }

    /**
     * Inicializa la base de datos con usuarios de prueba.
     */
    private void inicializarUsuarios() {
        try {
            // Verificar si ya existen usuarios
            if (usuarioRepository.count() > 0) {
                log.info("⚠ La base de datos ya contiene usuarios. Omitiendo creación de usuarios demo.");
                return;
            }

            // Usuario 1 - Rol USER
            Usuario usuario1 = new Usuario();
            usuario1.setFirstName("Alice");
            usuario1.setLastName("Johnson");
            usuario1.setEmail("alice.johnson@example.com");
            usuario1.setPassword(passwordEncoder.encode("password123"));
            usuario1.getRoles().add(Role.ROLE_USER);
            usuarioRepository.save(usuario1);
            log.info("✓ Usuario USER creado: {} ({})", usuario1.getEmail(), "password123");

            // Usuario 2 - Rol ADMIN
            Usuario usuario2 = new Usuario();
            usuario2.setFirstName("Bob");
            usuario2.setLastName("Smith");
            usuario2.setEmail("bob.smith@example.com");
            usuario2.setPassword(passwordEncoder.encode("password456"));
            usuario2.getRoles().add(Role.ROLE_ADMIN);
            usuarioRepository.save(usuario2);
            log.info("✓ Usuario ADMIN creado: {} ({})", usuario2.getEmail(), "password456");

            // Usuario 3 - Rol USER
            Usuario usuario3 = new Usuario();
            usuario3.setFirstName("Carol");
            usuario3.setLastName("Davis");
            usuario3.setEmail("carol.davis@example.com");
            usuario3.setPassword(passwordEncoder.encode("password789"));
            usuario3.getRoles().add(Role.ROLE_USER);
            usuarioRepository.save(usuario3);
            log.info("✓ Usuario USER creado: {} ({})", usuario3.getEmail(), "password789");

            // Usuarios adicionales con JavaFaker
            Faker faker = new Faker(new Locale("es"));
            for (int i = 0; i < 5; i++) {
                String email = faker.internet().emailAddress();
                Usuario usuarioAleatorio = new Usuario();
                usuarioAleatorio.setFirstName(faker.name().firstName());
                usuarioAleatorio.setLastName(faker.name().lastName());
                usuarioAleatorio.setEmail(email);
                usuarioAleatorio.setPassword(passwordEncoder.encode("demo123"));
                usuarioAleatorio.getRoles().add(Role.ROLE_USER);
                usuarioRepository.save(usuarioAleatorio);
            }
            log.info("✓ 5 usuarios aleatorios creados (password: demo123)");

            // Mostrar credenciales de acceso
            log.info("");
            log.info("CREDENCIALES DE ACCESO:");
            log.info("  USER:  alice.johnson@example.com / password123");
            log.info("  ADMIN: bob.smith@example.com / password456");
            log.info("  USER:  carol.davis@example.com / password789");
            log.info("  Otros usuarios aleatorios: demo123");

        } catch (Exception e) {
            log.error("✗ Error al inicializar usuarios: {}", e.getMessage(), e);
        }
    }
}