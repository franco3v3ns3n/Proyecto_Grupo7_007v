package com.veranum.pago.services;

import com.veranum.pago.clients.EstadiaClient;
import com.veranum.pago.dtos.request.PagoRequestDTO;
import com.veranum.pago.dtos.response.PagoResponseDTO;
import com.veranum.pago.exceptions.RemoteServiceException;
import com.veranum.pago.exceptions.ResourceNotFoundException;
import com.veranum.pago.models.PagoModel;
import com.veranum.pago.repositories.PagoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;
    private final EstadiaClient estadiaClient;

    public PagoService(
            PagoRepository pagoRepository,
            EstadiaClient estadiaClient
    ) {
        this.pagoRepository = pagoRepository;
        this.estadiaClient = estadiaClient;
    }

    public List<PagoResponseDTO> obtenerPagos() {
        log.info("Obteniendo pagos");

        return pagoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PagoResponseDTO obtenerPagoPorId(Integer idPago) {
        log.info("Buscando pago con id: {}", idPago);

        PagoModel pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        return mapToResponse(pago);
    }

    public List<PagoResponseDTO> obtenerPagosPorEstadia(Integer idEstadia) {
        log.info("Obteniendo pagos de la estadía con id: {}", idEstadia);

        validarEstadiaExiste(idEstadia);

        return pagoRepository.findByIdEstadia(idEstadia)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PagoResponseDTO crearPago(PagoRequestDTO request) {
        log.info("Creando pago para estadía: {}", request.getIdEstadia());

        validarEstadiaExiste(request.getIdEstadia());

        PagoModel pago = PagoModel.builder()
                .idEstadia(request.getIdEstadia())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estadoPago(request.getEstadoPago())
                .fechaPago(request.getFechaPago())
                .build();

        PagoModel pagoGuardado = pagoRepository.save(pago);

        return mapToResponse(pagoGuardado);
    }

    public PagoResponseDTO actualizarPago(
            Integer idPago,
            PagoRequestDTO request
    ) {
        log.info("Actualizando pago con id: {}", idPago);

        PagoModel pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        validarEstadiaExiste(request.getIdEstadia());

        pago.setIdEstadia(request.getIdEstadia());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstadoPago(request.getEstadoPago());
        pago.setFechaPago(request.getFechaPago());

        PagoModel pagoActualizado = pagoRepository.save(pago);

        return mapToResponse(pagoActualizado);
    }

    public void eliminarPago(Integer idPago) {
        log.info("Eliminando pago con id: {}", idPago);

        PagoModel pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        pagoRepository.delete(pago);
    }

    private void validarEstadiaExiste(Integer idEstadia) {
        try {
            estadiaClient.obtenerEstadiaPorId(idEstadia);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Estadía no encontrada con id: " + idEstadia);
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al comunicarse con el microservicio estadia");
        }
    }

    private PagoResponseDTO mapToResponse(PagoModel pago) {
        return PagoResponseDTO.builder()
                .idPago(pago.getIdPago())
                .idEstadia(pago.getIdEstadia())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estadoPago(pago.getEstadoPago())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
