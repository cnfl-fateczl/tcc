package com.gerencia_restaurante.domain.delivery.enums;

import java.util.EnumSet;
import java.util.Set;

public enum DeliveryOrderStatus {

    RECEIVED,        // pedido chegou
    CONFIRMED,       // aceito pelo restaurante
    IN_PREPARATION,  // cozinha preparando
    READY,           // pronto para retirar/entregar
    READY_TO_PICKUP,
    DISPATCHED,      // saiu para entrega
    DELIVERED,       // entregue
    CONCLUDED,
    CANCELLED;        // cancelado

    public Set<DeliveryOrderStatus> nextAllowed() {
        return switch (this) {
            case RECEIVED -> EnumSet.of(CONFIRMED, CANCELLED);
            case CONFIRMED -> EnumSet.of(IN_PREPARATION, CANCELLED);
            case IN_PREPARATION -> EnumSet.of(READY, CANCELLED);
            case READY, READY_TO_PICKUP -> EnumSet.of(DISPATCHED, CANCELLED);
            case DISPATCHED -> EnumSet.of(DELIVERED, CANCELLED);
            case DELIVERED, CANCELLED, CONCLUDED -> EnumSet.noneOf(DeliveryOrderStatus.class); // estado final
        };
    }

    public static DeliveryOrderStatus fromIfoodCode(String fullCode) {
        return switch (fullCode) {
            case "PLACED" -> RECEIVED;
            case "CONFIRMED" -> CONFIRMED;
            case "READY_TO_PICKUP" -> READY_TO_PICKUP;
            case "DISPATCHED" -> DISPATCHED;
            case "CONCLUDED" -> CONCLUDED;
            case "CANCELLED" -> CANCELLED;
            default -> null;
        };
    }
}
