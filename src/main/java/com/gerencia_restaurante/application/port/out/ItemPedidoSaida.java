package com.gerencia_restaurante.application.port.out;

public record ItemPedidoSaida
(
    Integer numeroItem,
    Long idProduto,
    String nomeProduto,
    Double precoProduto,
    Integer quantidade,
    String observacao
) {}