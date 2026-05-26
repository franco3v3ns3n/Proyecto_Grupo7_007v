package com.veranum.mantenimiento.services;

import com.veranum.mantenimiento.clients.HabitacionClient;
import com.veranum.mantenimiento.dtos.request.MantenimientoRequestDTO;
import com.veranum.mantenimiento.dtos.response.MantenimientoResponseDTO;
import com.veranum.mantenimiento.exceptions.RemoteServiceException;
import com.veranum.mantenimiento.exceptions.ResourceNotFoundException;
import com.veranum.mantenimiento.models.MantenimientoModel;
import com.veranum.mantenimiento.repositories.MantenimientoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final HabitacionClient habitacionClient;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            HabitacionClient habitacionClient
    ) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.habitacionClient = habitacionClient;
    }

    public List<MantenimientoResponseDTO> obtenerMantenimientos() {
        log.info("Obteniendo mantenimientos");

        return mantenimientoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MantenimientoResponseDTO obtenerMantenimientoPorId(Integer idMantenimiento) {
        log.info("Buscando mantenimiento con id: {}", idMantenimiento);

        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado"));

        return mapToResponse(mantenimiento);
    }

    public List<MantenimientoResponseDTO> obtenerMantenimientosPorHabitacion(Integer idHabitacion) {
        log.info("Obteniendo mantenimientos de la habitación con id: {}", idHabitacion);

        validarHabitacionExiste(idHabitacion);

        return mantenimientoRepository.findByIdHabitacion(idHabitacion)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MantenimientoResponseDTO> obtenerMantenimientosPorEstado(
            String estadoMantenimiento
    ) {
        log.info(
                "Obteniendo mantenimientos con estado: {}",
                estadoMantenimiento
        );

        return mantenimientoRepository.findByEstadoMantenimiento(estadoMantenimiento)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MantenimientoResponseDTO crearMantenimiento(MantenimientoRequestDTO request) {
        log.info("Creando mantenimiento para habitación: {}", request.getIdHabitacion());

        validarHabitacionExiste(request.getIdHabitacion());

        MantenimientoModel mantenimiento = MantenimientoModel.builder()
                .idHabitacion(request.getIdHabitacion())
                .tipoMantenimiento(request.getTipoMantenimiento())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estadoMantenimiento(request.getEstadoMantenimiento())
                .build();

        MantenimientoModel mantenimientoGuardado = mantenimientoRepository.save(mantenimiento);

        return mapToResponse(mantenimientoGuardado);
    }

    public MantenimientoResponseDTO actualizarMantenimiento(
            Integer idMantenimiento,
            MantenimientoRequestDTO request
    ) {
        log.info("Actualizando mantenimiento con id: {}", idMantenimiento);

        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado"));

        validarHabitacionExiste(request.getIdHabitacion());

        mantenimiento.setIdHabitacion(request.getIdHabitacion());
        mantenimiento.setTipoMantenimiento(request.getTipoMantenimiento());
        mantenimiento.setFechaInicio(request.getFechaInicio());
        mantenimiento.setFechaFin(request.getFechaFin());
        mantenimiento.setEstadoMantenimiento(request.getEstadoMantenimiento());

        MantenimientoModel mantenimientoActualizado = mantenimientoRepository.save(mantenimiento);

        return mapToResponse(mantenimientoActualizado);
    }

    public void eliminarMantenimiento(Integer idMantenimiento) {
        log.info("Eliminando mantenimiento con id: {}", idMantenimiento);

        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado"));

        mantenimientoRepository.delete(mantenimiento);
    }

    private void validarHabitacionExiste(Integer idHabitacion) {
        try {
            habitacionClient.obtenerHabitacionPorId(idHabitacion);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Habitación no encontrada con id: " + idHabitacion);
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al comunicarse con el microservicio habitacion");
        }
    }

    private MantenimientoResponseDTO mapToResponse(MantenimientoModel mantenimiento) {
        return MantenimientoResponseDTO.builder()
                .idMantenimiento(mantenimiento.getIdMantenimiento())
                .idHabitacion(mantenimiento.getIdHabitacion())
                .tipoMantenimiento(mantenimiento.getTipoMantenimiento())
                .fechaInicio(mantenimiento.getFechaInicio())
                .fechaFin(mantenimiento.getFechaFin())
                .estadoMantenimiento(mantenimiento.getEstadoMantenimiento())
                .build();
    }
}
