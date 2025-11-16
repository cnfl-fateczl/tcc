package com.gerencia_restaurante.domain.delivery.enums;

import java.util.EnumSet;
import java.util.Set;

public enum DeliveryOrderStatus {

    RECEIVED,        // pedido chegou
    CONFIRMED,       // aceito pelo restaurante
    IN_PREPARATION,  // cozinha preparando
    READY,           // pronto para retirar/entregar
    DISPATCHED,      // saiu para entrega
    DELIVERED,       // entregue
    CANCELED;        // cancelado

    public Set<DeliveryOrderStatus> nextAllowed() {
        return switch (this) {
            case RECEIVED -> EnumSet.of(CONFIRMED, CANCELED);
            case CONFIRMED -> EnumSet.of(IN_PREPARATION, CANCELED);
            case IN_PREPARATION -> EnumSet.of(READY, CANCELED);
            case READY -> EnumSet.of(DISPATCHED, CANCELED);
            case DISPATCHED -> EnumSet.of(DELIVERED, CANCELED);
            case DELIVERED, CANCELED -> EnumSet.noneOf(DeliveryOrderStatus.class); // estado final
        };
    }
}
