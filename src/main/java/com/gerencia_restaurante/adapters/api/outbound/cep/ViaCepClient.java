package com.gerencia_restaurante.adapters.api.outbound.cep;

import com.gerencia_restaurante.adapters.api.outbound.cep.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViaCepClient {

    private final WebClient webClient;

    public ViaCepResponse consultarCep(String cep) {

        try {
            return webClient
                    .get()
                    .uri("https://viacep.com.br/ws/" + cep + "/json/")
                    .retrieve()
                    .bodyToMono(ViaCepResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Erro ao consultar CEP {}: {}", cep, e.getMessage());
            throw new RuntimeException("Falha ao consultar o CEP");
        }
    }
}
