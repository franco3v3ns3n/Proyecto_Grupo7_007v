package com.veranum.habitacion.services;

import com.veranum.habitacion.clients.HotelClient;
import com.veranum.habitacion.dtos.request.HabitacionRequestDTO;
import com.veranum.habitacion.dtos.response.HabitacionResponseDTO;
import com.veranum.habitacion.exceptions.RemoteServiceException;
import com.veranum.habitacion.exceptions.ResourceNotFoundException;
import com.veranum.habitacion.models.HabitacionModel;
import com.veranum.habitacion.repositories.HabitacionRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HotelClient hotelClient;

    public List<HabitacionResponseDTO> obtenerHabitaciones() {
        log.info("Obteniendo todas las habitaciones");

        return habitacionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public HabitacionResponseDTO obtenerHabitacionPorId(Integer idHabitacion) {
        log.info("Buscando habitación con id: {}", idHabitacion);

        HabitacionModel habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idHabitacion));

        return mapToResponse(habitacion);
    }

    public List<HabitacionResponseDTO> obtenerHabitacionesPorHotel(Integer idHotel) {
        log.info("Obteniendo habitaciones del hotel con id: {}", idHotel);

        validarHotelExiste(idHotel);

        return habitacionRepository.findByIdHotel(idHotel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<HabitacionResponseDTO> obtenerHabitacionesPorHotelYTipo(
            Integer idHotel,
            String tipoHabitacion
    ) {
        log.info(
                "Obteniendo habitaciones del hotel con id: {} y tipo: {}",
                idHotel,
                tipoHabitacion
        );

        validarHotelExiste(idHotel);

        return habitacionRepository.findByIdHotelAndTipoHabitacion(idHotel, tipoHabitacion)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<HabitacionResponseDTO> obtenerHabitacionesPorHotelYEstado(
            Integer idHotel,
            String estadoHabitacion
    ) {
        log.info(
                "Obteniendo habitaciones del hotel con id: {} y estado: {}",
                idHotel,
                estadoHabitacion
        );

        validarHotelExiste(idHotel);

        return habitacionRepository.findByIdHotelAndEstadoHabitacion(idHotel, estadoHabitacion)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public HabitacionResponseDTO crearHabitacion(HabitacionRequestDTO request) {
        log.info("Creando habitación número: {}", request.getNumeroHabitacion());

        validarHotelExiste(request.getIdHotel());

        HabitacionModel habitacion = HabitacionModel.builder()
                .idHotel(request.getIdHotel())
                .tipoHabitacion(request.getTipoHabitacion())
                .numeroHabitacion(request.getNumeroHabitacion())
                .capacidadPersonas(request.getCapacidadPersonas())
                .cantidadCamas(request.getCantidadCamas())
                .cantidadBanos(request.getCantidadBanos())
                .precioDiario(request.getPrecioDiario())
                .estadoHabitacion(request.getEstadoHabitacion())
                .build();

        HabitacionModel habitacionGuardada = habitacionRepository.save(habitacion);

        return mapToResponse(habitacionGuardada);
    }

    public HabitacionResponseDTO actualizarHabitacion(Integer idHabitacion, HabitacionRequestDTO request) {
        log.info("Actualizando habitación con id: {}", idHabitacion);

        HabitacionModel habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idHabitacion));

        validarHotelExiste(request.getIdHotel());

        habitacion.setIdHotel(request.getIdHotel());
        habitacion.setTipoHabitacion(request.getTipoHabitacion());
        habitacion.setNumeroHabitacion(request.getNumeroHabitacion());
        habitacion.setCapacidadPersonas(request.getCapacidadPersonas());
        habitacion.setCantidadCamas(request.getCantidadCamas());
        habitacion.setCantidadBanos(request.getCantidadBanos());
        habitacion.setPrecioDiario(request.getPrecioDiario());
        habitacion.setEstadoHabitacion(request.getEstadoHabitacion());

        HabitacionModel habitacionActualizada = habitacionRepository.save(habitacion);

        return mapToResponse(habitacionActualizada);
    }

    public void eliminarHabitacion(Integer idHabitacion) {
        log.info("Eliminando habitación con id: {}", idHabitacion);

        HabitacionModel habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idHabitacion));

        habitacionRepository.delete(habitacion);
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

    private HabitacionResponseDTO mapToResponse(HabitacionModel habitacion) {
        return HabitacionResponseDTO.builder()
                .idHabitacion(habitacion.getIdHabitacion())
                .idHotel(habitacion.getIdHotel())
                .tipoHabitacion(habitacion.getTipoHabitacion())
                .numeroHabitacion(habitacion.getNumeroHabitacion())
                .capacidadPersonas(habitacion.getCapacidadPersonas())
                .cantidadCamas(habitacion.getCantidadCamas())
                .cantidadBanos(habitacion.getCantidadBanos())
                .precioDiario(habitacion.getPrecioDiario())
                .estadoHabitacion(habitacion.getEstadoHabitacion())
                .build();
    }
}
