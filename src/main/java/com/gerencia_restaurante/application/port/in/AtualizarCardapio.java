package com.gerencia_restaurante.application.port.in;
import com.gerencia_restaurante.domain.entity.Produto;
import java.util.HashSet;

public record AtualizarCardapio(
        Long id,
        String nome,
        String descricao,
        Boolean statusDisponivel,
        HashSet<Long> produtosIds
) {}
