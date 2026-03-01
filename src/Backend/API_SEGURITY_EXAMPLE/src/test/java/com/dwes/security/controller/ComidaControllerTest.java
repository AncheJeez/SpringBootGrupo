package com.dwes.security.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.dwes.security.entities.Role;
import com.dwes.security.entities.Usuario;
import com.dwes.security.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ComidaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    String jwtSecret;

    String tokenUser;
    String tokenAdmin;

    @BeforeEach
    void setUp() {
        seedUsuarios();

        tokenUser = tokenConRoles("user@test.com", List.of("ROLE_USER"));
        tokenAdmin = tokenConRoles("admin@test.com", List.of("ROLE_ADMIN"));
    }

    private void seedUsuarios() {
        // Evita violación de unique(email) entre tests
        userRepository.deleteAll();

        Usuario user = new Usuario();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("user@test.com");
        user.setPassword("test");
        user.setRoles(Set.of(Role.ROLE_USER));

        Usuario admin = new Usuario();
        admin.setFirstName("Test");
        admin.setLastName("Admin");
        admin.setEmail("admin@test.com");
        admin.setPassword("test");
        admin.setRoles(Set.of(Role.ROLE_ADMIN));

        userRepository.save(user);
        userRepository.save(admin);
    }

    @Test
    void getComidas_sinToken_401o403() throws Exception {
        mockMvc.perform(get("/api/v1/comidas"))
                .andExpect(result -> {
                    int s = result.getResponse().getStatus();
                    if (s != 401 && s != 403) {
                        throw new AssertionError("Esperaba 401 o 403, pero fue: " + s);
                    }
                });
    }

    @Test
    void getComidas_conUser_200_yValidaJsonDeListado() throws Exception {
        mockMvc.perform(get("/api/v1/comidas")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void postComidas_conUser_403() throws Exception {
        String body = """
                {"nombre":"Paella","paisOrigen":"España"}
                """;

        mockMvc.perform(post("/api/v1/comidas")
                        .header("Authorization", "Bearer " + tokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isForbidden());
    }

    @Test
    void postComidas_conAdmin_200o201_yValidaJsonDeRespuesta() throws Exception {
        String body = """
                {"nombre":"Paella","paisOrigen":"España"}
                """;

        mockMvc.perform(post("/api/v1/comidas")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8)))
                .andExpect(result -> {
                    int s = result.getResponse().getStatus();
                    if (s != 200 && s != 201) {
                        throw new AssertionError("Esperaba 200 o 201, pero fue: " + s);
                    }
                })
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Paella"))
                .andExpect(jsonPath("$.paisOrigen").value("España"));
    }

    @Test
    void getComidaById_inexistente_conUser_404() throws Exception {
        mockMvc.perform(get("/api/v1/comidas/999999")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isNotFound());
    }

    @Test
    void postComidas_jsonMalformado_conAdmin_400o500_detectaError() throws Exception {
        String body = """
        {"nombre":"Paella","paisOrigen":
        """;

        mockMvc.perform(post("/api/v1/comidas")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8)))
                .andExpect(result -> {
                    int s = result.getResponse().getStatus();
                    if (s != 400 && s != 500) {
                        throw new AssertionError("Esperaba 400 o 500, pero fue: " + s);
                    }
                });
    }

    @Test
    void putComidas_conUser_403() throws Exception {
        String body = """
                {"nombre":"Ramen","paisOrigen":"Japón"}
                """;

        mockMvc.perform(put("/api/v1/comidas/1")
                        .header("Authorization", "Bearer " + tokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isForbidden());
    }

    @Test
    void putComidas_conAdmin_200_yValidaJsonActualizado() throws Exception {
        long id = crearComidaYDevolverId("Tortilla", "España");

        String updateBody = """
                {"nombre":"Ramen","paisOrigen":"Japón"}
                """;

        mockMvc.perform(put("/api/v1/comidas/" + id)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody.getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                // jsonPath a veces trata números como Integer
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.nombre").value("Ramen"))
                .andExpect(jsonPath("$.paisOrigen").value("Japón"));

        // Extra: comprobar con GET que se quedó actualizado
        mockMvc.perform(get("/api/v1/comidas/" + id)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.nombre").value("Ramen"))
                .andExpect(jsonPath("$.paisOrigen").value("Japón"));
    }

    @Test
    void deleteComidas_conUser_403() throws Exception {
        mockMvc.perform(delete("/api/v1/comidas/1")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComidas_conAdmin_200_yLuegoGet_404() throws Exception {
        long id = crearComidaYDevolverId("Sushi", "Japón");

        // Tu controller devuelve void -> normalmente 200 OK
        mockMvc.perform(delete("/api/v1/comidas/" + id)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/comidas/" + id)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isNotFound());
    }

    private long crearComidaYDevolverId(String nombre, String paisOrigen) throws Exception {
        String body = """
                {"nombre":"%s","paisOrigen":"%s"}
                """.formatted(nombre, paisOrigen);

        MvcResult postRes = mockMvc.perform(post("/api/v1/comidas")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8)))
                .andExpect(result -> {
                    int s = result.getResponse().getStatus();
                    if (s != 200 && s != 201) {
                        throw new AssertionError("Esperaba 200 o 201, pero fue: " + s);
                    }
                })
                .andReturn();

        JsonNode node = objectMapper.readTree(postRes.getResponse().getContentAsString());
        assertThat(node.hasNonNull("id")).as("El POST debería devolver el campo 'id'").isTrue();
        return node.get("id").asLong();
    }

    private String tokenConRoles(String subject, List<String> roles) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(subject) // email, coincide con loadUserByUsername
                // Tu JwtAuthenticationFilter NO lee este claim; las authorities vienen de BD (Usuario.roles)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofHours(2))))
                .signWith(key)
                .compact();
    }
}