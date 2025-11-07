package com.gerencia_restaurante.application.port.out;

import java.util.List;

public record PedidoComandaItens
(
    Long id,
    List<ItemPedidoSaida> itens
) {}