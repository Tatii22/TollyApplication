package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationStatusRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationStatusResponse;
import com.rentaherramientas.tolly.application.mapper.ReservationStatusMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;

@Service
public class RechazarSolicitudUseCase {
    private final ReservationStatusRepository reservationStatus;
    private final ReservationRepository reservation;
    private final ReservationStatusMapper mapper;

    


    public RechazarSolicitudUseCase(ReservationStatusRepository reservationStatus, ReservationRepository reservation,
            ReservationStatusMapper mapper) {
        this.reservationStatus = reservationStatus;
        this.reservation = reservation;
        this.mapper = mapper;
    }




    public ReservationStatusResponse Rechazo(Long idReserva, ReservationStatusRequest request){

        reservationStatus.findByStatusName(request.statusName()).ifPresent(
            estado ->{
                if(!estado.getId_reservacion().equals(idReserva)){
                    throw new DomainException("Ya existe otra categoría con el nombre: " + request.statusName());
                }
            }
        );

        Reservation actualizar = new Reservation(idReserva, request.statusName());
        Reservation saved = reservationStatus.save(actualizar);

        return mapper.
        
    }

    
}
