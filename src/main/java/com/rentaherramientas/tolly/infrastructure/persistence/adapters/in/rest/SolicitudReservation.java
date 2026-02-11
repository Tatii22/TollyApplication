package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.returns.RechazarSolicitudUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.SolicitudReservationUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/solicitud/supplier")
@Tag(name = "Returns", description = "Gestion de devoluciones")
public class SolicitudReservation {
    private final SolicitudReservationUseCase solicitudReserva;
    private final ReservationRequest reservationRequest;
    private final RechazarSolicitudUseCase rechazarSolicitudUseCase;


    

    public SolicitudReservation(SolicitudReservationUseCase solicitudReserva, ReservationRequest reservationRequest,
            RechazarSolicitudUseCase rechazarSolicitudUseCase) {
        this.solicitudReserva = solicitudReserva;
        this.reservationRequest = reservationRequest;
        this.rechazarSolicitudUseCase = rechazarSolicitudUseCase;
    }

    @GetMapping("{id}/{status}")
    public ResponseEntity<List<ReservationResponse>> solicitud(@PathVariable(name = "id") long id, 
                                                         @PathVariable(name = "status") String status) {
        List<ReservationResponse> response = solicitudReserva.aprobarReserva(status, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity putMethodName(@PathVariable String id) {

        return ResponseEntity.ok(rechazarSolicitudUseCase.Rechazo(id,reservationRequest))
        
    }
    
}
