package com.facturacion.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estados_factura")
@Data
@NoArgsConstructor
public class EstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;

    @Column(name = "codigo_estado", nullable = false, unique = true, length = 20)
    private String codigoEstado;

    @Column(name = "nombre_estado", nullable = false, length = 50)
    private String nombreEstado;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}