package com.gerencia_restaurante.application.port.in;

public record CadastrarItemPedido
(
    Long produtoId,
    Integer quantidade,
    String observacao
) {}