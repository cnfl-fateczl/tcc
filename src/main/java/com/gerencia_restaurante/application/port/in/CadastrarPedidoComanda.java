package com.gerencia_restaurante.application.port.in;

import java.util.List;

public record CadastrarPedidoComanda
(
    Integer mesa,    
    List<CadastrarItemPedido> itens
) {}