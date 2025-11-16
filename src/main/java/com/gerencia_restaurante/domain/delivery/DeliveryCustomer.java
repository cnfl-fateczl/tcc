package com.gerencia_restaurante.domain.delivery;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class DeliveryCustomer {
    private String customerId;
    private String name;
    private String documentNumber;
    private String phoneNumber;
}
