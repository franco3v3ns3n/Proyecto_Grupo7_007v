package com.veranum.estadia.services;

import com.veranum.estadia.clients.ClienteClient;
import com.veranum.estadia.clients.HabitacionClient;
import com.veranum.estadia.clients.ReservaClient;
import com.veranum.estadia.dtos.request.EstadiaRequestDTO;
import com.veranum.estadia.dtos.response.EstadiaResponseDTO;
import com.veranum.estadia.exceptions.RemoteServiceException;
import com.veranum.estadia.exceptions.ResourceNotFoundException;
import com.veranum.estadia.models.EstadiaModel;
import com.veranum.estadia.repositories.EstadiaRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private HabitacionClient habitacionClient;

    @Autowired
    private ReservaClient reservaClient;

    public List<EstadiaResponseDTO> obtenerEstadias() {
        log.info("Obteniendo estadías");

        return estadiaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EstadiaResponseDTO obtenerEstadiaPorId(Integer idEstadia) {
        log.info("Buscando estadía con id: {}", idEstadia);

        EstadiaModel estadia = estadiaRepository.findById(idEstadia)
                .orElseThrow(() -> new ResourceNotFoundException("Estadía no encontrada"));

        return mapToResponse(estadia);
    }

    public List<EstadiaResponseDTO> obtenerEstadiasPorCliente(Integer idCliente) {
        log.info("Obteniendo estadías del cliente con id: {}", idCliente);

        validarClienteExiste(idCliente);

        return estadiaRepository.findByIdCliente(idCliente)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EstadiaResponseDTO> obtenerEstadiasPorHabitacion(Integer idHabitacion) {
        log.info("Obteniendo estadías de la habitación con id: {}", idHabitacion);

        validarHabitacionExiste(idHabitacion);

        return estadiaRepository.findByIdHabitacion(idHabitacion)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EstadiaResponseDTO> obtenerEstadiasPorReserva(Integer idReserva) {
        log.info("Obteniendo estadías de la reserva con id: {}", idReserva);

        validarReservaExiste(idReserva);

        return estadiaRepository.findByIdReserva(idReserva)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EstadiaResponseDTO> obtenerEstadiasPorEstado(
            String estadoEstadia
    ) {
        log.info(
                "Obteniendo estadías con estado: {}",
                estadoEstadia
        );

        return estadiaRepository.findByEstadoEstadia(estadoEstadia)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EstadiaResponseDTO crearEstadia(EstadiaRequestDTO request) {
        log.info("Creando estadía para cliente: {}", request.getIdCliente());

        validarClienteExiste(request.getIdCliente());
        validarHabitacionExiste(request.getIdHabitacion());

        if (request.getIdReserva() != null) {
            validarReservaExiste(request.getIdReserva());
        }

        EstadiaModel estadia = EstadiaModel.builder()
                .idCliente(request.getIdCliente())
                .idHabitacion(request.getIdHabitacion())
                .idReserva(request.getIdReserva())
                .fechaCheckin(request.getFechaCheckin())
                .fechaCheckout(request.getFechaCheckout())
                .estadoEstadia(request.getEstadoEstadia())
                .build();

        EstadiaModel estadiaGuardada = estadiaRepository.save(estadia);

        return mapToResponse(estadiaGuardada);
    }

    public EstadiaResponseDTO actualizarEstadia(Integer idEstadia, EstadiaRequestDTO request) {
        log.info("Actualizando estadía con id: {}", idEstadia);

        EstadiaModel estadia = estadiaRepository.findById(idEstadia)
                .orElseThrow(() -> new ResourceNotFoundException("Estadía no encontrada"));

        validarClienteExiste(request.getIdCliente());
        validarHabitacionExiste(request.getIdHabitacion());

        if (request.getIdReserva() != null) {
            validarReservaExiste(request.getIdReserva());
        }

        estadia.setIdCliente(request.getIdCliente());
        estadia.setIdHabitacion(request.getIdHabitacion());
        estadia.setIdReserva(request.getIdReserva());
        estadia.setFechaCheckin(request.getFechaCheckin());
        estadia.setFechaCheckout(request.getFechaCheckout());
        estadia.setEstadoEstadia(request.getEstadoEstadia());

        EstadiaModel estadiaActualizada = estadiaRepository.save(estadia);

        return mapToResponse(estadiaActualizada);
    }

    public void eliminarEstadia(Integer idEstadia) {
        log.info("Eliminando estadía con id: {}", idEstadia);

        EstadiaModel estadia = estadiaRepository.findById(idEstadia)
                .orElseThrow(() -> new ResourceNotFoundException("Estadía no encontrada"));

        estadiaRepository.delete(estadia);
    }

    private void validarClienteExiste(Integer idCliente) {
        try {
            clienteClient.obtenerClientePorId(idCliente);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + idCliente);
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al comunicarse con el microservicio cliente");
        }
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

    private void validarReservaExiste(Integer idReserva) {
        try {
            reservaClient.obtenerReservaPorId(idReserva);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Reserva no encontrada con id: " + idReserva);
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al comunicarse con el microservicio reserva");
        }
    }

    private EstadiaResponseDTO mapToResponse(EstadiaModel estadia) {
        return EstadiaResponseDTO.builder()
                .idEstadia(estadia.getIdEstadia())
                .idCliente(estadia.getIdCliente())
                .idHabitacion(estadia.getIdHabitacion())
                .idReserva(estadia.getIdReserva())
                .fechaCheckin(estadia.getFechaCheckin())
                .fechaCheckout(estadia.getFechaCheckout())
                .estadoEstadia(estadia.getEstadoEstadia())
                .build();
    }
}
