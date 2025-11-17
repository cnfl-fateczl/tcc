package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

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
                    .uri(baseUrl + "/v1.0/orders/" + orderId)
                    .header("Authorization", "Bearer " + authService.getAccessToken())
                    .retrieve()
                    .onStatus(
                            status -> status.isSameCodeAs(HttpStatusCode.valueOf(404)),
                            response -> {
                                System.out.println("⚠ Pedido não encontrado no iFood: " + orderId);
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
     * ACKNOWLEDGE — Confirma imediatamente o recebimento do pedido PLACED
     */
    @Retry(name = "ifoodConfirm", fallbackMethod = "confirmFallback")
    public void acknowledgeOrder(String orderId) {

        String token = authService.getAccessToken();

        webClient.post()
                .uri(baseUrl + "/v1.0/orders/" + orderId + "/acknowledgment")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    /**
     * Fallback se o acknowledgment falhar todas as tentativas
     */
    public void confirmFallback(String orderId, Throwable e) {
        System.err.println("Falha ao confirmar pedido no iFood após retries: " + orderId);
        System.err.println("Motivo: " + e.getMessage());
    }
}
