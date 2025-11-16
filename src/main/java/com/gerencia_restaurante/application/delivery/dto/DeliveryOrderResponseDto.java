package com.gerencia_restaurante.application.delivery.dto;

import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class DeliveryOrderResponseDto {

    private String id;
    private String displayId;
    private String orderType;
    private String orderTiming;
    private String salesChannel;
    private boolean testOrder;

    private OffsetDateTime createdAt;
    private OffsetDateTime preparationStartTime;

    private DeliveryOrderStatus status;

    private CustomerDto customer;
    private AddressDto deliveryAddress;

    private List<ItemDto> items;

    private List<StatusHistoryDto> statusHistory;

    @Data
    public static class CustomerDto {
        private String customerId;
        private String name;
        private String documentNumber;
        private String phoneNumber;
    }

    @Data
    public static class AddressDto {
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

    @Data
    public static class ItemDto {
        private String externalProductId;
        private String name;
        private double quantity;
        private double unitPrice;
        private double totalPrice;
        private Long produtoId; // opcional
    }

    @Data
    public static class StatusHistoryDto {
        private DeliveryOrderStatus status;
        private OffsetDateTime changedAt;
    }
}
