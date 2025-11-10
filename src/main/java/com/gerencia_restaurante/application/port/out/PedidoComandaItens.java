package com.gerencia_restaurante.application.port.out;

import java.time.LocalDate;
import java.util.List;

public record PedidoComandaItens
(
    Long id,
    LocalDate data,
    Integer mesa,
    List<ItemPedidoSaida> itens
) {}