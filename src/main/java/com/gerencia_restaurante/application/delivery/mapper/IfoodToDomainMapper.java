package com.gerencia_restaurante.application.delivery.mapper;

import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.domain.delivery.*;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IfoodToDomainMapper {

    public DeliveryOrder toDomain(IfoodOrderDetailsDto dto) {

        DeliveryOrder.DeliveryOrderBuilder builder = DeliveryOrder.builder();

        builder.id(dto.getId());
        builder.displayId(dto.getDisplayId());
        builder.orderType(dto.getOrderType());
        builder.orderTiming(dto.getOrderTiming());
        builder.salesChannel(dto.getSalesChannel());
        builder.testOrder(dto.isTest());
        builder.createdAt(dto.getCreatedAt());
        builder.preparationStartTime(dto.getPreparationStartDateTime());

        // customer
        if (dto.getCustomer() != null) {
            var c = DeliveryCustomer.builder()
                    .customerId(dto.getCustomer().getId())
                    .name(dto.getCustomer().getName())
                    .documentNumber(dto.getCustomer().getDocumentNumber())
                    .phoneNumber(
                            dto.getCustomer().getPhone() == null
                                    ? null
                                    : dto.getCustomer().getPhone().getNumber()
                    )
                    .build();
            builder.customer(c);
        }

        // address
        if (dto.getDelivery() != null && dto.getDelivery().getDeliveryAddress() != null) {
            var a = dto.getDelivery().getDeliveryAddress();
            var addr = DeliveryAddress.builder()
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
            builder.items(dto.getItems().stream().map(itemDto -> DeliveryItem.builder()
                    .externalProductId(itemDto.getExternalCode())
                    .name(itemDto.getName())
                    .quantity(itemDto.getQuantity() == null ? 0.0 : itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice() == null ? 0.0 : itemDto.getUnitPrice())
                    .totalPrice(itemDto.getTotalPrice() == null ? 0.0 : itemDto.getTotalPrice())
                    .build()
            ).collect(Collectors.toList()));
        }

        // status inicial
        builder.status(DeliveryOrderStatus.RECEIVED);

        return builder.build();
    }
}
