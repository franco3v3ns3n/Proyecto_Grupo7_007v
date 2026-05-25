package com.veranum.empleado.services;

import com.veranum.empleado.clients.HotelClient;
import com.veranum.empleado.dtos.request.EmpleadoRequestDTO;
import com.veranum.empleado.dtos.response.EmpleadoResponseDTO;
import com.veranum.empleado.exceptions.RemoteServiceException;
import com.veranum.empleado.exceptions.ResourceNotFoundException;
import com.veranum.empleado.models.EmpleadoModel;
import com.veranum.empleado.repositories.EmpleadoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final HotelClient hotelClient;

    public EmpleadoService(
            EmpleadoRepository empleadoRepository,
            HotelClient hotelClient
    ) {
        this.empleadoRepository = empleadoRepository;
        this.hotelClient = hotelClient;
    }

    public List<EmpleadoResponseDTO> obtenerEmpleados() {
        log.info("Obteniendo empleados");

        return empleadoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmpleadoResponseDTO obtenerEmpleadoPorId(Integer idEmpleado) {
        log.info("Buscando empleado con id: {}", idEmpleado);

        EmpleadoModel empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));

        return mapToResponse(empleado);
    }

    public List<EmpleadoResponseDTO> obtenerEmpleadosPorHotel(Integer idHotel) {
        log.info("Obteniendo empleados del hotel con id: {}", idHotel);

        validarHotelExiste(idHotel);

        return empleadoRepository.findByIdHotel(idHotel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO request) {
        log.info("Creando empleado con rut: {}", request.getRut());

        validarHotelExiste(request.getIdHotel());

        EmpleadoModel empleado = EmpleadoModel.builder()
                .idHotel(request.getIdHotel())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .rut(request.getRut())
                .tipoEmpleado(request.getTipoEmpleado())
                .estadoEmpleado(request.getEstadoEmpleado())
                .build();

        EmpleadoModel empleadoGuardado = empleadoRepository.save(empleado);

        return mapToResponse(empleadoGuardado);
    }

    public EmpleadoResponseDTO actualizarEmpleado(
            Integer idEmpleado,
            EmpleadoRequestDTO request
    ) {
        log.info("Actualizando empleado con id: {}", idEmpleado);

        EmpleadoModel empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));

        validarHotelExiste(request.getIdHotel());

        empleado.setIdHotel(request.getIdHotel());
        empleado.setNombres(request.getNombres());
        empleado.setApellidos(request.getApellidos());
        empleado.setRut(request.getRut());
        empleado.setTipoEmpleado(request.getTipoEmpleado());
        empleado.setEstadoEmpleado(request.getEstadoEmpleado());

        EmpleadoModel empleadoActualizado = empleadoRepository.save(empleado);

        return mapToResponse(empleadoActualizado);
    }

    public void eliminarEmpleado(Integer idEmpleado) {
        log.info("Eliminando empleado con id: {}", idEmpleado);

        EmpleadoModel empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));

        empleadoRepository.delete(empleado);
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

    private EmpleadoResponseDTO mapToResponse(EmpleadoModel empleado) {
        return EmpleadoResponseDTO.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .idHotel(empleado.getIdHotel())
                .nombres(empleado.getNombres())
                .apellidos(empleado.getApellidos())
                .rut(empleado.getRut())
                .tipoEmpleado(empleado.getTipoEmpleado())
                .estadoEmpleado(empleado.getEstadoEmpleado())
                .build();
    }
}
