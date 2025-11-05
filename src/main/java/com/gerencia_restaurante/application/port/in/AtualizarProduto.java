package com.gerencia_restaurante.application.port.in;

import jakarta.validation.constraints.DecimalMin;

public record AtualizarProduto(
        Long id,
        String nome,
        String categoria,
        String descricao,
        @DecimalMin(value = "0.01", message = "Preço não pode ser zerado")
        Double precoProduto
) {}
