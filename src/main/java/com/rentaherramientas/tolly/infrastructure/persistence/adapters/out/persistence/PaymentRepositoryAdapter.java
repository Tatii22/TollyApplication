package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.application.mapper.PaymentMapper;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.PaymentJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.PaymentStatusJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationJpaRepository;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final PaymentStatusJpaRepository paymentStatusJpaRepository;

    public PaymentRepositoryAdapter(PaymentJpaRepository paymentJpaRepository,
                                    ReservationJpaRepository reservationJpaRepository,
                                    PaymentStatusJpaRepository paymentStatusJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.reservationJpaRepository = reservationJpaRepository;
        this.paymentStatusJpaRepository = paymentStatusJpaRepository;
    }

    @Override
    public Payment save(Payment payment) {

        Long reservationId = payment.getReservation().getId();

        PaymentStatusEntity statusEntity = paymentStatusJpaRepository
                .findByNameIgnoreCase(payment.getStatus().getName())
                .orElseThrow(() -> new IllegalArgumentException("PaymentStatus not found: " + payment.getStatus().getName()));

        if (payment.getId() != null) {
            PaymentEntity existing = paymentJpaRepository.findById(payment.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + payment.getId()));
            existing.setAmount(payment.getAmount());
            existing.setPaymentDate(payment.getPaymentDate());
            existing.setStatus(statusEntity);
            PaymentEntity saved = paymentJpaRepository.save(existing);
            return PaymentMapper.toDomain(saved);
        }

        if (paymentJpaRepository.existsByReservation_Id(reservationId)) {
            throw new IllegalStateException("Payment already exists for reservation " + reservationId);
        }

        ReservationEntity reservationEntity = reservationJpaRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

        PaymentEntity entity = new PaymentEntity(
                null,
                reservationEntity,
                payment.getAmount(),
                payment.getPaymentDate(),
                statusEntity
        );

        PaymentEntity saved = paymentJpaRepository.save(entity);

        return PaymentMapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentJpaRepository.findById(id).map(PaymentMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByReservationId(Long reservationId) {
        return paymentJpaRepository.findByReservation_Id(reservationId).map(PaymentMapper::toDomain);
    }

    @Override
    public boolean existsByReservationId(Long reservationId) {
        return paymentJpaRepository.existsByReservation_Id(reservationId);
    }

    @Override
    public List<Payment> findAll() {
        return paymentJpaRepository.findAll()
                .stream()
                .map(PaymentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Payment> findByClientId(Long clientId) {
        return paymentJpaRepository.findByReservation_Client_Id(clientId)
                .stream()
                .map(PaymentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Payment> findByStatusName(String statusName) {
        return paymentJpaRepository.findByStatus_NameIgnoreCase(statusName)
                .stream()
                .map(PaymentMapper::toDomain)
                .toList();
    }
}
