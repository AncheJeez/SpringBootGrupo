package com.dwes.security.service.user.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dwes.security.service.user.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
    /**
     * #############
     * #   CLAIMS  #
     * #############
     * En los tokens JWT, los claims comúnmente incluyen información como:
     *
     * sub (Subject): Identificador del usuario.
     * exp (Expiration Time): Tiempo de expiración del token.
     * iat (Issued At): Momento en que se emitió el token.
     * iss (Issuer): Quién emitió el token.
     * aud (Audience): A quién está destinado el token.
     */
    
    // Llave para firmar el JWT, obtenida del archivo de propiedades de la aplicación.
    @Value("${jwt.secret}")
    private String jwtSigningKey;

    // Tiempo de expiración del token en milisegundos (por defecto 10 horas)
    @Value("${jwt.expiration:36000000}")
    private Long jwtExpiration;

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     * En un JWT, el 'subject' suele referirse al identificador del usuario.
     */
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Genera un token JWT para un usuario con detalles específicos (UserDetails).
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Verifica si el token es válido comparando el nombre de usuario y comprobando si ha expirado.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Método genérico para extraer información del token JWT.
     * Los claims son declaraciones que contienen información sobre el usuario y metadatos adicionales.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Genera el token JWT incluyendo los claims adicionales y los detalles del usuario.
     * Los claims adicionales pueden ser usados para almacenar información adicional sobre el usuario o el token.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        long currentTimeMillis = System.currentTimeMillis();
        
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + jwtExpiration)) // Token expira según configuración
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Verifica si el token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todas las claims del token.
     * Aquí se extrae y se procesa el conjunto completo de claims del JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene la llave de firma a partir de la cadena codificada en base64.
     */
    private SecretKey getSigningKey() {
       /* byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);*/
    	return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSigningKey));



    }
}