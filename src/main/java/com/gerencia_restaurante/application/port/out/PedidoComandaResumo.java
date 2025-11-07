package com.gerencia_restaurante.application.port.out;

public record PedidoComandaResumo
(
    Long id,
    Integer qtdItens,
    Double valorTotal
) {}