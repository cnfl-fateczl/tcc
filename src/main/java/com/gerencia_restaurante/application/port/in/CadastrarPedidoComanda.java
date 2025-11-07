package com.gerencia_restaurante.application.port.in;

import java.util.List;

public record CadastrarPedidoComanda
(
    List<CadastrarItemPedido> itens
) {}