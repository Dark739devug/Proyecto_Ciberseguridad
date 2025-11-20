package com.facturacion.backend.services;

import com.facturacion.backend.models.*;
import com.facturacion.backend.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final ClienteRepository clienteRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoFacturaRepository estadoFacturaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public FacturaService(FacturaRepository facturaRepository,
                          DetalleFacturaRepository detalleFacturaRepository,
                          ClienteRepository clienteRepository,
                          EstablecimientoRepository establecimientoRepository,
                          TipoDocumentoRepository tipoDocumentoRepository,
                          EstadoFacturaRepository estadoFacturaRepository,
                          ProductoRepository productoRepository,
                          UsuarioRepository usuarioRepository) {
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.clienteRepository = clienteRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoFacturaRepository = estadoFacturaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Crea una factura completa con sus detalles
     * Calcula automáticamente subtotales, IVA y totales
     * Valida stock de productos
     */
    @Transactional
    public Factura crearFactura(Long idEstablecimiento, Long idCliente, Long idTipoDocumento,
                                Long idUsuario, List<DetalleFacturaRequest> detalles,
                                String observaciones) {

        // 1. Validar entidades relacionadas
        Establecimiento establecimiento = establecimientoRepository.findById(idEstablecimiento)
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        EstadoFactura estadoBorrador = estadoFacturaRepository.findByCodigoEstado("BORRADOR")
                .orElseThrow(() -> new RuntimeException("Estado BORRADOR no encontrado"));

        // 2. Crear factura (encabezado)
        Factura factura = new Factura();
        factura.setEstablecimiento(establecimiento);
        factura.setCliente(cliente);
        factura.setTipoDocumento(tipoDocumento);
        factura.setUsuarioCreacion(usuario);
        factura.setEstado(estadoBorrador);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setObservaciones(observaciones);
        // Generar número de factura si está vacío
        if (factura.getNumeroFactura() == null) {
            String serie = String.valueOf(LocalDateTime.now().getYear());
            Integer ultimoCorrelativo = facturaRepository.findUltimoCorrelativoPorEstablecimientoYSerie(
                    idEstablecimiento,
                    serie
            );

            Integer nuevoCorrelativo = (ultimoCorrelativo != null ? ultimoCorrelativo : 0) + 1;
            String numeroFactura = serie + "-" + String.format("%08d", nuevoCorrelativo);

            factura.setSerie(serie);
            factura.setCorrelativo(nuevoCorrelativo);
            factura.setNumeroFactura(numeroFactura);
        }

        // 3. Inicializar totales
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDescuentos = BigDecimal.ZERO;
        BigDecimal totalIva = BigDecimal.ZERO;

        // 4. Establecer valores iniciales para cumplir con NOT NULL
        factura.setSubtotal(BigDecimal.ZERO);
        factura.setTotalDescuentos(BigDecimal.ZERO);
        factura.setSubtotalConDescuento(BigDecimal.ZERO);
        factura.setTotalIva(BigDecimal.ZERO);
        factura.setTotalFactura(BigDecimal.ZERO);

        // 5. Guardar factura para obtener ID
        factura = facturaRepository.save(factura);

        // 6. Procesar cada detalle
        int numeroLinea = 1;
        for (DetalleFacturaRequest detalleReq : detalles) {

            // Validar producto
            Producto producto = productoRepository.findById(detalleReq.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleReq.getIdProducto()));

            // Validar stock
            if (producto.getStockActual() < detalleReq.getCantidad().intValue()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }

            // Crear detalle
            DetalleFactura detalle = new DetalleFactura();
            detalle.setFactura(factura);
            detalle.setNumeroLinea(numeroLinea++);
            detalle.setProducto(producto);
            detalle.setDescripcionProducto(producto.getNombreProducto());
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setUnidadMedida(producto.getUnidadMedida());
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setDescuento(detalleReq.getDescuento() != null ? detalleReq.getDescuento() : BigDecimal.ZERO);
            detalle.setAplicaIva(producto.getAplicaIva());

            // Calcular subtotal de línea
            BigDecimal subtotalLinea = detalle.getPrecioUnitario()
                    .multiply(detalle.getCantidad())
                    .subtract(detalle.getDescuento())
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.setSubtotalLinea(subtotalLinea);

            // Calcular IVA de línea (12% en Guatemala)
            BigDecimal ivaLinea = BigDecimal.ZERO;
            if (detalle.getAplicaIva()) {
                ivaLinea = subtotalLinea.multiply(new BigDecimal("0.12"))
                        .setScale(2, RoundingMode.HALF_UP);
            }
            detalle.setMontoIva(ivaLinea);

            // Calcular total de línea
            BigDecimal totalLinea = subtotalLinea.add(ivaLinea);
            detalle.setTotalLinea(totalLinea);

            // Guardar detalle
            detalleFacturaRepository.save(detalle);

            // Acumular totales
            subtotal = subtotal.add(subtotalLinea);
            totalDescuentos = totalDescuentos.add(detalle.getDescuento());
            totalIva = totalIva.add(ivaLinea);
        }

        // 6. Actualizar totales de factura
        factura.setSubtotal(subtotal);
        factura.setTotalDescuentos(totalDescuentos);
        factura.setSubtotalConDescuento(subtotal);
        factura.setTotalIva(totalIva);
        factura.setTotalFactura(subtotal.add(totalIva));

        return facturaRepository.save(factura);
    }

    /**
     * Obtiene una factura por ID
     */
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
    }

    /**
     * Cambia el estado de una factura a PENDIENTE para certificación
     */
    @Transactional
    public Factura enviarACertificar(Long idFactura) {
        Factura factura = obtenerPorId(idFactura);

        if (!"BORRADOR".equals(factura.getEstado().getCodigoEstado())) {
            throw new RuntimeException("Solo se pueden certificar facturas en estado BORRADOR");
        }

        EstadoFactura estadoPendiente = estadoFacturaRepository.findByCodigoEstado("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));

        factura.setEstado(estadoPendiente);
        return facturaRepository.save(factura);
    }

    /**
     * Anula una factura
     */
    @Transactional
    public Factura anularFactura(Long idFactura, Long idUsuarioAnulacion, String motivo) {
        Factura factura = obtenerPorId(idFactura);

        if ("ANULADA".equals(factura.getEstado().getCodigoEstado())) {
            throw new RuntimeException("La factura ya está anulada");
        }

        Usuario usuario = usuarioRepository.findById(idUsuarioAnulacion)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        EstadoFactura estadoAnulada = estadoFacturaRepository.findByCodigoEstado("ANULADA")
                .orElseThrow(() -> new RuntimeException("Estado ANULADA no encontrado"));

        factura.setEstado(estadoAnulada);
        factura.setFechaAnulacion(LocalDateTime.now());
        factura.setMotivoAnulacion(motivo);
        factura.setUsuarioAnulacion(usuario);

        return facturaRepository.save(factura);
    }

    /**
     * Lista todas las facturas
     */
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    /**
     * Lista facturas por estado
     */
    public List<Factura> obtenerPorEstado(String codigoEstado) {
        EstadoFactura estado = estadoFacturaRepository.findByCodigoEstado(codigoEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        return facturaRepository.findByEstado_IdEstado(estado.getIdEstado());
    }

    /**
     * Lista facturas por cliente
     */
    public List<Factura> obtenerPorCliente(Long idCliente) {
        return facturaRepository.findByCliente_IdCliente(idCliente);
    }

    // DTO interno para recibir detalles
    public static class DetalleFacturaRequest {
        private Long idProducto;
        private BigDecimal cantidad;
        private BigDecimal descuento;

        public Long getIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

        public BigDecimal getCantidad() { return cantidad; }
        public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

        public BigDecimal getDescuento() { return descuento; }
        public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    }

    /**
     * Obtiene los detalles de una factura
     */
    public List<DetalleFactura> obtenerDetallesPorFactura(Long idFactura) {
        return detalleFacturaRepository.findByFactura_IdFactura(idFactura);
    }

    // Obtener factura por número
    public Factura obtenerPorNumero(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    // Obtener facturas por rango de fechas
    public List<Factura> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return facturaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
    }


}