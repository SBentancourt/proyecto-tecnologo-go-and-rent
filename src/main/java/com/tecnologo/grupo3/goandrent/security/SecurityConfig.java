package com.tecnologo.grupo3.goandrent.security;

import com.tecnologo.grupo3.goandrent.security.jwt.JwtAuthenticationEntryPoint;
import com.tecnologo.grupo3.goandrent.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// -- La anotacion @Configuration se encarga de controlar Beans, configurarlos, etc
@Configuration
// -- @EnableWebSecurity se usa para crear una clase de seguridad personalizada
@EnableWebSecurity
// -- @EnableGlobalMethodSecurity se usa para poder redefinir algunos metodos brindados por SpringSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    // -- metodo para codificar / hashear la contraseña
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()       // -- se desabilita porque springboot ya tiene el propio csrf
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)  //-- Con JWT se agrega el manejo de excepciones jwtAuthenticationEntryPoint
                .and()
                // -- Con JWT se agrega sessiongManagement() el cual se encarga de almacenar datos de sesión para un usuario en específico
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // -- Ademas se agrega sessionCreationPolicy() para indicarle que el protocolo es sin estado
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/data/**").permitAll()
                .antMatchers("/api/healthchecks/**").permitAll()
                .antMatchers("/api/assets/**").permitAll()
                .antMatchers("/booking/pay/**").permitAll()
                /*.antMatchers("/user/update-profile/**").permitAll()
                .antMatchers("/user/update/password/**").permitAll()
                .antMatchers("/booking/**").permitAll()
                .antMatchers("/guests/**").permitAll()
                .antMatchers("/hosts/**").permitAll()
                .antMatchers("/admin/**").permitAll()*/
                .anyRequest()
                .authenticated();//.and().cors().disable(); //.and().cors().disable();   // -- esto hace que el resto de las peticiones (distintos de GET) tienen que ser autenticadas

        // -- Con JWT ya no se usa la autenticación básica
        //.and()
        //.httpBasic();   // -- con esto indico que se va a autenticar a traves de una autenticacion basica
        // -- se realiza la validación con la clase del filtro que creamos.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // -- este metodo va a permitir construir nuestros usuarios
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setMaxAge(5184000L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
