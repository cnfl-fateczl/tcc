package com.gerencia_restaurante.application.port.in;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarProduto (
    @NotBlank(message = "O nome do produto é obrigatório")
    String nome,

    @NotBlank(message = "A categoria do produto é obrigatória")
    String categoria,

    @NotBlank(message = "Descrição do produto é obrigatória")
    String descricao,

    @NotNull(message = "Preço mínimo obrigatório")
    @DecimalMin(value = "0.01", message = "Preço não pode ser zerado")
    Double precoProduto,
    
    String codigoIfood
    ){}
