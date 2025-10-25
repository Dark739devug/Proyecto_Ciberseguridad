package com.facturacion.backend.services;

import com.facturacion.backend.models.Establecimiento;
import com.facturacion.backend.repositories.EstablecimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;

    public EstablecimientoService(EstablecimientoRepository establecimientoRepository) {
        this.establecimientoRepository = establecimientoRepository;
    }

    public List<Establecimiento> obtenerTodos() {
        return establecimientoRepository.findAll();
    }

    public List<Establecimiento> obtenerActivos() {
        return establecimientoRepository.findByActivo(true);
    }

    public Establecimiento obtenerPorId(Long id) {
        return establecimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado con ID: " + id));
    }

    public Establecimiento obtenerPorNit(String nit) {
        return establecimientoRepository.findByNit(nit)
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado con NIT: " + nit));
    }

    @Transactional
    public Establecimiento crear(Establecimiento establecimiento) {
        return establecimientoRepository.save(establecimiento);
    }

    @Transactional
    public Establecimiento actualizar(Long id, Establecimiento establecimientoActualizado) {
        Establecimiento establecimiento = obtenerPorId(id);

        establecimiento.setNit(establecimientoActualizado.getNit());
        establecimiento.setNombreComercial(establecimientoActualizado.getNombreComercial());
        establecimiento.setRazonSocial(establecimientoActualizado.getRazonSocial());
        establecimiento.setDireccion(establecimientoActualizado.getDireccion());
        establecimiento.setMunicipio(establecimientoActualizado.getMunicipio());
        establecimiento.setDepartamento(establecimientoActualizado.getDepartamento());
        establecimiento.setCodigoPostal(establecimientoActualizado.getCodigoPostal());
        establecimiento.setTelefono(establecimientoActualizado.getTelefono());
        establecimiento.setEmail(establecimientoActualizado.getEmail());
        establecimiento.setCodigoEstablecimiento(establecimientoActualizado.getCodigoEstablecimiento());
        establecimiento.setActivoCertificador(establecimientoActualizado.getActivoCertificador());

        return establecimientoRepository.save(establecimiento);
    }

    @Transactional
    public void desactivar(Long id) {
        Establecimiento establecimiento = obtenerPorId(id);
        establecimiento.setActivo(false);
        establecimientoRepository.save(establecimiento);
    }
}