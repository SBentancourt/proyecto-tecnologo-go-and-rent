package com.tecnologo.grupo3.goandrent.security.jwt;

import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // -- Aca se van a manejar los errores de que un usuario X no está autorizado
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        final String expired = (String) request.getAttribute("expired");
        final String inhabilitado = (String) request.getAttribute("inhabilitado");
        if (expired != null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, expired);
        } else {
            if (inhabilitado != null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, inhabilitado);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
            }
        }
    }
}
