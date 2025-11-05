package com.gerencia_restaurante.application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.gerencia_restaurante.application.port.in.AtualizarProduto;
import com.gerencia_restaurante.application.port.in.CadastrarProduto;
import com.gerencia_restaurante.domain.entity.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    // Converte Entity para DTO
    AtualizarProduto toAtualizarProduto(Produto produto);

    // Converte DTO para Entity (criação nova)
    @Mapping(target = "id", ignore = true)
    Produto toProdutoFromCadastrarProduto(CadastrarProduto cadastrarProduto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProdutoFromDto(AtualizarProduto atualizarProduto, @MappingTarget Produto produto);
}
