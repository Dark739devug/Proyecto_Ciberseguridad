package com.facturacion.backend.services;

import com.facturacion.backend.models.Login;
import com.facturacion.backend.models.Role;
import com.facturacion.backend.models.Usuario;
import com.facturacion.backend.repositories.LoginRepository;
import com.facturacion.backend.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          LoginRepository loginRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario con su login
     * @param usuario Objeto Usuario con nombre, apellido, email, rol
     * @param email Email para login
     * @param contrasenaPlana Contraseña sin hashear
     * @return Usuario guardado
     */
    @Transactional
    public Usuario registrarNuevoUsuario(Usuario usuario, String email, String contrasenaPlana) {

        // Establecer el email en el usuario
        usuario.setEmail(email);

        // Hashear la contraseña
        String contrasenaHasheada = passwordEncoder.encode(contrasenaPlana);

        // Guardar el usuario primero
        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        // Crear y guardar el login
        Login login = new Login();
        login.setEmail(email);
        login.setPasswordHash(contrasenaHasheada);
        login.setUsuario(nuevoUsuario);
        loginRepository.save(login);

        // Establecer la relación bidireccional
        nuevoUsuario.setLogin(login);

        return nuevoUsuario;
    }

    /**
     * Implementación de UserDetailsService
     * Carga el usuario por email y retorna el Usuario (que implementa UserDetails)
     * Esto permite que Spring Security tenga acceso a los roles
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Login login = loginRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        Usuario usuario = login.getUsuario();

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        // Retornar el Usuario que implementa UserDetails
        // Esto incluye automáticamente los roles desde getAuthorities()
        return usuario;
    }
}