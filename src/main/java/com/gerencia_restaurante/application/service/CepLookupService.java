package com.gerencia_restaurante.application.service;

import com.gerencia_restaurante.adapters.api.outbound.cep.ViaCepClient;
import com.gerencia_restaurante.adapters.api.outbound.cep.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepLookupService {

    private final ViaCepClient viaCepClient;

    public String montarEndereco(String cep) {

        ViaCepResponse r = viaCepClient.consultarCep(cep);

        if (r == null || r.getCep() == null) {
            throw new RuntimeException("CEP inválido ou não encontrado.");
        }

        return String.format(
                "%s, %s - %s, %s/%s, CEP:%s",
                r.getLogradouro(),
                r.getComplemento() == null ? "" : r.getComplemento(),
                r.getBairro(),
                r.getLocalidade(),
                r.getUf(),
                r.getCep()
        );
    }
}
