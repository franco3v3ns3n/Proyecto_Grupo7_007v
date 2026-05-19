package com.veranum.evento.services;

import com.veranum.evento.clients.HotelClient;
import com.veranum.evento.dtos.request.CentroEventoRequestDTO;
import com.veranum.evento.dtos.response.CentroEventoResponseDTO;
import com.veranum.evento.exceptions.RemoteServiceException;
import com.veranum.evento.exceptions.ResourceNotFoundException;
import com.veranum.evento.models.CentroEventoModel;
import com.veranum.evento.repositories.CentroEventoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CentroEventoService {

    private final CentroEventoRepository centroEventoRepository;
    private final HotelClient hotelClient;

    public CentroEventoService(CentroEventoRepository centroEventoRepository, HotelClient hotelClient) {
        this.centroEventoRepository = centroEventoRepository;
        this.hotelClient = hotelClient;
    }

    public List<CentroEventoResponseDTO> obtenerCentrosEventos() {
        log.info("Obteniendo centros de eventos");

        return centroEventoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CentroEventoResponseDTO obtenerCentroEventoPorId(Integer idCentroEvento) {
        log.info("Buscando centro de evento con id: {}", idCentroEvento);

        CentroEventoModel centroEvento = centroEventoRepository.findById(idCentroEvento)
                .orElseThrow(() -> new ResourceNotFoundException("Centro de evento no encontrado"));

        return mapToResponse(centroEvento);
    }

    public List<CentroEventoResponseDTO> obtenerCentrosEventosPorHotel(Integer idHotel) {
        log.info("Obteniendo centros de eventos del hotel con id: {}", idHotel);

        validarHotelExiste(idHotel);

        return centroEventoRepository.findByIdHotel(idHotel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CentroEventoResponseDTO crearCentroEvento(CentroEventoRequestDTO request) {
        log.info("Creando centro de evento: {}", request.getNombre());

        validarHotelExiste(request.getIdHotel());

        CentroEventoModel centroEvento = CentroEventoModel.builder()
                .idHotel(request.getIdHotel())
                .nombre(request.getNombre())
                .capacidadPersonas(request.getCapacidadPersonas())
                .precioCentroEvento(request.getPrecioCentroEvento())
                .estadoCentroEvento(request.getEstadoCentroEvento())
                .build();

        CentroEventoModel centroEventoGuardado = centroEventoRepository.save(centroEvento);

        return mapToResponse(centroEventoGuardado);
    }

    public CentroEventoResponseDTO actualizarCentroEvento(Integer idCentroEvento, CentroEventoRequestDTO request) {
        log.info("Actualizando centro de evento con id: {}", idCentroEvento);

        CentroEventoModel centroEvento = centroEventoRepository.findById(idCentroEvento)
                .orElseThrow(() -> new ResourceNotFoundException("Centro de evento no encontrado"));

        validarHotelExiste(request.getIdHotel());

        centroEvento.setIdHotel(request.getIdHotel());
        centroEvento.setNombre(request.getNombre());
        centroEvento.setCapacidadPersonas(request.getCapacidadPersonas());
        centroEvento.setPrecioCentroEvento(request.getPrecioCentroEvento());
        centroEvento.setEstadoCentroEvento(request.getEstadoCentroEvento());

        CentroEventoModel centroEventoActualizado = centroEventoRepository.save(centroEvento);

        return mapToResponse(centroEventoActualizado);
    }

    public void eliminarCentroEvento(Integer idCentroEvento) {
        log.info("Eliminando centro de evento con id: {}", idCentroEvento);

        CentroEventoModel centroEvento = centroEventoRepository.findById(idCentroEvento)
                .orElseThrow(() -> new ResourceNotFoundException("Centro de evento no encontrado"));

        centroEventoRepository.delete(centroEvento);
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

    private CentroEventoResponseDTO mapToResponse(CentroEventoModel centroEvento) {
        return CentroEventoResponseDTO.builder()
                .idCentroEvento(centroEvento.getIdCentroEvento())
                .idHotel(centroEvento.getIdHotel())
                .nombre(centroEvento.getNombre())
                .capacidadPersonas(centroEvento.getCapacidadPersonas())
                .precioCentroEvento(centroEvento.getPrecioCentroEvento())
                .estadoCentroEvento(centroEvento.getEstadoCentroEvento())
                .build();
    }
}