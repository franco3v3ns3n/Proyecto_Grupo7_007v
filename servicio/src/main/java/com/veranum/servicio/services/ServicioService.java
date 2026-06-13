package com.veranum.servicio.services;

import com.veranum.servicio.clients.HotelClient;
import com.veranum.servicio.dtos.request.ServicioRequestDTO;
import com.veranum.servicio.dtos.response.ServicioResponseDTO;
import com.veranum.servicio.exceptions.RemoteServiceException;
import com.veranum.servicio.exceptions.ResourceNotFoundException;
import com.veranum.servicio.models.ServicioModel;
import com.veranum.servicio.repositories.ServicioRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private HotelClient hotelClient;

    public List<ServicioResponseDTO> obtenerServicios() {
        log.info("Obteniendo servicios");

        return servicioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ServicioResponseDTO obtenerServicioPorId(Integer idServicio) {
        log.info("Buscando servicio con id: {}", idServicio);

        ServicioModel servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        return mapToResponse(servicio);
    }

    public List<ServicioResponseDTO> obtenerServiciosPorHotel(Integer idHotel) {
        log.info("Obteniendo servicios del hotel con id: {}", idHotel);

        validarHotelExiste(idHotel);

        return servicioRepository.findByIdHotel(idHotel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ServicioResponseDTO> obtenerServiciosPorEstado(
            String estadoServicio
    ) {
        log.info(
                "Obteniendo servicios con estado: {}",
                estadoServicio
        );

        return servicioRepository.findByEstadoServicio(estadoServicio)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ServicioResponseDTO crearServicio(ServicioRequestDTO request) {
        log.info("Creando servicio: {}", request.getNombre());

        validarHotelExiste(request.getIdHotel());

        ServicioModel servicio = ServicioModel.builder()
                .idHotel(request.getIdHotel())
                .nombre(request.getNombre())
                .valorDiario(request.getValorDiario())
                .estadoServicio(request.getEstadoServicio())
                .build();

        ServicioModel servicioGuardado = servicioRepository.save(servicio);

        return mapToResponse(servicioGuardado);
    }

    public ServicioResponseDTO actualizarServicio(Integer idServicio, ServicioRequestDTO request) {
        log.info("Actualizando servicio con id: {}", idServicio);

        ServicioModel servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        validarHotelExiste(request.getIdHotel());

        servicio.setIdHotel(request.getIdHotel());
        servicio.setNombre(request.getNombre());
        servicio.setValorDiario(request.getValorDiario());
        servicio.setEstadoServicio(request.getEstadoServicio());

        ServicioModel servicioActualizado = servicioRepository.save(servicio);

        return mapToResponse(servicioActualizado);
    }

    public void eliminarServicio(Integer idServicio) {
        log.info("Eliminando servicio con id: {}", idServicio);

        ServicioModel servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        servicioRepository.delete(servicio);
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

    private ServicioResponseDTO mapToResponse(ServicioModel servicio) {
        return ServicioResponseDTO.builder()
                .idServicio(servicio.getIdServicio())
                .idHotel(servicio.getIdHotel())
                .nombre(servicio.getNombre())
                .valorDiario(servicio.getValorDiario())
                .estadoServicio(servicio.getEstadoServicio())
                .build();
    }
}