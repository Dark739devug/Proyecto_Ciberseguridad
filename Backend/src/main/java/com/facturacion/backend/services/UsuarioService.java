package com.facturacion.backend.services;

import com.facturacion.backend.models.Login;
import com.facturacion.backend.models.Usuario;
import com.facturacion.backend.repositories.LoginRepository;
import com.facturacion.backend.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
public class UsuarioService implements UserDetailsService {

    // 1. Declaración de dependencias como final (sin @Autowired)
    private final UsuarioRepository usuarioRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    // 2. CONSTRUCTOR PARA INYECTAR LAS DEPENDENCIAS (¡Rompe el ciclo!)
    // Spring inyecta automáticamente los Beans creados en SecurityConfig.
    public UsuarioService(UsuarioRepository usuarioRepository,
                          LoginRepository loginRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // *** Lógica de Registro (Ya probada) ***
    @Transactional
    public Usuario registrarNuevoUsuario(Usuario usuario, String correoElectronico, String contrasenaPlana) {

        String contrasenaHasheada = passwordEncoder.encode(contrasenaPlana);
        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        Login login = new Login();
        login.setCorreoElectronico(correoElectronico);
        login.setContrasena(contrasenaHasheada);
        login.setUsuario(nuevoUsuario);
        loginRepository.save(login);

        return nuevoUsuario;
    }

    // *** Lógica de Carga de Usuario para Spring Security (UserDetailsService) ***
    @Override
    public UserDetails loadUserByUsername(String correoElectronico) throws UsernameNotFoundException {

        Login login = loginRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correoElectronico));

        // Construye el objeto UserDetails
        return new org.springframework.security.core.userdetails.User(
                login.getCorreoElectronico(),
                login.getContrasena(),
                new ArrayList<>() // Roles/Autoridades (vacía por ahora)
        );
    }
}