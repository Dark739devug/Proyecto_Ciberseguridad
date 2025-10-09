package com.facturacion.backend.controllers;

import com.facturacion.backend.models.Usuario;
import com.facturacion.backend.services.UsuarioService;
import com.facturacion.backend.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    // DTO para recibir el cuerpo de la petición de registro
    public record RegistroRequest(
            String nombre,
            String apellido,
            LocalDate fechaNacimiento,
            String nit,
            String dpi,
            String correoElectronico,
            String contrasena
    ) {}

    // DTO para recibir el cuerpo de la petición de login
    public record LoginRequest(String correoElectronico, String contrasena) {}

    // 1. ENDPOINT DE REGISTRO: POST /api/auth/register
    // Este endpoint ya lo probamos y funciona para crear el usuario y cifrar la contraseña
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario registrarUsuario(@RequestBody RegistroRequest request) {

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.nombre());
        nuevoUsuario.setApellido(request.apellido());
        nuevoUsuario.setFechaNacimiento(request.fechaNacimiento());
        nuevoUsuario.setNit(request.nit());
        nuevoUsuario.setDpi(request.dpi());

        return usuarioService.registrarNuevoUsuario(
                nuevoUsuario,
                request.correoElectronico(),
                request.contrasena()
        );
    }

    // 2. ENDPOINT DE LOGIN: POST /api/auth/token
    // Este endpoint es el que reemplaza el formLogin y devuelve el JWT
    @PostMapping("/token")
    public String token(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Intentar autenticar las credenciales usando el AuthenticationManager
            // El AuthenticationManager usará tu UsuarioService para buscar el usuario y BCrypt para verificar la contraseña
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.correoElectronico(),
                            loginRequest.contrasena()
                    )
            );

            // 2. Si la autenticación es exitosa, generar el token JWT
            return tokenService.generateToken(authentication);

        } catch (AuthenticationException e) {
            // Si las credenciales son incorrectas, lanza una excepción de no autorizado (401)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas o usuario no encontrado.");
        }
    }
}