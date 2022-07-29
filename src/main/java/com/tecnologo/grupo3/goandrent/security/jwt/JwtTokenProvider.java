package com.tecnologo.grupo3.goandrent.security.jwt;

import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // -- método que se encarga de generar el token con los datos de autenticacion recibidos
    public String generarToken(Authentication authentication){
        String username = authentication.getName();     // -- en nuestro caso el username es el correo
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpirationInMs);

        String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(fechaExpiracion)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

        return token;
    }

    // -- método que se encarga de obtener el usuario (username) del token
    public String obtenerUsernameDelJWT(String token){
        // -- los claims del token son sus datos: usuario, password, fecha de expiracion, etc
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // -- método que se encarga de validar el token
    public boolean validarToken(String token, HttpServletRequest request){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Firma JWT no válida");
        } catch (MalformedJwtException ex) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Token JWT no válido");
        } catch (ExpiredJwtException ex) {
            request.setAttribute("expired", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Token JWT no compatible");
        } catch (IllegalArgumentException ex) {
            throw new UserException(HttpStatus.BAD_REQUEST, "La cadena Claims JWT está vacía");
        }
        return false;
    }
}
