package com.gerencia_restaurante.application.delivery;

import com.gerencia_restaurante.adapters.api.outbound.ifood.dto.IfoodOrderDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final WebClient webClient = WebClient.builder().build();

    public IfoodOrderDetailsDto getOrder(String orderId) {
        try {
            return webClient.get()
                    .uri(baseUrl + "/orders/" + orderId)
                    .header("Authorization", "Bearer " + authService.getAccessToken())
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, resp -> {
                        System.out.println("Pedido não encontrado no iFood: " + orderId);
                        return Mono.empty(); // devolve vazio, não é erro
                    })
                    .bodyToMono(IfoodOrderDetailsDto.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar pedido iFood: " + e.getMessage(), e);
        }
    }

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

    // Fallback automático se todas as tentativas falharem
    public void confirmFallback(String orderId, Throwable e) {
        System.err.println(" Falha ao confirmar pedido no iFood após retries: " + orderId);
        System.err.println("Motivo: " + e.getMessage());

        // Aqui temos 3 opções (todas válidas):

        // OPTION A (V1): só logar

        // OPTION B (V2): salvar pedido para reprocessar depois
        // retryQueue.save(orderId);

        // OPTION C (SÊNIOR): enviar para fila (Kafka / RabbitMQ)
    }
}
