package com.gerencia_restaurante.adapters.api.inbound.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerencia_restaurante.adapters.api.inbound.webhook.dto.EventDto;
import com.gerencia_restaurante.application.delivery.DeliveryWebhookProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhooks/ifood")
@RequiredArgsConstructor
public class IfoodWebhookController {

    private final DeliveryWebhookProcessor processor;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody Map<String, Object> rawBody,
            @RequestHeader(value = "X-Ifood-Request-Id", required = false) String requestId
    ) {

        try {
            log.info("[WEBHOOK] Payload recebido: {}", rawBody);

            // campos principais
            String fullCode = (String) rawBody.get("fullCode");
            String code     = (String) rawBody.get("code");
            String orderId  = (String) rawBody.get("orderId");

            if (orderId == null) {
                log.warn("[WEBHOOK] Ignorado: body sem orderId.");
                return ResponseEntity.ok().build();
            }

            // normaliza evento → sempre dar preferência ao fullCode
            String event = fullCode != null ? fullCode : code;

            // ignorar keepalive
            if ("KEEPALIVE".equalsIgnoreCase(event) || "KPL".equalsIgnoreCase(code)) {
                log.info("[WEBHOOK] KEEPALIVE ignorado.");
                return ResponseEntity.ok().build();
            }

            log.info("[WEBHOOK] Evento final: orderId={} / status={}", orderId, event);

            // monta DTO para o Processor
            EventDto dto = new EventDto();
            dto.setOrderId(orderId);
            dto.setFullCode(event);

            // delega processamento
            processor.processEvent(dto);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("[WEBHOOK] Erro ao processar evento do iFood", e);
            return ResponseEntity.status(500).build();
        }
    }
}
