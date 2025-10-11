package com.facturacion.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer; // IMPORT NECESARIO PARA CORS Y JWT
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Dependencias de CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays; // Necesario para Arrays.asList

// Dependencias de JWT/OAuth2
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;

// Dependencias de Claves RSA
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Lista de rutas públicas (no requieren Token)
    private static final String[] WHITE_LIST_URLS = {
            "/api/auth/**",            // Registro y Login
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };

    // Generar claves RSA para firmar y verificar tokens JWT
    private final KeyPair keyPair = generateRsaKey();
    private final RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
    private final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

    // Método auxiliar para generar las claves RSA
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo generar el par de claves RSA", e);
        }
    }

    /** Define el Bean de BCryptPasswordEncoder para cifrar contraseñas. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Define el Bean de AuthenticationManager, necesario para el AuthController. */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /** Configuración del codificador (Encoder) JWT para crear el token. */
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /** Configuración del decodificador (Decoder) JWT para validar el token. */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build();
    }

    // =========================================================================
    // CONFIGURACIÓN CORS (Crucial para la comunicación con el Frontend en 3000)
    // =========================================================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite el origen del Front-End (Next.js/React)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        // Permite todos los métodos HTTP que usarán (GET, POST, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permite las cabeceras necesarias, especialmente 'Authorization' para JWT
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Aplica esta configuración a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /** Configura el SecurityFilterChain (las reglas de seguridad). */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Activa la configuración CORS definida en el Bean de arriba
                .cors(Customizer.withDefaults())

                // Deshabilita CSRF para API REST (Ya lo tenías)
                .csrf(csrf -> csrf.disable())

                // **2. Configuración de Sesión Stateless (Sin cookies)**
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URLS).permitAll() // Permite acceso a rutas públicas
                        .anyRequest().authenticated() // Requiere autenticación para CUALQUIER otra ruta
                )

                // **4. Habilita el Servidor de Recursos OAuth2 (Validación JWT)**
                // Cambiamos a lambda explícito con Customizer.withDefaults() para compatibilidad
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}