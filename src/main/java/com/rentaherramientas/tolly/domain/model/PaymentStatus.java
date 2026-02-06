package com.rentaherramientas.tolly.domain.model;

import java.util.Objects;

public class PaymentStatus {

    public static final String PENDING = "PENDING";
    public static final String PAID = "PAID";
    public static final String CANCELLED = "CANCELLED";

    private final Long id;
    private final String name;

    public PaymentStatus(Long id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("PaymentStatus name cannot be null or blank");
        }
        this.id = id;
        this.name = name.trim().toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPaid() {
        return PAID.equalsIgnoreCase(name);
    }

    public boolean isPending() {
        return PENDING.equalsIgnoreCase(name);
    }

    public boolean isCancelled() {
        return CANCELLED.equalsIgnoreCase(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentStatus)) return false;
        PaymentStatus that = (PaymentStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
