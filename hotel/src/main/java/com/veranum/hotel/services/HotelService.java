package com.veranum.hotel.services;

import com.veranum.hotel.dtos.request.HotelRequestDTO;
import com.veranum.hotel.dtos.response.HotelResponseDTO;
import com.veranum.hotel.exceptions.ResourceNotFoundException;
import com.veranum.hotel.models.HotelModel;
import com.veranum.hotel.repositories.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelResponseDTO> obtenerHoteles() {
        log.info("Obteniendo hoteles");

        return hotelRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public HotelResponseDTO obtenerHotelPorId(Integer idHotel) {
        log.info("Buscando hotel con id: {}", idHotel);

        HotelModel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));

        return mapToResponse(hotel);
    }

    public HotelResponseDTO crearHotel(HotelRequestDTO request) {
        log.info("Creando hotel: {}", request.getNombre());

        HotelModel hotel = HotelModel.builder()
                .nombre(request.getNombre())
                .ubicacion(request.getUbicacion())
                .build();

        HotelModel hotelGuardado = hotelRepository.save(hotel);

        return mapToResponse(hotelGuardado);
    }

    public HotelResponseDTO actualizarHotel(Integer idHotel, HotelRequestDTO request) {
        log.info("Actualizando hotel con id: {}", idHotel);

        HotelModel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));

        hotel.setNombre(request.getNombre());
        hotel.setUbicacion(request.getUbicacion());

        HotelModel hotelActualizado = hotelRepository.save(hotel);

        return mapToResponse(hotelActualizado);
    }

    public void eliminarHotel(Integer idHotel) {
        log.info("Eliminando hotel con id: {}", idHotel);

        HotelModel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));

        hotelRepository.delete(hotel);
    }

    private HotelResponseDTO mapToResponse(HotelModel hotel) {
        return HotelResponseDTO.builder()
                .idHotel(hotel.getIdHotel())
                .nombre(hotel.getNombre())
                .ubicacion(hotel.getUbicacion())
                .build();
    }
}