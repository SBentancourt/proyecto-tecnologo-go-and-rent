package com.tecnologo.grupo3.goandrent.security.jwt;

import com.tecnologo.grupo3.goandrent.exceptions.UserInvalidException;
import com.tecnologo.grupo3.goandrent.security.CustomUserDetailsService;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // -- se obtiene el token de la solicitud http.
        String token =obtenerJWTdeSolicitud(request);
        // -- se valida el token
        if (StringUtils.hasText(token) && jwtTokenProvider.validarToken(token, request)){
            // -- se obtiene el username del token
            String username = jwtTokenProvider.obtenerUsernameDelJWT(token);
            if (userService.isValidUser(username)){
                // -- se carga el usuario asociado al token
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                // -- se autentica el usuario
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // -- se establece la seguridad
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                request.setAttribute("inhabilitado", "Usuario no habilitado para realizar esta acción.");
            }
        }
        filterChain.doFilter(request,response);

    }

    // -- Bearer token de acceso.
    private String obtenerJWTdeSolicitud(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");    // -- Aca se obtiene el token del header, del campo authorization
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){  // -- aca se verifica si el tipo de autorizacion que se está utilizando es Bearer token
            // -- aca se le quita los primeros 7 caracteres al token ya que viene de la siguiente forma:
            // -- Bearer XXXXXXXXXXXXXXXXXXXXXXXXXXX => Las primeras 7 posiciones corresponde a Bearer mas un espacio.
            // -- Se quita Bearer + espacio para retornar unicamente el token.
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
