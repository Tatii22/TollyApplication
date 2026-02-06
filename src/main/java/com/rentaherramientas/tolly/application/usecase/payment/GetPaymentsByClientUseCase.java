package com.rentaherramientas.tolly.application.usecase.payment;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;

@Service
public class GetPaymentsByClientUseCase {

    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;

    public GetPaymentsByClientUseCase(PaymentRepository paymentRepository,
                                      ClientRepository clientRepository) {
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
    }

    public List<Payment> execute(Long clientId, UUID userId, boolean validateOwner) {
        if (clientId == null) {
            throw new DomainException("ClientId is required");
        }

        if (validateOwner) {
            if (userId == null) {
                throw new DomainException("UserId is required");
            }
            Client client = clientRepository.findByUserId(User.restore(userId))
                    .orElseThrow(() -> new DomainException("Client not found for user"));
            if (!clientId.equals(client.getId())) {
                throw new DomainException("Client does not match authenticated user");
            }
        }

        return paymentRepository.findByClientId(clientId);
    }
}
