package com.gerencia_restaurante.application.port.in;

import com.gerencia_restaurante.domain.entity.Produto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CadastrarCardapio(
        @NotBlank(message = "Nome para cardápio é obrigatório")
        String nome,

        @NotBlank(message = "A descrição de cardápio é obrigatória")
        String descricao,

        Boolean statusDisponivel,

        List<Produto> produtos
) {}
