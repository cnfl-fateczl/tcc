package com.gerencia_restaurante.application.port.out;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record PedidoComandaResumo
(
    Long id,
    LocalDate data,
    Integer mesa,
    Integer qtdItens,
    Double valorTotal
) {}