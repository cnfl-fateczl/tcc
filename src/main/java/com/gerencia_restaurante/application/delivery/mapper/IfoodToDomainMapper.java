package com.gerencia_restaurante.application.delivery.mapper;
import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.domain.delivery.DeliveryAddress;
import com.gerencia_restaurante.domain.delivery.DeliveryCustomer;
import com.gerencia_restaurante.domain.delivery.DeliveryItem;
import com.gerencia_restaurante.domain.delivery.DeliveryOrder;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

public class IfoodToDomainMapper {

    public static DeliveryOrder toDeliveryOrder(IfoodOrderDetailsDto dto) {
        DeliveryOrder.DeliveryOrderBuilder builder = DeliveryOrder.builder();

        builder.id(dto.getId());
        builder.displayId(dto.getDisplayId());
        builder.orderType(dto.getOrderType());
        builder.orderTiming(dto.getOrderTiming());
        builder.salesChannel(dto.getSalesChannel());
        builder.testOrder(dto.isTest());
        builder.createdAt(dto.getCreatedAt());
        builder.preparationStartTime(dto.getPreparationStartDateTime());
        // map customer
        if (dto.getCustomer() != null) {
            DeliveryCustomer customer = DeliveryCustomer.builder()
                    .customerId(dto.getCustomer().getId())
                    .name(dto.getCustomer().getName())
                    .documentNumber(dto.getCustomer().getDocumentNumber())
                    .phoneNumber(dto.getCustomer().getPhone() == null ? null : dto.getCustomer().getPhone().getNumber())
                    .build();
            builder.customer(customer);
        }

        // map address
        if (dto.getDelivery() != null && dto.getDelivery().getDeliveryAddress() != null) {
            IfoodOrderDetailsDto.DeliveryDto.AddressDto a = dto.getDelivery().getDeliveryAddress();
            DeliveryAddress addr = DeliveryAddress.builder()
                    .streetName(a.getStreetName())
                    .streetNumber(a.getStreetNumber())
                    .neighborhood(a.getNeighborhood())
                    .complement(a.getComplement())
                    .reference(a.getReference())
                    .postalCode(a.getPostalCode())
                    .city(a.getCity())
                    .state(a.getState())
                    .country(a.getCountry())
                    .latitude(a.getCoordinates() == null ? null : a.getCoordinates().getLatitude())
                    .longitude(a.getCoordinates() == null ? null : a.getCoordinates().getLongitude())
                    .build();
            builder.deliveryAddress(addr);
        }

        // items
        if (dto.getItems() != null) {
            builder.items(dto.getItems().stream().map(itemDto -> {
                DeliveryItem item = DeliveryItem.builder()
                        .externalProductId(itemDto.getExternalCode())
                        .name(itemDto.getName())
                        .quantity(itemDto.getQuantity() == null ? 0.0 : itemDto.getQuantity())
                        .unitPrice(itemDto.getUnitPrice() == null ? 0.0 : itemDto.getUnitPrice())
                        .totalPrice(itemDto.getTotalPrice() == null ? 0.0 : itemDto.getTotalPrice())
                        .build();
                return item;
            }).collect(Collectors.toList()));
        }

        // default status on create
        builder.status(DeliveryOrderStatus.RECEIVED);

        return builder.build();
    }
}
