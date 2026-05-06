package com.gerencia_restaurante.application.port.out;

import java.util.List;

public record ProdutoSaida
(
    String nome,
    String categoria,
    String descricao,
    Float precoProduto,
    String codigoIfood,
    List<IngredienteSaida> ingredientes
) {}
