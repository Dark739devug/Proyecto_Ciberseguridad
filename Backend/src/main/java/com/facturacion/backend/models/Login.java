package com.facturacion.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Data
@NoArgsConstructor
public class Login {

    // Mapea id_login SERIAL PRIMARY KEY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogin;

    // Mapea correo_electronico VARCHAR(150) UNIQUE
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 150)
    private String correoElectronico;

    // Mapea contrasena VARCHAR(255)
    @Column(nullable = false)
    private String contrasena;

    // Relación One-to-One: Login tiene la clave foránea (FK) id_usuario
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;
}