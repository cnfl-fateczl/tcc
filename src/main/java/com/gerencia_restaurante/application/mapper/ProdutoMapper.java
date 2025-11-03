package com.gerencia_restaurante.application.mapper;

import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.domain.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    // Converte Entity para DTO
    AtualizarProduto toAtualizarProduto(Produto produto);

    // Converte DTO para Entity (criação nova)
    @Mapping(target = "id", ignore = true)
    Produto toProdutoFromAtualizarProduto(AtualizarProduto atualizarProduto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true)
    void updateProdutoFromDto(AtualizarProduto atualizarProduto, @MappingTarget Produto produto);
}
