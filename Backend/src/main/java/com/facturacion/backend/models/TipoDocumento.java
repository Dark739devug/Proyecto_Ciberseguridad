package com.facturacion.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipos_documento")
@Data
@NoArgsConstructor
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Long idTipoDocumento;

    @Column(name = "codigo_tipo", nullable = false, unique = true, length = 10)
    private String codigoTipo;

    @Column(name = "nombre_tipo", nullable = false, length = 100)
    private String nombreTipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;
}