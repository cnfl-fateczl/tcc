package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IfoodOrderClient {

    @Value("${ifood.base-url}")
    private String baseUrl;

    private final IfoodAuthService authService;
    private final WebClient webClient; // melhor usar DI do Spring Boot

    /**
     * Busca os detalhes completos do pedido no iFood.
     */
    public IfoodOrderDetailsDto getOrder(String orderId) {
        try {

            return webClient.get()
                    .uri(baseUrl + "/order/v1.0/orders/" + orderId)
                    .header("Authorization", "Bearer " + authService.getAccessToken())
                    .retrieve()
                    .onStatus(
                            status -> status.isSameCodeAs(HttpStatusCode.valueOf(404)),
                            response -> {
                                System.out.println("Pedido não encontrado no iFood: " + orderId);
                                return Mono.empty(); // NÃO quebra
                            }
                    )
                    .bodyToMono(IfoodOrderDetailsDto.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar pedido no iFood: " + e.getMessage(), e);
        }
    }


    /**
     * ACKNOWLEDGE — Confirma o recebimento do EVENTO (não do pedido!)
     * Necessário enviar o eventId
     */
    @Retry(name = "ifoodConfirm", fallbackMethod = "confirmFallback")
    public void acknowledgeEvent(String eventId) {

        String token = authService.getAccessToken();

        WebClient client = webClient.mutate()
                .baseUrl(baseUrl)
                .build();

        // Body correto exigido pelo iFood:
        List<String> eventIds = List.of(eventId);
        List<Map<String, String>> body =
                eventIds.stream()
                                .map(id -> Map.of("id", id))
                                        .toList();

        log.info("Body enviado em ACK: {}", body);


        try {
            client.post()
                    .uri("/order/v1.0/events/acknowledgment")
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            log.info("ACK enviado com sucesso para eventId {}", eventId);

        } catch (Exception e) {
            log.error("Falha ao enviar ACK para eventId {}: {}", eventId, e.getMessage());
            throw e;
        }
    }

    /**
     * Fallback se o acknowledgment falhar todas as tentativas
     */
    public void confirmFallback(String orderId, Throwable e) {
        System.err.println("Falha ao confirmar pedido no iFood após retries: " + orderId);
        System.err.println("Motivo: " + e.getMessage());
    }
}
