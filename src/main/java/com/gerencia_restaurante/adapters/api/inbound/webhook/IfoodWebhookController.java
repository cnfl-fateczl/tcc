package com.gerencia_restaurante.adapters.api.inbound.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.application.delivery.DeliveryWebhookProcessor;
import com.gerencia_restaurante.application.delivery.IfoodOrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhooks/ifood")
@RequiredArgsConstructor
public class IfoodWebhookController {

    private final DeliveryWebhookProcessor processor;
    private final ObjectMapper objectMapper;
    private final IfoodOrderClient ifoodOrderClient;

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody Map<String, Object> rawBody,
            @RequestHeader(value = "X-Ifood-Request-Id", required = false) String requestId) {

        try {
            // 1) KEEPALIVE
            if ("KEEPALIVE".equals(rawBody.get("code"))) {
                System.out.println("➡ KEEPALIVE recebido");
                return ResponseEntity.ok().build();
            }

            // 2) PLACED (ou PLC)
            String code = rawBody.get("code") != null ? rawBody.get("code").toString() : null;
            String fullCode = rawBody.get("fullCode") != null ? rawBody.get("fullCode").toString() : null;

            boolean isPlaced = "PLACED".equalsIgnoreCase(fullCode) || "PLC".equalsIgnoreCase(code);

            if (isPlaced) {

                String orderId = (String) rawBody.get("orderId");

                if (orderId == null) {
                    System.out.println("Recebido PLACED, mas sem orderId!");
                    return ResponseEntity.badRequest().build();
                }

                System.out.println("Evento PLACED recebido. Buscando pedido completo no iFood… id=" + orderId);

                // 3) Tentativa de busca do pedido completo com retry e fallback
                IfoodOrderDetailsDto dto = null;

                try {
                    dto = ifoodOrderClient.getOrder(orderId);

                } catch (Exception e) {
                    System.out.println("Não foi possível buscar o pedido (possível 404 do simulador)");
                }

                if (dto == null) {
                    System.out.println("Ifood não disponibilizou o pedido completo ainda. Ignorando PLACED.");
                    return ResponseEntity.ok().build(); // nunca retorna erro ao iFood
                }

                // 4) Agora sim, processa
                processor.processWebhookEvent(dto, requestId);
                return ResponseEntity.ok().build();
            }

            // 5) Caso raro: payload completo
            try {
                IfoodOrderDetailsDto dto = objectMapper.convertValue(rawBody, IfoodOrderDetailsDto.class);
                processor.processWebhookEvent(dto, requestId);
                return ResponseEntity.ok().build();
            } catch (Exception ignored) {
                System.out.println("Payload não era um pedido completo. Evento ignorado.");
                return ResponseEntity.ok().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
