package com.facturacion.backend.controllers;

import com.facturacion.backend.models.Establecimiento;
import com.facturacion.backend.repositories.EstablecimientoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/establecimientos")
@SecurityRequirement(name = "bearerAuth")
public class EstablecimientoController {

    @Autowired
    private EstablecimientoRepository establecimientoRepository;

    @GetMapping
    public List<Establecimiento> listarTodos() {
        return establecimientoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Establecimiento obtenerPorId(@PathVariable Long id) {
        return establecimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Establecimiento no encontrado"
                ));
    }

    @GetMapping("/nit/{nit}")
    public Establecimiento obtenerPorNit(@PathVariable String nit) {
        return establecimientoRepository.findByNit(nit)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Establecimiento no encontrado"
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Establecimiento crear(@RequestBody Establecimiento establecimiento) {
        return establecimientoRepository.save(establecimiento);
    }

    @PutMapping("/{id}")
    public Establecimiento actualizar(@PathVariable Long id, @RequestBody Establecimiento establecimiento) {
        if (!establecimientoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Establecimiento no encontrado");
        }
        establecimiento.setIdEstablecimiento(id);
        return establecimientoRepository.save(establecimiento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        establecimientoRepository.deleteById(id);
    }
}
