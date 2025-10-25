package com.facturacion.backend.services;

import com.facturacion.backend.models.CategoriaProducto;
import com.facturacion.backend.models.Establecimiento;
import com.facturacion.backend.models.Producto;
import com.facturacion.backend.repositories.CategoriaProductoRepository;
import com.facturacion.backend.repositories.EstablecimientoRepository;
import com.facturacion.backend.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaRepository;
    private final EstablecimientoRepository establecimientoRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaProductoRepository categoriaRepository,
                           EstablecimientoRepository establecimientoRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.establecimientoRepository = establecimientoRepository;
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivo(true);
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public Producto obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigoProducto(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + codigo));
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    public List<Producto> obtenerPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria);
    }

    @Transactional
    public Producto crear(Producto producto, Long idCategoria, Long idEstablecimiento) {
        if (idCategoria != null) {
            CategoriaProducto categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        if (idEstablecimiento != null) {
            Establecimiento establecimiento = establecimientoRepository.findById(idEstablecimiento)
                    .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));
            producto.setEstablecimiento(establecimiento);
        }

        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto productoActualizado, Long idCategoria, Long idEstablecimiento) {
        Producto producto = obtenerPorId(id);

        producto.setCodigoProducto(productoActualizado.getCodigoProducto());
        producto.setNombreProducto(productoActualizado.getNombreProducto());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecioUnitario(productoActualizado.getPrecioUnitario());
        producto.setStockActual(productoActualizado.getStockActual());
        producto.setUnidadMedida(productoActualizado.getUnidadMedida());
        producto.setAplicaIva(productoActualizado.getAplicaIva());

        if (idCategoria != null) {
            CategoriaProducto categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        if (idEstablecimiento != null) {
            Establecimiento establecimiento = establecimientoRepository.findById(idEstablecimiento)
                    .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));
            producto.setEstablecimiento(establecimiento);
        }

        return productoRepository.save(producto);
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional
    public void actualizarStock(Long id, Integer cantidad) {
        Producto producto = obtenerPorId(id);
        producto.setStockActual(producto.getStockActual() + cantidad);
        productoRepository.save(producto);
    }
}