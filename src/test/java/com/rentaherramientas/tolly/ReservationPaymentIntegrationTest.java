package com.rentaherramientas.tolly;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.rentaherramientas.tolly.application.usecase.payment.PayPaymentUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationFinishedUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationIncidentUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationCancelledUseCase;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.CategoryEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.CategoryJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ClientJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.PaymentJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.PaymentStatusJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationDetailJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationStatusJpaRespository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.SupplierJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolStatusJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserStatusJpaRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservationPaymentIntegrationTest {

    @Autowired private PayPaymentUseCase payPaymentUseCase;
    @Autowired private ReservationCancelledUseCase reservationCancelledUseCase;
    @Autowired private MarkReservationIncidentUseCase markReservationIncidentUseCase;
    @Autowired private MarkReservationFinishedUseCase markReservationFinishedUseCase;

    @Autowired private UserJpaRepository userJpaRepository;
    @Autowired private UserStatusJpaRepository userStatusJpaRepository;
    @Autowired private ClientJpaRepository clientJpaRepository;
    @Autowired private SupplierJpaRepository supplierJpaRepository;
    @Autowired private CategoryJpaRepository categoryJpaRepository;
    @Autowired private ToolStatusJpaRepository toolStatusJpaRepository;
    @Autowired private ToolJpaRepository toolJpaRepository;
    @Autowired private ReservationStatusJpaRespository reservationStatusJpaRespository;
    @Autowired private ReservationJpaRepository reservationJpaRepository;
    @Autowired private ReservationDetailJpaRepository reservationDetailJpaRepository;
    @Autowired private PaymentStatusJpaRepository paymentStatusJpaRepository;
    @Autowired private PaymentJpaRepository paymentJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void payReservationMovesToInProgressAndMarksPaid() {
        UserEntity clientUser = createUser("client1@test.com");
        ClientEntity client = createClient(clientUser);

        ReservationStatusEntity reserved = ensureReservationStatus("RESERVED");
        ReservationStatusEntity inProgress = ensureReservationStatus("IN_PROGRESS");
        ensurePaymentStatus("PENDING");
        ensurePaymentStatus("PAID");

        ReservationEntity reservation = createReservation(client, reserved, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        PaymentEntity payment = createPayment(reservation, "PENDING");

        Payment paid = payPaymentUseCase.execute(reservation.getId(), clientUser.getId());

        ReservationEntity updatedReservation = reservationJpaRepository.findById(reservation.getId()).orElseThrow();
        PaymentEntity updatedPayment = paymentJpaRepository.findById(payment.getId()).orElseThrow();

        assertThat(paid.getStatus().getName()).isEqualTo("PAID");
        assertThat(updatedPayment.getStatus().getName()).isEqualTo("PAID");
        assertThat(updatedPayment.getPaymentDate()).isNotNull();
        assertThat(updatedReservation.getReservationStatus().getStatusName()).isEqualTo(inProgress.getStatusName());
    }

    @Test
    void cancelReservationOnlyAllowedWhenReservedAndBeforeStart() {
        UserEntity clientUser = createUser("client2@test.com");
        ClientEntity client = createClient(clientUser);

        ReservationStatusEntity reserved = ensureReservationStatus("RESERVED");
        ensureReservationStatus("CANCELLED");
        ensurePaymentStatus("PENDING");
        ensurePaymentStatus("CANCELLED");

        ReservationEntity reservation = createReservation(client, reserved, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
        PaymentEntity payment = createPayment(reservation, "PENDING");

        reservationCancelledUseCase.cancelReservation(reservation.getId(), clientUser.getId());

        ReservationEntity updatedReservation = reservationJpaRepository.findById(reservation.getId()).orElseThrow();
        PaymentEntity updatedPayment = paymentJpaRepository.findById(payment.getId()).orElseThrow();

        assertThat(updatedReservation.getReservationStatus().getStatusName()).isEqualTo("CANCELLED");
        assertThat(updatedPayment.getStatus().getName()).isEqualTo("CANCELLED");
    }

    @Test
    void supplierCanMarkIncidentAndFinishWhenOwnsReservationTools() {
        UserEntity supplierUser = createUser("supplier1@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);
        ToolEntity tool = createTool(supplier);

        UserEntity clientUser = createUser("client3@test.com");
        ClientEntity client = createClient(clientUser);

        ReservationStatusEntity inProgress = ensureReservationStatus("IN_PROGRESS");
        ensureReservationStatus("IN_INCIDENT");
        ensureReservationStatus("FINISHED");
        ensurePaymentStatus("PAID");

        ReservationEntity reservation = createReservation(client, inProgress, LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
        createReservationDetail(reservation, tool);
        createPayment(reservation, "PAID");
        flushAndClear();

        markReservationIncidentUseCase.execute(reservation.getId(), supplierUser.getId(), false);

        ReservationEntity incidentReservation = reservationJpaRepository.findById(reservation.getId()).orElseThrow();
        assertThat(incidentReservation.getReservationStatus().getStatusName()).isEqualTo("IN_INCIDENT");

        markReservationFinishedUseCase.execute(reservation.getId(), supplierUser.getId(), false);

        ReservationEntity finishedReservation = reservationJpaRepository.findById(reservation.getId()).orElseThrow();
        assertThat(finishedReservation.getReservationStatus().getStatusName()).isEqualTo("FINISHED");
    }

    @Test
    void supplierCannotMarkIncidentWhenNotOwner() {
        UserEntity supplierUser = createUser("supplier2@test.com");
        createSupplier(supplierUser);

        SupplierEntity otherSupplier = createSupplier(createUser("supplier3@test.com"));
        ToolEntity tool = createTool(otherSupplier);

        UserEntity clientUser = createUser("client4@test.com");
        ClientEntity client = createClient(clientUser);

        ReservationStatusEntity inProgress = ensureReservationStatus("IN_PROGRESS");
        ensureReservationStatus("IN_INCIDENT");
        ensurePaymentStatus("PAID");

        ReservationEntity reservation = createReservation(client, inProgress, LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
        createReservationDetail(reservation, tool);
        createPayment(reservation, "PAID");
        flushAndClear();

        assertThrows(IllegalStateException.class,
                () -> markReservationIncidentUseCase.execute(reservation.getId(), supplierUser.getId(), false));
    }

    private UserEntity createUser(String email) {
        UserStatusEntity status = userStatusJpaRepository.findByName("ACTIVE")
                .orElseGet(() -> userStatusJpaRepository.save(new UserStatusEntity(null, "ACTIVE")));

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setPassword("test123");
        user.setStatus(status);
        return userJpaRepository.save(user);
    }

    private ClientEntity createClient(UserEntity user) {
        ClientEntity client = new ClientEntity();
        client.setUserId(user);
        client.setFirstName("Test");
        client.setLastName("Client");
        client.setNationalId(UUID.randomUUID().toString().substring(0, 10));
        client.setAddress("Address 123");
        client.setPhoneNumber("3101234567");
        return clientJpaRepository.save(client);
    }

    private SupplierEntity createSupplier(UserEntity user) {
        SupplierEntity supplier = new SupplierEntity();
        supplier.setUserId(user);
        supplier.setPhone("3101234567");
        supplier.setCompanyName("Test Company");
        supplier.setIdentification(UUID.randomUUID().toString().substring(0, 10));
        supplier.setContactName("Supplier Contact");
        return supplierJpaRepository.save(supplier);
    }

    private ToolEntity createTool(SupplierEntity supplier) {
        CategoryEntity category = new CategoryEntity();
        category.setName("Test Category");
        category = categoryJpaRepository.save(category);

        ToolStatusEntity toolStatus = new ToolStatusEntity();
        toolStatus.setName("AVAILABLE");
        toolStatus = toolStatusJpaRepository.save(toolStatus);

        ToolEntity tool = new ToolEntity();
        tool.setName("Test Tool");
        tool.setDescription("Desc");
        tool.setDailyPrice(10.0);
        tool.setTotalQuantity(5);
        tool.setAvailableQuantity(5);
        tool.setToolStatus(toolStatus);
        tool.setSupplier(supplier);
        tool.setCategory(category);
        return toolJpaRepository.save(tool);
    }

    private ReservationEntity createReservation(ClientEntity client, ReservationStatusEntity status,
                                                LocalDate start, LocalDate end) {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setClient(client);
        reservation.setReservationStatus(status);
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setTotalPrice(BigDecimal.valueOf(100));
        reservation.setCreatedAt(LocalDate.now());
        return reservationJpaRepository.save(reservation);
    }

    private ReservationDetailEntity createReservationDetail(ReservationEntity reservation, ToolEntity tool) {
        ReservationDetailEntity detail = new ReservationDetailEntity();
        detail.setReservationId(reservation.getId());
        detail.setToolId(tool.getId());
        detail.setDailyPrice(BigDecimal.valueOf(10));
        detail.setRentalDay(2);
        detail.setSubTotal(BigDecimal.valueOf(20));
        return reservationDetailJpaRepository.save(detail);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    private PaymentEntity createPayment(ReservationEntity reservation, String statusName) {
        PaymentStatusEntity status = ensurePaymentStatus(statusName);
        PaymentEntity payment = new PaymentEntity();
        payment.setReservation(reservation);
        payment.setAmount(reservation.getTotalPrice());
        payment.setStatus(status);
        payment.setPaymentDate("PAID".equalsIgnoreCase(statusName) ? LocalDateTime.now() : null);
        return paymentJpaRepository.save(payment);
    }

    private ReservationStatusEntity ensureReservationStatus(String name) {
        return reservationStatusJpaRespository.findByStatusName(name)
                .orElseGet(() -> {
                    ReservationStatusEntity status = new ReservationStatusEntity();
                    status.setStatusName(name);
                    return reservationStatusJpaRespository.save(status);
                });
    }

    private PaymentStatusEntity ensurePaymentStatus(String name) {
        return paymentStatusJpaRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> paymentStatusJpaRepository.save(new PaymentStatusEntity(null, name)));
    }
}
