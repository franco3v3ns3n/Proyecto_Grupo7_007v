package com.veranum.reserva.services;

import com.veranum.reserva.clients.ClienteClient;
import com.veranum.reserva.clients.HabitacionClient;
import com.veranum.reserva.dtos.request.ReservaRequestDTO;
import com.veranum.reserva.dtos.response.ReservaResponseDTO;
import com.veranum.reserva.exceptions.RemoteServiceException;
import com.veranum.reserva.exceptions.ResourceNotFoundException;
import com.veranum.reserva.models.ReservaModel;
import com.veranum.reserva.repositories.ReservaRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteClient clienteClient;
    private final HabitacionClient habitacionClient;

    public ReservaService(
            ReservaRepository reservaRepository,
            ClienteClient clienteClient,
            HabitacionClient habitacionClient
    ) {
        this.reservaRepository = reservaRepository;
        this.clienteClient = clienteClient;
        this.habitacionClient = habitacionClient;
    }

    public List<ReservaResponseDTO> obtenerReservas() {
        log.info("Obteniendo reservas");

        return reservaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ReservaResponseDTO obtenerReservaPorId(Integer idReserva) {
        log.info("Buscando reserva con id: {}", idReserva);

        ReservaModel reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        return mapToResponse(reserva);
    }

    public List<ReservaResponseDTO> obtenerReservasPorCliente(Integer idCliente) {
        log.info("Obteniendo reservas del cliente con id: {}", idCliente);

        validarClienteExiste(idCliente);

        return reservaRepository.findByIdCliente(idCliente)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReservaResponseDTO> obtenerReservasPorHabitacion(Integer idHabitacion) {
        log.info("Obteniendo reservas de la habitación con id: {}", idHabitacion);

        validarHabitacionExiste(idHabitacion);

        return reservaRepository.findByIdHabitacion(idHabitacion)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ReservaResponseDTO crearReserva(ReservaRequestDTO request) {
        log.info("Creando reserva para cliente {} y habitación {}", request.getIdCliente(), request.getIdHabitacion());

        validarClienteExiste(request.getIdCliente());
        validarHabitacionExiste(request.getIdHabitacion());

        ReservaModel reserva = ReservaModel.builder()
                .idCliente(request.getIdCliente())
                .idHabitacion(request.getIdHabitacion())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estadoReserva(request.getEstadoReserva())
                .fechaCreacion(LocalDateTime.now())
                .build();

        ReservaModel reservaGuardada = reservaRepository.save(reserva);

        return mapToResponse(reservaGuardada);
    }

    public ReservaResponseDTO actualizarReserva(Integer idReserva, ReservaRequestDTO request) {
        log.info("Actualizando reserva con id: {}", idReserva);

        ReservaModel reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        validarClienteExiste(request.getIdCliente());
        validarHabitacionExiste(request.getIdHabitacion());

        reserva.setIdCliente(request.getIdCliente());
        reserva.setIdHabitacion(request.getIdHabitacion());
        reserva.setFechaInicio(request.getFechaInicio());
        reserva.setFechaFin(request.getFechaFin());
        reserva.setEstadoReserva(request.getEstadoReserva());

        ReservaModel reservaActualizada = reservaRepository.save(reserva);

        return mapToResponse(reservaActualizada);
    }

    public void eliminarReserva(Integer idReserva) {
        log.info("Eliminando reserva con id: {}", idReserva);

        ReservaModel reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        reservaRepository.delete(reserva);
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
            throw new RemoteServiceException("Error al comunicarse con el microservicio habitación");
        }
    }

    private ReservaResponseDTO mapToResponse(ReservaModel reserva) {
        return ReservaResponseDTO.builder()
                .idReserva(reserva.getIdReserva())
                .idCliente(reserva.getIdCliente())
                .idHabitacion(reserva.getIdHabitacion())
                .fechaInicio(reserva.getFechaInicio())
                .fechaFin(reserva.getFechaFin())
                .estadoReserva(reserva.getEstadoReserva())
                .fechaCreacion(reserva.getFechaCreacion())
                .build();
    }
}
