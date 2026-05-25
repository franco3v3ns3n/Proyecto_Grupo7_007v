package com.veranum.mantenimiento.services;

import com.veranum.mantenimiento.clients.HotelClient;
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
    private final HotelClient hotelClient;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository, HotelClient hotelClient) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.hotelClient = hotelClient;
    }

    public List<MantenimientoResponseDTO> obtenerMantenimientos() {
        log.info("Obteniendo el listado de todos los mantenimientos");
        return mantenimientoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MantenimientoResponseDTO obtenerMantenimientoPorId(Integer idMantenimiento) {
        log.info("Buscando mantenimiento con id: {}", idMantenimiento);
        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + idMantenimiento));
        return mapToResponse(mantenimiento);
    }

    public List<MantenimientoResponseDTO> obtenerMantenimientosPorHotel(Integer idHotel) {
        log.info("Obteniendo mantenimientos del hotel con id: {}", idHotel);
        validarHotelExiste(idHotel);
        return mantenimientoRepository.findById_hotel(idHotel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MantenimientoResponseDTO crearMantenimiento(MantenimientoRequestDTO request) {
        log.info("Creando registro de mantenimiento para el hotel ID: {}", request.getIdHotel());
        validarHotelExiste(request.getIdHotel());

        MantenimientoModel mantenimiento = MantenimientoModel.builder()
                .id_hotel(request.getIdHotel())
                .descripcion_mantenimiento(request.getDescripcionMantenimiento())
                .fecha_inicio(request.getFechaInicio())
                .fecha_termino(request.getFechaTermino())
                .costo_mantenimiento(request.getCostoMantenimiento())
                .estado_mantenimiento(request.getEstadoMantenimiento())
                .build();

        return mapToResponse(mantenimientoRepository.save(mantenimiento));
    }

    public MantenimientoResponseDTO actualizarMantenimiento(Integer idMantenimiento, MantenimientoRequestDTO request) {
        log.info("Actualizando mantenimiento con id: {}", idMantenimiento);
        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + idMantenimiento));

        validarHotelExiste(request.getIdHotel());

        mantenimiento.setId_hotel(request.getIdHotel());
        mantenimiento.setDescripcion_mantenimiento(request.getDescripcionMantenimiento());
        mantenimiento.setFecha_inicio(request.getFechaInicio());
        mantenimiento.setFecha_termino(request.getFechaTermino());
        mantenimiento.setCosto_mantenimiento(request.getCostoMantenimiento());
        mantenimiento.setEstado_mantenimiento(request.getEstadoMantenimiento());

        return mapToResponse(mantenimientoRepository.save(mantenimiento));
    }

    public void eliminarMantenimiento(Integer idMantenimiento) {
        log.info("Eliminando mantenimiento con id: {}", idMantenimiento);
        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + idMantenimiento));
        mantenimientoRepository.delete(mantenimiento);
    }

    private void validarHotelExiste(Integer idHotel) {
        try {
            hotelClient.obtenerHotelPorId(idHotel);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Hotel no encontrado con id: " + idHotel);
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al comunicarse con el microservicio hotel");
        }
    }

    private MantenimientoResponseDTO mapToResponse(MantenimientoModel mantenimiento) {
        return MantenimientoResponseDTO.builder()
                .idMantenimiento(mantenimiento.getId_mantenimiento())
                .idHotel(mantenimiento.getId_hotel())
                .descripcionMantenimiento(mantenimiento.getDescripcion_mantenimiento())
                .fechaInicio(mantenimiento.getFecha_inicio())
                .fechaTermino(mantenimiento.getFecha_termino())
                .costoMantenimiento(mantenimiento.getCosto_mantenimiento())
                .estadoMantenimiento(mantenimiento.getEstado_mantenimiento())
                .build();
    }
}