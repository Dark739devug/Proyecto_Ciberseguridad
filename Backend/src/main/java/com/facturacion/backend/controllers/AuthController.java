package com.facturacion.backend.controllers;

import com.facturacion.backend.models.Role;
import com.facturacion.backend.models.Usuario;
import com.facturacion.backend.repositories.RoleRepository;
import com.facturacion.backend.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtEncoder jwtEncoder;

    public AuthController(UsuarioService usuarioService,
                          AuthenticationManager authenticationManager,
                          RoleRepository roleRepository,
                          JwtEncoder jwtEncoder) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtEncoder = jwtEncoder;
    }

    // DTOs usando records (como tenías antes)
    public record RegistroRequest(
            String nombre,
            String apellido,
            String email,
            String contrasena,
            Long idRol
    ) {}

    public record LoginRequest(
            String email,
            String password
    ) {}

    /**
     * POST /api/auth/register
     * Registra un nuevo usuario + credenciales en login_usuarios
     */
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest request) {
        try {
            // Buscar el rol
            Role rol = roleRepository.findById(request.idRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.idRol()));

            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(request.nombre());
            usuario.setApellido(request.apellido());
            usuario.setRol(rol);
            usuario.setActivo(true);

            // Registrar (guarda en usuarios + login_usuarios)
            Usuario nuevoUsuario = usuarioService.registrarNuevoUsuario(
                    usuario,
                    request.email(),
                    request.contrasena()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("idUsuario", nuevoUsuario.getIdUsuario());
            response.put("nombre", nuevoUsuario.getNombre() + " " + nuevoUsuario.getApellido());
            response.put("email", request.email());
            response.put("rol", rol.getNombreRol());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al registrar: " + e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login
     * Valida credenciales y retorna JWT token + info del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();

            // Generar JWT Token
            Instant now = Instant.now();
            long expiry = 36000L; // 10 horas

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(usuario.getUsername())
                    .claim("rol", usuario.getRol().getNombreRol())
                    .claim("idUsuario", usuario.getIdUsuario())
                    .claim("nombre", usuario.getNombre() + " " + usuario.getApellido())
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            // Respuesta con token + info del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", usuario.getUsername());
            response.put("nombre", usuario.getNombre() + " " + usuario.getApellido());
            response.put("rol", usuario.getRol().getNombreRol());
            response.put("idUsuario", usuario.getIdUsuario());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }

    /**
     * POST /api/auth/logout
     * Invalida el token actual 
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // En JWT stateless, el logout se maneja en el frontend eliminando el token
        // Aquí solo confirmamos la acción
        return ResponseEntity.ok().body(Map.of(
                "mensaje", "Logout exitoso. Token invalidado en el cliente."
        ));
    }

    /**
     * POST /api/auth/refresh
     * Refresca el token JWT
     */
    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> refreshToken(Authentication authentication) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            // Generar nuevo token
            Instant now = Instant.now();
            long expiry = 36000L;

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(usuario.getUsername())
                    .claim("rol", usuario.getRol().getNombreRol())
                    .claim("idUsuario", usuario.getIdUsuario())
                    .claim("nombre", usuario.getNombre() + " " + usuario.getApellido())
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "mensaje", "Token refrescado exitosamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "No se pudo refrescar el token"));
        }
    }

}