package com.gerencia_restaurante.application.port.in;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AtualizarProduto(
        Long id,

        @NotBlank(message = "Nome do Produto é Obrigatório")
        String nome,

        @NotBlank(message = "Categoria do Produto é Obrigatória")
        String categoria,

        @NotBlank(message = "Descrição do produto é obrigatória")
        String descricao,

        @DecimalMin(value = "0.01", message = "Preço não pode ser zerado")
        Double precoProduto
        ) {}
