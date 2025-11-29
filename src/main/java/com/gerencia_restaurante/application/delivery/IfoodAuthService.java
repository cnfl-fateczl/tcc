package com.gerencia_restaurante.application.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IfoodAuthService {

    @Value("${ifood.client-id}")
    private String clientId;

    @Value("${ifood.client-secret}")
    private String clientSecret;

    @Value("${ifood.auth-url}")
    private String authUrl;

    private final WebClient webClient;  // ⭐ agora é injetado

    // ⭐ Cache em memória (simples e eficiente)
    private String cachedToken;
    private Instant expiresAt;

    public synchronized String getAccessToken() {
        // ⭐ Se já temos token válido, usamos ele
        if (cachedToken != null && expiresAt != null && Instant.now().isBefore(expiresAt.minusSeconds(30))) {
            return cachedToken;
        }

        // ⭐ Caso contrário, pedir novo token ao iFood
        try {
            TokenResponse response = webClient.post()
                    .uri(authUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body(BodyInserters.fromFormData("grantType", "client_credentials")
                            .with("clientId", clientId)
                            .with("clientSecret", clientSecret))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            resp -> resp.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException("iFood auth failed: " +
                                            resp.statusCode() + " - " + body)))
                    )
                    .bodyToMono(TokenResponse.class)
                    .block();

            // ⭐ Salva cache
            this.cachedToken = response.accessToken();
            this.expiresAt = Instant.now().plusSeconds(response.expiresIn());

            return cachedToken;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter token iFood: " + e.getMessage(), e);
        }
    }

    public record TokenResponse(String accessToken, String tokenType, int expiresIn) {}
}
