package com.gerencia_restaurante.application.port.in;

import com.gerencia_restaurante.domain.entity.Produto;

import java.util.List;

public record AtualizarCardapio(
        Long id,
        String nome,
        String descricao,
        Boolean statusDisponivel,
        List<Produto> produtos
) {}
