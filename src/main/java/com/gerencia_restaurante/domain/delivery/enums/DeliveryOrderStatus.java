package com.gerencia_restaurante.domain.delivery.enums;

import java.util.EnumSet;
import java.util.Set;

public enum DeliveryOrderStatus {

    RECEIVED,           // pedido chegou (PLACED)
    CONFIRMED,          // aceito pelo restaurante
    IN_PREPARATION,     // cozinha preparando (PREPARATION_STARTED / IN_PREPARATION)
    READY_TO_PICKUP,    // pronto para retirada
    DISPATCHED,         // saiu para entrega
    DELIVERED,          // entregue
    CONCLUDED,          // finalizado
    CANCELLED;          // cancelado (padrão iFood)

    /**
     * Fluxo de status permitido dentro do restaurante.
     * O iFood às vezes envia eventos fora de ordem → validamos aqui.
     */
    public Set<DeliveryOrderStatus> nextAllowed() {
        return switch (this) {

            case RECEIVED -> EnumSet.of(CONFIRMED, CANCELLED);

            case CONFIRMED -> EnumSet.of(IN_PREPARATION, CANCELLED);

            case IN_PREPARATION -> EnumSet.of(READY_TO_PICKUP, CANCELLED);

            case READY_TO_PICKUP -> EnumSet.of(DISPATCHED, CANCELLED);

            case DISPATCHED -> EnumSet.of(DELIVERED, CANCELLED);

            case DELIVERED, CONCLUDED, CANCELLED -> EnumSet.noneOf(DeliveryOrderStatus.class);
        };
    }


    /**
     * Mapeamento dos códigos oficiais do iFood → domínio interno.
     * Aqui ficam SOMENTE os status que mudam a linha do tempo do pedido.
     */
    public static DeliveryOrderStatus fromIfoodCode(String code) {
        if (code == null) return null;

        switch (code) {

            // Pedido chegou
            case "PLACED":
                return RECEIVED;

            // Pedido aceito pela loja
            case "CONFIRMED":
                return CONFIRMED;

            // Cozinha começou a preparar
            case "PREPARATION_STARTED":
            case "IN_PREPARATION":
            case "ORDER_PREPARATION_STARTED":
                return IN_PREPARATION;

            // Pedido pronto para retirada
            case "READY_TO_PICKUP":
            case "READY_FOR_PICKUP":
            case "ORDER_PREPARATION_READY":
                return READY_TO_PICKUP;

            // Saiu para entrega
            case "DISPATCHED":
            case "ORDER_DISPATCHED":
                return DISPATCHED;

            // Entregado com sucesso
            case "DELIVERED":
            case "DELIVERY_COMPLETED":
                return DELIVERED;

            // Finalizado no app
            case "CONCLUDED":
                return CONCLUDED;

            // Cancelamentos (iFood mistura CANCELLED/CANCELED)
            case "CANCELLED":
            case "CANCELED":
                return CANCELLED;

            // EVENTOS QUE NÃO MUDAM O STATUS DO PEDIDO
            case "DELIVERY_DROP_CODE_REQUESTED":
                return null; // ignorar → não muda status

            default:
                return null;
        }
    }
}
