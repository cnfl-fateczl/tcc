package com.gerencia_restaurante.domain.delivery;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class DeliveryAddress {
    private String streetName;
    private String streetNumber;
    private String neighborhood;
    private String complement;
    private String reference;
    private String postalCode;
    private String city;
    private String state;
    private String country;

    private Double latitude;
    private Double longitude;
}
