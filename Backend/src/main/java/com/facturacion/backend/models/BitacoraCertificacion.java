package com.facturacion.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "bitacora_certificacion")
@Data
@NoArgsConstructor
public class BitacoraCertificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long idBitacora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;

    @Column(name = "fecha_intento", nullable = false)
    private LocalDateTime fechaIntento;

    @Column(nullable = false)
    private Boolean exitoso;

    @Column(name = "codigo_respuesta", length = 20)
    private String codigoRespuesta;

    @Column(name = "mensaje_respuesta", columnDefinition = "TEXT")
    private String mensajeRespuesta;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_enviados", columnDefinition = "jsonb")
    private Map<String, Object> datosEnviados;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_recibidos", columnDefinition = "jsonb")
    private Map<String, Object> datosRecibidos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsable")
    private Usuario usuarioResponsable;

    @PrePersist
    protected void onCreate() {
        if (fechaIntento == null) {
            fechaIntento = LocalDateTime.now();
        }
    }
}