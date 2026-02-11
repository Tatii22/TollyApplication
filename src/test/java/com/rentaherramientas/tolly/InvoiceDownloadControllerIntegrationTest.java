package com.rentaherramientas.tolly;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.usecase.payment.PayPaymentUseCase;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.CategoryEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceEntity;
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
import com.rentaherramientas.tolly.infrastructure.persistence.repository.InvoiceJpaRepository;
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
class InvoiceDownloadControllerIntegrationTest {

  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;

  @Autowired private PayPaymentUseCase payPaymentUseCase;
  @Autowired private InvoiceJpaRepository invoiceJpaRepository;
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

  @BeforeEach
  void setUpMockMvc() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  void downloadInvoiceHtmlAsClient() throws Exception {
    InvoiceTestContext ctx = preparePaidInvoice();

    mockMvc.perform(get("/invoices/{id}/html", ctx.invoice.getId())
            .with(SecurityMockMvcRequestPostProcessors.authentication(auth(ctx.clientUser.getId(), "ROLE_CLIENT"))))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("text/html")));
  }

  @Test
  void downloadInvoicePdfAsClient() throws Exception {
    InvoiceTestContext ctx = preparePaidInvoice();

    mockMvc.perform(get("/invoices/{id}/pdf", ctx.invoice.getId())
            .with(SecurityMockMvcRequestPostProcessors.authentication(auth(ctx.clientUser.getId(), "ROLE_CLIENT"))))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString("application/pdf")))
        .andExpect(result -> assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(100));
  }

  private InvoiceTestContext preparePaidInvoice() {
    UserEntity supplierUser = createUser(uniqueEmail("supplier_pdf"));
    SupplierEntity supplier = createSupplier(supplierUser);
    ToolEntity tool = createTool(supplier);

    UserEntity clientUser = createUser(uniqueEmail("client_pdf"));
    ClientEntity client = createClient(clientUser);

    ReservationStatusEntity reserved = ensureReservationStatus("RESERVED");
    ensureReservationStatus("IN_PROGRESS");
    ensurePaymentStatus("PENDIENTE_DEVOLUCION");
    ensurePaymentStatus("PAID");

    ReservationEntity reservation = createReservation(client, reserved,
        LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
    createReservationDetail(reservation, tool);
    PaymentEntity payment = createPayment(reservation, "PENDIENTE_DEVOLUCION");
    flushAndClear();

    payPaymentUseCase.execute(reservation.getId(), clientUser.getId());
    InvoiceEntity invoice = invoiceJpaRepository.findByPayment_Id(payment.getId()).orElseThrow();

    InvoiceTestContext ctx = new InvoiceTestContext();
    ctx.invoice = invoice;
    ctx.clientUser = clientUser;
    ctx.supplierUser = supplierUser;
    return ctx;
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
    detail.setQuantity(1);
    detail.setSubTotal(BigDecimal.valueOf(20));
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

  private PaymentStatusEntity ensurePaymentStatus(String name) {
    return paymentStatusJpaRepository.findByNameIgnoreCase(name)
        .orElseGet(() -> paymentStatusJpaRepository.save(new PaymentStatusEntity(null, name)));
  }

  private void flushAndClear() {
    entityManager.flush();
    entityManager.clear();
  }

  private String uniqueEmail(String prefix) {
    return prefix + "_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
  }

  private static class InvoiceTestContext {
    private InvoiceEntity invoice;
    private UserEntity clientUser;
    private UserEntity supplierUser;
  }
}
