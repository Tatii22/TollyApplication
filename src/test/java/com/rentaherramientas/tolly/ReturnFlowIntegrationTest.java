package com.rentaherramientas.tolly;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.CategoryEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnStatusEntity;
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
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReturnJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReturnStatusJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.SupplierJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolStatusJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserStatusJpaRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReturnFlowIntegrationTest {

    private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;

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
    @Autowired private ReturnStatusJpaRepository returnStatusJpaRepository;
    @Autowired private ReturnJpaRepository returnJpaRepository;
    @Autowired private PaymentStatusJpaRepository paymentStatusJpaRepository;
    @Autowired private PaymentJpaRepository paymentJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    void clientCanCreateAndConfirmReturn() throws Exception {
        UserEntity supplierUser = createUser("supplier_return1@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);
        ToolEntity tool = createTool(supplier, "AVAILABLE");

        UserEntity clientUser = createUser("client_return1@test.com");
        ClientEntity client = createClient(clientUser);

        ensureReservationStatus("IN_PROGRESS");
        ensureReturnStatus("PENDING");
        ensureReturnStatus("SENT");

        ReservationEntity reservation = createReservation(
            client, "IN_PROGRESS", LocalDate.now(), LocalDate.now().plusDays(1));
        createReservationDetail(reservation, tool, 2);
        flushAndClear();

        mockMvc.perform(post("/returns")
                .contentType("application/json")
                .content("""
                    {
                      "reservationId": %d,
                      "returnDate": "%s",
                      "details": [
                        { "toolId": %d, "quantity": 1, "observations": "OK" }
                      ],
                      "observations": "Return created"
                    }
                    """.formatted(reservation.getId(), LocalDate.now().toString(), tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.returnStatusName").value("PENDING"));

        ReturnEntity created = returnJpaRepository.findAll().get(0);

        mockMvc.perform(put("/returns/{id}/confirm", created.getId())
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.returnStatusName").value("SENT"));
    }

    @Test
    void supplierReceivesReturnAndFinishesReservation() throws Exception {
        UserEntity supplierUser = createUser("supplier_return2@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);
        ToolEntity tool = createTool(supplier, "AVAILABLE");

        UserEntity clientUser = createUser("client_return2@test.com");
        ClientEntity client = createClient(clientUser);

        ensureReservationStatus("IN_PROGRESS");
        ensureReservationStatus("FINISHED");
        ensureReturnStatus("PENDING");
        ensureReturnStatus("SENT");
        ensureReturnStatus("RECEIVED");
        ensureToolStatus("AVAILABLE");
        ensurePaymentStatus("PAID");

        ReservationEntity reservation = createReservation(
            client, "IN_PROGRESS", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        createReservationDetail(reservation, tool, 1);
        createPayment(reservation, "PAID");
        flushAndClear();

        mockMvc.perform(post("/returns")
                .contentType("application/json")
                .content("""
                    {
                      "reservationId": %d,
                      "returnDate": "%s",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ]
                    }
                    """.formatted(reservation.getId(), LocalDate.now().toString(), tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isCreated());

        ReturnEntity created = returnJpaRepository.findAll().get(0);

        mockMvc.perform(put("/returns/{id}/confirm", created.getId())
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isOk());

        mockMvc.perform(put("/returns/{id}/receive", created.getId())
                .contentType("application/json")
                .content("""
                    {
                      "returnStatusName": "RECEIVED",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ],
                      "observations": "Received ok"
                    }
                    """.formatted(tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(supplierUser.getId(), "ROLE_SUPPLIER"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.returnStatusName").value("RECEIVED"));

        ReservationEntity updatedReservation = reservationJpaRepository.findById(reservation.getId()).orElseThrow();
        ToolEntity updatedTool = toolJpaRepository.findById(tool.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("FINISHED", updatedReservation.getReservationStatus().getStatusName());
        org.junit.jupiter.api.Assertions.assertEquals("AVAILABLE", updatedTool.getToolStatus().getName());
    }

    @Test
    void supplierReceivesDamagedReturnAndMarksIncident() throws Exception {
        UserEntity supplierUser = createUser("supplier_return3@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);
        ToolEntity tool = createTool(supplier, "AVAILABLE");

        UserEntity clientUser = createUser("client_return3@test.com");
        ClientEntity client = createClient(clientUser);

        ensureReservationStatus("IN_PROGRESS");
        ensureReservationStatus("IN_INCIDENT");
        ensureReturnStatus("PENDING");
        ensureReturnStatus("SENT");
        ensureReturnStatus("DAMAGED");
        ensureToolStatus("UNAVAILABLE");
        ensurePaymentStatus("PAID");

        ReservationEntity reservation = createReservation(
            client, "IN_PROGRESS", LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
        createReservationDetail(reservation, tool, 1);
        createPayment(reservation, "PAID");
        flushAndClear();

        mockMvc.perform(post("/returns")
                .contentType("application/json")
                .content("""
                    {
                      "reservationId": %d,
                      "returnDate": "%s",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ]
                    }
                    """.formatted(reservation.getId(), LocalDate.now().toString(), tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isCreated());

        ReturnEntity created = returnJpaRepository.findAll().get(0);

        mockMvc.perform(put("/returns/{id}/confirm", created.getId())
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isOk());

        mockMvc.perform(put("/returns/{id}/receive", created.getId())
                .contentType("application/json")
                .content("""
                    {
                      "returnStatusName": "DAMAGED",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ],
                      "observations": "Damaged"
                    }
                    """.formatted(tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(supplierUser.getId(), "ROLE_SUPPLIER"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.returnStatusName").value("DAMAGED"));

        ReservationEntity updatedReservation = reservationJpaRepository.findById(reservation.getId()).orElseThrow();
        ToolEntity updatedTool = toolJpaRepository.findById(tool.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("IN_INCIDENT", updatedReservation.getReservationStatus().getStatusName());
        org.junit.jupiter.api.Assertions.assertEquals("UNAVAILABLE", updatedTool.getToolStatus().getName());
    }

    @Test
    void receiveWithoutSentReturnsBadRequest() throws Exception {
        UserEntity supplierUser = createUser("supplier_return4@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);
        ToolEntity tool = createTool(supplier, "AVAILABLE");

        UserEntity clientUser = createUser("client_return4@test.com");
        ClientEntity client = createClient(clientUser);

        ensureReservationStatus("IN_PROGRESS");
        ensureReturnStatus("PENDING");
        ensureReturnStatus("RECEIVED");

        ReservationEntity reservation = createReservation(
            client, "IN_PROGRESS", LocalDate.now(), LocalDate.now().plusDays(1));
        createReservationDetail(reservation, tool, 1);
        flushAndClear();

        mockMvc.perform(post("/returns")
                .contentType("application/json")
                .content("""
                    {
                      "reservationId": %d,
                      "returnDate": "%s",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ]
                    }
                    """.formatted(reservation.getId(), LocalDate.now().toString(), tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isCreated());

        ReturnEntity created = returnJpaRepository.findAll().get(0);

        mockMvc.perform(put("/returns/{id}/receive", created.getId())
                .contentType("application/json")
                .content("""
                    {
                      "returnStatusName": "RECEIVED",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ]
                    }
                    """.formatted(tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(supplierUser.getId(), "ROLE_SUPPLIER"))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createReturnFailsWhenQuantityExceedsReserved() throws Exception {
        UserEntity supplierUser = createUser("supplier_return5@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);
        ToolEntity tool = createTool(supplier, "AVAILABLE");

        UserEntity clientUser = createUser("client_return5@test.com");
        ClientEntity client = createClient(clientUser);

        ensureReservationStatus("IN_PROGRESS");
        ensureReturnStatus("PENDING");

        ReservationEntity reservation = createReservation(
            client, "IN_PROGRESS", LocalDate.now(), LocalDate.now().plusDays(1));
        createReservationDetail(reservation, tool, 1);
        flushAndClear();

        mockMvc.perform(post("/returns")
                .contentType("application/json")
                .content("""
                    {
                      "reservationId": %d,
                      "returnDate": "%s",
                      "details": [
                        { "toolId": %d, "quantity": 2 }
                      ]
                    }
                    """.formatted(reservation.getId(), LocalDate.now().toString(), tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void supplierCannotReceiveToolFromOtherSupplier() throws Exception {
        UserEntity supplierUser = createUser("supplier_return6@test.com");
        SupplierEntity supplier = createSupplier(supplierUser);

        UserEntity otherSupplierUser = createUser("supplier_return6b@test.com");
        SupplierEntity otherSupplier = createSupplier(otherSupplierUser);
        ToolEntity tool = createTool(otherSupplier, "AVAILABLE");

        UserEntity clientUser = createUser("client_return6@test.com");
        ClientEntity client = createClient(clientUser);

        ensureReservationStatus("IN_PROGRESS");
        ensureReturnStatus("PENDING");
        ensureReturnStatus("SENT");
        ensureReturnStatus("RECEIVED");

        ReservationEntity reservation = createReservation(
            client, "IN_PROGRESS", LocalDate.now(), LocalDate.now().plusDays(1));
        createReservationDetail(reservation, tool, 1);
        flushAndClear();

        mockMvc.perform(post("/returns")
                .contentType("application/json")
                .content("""
                    {
                      "reservationId": %d,
                      "returnDate": "%s",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ]
                    }
                    """.formatted(reservation.getId(), LocalDate.now().toString(), tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isCreated());

        ReturnEntity created = returnJpaRepository.findAll().get(0);

        mockMvc.perform(put("/returns/{id}/confirm", created.getId())
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(clientUser.getId(), "ROLE_CLIENT"))))
            .andExpect(status().isOk());

        mockMvc.perform(put("/returns/{id}/receive", created.getId())
                .contentType("application/json")
                .content("""
                    {
                      "returnStatusName": "RECEIVED",
                      "details": [
                        { "toolId": %d, "quantity": 1 }
                      ]
                    }
                    """.formatted(tool.getId()))
                .with(SecurityMockMvcRequestPostProcessors.authentication(auth(supplierUser.getId(), "ROLE_SUPPLIER"))))
            .andExpect(status().isBadRequest());
    }

    private UsernamePasswordAuthenticationToken auth(UUID userId, String role) {
        return new UsernamePasswordAuthenticationToken(
            userId,
            null,
            List.of(new SimpleGrantedAuthority(role))
        );
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
        client.setDocumentId(UUID.randomUUID().toString().substring(0, 10));
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

    private ToolEntity createTool(SupplierEntity supplier, String statusName) {
        CategoryEntity category = new CategoryEntity();
        category.setName("Test Category");
        category = categoryJpaRepository.save(category);

        ToolStatusEntity toolStatus = ensureToolStatus(statusName);

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

    private ReservationEntity createReservation(ClientEntity client, String statusName,
                                                LocalDate start, LocalDate end) {
        ReservationStatusEntity status = ensureReservationStatus(statusName);
        ReservationEntity reservation = new ReservationEntity();
        reservation.setClient(client);
        reservation.setReservationStatus(status);
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setTotalPrice(BigDecimal.valueOf(100));
        reservation.setCreatedAt(LocalDate.now());
        return reservationJpaRepository.save(reservation);
    }

    private ReservationDetailEntity createReservationDetail(ReservationEntity reservation, ToolEntity tool, int quantity) {
        ReservationDetailEntity detail = new ReservationDetailEntity();
        detail.setReservationId(reservation.getId());
        detail.setToolId(tool.getId());
        detail.setDailyPrice(BigDecimal.valueOf(10));
        detail.setRentalDay(2);
        detail.setQuantity(quantity);
        detail.setSubTotal(BigDecimal.valueOf(10)
            .multiply(BigDecimal.valueOf(2))
            .multiply(BigDecimal.valueOf(quantity)));
        return reservationDetailJpaRepository.save(detail);
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

    private ReturnStatusEntity ensureReturnStatus(String name) {
        return returnStatusJpaRepository.findByName(name)
            .orElseGet(() -> {
                ReturnStatusEntity status = new ReturnStatusEntity();
                status.setName(name);
                return returnStatusJpaRepository.save(status);
            });
    }

    private ToolStatusEntity ensureToolStatus(String name) {
        return toolStatusJpaRepository.findByName(name)
            .orElseGet(() -> {
                ToolStatusEntity status = new ToolStatusEntity();
                status.setName(name);
                return toolStatusJpaRepository.save(status);
            });
    }

    private PaymentStatusEntity ensurePaymentStatus(String name) {
        return paymentStatusJpaRepository.findByNameIgnoreCase(name)
            .orElseGet(() -> paymentStatusJpaRepository.save(new PaymentStatusEntity(null, name)));
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
