package com.gerencia_restaurante.adapters.api.outbound.ifood.dto;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class IfoodOrderDetailsDto {

    private String id;
    private String displayId;
    private String orderType;         // enum no iFood: DELIVERY / TAKEOUT / DINE_IN etc.
    private String orderTiming;       // IMMEDIATE / SCHEDULED
    private String salesChannel;      // IFOOD, POS, etc.
    private String category;           // FOOD, GROCERY, etc.
    private OffsetDateTime createdAt;
    private OffsetDateTime preparationStartDateTime;
    private boolean isTest;
    private String extraInfo;

    private MerchantDto merchant;
    private CustomerDto customer;
    private DeliveryDto delivery;
    private List<ItemDto> items;

    @Data
    public static class MerchantDto {
        private String id;
        private String name;
    }

    @Data
    public static class CustomerDto {
        private String id;
        private String name;
        private String documentNumber;
        private PhoneDto phone;

        @Data
        public static class PhoneDto {
            private String number;
            private String localizer;
            private OffsetDateTime localizerExpiration;
        }
    }

    @Data
    public static class DeliveryDto {
        private AddressDto deliveryAddress;

        @Data
        public static class AddressDto {
            private String streetName;
            private String streetNumber;
            private String formattedAddress;
            private String neighborhood;
            private String complement;
            private String reference;
            private String postalCode;
            private String city;
            private String state;
            private String country;
            private CoordinatesDto coordinates;

            @Data
            public static class CoordinatesDto {
                private Double latitude;
                private Double longitude;
            }
        }
    }

    @Data
    public static class ItemDto {
        private String id;            // id do item no pedido (se houver)
        private String externalCode;  // ou código do SKU / item
        private String name;
        private Double quantity;
        private Double unitPrice;
        private Double totalPrice;
        private List<OptionDto> options;

        @Data
        public static class OptionDto {
            private String id;
            private String name;
            private String externalCode;
            private Double quantity;
            private Double unitPrice;
            private Double price;
            private List<CustomizationDto> customizations;

            @Data
            public static class CustomizationDto {
                private String id;
                private String name;
                private String groupName;
                private String externalCode;
                private String type;
                private Double quantity;
                private Double unitPrice;
                private Double addition;
                private Double price;
            }
        }
    }
}

