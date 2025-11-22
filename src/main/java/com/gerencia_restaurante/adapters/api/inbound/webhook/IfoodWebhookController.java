package com.gerencia_restaurante.adapters.api.inbound.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import com.gerencia_restaurante.application.delivery.DeliveryWebhookProcessor;
import com.gerencia_restaurante.application.delivery.IfoodOrderClient;
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
    private final IfoodOrderClient ifoodOrderClient;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody Map<String, Object> rawBody,
            @RequestHeader(value = "X-Ifood-Request-Id", required = false) String requestId
    ) {

        try {
            String fullCode = (String) rawBody.get("fullCode");
            log.info("rawBody: {}", rawBody);

            String code = (String) rawBody.get("code"); // PLC, CFM, CAN, etc

            // 🔹 Normalizar código (fullCode sempre preferido)
            String event = fullCode != null ? fullCode : code;

            // 1) KEEPALIVE – ignorar
            if ("KEEPALIVE".equals(event) || "KPL".equals(code)) {
                System.out.println("KEEPALIVE recebido.");
                return ResponseEntity.ok().build();
            }

            // 2) PLACED – fluxo completo (ACK → GET → SAVE)
            if ("PLACED".equals(event)) {

                String orderId = (String) rawBody.get("orderId");

                System.out.println("Evento PLACED recebido para pedido " + orderId);

                // 2.1) ACK IMEDIATO
                System.out.println("Enviando ACK...");
                ifoodOrderClient.acknowledgeEvent(orderId);
                System.out.println("ACK enviado.");

                // 2.2) Buscar detalhes do pedido
                System.out.println("Buscando detalhes...");
                IfoodOrderDetailsDto dto = ifoodOrderClient.getOrder(orderId);

                if (dto == null) {
                    System.err.println("Pedido não encontrado no GET. Pode ter sido cancelado antes.");
                    return ResponseEntity.ok().build();
                }

                // 2.3) Processar e salvar
                processor.processWebhookEvent(dto, requestId);
                System.out.println("Pedido salvo com sucesso.");

                return ResponseEntity.ok().build();
            }

            // 3) QUALQUER OUTRO EVENTO – status update
            System.out.println("Evento recebido: " + event);
            processor.processStatusEvent(rawBody);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao processar webhook iFood: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
