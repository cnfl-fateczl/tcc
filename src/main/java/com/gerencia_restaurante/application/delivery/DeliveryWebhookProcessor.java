package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.inbound.webhook.dto.EventDto;
import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.domain.delivery.enums.DeliveryOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryWebhookProcessor {

    private final DeliveryOrderService orderService;
    private final IfoodOrderClient ifoodOrderClient;

    /**
     * Processa qualquer evento recebido do webhook do iFood.
     */
    public void processEvent(EventDto event) {

        String rawStatus = event.getFullCode();
        String orderId = event.getOrderId();

        log.info("[IFOOD] Webhook recebido → orderId={}, status={}", orderId, rawStatus);

        // Normaliza o código do iFood para enum interno
        DeliveryOrderStatus normalized = DeliveryOrderStatus.fromIfoodCode(rawStatus);

        if (normalized == null) {
            log.warn("[IFOOD] Status desconhecido recebido: {}", rawStatus);
            return;
        }

        // ---------------------------------------------------------
        // CASO 1 — PLACED → precisa buscar detalhes completos via GET
        // ---------------------------------------------------------
        if (normalized == DeliveryOrderStatus.RECEIVED) {

            log.info("[IFOOD] Status PLACED detectado → Buscando detalhes do pedido...");

            IfoodOrderDetailsDto details = ifoodOrderClient.getOrder(orderId);

            if (details == null) {
                log.error("[IFOOD] PLACED recebido, mas o GET retornou null. Pedido pode ter sido cancelado antes.");
                return;
            }

            // salvar ou atualizar pedido completo
            orderService.createOrUpdateFromIfood(details, rawStatus, normalized);
            return;
        }

        // ---------------------------------------------------------
        // CASO 2 — QUALQUER OUTRO EVENTO → apenas atualização de status
        // ---------------------------------------------------------
        log.info("[IFOOD] Atualizando status do pedido {} para {}", orderId, normalized);
        orderService.updateStatus(orderId, rawStatus, normalized);
    }
}
