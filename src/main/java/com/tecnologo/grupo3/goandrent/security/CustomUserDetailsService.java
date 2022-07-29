package com.tecnologo.grupo3.goandrent.security;

import com.tecnologo.grupo3.goandrent.repositories.AdminRepository;
import com.tecnologo.grupo3.goandrent.repositories.GuestRepository;
import com.tecnologo.grupo3.goandrent.repositories.HostRepository;
import com.tecnologo.grupo3.goandrent.repositories.UserRepository;
import com.tecnologo.grupo3.goandrent.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.DiscriminatorValue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.tecnologo.grupo3.goandrent.entities.User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));
        Set<Role> rolesDelUsuario = new HashSet<>();

        String role = user.getClass().getAnnotation(DiscriminatorValue.class).value();
        rolesDelUsuario.add(Role.valueOf(role));
        // -- aca retorno el usuario User de tipo UserDetails, con el correo, password y rol / roles asociados.
        return new User(user.getEmail(), user.getPassword(), mapearRoles(rolesDelUsuario));
    }

    // -- metodo para mapear roles
    private Collection<? extends GrantedAuthority> mapearRoles(Set<Role> roles){
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.name())).collect(Collectors.toList());
    }
}

